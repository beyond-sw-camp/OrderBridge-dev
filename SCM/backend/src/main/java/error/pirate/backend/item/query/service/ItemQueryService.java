package error.pirate.backend.item.query.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.item.command.domain.repository.ItemInventoryRepository;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.item.query.dto.ItemDetailResponse;
import error.pirate.backend.item.query.dto.ItemResponse;
import error.pirate.backend.item.query.dto.ItemFilterRequest;
import error.pirate.backend.item.query.dto.ItemInventoryDTO;
import error.pirate.backend.item.query.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final BomItemRepository bomItemRepository;
    private final ModelMapper modelMapper;

    public List<ItemResponse> readItemList(ItemFilterRequest itemFilterRequest) {
        int offset = itemFilterRequest.getSize() * (itemFilterRequest.getPage() - 1) ;

        return itemMapper.findItemListByFilter(
                itemFilterRequest.getItemName(),
                itemFilterRequest.getItemDivision(),
                itemFilterRequest.getItemExpirationHour(),
                itemFilterRequest.getSortBy(),
                itemFilterRequest.getSortDirection(),
                offset,
                itemFilterRequest.getSize()
        );
    }

    public ItemDetailResponse readItem(Long itemSeq) {
        Item item = itemRepository.findById(itemSeq).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

        ItemResponse itemDTO = modelMapper.map(item, ItemResponse.class);

        List<ItemResponse> childItemList = new ArrayList<>();
        List<ItemInventoryDTO> itemInventoryList = new ArrayList<>();

        List<BomItem> bomItems = bomItemRepository.findAllByParentItem(item);
        for(BomItem bomItem : bomItems) {
            Item childItem = itemRepository.findById(bomItem.getChildItem().getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

            childItemList.add(modelMapper.map(childItem, ItemResponse.class));
        }

        // 재고가 다 떨어진 재고는 조회하지 않는다.
        List<ItemInventory> itemInventories = itemInventoryRepository
                .findAllByItemAndItemInventoryRemainAmountGreaterThanOrderByItemInventoryExpirationDate(item, 0);
        for(ItemInventory itemInventory : itemInventories) {
            itemInventoryList.add(modelMapper.map(itemInventory, ItemInventoryDTO.class));
        }

        return new ItemDetailResponse(itemDTO, childItemList, itemInventoryList);
    }
}
