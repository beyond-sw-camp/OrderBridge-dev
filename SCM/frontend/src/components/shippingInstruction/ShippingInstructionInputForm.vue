<script setup>
import {computed, defineProps, ref, watch} from "vue";
import searchIcon from '@/assets/searchIcon.svg'
import dayjs from "dayjs";

const props = defineProps({
  salesOrderList: {type: Array, required: true},       // 주문서 목록
  salesOrderStatusList: {type: Array, required: true},       // 주문서 상태 목록
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
  shippingAddressList: {type: Array, required: true},   // 출하주소 목록
});

const emit = defineEmits(
    ['pageEvent', 'salesOrderEvent']);

const formData = ref({
  salesOrderSeq: 0,
  shippingInstructionDate: '',
  salesOrder: '',
  client: '',
  address: '',
  note: '',
});
const pageNumber = ref(props.pageNumber);

watch(pageNumber, () => {
  emit('pageEvent', pageNumber);
})

const addClient = (index) => {
  formData.value.salesOrderSeq = props.salesOrderList[index].salesOrderSeq;
  formData.value.salesOrder = props.salesOrderList[index].salesOrderName;
  formData.value.client = props.salesOrderList[index].clientName;
  emit('salesOrderEvent', formData);
}

// addressList를 <b-form-select>의 options 형식으로 변환
const addressOptions = computed(() =>
    props.shippingAddressList.map((address) => ({
      value: address.key, // 고유 값 (key)
      text: address.value, // 표시되는 텍스트 (value)
    }))
);

// 상태 키로 값 반환
function findStatusValue(array, key) {
  for (const item of array) {
    if (item.key === key) {
      return item.value
    }
  }
}

// 부모 컴포넌트가 이 메서드를 호출해 데이터를 가져갈 수 있도록 노출
const getData  = () => {
  return formData;
};
defineExpose({ getData });
</script>

<template>
  <div class="col-6 d-flex flex-column">
    <b-form-group label-cols="4" label-cols-lg="2" label="출하예정일자" label-for="shippingInstructionDate">
      <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
      <input
          class="form-control form-control-sm w-75"
          type="datetime-local"
          id="shippingInstructionDate"
          v-model="formData.shippingInstructionDate"
          placeholder="출하지시일자를 입력해 주세요.">
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="주문서" label-for="salesOrder">
      <b-input-group class="w-75" size="sm">
        <b-form-input
            type="text"
            id="salesOrder"
            v-model="formData.salesOrder"
            placeholder="주문서를 선택해 주세요."
            readonly="">
        </b-form-input>
        <b-input-group-text>
          <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#SalesOrderModal"/>
        </b-input-group-text>
      </b-input-group>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처" label-for="client">
      <b-form-input
          class="w-75"
          size="sm"
          type="text"
          id="client"
          v-model="formData.client"
          placeholder="거래처"
          readonly="">
      </b-form-input>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="출하 주소" label-for="address">
      <b-form-select
          class="w-75"
          :options="addressOptions"
          size="sm"
          id="address"
          v-model="formData.address"
          placeholder="출하 주소를 입력해 주세요.">
      </b-form-select>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="출하지시 비고" label-for="note">
      <b-form-input
          type="text"
          size="sm"
          id="note"
          v-model="formData.note"
          placeholder="내용을 입력해 주세요.">
      </b-form-input>
    </b-form-group>
  </div>

  <!-- client Modal bootstrap -->
  <div class="modal fade" id="SalesOrderModal" tabindex="-1" aria-labelledby="SalesOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: auto"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-md-6">주문서</div>
              <div class="list-head col-md-2">거래처</div>
              <div class="list-head col-md-2">주문일</div>
              <div class="list-head col-md-2">상태</div>
            </div>
            <template v-if="salesOrderList.length > 0">
              <div v-for="(salesOrder, index) in salesOrderList"
                   :key="salesOrder.salesOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="addClient(index)">
                <div class="list-body col-md-6 left">
                  {{ salesOrder.salesOrderName }}
                  <br>
                  <div v-if="!salesOrder.itemName"><br></div>
                  <div v-else>{{ salesOrder.itemName }}</div>
                </div>
                <div class="list-body col-md-2">{{ salesOrder.clientName }}</div>
                <div class="list-body col-md-2">{{
                    dayjs(salesOrder.salesOrderOrderDate).format('YYYY/MM/DD')
                  }}
                </div>
                <div class="list-body col-md-2">{{ findStatusValue(salesOrderStatusList, salesOrder.salesOrderStatus) }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 주문서가 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination
              v-model="pageNumber"
              :totalRows="props.totalCount"
              :perPage="props.pageSize">
          </b-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
div {
  font-size: small;
}

.icon {
  width: 20px;
  height: 20px;
}

.list-headline {
  width: 90%;
  border-bottom: 1px solid black;
  margin-left: 1px;
  margin-bottom: 10px;
  padding: 10px 5px 10px 5px;
}

.list-head {
  text-align: center;
}

.list-line {
  width: 90%;
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
</style>