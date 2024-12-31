<script setup>
import {defineProps, ref, watch} from 'vue';
import dayjs from "dayjs";
import searchIcon from "@/assets/searchIcon.svg";

const props = defineProps({
  searchStartDate: {type: String, required: false}, // 시작 날짜
  searchEndDate: {type: String, required: false},   // 종료 날짜
  searchName: {type: String, required: false},      // 검색 조건 이름
  shippingSlipSituationList: {type: Array, required: true},       // 출하전표 현황 목록
  shippingSlipSituationTotal: {type: Number, required: true},       // 출하전표 총수량 총합계
  shippingAddressList: {type: Array, required: true},       // 출하주소 목록
});

const emit = defineEmits(['searchEvent', 'excelEvent']);

const startDate = ref(props.searchStartDate);
const endDate = ref(props.searchEndDate);
const clientName = ref(props.searchName);

watch([startDate, endDate], () => {
  search();
})

const search = () => {
  emit('searchEvent', {
    startDate: startDate.value,
    endDate: endDate.value,
    clientName: clientName.value,
  });
};

const excel = () => {
  emit('excelEvent');
}

// 상태 키로 값 반환
function findStatusValue(array, key) {
  for (const item of array) {
    if (item.key === key) {
      return item.value
    }
  }
}

// 인쇄
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
</script>

<template>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">출하일</p>
          <input type="date" v-model="startDate"/> ~ <input type="date" v-model="endDate"/>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">거래처명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="clientName"></b-form-input>
            <b-button variant="light" class="button" @click="search()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div class="d-flex justify-content-end mt-3">
        <b-button @click="excel()" variant="light" size="sm" class="button ms-2 mb-3">엑셀 다운로드</b-button>
        <b-button @click="printTable()" variant="light" size="sm" class="button ms-2 mb-3">인쇄</b-button>
      </div>
      <div id="print-area" class="content">
        <div class="table-container">
          <!-- 테이블 -->
          <table>
            <thead>
              <tr>
                <th>번호</th>
                <th>출하일</th>
                <th>출하전표명</th>
                <th>총수량</th>
                <th>거래처명</th>
                <th>출하주소</th>
                <th>출하전표 비고</th>
              </tr>
            </thead>
            <tbody v-if="shippingSlipSituationList.length > 0">
              <!-- 필터링된 결과 및 월별 합계 출력 -->
              <template v-for="(shippingSlipSituation, index) in shippingSlipSituationList"
                        :key="index">
                <tr v-if="shippingSlipSituation.shippingSlipShippingDate">
                  <td>{{ index+1 }}</td>
                  <td>{{ dayjs(shippingSlipSituation.shippingSlipShippingDate).format('YYYY/MM/DD HH:mm:ss') }}</td>
                  <td>{{ shippingSlipSituation.shippingSlipName }}</td>
                  <td>{{ shippingSlipSituation.shippingSlipTotalQuantity }} 개</td>
                  <td>{{ shippingSlipSituation.clientName}}</td>
                  <td>{{ findStatusValue(shippingAddressList, shippingSlipSituation.shippingAddress) }}</td>
                  <td>{{ shippingSlipSituation.shippingSlipNote }}</td>
                </tr>
                <tr v-else class="monthly-total">
                  <td>{{ index+1 }}</td>
                  <td>{{ shippingSlipSituation.shippingSlipShippingMonthDate }}</td>
                  <td> - </td>
                  <td>{{ shippingSlipSituation.shippingSlipTotalQuantitySum }} 개</td>
                  <td> - </td>
                  <td> - </td>
                  <td> - </td>
                </tr>
              </template>

            </tbody>
            <tbody v-else>
              <tr>
                <td colspan="7">해당 검색조건에 부합한 출하전표가 존재하지 않습니다</td>
              </tr>
            </tbody>
            <!-- 총합 -->
            <tfoot v-if="shippingSlipSituationTotal">
              <tr>
                <td colspan="3">총합</td>
                <td colspan="1">{{shippingSlipSituationTotal.shippingSlipTotalQuantitySum}} 개</td>
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

  /* 버튼 숨기기 */
  button {
    display: none;
  }
}
</style>