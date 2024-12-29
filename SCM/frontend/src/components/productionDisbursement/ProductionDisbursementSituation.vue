<script setup>
import axios from "@/axios"
import dayjs from "dayjs";
import searchIcon from "@/assets/searchIcon.svg";
import {onMounted, ref, watch} from "vue";

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchStore = ref(null);
const searchFactory = ref(null);
const productionDisbursementSituationList = ref([]);
const productionDisbursementSituationTotal = ref(null);

const fetchProductionDisbursementSituationList = async () => {
  try {
    const response = await axios.get(`productionDisbursement/currentSituation`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        storeName: searchStore.value,
        factoryName: searchFactory.value,
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });
    console.log(response.data);
    console.log(response.data.monthlySituations);

    productionDisbursementSituationTotal.value = response.data.totalQuantity;
    console.log(productionDisbursementSituationTotal.value)
    productionDisbursementSituationList.value = response.data.monthlySituations;
  } catch (error) {
    if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert(error.response.data.message);
    }
    console.error("생산불출 현황 불러오기 실패 :", error);
  }
};

const printTable = () => {
  const printContent = document.getElementById('print-area').innerHTML; // 특정 영역 가져오기
  const originalContent = document.body.innerHTML; // 현재 페이지 내용 저장

  // 페이지 내용을 특정 영역으로 교체
  document.body.innerHTML = printContent;

  // 인쇄
  window.print();

  // 원래 내용 복원
  document.body.innerHTML = originalContent;

  // SPA일 경우 Vue의 리렌더링 강제 호출
  location.reload(); // 상태를 새로고침하여 업데이트
}

// 생산불출 현황 엑셀 다운로드
const excelDown = async () => {
  try {
    const response = await axios.get(`productionDisbursement/situation/excelDownload`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        factoryName: searchFactory.value,
        storeName: searchStore.value,
      }, responseType: "blob"
    });
    const blob = new Blob([response.data], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;

    // 헤더 인식이 안될때 대비
    let fileName = '생산불출 현황 download.xlsx';
    const disposition = response.headers['content-disposition'];

    if (disposition) {
      const fileNameMatch = disposition.match(/filename="?([^"]+)"?/);
      if (fileNameMatch) {
        fileName = decodeURIComponent(fileNameMatch[1]);
      }
    }

    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('Excel 다운로드 실패:', error);
    if (error.response.data.errorCode === 'EXCEL_DOWN_ERROR_001') {
      alert(error.response.data.message);
    }
  }
};


onMounted(() => {
  fetchProductionDisbursementSituationList();
});

watch([searchStartDate, searchEndDate], () => {
  fetchProductionDisbursementSituationList();
})

</script>

<template>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산불출일</p>
          <input type="date"  v-model="searchStartDate"/> ~
          <input type="date"  v-model="searchEndDate"/>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">출고창고명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchStore"></b-form-input>
            <b-button variant="light" class="button" @click="fetchProductionDisbursementSituationList()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산공장명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchFactory"></b-form-input>
            <b-button variant="light" class="button" @click="fetchProductionDisbursementSituationList()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div class="d-flex justify-content-end mt-3">
        <b-button @click="excelDown()" variant="light" size="sm" class="button ms-2 mb-3">엑셀 다운로드</b-button>
        <b-button @click="printTable()" variant="light" size="sm" class="button ms-2 mb-3">인쇄</b-button>
      </div>
      <div id="print-area" class="content">
        <div class="table-container">
          <!-- 테이블 -->
          <table>
            <thead>
            <tr>
              <th>번호</th>
              <th>생산불출명</th>
              <th>생산불출일</th>
              <th>총불출량</th>
              <th>생산공장명</th>
              <th>출고창고명</th>
              <th>비고</th>
            </tr>
            </thead>
            <tbody v-if="productionDisbursementSituationList.length > 0">
            <!-- 필터링된 결과 및 월별 데이터 출력 -->
            <template v-for="(monthData, index) in productionDisbursementSituationList" :key="index">

              <!-- 생산불출 데이터 -->
              <tr v-for="(situation, idx) in monthData.situations" :key="idx">
                <td>{{ idx + 1 }}</td>
                <td>{{ situation.productionDisbursementName }}</td>
                <td>{{ dayjs(situation.productionDisbursementDepartureDate).format('YYYY-MM-DD') }}</td>
                <td>{{ situation.productionDisbursementTotalQuantity }} 개</td>
                <td>{{ situation.factoryName }}</td>
                <td>{{ situation.storeName }}</td>
                <td>{{ situation.productionDisbursementNote }}</td>
              </tr>
              <!-- 월별 합계 -->
              <tr class="monthly-total">
                <td colspan="2">월 합계</td>
                <td> <strong>{{ monthData.month }}</strong> </td>
                <td> - </td>
                <td> {{ monthData.monthlyTotalQuantity }} 개 </td>
                <td> - </td>
                <td> - </td>
              </tr>

            </template>

            </tbody>
            <tbody v-else>
            <tr>
              <td colspan="6">해당 검색조건에 부합한 생산불출이 존재하지 않습니다</td>
            </tr>
            </tbody>

            <!-- 총합 -->
            <tfoot v-if="productionDisbursementSituationTotal">
            <tr>
              <td colspan="2">총합</td>
              <td> - </td>
              <td> - </td>
              <td>{{ productionDisbursementSituationTotal }} 개</td>
              <td> - </td>
              <td> - </td>
              <td> - </td>
            </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.content{
  display: flex;
  justify-content: space-around;
  max-height: 400px; /* 스크롤바가 나타날 최대 높이 */
  overflow-y: auto; /* 수직 스크롤바 */
}


.table-container {
  width: 100%;
  overflow-x: auto; /* 가로 스크롤바 */
}

.search-input>input {
  width: 100%;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  padding: 10px 12px;
  font-size: 14px;
}

.search-input>img {
  position : absolute;
  width: 17px;
  top: 10px;
  right: 12px;
  margin: 0;
}

.table-container{
  border: 1px solid #000000;
}

table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}

th, td {
  padding: 10px;
  border: none;
}

thead th {
  border-bottom: 4px solid #AAAAAA; /* 헤더 아래쪽만 선 표시 */
}

tfoot tr {
  border-top: 4px solid #AAAAAA;
}

tfoot {
  font-weight: bold;
}

.monthly-total {
  font-weight: bold;
  border-top: 2px solid #AAAAAA;
  border-bottom: 1px solid #AAAAAA;
}

div {
  font-size: small;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.side-box {
  min-height: 100px;
  margin-top: 20px;
}

.card-title {
  font-size: medium;
  font-weight: bold;
}

.icon {
  width: 20px;
  height: 20px;
}

/* 인쇄 스타일 */
@media print {
  /* 인쇄할 영역만 표시 */
  body * {
    visibility: hidden; /* 전체 요소 숨기기 */
  }

  #print-area, #print-area * {
    visibility: visible; /* 특정 영역만 표시 */
  }

  #print-area {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
  }

  /* 페이지 여백 최소화 */
  @page {
    margin: 5mm; /* 페이지 여백 설정 */
  }

  /* 버튼 숨기기 */
  button {
    display: none;
  }
}

</style>