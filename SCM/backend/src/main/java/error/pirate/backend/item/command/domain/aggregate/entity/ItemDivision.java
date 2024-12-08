package error.pirate.backend.item.command.domain.aggregate.entity;

import lombok.Getter;

@Getter
public enum ItemDivision {
    FINISHED("완제품"),
    RAW("구성품"),
    PART("부재료"),
    SUB("원재료");
    private final String description;

    ItemDivision(String description) {
        this.description = description;
    }

}
