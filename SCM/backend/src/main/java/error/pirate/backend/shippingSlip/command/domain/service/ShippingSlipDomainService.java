package error.pirate.backend.shippingSlip.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.repository.ShippingSlipRepository;
import error.pirate.backend.shippingSlip.command.mapper.ShippingSlipMapper;
import error.pirate.backend.shippingSlip.query.service.ShippingSlipQueryService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingSlipDomainService {

    private final ShippingSlipRepository shippingSlipRepository;
    private final ShippingSlipQueryService shippingSlipQueryService;

    /* 도메인 객체를 생성하는 로직 */
    public ShippingSlip createShippingSlip(
            ShippingSlipRequest shippingSlipRequest,
            ShippingInstruction shippingInstruction, User user, String shippingInstructionName,
            LocalDateTime shippingSlipShippingDate,
            int itemTotalQuantity) {

        /* dto to entity */
        ShippingSlip newShippingSlip = ShippingSlipMapper.toEntity(
                shippingSlipRequest, shippingInstruction, user, shippingInstructionName,
                shippingSlipShippingDate, itemTotalQuantity);
        return newShippingSlip;
    }

    /* 도메인 객체를 저장하는 로직 */
    public ShippingSlip saveShippingSlip(ShippingSlip newShippingSlip) {
        return shippingSlipRepository.save(newShippingSlip);
    }

    /* 출하지시서가 결재후 상태인지 체크 */
    public void checkShippingInstructionStatus(ShippingInstruction shippingInstruction) {
        if (!shippingInstruction.getShippingInstructionStatus().equals(ShippingInstructionStatus.AFTER)) {
            throw new CustomException(ErrorCodeType.SHIPPING_INSTRUCTION_STATE_BAD_REQUEST);
        }
    }

    /* 출하일 서울시간 설정 */
    public LocalDateTime setShippingSlipShippingDate(LocalDateTime shippingSlipShippingDate) {
        ZonedDateTime systemZonedDateTime = shippingSlipShippingDate.atZone(ZoneId.systemDefault());
        ZonedDateTime seoulZonedDateTime = systemZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return seoulZonedDateTime.toLocalDateTime();
    }

    /* 총수량 계산 */
    public int calculateTotalQuantity(ShippingSlipRequest request) {
        // Null 검사를 수행하여 NPE 방지
        if (request.getShippingSlipItems() == null || request.getShippingSlipItems().isEmpty()) {
            return 0;
        }

        // stream을 사용해 shippingSlipItemQuantity 필드 합산
        return request.getShippingSlipItems()
                .stream()
                .mapToInt(ShippingSlipItemRequest::getShippingSlipItemQuantity)
                .sum();
    }

    // 이미 출하지시서에 해당하는 출하전표가 있는지 체크
    public void checkShippingSlip(long shippingInstructionSeq) {
        List<ShippingSlip> shippingSlip = shippingSlipRepository.findByShippingInstructionShippingInstructionSeq (shippingInstructionSeq);
        if(!shippingSlip.isEmpty()) {
            throw new CustomException(ErrorCodeType.SHIPPING_SLIP_ALREADY_EXISTS);
        }
    }
}
