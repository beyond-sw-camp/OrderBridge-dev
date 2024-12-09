package error.pirate.backend.productionReceiving.query.service;

import error.pirate.backend.productionReceiving.command.application.service.ProductionReceivingService;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@Transactional
class ProductionReceivingQueryServiceTest {

    @Autowired
    ProductionReceivingQueryService productionReceivingQueryService;

    private static Stream<Arguments> readProductionReceivingParam() {
        return Stream.of(
                arguments(1L, "2024-12-09", "2024-12-12", "2024/12/09-1", "BEFORE", 0, 10),
                arguments(1L, null, null, "2024/12/09-1", "BEFORE", 0, 4),
                arguments(1L, null, "2024-12-12", "2024/12/09-1", "BEFORE", 0, 8),
                arguments(1L, "2024-12-09", "2024-12-12", null, "BEFORE", 0, 5),
                arguments(1L, "2024-12-09", "2024-12-12", "2024/12/09-1", null, 0, 1),
                arguments(1L, null, null, null, null, 0, 20)
        );
    }

    @DisplayName("생산입고 리스트 조회")
    @ParameterizedTest(autoCloseArguments = true)
    @MethodSource("readProductionReceivingParam")
    void readProductionReceiving(Long userSeq, LocalDate searchStartDate,
                                 LocalDate searchEndDate, String searchName,
                                 ProductionReceivingStatus searchStatus,
                                 int offset, int limit) {
        ProductionReceivingListRequest request = new ProductionReceivingListRequest(
            userSeq, searchStartDate, searchEndDate, searchName, searchStatus
        );

        Pageable pageable = PageRequest.of(offset, limit);
        assertDoesNotThrow(() -> productionReceivingQueryService.readProductionReceivingList(request, pageable));
    }
}