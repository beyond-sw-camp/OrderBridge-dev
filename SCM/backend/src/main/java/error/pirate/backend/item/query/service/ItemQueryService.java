package error.pirate.backend.item.query.service;

import error.pirate.backend.item.query.dto.ItemFilterDTO;
import error.pirate.backend.item.query.dto.ItemSearchDTO;
import error.pirate.backend.item.query.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQueryService {

    private final ItemMapper itemMapper;

    public List<ItemSearchDTO> findItem(ItemFilterDTO itemFilterDTO) {
        int offset = itemFilterDTO.getSize() * (itemFilterDTO.getPage() - 1) ;

        return itemMapper.findItemsByFilter(
                itemFilterDTO.getItemName(),
                itemFilterDTO.getItemDivision(),
                itemFilterDTO.getItemExpirationHour(),
                itemFilterDTO.getSortBy(),
                itemFilterDTO.getSortDirection(),
                offset,
                itemFilterDTO.getSize()
        );
    }
}
