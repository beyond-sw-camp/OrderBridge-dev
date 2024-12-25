package error.pirate.backend.client.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateRequest {

    private Long userSeq; // 거래처 등록 회원 ID
    private String clientRegistrationNo; // 거래처 사업자 등록번호
    private String clientName; // 거래처 이름
    private String clientPhoneNo; // 거래처 전화번호
    private String clientEmail; // 거래처 이메일
    private String clientRepresentative; //대표자
}

