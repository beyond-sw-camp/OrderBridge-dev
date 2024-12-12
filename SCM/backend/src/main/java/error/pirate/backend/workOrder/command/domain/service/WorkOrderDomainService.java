package error.pirate.backend.workOrder.command.domain.service;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderDomainService {

    private final WorkOrderRepository workOrderRepository;

    // 등록일이 오늘인 개수 카운트
    public long countTodayWorkOrders() {
        LocalDate today = LocalDate.now();
        return workOrderRepository.countByWorkOrderRegDateBetween(
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());
    }

    // 작업지시서명 설정(등록일과 카운트로 이름 설정)
    public String generateWorkOrderName(long count) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return date + "-" + (count + 1);
    }

    // 작업지시서 생성
    public WorkOrder createWorkOrder(CreateWorkOrderRequest request, SalesOrder salesOrder, SalesOrderItem salesOrderItem, Warehouse warehouse, User user,
                                     String workOrderName, LocalDateTime seoulDueDate, LocalDateTime seoulIndicatedDate) {
        return WorkOrder.createWorkOrder(request, salesOrder, salesOrderItem, warehouse, user, workOrderName, seoulIndicatedDate, seoulDueDate);
    }

    // 작업지시서 저장
    public void saveWorkOrder(WorkOrder workOrder) {
        workOrderRepository.save(workOrder);
    }

    // 서울 시간 기준으로 변경
    public LocalDateTime convertToSeoulTime(LocalDateTime localDateTime) {
        ZoneId systemZone = ZoneId.systemDefault(); // 서버 기본 시간대
        ZoneId seoulZone = ZoneId.of("Asia/Seoul"); // 서울 시간대

        ZonedDateTime systemZonedDateTime = localDateTime.atZone(systemZone); // 요청 시간을 시스템 시간대로 설정
        ZonedDateTime seoulZonedDateTime = systemZonedDateTime.withZoneSameInstant(seoulZone); // 서울 시간대로 변환
        log.info("Converted {} to Seoul Time: {}", localDateTime, seoulZonedDateTime.toLocalDateTime());

        return seoulZonedDateTime.toLocalDateTime(); // LocalDateTime 반환
    }

    public boolean existsBySalesOrderItem(Long salesOrderItemSeq) {
        return workOrderRepository.existsBySalesOrderItemSeq(salesOrderItemSeq);
    }
}
