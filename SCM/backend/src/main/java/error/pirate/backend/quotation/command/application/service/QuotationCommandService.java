package error.pirate.backend.quotation.command.application.service;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.quotation.command.application.dto.CreateQuotationRequest;
import error.pirate.backend.quotation.command.application.dto.QuotationItemDTO;
import error.pirate.backend.quotation.command.application.dto.QuotationItemUpdateDTO;
import error.pirate.backend.quotation.command.application.dto.UpdateQuotationRequest;
import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationItem;
import error.pirate.backend.quotation.command.domain.repository.QuotationItemRepository;
import error.pirate.backend.quotation.command.domain.repository.QuotationRepository;
import error.pirate.backend.quotation.command.domain.aggregate.entity.Quotation;
import error.pirate.backend.quotation.query.service.QuotationQueryService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class QuotationCommandService {

    private final QuotationQueryService quotationQueryService;
    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final EntityManager entityManager;
    private final NameGenerator nameGenerator;

    // 견적서 등록
    @Transactional
    public void createQuotation(CreateQuotationRequest request) {

        // 엔티티 요구 변수 작성
        Client client = entityManager.getReference(Client.class, request.getClientSeq());
        User user = entityManager.getReference(User.class, request.getUserSeq());
        String quotationName = nameGenerator.nameGenerator(Quotation.class);

        // 견적서 합계 계산
        int quotationExtendedPrice = 0;
        int quotationTotalQuantity = 0;

        for (QuotationItemDTO quotationItemDTO : request.getQuotationItem()) {
            quotationExtendedPrice +=
                    quotationItemDTO.getQuotationItemQuantity() * quotationItemDTO.getQuotationItemPrice();
            quotationTotalQuantity +=
                    quotationItemDTO.getQuotationItemPrice();
        }

        // 견적서 등록
        Quotation quotation = new Quotation(
                user, client, quotationName, request.getQuotationQuotationDate(),
                quotationExtendedPrice, quotationTotalQuantity, request.getQuotationNote());

        Long quotationSeq = quotationRepository.save(quotation).getQuotationSeq();

        // 견적서 품목 등록
        for (QuotationItemDTO quotationItemDTO : request.getQuotationItem()) {
            QuotationItem quotationItem = new QuotationItem(
                    entityManager.getReference(Quotation.class, quotationSeq),
                    entityManager.getReference(Item.class, quotationItemDTO.getItemSeq()),
                    quotationItemDTO.getQuotationItemQuantity(),
                    quotationItemDTO.getQuotationItemPrice(),
                    quotationItemDTO.getQuotationItemNote());
            quotationItemRepository.save(quotationItem);
        }
    }

    // 견적서 수정
    @Transactional
    public void updateQuotation(Long quotationSeq, UpdateQuotationRequest request) {
        Quotation quotation = quotationRepository.findById(quotationSeq).orElseThrow();

        // 엔티티 요구 변수 작성
        Client client = request.getClientSeq() == null ? null :
                entityManager.getReference(Client.class, request.getClientSeq());
        User user = request.getUserSeq() == null ? null :
                entityManager.getReference(User.class, request.getUserSeq());

        // 기존 견적서 품목 삭제
        ArrayList<QuotationItem> quotationItemList = quotationItemRepository.findByQuotationQuotationSeq(quotationSeq);

        for (QuotationItem quotationItem : quotationItemList) {
            quotationItemRepository.delete(quotationItem);
        }

        // 합계 계산용 변수
        int quotationExtendedPrice = 0;
        int quotationTotalQuantity = 0;

        // 견적서 품목 등록
        for (QuotationItemUpdateDTO quotationItemUpdateDTO : request.getQuotationItemUpdateList()) {
            // 견적서 합계 계산
            quotationExtendedPrice +=
                    quotationItemUpdateDTO.getQuotationItemQuantity() * quotationItemUpdateDTO.getQuotationItemPrice();
            quotationTotalQuantity +=
                    quotationItemUpdateDTO.getQuotationItemPrice();

            // 견적서 품목 등록
            QuotationItem quotationItem = new QuotationItem(
                    entityManager.getReference(Quotation.class, quotationSeq),
                    entityManager.getReference(Item.class, quotationItemUpdateDTO.getItemSeq()),
                    quotationItemUpdateDTO.getQuotationItemQuantity(),
                    quotationItemUpdateDTO.getQuotationItemPrice(),
                    quotationItemUpdateDTO.getQuotationItemNote());
            quotationItemRepository.save(quotationItem);
        }

        // 견적서 변경 사항 적용
        quotation.updateQuotation(
                request.getQuotationQuotationDate(), client, user, request.getQuotationNote(),
                quotationExtendedPrice, quotationTotalQuantity);
    }
}
