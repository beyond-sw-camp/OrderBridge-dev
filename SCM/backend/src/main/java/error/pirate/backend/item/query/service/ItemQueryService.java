package error.pirate.backend.item.query.service;

import error.pirate.backend.item.query.dto.ItemDTO;
import error.pirate.backend.item.query.dto.ItemFilterRequest;
import error.pirate.backend.item.query.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ItemMapper itemMapper;

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
}
