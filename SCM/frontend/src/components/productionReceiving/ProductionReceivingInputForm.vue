<script setup>
import {defineProps, ref, watch} from "vue";
import searchIcon from '@/assets/searchIcon.svg'

const props = defineProps({
  workOrderList: {type: Array, required: true},       // 출하지시서 목록
  totalCount: {type: Number, required: true},       // 검색 결과 총 개수
  pageNumber: {type: Number, required: true},       // 현재 페이지 번호
  pageSize: {type: Number, required: true},         // 페이지 사이즈
});

const emit = defineEmits(
    ['pageEvent', 'workOrderEvent']);

const productionWarehouseName = ref('');
const formData = ref({
  productionWarehouseSeq: 0,
  storeWarehouseSeq: 0,
  userSeq: 0,
  workOrderSeq: 0,
  productionReceivingName: '',
  productionReceivingExtendedPrice: 0,
  productionReceivingNote: '',
  productionReceivingReceiptDate: '',
});
const pageNumber = ref(props.pageNumber);

watch(pageNumber, () => {
  emit('pageEvent', pageNumber);
})

const addClient = (index) => {
  formData.value.salesOrderSeq = props.workOrderList[index].salesOrderSeq;
  formData.value.salesOrder = props.workOrderList[index].salesOrderName;
  formData.value.client = props.workOrderList[index].clientName;
  emit('workOrderEvent', formData);
}

// 부모 컴포넌트가 이 메서드를 호출해 데이터를 가져갈 수 있도록 노출
const getData  = () => {
  return formData;
};

defineExpose({ getData });

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
  if (status === 'PRODUCTION_COMPLETE') {
    return '생산완료';
  }
  return status; // 상태가 다른 경우 그대로 반환
};
</script>

<template>
  <div class="col-6 d-flex flex-column">
    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="생산 공장" label-for="productionWarehouse">
      <b-input-group class="w-75" size="sm">
        <b-form-input
            type="text"
            id="productionWarehouse"
            v-model="productionWarehouseName"
            placeholder="생산 공장을 선택해 주세요."
            readonly="true">
        </b-form-input>
        <b-input-group-text>
          <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#ClientModal"/>
        </b-input-group-text>
      </b-input-group>
    </b-form-group>
    <b-form-group label-cols="4" label-cols-lg="2" label="생산입고일" label-for="quote-date">
      <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
      <input
          class="form-control form-control-sm w-75"
          type="datetime-local"
          id="quote-date"
          v-model="formData.shippingInstructionDate"
          placeholder="출하지시일자를 입력해 주세요.">
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="작업지시서" label-for="workOrder">
      <b-input-group class="w-75" size="sm">
        <b-form-input
            type="text"
            id="client"
            v-model="formData.salesOrder"
            placeholder="작업지시서를 선택해 주세요."
            readonly="">
        </b-form-input>
        <b-input-group-text>
          <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#ClientModal"/>
        </b-input-group-text>
      </b-input-group>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처" label-for="manager">
      <b-form-input
          class="w-75"
          size="sm"
          type="text"
          id="manager"
          v-model="formData.client"
          placeholder="거래처"
          readonly="">
      </b-form-input>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="출하 주소" label-for="note">
      <b-form-input
          class="w-75"
          type="text"
          size="sm"
          id="note"
          v-model="formData.address"
          placeholder="출하 주소를 입력해 주세요.">
      </b-form-input>
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

  <!-- production WarehouseModal bootstrap -->
  <div class="modal fade" id="productionWarehouseModal" tabindex="-1" aria-labelledby="productionWarehouseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">생산 공장 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">주문서</div>
              <div class="list-head col-2">거래처</div>
              <div class="list-head col-2">주문일</div>
              <div class="list-head col-2">상태</div>
            </div>
            <template v-if="workOrderList.length > 0">
              <div v-for="(workOrder, index) in workOrderList"
                   :key="workOrder.salesOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="addClient(index)">
                <div class="list-body col-6 left">
                  {{ workOrder.workOrderName }}
                  <br>
                  <div v-if="!workOrder.itemName"><br></div>
                  <div v-else>{{ workOrder.itemName }}</div>
                </div>
                <div class="list-body col-2">{{ workOrder.clientName }}</div>
                <div class="list-body col-2">{{
                    formatDate(workOrder.workOrderIndicatedDate)
                  }}
                </div>
                <div class="list-body col-2">{{ formatStatus(workOrder.workOrderStatus) }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 출하지시서가 존재하지 않습니다.</b-card-text>
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
  <!-- client Modal bootstrap -->
  <div class="modal fade" id="ClientModal" tabindex="-1" aria-labelledby="ClientModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">주문서</div>
              <div class="list-head col-2">거래처</div>
              <div class="list-head col-2">주문일</div>
              <div class="list-head col-2">상태</div>
            </div>
            <template v-if="workOrderList.length > 0">
              <div v-for="(workOrder, index) in workOrderList"
                   :key="workOrder.salesOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="addClient(index)">
                <div class="list-body col-6 left">
                  {{ workOrder.workOrderName }}
                  <br>
                  <div v-if="!workOrder.itemName"><br></div>
                  <div v-else>{{ workOrder.itemName }}</div>
                </div>
                <div class="list-body col-2">{{ workOrder.clientName }}</div>
                <div class="list-body col-2">{{
                    formatDate(workOrder.workOrderIndicatedDate)
                  }}
                </div>
                <div class="list-body col-2">{{ formatStatus(workOrder.workOrderStatus) }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 출하지시서가 존재하지 않습니다.</b-card-text>
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
  <!-- client Modal bootstrap -->
  <div class="modal fade" id="ClientModal" tabindex="-1" aria-labelledby="ClientModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">주문서</div>
              <div class="list-head col-2">거래처</div>
              <div class="list-head col-2">주문일</div>
              <div class="list-head col-2">상태</div>
            </div>
            <template v-if="workOrderList.length > 0">
              <div v-for="(workOrder, index) in workOrderList"
                   :key="workOrder.salesOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="addClient(index)">
                <div class="list-body col-6 left">
                  {{ workOrder.workOrderName }}
                  <br>
                  <div v-if="!workOrder.itemName"><br></div>
                  <div v-else>{{ workOrder.itemName }}</div>
                </div>
                <div class="list-body col-2">{{ workOrder.clientName }}</div>
                <div class="list-body col-2">{{
                    formatDate(workOrder.workOrderIndicatedDate)
                  }}
                </div>
                <div class="list-body col-2">{{ formatStatus(workOrder.workOrderStatus) }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 출하지시서가 존재하지 않습니다.</b-card-text>
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
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
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
  padding: 20px 40px 20px 20px;
}

.list-body {
  text-align: center;
  margin: auto 0;
}

.pagination {
  justify-content: center; /* 가로 중앙 정렬 */
  margin-top: 20px;
}
</style>