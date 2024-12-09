package error.pirate.backend.productionReceiving.command.application.service;

import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductionReceivingServiceTest {

    @Autowired
    ProductionReceivingService productionReceivingService;

    @Test
    void createProductionReceiving() {
        ProductionReceivingCreateRequest productionReceivingCreateRequest = new ProductionReceivingCreateRequest();
        productionReceivingCreateRequest.setProductionWarehouseSeq(1L);
        productionReceivingCreateRequest.setStoreWarehouseSeq(11L);
        productionReceivingCreateRequest.setUserSeq(1L);
        productionReceivingCreateRequest.setWorkOrderSeq(1L);
        productionReceivingCreateRequest.setProductionReceivingName("2024/12/08-1");
        productionReceivingCreateRequest.setProductionReceivingExtendedPrice(1000000);
        productionReceivingCreateRequest.setProductionReceivingNote("생산입고 비고 입력 테스트");
        productionReceivingCreateRequest.setProductionReceivingItemList(getProductionReceivingItemList());

        assertDoesNotThrow(() -> productionReceivingService.createProductionReceiving(productionReceivingCreateRequest));
    }

    private static List<ProductionReceivingItemDTO> getProductionReceivingItemList() {
        ProductionReceivingItemDTO productionReceivingItemDTO_1 = new ProductionReceivingItemDTO();
        productionReceivingItemDTO_1.setItemSeq(1L);
        productionReceivingItemDTO_1.setProductionReceivingItemQuantity(100);
        productionReceivingItemDTO_1.setProductionReceivingUnitPrice(5000);

        ProductionReceivingItemDTO productionReceivingItemDTO_2 = new ProductionReceivingItemDTO();
        productionReceivingItemDTO_2.setItemSeq(2L);
        productionReceivingItemDTO_2.setProductionReceivingItemQuantity(100);
        productionReceivingItemDTO_2.setProductionReceivingUnitPrice(5000);

        List<ProductionReceivingItemDTO> productionReceivingItemList = new ArrayList<>();
        productionReceivingItemList.add(productionReceivingItemDTO_1);
        productionReceivingItemList.add(productionReceivingItemDTO_2);

        return productionReceivingItemList;
    }
}