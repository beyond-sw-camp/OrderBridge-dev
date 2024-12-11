package error.pirate.backend.shippingSlip.command.application.service;

import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.service.ShippingInstructionDomainService;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemDTO;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipItem;
import error.pirate.backend.shippingSlip.command.domain.service.ShippingSlipDomainService;
import error.pirate.backend.shippingSlip.command.domain.service.ShippingSlipItemDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingSlipApplicationService {

    private final ShippingSlipDomainService shippingSlipDomainService;
    private final ShippingSlipItemDomainService shippingSlipItemDomainService;
    private final ShippingInstructionDomainService shippingInstructionDomainService;
    private final ItemService itemService;
    private final SalesOrderDomainService salesOrderDomainService;

    /* 출하전표 등록 */
    @Transactional
    public void createShippingSlip(ShippingSlipRequest shippingSlipRequest) {
        ShippingInstruction shippingInstruction
                = shippingInstructionDomainService.findByShippingInstructionSeq(shippingSlipRequest.getShippingInstructionSeq());

        // 출하지시서가 결재후 상태인지 체크
        shippingSlipDomainService.checkShippingInstructionStatus(shippingInstruction);

        // 출하전표 유저는 출하지시서 작성 유저
        User user = shippingInstruction.getUser();

        /* 출하일을 서울 시간으로 변경 */
        LocalDateTime shippingSlipShippingDate =
                shippingSlipDomainService.setShippingSlipShippingDate(
                        shippingSlipRequest.getShippingSlipShippingDate());

        /* 등록일을 기반으로 ShippingSlip 명 설정 */
        long count = shippingSlipDomainService.countTodayShippingSlip();
        String shippingSlipName = shippingSlipDomainService.setShippingSlipName(count);

        /* 총수량 연산 */
        int itemTotalQuantity = shippingSlipDomainService.calculateTotalQuantity(shippingSlipRequest);

        /* ShippingSlip 도메인 생성 로직 실행, entity 반환 */
        ShippingSlip newShippingSlip =
                shippingSlipDomainService.createShippingSlip(
                        shippingSlipRequest, shippingInstruction, user,
                        shippingSlipName, shippingSlipShippingDate, itemTotalQuantity
                );

        /* save 로직 실행 */
        ShippingSlip shippingSlip =
                shippingSlipDomainService.saveShippingSlip(newShippingSlip);

        /* 물품 불러오기 */
        List<Long> itemNameList = shippingSlipRequest.getShippingSlipItems()
                .stream()
                .map(ShippingSlipItemDTO::getItemSeq) // itemSeq 필드 추출
                .toList(); // 추출한 값을 List로 변환
        List<Item> itemList = itemService.findAllById(itemNameList);

        /* ShippingSlipItem 도메인 생성 로직 실행, entity 반환 */
        List<ShippingSlipItem> newShippingSlipItemList
                = shippingSlipItemDomainService.createShippingSlipItemList(
                        shippingSlipRequest, shippingSlip, itemList);

        /* saveAll 로직 실행 */
        List<ShippingSlipItem> shippingSlipItem
                = shippingSlipItemDomainService.saveShippingSlipItem(newShippingSlipItemList);

        /* 주문서 상태를 출하완료로 변경 */
        salesOrderDomainService.updateSalesOrderStatus(shippingInstruction.getSalesOrder(), "SHIPMENT_COMPLETE");
    }

    /* 출하전표 수정 */
    @Transactional
    public void updateShippingSlip(Long shippingSlipSeq, ShippingSlipRequest shippingSlipRequest) {
        /* 출하전표 찾기 */
        ShippingSlip shippingSlip
                = shippingSlipDomainService.findByShippingSlipSeq(shippingSlipSeq);

        /* 수정이 가능한 상태인지 체크 */
        shippingSlipDomainService.checkShippingSlipDeleteStatus(shippingSlip.getShippingSlipStatus());

        /* 현재 출하지시서 저장 */
        ShippingInstruction shippingInstruction = shippingSlip.getShippingInstruction();

        // 등록과 동일
        ShippingInstruction newShippingInstruction
                = shippingInstructionDomainService.findByShippingInstructionSeq(shippingSlipRequest.getShippingInstructionSeq());

        // 출하지시서가 결재후 상태인지 체크
        shippingSlipDomainService.checkShippingInstructionStatus(newShippingInstruction);

        // 출하전표 유저는 출하지시서 작성 유저
        User user = newShippingInstruction.getUser();

        /* 출하일을 서울 시간으로 변경 */
        LocalDateTime shippingSlipShippingDate =
                shippingSlipDomainService.setShippingSlipShippingDate(
                        shippingSlipRequest.getShippingSlipShippingDate());

        /* 총수량 연산 */
        int itemTotalQuantity = shippingSlipDomainService.calculateTotalQuantity(shippingSlipRequest);

        /* ShippingInstruction 도메인 수정 로직 실행 */
        ShippingSlip newShippingSlip =
                shippingSlipDomainService.updateShippingSlip(
                        shippingSlip, shippingSlipRequest, newShippingInstruction, user,
                        shippingSlipShippingDate, itemTotalQuantity
                );

        /* 출하지시서가 변경되었는지 체크 */
        boolean changeShippingInstruction =
                shippingSlipDomainService.checkChangedShippingInstruction(shippingInstruction, newShippingInstruction);

        /* 출하지시서가 변경되었다면 출하전표 품목도 변경*/
        if (changeShippingInstruction){
            /* 물품 불러오기 */
            List<Long> itemNameList = shippingSlipRequest.getShippingSlipItems()
                    .stream()
                    .map(ShippingSlipItemDTO::getItemSeq) // itemSeq 필드 추출
                    .toList(); // 추출한 값을 List로 변환
            List<Item> itemList = itemService.findAllById(itemNameList);

            /* 기존에 저장된 출하전표 품목 리스트 삭제 */
            shippingSlipItemDomainService.deleteByShippingSlip(newShippingSlip);

            /* ShippingSlipItem 도메인 생성 로직 실행, entity 반환 */
            List<ShippingSlipItem> newShippingSlipItemList
                    = shippingSlipItemDomainService.createShippingSlipItemList(
                            shippingSlipRequest, newShippingSlip, itemList);

            /* saveAll 로직 실행 */
            List<ShippingSlipItem> shippingSlipItem
                    = shippingSlipItemDomainService.saveShippingSlipItem(newShippingSlipItemList);
        }
    }

    /* 출하전표 삭제 */
    @Transactional
    public void deleteShippingSlip(Long shippingSlipSeq) {
        /* 출하전표 찾기 */
        ShippingSlip shippingSlip = shippingSlipDomainService.findByShippingSlipSeq(shippingSlipSeq);

        /* 삭제 상태로 변경 */
        shippingSlipDomainService.deleteShippingSlip(shippingSlip);

        /* 주문서 상태를 생산완료로 변경 */
        salesOrderDomainService.updateSalesOrderStatus(shippingSlip.getShippingInstruction().getSalesOrder(), "PRODUCTION_COMPLETE");
    }
}
