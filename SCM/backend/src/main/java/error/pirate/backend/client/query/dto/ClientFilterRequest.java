package error.pirate.backend.client.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientFilterRequest {
    private String clientName;            // 거래처 이름
    private String clientRegistrationNo; // 사업자 등록번호
    private String sortBy;
    private String sortDirection;
}
