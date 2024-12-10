package error.pirate.backend.item.command.domain.aggregate.entity;

import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
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
@Table(name = "tb_item") // 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq")
    private User user; // 품목 등록자

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemUnitSeq")
    private ItemUnit itemUnit; // 품목 단위

    private String itemName; // 품목명

    @Enumerated(EnumType.STRING)
    private ItemDivision itemDivision; // 품목 구분

    @CreatedDate
    private LocalDateTime itemRegDate; // 품목 등록일

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime itemModDate; // 품목 수정일

    private int itemExpirationHour; // 품목 유통기한(시간)

    private String itemImageUrl; // 품목 이미지 주소

    private Integer itemPrice; // 품목 단가

    private String itemNote;

    public void updateItem(ItemUnit itemUnit, ItemUpdateRequest request) {
        if(itemUnit != null) {
            this.itemUnit = itemUnit;
        }
        if(request.getItemName() != null) {
            this.itemName = request.getItemName();
        }
        if(request.getItemDivision() != null) {
            this.itemDivision = request.getItemDivision();
        }
        if(request.getItemExpirationHour() != null) {
            this.itemExpirationHour = request.getItemExpirationHour();
        }
        if(request.getItemImageUrl() != null) {
            this.itemImageUrl = request.getItemImageUrl();
        }
        if(request.getItemPrice() != null) {
            this.itemPrice = request.getItemPrice();
        }
        if(request.getItemNote() != null) {
            this.itemNote = request.getItemNote();
        }
    }
}
