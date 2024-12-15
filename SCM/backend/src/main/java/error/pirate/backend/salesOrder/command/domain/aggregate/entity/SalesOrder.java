package error.pirate.backend.salesOrder.command.domain.aggregate.entity;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.quotation.command.domain.aggregate.entity.Quotation;
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
@Table(name = "tb_sales_order") // 주문서
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesOrderSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationSeq")
    private Quotation quotation; // 견적서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 주문서 담당자 번호

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientSeq")
    private Client client; // 거래처 번호

    private String salesOrderName; // 주문서 명

    @Enumerated(EnumType.STRING)
    private SalesOrderStatus salesOrderStatus; // 주문서 상태

    @CreatedDate
    private LocalDateTime salesOrderRegDate; // 주문서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime salesOrderModDate; // 주문서 수정일

    private LocalDateTime salesOrderOrderDate; // 주문서 주문일

    private LocalDateTime salesOrderDueDate; // 주문서 물품 납기일

    private Integer salesOrderExtendedPrice; // 주문서 총 금액

    private Integer salesOrderTotalQuantity; // 주문서 총 수량

    private String salesOrderNote; // 주문서 비고

    public void updateSalesOrderStatus(SalesOrderStatus salesOrderStatus) {
        this.salesOrderStatus = salesOrderStatus;
    }

    // 상태를 변경하는 메소드
    public void updateStatus(String status) {
        this.salesOrderStatus = SalesOrderStatus.valueOf(status);
    }

    public SalesOrder(Quotation quotation, User user, Client client, String salesOrderName,
                      LocalDateTime salesOrderOrderDate, LocalDateTime salesOrderDueDate, String salesOrderNote) {
        this.quotation = quotation;
        this.user = user;
        this.client = client;
        this.salesOrderName = salesOrderName;
        this.salesOrderStatus = SalesOrderStatus.BEFORE;
        this.salesOrderOrderDate = salesOrderOrderDate;
        this.salesOrderDueDate = salesOrderDueDate;
        this.salesOrderNote = salesOrderNote;
    }

    public void updateSalesOrder(LocalDateTime salesOrderOrderDate, LocalDateTime salesOrderDueDate,
                                 Client client, User user, String salesOrderNote,
                                 int salesOrderExtendedPrice, int salesOrderTotalQuantity) {
        if (salesOrderOrderDate != null) { this.salesOrderOrderDate = salesOrderOrderDate; }
        if (salesOrderDueDate != null) { this.salesOrderDueDate = salesOrderDueDate; }
        if (client != null) { this.client = client; }
        if (user != null) { this.user = user; }
        if (salesOrderNote != null) { this.salesOrderNote = salesOrderNote; }
        this.salesOrderExtendedPrice = salesOrderExtendedPrice;
        this.salesOrderTotalQuantity = salesOrderTotalQuantity;
    }
}
