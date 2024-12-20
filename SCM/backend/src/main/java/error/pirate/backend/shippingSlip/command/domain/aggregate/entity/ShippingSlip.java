package error.pirate.backend.shippingSlip.command.domain.aggregate.entity;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingAddress;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_shipping_slip") // 출하전표
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ShippingSlip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingSlipSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingInstructionSeq")
    private ShippingInstruction shippingInstruction; // 출하 지시서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 출하전표 담당자

    private String shippingSlipName; // 출하전표 명

    @Enumerated(EnumType.STRING)
    @Column(name = "shippingSlipAddress")
    private ShippingAddress shippingAddress; // 출하 주소

    @Enumerated(EnumType.STRING)
    private ShippingSlipStatus shippingSlipStatus; // 출하전표 상태

    @CreatedDate
    private LocalDateTime shippingSlipRegDate; // 출하전표 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime shippingSlipModDate; // 출하전표 수정일

    private LocalDateTime shippingSlipShippingDate; // 출하전표 출하일

    private Integer shippingSlipTotalQuantity; // 출하전표 총 수량

    private String shippingSlipNote; // 출하전표 비고

    private ShippingSlip(ShippingInstruction shippingInstruction, User user, String shippingSlipName, ShippingAddress shippingAddress, LocalDateTime shippingSlipShippingDate, int itemTotalQuantity, String shippingSlipNote){
        this.shippingInstruction = shippingInstruction;
        this.user = user;
        this.shippingSlipName = shippingSlipName;
        this.shippingAddress = shippingAddress;
        this.shippingSlipShippingDate = shippingSlipShippingDate;
        this.shippingSlipTotalQuantity = itemTotalQuantity;
        this.shippingSlipNote = shippingSlipNote;
    }

    public static ShippingSlip create(ShippingInstruction shippingInstruction, User user, String shippingSlipName, ShippingAddress shippingSlipAddress, LocalDateTime shippingSlipShippingDate, int itemTotalQuantity, String shippingSlipNote) {
        return new ShippingSlip(shippingInstruction, user, shippingSlipName, shippingSlipAddress, shippingSlipShippingDate, itemTotalQuantity, shippingSlipNote);
    }

    // 다른 필드를 변경하는 메소드
    public void update(ShippingInstruction shippingInstruction, User user, ShippingAddress shippingAddress, LocalDateTime shippingSlipShippingDate, int itemTotalQuantity, String shippingSlipNote) {
        this.shippingInstruction = shippingInstruction;
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.shippingSlipShippingDate = shippingSlipShippingDate;
        this.shippingSlipTotalQuantity = itemTotalQuantity;
        this.shippingSlipNote = shippingSlipNote;
    }

    // 상태를 변경하는 메소드
    public void updateStatus(String status) {
        this.shippingSlipStatus = ShippingSlipStatus.valueOf(status);
    }
}
