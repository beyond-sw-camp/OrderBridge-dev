package error.pirate.backend.item.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BomItemDTO {
    private Long itemSeq;
    private Integer bomChildItemQuantity;
}
