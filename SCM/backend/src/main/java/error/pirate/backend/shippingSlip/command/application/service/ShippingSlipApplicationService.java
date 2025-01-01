package error.pirate.backend.shippingSlip.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.service.ItemInventoryDomainService;
import error.pirate.backend.notification.command.domain.aggregate.entity.NotificationType;
import error.pirate.backend.notification.command.domain.service.NotificationDomainService;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.service.ShippingInstructionDomainService;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipItem;
import error.pirate.backend.shippingSlip.command.domain.service.ShippingSlipDomainService;
import error.pirate.backend.shippingSlip.command.domain.service.ShippingSlipItemDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.service.UserDomainService;
import jakarta.persistence.EntityManager;
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
    private final SalesOrderDomainService salesOrderDomainService;
    private final ShippingInstructionDomainService shippingInstructionDomainService;
    private final ItemService itemService;
    private final UserDomainService userDomainService;
    private final ItemInventoryDomainService itemInventoryDomainService;
    private final NotificationDomainService notificationDomainService;
    private final EntityManager entityManager;
    private final NameGenerator nameGenerator;

    /* 출하전표 등록 */
    @Transactional
    public void createShippingSlip(ShippingSlipRequest shippingSlipRequest, String userNo) {
        // 불러온 출하지시서가 있으면 출하지시서 확인 절차
        shippingSlipItemDomainService.validateItem(
                shippingSlipRequest.getShippingInstructionSeq(), shippingSlipRequest.getShippingSlipItems());

        // 이미 해당 출하지시서에 해당하는 출하전표가 존재하면 에러 발생
        shippingSlipDomainService.checkShippingSlip(shippingSlipRequest.getShippingInstructionSeq());

        ShippingInstruction shippingInstruction
                = entityManager.getReference(ShippingInstruction.class, shippingSlipRequest.getShippingInstructionSeq());

        // 출하지시서가 결재후 상태인지 체크
        shippingSlipDomainService.checkShippingInstructionStatus(shippingInstruction);
        // 출하전표 유저는 현재 로그인한 유저
        User user = userDomainService.findByUserEmployeeNo(userNo);
        /* 등록일 설정 공통코드 */
        String shippingSlipName = nameGenerator.nameGenerator(ShippingSlip.class);

        /* 출하일을 서울 시간으로 변경 */
        LocalDateTime shippingSlipShippingDate =
                shippingSlipDomainService.setShippingSlipShippingDate(
                        shippingSlipRequest.getShippingSlipShippingDate());

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
        List<Long> itemSeqList = shippingSlipRequest.getShippingSlipItems()
                .stream()
                .map(ShippingSlipItemRequest::getItemSeq) // itemSeq 필드 추출
                .toList(); // 추출한 값을 List로 변환
        List<Item> itemList = itemService.findAllById(itemSeqList);

        // 물품 재고 변경
        List<Integer> itemQuantityList = shippingSlipRequest.getShippingSlipItems()
                .stream()
                .map(ShippingSlipItemRequest::getShippingSlipItemQuantity) // itemQuantity 필드 추출
                .toList(); // 추출한 값을 List로 변환
        itemInventoryDomainService.updateItemQuantity(itemList, itemQuantityList);

        /* ShippingSlipItem 도메인 생성 로직 실행, entity 반환 */
        List<ShippingSlipItem> newShippingSlipItemList
                = shippingSlipItemDomainService.createShippingSlipItemList(
                        shippingSlipRequest, shippingSlip, itemList);

        /* saveAll 로직 실행 */
        List<ShippingSlipItem> shippingSlipItem
                = shippingSlipItemDomainService.saveShippingSlipItem(newShippingSlipItemList);

        // 주문서와 출하지시서의 품목 수량 비교(0개인지 체크)
        Boolean shipmentCompleteStatus
                = shippingInstructionDomainService.validateItem(shippingInstruction.getSalesOrder().getSalesOrderSeq());
        if(shipmentCompleteStatus) {
            /* 주문서 상태를 출하완료로 변경 */
            salesOrderDomainService.updateSalesOrderStatus(shippingInstruction.getSalesOrder(), "SHIPMENT_COMPLETE");
        }

        //알림 생성
        notificationDomainService.createNotificationMessage(NotificationType.shippingSlip, shippingSlip.getShippingSlipSeq(), shippingSlip.getShippingSlipName());
    }
}
