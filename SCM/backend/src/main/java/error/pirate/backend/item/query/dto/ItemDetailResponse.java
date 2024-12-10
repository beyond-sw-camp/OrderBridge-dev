package error.pirate.backend.item.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ItemDetailResponse {
    ItemDTO itemDTO;
    List<ItemDTO> childItemList;
    List<ItemInventoryDTO> itemInventoryList;
}
