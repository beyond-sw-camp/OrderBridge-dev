package error.pirate.backend.shippingInstruction.query.service;

import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionListRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class ShippingInstructionQueryServiceTest {

    @Autowired
    private ShippingInstructionQueryService shippingInstructionQueryService;

    private static Stream<Arguments> getShippingInstructionListArguments() {
        return Stream.of(
                arguments(new ShippingInstructionListRequest(1, 10, null, null, null, null)),
                arguments(new ShippingInstructionListRequest(2, 10, null, null, null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, LocalDate.of(2024, 12, 20), null, null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, null, LocalDate.of(2024, 12, 25), null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), null, null)),
                arguments(new ShippingInstructionListRequest(1, 10, null, null, "대한항공", null)),
                arguments(new ShippingInstructionListRequest(1, 10, null, null, null, "결재전")),
                arguments(new ShippingInstructionListRequest(1, 10, null, null, null, "결재후"))
        );
    }

    @DisplayName("출하지시서 목록 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("getShippingInstructionListArguments")
    void readShippingInstructionListTest(
            ShippingInstructionListRequest request
    ){
        assertDoesNotThrow(
                () -> shippingInstructionQueryService.readShippingInstructionList(request));
    }

    private static Stream<Arguments> getShippingInstructionArguments() {
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
    @MethodSource("getShippingInstructionArguments")
    void readShippingInstructionTest(
            long shippingInstructionSeq
    ){
        assertDoesNotThrow(
                () -> shippingInstructionQueryService.readShippingInstruction(shippingInstructionSeq));
    }
}