package error.pirate.backend.shippingInstruction.command.application.service;

import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemRequest;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingAddress;
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
                                LocalDateTime.of(2024, 12, 31, 12, 0, 0),
                                51L,
                                ShippingAddress.GATE_7,
                                "첫번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemRequest(1L, 50, "출하지시서 물품1"),
                                        new ShippingInstructionItemRequest(2L, 50, "출하지시서 물품2"),
                                        new ShippingInstructionItemRequest(3L, 500, "출하지시서 물품3"),
                                        new ShippingInstructionItemRequest(4L, 500, "출하지시서 물품4")
                                )
                        ),
                        "jh"
                ),
                arguments(
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 31, 10, 0, 0),
                                51L,
                                ShippingAddress.GATE_3,
                                "두번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemRequest(1L, 70, "출하지시서 물품1"),
                                        new ShippingInstructionItemRequest(2L, 70, "출하지시서 물품2"),
                                        new ShippingInstructionItemRequest(3L, 300, "출하지시서 물품3"),
                                        new ShippingInstructionItemRequest(4L, 300, "출하지시서 물품4")
                                )
                        ),
                        "jh"
                )
        );
    }

    @DisplayName("출하지시서 등록")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("createShippingInstructionArguments")
    void readShippingInstructionListTest(
            ShippingInstructionRequest request, String userNo
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.createShippingInstruction(request, userNo));
    }

    private static Stream<Arguments> updateShippingInstructionArguments() {
        return Stream.of(
                arguments(
                        77L,
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 31, 10, 0, 0),
                                51L,
                                ShippingAddress.GATE_8,
                                "두번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemRequest(1L, 70, "출하지시서 물품1"),
                                        new ShippingInstructionItemRequest(2L, 70, "출하지시서 물품2"),
                                        new ShippingInstructionItemRequest(3L, 300, "출하지시서 물품3"),
                                        new ShippingInstructionItemRequest(4L, 300, "출하지시서 물품4")
                                )
                        ),
                        "jh"
                ),
                arguments(
                        78L,
                        new ShippingInstructionRequest(
                                LocalDateTime.of(2024, 12, 31, 12, 0, 0),
                                51L,
                                ShippingAddress.GATE_4,
                                "첫번째 출하지시서",
                                List.of(
                                        new ShippingInstructionItemRequest(1L, 50, "출하지시서 물품1"),
                                        new ShippingInstructionItemRequest(2L, 50, "출하지시서 물품2"),
                                        new ShippingInstructionItemRequest(3L, 500, "출하지시서 물품3"),
                                        new ShippingInstructionItemRequest(4L, 500, "출하지시서 물품4")
                                )
                        ),
                        "jh"
                )
        );
    }

    @DisplayName("출하지시서 수정")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("updateShippingInstructionArguments")
    void updateShippingInstructionTest(
            Long shippingInstructionSeq, ShippingInstructionRequest request, String userNo
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.updateShippingInstruction(
                        shippingInstructionSeq, request, userNo));
    }

    private static Stream<Arguments> updateShippingInstructionStateArguments() {
        return Stream.of(
                arguments(77L),
                arguments(78L)
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
                arguments(77L, "jh"),
                arguments(78L, "jh")
        );
    }

    @DisplayName("출하지시서 삭제")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("deleteShippingInstructionArguments")
    void deleteShippingInstructionTest(Long shippingInstructionSeq, String userNo) {
        assertDoesNotThrow(
                () -> shippingInstructionApplicationService.deleteShippingInstruction(shippingInstructionSeq, userNo)
        );
    }
}