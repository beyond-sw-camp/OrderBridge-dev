package error.pirate.backend.warehouse.command.domain.aggregate.entity;

import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_warehouse") // 창고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Warehouse {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long warehouseSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 창고 관리자 번호

    private String warehouseName; // 창고명

    private WarehouseType warehouseType; // 창고 구분

    @CreatedDate
    private LocalDateTime warehouseRegDate; // 창고 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime warehouseModDate; // 창고 수정일

    private String warehouseNote; // 창고 비고
}
