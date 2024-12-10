package error.pirate.backend.quotation.command.application.service;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.quotation.command.application.dto.CreateQuotationRequest;
import error.pirate.backend.quotation.command.application.dto.QuotationItemDTO;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuotationCommandService {

    private final QuotationQueryService quotationQueryService;
    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final EntityManager entityManager;

    @Transactional
    public void createQuotation(CreateQuotationRequest request) {

        // 엔티티 요구 변수 작성
        Client client = entityManager.getReference(Client.class, request.getClientSeq());
        User user = entityManager.getReference(User.class, request.getUserSeq());
        String quotationName = quotationQueryService.makeQuotationName();

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
}
