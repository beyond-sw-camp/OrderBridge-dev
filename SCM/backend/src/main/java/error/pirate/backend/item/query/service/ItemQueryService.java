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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final BomItemRepository bomItemRepository;
    private final ModelMapper modelMapper;

    public Map<String, Object> readItemList(ItemFilterRequest itemFilterRequest) {

        int page = Math.max(1, itemFilterRequest.getPage());
        int offset = itemFilterRequest.getSize() * (page - 1);

        // 1. 전체 데이터 개수 조회
        int totalElements = itemMapper.countItemsByFilter(
                itemFilterRequest.getItemName(),
                itemFilterRequest.getItemDivisions(),
                itemFilterRequest.getMinExpirationHour(),
                itemFilterRequest.getMaxExpirationHour()
        );

        // 2. 현재 페이지 데이터 목록 조회
        List<ItemResponse> items = itemMapper.findItemListByFilter(
                itemFilterRequest.getItemName(),
                itemFilterRequest.getItemDivisions(),
                itemFilterRequest.getMinExpirationHour(),
                itemFilterRequest.getMaxExpirationHour(),
                itemFilterRequest.getSize(),
                offset
        );

        Map<String, Object> result = new HashMap<>();
        result.put("content", items);
        result.put("totalElements", totalElements);
        result.put("size", itemFilterRequest.getSize());
        result.put("number", page);

        return result;
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
