package error.pirate.backend.common;

import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Pagination {

    private int pageNo;  // 현재 페이지 번호

    private Integer limit = 10;  // 페이지당 데이터 수

    private Integer offset; // 조회 시작숫자 -1 (limit=10일 경우 11부터 가져옴)

    private String searchStartDate;

    private String searchEndDate;

    private String searchStatus;

    private String searchName;

    private int totalCount;

    private int totalPageNo;

    public String getSearchEndDate() {
        if(StringUtils.isNotEmpty(searchEndDate)) {
            return searchEndDate + " 23:59:59";
        }
        return null;
    }

    public int getOffset() {
        return (pageNo - 1) * limit;
    }

    public void responsePaging (int pageNo, int totalCount) {
        this.pageNo = pageNo;
        this.totalCount = totalCount;
        this.totalPageNo = (int) Math.ceil((double) totalCount / limit);
    }

}
