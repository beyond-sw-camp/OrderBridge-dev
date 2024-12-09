package error.pirate.backend.item.command.application.service;

import error.pirate.backend.BackendApplication;
import error.pirate.backend.item.command.application.dto.ItemDTO;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(classes = BackendApplication.class)
@Transactional
class ItemServiceTest {

    @Autowired
    private  ItemService itemService;

    private static Stream<Arguments> getCreateItemArguments() {
        return Stream.of(
                arguments(new ItemDTO(1L, 1L, "Test Item 1", ItemDivision.FINISHED, 720, "http://example.com/item1.jpg", 1000)),
                arguments(new ItemDTO(1L, 2L, "Test Item 2", ItemDivision.RAW, 1440, "http://example.com/item2.jpg", 2000)),
                arguments(new ItemDTO(2L, 1L, "Test Item 3", ItemDivision.PART, null, "http://example.com/item3.jpg", 500)),
                arguments(new ItemDTO(3L, 3L, "Test Item 4", ItemDivision.SUB, 0, null, 1500))
        );
    }

    @DisplayName("품목 등록 테스트 - 성공")
    @ParameterizedTest
    @MethodSource("getCreateItemArguments")
    void createItemTest(ItemDTO itemDTO) {
        assertDoesNotThrow(() -> itemService.createItem(itemDTO));
    }
}
