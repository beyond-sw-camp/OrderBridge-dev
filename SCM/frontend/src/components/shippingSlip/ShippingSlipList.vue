<script setup>
import {computed, defineProps, ref, watch} from 'vue';
import searchIcon from "@/assets/searchIcon.svg";
import trashIcon from "@/assets/trashIcon.svg";
import editIcon from "@/assets/editIcon.svg";
import printIcon from "@/assets/printIcon.svg";
import dayjs from "dayjs";

const props = defineProps({
  searchStartDate: {type: String, required: false}, // 시작 날짜
  searchEndDate: {type: String, required: false},   // 종료 날짜
  searchName: {type: String, required: false},      // 검색 조건 이름
  shippingSlipList: {type: Array, required: true},       // 출하전표 목록
  shippingAddressList: {type: Array, required: true},       // 출하주소 목록
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
  expandShippingSlip: {type: Object, required: true},
  expandItemList: {type: Object, required: true},
  itemDivisionList: {type: Array, required: true},       // 물품 구분 목록
  clientHintList: {type: Array, required: true},      // 거래처명 목록
});

const emit = defineEmits(
    ['pageEvent', 'clientEvent', 'searchEvent','extendItemEvent', 'excelEvent']);

const startDate = ref(props.searchStartDate);
const endDate = ref(props.searchEndDate);
const pageNumber = ref(props.pageNumber);
const clientName = ref(props.searchName);

watch([startDate, endDate], () => {
  search();
})

watch(pageNumber, () => {
  emit('pageEvent', pageNumber);
})

watch(clientName, () => {
  emit('clientEvent', clientName);
})

const search = () => {
  emit('searchEvent', {
    startDate: startDate.value,
    endDate: endDate.value,
    clientName: clientName.value,
  });
};

