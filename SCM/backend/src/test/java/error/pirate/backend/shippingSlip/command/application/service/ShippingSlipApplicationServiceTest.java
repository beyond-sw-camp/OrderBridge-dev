package error.pirate.backend.shippingSlip.command.application.service;

import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
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
class ShippingSlipApplicationServiceTest {

    @Autowired
    private ShippingSlipApplicationService shippingSlipApplicationService;

    private static Stream<Arguments> createShippingSlipArguments() {
        return Stream.of(
                arguments(
                        new ShippingSlipRequest(
                                LocalDateTime.of(2024, 12, 31, 12, 0, 0),
                                79L,
                                "첫번째 출하전표",
                                List.of(
                                        new ShippingSlipItemRequest(1L, 50, "출하전표 물품1"),
                                        new ShippingSlipItemRequest(2L, 50, "출하전표 물품2"),
                                        new ShippingSlipItemRequest(3L, 500, "출하전표 물품3"),
                                        new ShippingSlipItemRequest(4L, 500, "출하전표 물품4")
                                )
                        ),
                        "jh"
                ),
                arguments(
                        new ShippingSlipRequest(
                                LocalDateTime.of(2024, 12, 31, 10, 0, 0),
                                80L,
                                "두번째 출하전표",
                                List.of(
                                        new ShippingSlipItemRequest(1L, 70, "출하전표 물품1"),
                                        new ShippingSlipItemRequest(2L, 70, "출하전표 물품2"),
                                        new ShippingSlipItemRequest(3L, 300, "출하전표 물품3"),
                                        new ShippingSlipItemRequest(4L, 300, "출하전표 물품4")
                                )
                        ),
                        "jh"
                )
        );
    }

    @DisplayName("출하전표 등록")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("createShippingSlipArguments")
    void readShippingSlipListTest(
            ShippingSlipRequest request, String userNo
    ) {
        assertDoesNotThrow(
                () -> shippingSlipApplicationService.createShippingSlip(request, userNo));
    }
}