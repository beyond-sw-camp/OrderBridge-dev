package error.pirate.backend.productionReceiving.command.application.service;

import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingItemDTO;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingUpdateRequest;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    private static Stream<Arguments> updateProductionReceivingParam() {
        List<ProductionReceivingItemDTO> itemArguments = new ArrayList<>();

        itemArguments.add(new ProductionReceivingItemDTO(1L, 100, 10000, "만원짜리 감자"));
        itemArguments.add(new ProductionReceivingItemDTO(2L, 50, 10000, "만원짜리 음식1"));
        itemArguments.add(new ProductionReceivingItemDTO(3L, 50, 10000, "만원짜리 음식2"));

        return Stream.of(
                arguments(21L, 2L, 12L, "2024/12/08-1-수정", 2000000, "수정한 생산입고", null, itemArguments),
                arguments(2L, null, null, null, null, "수정한 생산입고", null, null),
                arguments(3L, null, null, "2024/12/08-1-수정", null, null, null, itemArguments),
                arguments(4L, 2L, 12L, "2024/12/08-1-수정", 2000000, "수정한 생산입고", null, null),
                arguments(4L, null, null, null, null, null, ProductionReceivingStatus.AFTER, null)
        );
    }

    @DisplayName("생산입고 수정")
    @ParameterizedTest()
    @MethodSource("updateProductionReceivingParam")
    void updateProductionReceiving(Long productionReceivingSeq,
                                     Long productionWarehouseSeq, Long storeWarehouseSeq,
                                     String productionReceivingName, Integer productionReceivingExtendedPrice,
                                     String productionReceivingNote, ProductionReceivingStatus productionReceivingStatus,
                                   List<ProductionReceivingItemDTO> productionReceivingItemList) {

        ProductionReceivingUpdateRequest request = new ProductionReceivingUpdateRequest(
                productionWarehouseSeq,
                storeWarehouseSeq,
                productionReceivingName,
                productionReceivingExtendedPrice,
                productionReceivingStatus,
                productionReceivingNote,
                productionReceivingItemList
        );

        assertDoesNotThrow(() -> productionReceivingService.updateProductionReceiving(productionReceivingSeq, request));
    }

    private static Stream<Arguments> deleteProductionReceivingParam() {

        return Stream.of(
                arguments(21L),
                arguments(25L)
        );
    }

    @DisplayName("생산입고 삭제")
    @ParameterizedTest()
    @MethodSource("deleteProductionReceivingParam")
    void deleteProductionReceiving(Long productionReceivingSeq) {

        assertDoesNotThrow(() -> productionReceivingService.deleteProductionReceiving(productionReceivingSeq));
    }
}