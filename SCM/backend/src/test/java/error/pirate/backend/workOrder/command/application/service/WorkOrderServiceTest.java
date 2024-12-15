package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Transactional
class WorkOrderServiceTest {

    @MockitoBean
    private ItemInventoryDomainService itemInventoryDomainService;  // 재고가 충분하다고 가정하기 위해 mock 으로 설정

    @Autowired
    private WorkOrderService workOrderService;

    @Test
    @DisplayName("작업지시서 생성 테스트")
    void createWorkOrderForItemTest() {
        doNothing().when(itemInventoryDomainService).checkInventoryForBomItems(any(), anyInt());

        // given
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setSalesOrderSeq(1L);
        request.setSalesOrderItemSeq(1L);
        request.setUserSeq(1L);
        request.setWarehouseSeq(1L);
        request.setWorkOrderName("2024/12/13 - 1");
        request.setWorkOrderIndicatedDate(LocalDate.now());
        request.setWorkOrderDueDate(LocalDate.now().plusDays(7));
        request.setWorkOrderIndicatedQuantity(100);
        request.setWorkOrderNote("테스트 작업지시서");

        // when & then
        assertDoesNotThrow(() -> workOrderService.createWorkOrderForItem(request));
    }

}