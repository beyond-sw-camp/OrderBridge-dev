package error.pirate.backend.client.query.mapper;

import error.pirate.backend.client.query.dto.ClientResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientMapper {
    List<ClientResponse> findClientListByFilter(
            @Param("clientName") String clientName,
            @Param("clientRegistrationNo") String clientRegistrationNo,
            @Param("offset") int offset,
            @Param("size") int size
    );

    int countClientsByFilter(
            @Param("clientName") String clientName,
            @Param("clientRegistrationNo") String clientRegistrationNo
    );
    ClientResponse findClientSeq(@Param("clientSeq") Long clientSeq);

    // 거래처 힌트 조회
    List<String> readClientHint(
            @Param("keyword") String keyword);
}
