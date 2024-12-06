package error.pirate.backend.shippingInstruction.command.domain.aggregate.entity;

public enum ShippingInstructionStatus {

    BEFOREAPPROVAL("결재 전"),
    AFTERAPPROVAL("결재 후"),
    DELETE("삭제");

    private final String label;

    ShippingInstructionStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
