package error.pirate.backend.item.command.domain.aggregate.entity;

public enum ItemStatus {
    ACTIVE("활성"),
    DELETED("삭제");

    private final String description;

    ItemStatus(String description) {
        this.description = description;
    }
}
