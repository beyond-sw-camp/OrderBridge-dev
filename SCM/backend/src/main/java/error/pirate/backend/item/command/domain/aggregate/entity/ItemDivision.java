package error.pirate.backend.item.command.domain.aggregate.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ItemDivision {
    FINISHED("완제품"),
    PART("부재료"),
    SUB("원재료");

    private final String description;

    @Getter
    public static class ItemDivisionResponse {
        private final String key;
        private final String value;

        public ItemDivisionResponse(String key, ItemDivision itemDivision) {
            this.key = key;
            this.value = itemDivision.getDescription();
        }
    }

    public static List<ItemDivision.ItemDivisionResponse> readItemDivisionList() {
        return Arrays.stream(ItemDivision.class.getEnumConstants())
                .map(key ->
                        new ItemDivision.ItemDivisionResponse(key.toString(), ItemDivision.valueOf(key.toString())))
                .toList();
    }
}
