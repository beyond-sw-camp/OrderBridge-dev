package error.pirate.backend.shippingInstruction.command.application.service;

import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemDTO;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class ShippingInstructionApplicationServiceTest {

    @Autowired
    private ShippingInstructionApplicationService shippingInstructionApplicationService;

    private static Stream<Arguments> createShippingInstructionArguments() {
        return Stream.of(
                arguments(
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 15, 12, 0, 0),
                                3L,
                                "인천시",
                                "첫번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemDTO(1L, 100, "출하지시서 물품1"),
                                        new ShippingInstructionItemDTO(2L, 500, "출하지시서 물품2"),
                                        new ShippingInstructionItemDTO(3L, 5000, "출하지시서 물품3")
                                )
                        )
                ),
                arguments(
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 12, 10, 0, 0),
                                10L,
                                "부산시",
                                "두번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemDTO(4L, 100, "출하지시서 물품1"),
                                        new ShippingInstructionItemDTO(5L, 500, "출하지시서 물품2")
                                )
                        )
                )
        );
    }

    @DisplayName("출하지시서 등록")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("createShippingInstructionArguments")
    void readShippingInstructionListTest(
            ShippingInstructionRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.createShippingInstruction(request));
    }

    private static Stream<Arguments> updateShippingInstructionArguments() {
        return Stream.of(
                arguments(
                        34L,
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 12, 10, 0, 0),
                                10L,
                                "부산시",
                                "두번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemDTO(4L, 100, "출하지시서 물품1"),
                                        new ShippingInstructionItemDTO(5L, 500, "출하지시서 물품2")
                                )
                        )
                ),
                arguments(
                        35L,
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 15, 12, 0, 0),
                                3L,
                                "인천시",
                                "첫번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemDTO(1L, 100, "출하지시서 물품1"),
                                        new ShippingInstructionItemDTO(2L, 500, "출하지시서 물품2"),
                                        new ShippingInstructionItemDTO(3L, 5000, "출하지시서 물품3")
                                )
                        )
                )
        );
    }

    @DisplayName("출하지시서 수정")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("updateShippingInstructionArguments")
    void updateShippingInstructionTest(
            Long shippingInstructionSeq, ShippingInstructionRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.updateShippingInstruction(
                        shippingInstructionSeq, request));
    }

    private static Stream<Arguments> updateShippingInstructionStateArguments() {
        return Stream.of(
                arguments(34L),
                arguments(35L)
        );
    }

    @DisplayName("출하지시서 결재 상태 변경")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("updateShippingInstructionStateArguments")
    void updateShippingInstructionStatusTest(Long shippingInstructionSeq) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.updateShippingInstructionApprovalStatus(shippingInstructionSeq)
        );
    }

    private static Stream<Arguments> deleteShippingInstructionArguments() {
        return Stream.of(
                arguments(34L),
                arguments(35L)
        );
    }

    @DisplayName("출하지시서 삭제")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("deleteShippingInstructionArguments")
    void deleteShippingInstructionTest(Long shippingInstructionSeq) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.deleteShippingInstruction(shippingInstructionSeq)
        );
    }
}