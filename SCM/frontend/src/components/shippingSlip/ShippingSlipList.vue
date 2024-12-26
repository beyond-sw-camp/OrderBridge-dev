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
  shippingSlipList: {type: Array, required: true},       // 출하전표 목록
  shippingAddressList: {type: Array, required: true},       // 출하주소 목록
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
  expandShippingSlip: {type: Object, required: true},
  expandItemList: {type: Object, required: true},
  itemDivisionList: {type: Array, required: true},       // 물품 구분 목록
});

const emit = defineEmits(
    ['pageEvent', 'searchEvent','extendItemEvent', 'excelEvent']);

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
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div style="width: 90%;">
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
                      <printIcon class="me-3 icon" @click.stop="printItem(index)"/>
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
          <b-card-text class="no-list-text">해당 검색조건에 부합한 출하전표가 존재하지 않습니다.</b-card-text>
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

.pagenation {
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


</style>
