<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from '@/axios';
import router from '@/router/index.js';
import searchIcon from '@/assets/searchIcon.svg';
import dayjs from 'dayjs';
import { Modal } from 'bootstrap';

const salesOrderList = ref([]);
const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);

const warehouses = ref([]);
const salesOrderStatusList = ref([]);
const salesOrderIndicatedDate = ref('');

const stockStatusList = ref([]);
const selectedItem = ref({
  salesOrderItemSeq: null,
  isRegistered: false, // 등록 여부 초기화
});

const itemList = ref([]); // 주문서 품목 목록
// const selectedItem = ref(null); // 선택된 품목
const selectedSalesOrder = ref(false); // 주문서 선택 여부

// 폼 데이터
const formData = ref({
  salesOrder: '',
  salesOrderItemSeq: '',
  warehouseSeq: '',
  workOrderIndicatedDate: '',
  workOrderDueDate: '',
  workOrderNote: '',
});

watch(pageNumber, () => {
  fetchSalesOrderList();
})

// 주문서 목록 조회
const fetchSalesOrderList = async () => {
  try {
    const response = await axios.get('sales-order', {
      params: {
        salesOrderStatus: 'AFTER',
        page: pageNumber.value,
        size: pageSize.value,
      },
      paramsSerializer: (params) => {
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      },
    });
    console.log(response.data);

    salesOrderList.value = response.data.salesOrder;
    totalCount.value = response.data.totalSalesOrder;
  } catch (error) {
    console.error('주문서 불러오기 실패:', error);
  }
};

// 셀렉트박스로 창고목록 불러오기
const fetchWarehouses = async () => {
  try {
    const response = await axios.get('warehouse/all');

    const factory = response.data.filter(warehouse => warehouse.warehouseType === 'FACTORY');
    console.log(factory);
    warehouses.value = factory;
  } catch (error) {
    console.error('창고 목록 불러오기 실패:', error);
  }
};

// 주문서 상태 목록 요청
const fetchSalesOrderStatusList = async () => {
  try {
    const response = await axios.get(`sales-order/status`, {});

    salesOrderStatusList.value = response.data;

  } catch (error) {
    console.error("출하지시서 상태 목록 불러오기 실패 :", error);
  }
};

// 주문서 정보 채우기
const addDueDate = (index) => {
  const selectedOrder = salesOrderList.value[index];

  // 주문서 정보 채우기
  formData.value.salesOrderSeq = selectedOrder.salesOrderSeq;
  formData.value.salesOrder = selectedOrder.salesOrderName;

  if (selectedOrder && selectedOrder.salesOrderDueDate) {
    salesOrderIndicatedDate.value = selectedOrder.salesOrderDueDate || ''; // 납기일 설정
    console.log('납기일:', selectedOrder.salesOrderDueDate); // 납기일 확인
  } else {
    console.log('선택된 주문서에 납기일이 없습니다.');

  }
  console.log('선택된 주문서:', selectedOrder);

  // 선택된 주문서의 재고 정보 가져오기
  fetchSalesOrderItemStock(selectedOrder.salesOrderSeq);
}

const openSalesOrderModal = async () => {
  await fetchSalesOrderList(); // 주문서 목록 불러오기
  await fetchSalesOrderStatusList(); //상태 불러오기

  const modalElement = document.getElementById('SalesOrderModal');
  if (modalElement) {
    const modalInstance = new Modal(modalElement);
    modalInstance.show(); // 모달 표시
  }
};

onMounted(() => {
  fetchSalesOrderList();
  fetchWarehouses();
  fetchSalesOrderStatusList();
  fetchSalesOrderItemStock();
});

// 상태 키로 값 반환
function findStatusValue(array, key) {
  for (const item of array) {
    if (item.key === key) {
      return item.value
    }
  }
}

// 선택한 주문서의 품목 재고 조회
const fetchSalesOrderItemStock = async (salesOrderSeq) => {
  try {
    const response = await axios.get(`sales-order/${salesOrderSeq}/available-quantity`);

    stockStatusList.value = response.data;
    console.log('품목 재고 상태 목록:', stockStatusList.value);

  } catch (error) {
    console.error('품목 재고 조회 실패:', error);
    if (error.response.data.errorCode === 'SALES_ORDER_ITEM_ERROR_001') {
      alert(error.response.data.message);
    }
  }
};

// 발주서 작성으로 이동
const goToOrderPage = () => {
  router.push('/purchaseOrder/input');
};

