package error.pirate.backend.shippingSlip.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipStatus;
import error.pirate.backend.shippingSlip.command.domain.repository.ShippingSlipRepository;
import error.pirate.backend.shippingSlip.command.mapper.ShippingSlipMapper;
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
public class ShippingSlipDomainService {

    private final ShippingSlipRepository shippingSlipRepository;

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

    /* 오늘날짜의 등록된 출하전표 갯수 찾기 */
    public long countTodayShippingSlip() {
        // 서울 시간대(Asia/Seoul)를 지정하여 시작과 끝 시간 생성
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

        // 서울 시간대 기준으로 오늘의 시작과 끝 시간을 계산
        LocalDateTime startOfDay = LocalDate.now(seoulZoneId).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return shippingSlipRepository.countByShippingSlipRegDateBetween(startOfDay, endOfDay);
    }

    /* 출하전표 이름 설정 */
    public String setShippingSlipName(long count) {
        // 서울 기준 현재 날짜 가져오기
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // yyyy/MM/dd 형식으로 변환
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return formattedDate + " - " + (count + 1);
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

    /* 출하전표 번호로 출하전표 찾기 */
    public ShippingSlip findByShippingSlipSeq(Long shippingSlipSeq) {
        return shippingSlipRepository.findById(shippingSlipSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SHIPPING_SLIP_NOT_FOUND));
    }

    /* 도메인 객체를 수정하는 로직 */
    public ShippingSlip updateShippingSlip(
            ShippingSlip shippingSlip, ShippingSlipRequest shippingSlipRequest,
            ShippingInstruction shippingInstruction, User user,
            LocalDateTime shippingSlipShippingDate, int itemTotalQuantity) {

        /* 수정을 위해 엔터티 정보 변경 */
        shippingSlip.update(
                shippingInstruction,
                user,
                shippingInstruction.getShippingInstructionAddress(),
                shippingSlipShippingDate,
                itemTotalQuantity,
                shippingSlipRequest.getShippingSlipNote()
        );

        return shippingSlip;
    }

    /* 출하전표 수정이 가능한 상태인지 체크 */
    public void checkShippingSlipDeleteStatus(ShippingSlipStatus shippingSlipStatus) {
        /* 삭제 상태라면 변경 불가*/
        if (shippingSlipStatus.equals(ShippingSlipStatus.DELETE)) {
            throw new CustomException(ErrorCodeType.SHIPPING_SLIP_DELETE_STATE);
        }
    }

    /* 출하지시서가 변경되었는지 체크 */
    public boolean checkChangedShippingInstruction(ShippingInstruction shippingInstruction, ShippingInstruction newShippingInstruction) {
        return !Objects.equals(
                shippingInstruction.getShippingInstructionSeq(), newShippingInstruction.getShippingInstructionSeq());
    }

    /* 도메인 객체를 삭제 로직 */
    public void deleteShippingSlip(ShippingSlip shippingSlip) {
        /* 수정을 위해 엔터티 정보 변경 */
        shippingSlip.updateStatus("DELETE");
    }


}
