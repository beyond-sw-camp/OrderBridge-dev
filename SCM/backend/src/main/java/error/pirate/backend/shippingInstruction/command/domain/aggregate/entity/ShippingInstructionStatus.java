package error.pirate.backend.shippingInstruction.command.domain.aggregate.entity;

public enum ShippingInstructionStatus {
    BEFORE, // 결재전
    AFTER,  // 결재후
    DELETE  // 삭제
}