// 선택한 주문서 품목 저장
const selectItem = async (index) => {
  console.log(index)
  const item = stockStatusList.value[index]; // 선택된 품목 저장
  console.log(item)

  selectedItem.value = {
    ...item,
    isRegistered: false, // 기본값
  };

  console.log('선택된 품목:', selectedItem.value);
  await fetchRegisteredItems();
};

// 주문서 품목의 작업지시서 등록여부 조회
const fetchRegisteredItems = async (salesOrderSeq) => {
  try {
    const response = await axios.get(`sales-order/${salesOrderSeq}/registered-items`);
    console.log(response.data);

    const registeredItemSeqs = response.data;
    stockStatusList.value = stockStatusList.value.map(item => ({
      ...item,
      isRegistered: registeredItemSeqs.includes(item.salesOrderItemSeq)
    }));

    console.log('등록 여부 조회된 품목 목록:', stockStatusList.value);

  } catch (error) {
    console.error('등록여부 조회 실패:', error);
  }
};

const writeWorkOrder = (item) => {
  if (selectedItem.value.isRegistered) {
    console.log(`이미 등록된 작업지시서입니다. 작업지시서를 수정합니다: ${item.itemName}`);
    // 수정 페이지로 이동하거나 수정 API 호출

  } else {
    console.log(`작업지시서를 등록합니다: ${item.itemName}`);
    // 등록 페이지로 이동하거나 등록 API 호출

  }
};


</script>


<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/workOrder')" variant="light" size="sm" class="button ms-2">목록</b-button>
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-6 d-flex flex-column">
      <!-- 주문서 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="주문서" label-for="salesOrder">
        <b-input-group class="w-75" size="sm"  @click="openSalesOrderModal"  data-bs-toggle="modal" data-bs-target="#salesOrderModal">
          <b-form-input type="text" id="salesOrder" placeholder="주문서를 선택해 주세요."  v-model="formData.salesOrder" readonly=""/>
          <b-input-group-text>
            <searchIcon class="icon"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
<!--      &lt;!&ndash; 작업지시일자 &ndash;&gt;-->
<!--      <b-form-group label-cols="4" label-cols-lg="2" label="작업지시일" label-for="workOrderIndicatedDate">-->
<!--        &lt;!&ndash;   b-form-input 에서  datetime-local 을 사용할 수 없음  &ndash;&gt;-->
<!--        <input class="form-control form-control-sm w-75" type="datetime-local" id="workOrderIndicatedDate"-->
<!--               v-model="formData.workOrderIndicatedDate" placeholder="작업지시일자를 선택해 주세요."/>-->
<!--      </b-form-group>-->
      <!-- 주문납기일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="주문납기일" label-for="salesOrderIndicatedDate">
        <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
        <input class="form-control form-control-sm w-75" type="date" id="salesOrderIndicatedDate"
               v-model="salesOrderIndicatedDate" readonly=""/>
      </b-form-group>
<!--      &lt;!&ndash; 작업 납기일자 &ndash;&gt;-->
<!--      <b-form-group label-cols="4" label-cols-lg="2" label="작업납기일" label-for="workOrderDueDate">-->
<!--        &lt;!&ndash;   b-form-input 에서  datetime-local 을 사용할 수 없음  &ndash;&gt;-->
<!--        <input class="form-control form-control-sm w-75" type="datetime-local" id="workOrderDueDate"-->
<!--               v-model="formData.workOrderDueDate" placeholder="목표납기일자를 선택해 주세요."/>-->
<!--      </b-form-group>-->

<!--      <b-form-group label-cols="4"  label-cols-lg="2" label-size="default" label="생산공장" label-for="warehouseSeq">-->
<!--        <b-form-select class="w-75" size="sm" id="warehouseSeq" v-model="formData.warehouseSeq">-->
<!--          <option value="">선택하세요</option>-->
<!--          <option v-for="warehouse in warehouses" :key="warehouse.warehouseSeq"-->
<!--                  :value="warehouse.warehouseSeq">{{ warehouse.warehouseName }}</option>-->
<!--        </b-form-select>-->
<!--      </b-form-group>-->

