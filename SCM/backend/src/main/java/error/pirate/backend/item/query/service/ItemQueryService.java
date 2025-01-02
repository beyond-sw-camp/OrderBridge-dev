package error.pirate.backend.item.query.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.application.dto.BomItemDTO;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.item.command.domain.repository.ItemInventoryRepository;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.item.command.domain.repository.ItemUnitRepository;
import error.pirate.backend.item.query.dto.ItemDetailResponse;
import error.pirate.backend.item.query.dto.ItemResponse;
import error.pirate.backend.item.query.dto.ItemFilterRequest;
import error.pirate.backend.item.query.dto.ItemInventoryDTO;
import error.pirate.backend.item.command.domain.aggregate.entity.*;
import error.pirate.backend.item.command.domain.repository.*;
import error.pirate.backend.item.query.dto.*;
import error.pirate.backend.item.query.mapper.ItemMapper;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final ItemInventoryRepository itemInventoryRepository;
    private final BomItemRepository bomItemRepository;
    private final ModelMapper modelMapper;
    private final ItemUnitRepository itemUnitRepository;
    private final WarehouseRepository warehouseRepository;


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
        ItemUnit itemUnit = itemUnitRepository.findById(item.getItemUnit().getItemUnitSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_UNIT_NOT_FOUND));
        Warehouse warehouse = warehouseRepository.findById(item.getWarehouse().getWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
        itemDTO.setItemUnitSeq(itemUnit.getItemUnitSeq());
        itemDTO.setWarehouseSeq(warehouse.getWarehouseSeq());

        List<ItemResponse> childItemList = new ArrayList<>();
        List<ItemInventoryDTO> itemInventoryList = new ArrayList<>();

        List<BomItem> bomItems = bomItemRepository.findAllByParentItem(item);
        for (BomItem bomItem : bomItems) {
            Item childItem = itemRepository.findById(bomItem.getChildItem().getItemSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
            ItemUnit childItemUnit = itemUnitRepository.findById(childItem.getItemUnit().getItemUnitSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_UNIT_NOT_FOUND));

            ItemResponse itemResponse = modelMapper.map(childItem, ItemResponse.class);
            itemResponse.setItemUnitTitle(childItemUnit.getItemUnitTitle());
            itemResponse.setBomChildItemQuantity(bomItem.getBomChildItemQuantity());

            childItemList.add(itemResponse);
        }

        // 재고가 다 떨어진 재고는 조회하지 않는다.
        List<ItemInventory> itemInventories = itemInventoryRepository
                .findAllByItemAndItemInventoryRemainAmountGreaterThanOrderByItemInventoryExpirationDate(item, 0);
        for (ItemInventory itemInventory : itemInventories) {
            itemInventoryList.add(modelMapper.map(itemInventory, ItemInventoryDTO.class));
        }

        return new ItemDetailResponse(itemDTO, childItemList, itemInventoryList);
    }

    public List<ItemUnitResponse> getItemUnits() {
        List<ItemUnit> itemUnits = itemUnitRepository.findAll();
        return itemUnits.stream()
                .map(unit -> new ItemUnitResponse(unit.getItemUnitSeq(), unit.getItemUnitTitle()))
                .collect(Collectors.toList());
    }


    // 품목 구분 조회
    public List<ItemDivisionResponse> getAllItemDivisions() {
        return Arrays.stream(ItemDivision.values())
                .map(division -> new ItemDivisionResponse(division.name(), division.getDescription()))
                .collect(Collectors.toList());
    }

    // bom 조회
    public List<BomItemDTO> readBomItems(Long itemSeq) {
        List<BomItemDTO> bomItems = itemMapper.readBomItems(itemSeq);
        if (bomItems.isEmpty()) {
            throw new CustomException(ErrorCodeType.BOM_ITEM_NOT_FOUND);
        }
        return bomItems;
    }
}
