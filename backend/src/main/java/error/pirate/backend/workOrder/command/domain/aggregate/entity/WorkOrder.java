package error.pirate.backend.workOrder.command.domain.aggregate.entity;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_work_order") // 작업지시서
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkOrder {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long workOrderSeq;
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientSeq")
    private Client client; // 거래처(납품처)

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 작업지시서 담당자

    @CreatedDate
    private LocalDateTime workOrderRegDate; // 작업지시서 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime workOrderModDate; // 작업지시서 수정일

    private LocalDateTime workOrderIndicatedDate; // 작업지시서 지시일

    private LocalDateTime workOrderEndDate; // 작업지시서 완료일

    private LocalDateTime workOrderDueDate; // 작업지시서 납기일

    private int workOrderProductionQuantity; // 작업지시서 생산수량

    private WorkOrderStatus workOrderStatus; // 작업지시서 상태

    private String workOrderNote; // 작업지시서 비고
}