<!--      &lt;!&ndash; 작업지시서 비고 &ndash;&gt;-->
<!--      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="작업지시서 비고" label-for="workOrderNote">-->
<!--        <b-form-input class="w-75" size="sm" type="text" id="workOrderNote" v-model="formData.workOrderNote" placeholder="작업지시서 비고를 입력해주세요."/>-->
<!--      </b-form-group>-->
    </div>
  </div>

  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>

  <!-- 주문서 품목 표시-->
  <div v-if="stockStatusList.length > 0" class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">주문서 품목</h5>
      <!-- 품목 반복 -->
      <div v-for="item in stockStatusList" :key="item.salesOrderItemSeq" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
          <div class="col-md-8">
            <ul class="d-flex flex-wrap align-items-start">
              <li class="mb-3 col-md-12 d-flex align-items-center">· 품목명 : {{ item.itemName }}</li>
              <li class="mb-3 col-md-12 d-flex align-items-center">
                <span class="me-3 text-nowrap">· 품목 비고 : {{item.salesOrderItemNote}}</span>
              </li>
              <li class="mb-3 col-md-6">· 주문 수량 : {{ item.requiredQuantity }}</li>
              <li class="mb-3 col-md-6">· 생산가능 수량 : {{ item.availableQuantity }}</li>
              <li class="mb-3 col-md-6" v-if="!item.isStockEnough">· <span style="color: red">[재고 부족!]</span> 부족한 수량: {{ item.insufficientQuantity }}</li>
              <li class="mb-3 col-md-6">
                <b-button v-if="!item.isStockEnough" @click="goToOrderPage" variant="light" size="sm" class="button ms-2 mb-3" style="top: 10px; right: 10px;">발주하러 가기</b-button>
              </li>
            </ul>
          </div>
          <!-- 이미지 -->
<!--          <div class="col-md-4 d-flex justify-content-center align-items-center">-->
          <div class="col-md-4 d-flex flex-column justify-content-around align-items-center">
            <img :src="item.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
            <div>
<!--              <b-button @click="writeWorkOrder(item)" variant="light" size="sm" class="button ms-2 mb-3" style="top: 10px; right: 10px;">-->
              <b-button @click="selectItem(item)" variant="light" size="sm" class="button ms-2 mb-3" style="top: 10px; right: 10px;">
<!--                작업지시서 등록-->
<!--                {{ selectedItem.salesOrderItemSeq === item.salesOrderItemSeq && selectedItem.isRegistered ? '작업지시서 수정' : '작업지시서 등록' }}-->
                {{ item.isRegistered ? '작업지시서 수정' : '작업지시서 등록' }}
              </b-button>
            </div>
          </div>
          <!-- x 버튼 -->
<!--          <b-button @click="checkWorkOrder(workOrder.workOrderSeq)" variant="light" size="sm" class="position-absolute btn-close" style="top: 10px; right: 10px;"></b-button>-->
        </div>
      </div>
      <!-- 확인 버튼 -->
      <div class="mx-5 my-3 d-flex justify-content-end">
<!--        <b-button v-if="props" @click="updateProductionReceiving" variant="light" size="sm" class="button ms-2">수정</b-button>-->
<!--        <b-button v-else @click="createProductionReceiving" variant="light" size="sm" class="button ms-2">등록</b-button>-->
      </div>
    </div>
  </div>

<!--  <div v-if="stockStatusList.length > 0" class="d-flex justify-content-center">-->
<!--    <div class="col-md-10 d-flex flex-column">-->
<!--      <h5 class="px-4">작업지시서 등록</h5>-->
<!--      &lt;!&ndash; 작업 지시서 반복 &ndash;&gt;-->
<!--      <div v-for="workOrder in checkWorkOrderList" :key="workOrder.workOrderSeq" class="mx-5 my-3">-->
<!--        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">-->
<!--          &lt;!&ndash; 텍스트 정보 &ndash;&gt;-->
<!--          <div class="col-md-8">-->
<!--            <div class="d-flex justify-content-between align-items-center mb-4">-->
<!--              <h6 class="fw-bold">{{ workOrder.workOrderName }}</h6>-->
<!--            </div>-->
<!--            <ul class="d-flex flex-wrap align-items-start">-->
<!--              <li class="mb-3 col-md-6">· 납품처 : {{ workOrder.clientName }}</li>-->
<!--              <li class="mb-3 col-md-6">· 담당자명 : {{ workOrder.userName }}</li>-->
<!--              <li class="mb-3 col-md-6">· 생산창고명 : {{ workOrder.warehouseName }}</li>-->
<!--              <li class="mb-3 col-md-6">· 입고창고명 : {{ workOrder.warehouseName }}</li>-->
<!--              <li class="mb-3 col-md-6">· 품목명 : {{ workOrder.itemName }}</li>-->
<!--              <li class="mb-3 col-md-6">· 품목 가격 : {{ workOrder.itemPrice.toLocaleString() }} ₩</li>-->
<!--              <li class="mb-3 col-md-6">· 주문 개수 : {{ workOrder.workOrderIndicatedQuantity.toLocaleString() }}</li>-->
<!--              <li class="mb-3 col-md-6">· 주문 총액 : {{ workOrder.workOrderPrice }} ₩</li>-->
<!--              <li class="mb-3 col-md-12 d-flex align-items-center">-->
<!--                <span class="me-3 text-nowrap">· 품목 비고 : {{workOrder.itemNote}}</span>-->
<!--              </li>-->
<!--            </ul>-->
<!--          </div>-->
<!--          &lt;!&ndash; 이미지 &ndash;&gt;-->
<!--          <div class="col-md-4 d-flex justify-content-center align-items-center">-->
<!--            <img :src="workOrder.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">-->
<!--          </div>-->
<!--          &lt;!&ndash; x 버튼 &ndash;&gt;-->
<!--          <b-button @click="checkWorkOrder(workOrder.workOrderSeq)" variant="light" size="sm" class="position-absolute btn-close" style="top: 10px; right: 10px;"></b-button>-->
<!--        </div>-->
<!--      </div>-->
<!--      &lt;!&ndash; 확인 버튼 &ndash;&gt;-->
<!--      <div class="mx-5 my-3 d-flex justify-content-end">-->
<!--        <b-button v-if="props" @click="updateProductionReceiving" variant="light" size="sm" class="button ms-2">수정</b-button>-->
<!--        <b-button v-else @click="createProductionReceiving" variant="light" size="sm" class="button ms-2">등록</b-button>-->

