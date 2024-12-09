package error.pirate.backend.shippingInstruction.command.application.service;

import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemDTO;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionItem;
import error.pirate.backend.shippingInstruction.command.domain.service.ShippingInstructionDomainService;
import error.pirate.backend.shippingInstruction.command.domain.service.ShippingInstructionItemDomainService;
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
public class ShippingInstructionApplicationService {

    private final ShippingInstructionDomainService shippingInstructionDomainService;
    private final ShippingInstructionItemDomainService shippingInstructionItemDomainService;
    private final SalesOrderDomainService salesOrderDomainService;
    private final ItemService itemService;

    /* 출하지시서 등록 */
    @Transactional
    public void createShippingInstruction(ShippingInstructionRequest shippingInstructionRequest) {
        SalesOrder salesOrder = salesOrderDomainService.findById(shippingInstructionRequest.getSalesOrderSeq());

        // 출하지시서 유저는 주문서 작성 유저
        User user = salesOrder.getUser();

        /* 출하예정일을 서울 시간으로 변경 */
        LocalDateTime shippingInstructionScheduledShipmentDate =
                shippingInstructionDomainService.setShippingInstructionScheduledShipmentDate(
                        shippingInstructionRequest.getShippingInstructionScheduledShipmentDate());

        /* 등록일을 기반으로 ShippingInstruction 명 설정 */
        long count = shippingInstructionDomainService.countTodayShippingInstruction();
        String shippingInstructionName = shippingInstructionDomainService.setShippingInstructionName(count);

        /* 총수량 연산 */
        int itemTotalQuantity = shippingInstructionDomainService.calculateTotalQuantity(shippingInstructionRequest);

        /* ShippingInstruction 도메인 생성 로직 실행, entity 반환 */
        ShippingInstruction newShippingInstruction =
                shippingInstructionDomainService.createShippingInstruction(
                        shippingInstructionRequest, salesOrder, user,
                        shippingInstructionName, shippingInstructionScheduledShipmentDate, itemTotalQuantity
                );

        /* save 로직 실행 */
        ShippingInstruction shippingInstruction =
                shippingInstructionDomainService.saveShippingInstruction(newShippingInstruction);

        /* 물품 불러오기 */
        List<Long> itemNameList = shippingInstructionRequest.getShippingInstructionItems()
                .stream()
                .map(ShippingInstructionItemDTO::getItemSeq) // itemSeq 필드 추출
                .toList(); // 추출한 값을 List로 변환
        List<Item> itemList = itemService.findAllById(itemNameList);

        /* ShippingInstructionItem 도메인 생성 로직 실행, entity 반환 */
        List<ShippingInstructionItem> newShippingInstructionItemList
                = shippingInstructionItemDomainService.createShippingInstructionItemList(shippingInstructionRequest, shippingInstruction, itemList);

        /* saveAll 로직 실행 */
        List<ShippingInstructionItem> shippingInstructionItem
                = shippingInstructionItemDomainService.saveShippingInstructionItem(newShippingInstructionItemList);
    }

    /* 출하지시서 수정 */
    @Transactional
    public void updateShippingInstruction(Long shippingInstructionSeq, ShippingInstructionRequest shippingInstructionRequest) {
        /* 출하지시서 찾기(결재전 상태일 때만) */
        ShippingInstruction shippingInstruction = shippingInstructionDomainService.findByShippingInstructionSeq(shippingInstructionSeq);

        /* 수정이 가능한 상태인지 체크 */
        shippingInstructionDomainService.checkShippingInstructionStatus(shippingInstruction.getShippingInstructionStatus());

        /* 현재 주문서 저장 */
        SalesOrder salesOrder = shippingInstruction.getSalesOrder();

        SalesOrder newSalesOrder = salesOrderDomainService.findBySalesOrderName(shippingInstructionRequest.getSalesOrderName());

        // 출하지시서 유저는 주문서 작성 유저
        User user = newSalesOrder.getUser();

        /* 출하예정일을 서울 시간으로 변경 */
        LocalDateTime shippingInstructionScheduledShipmentDate =
                shippingInstructionDomainService.setShippingInstructionScheduledShipmentDate(
                        shippingInstructionRequest.getShippingInstructionScheduledShipmentDate());

        /* 등록일을 기반으로 ShippingInstruction 명 설정 */
        long count = shippingInstructionDomainService.countTodayShippingInstruction();
        String newShippingInstructionName = shippingInstructionDomainService.setShippingInstructionName(count);

        /* 총수량 연산 */
        int itemTotalQuantity = shippingInstructionDomainService.calculateTotalQuantity(shippingInstructionRequest);

        /* ShippingInstruction 도메인 수정 로직 실행 */
        ShippingInstruction newShippingInstruction =
                shippingInstructionDomainService.updateShippingInstruction(
                        shippingInstruction, shippingInstructionRequest, newSalesOrder, user,
                        newShippingInstructionName, shippingInstructionScheduledShipmentDate,
                        itemTotalQuantity
                );

        /* 주문서가 변경되었는지 체크 */
        boolean changeSalesOrder =
                shippingInstructionDomainService.checkShippingInstructionSalesOrder(salesOrder, newSalesOrder);

        /* 주문서가 변경되었다면 출하지시서 품목도 변경*/
        if (changeSalesOrder){
            /* 물품 불러오기 */
            List<String> itemNameList = shippingInstructionRequest.getShippingInstructionItems()
                    .stream()
                    .map(ShippingInstructionItemDTO::getItemName) // itemName 필드 추출
                    .toList(); // 추출한 값을 List로 변환
            List<Item> itemList = itemDomainService.findByItemNameIn(itemNameList);

            /* 기존에 저장된 출하지시서 품목 삭제 */
            shippingInstructionItemDomainService.deleteByShippingInstruction(newShippingInstruction);

            /* ShippingInstructionItem 도메인 생성 로직 실행, entity 반환 */
            List<ShippingInstructionItem> newShippingInstructionItemList
                    = shippingInstructionItemDomainService.createShippingInstructionItemList(shippingInstructionRequest, newShippingInstruction, itemList);

            /* saveAll 로직 실행 */
            List<ShippingInstructionItem> shippingInstructionItem
                    = shippingInstructionItemDomainService.saveShippingInstructionItem(newShippingInstructionItemList);
        }
    }

    /* 출하지시서 상태 변경 */
    @Transactional
    public void updateShippingInstructionStatus(Long shippingInstructionSeq) {
        /* 출하지시서 찾기(결재전 상태일 때만) */
        ShippingInstruction shippingInstruction = shippingInstructionDomainService.findByShippingInstructionSeq(shippingInstructionSeq);

        /* 수정이 가능한 상태인지 체크 */
        shippingInstructionDomainService.checkShippingInstructionStatus(shippingInstruction.getShippingInstructionStatus());

        /* 상태 변경 */
        shippingInstructionDomainService.updateShippingInstructionStatus(shippingInstruction);
    }
}
