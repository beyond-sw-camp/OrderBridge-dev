package error.pirate.backend.item.query.service;

import error.pirate.backend.item.command.application.dto.BomItemDTO;
import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
import error.pirate.backend.item.command.application.service.ItemService;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@Transactional
class ItemQueryServiceTest {
    @Autowired
    private ItemQueryService itemQueryService;

    private static Stream<Arguments> readItemParam() {

        return Stream.of(
                arguments(1L),
                arguments(2L),
                arguments(3L)
        );
    }

    @DisplayName("품목 조회")
    @ParameterizedTest
    @MethodSource("readItemParam")
    void createItemTest(Long itemSeq) {
        assertDoesNotThrow(() -> itemQueryService.readItem(itemSeq));
    }
}