package error.pirate.backend.purchase.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.purchase.command.application.dto.PurchaseUpdateRequest;
import error.pirate.backend.purchase.command.domain.aggregate.entity.Purchase;
import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseStatus;
import error.pirate.backend.purchase.command.domain.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PurchaseDomainService {

    private final PurchaseRepository purchaseRepository;

    public Purchase findById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));
    }

    public Purchase createPurchase(Purchase purchase) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        int purchaseCount = (int) purchaseRepository.count();

        // 구매서 이름 생성
        String purchaseName = today.format(formatter) + " - "  + (purchaseCount+1);
        purchase.changePurchaseName(purchaseName);

        return purchaseRepository.save(purchase);
    }

    public void updatePurchase(PurchaseUpdateRequest request) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));

        if(purchase.getPurchaseStatus() == PurchaseStatus.COMPLETE) {
            throw new CustomException(ErrorCodeType.PURCHASE_UPDATE_ERROR);
        }

        purchase.updatePurchase(request);
    }


}
