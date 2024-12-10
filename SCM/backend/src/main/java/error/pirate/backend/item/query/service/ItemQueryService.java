package error.pirate.backend.item.query.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.item.command.domain.repository.ItemInventoryRepository;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.item.query.dto.ItemDTO;
import error.pirate.backend.item.query.dto.ItemDetailResponse;
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

    public List<ItemDTO> readItemList(ItemFilterRequest itemFilterRequest) {
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

        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);

        List<ItemDTO> childItemList = new ArrayList<>();
        List<ItemInventoryDTO> itemInventoryList = new ArrayList<>();

        List<BomItem> bomItems = bomItemRepository.findAllByParentItem(item);
        for(BomItem bomItem : bomItems) {
            Item childItem = itemRepository.findById(bomItem.getChildItem().getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

            childItemList.add(modelMapper.map(childItem, ItemDTO.class));
        }

        List<ItemInventory> itemInventories = itemInventoryRepository.findAllByItem(item);
        for(ItemInventory itemInventory : itemInventories) {
            itemInventoryList.add(modelMapper.map(itemInventory, ItemInventoryDTO.class));
        }

        return new ItemDetailResponse(itemDTO, childItemList, itemInventoryList);
    }
}
