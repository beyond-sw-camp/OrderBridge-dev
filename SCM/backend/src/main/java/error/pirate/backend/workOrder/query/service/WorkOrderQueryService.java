package error.pirate.backend.workOrder.query.service;

import error.pirate.backend.workOrder.query.dto.WorkOrderListDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderQueryService {
    public Page<WorkOrderListDTO> readWorkOrderList() {
        return null;
    }
}
