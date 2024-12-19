package error.pirate.backend.item.query.mapper;

import error.pirate.backend.item.query.dto.ItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<ItemResponse> findItemListByFilter(
            @Param("itemName") String itemName,
            @Param("itemDivisions") String itemDivisions,
            @Param("minExpirationHour") Integer minExpirationHour,
            @Param("maxExpirationHour") Integer maxExpirationHour,
            @Param("size") Integer size,
            @Param("offset") Integer offset
    );

    int countItemsByFilter(
            @Param("itemName") String itemName,
            @Param("itemDivisions") String itemDivisions,
            @Param("minExpirationHour") Integer minExpirationHour,
            @Param("maxExpirationHour") Integer maxExpirationHour
    );
}
