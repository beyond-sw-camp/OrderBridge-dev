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
  shippingInstructionList: {type: Array, required: true},       // 출하지시서 목록
  shippingInstructionStatusList: {type: Array, required: true},       // 출하지시서 상태 목록
  shippingAddressList: {type: Array, required: true},       // 출하주소 목록
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
  expandShippingInstruction: {type: Object, required: true},
  expandItemList: {type: Object, required: true},
  itemDivisionList: {type: Array, required: true},       // 물품 구분 목록
});

const emit = defineEmits(
    ['pageEvent', 'searchEvent', 'checkStatusEvent', 'extendItemEvent',
      'itemEditEvent', 'itemDeleteEvent', 'registerEvent', 'excelEvent', 'shippingSlipRegisterEvent']);

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

const search = () => {
  emit('searchEvent', {
    startDate: startDate.value,
    endDate: endDate.value,
    clientName: clientName.value,
  });
};

const check = (status) => {
  emit('checkStatusEvent', status);
};

// 선택한 Item 확장 | 축소
const itemExtend = (seq) => {
  emit('extendItemEvent', seq);
};

const register = () => {
  emit('registerEvent');
}

const excel = () => {
  emit('excelEvent');
}

const itemEdit = (seq) => {
  emit('itemEditEvent', seq);
}

const itemDelete = (seq) => {
  const result = confirm("정말 삭제하시겠습니까?");
  if (result) {
    emit('itemDeleteEvent', seq);
  }
}

