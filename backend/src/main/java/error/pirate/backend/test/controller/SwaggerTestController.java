package error.pirate.backend.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Spring Boot Swagger 연동 (user)") // API 그룹화 (name : Tag 이름, description : Tag 설명)
@RestController
@RequestMapping("/swagger")
public class SwaggerTestController {

    @Operation(
            summary = "전체 회원 조회",
            description = "전체 회원 목록을 조회한다."
    )
    // 해당 API 작업 설명 (summary : 작업의 간단 요약, description : 작업에 대한 자세한 설명)
    @GetMapping("/userList")
    public ResponseEntity<?> findList() {
        return null;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원정보 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못 입력된 파라미터")
    })
    // API 응답에 대한 메타데이터 제공 (HTTP 응답 코드에 대한 설명, 헤더 등을 명시)
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {
        return null;
    }
}