<!--      </div>-->
<!--    </div>-->
<!--  </div>-->

  <!-- 주문서 Modal -->
  <div class="modal fade" id="SalesOrderModal" tabindex="-1" aria-labelledby="SalesOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">주문서 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: auto"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-md-6">주문서</div>
              <div class="list-head col-md-2">주문일</div>
              <div class="list-head col-md-2">납기일</div>
              <div class="list-head col-md-2">상태</div>
            </div>
            <template v-if="salesOrderList.length > 0">
              <div v-for="(salesOrder, index) in salesOrderList"
                   :key="salesOrder.salesOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="addDueDate(index)">
                <div class="list-body col-md-6 left">
                  {{ salesOrder.salesOrderName }}
                  <br>
                  <div v-if="!salesOrder.itemName"><br></div>
                  <div v-else>{{ salesOrder.itemName }}</div>
                </div>
                <div class="list-body col-md-2">{{
                    dayjs(salesOrder.salesOrderOrderDate).format('YYYY/MM/DD')
                  }}
                </div>
                <div class="list-body col-md-2">{{
                    dayjs(salesOrder.salesOrderDueDate).format('YYYY/MM/DD')
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
              :totalRows="totalCount"
              :perPage="pageSize"/>
        </div>
      </div>
    </div>
  </div>

<!--  <div class="col-md-10 d-flex flex-column">-->
<!--    <h5 class="px-4">품목 목록</h5>-->
<!--      <div style="max-height: 250px; overflow-y: auto;">-->
<!--        <div style="max-height: 200px;" v-for="(item, index) in itemList" :key="item.salesOrderItemSeq"-->
<!--             class="mx-5 my-3 d-flex flex-row border border-secondary rounded">-->
<!--          <img :src=item.itemImageUrl class="p-2 col-md-4 card-img-top">-->
<!--          <div class="p-2 col-md-8">-->
<!--            <div class="mb-4 d-flex justify-content-between">-->
<!--              <span class="fw-bold">{{ item.itemName }}</span>-->
<!--              <trashIcon class="icon" @click="deleteItem(index)"/>-->
<!--            </div>-->
<!--            <div class="d-flex">-->
<!--              <ul class="col-md-6 p-0">-->
<!--                <li class="mb-3 ">-->
<!--                  · 품목 구분 : {{ findStatusValue(itemDivisionList, item.itemDivision) }}-->
<!--                </li>-->
<!--                <li class="mb-3 d-flex flex-row">-->
<!--                  · 수량 :-->
<!--                  <b-form-input class="ms-2 me-2 w-25" type="text" v-model="itemData[index].quantity"-->
<!--                                size="sm"></b-form-input>-->
<!--                  개 (최대 : {{ numberThree(itemData[index].originalQuantity) }} 개)-->
<!--                </li>-->
<!--              </ul>-->
<!--              <ul class="col-md-6 p-0 d-flex flex-column justify-content-between">-->
<!--                <li class="mb-3 d-flex flex-row">-->
<!--                  · 비고 :-->
<!--                  <b-form-input class="ms-2 w-75" type="text" v-model="itemData[index].note" size="sm"></b-form-input>-->
<!--                </li>-->
<!--              </ul>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--  </div>-->

</template>

<style scoped>

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

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

.no-list-text {
  text-align: center;
  margin-top: 2%;
}

li {
  list-style: none;
}
</style>
