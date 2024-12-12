package error.pirate.backend.shippingSlip.command.domain.service;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipItem;
import error.pirate.backend.shippingSlip.command.domain.repository.ShippingSlipItemRepository;
import error.pirate.backend.shippingSlip.command.mapper.ShippingSlipItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingSlipItemDomainService {

    private final ShippingSlipItemRepository shippingSlipItemRepository;

    /* 도메인 객체를 생성하는 로직 */
    public List<ShippingSlipItem> createShippingSlipItemList(
            ShippingSlipRequest shippingSlipRequest,
            ShippingSlip shippingSlip,
            List<Item> itemList) {

        /* dto to entity */
        List<ShippingSlipItem> newShippingSlipItems = ShippingSlipItemMapper.toEntity(
                shippingSlipRequest, shippingSlip, itemList
        );

        return newShippingSlipItems;
    }

    /* 도메인 객체를 저장하는 로직 */
    public List<ShippingSlipItem> saveShippingSlipItem(List<ShippingSlipItem> newShippingSlipItemList) {
        return shippingSlipItemRepository.saveAll(newShippingSlipItemList);
    }

    /* 출하지시서 번호로 도메인 객체 삭제하는 로직 */
    public void deleteByShippingSlip(ShippingSlip newShippingSlip) {
        shippingSlipItemRepository.deleteByShippingSlip(newShippingSlip);
    }
}
