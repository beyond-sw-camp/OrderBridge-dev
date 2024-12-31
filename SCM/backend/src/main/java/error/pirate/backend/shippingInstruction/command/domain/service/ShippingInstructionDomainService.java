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
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionItemCheckDTO;
import error.pirate.backend.shippingInstruction.query.service.ShippingInstructionQueryService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingInstructionDomainService {

    private final ShippingInstructionRepository shippingInstructionRepository;
    private final ShippingInstructionQueryService shippingInstructionQueryService;

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

    /* 주문서 번호로 출하지시서 리스트 찾기 */
    public List<ShippingInstruction> findBySalesOrder(SalesOrder salesOrder) {
        return shippingInstructionRepository.findBySalesOrder(salesOrder);
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
                shippingInstructionRequest.getShippingAddress(),
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

    // 주문서와 출하지시서의 품목 수량 비교
    public Boolean validateItem(long salesOrderSeq) {
        boolean completeStatus = true;

        List<ShippingInstructionItemCheckDTO> shippingInstructionItemCheckList =
                shippingInstructionQueryService.shippingInstructionItemCheck(salesOrderSeq);

        /* 체크 후 잔여 수량이 마이너스면 에러 발생 */
        /* 체크 후 잔여 수량이 0이 아니면 false 반환 */
        for (ShippingInstructionItemCheckDTO shippingInstructionItemCheck : shippingInstructionItemCheckList) {
            if (shippingInstructionItemCheck.getRemainingQuantity() < 0) {
                throw new CustomException(ErrorCodeType.SHIPPING_INSTRUCTION_ITEM_NOT_MATCH);
            }
            if (shippingInstructionItemCheck.getRemainingQuantity() != 0) {
                completeStatus = false;
            }
        }

        return completeStatus;
    }

    // 유저 체킹
    public void checkUser(ShippingInstruction shippingInstruction, User user) {
        if (!user.equals(shippingInstruction.getUser())) {
            throw new CustomException(ErrorCodeType.SECURITY_ACCESS_ERROR);
        }
    }
}
