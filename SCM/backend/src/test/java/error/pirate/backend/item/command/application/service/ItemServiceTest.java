//package error.pirate.backend.item.command.application.service;
//
//import error.pirate.backend.BackendApplication;
//import error.pirate.backend.item.command.application.dto.BomItemDTO;
//import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
//import error.pirate.backend.item.command.application.dto.ItemDTO;
//import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
//import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//
//@SpringBootTest(classes = BackendApplication.class)
//@Transactional
//class ItemServiceTest {
//
//    @Autowired
//    private  ItemService itemService;
//
//    private static Stream<Arguments>  createItemArguments() {
//
//        List<BomItemDTO> bomItemList = new ArrayList<>();
//        bomItemList.add(new BomItemDTO(7L, 100));
//        bomItemList.add(new BomItemDTO(8L, 100));
//        bomItemList.add(new BomItemDTO(9L, 100));
//        bomItemList.add(new BomItemDTO(10L, 100));
//        bomItemList.add(new BomItemDTO(11L, 100));
//
//        return Stream.of(
//                arguments(new ItemCreateRequest(1L, 1L, "Test Item 1", ItemDivision.FINISHED, 720, "https://example.com/item1.jpg", 1000, "품목 비고1",null)),
//                arguments(new ItemCreateRequest(2L, 1L, "Test Item 3", ItemDivision.PART, null, "https://example.com/item3.jpg", 500, "품목 비고3", null)),
//                arguments(new ItemCreateRequest(3L, 3L, "Test Item 4", ItemDivision.SUB, 0, null, 1500, "품목 비고4", null)),
//                arguments(new ItemCreateRequest(3L, 2L, "볶음밥", ItemDivision.FINISHED, 72, null, 7500, "볶음밥 만들기~", bomItemList))
//        );
//    }
//
//    @DisplayName("품목 등록 테스트 - 성공")
//    @ParameterizedTest
//    @MethodSource("createItemArguments")
//    void createItemTest(ItemCreateRequest request) {
//        assertDoesNotThrow(() -> itemService.createItem(request));
//    }
//
//    private static Stream<Arguments> updateItemArguments() {
//
//        List<BomItemDTO> bomItemList = new ArrayList<>();
//        bomItemList.add(new BomItemDTO(7L, 100));
//        bomItemList.add(new BomItemDTO(8L, 100));
//        bomItemList.add(new BomItemDTO(9L, 100));
//        bomItemList.add(new BomItemDTO(10L, 100));
//        bomItemList.add(new BomItemDTO(11L, 100));
//
//        return Stream.of(
//                arguments(40L, new ItemCreateRequest(1L, 1L, "Test Item 1", ItemDivision.FINISHED, 720, "https://example.com/item1.jpg", 1000, "품목 비고1",null)),
//                arguments(21L, new ItemCreateRequest(2L, 1L, "Test Item 3", ItemDivision.PART, null, "https://example.com/item3.jpg", 500, "품목 비고3", null)),
//                arguments(22L, new ItemCreateRequest(3L, 3L, "Test Item 4", ItemDivision.SUB, 0, null, 1500, "품목 비고4", null)),
//                arguments(23L, new ItemCreateRequest(3L, 2L, "김치볶음밥", ItemDivision.FINISHED, 72, null, 12500, "김치볶음밥 만들기~", bomItemList))
//        );
//    }
//
//    @DisplayName("품목 등록 테스트 - 성공")
//    @ParameterizedTest
//    @MethodSource("updateItemArguments")
//    void updateItemTest(Long itemSeq, ItemUpdateRequest request) {
//        assertDoesNotThrow(() -> itemService.updateItem(itemSeq, request));
//    }
//}