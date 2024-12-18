package error.pirate.backend.shippingInstruction.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemRequest;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingInstruction.command.domain.repository.ShippingInstructionRepository;
import error.pirate.backend.shippingInstruction.command.mapper.ShippingInstructionMapper;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingInstructionDomainService {

    private final ShippingInstructionRepository shippingInstructionRepository;

    /* 도메인 객체를 생성하는 로직 */
    public ShippingInstruction createShippingInstruction(
            ShippingInstructionRequest shippingInstructionRequest,
            SalesOrder salesOrder, User user, String shippingInstructionName,
            LocalDateTime shippingInstructionScheduledShipmentDate,
            int itemTotalQuantity) {

        /* dto to entity */
        ShippingInstruction newShippingInstruction = ShippingInstructionMapper.toEntity(
                shippingInstructionRequest, salesOrder, user, shippingInstructionName, shippingInstructionScheduledShipmentDate, itemTotalQuantity);
        return newShippingInstruction;
    }

    /* 도메인 객체를 저장하는 로직 */
    public ShippingInstruction saveShippingInstruction(ShippingInstruction newShippingInstruction) {
        return shippingInstructionRepository.save(newShippingInstruction);
    }

    /* 주문서 상태가 생산완료인지 체크 */
    public void checkSalesOrderStatus(SalesOrder salesOrder) {
        if (!salesOrder.getSalesOrderStatus().equals(SalesOrderStatus.PRODUCTION_COMPLETE)) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }
    }

    /* 출하예정일 서울시간 설정 */
    public LocalDateTime setShippingInstructionScheduledShipmentDate(LocalDateTime shippingInstructionScheduledShipmentDate) {
        ZonedDateTime systemZonedDateTime = shippingInstructionScheduledShipmentDate.atZone(ZoneId.systemDefault());
        ZonedDateTime seoulZonedDateTime = systemZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return seoulZonedDateTime.toLocalDateTime();
    }

    /* 오늘날짜의 등록된 출하지지서 갯수 찾기 */
    public long countTodayShippingInstruction() {
        // 서울 시간대(Asia/Seoul)를 지정하여 시작과 끝 시간 생성
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        // 서울 시간대 기준으로 오늘의 시작과 끝 시간을 계산
        LocalDateTime startOfDay = LocalDate.now(seoulZoneId).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return shippingInstructionRepository.countByShippingInstructionRegDateBetween(startOfDay, endOfDay);
    }

    /* 출하지시서 이름 설정 */
    public String setShippingInstructionName(long count) {
        // 서울 기준 현재 날짜 가져오기
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // yyyy/MM/dd 형식으로 변환
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return formattedDate + " - " + (count + 1);
    }

    /* 총수량 계산 */
    public int calculateTotalQuantity(ShippingInstructionRequest request) {
        // Null 검사를 수행하여 NPE 방지
        if (request.getShippingInstructionItems() == null || request.getShippingInstructionItems().isEmpty()) {
            return 0;
        }

        // stream을 사용해 shippingInstructionItemQuantity 필드 합산
        return request.getShippingInstructionItems()
                .stream()
                .mapToInt(ShippingInstructionItemRequest::getShippingInstructionItemQuantity)
                .sum();
    }

    /* 출하지시서 번호로 출하지시서 찾기 */
    public ShippingInstruction findByShippingInstructionSeq(Long shippingInstructionSeq) {
        return shippingInstructionRepository.findById(shippingInstructionSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SHIPPING_INSTRUCTION_NOT_FOUND));
    }

    /* 도메인 객체를 수정하는 로직 */
    public ShippingInstruction updateShippingInstruction(
            ShippingInstruction shippingInstruction, ShippingInstructionRequest shippingInstructionRequest,
            SalesOrder salesOrder, User user, LocalDateTime shippingInstructionScheduledShipmentDate,
            int itemTotalQuantity) {

        /* 수정을 위해 엔터티 정보 변경 */
        shippingInstruction.update(
                salesOrder,
                user,
                shippingInstructionRequest.getShippingInstructionAddress(),
                shippingInstructionScheduledShipmentDate,
                itemTotalQuantity,
                shippingInstructionRequest.getShippingInstructionNote()
        );

        return shippingInstruction;
    }

    /* 출하지시서 수정이 가능한 상태인지 체크 */
    public void checkShippingInstructionApprovalStatus(ShippingInstructionStatus shippingInstructionStatus) {
        /* 결재전이 아니라면 변경 불가*/
        if (!shippingInstructionStatus.equals(ShippingInstructionStatus.BEFORE)) {
            throw new CustomException(ErrorCodeType.SHIPPING_INSTRUCTION_STATE_BAD_REQUEST);
        }
    }

    /* 주문서가 변경되었는지 체크 */
    public boolean checkChangedSalesOrder(SalesOrder salesOrder, SalesOrder newSalesOrder) {
        return !Objects.equals(salesOrder.getSalesOrderSeq(), newSalesOrder.getSalesOrderSeq());
    }

    /* 상태를 수정하는 로직 */
    public void updateShippingInstructionStatus(ShippingInstruction shippingInstruction, String status) {
        /* 수정을 위해 엔터티 정보 변경 */
        shippingInstruction.updateStatus(status);
    }

    /* 도메인 객체를 삭제 로직 */
    public void deleteShippingInstruction(ShippingInstruction shippingInstruction) {
        /* 수정을 위해 엔터티 정보 변경 */
        shippingInstruction.updateStatus("DELETE");
    }
}
