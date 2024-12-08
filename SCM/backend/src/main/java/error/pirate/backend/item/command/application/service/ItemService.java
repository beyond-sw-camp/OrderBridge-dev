package error.pirate.backend.item.command.application.service;

import error.pirate.backend.item.command.application.dto.ItemDTO;
import error.pirate.backend.item.command.application.dto.ItemSaveDTO;
import error.pirate.backend.item.command.application.dto.UpdateItemReqDTO;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import error.pirate.backend.item.command.Infrastructure.repository.ItemRepository;
import error.pirate.backend.item.command.Infrastructure.repository.ItemUnitRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemUnitRepository itemUnitRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Item createItem(ItemDTO itemDTO) {
        User user = userRepository.findById(itemDTO.getUserSeq())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userSeq: " + itemDTO.getUserSeq()));

        ItemUnit itemUnit = itemUnitRepository.findById(itemDTO.getItemUnitSeq())
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemUnitSeq: " + itemDTO.getItemUnitSeq()));


        ItemSaveDTO itemSaveDTO = modelMapper.map(itemDTO, ItemSaveDTO.class);
        itemSaveDTO.setUser(user);
        itemSaveDTO.setItemUnit(itemUnit);

        Item item = modelMapper.map(itemSaveDTO, Item.class);

        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemSeq, UpdateItemReqDTO updateItemReqDTO) {

        // userSeq는 나중에 토큰에서 빼올 것
        // Long loginUserSeq = CustomUtil.getUserSeq();

        Long loginUserSeq = 1L;

        // 기존 품목 조회
        Item existingItem = itemRepository.findById(itemSeq)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemSeq: " + itemSeq));

        // 연관 엔티티 조회
        User user = userRepository.findById(loginUserSeq)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userSeq: " + loginUserSeq));

        ItemUnit itemUnit = itemUnitRepository.findById(updateItemReqDTO.getItemUnitSeq())
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemUnitSeq: " + updateItemReqDTO.getItemUnitSeq()));

        // DTO를 기존 엔티티로 매핑
        ItemSaveDTO updateDTO = modelMapper.map(updateItemReqDTO, ItemSaveDTO.class);
        updateDTO.setUser(user);
        updateDTO.setItemUnit(itemUnit);

        modelMapper.map(updateDTO, existingItem);
    }
}
