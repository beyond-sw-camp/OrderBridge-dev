package error.pirate.backend.item.query.mapper;

import error.pirate.backend.item.query.dto.ItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<ItemResponse> findItemListByFilter(
            @Param("itemName") String itemName,
            @Param("itemDivision") String itemDivision,
            @Param("itemExpirationHour") Integer itemExpirationHour,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection,
            @Param("offset") int offset,
            @Param("size") int size
    );
}
