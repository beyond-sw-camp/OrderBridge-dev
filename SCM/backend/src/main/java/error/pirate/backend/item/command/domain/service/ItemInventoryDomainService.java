package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemInventoryDomainService {

    public void checkInventoryForBomItems(List<BomItem> bomItems, int workOrderIndicatedQuantity) {
    }
}
