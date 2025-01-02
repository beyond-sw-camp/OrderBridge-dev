package error.pirate.backend.workOrder.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.application.dto.UpdateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
//        request.setUserSeq(1L);
        request.setWarehouseSeq(1L);
//        request.setWorkOrderName("2024/12/13 - 1");
        request.setWorkOrderIndicatedDate(LocalDateTime.now());
        request.setWorkOrderDueDate(LocalDateTime.now().plusDays(7));
//        request.setWorkOrderIndicatedQuantity(100);
        request.setWorkOrderNote("테스트 작업지시서");
        String userNo = "2";

        // when & then
        assertDoesNotThrow(() -> workOrderService.createWorkOrderForItem(request, userNo));
    }

    private static List<UpdateWorkOrderRequest> getUpdateWorkOrderRequest() {
        UpdateWorkOrderRequest request1 = new UpdateWorkOrderRequest();
        request1.setWorkOrderIndicatedDate(LocalDateTime.now());
        request1.setWorkOrderDueDate(LocalDateTime.now().plusDays(2));
        request1.setWorkOrderIndicatedQuantity(100);

        UpdateWorkOrderRequest request2 = new UpdateWorkOrderRequest();
        request2.setWorkOrderIndicatedDate(LocalDateTime.now());
        request2.setWorkOrderDueDate(LocalDateTime.now().plusDays(2));
        request2.setWorkOrderIndicatedQuantity(100);

        List<UpdateWorkOrderRequest> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);

        return requests;
    }

    private static Stream<Arguments> updateWorkOrderParam() {
        return Stream.of(
                // BEFORE 상태: 모든 필드 수정 가능
                arguments(1L, "BEFORE",
                        LocalDate.of(2024, 12, 18), LocalDate.of(2024, 12, 23),
                        150, "수정된 작업지시서 비고", true),

                // ONGOING 상태: 제한된 필드만 수정 가능
                arguments(6L, "ONGOING",
                        null, LocalDate.now().plusDays(5),
                        300, "진행 중 작업지시서 비고", true),

                // COMPLETE 상태: 수정 불가
                arguments(3L, "COMPLETE",
                        LocalDate.of(2024, 12, 17), LocalDate.of(2024, 12, 19),
                        100, "완료된 작업지시서 비고", false)
        );
    }

    @ParameterizedTest
    @MethodSource("updateWorkOrderParam")
    @DisplayName("작업지시서 상태별 수정 테스트")
    void updateWorkOrderTest(Long workOrderSeq, String status,
                             LocalDateTime workOrderIndicatedDate, LocalDateTime workOrderDueDate,
                             Integer workOrderIndicatedQuantity, String workOrderNote,
                             boolean expectedSuccess) {
        // Given: 작업지시서 요청 데이터 생성
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setWorkOrderIndicatedDate(workOrderIndicatedDate);
        request.setWorkOrderDueDate(workOrderDueDate);
        request.setWorkOrderIndicatedQuantity(workOrderIndicatedQuantity);
        request.setWorkOrderNote(workOrderNote);
        String  userNo = "1";

        WorkOrder.updateTestWorkOrder(WorkOrderStatus.valueOf(status));

        if (expectedSuccess) {
            // When & Then: 성공 케이스
            assertDoesNotThrow(() -> workOrderService.updateWorkOrder(workOrderSeq, request, userNo));
        } else {
            // When & Then: 실패 케이스
            assertThrows(CustomException.class, () -> workOrderService.updateWorkOrder(workOrderSeq, request, userNo));
        }
    }

    private static Stream<Arguments> deleteWorkOrderParam() {
        return Stream.of(
                arguments(7L),
                arguments(11L)
        );
    }

    @DisplayName("작업지시서 삭제")
    @ParameterizedTest
    @MethodSource("deleteWorkOrderParam")
    void deleteWorkOrderTest(Long workOrderSeq) {
        // given
        String userNo = "1";

        // when & then
        assertDoesNotThrow(() -> workOrderService.deleteWorkOrder(workOrderSeq, userNo));
    }

    private static Stream<Arguments> updateWorkOrderStatusParam() {
        return Stream.of(
                arguments(5L, WorkOrderStatus.AFTER),     // 결재 전 -> 결재 후
                arguments(9L, WorkOrderStatus.ONGOING),   // 결재 후 -> 진행중
                arguments(14L, WorkOrderStatus.ONGOING),   // 중단 -> 진행중
                arguments(6L, WorkOrderStatus.STOP),      // 진행중 -> 중단
                arguments(6L, WorkOrderStatus.COMPLETE)   // 진행중 -> 완료
        );
    }

    @DisplayName("작업지시서 상태 변경")
    @ParameterizedTest
    @MethodSource("updateWorkOrderStatusParam")
    void updateWorkOrderStatusTest(Long workOrderSeq, WorkOrderStatus newStatus) {
        // when & then
        assertDoesNotThrow(() -> workOrderService.updateWorkOrderStatus(workOrderSeq, newStatus));
    }

}