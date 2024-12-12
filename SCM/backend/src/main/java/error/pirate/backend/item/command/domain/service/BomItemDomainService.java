package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BomItemDomainService {

    private final BomItemRepository bomItemRepository;

    public List<BomItem> findAllByParentItem(Item parentItem) {
        return bomItemRepository.findAllByParentItem(parentItem);
    }
}
