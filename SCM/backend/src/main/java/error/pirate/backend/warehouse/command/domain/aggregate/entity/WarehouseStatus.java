package error.pirate.backend.warehouse.command.domain.aggregate.entity;

public enum WarehouseStatus {
    ACTIVE("활성"),
    DELETED("삭제");

    private final String description;

    WarehouseStatus(String description) {
        this.description = description;
    }
}
