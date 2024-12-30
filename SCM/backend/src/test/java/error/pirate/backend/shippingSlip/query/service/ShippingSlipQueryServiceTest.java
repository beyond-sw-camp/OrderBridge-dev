package error.pirate.backend.shippingSlip.query.service;

import error.pirate.backend.shippingSlip.query.dto.ShippingSlipListRequest;
import error.pirate.backend.shippingSlip.query.dto.ShippingSlipSituationRequest;
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
class ShippingSlipQueryServiceTest {

    @Autowired
    private ShippingSlipQueryService shippingSlipQueryService;

    private static Stream<Arguments> readShippingSlipListArguments() {
        return Stream.of(
                arguments(new ShippingSlipListRequest(1, 10, null, null, null)),
                arguments(new ShippingSlipListRequest(2, 10, null, null, null)),
                arguments(new ShippingSlipListRequest(1, 10, LocalDate.of(2024, 12, 20), null, null)),
                arguments(new ShippingSlipListRequest(1, 10, null, LocalDate.of(2024, 12, 25), null)),
                arguments(new ShippingSlipListRequest(1, 10, LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), null)),
                arguments(new ShippingSlipListRequest(1, 10, null, null, "대한항공"))
        );
    }

    @DisplayName("출하전표 목록 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingSlipListArguments")
    void readShippingSlipListTest(
            ShippingSlipListRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingSlipQueryService.readShippingSlipList(request));
    }

    @DisplayName("출하전표 엑셀 다운")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingSlipListArguments")
    void shippingSlipExcelDownTest(
            ShippingSlipListRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingSlipQueryService.shippingSlipExcel(request));
    }

    private static Stream<Arguments> readShippingSlipArguments() {
        return Stream.of(
                arguments(1L),
                arguments(2L),
                arguments(3L),
                arguments(4L),
                arguments(5L)
        );
    }

    @DisplayName("출하전표 상세 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingSlipArguments")
    void readShippingSlipTest(
            long shippingSlipSeq
    ) {
        assertDoesNotThrow(
                () -> shippingSlipQueryService.readShippingSlip(shippingSlipSeq));
    }

    private static Stream<Arguments> readShippingSlipSituationArguments() {
        return Stream.of(
                arguments(new ShippingSlipSituationRequest(LocalDate.of(2024, 12, 20), null, null)),
                arguments(new ShippingSlipSituationRequest(null, LocalDate.of(2024, 12, 25), null)),
                arguments(new ShippingSlipSituationRequest(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), null)),
                arguments(new ShippingSlipSituationRequest(null, null, "대한항공"))
        );
    }

    @DisplayName("출하전표 현황 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readShippingSlipSituationArguments")
    void readShippingSlipSituationTest(
            ShippingSlipSituationRequest request
    ) {
        assertDoesNotThrow(
                () -> shippingSlipQueryService.readShippingSlipSituation(request));
    }
}