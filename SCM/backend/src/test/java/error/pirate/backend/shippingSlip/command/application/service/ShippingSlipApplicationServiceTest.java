package error.pirate.backend.shippingSlip.command.application.service;

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
                                LocalDateTime.of(2024, 12, 15, 12, 0, 0),
                                10L,
                                "첫번째 출하전표",
                                List.of(
                                        new ShippingSlipItemRequest(1L, 100, "출하전표 물품1"),
                                        new ShippingSlipItemRequest(2L, 500, "출하전표 물품2"),
                                        new ShippingSlipItemRequest(3L, 5000, "출하전표 물품3")
                                )
                        )
                ),
                arguments(
                        new ShippingSlipRequest(
                                LocalDateTime.of(2024, 12, 12, 10, 0, 0),
                                20L,
                                "두번째 출하전표",
                                List.of(
                                        new ShippingSlipItemRequest(4L, 100, "출하전표 물품1"),
                                        new ShippingSlipItemRequest(5L, 500, "출하전표 물품2")
                                )
                        )
                )
        );
    }

    @DisplayName("출하전표 등록")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("createShippingSlipArguments")
    void readShippingSlipListTest(
            ShippingSlipRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingSlipApplicationService.createShippingSlip(request, userNo));
    }

    private static Stream<Arguments> updateShippingSlipArguments() {
        return Stream.of(
                arguments(
                        24L,
                        new ShippingSlipRequest(
                                LocalDateTime.of(2024, 12, 12, 10, 0, 0),
                                20L,
                                "두번째 출하전표",
                                List.of(
                                        new ShippingSlipItemRequest(4L, 100, "출하전표 물품1"),
                                        new ShippingSlipItemRequest(5L, 500, "출하전표 물품2")
                                )
                        )
                ),
                arguments(
                        25L,
                        new ShippingSlipRequest(
                                LocalDateTime.of(2024, 12, 15, 12, 0, 0),
                                10L,
                                "첫번째 출하전표",
                                List.of(
                                        new ShippingSlipItemRequest(1L, 100, "출하전표 물품1"),
                                        new ShippingSlipItemRequest(2L, 500, "출하전표 물품2"),
                                        new ShippingSlipItemRequest(3L, 5000, "출하전표 물품3")
                                )
                        )
                )
        );
    }

    @DisplayName("출하전표 수정")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("updateShippingSlipArguments")
    void updateShippingSlipTest(
            Long shippingSlipSeq, ShippingSlipRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingSlipApplicationService.updateShippingSlip(
                        shippingSlipSeq, request));
    }

    private static Stream<Arguments> deleteShippingSlipArguments() {
        return Stream.of(
                arguments(24L),
                arguments(25L)
        );
    }

    @DisplayName("출하전표 삭제")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("deleteShippingSlipArguments")
    void deleteShippingSlipTest(Long shippingSlipSeq) {
        assertDoesNotThrow(
                () -> shippingSlipApplicationService.deleteShippingSlip(shippingSlipSeq)
        );
    }
}