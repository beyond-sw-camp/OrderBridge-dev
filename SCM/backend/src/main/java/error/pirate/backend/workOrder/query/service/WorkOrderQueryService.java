package error.pirate.backend.workOrder.query.service;

import error.pirate.backend.workOrder.query.dto.WorkOrderListDTO;
import error.pirate.backend.workOrder.query.dto.WorkOrderListResponse;
import error.pirate.backend.workOrder.query.mapper.WorkOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkOrderQueryService {

    private final WorkOrderMapper workOrderMapper;

    /* 작업지시서 목록 조회 */
    @Transactional
    public WorkOrderListResponse readWorkOrderList(String warehouseName, String workOrderStatus,
                                                   LocalDateTime startDate, LocalDateTime endDate,
                                                   Integer page, Integer size) {
        log.info("-------------- 작업지시서 목록조회 서비스 진입 :목록조회 필터링 조건 - warehouseName: {}, workOrderStatus: {}, " +
                "startDate: {}, endDate: {} --------------", warehouseName, workOrderStatus, startDate, endDate);

        int offset = (page - 1) * size;

        // 총 개수
        Long totalItems = workOrderMapper.readWorkOrderListCount(warehouseName, workOrderStatus, startDate, endDate);
        // 총 페이지 수
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // 작업지시서 목록 조회
        List<WorkOrderListDTO> workOrderList = workOrderMapper.readWorkOrderList(warehouseName, workOrderStatus, startDate, endDate, page, size);

        log.info("-------------- readWorkOrderList 완료 - 페이지에 조회된 작업지시서 수 : {}, 총 작업지시서 수 : {}, 총 페이지 수 : {} --------------",
                                                                            workOrderList.size(), totalItems, totalPages);

        return WorkOrderListResponse.builder()
                .workOrderList(workOrderList)
                .currentPage(page)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
    }



}