// 선택한 Item 확장 | 축소
const itemExtend = (seq) => {
  emit('extendItemEvent', seq);
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

// 숫자 쉼표 삽입
function numberThree(number) {
  return `${number.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}`;
}

// 모달에 데이터 전달을 위한 함수
const printModalData = ref({});
const extendPrintModalData = ref({});
const itemPrint = (shippingSlip) => {
  printModalData.value = shippingSlip;
  extendPrintModalData.value = props.expandShippingSlip[shippingSlip.shippingSlipSeq];
  console.log(printModalData.value);
}

// 빈칸을 계산하는 computed 속성
const emptyRows = computed(() => {
  const totalRows = 10; // 최소 10행을 유지
  const currentDataCount = props.expandItemList[printModalData.value.shippingSlipSeq];
  // 배열, 객체 여부 확인
  if (Array.isArray(currentDataCount)) {
    return currentDataCount.length; // 배열이면 길이를 반환
  } else if (currentDataCount && typeof currentDataCount === 'object') {
    return Object.keys(currentDataCount).length; // 객체면 키 개수 반환
  }
  return currentDataCount < totalRows ? totalRows - currentDataCount : 0;
});

// 전체 수량 구하는 computed 속성
const totalQuantity = computed(() => {
  const data = props.expandItemList[printModalData.value.shippingSlipSeq];
  // 객체 라면
  if (data && typeof data === 'object') {
    return Object.values(data).reduce((sum, product) => sum + (product.shippingSlipItemQuantity || 0), 0);
  }
});

const printPage = () => {
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
            <b-button variant="light" class="button" @click="search()">
              <searchIcon class="icon"/>
            </b-button>
          </b-input-group>
          <div class="clientHint" style="position: absolute; z-index: 5;">
            <ul class="list-group">
              <template v-for="hint in clientHintList">
                <li class="list-group-item list-group-item-action" @click="clientName = hint">{{ hint }}</li>
              </template>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div>
        <div class="d-flex justify-content-between">
          <div>검색결과: {{ totalCount }}개</div>
          <div class="d-flex justify-content-end mt-3">
            <b-button @click="excel()" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
          </div>
        </div>
        <div class="list-headline row">
          <div class="list-headvalue col-md-8">출하전표명</div>
          <div class="list-headvalue col-md-2">거래처</div>
          <div class="list-headvalue col-md-2">출하일</div>
        </div>
        <template v-if="shippingSlipList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="(shippingSlip, index) in shippingSlipList"
                 :key="shippingSlip.shippingSlipSeq"
                 @click="itemExtend(shippingSlip.shippingSlipSeq)">
              <div class="list-line row" :id="'print-area-' + index">
                <div class="col-md-8">
                  <b>{{ shippingSlip.shippingSlipName }}</b>
                  <div v-if="!expandShippingSlip[shippingSlip.shippingSlipSeq]">
                    <div v-if="!shippingSlip.itemName"><br></div>
                    <div v-else>{{ shippingSlip.itemName }}</div>
                  </div>
                </div>
                <div class="list-value col-md-2">{{ shippingSlip.clientName }}</div>
                <div class="list-value col-md-2">{{
                    dayjs(shippingSlip.shippingSlipShippingDate).format('YYYY/MM/DD')
                  }}
                </div>

                <!-- 확장된 상세 정보 표시 -->
                <div class="d-flex justify-content-center">
                  <div v-if="expandShippingSlip[shippingSlip.shippingSlipSeq]"
                       class="col-md-11 mt-3">
                    <b>총수량 : </b>
                    {{ expandShippingSlip[shippingSlip.shippingSlipSeq].shippingSlipTotalQuantity }} 개<br>
                    <b>출하 주소 : </b>
                    {{ findStatusValue(shippingAddressList,
                      expandShippingSlip[shippingSlip.shippingSlipSeq].shippingAddress) }}
                    {{  }}<br>
                    <b>담당자 : </b>
                    {{ expandShippingSlip[shippingSlip.shippingSlipSeq].userName }}<br>
                    <b>출하일시 : </b>
                    {{ dayjs(expandShippingSlip[shippingSlip.shippingSlipSeq].shippingSlipShippingDate).format('YYYY/MM/DD HH:mm:ss') }}<br>
                    <div v-if="expandShippingSlip[shippingSlip.shippingSlipSeq].shippingSlipNote">
                      <b>출하전표 비고 :</b>
                      {{ expandShippingSlip[shippingSlip.shippingSlipSeq].shippingSlipNote }}<br>
                    </div>
                    <div v-else>
                      <b>출하전표 비고 :</b>없음<br>
                    </div>
                    <!-- 확장된 상세 품목 정보 표시-->
                    <div style="display:flex; flex-wrap: wrap;">
                      <template v-for="expandItem in expandItemList[shippingSlip.shippingSlipSeq]">
                        <div class="card item-card">
                          <img :src=expandItem.itemImageUrl class="card-img-top">
                          <div style="margin: 5px;">
                            <small>{{ findStatusValue(itemDivisionList, expandItem.itemDivision) }}</small>
                            <div style="display: flex; justify-content: space-between;">
                              <b style="font-size: medium;">{{ expandItem.itemName }}</b>
                            </div>
                            <small>{{ numberThree(expandItem.shippingSlipItemQuantity) }}개 </small>
                            <br><br>
                            <small v-if="expandItem.shippingSlipItemNote" style="margin-top: 20px;">
                              비고: {{ expandItem.shippingSlipItemNote }}</small>
                            <small v-else style="margin-top: 20px;">비고: 없음</small>
                          </div>
                        </div>
                      </template>
                    </div>
                    <div class="d-flex justify-content-end align-items-center">
                      <printIcon class="me-3 icon" data-bs-toggle="modal" data-bs-target="#PrintModal" @click.stop="itemPrint(shippingSlip)"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="pagination d-flex justify-content-center">
            <b-pagination
                v-model="pageNumber"
                :totalRows="props.totalCount"
                :perPage="props.pageSize">
            </b-pagination>
          </div>
        </template>
        <template v-else>
          <b-card-text class="no-list-text">해당 검색조건에 부합한 출하전표가 존재하지 않습니다.</b-card-text>
        </template>
      </div>
    </div>
  </div>
  <!-- print Modal bootstrap -->
  <div class="modal fade" id="PrintModal" tabindex="-1" aria-labelledby="PrintModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-body" id="print-area">
          <!-- 인쇄 미리보기 버튼 -->
          <div class="d-flex justify-content-between">
            <button class="button" @click="printPage">인쇄 미리보기</button>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="container mt-4">
            <h2 class="text-center">출하전표</h2>
            <br/><br/>
            <table class="table first-table" style="height: 140px">
              <tbody>
              <tr>
                <td class="align-content-center">출하전표명</td>
                <td class="color-column align-content-center">{{ printModalData.shippingSlipName }}</td>
              </tr>
              <tr>
                <td class="align-content-center">출하일</td>
                <td class="color-column align-content-center">{{ dayjs(printModalData.shippingSlipShippingDate).format('YYYY/MM/DD') }}</td>
              </tr>
              <tr>
                <td class="align-content-center">거래처</td>
                <td class="color-column align-content-center">{{ printModalData.clientName }}</td>
                <td class="align-content-center">담당자</td>
                <td class="color-column align-content-center">{{ extendPrintModalData.userName }}</td>
              </tr>
              <tr>
                <td class="align-content-center">출하주소</td>
                <td class="color-column align-content-center" colspan="3">
                  {{ findStatusValue(shippingAddressList, extendPrintModalData.shippingAddress) }}</td>
              </tr>
              </tbody>
            </table>
            <table class="table table-bordered second-table">
              <thead>
              <tr>
                <th>품목</th>
                <th>수량</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(itemInfo, index) in expandItemList[printModalData.shippingSlipSeq]" :key="'data-' + index">
                <td>{{ itemInfo.itemName }}</td>
                <td>{{ numberThree(itemInfo.shippingSlipItemQuantity) }}</td>
              </tr>
              <tr v-for="index in emptyRows" :key="'empty-' + index">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              </tbody>
              <tfoot>
              <tr>
                <td>합계</td>
                <td>{{ totalQuantity }}</td>
              </tr>
              </tfoot>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

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

.list-headline {
  border-bottom: 1px solid black;
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
}

.list-headvalue {
  text-align: center;
}

.list-line {
  width: 99%;
  border: 1px solid Silver;
  border-radius: 8px;
  margin-left: 1px;
  margin-top: 20px;
  padding: 10px 5px 10px 5px;
}

.list-value {
  text-align: center;
  margin: auto 0;
  overflow: hidden;
  word-break: keep-all;
}

.pagination {
  justify-items: center;
  margin-top: 20px;
}

.no-list-text {
  text-align: center;
  margin-top: 100px;
}

.icon {
  width: 20px;
  height: 20px;
}

.item-card {
  width: 220px;
  margin: 10px;
}

.card-img-top {
  max-height: 80px;
  object-fit: cover;
}

.table {
  text-align: center;
  border-collapse: collapse;
}

.color-column {
  background-color: #f5f5f5;
  border-radius: 15px;
}

/* 인쇄 시 적용되는 스타일 */
@media print {
  @page {margin:0 1.3cm}

  body {
    font-family: 'Abhaya Libre', serif;
    font-size: 12px;
    color-adjust: exact;
  }

  .content-to-print {
    padding: 0;
    border: none;
  }

  /* 인쇄 미리보기에서 버튼 숨기기 - 꼭 있어야함!!!! */
  button {
    display: none;
  }
}
</style>
