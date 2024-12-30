package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemDomainService {
    private final ItemRepository itemRepository;

    public Item findById(Long itemSeq) {
        return itemRepository.findById(itemSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
    }
}
