package error.pirate.backend.invoice.command.domain.aggregate.entity;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
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
@Table(name = "tb_invoice") // 거래 명세서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "salesOrderSeq")
    private SalesOrder salesOrder; // 주문서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 거래 명세서 담당자

    private String invoiceName;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @CreatedDate
    private LocalDateTime invoiceRegDate; // 거래 명세서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime invoiceModDate; // 거래 명세서 수정일

    private LocalDateTime invoiceSaleDate; // 거래 명세서 판매일

    private Integer invoiceExtendedPrice; // 거래 명세서 총 금액

    private Integer invoiceTotalQuantity; // 거래 명세서 총 수량

    private String invoiceNote; // 거래 명세서 비고

    public Invoice(SalesOrder salesOrder, User user, String invoiceName, LocalDateTime invoiceSaleDate,
                   Integer invoiceExtendedPrice, Integer invoiceTotalQuantity, String invoiceNote) {
        this.salesOrder = salesOrder;
        this.user = user;
        this.invoiceName = invoiceName;
        this.invoiceSaleDate = invoiceSaleDate;
        this.invoiceExtendedPrice = invoiceExtendedPrice;
        this.invoiceTotalQuantity = invoiceTotalQuantity;
        this.invoiceNote = invoiceNote;
    }

    public void updateInvoice(User user, LocalDateTime invoiceSaleDate, String invoiceNote,
                              int invoiceExtendedPrice, int invoiceTotalQuantity) {
        if (user != null) this.user = user;
        if (invoiceSaleDate != null) this.invoiceSaleDate = invoiceSaleDate;
        if (invoiceNote != null) this.invoiceNote = invoiceNote;
        this.invoiceExtendedPrice = invoiceExtendedPrice;
        this.invoiceTotalQuantity = invoiceTotalQuantity;
    }

    public void delete() {
        this.invoiceStatus = InvoiceStatus.DELETE;
    }
}
