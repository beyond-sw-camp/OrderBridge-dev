package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemDomainService {

    private final ItemRepository itemRepository;

    /* 물품 시퀀스들로 물품 리스트 불러오기 */
    public List<Item> findAllById(List<Long> itemNameList) {
        return itemRepository.findAllById(itemNameList);
    }
}