const shippingSlipRegister = (seq) => {
  const result = confirm("출하전표를 등록하시겠습니까?");
  if (result) {
    emit('shippingSlipRegisterEvent', seq);
  }
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
const itemPrint = (shippingInstruction) => {
  printModalData.value = shippingInstruction;
  extendPrintModalData.value = props.expandShippingInstruction[shippingInstruction.shippingInstructionSeq];
  console.log(printModalData.value);
}

// 빈칸을 계산하는 computed 속성
const emptyRows = computed(() => {
  const totalRows = 10; // 최소 10행을 유지
  const currentDataCount = props.expandItemList[printModalData.value.shippingInstructionSeq];
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
  const data = props.expandItemList[printModalData.value.shippingInstructionSeq];
  // 객체 라면
  if (data && typeof data === 'object') {
    return Object.values(data).reduce((sum, product) => sum + (product.shippingInstructionItemQuantity || 0), 0);
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
          <p class="card-title">출하예정일</p>
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
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">출하지시서 상태</p>
          <template v-for="shippingInstructionStatus in props.shippingInstructionStatusList">
            <b-form-checkbox
                @click="check(shippingInstructionStatus.key)">{{ shippingInstructionStatus.value }}
            </b-form-checkbox>
          </template>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div>
        <div class="d-flex justify-content-between">
          <div>검색결과: {{ totalCount }}개</div>
          <div class="d-flex justify-content-end mt-3">
            <b-button @click="excel()" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
            <b-button @click="register()" variant="light" size="sm" class="button ms-2">출하지시서 등록</b-button>
          </div>
        </div>
        <div class="list-headline row">
          <div class="list-headvalue col-md-6">출하지시서명</div>
          <div class="list-headvalue col-md-2">거래처</div>
          <div class="list-headvalue col-md-2">출하예정일</div>
          <div class="list-headvalue col-md-2">상태</div>
        </div>
        <template v-if="shippingInstructionList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="(shippingInstruction, index) in shippingInstructionList"
                 :key="shippingInstruction.shippingInstructionSeq"
                 @click="itemExtend(shippingInstruction.shippingInstructionSeq)">
              <div class="list-line row" :id="'print-area-' + index">
                <div class="col-md-6">
                  <b>{{ shippingInstruction.shippingInstructionName }}</b>
                  <div v-if="!expandShippingInstruction[shippingInstruction.shippingInstructionSeq]">
                    <div v-if="!shippingInstruction.itemName"><br></div>
                    <div v-else>{{ shippingInstruction.itemName }}</div>
                  </div>
                </div>
                <div class="list-value col-md-2">{{ shippingInstruction.clientName }}</div>
                <div class="list-value col-md-2">{{
                    dayjs(shippingInstruction.shippingInstructionScheduledShipmentDate).format('YYYY/MM/DD')
                  }}
                </div>
                <div class="list-value col-md-2">
                  {{ findStatusValue(shippingInstructionStatusList, shippingInstruction.shippingInstructionStatus) }}
                </div>

                <!-- 확장된 상세 정보 표시 -->
                <div class="d-flex justify-content-center">
                  <div v-if="expandShippingInstruction[shippingInstruction.shippingInstructionSeq]"
                       class="col-md-11 mt-3">
                    <b>총수량 : </b>
                    {{
                      expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionTotalQuantity
                    }} 개<br>
                    <b>출하 주소 : </b>
                    {{
                      findStatusValue(shippingAddressList,
                          expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingAddress)
                    }}<br>
                    <b>담당자 : </b>
                    {{ expandShippingInstruction[shippingInstruction.shippingInstructionSeq].userName }}<br>
                    <b>출하예정일시 : </b>
                    {{
                      dayjs(expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionScheduledShipmentDate).format('YYYY/MM/DD HH:mm:ss')
                    }}<br>
                    <div
                        v-if="expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionNote">
                      <b>출하지시서 비고 :</b>
                      {{
                        expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionNote
                      }}<br>
                    </div>
                    <div v-else>
                      <b>출하지시서 비고 :</b>없음<br>
                    </div>
                    <!-- 확장된 상세 품목 정보 표시-->
                    <div style="display:flex; flex-wrap: wrap;">
                      <template v-for="expandItem in expandItemList[shippingInstruction.shippingInstructionSeq]">
                        <div class="card item-card">
                          <img :src=expandItem.itemImageUrl class="card-img-top">
                          <div style="margin: 5px;">
                            <small>{{ findStatusValue(itemDivisionList, expandItem.itemDivision) }}</small>
                            <div style="display: flex; justify-content: space-between;">
                              <b style="font-size: medium;">{{ expandItem.itemName }}</b>
                            </div>
                            <small>{{ numberThree(expandItem.shippingInstructionItemQuantity) }}개 </small>
                            <br><br>
                            <small v-if="expandItem.shippingInstructionItemNote" style="margin-top: 20px;">
                              비고: {{ expandItem.shippingInstructionItemNote }}</small>
                            <small v-else style="margin-top: 20px;">비고: 없음</small>
                          </div>
                        </div>
                      </template>
                    </div>
                    <div class="d-flex justify-content-end align-items-center">
                      <b-button v-if="shippingInstruction.shippingInstructionStatus === 'AFTER'" variant="light"
                                class="me-3 button" size="sm"
                                @click="shippingSlipRegister(shippingInstruction.shippingInstructionSeq)">출하전표 등록
                      </b-button>
                      <printIcon class="me-3 icon" data-bs-toggle="modal" data-bs-target="#PrintModal" @click.stop="itemPrint(shippingInstruction)"/>
                      <editIcon v-if="shippingInstruction.shippingInstructionStatus === 'BEFORE'" class="me-3 icon"
                                @click.stop="itemEdit(shippingInstruction.shippingInstructionSeq)"/>
                      <trashIcon v-if="shippingInstruction.shippingInstructionStatus === 'BEFORE'" class="icon"
                                 @click.stop="itemDelete(shippingInstruction.shippingInstructionSeq)"/>
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
          <b-card-text class="no-list-text">해당 검색조건에 부합한 출하지시서가 존재하지 않습니다.</b-card-text>
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
            <h2 class="text-center">출하지시서</h2>
            <br/><br/>
            <table class="table first-table" style="height: 140px">
              <tbody>
              <tr>
                <td class="align-content-center">출하지시서명</td>
                <td class="color-column align-content-center">{{ printModalData.shippingInstructionName }}</td>
              </tr>
              <tr>
                <td class="align-content-center">출하예정일</td>
                <td class="color-column align-content-center">{{ dayjs(printModalData.shippingInstructionScheduledShipmentDate).format('YYYY/MM/DD') }}</td>
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
              <tr v-for="(itemInfo, index) in expandItemList[printModalData.shippingInstructionSeq]" :key="'data-' + index">
                <td>{{ itemInfo.itemName }}</td>
                <td>{{ numberThree(itemInfo.shippingInstructionItemQuantity) }}</td>
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
