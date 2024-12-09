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

@Service
@RequiredArgsConstructor
public class QuotationCommandService {

    private final QuotationQueryService quotationQueryService;
    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final EntityManager entityManager;

    @Transactional
    public void createQuotation(CreateQuotationRequest request) {

        // 견적서 등록
        Client client = entityManager.getReference(Client.class, request.getClientSeq());
        User user = entityManager.getReference(User.class, request.getUserSeq());
        String quotationName = quotationQueryService.makeQuotationName();

        Quotation quotation = new Quotation(
                user, client, quotationName, request.getQuotationQuotationDate(), request.getQuotationNote());

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

        // 견적서 합계 계산
        quotation.itemCalculate(quotationQueryService.calculateSum(quotationSeq));
        quotationRepository.save(quotation);
    }
}
