package error.pirate.backend.shippingInstruction.query.service;

import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListRequest;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionSituationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class ShippingInstructionQueryServiceTest {

    @Autowired
    private ShippingInstructionQueryService shippingInstructionQueryService;

    private static Stream<Arguments> readShippingInstructionListArguments() {
        return Stream.of(
                arguments(new ShippingInstructionListRequest(1, 10, null, null, null, null)),
                arguments(new ShippingInstructionListRequest(2, 10, null, null, null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, LocalDate.of(2024, 12, 20), null, null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, null, LocalDate.of(2024, 12, 25), null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, null, null, "대한항공", null)),
                arguments(new ShippingInstructionListRequest(1, 10, null, null, null, "BEFORE")),
                arguments(new ShippingInstructionListRequest(1, 10, null, null, null, "AFTER"))
        );
    }

    @DisplayName("출하지시서 목록 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingInstructionListArguments")
    void readShippingInstructionListTest(
            ShippingInstructionListRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionQueryService.readShippingInstructionList(request));
    }

    private static Stream<Arguments> readShippingInstructionArguments() {
        return Stream.of(
                arguments(1L),
                arguments(2L),
                arguments(3L),
                arguments(4L),
                arguments(5L)
        );
    }

    @DisplayName("출하지시서 상세 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingInstructionArguments")
    void readShippingInstructionTest(
            long shippingInstructionSeq
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionQueryService.readShippingInstruction(shippingInstructionSeq));
    }

    private static Stream<Arguments> readShippingInstructionSituationArguments() {
        return Stream.of(
                arguments(new ShippingInstructionSituationRequest(LocalDate.of(2024, 12, 20), null, null)),
                arguments(new ShippingInstructionSituationRequest(null, LocalDate.of(2024, 12, 25), null)),
                arguments(new ShippingInstructionSituationRequest(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), null)),
                arguments(new ShippingInstructionSituationRequest(null, null, "대한항공"))
        );
    }

    @DisplayName("출하지시서 현황 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingInstructionSituationArguments")
    void readShippingInstructionSituationTest(
            ShippingInstructionSituationRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingInstructionQueryService.readShippingInstructionSituation(request));
    }
}