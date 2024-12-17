<script setup>
import {defineProps, ref, watch} from 'vue';
import searchIcon from "@/assets/searchIcon.svg";

const props = defineProps({
  searchStartDate: {type: String, required: false}, // 시작 날짜
  searchEndDate: {type: String, required: false},   // 종료 날짜
  searchName: {type: String, required: false},      // 검색 조건 이름
  shippingInstructionList: {type: Array, required: true},       // 출하지시서 목록
  shippingInstructionStatusList: {type: Array, required: true},       // 출하지시서 상태 목록
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
});

const emit = defineEmits(
    ['pageEvent', 'searchEvent', 'checkStatusEvent', 'extendItemEvent', 'registerEvent', 'excelEvent']);

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

const itemExtend = () => {
  emit('extendItemEvent');
};

const register = () => {
  emit('registerEvent');
}

const excel = () => {
  emit('excelEvent');
}

// 날짜 포맷 함수
const formatDate = (dateString) => {
  if (!dateString) return '';

  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');

  return `${year}/${month}/${day}`;
};

// 상태 포맷 함수
const formatStatus = (status) => {
  if (status === 'BEFORE') {
    return '결제 전';
  } else if (status === 'AFTER') {
    return '결제 후';
  }
  return status; // 상태가 다른 경우 그대로 반환
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
            <b-form-checkbox v-if="shippingInstructionStatus.key !== 'DELETE'"
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
          <div class="list-head col-6">출하지시서명</div>
          <div class="list-head col-2">거래처</div>
          <div class="list-head col-2">출하예정일</div>
          <div class="list-head col-2">상태</div>
        </div>
        <template v-if="shippingInstructionList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="shippingInstruction in shippingInstructionList"
                 :key="shippingInstruction.shippingInstructionSeq" class="list-line row" @click="itemExtend">
              <div class="list-body col-6 left">
                {{ shippingInstruction.shippingInstructionName }}
                <br>
                <div v-if="!shippingInstruction.itemName"><br></div>
                <div v-else>{{ shippingInstruction.itemName }}</div>
              </div>
              <div class="list-body col-2">{{ shippingInstruction.clientName }}</div>
              <div class="list-body col-2">{{
                  formatDate(shippingInstruction.shippingInstructionScheduledShipmentDate)
                }}
              </div>
              <div class="list-body col-2">{{ formatStatus(shippingInstruction.shippingInstructionStatus) }}</div>
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
