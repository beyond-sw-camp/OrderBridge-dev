<script setup>
import {defineProps, ref, watch} from 'vue';
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
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
  expandShippingInstruction: {type: Object, required: true},
  expandItemList: {type: Object, required: true},
});

const emit = defineEmits(
    ['pageEvent', 'searchEvent', 'checkStatusEvent', 'extendItemEvent',
      'itemEditEvent', 'itemDeleteEvent', 'registerEvent', 'excelEvent']);

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

// 상태 포맷 함수
const formatStatus = (status) => {
  if (status === 'BEFORE') {
    return '결재 전';
  } else if (status === 'AFTER') {
    return '결재 후';
  }
  return status; // 상태가 다른 경우 그대로 반환
};

// 품목 포맷 함수
const formatDivision = (divisionString) => {
  if (divisionString === 'FINISHED') {
    return '생산완료';
  } else if (divisionString === 'RAW') {
    return '구성품';
  } else if (divisionString === 'PART') {
    return '부재료';
  } else if (divisionString === 'SUB') {
    return '원재료';
  }
  return divisionString; // 품목가 다른 경우 그대로 반환
}

// 3개 단위로 나눈 데이터를 반환하는 함수
const getChunkedItems = (seq) => {
  const items = props.expandItemList[seq] || [];
  const chunkedItems = [];
  for (let i = 0; i < items.length; i += 3) {
    chunkedItems.push(items.slice(i, i + 3));
  }
  return chunkedItems;
};

// 인쇄 함수
const printItem = (index) => {
  const printContent = document.getElementById(`print-area-${index}`).innerHTML;
  const originalContent = document.body.innerHTML;

  // 선택된 영역만 표시
  document.body.innerHTML = printContent;

  window.print();

  // 원래 내용 복원
  document.body.innerHTML = originalContent;

  // Vue 리렌더링 방지
  location.reload();
};
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
          <div class="list-head col-md-6">출하지시서명</div>
          <div class="list-head col-md-2">거래처</div>
          <div class="list-head col-md-2">출하예정일</div>
          <div class="list-head col-md-2">상태</div>
        </div>
        <template v-if="shippingInstructionList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="(shippingInstruction, index) in shippingInstructionList"
                 :key="shippingInstruction.shippingInstructionSeq"
                 @click="itemExtend(shippingInstruction.shippingInstructionSeq)">
              <div class="list-line row" :id="'print-area-' + index">
                <div class="list-body col-md-6 left">
                  {{ shippingInstruction.shippingInstructionName }}
                  <div v-if="!expandShippingInstruction[shippingInstruction.shippingInstructionSeq]">
                    <div v-if="!shippingInstruction.itemName"><br></div>
                    <div v-else>{{ shippingInstruction.itemName }}</div>
                  </div>
                </div>
                <div class="list-body col-md-2">{{ shippingInstruction.clientName }}</div>
                <div class="list-body col-md-2">{{
                    dayjs(shippingInstruction.shippingInstructionScheduledShipmentDate).format('YYYY/MM/DD')
                  }}
                </div>
                <div class="list-body col-md-2">{{ formatStatus(shippingInstruction.shippingInstructionStatus) }}</div>

                <!-- 확장된 상세 정보 표시 -->
                <div class="d-flex justify-content-center">
                  <div v-if="expandShippingInstruction[shippingInstruction.shippingInstructionSeq]"
                       class="col-md-11 mt-3">
                    <p>총수량 : {{
                        expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionTotalQuantity
                      }} 개</p>
                    <p>출하 주소 : {{
                        expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionAddress
                      }}</p>
                    <p>담당자 : {{ expandShippingInstruction[shippingInstruction.shippingInstructionSeq].userName }}</p>
                    <p>출하예정일시 : {{
                        dayjs(expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionScheduledShipmentDate).format('YYYY/MM/DD HH:mm:ss')
                      }}</p>
                    <p v-if="expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionNote">
                      출하지시서 비고 :
                      {{
                        expandShippingInstruction[shippingInstruction.shippingInstructionSeq].shippingInstructionNote
                      }}
                    </p>
                    <!-- 확장된 상세 품목 정보 표시-->
                    <div v-for="(row, rowIndex) in getChunkedItems(shippingInstruction.shippingInstructionSeq)"
                         :key="rowIndex"
                         class="mb-3 d-flex flex-row">
                      <div style="max-height: 250px;"
                           v-for="(expandItem, index) in row"
                           :key="index"
                           class="me-5 col-md-3 d-flex flex-column border border-secondary rounded">
                        <b-img style="max-height: 100px;" src="https://picsum.photos/200/200" fluid
                               alt="Responsive image"></b-img>
                        <p class="ms-3">· 구분 : {{ formatDivision(expandItem.itemDivision) }}</p>
                        <p class="ms-3">· 품목 : {{ expandItem.itemName }}</p>
                        <p class="ms-3">· 수량 : {{ expandItem.shippingInstructionItemQuantity }} 개</p>
                        <p v-if="expandItem.shippingInstructionItemNote" class="ms-3">· 비고 :
                          {{ expandItem.shippingInstructionItemNote }}</p>
                        <p v-else class="ms-3">· 비고 : 없음</p>
                      </div>
                    </div>
                    <div class="d-flex justify-content-end align-items-center">
                      <b-button v-if="shippingInstruction.shippingInstructionStatus === 'AFTER'" variant="light" class="me-3 button" @click="">출하전표 등록</b-button>
                      <printIcon class="me-3 icon" @click.stop="printItem(index)"/>
                      <editIcon v-if="shippingInstruction.shippingInstructionStatus === 'BEFORE'" class="me-3 icon" @click.stop="itemEdit(shippingInstruction.shippingInstructionSeq)"/>
                      <trashIcon v-if="shippingInstruction.shippingInstructionStatus === 'BEFORE'" class="icon" @click.stop="itemDelete(shippingInstruction.shippingInstructionSeq)"/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="pagination">
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

.list-head {
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

.list-body {
  text-align: center;
  margin: auto 0;
}

.pagination {
  justify-content: center; /* 가로 중앙 정렬 */
  margin-top: 20px;
}

.left {
  text-align: left;
}

.no-list-text {
  text-align: center;
  margin-top: 10%;
}

.icon {
  width: 20px;
  height: 20px;
}
</style>
