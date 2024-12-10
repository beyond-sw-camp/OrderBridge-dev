package error.pirate.backend.item.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.application.dto.ItemDTO;
import error.pirate.backend.item.command.application.dto.ItemCreateRequest;
import error.pirate.backend.item.command.application.dto.ItemUpdateRequest;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.item.command.domain.repository.ItemUnitRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemUnitRepository itemUnitRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Item createItem(ItemCreateRequest request) {
        User user = userRepository.findById(request.getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

        ItemUnit itemUnit = itemUnitRepository.findById(request.getItemUnitSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_UNIT_NOT_FOUND));

        ItemDTO itemDTO = modelMapper.map(request, ItemDTO.class);
        itemDTO.setUser(user);
        itemDTO.setItemUnit(itemUnit);

        Item item = modelMapper.map(itemDTO, Item.class);

        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemSeq, ItemUpdateRequest request) {

        // userSeq는 나중에 토큰에서 빼올 것
        // Long loginUserSeq = CustomUtil.getUserSeq();

        Long loginUserSeq = 1L;

        // 기존 품목 조회
        Item item = itemRepository.findById(itemSeq)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemSeq: " + itemSeq));

        ItemUnit itemUnit = itemUnitRepository.findById(request.getItemUnitSeq())
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemUnitSeq: " + request.getItemUnitSeq()));

        item.updateItem(itemUnit, request);
    }

    /* 물품 시퀀스들로 물품 리스트 불러오기 */
    @Transactional
    public List<Item> findAllById(List<Long> itemNameList) {
        return itemRepository.findAllById(itemNameList);
    }
}
