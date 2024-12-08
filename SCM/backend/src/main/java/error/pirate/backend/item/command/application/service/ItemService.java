package error.pirate.backend.item.command.application.service;

import error.pirate.backend.item.command.application.dto.ItemDTO;
import error.pirate.backend.item.command.application.dto.ItemSaveDTO;
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
}
