package error.pirate.backend.warehouse.command.domain.aggregate.entity;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.warehouse.command.application.dto.WarehouseUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_warehouse") // 창고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseSeq;

    // User 설정 메서드
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 창고 관리자 번호

    private String warehouseName; // 창고명

    @Enumerated(EnumType.STRING)
    private WarehouseType warehouseType; // 창고 구분

    @CreatedDate
    private LocalDateTime warehouseRegDate; // 창고 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime warehouseModDate; // 창고 수정일

    private String warehouseNote; // 창고 비고

    public void specifyUser(User user) {
        this.user = user;
    }
    public void updateWarehouse(WarehouseUpdateRequest request) {
        if (request.getWarehouseName() != null) {
            this.warehouseName = request.getWarehouseName();
        }
        if (request.getWarehouseType() != null) {
            this.warehouseType = request.getWarehouseType();
        }
        if (request.getWarehouseNote() != null) {
            this.warehouseNote = request.getWarehouseNote();
        }
    }
    @Enumerated(EnumType.STRING)
    private WarehouseStatus warehouseStatus = WarehouseStatus.ACTIVE;

    // 상태 변경 메서드
    public void delete() {
        this.warehouseStatus = WarehouseStatus.DELETED;
    }

}
