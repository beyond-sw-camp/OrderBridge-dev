<script setup>
import {ref, onMounted, watch, defineProps} from 'vue';
import axios from '@/axios';
// import router from '@/router/index.js';
import searchIcon from '@/assets/searchIcon.svg';
import dayjs from 'dayjs';
import { Modal } from 'bootstrap';
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  workOrderDetail: {type: Object, required: false},       // 출하지시서 목록
  workOrderItem: {type: Object, required: false},       // 출하지시서 목록
  salesOrder: { type: Object, required: false },      // 주문서 정보
  stockStatusList: { type: Array, required: false },  // 주문서 품목 목록
  isEditMode: {type: Boolean, required: false},       // 검색 결과 총 개수
});

const salesOrderList = ref([]); // 주문서 목록
const totalCount = ref(0); // 총 주문서 수
const pageSize = ref(10); // 페이지 크기
const pageNumber = ref(1); // 페이지 번호

const warehouses = ref([]); // 창고 목록
const salesOrderStatusList = ref([]); // 주문서 상태 목록
const salesOrderDueDate = ref(''); // 주문서 납기일

const stockStatusList = ref([]); // 주문서 품목 재고 상태 목록

const selectedSalesOrder = ref(null); // 선택된 주문서
const selectedItem = ref(null); // 선택된 품목

// 폼 데이터
const formData = ref({
  salesOrder: '',
  salesOrderSeq: '',
  salesOrderItemSeq: '',
  warehouseSeq: '',
  workOrderName: '',
  workOrderIndicatedDate: '',
  workOrderDueDate: '',
  workOrderIndicatedQuantity: '',
  workOrderWorkQuantity: '',
  workOrderNote: '',
});

watch(pageNumber, () => {
  fetchSalesOrderList();
})

watch(props, () => {

  formData.value.salesOrder = props.salesOrder.salesOrderName;
  formData.value.salesOrderSeq = props.salesOrder.salesOrderSeq;
  salesOrderDueDate.value = props.salesOrder.salesOrderDueDate;
  formData.value.workOrderIndicatedQuantity = props.workOrderDetail.workOrderIndicatedQuantity;
  formData.value.warehouseSeq = props.workOrderDetail.warehouseSeq;
  formData.value.workOrderIndicatedDate = props.workOrderDetail.workOrderIndicatedDate;
  formData.value.workOrderDueDate = props.workOrderDetail.workOrderDueDate;
  formData.value.workOrderIndicatedQuantity = props.workOrderDetail.workOrderIndicatedQuantity;
  formData.value.workOrderWorkQuantity = props.workOrderDetail.workOrderWorkQuantity;
  formData.value.workOrderNote = props.workOrderDetail.workOrderNote;
  formData.value.workOrderName = props.workOrderDetail.workOrderName;

  // salesOrderItemSeq 자동 매칭
  if (props.workOrderItem?.itemName && props.salesOrder?.salesOrderItem) {
    const matchedItem = props.salesOrder.salesOrderItem.find(
        item => item.itemName === props.workOrderItem.itemName
    );

    if (matchedItem) {
      formData.value.salesOrderItemSeq = matchedItem.salesOrderItemSeq;
      console.log('매칭된 salesOrderItemSeq:', matchedItem.salesOrderItemSeq);
    } else {
      console.warn('해당하는 salesOrderItemSeq를 찾을 수 없습니다.');
    }
  }
});

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
    console.error("주문서 상태 목록 불러오기 실패 :", error);
  }
};

const selectSalesOrder = (salesOrder) => {
  selectedSalesOrder.value = salesOrder;

  // 폼 데이터에 주문서 정보 저장
  formData.value.salesOrderSeq = salesOrder.salesOrderSeq;
  formData.value.salesOrder = salesOrder.salesOrderName;
  if (salesOrder && salesOrder.salesOrderDueDate) {
    salesOrderDueDate.value = salesOrder.salesOrderDueDate || ''; // 납기일 설정
    console.log('납기일:', salesOrder.salesOrderDueDate); // 납기일 확인
  } else {
    console.log('선택된 주문서에 납기일이 없습니다.');
  }

  console.log('선택된 주문서:', selectedSalesOrder.value);

  fetchSalesOrderItemStock(salesOrder.salesOrderSeq); // 재고 상태 조회
};

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

watch(() => stockStatusList.value, (newList) => {
  console.log('🔍 stockStatusList 업데이트됨:', newList);
  newList.forEach((item, index) => {
    console.log(`🔍 Item ${index} isRegistered:`, item.isRegistered);
  });
}, { deep: true });

// 등록페이지에서 수정 구분
const handleItemSelection = (index) => {
  // props.isEditMode가 false(수정모드가 아니)고 이미 등록된 품목일 때
    alert('이미 작업지시서가 등록된 주문서입니다. \n 상세보기에서 수정 버튼을 눌러주세요.');
    router.push('/workOrder'); // 목록 페이지로 이동
};

// 선택한 주문서 품목 저장
const selectItem = async (index) => {
  const item = stockStatusList.value[index];
  console.log('선택된 아이템:', item);

  selectedItem.value = {
    ...item,
    isRegistered: false, // 기본값
  };

  formData.value.salesOrderSeq = selectedSalesOrder.value?.salesOrderSeq || ''; // 주문서 시퀀스
  formData.value.salesOrderItemSeq = item.salesOrderItemSeq || ''; // 품목 시퀀스
  formData.value.warehouseSeq = '';
  formData.value.workOrderIndicatedDate = '';
  formData.value.workOrderDueDate = '';
  formData.value.workOrderNote = '';
  console.log('폼 데이터 초기화 완료:', formData.value);

  console.log('선택된 품목:', selectedItem.value);
  await fetchRegisteredItems(selectedSalesOrder.value.salesOrderSeq);

  createOrUpdateWorkOrder(selectedItem.value);
};

// 주문서 품목의 작업지시서 등록여부 조회
const fetchRegisteredItems = async (salesOrderSeq) => {
  if (!salesOrderSeq) {
    console.warn('salesOrderSeq 가 존재하지 않습니다.');
    return;
  }

  try {
    const response = await axios.get(`sales-order/${salesOrderSeq}/registered-items`);
    console.log(response.data);

    const registeredItemSeqs = response.data.map(Number);

    stockStatusList.value = stockStatusList.value.map(item => ({
      ...item,
      isRegistered: registeredItemSeqs.includes(Number(item.salesOrderItemSeq))
    }));

    console.log('등록 여부 조회된 품목 목록:', stockStatusList.value);

    if (selectedItem.value) {
      const updatedItem = stockStatusList.value.find(item =>
          item.salesOrderItemSeq === selectedItem.value.salesOrderItemSeq
      );

      if (updatedItem) {
        selectedItem.value = updatedItem;
        createOrUpdateWorkOrder(updatedItem);
      } else {
        console.warn('선택된 품목을 stockStatusList 에서 찾을 수 없습니다.');
      }
    }


  } catch (error) {
    console.error('등록여부 조회 실패:', error);
  }

};

const createOrUpdateWorkOrder = (item) => {
  if (!item) {
    console.warn('writeWorkOrder: item이 존재하지 않습니다.');
    return;
  }

  if (item.isRegistered) {
    console.log(`이미 등록된 작업지시서입니다. 작업지시서를 수정합니다: ${item.itemName}`);
  } else {
    console.log(`작업지시서를 등록합니다: ${item.itemName}`);
  }

};

// 작업지시서 수정
const updateWorkOrder = async (workOrderSeq) => {
  if (!workOrderSeq) {
    console.warn('workOrderSeq 값이 존재하지 않습니다.');
    return;
  }

  try {
    const response = await axios.put(`workOrder/${workOrderSeq}`, {
      warehouseSeq: formData.value.warehouseSeq,
      workOrderIndicatedDate: formData.value.workOrderIndicatedDate,
      workOrderDueDate: formData.value.workOrderDueDate,
      workOrderNote: formData.value.workOrderNote,
    });
    console.log(response.data);
    alert('작업지시서가 수정되었습니다!');
    await router.push('/workOrder')

  } catch (error) {
    console.error("작업지시서 수정 실패 :", error);
    if (error.response.data.errorCode === 'WORK_ORDER_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'WORK_ORDER_ERROR_005') {
      alert('작업지시서의 상태가 결재 전이거나 진행중일때만 수정 가능합니다.');
    } else if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert('작업납기일은 현재보다 이전일 수 없습니다.');
    } else if (error.response.data.errorCode === 'WORK_ORDER_ERROR_006') {
      alert('지시수량은 이미 완료된 수량보다 적을 수 없습니다.');
    } else if (error.response.data.errorCode === 'SALES_ORDER_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'SALES_ORDER_ERROR_002') {
      alert('주문서의 상태가 결재 후일때만 수정 가능합니다.');
    } else if (error.response.data.errorCode === 'SALES_ORDER_ITEM_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'ITEM_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'WORK_ORDER_ERROR_004') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'ITEM_ERROR_003') {
      alert('하위품목 정보가 존재하지 않습니다.');
    } else if (error.response.data.errorCode === 'STOCK_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'WAREHOUSE_ERROR_001') {
      alert(error.response.data.message);
    } else {
      alert('수정에 실패했습니다. 다시 시도해주세요.');
    }
  }
};

// 작업지시서 등록
const createWorkOrder = async () => {
  try {
    console.log('폼 데이터:', formData.value);

    const response = await axios.post(`workOrder`, {
      salesOrderSeq: formData.value.salesOrderSeq,
      salesOrderItemSeq: formData.value.salesOrderItemSeq,
      warehouseSeq: formData.value.warehouseSeq,
      workOrderIndicatedDate: formData.value.workOrderIndicatedDate,
      workOrderDueDate: formData.value.workOrderDueDate,
      workOrderNote: formData.value.workOrderNote,
    });
      console.log(response.data);
      alert('작업지시서가 등록되었습니다!');
      await router.push('/workOrder')

  } catch (error) {
    console.error("작업지시서 등록 실패 :", error);

    if (error.response.data.errorCode === 'SALES_ORDER_ITEM_NOT_FOUND') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'SALES_ORDER_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'SALES_ORDER_ERROR_002') {
      alert('주문서의 상태가 결재 후일때만 등록 가능합니다.');
    } else if (error.response.data.errorCode === 'WAREHOUSE_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'WORK_ORDER_ERROR_006') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'ITEM_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'WORK_ORDER_ERROR_004') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'ITEM_ERROR_003') {
      alert(error.response.data.message);
    }  else if (error.response.data.errorCode === 'STOCK_ERROR_001') {
      alert('재료가 부족해 작업지시가 불가능합니다.\n 발주서를 작성해 재료를 먼저 구매해주세요.');
    } else if (error.response.data.errorCode === 'WORK_ORDER_ERROR_003') {
      alert('작업지시일과 납기일을 설정해주세요');
    } else if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert(error.response.data.message);
    } else {
      alert('등록에 실패했습니다. 다시 시도해주세요.');
    }

  }
};

// 작업지시서 제출
const submitWorkOrder = async () => {
  if (!validateFormData()) {
    console.warn('폼 데이터가 완전하지 않습니다.');
    return;
  }

  if (props.isEditMode) {
    await updateWorkOrder(props.workOrderDetail.workOrderSeq);
    console.log(props.workOrderDetail.workOrderSeq)
  } else {
    await createWorkOrder();
  }
};

// 폼 데이터 검증
const validateFormData = () => {
  if (!formData.value.salesOrderSeq || !formData.value.salesOrderItemSeq || !formData.value.warehouseSeq) {
    console.warn('필수 항목이 누락되었습니다.', formData.value);
    alert('생산공장을 선택해주세요.')
    return false;
  }
  return true;
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
      <!-- 주문납기일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="주문납기일" label-for="salesOrderDueDate">
        <input class="form-control form-control-sm w-75" type="date" id="salesOrderDueDate"
               v-model="salesOrderDueDate" readonly=""/>
      </b-form-group>
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
      <div v-for="(item, index) in stockStatusList" :key="item.salesOrderItemSeq" class="mx-5 my-3">
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
          <div class="col-md-4 d-flex flex-column justify-content-around align-items-center">
            <img :src="item.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
            <div>
              <b-button v-if="item?.isRegistered && !props?.isEditMode"
                  @click="handleItemSelection(index)"
                        variant="light" size="sm" class="button ms-2 mb-3" style="top: 10px; right: 10px;">
                {{ '목록에서 수정하기' }}
              </b-button>
              <b-button v-else
                        @click="selectItem(index)"
                        variant="light" size="sm" class="button ms-2 mb-3" style="top: 10px; right: 10px;">
                {{ item.isRegistered ? '작업지시서 수정' : '품목선택 & 등록' }}
              </b-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 작업지시서 등록폼 -->
  <div class="d-flex justify-content-center">
    <div class="col-6 d-flex flex-column">
            <h5 v-if="props.isEditMode " class="px-4">작업지시서 수정</h5>
            <h5 v-else class="px-4">작업지시서 등록</h5>
          <!-- 작업지시서명 -->
          <b-form-group v-if="props.isEditMode" label-cols="4" label-cols-lg="2" label="작업지시서명" label-for="workOrderName">
            <input class="form-control form-control-sm w-75" type="text" id="workOrderName"
                   v-model="formData.workOrderName" readonly="readonly"/>
          </b-form-group>
          <!-- 작업지시일자 -->
          <b-form-group label-cols="4" label-cols-lg="2" label="작업지시일" label-for="workOrderIndicatedDate">
            <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
            <input class="form-control form-control-sm w-75" type="datetime-local" id="workOrderIndicatedDate"
                   v-model="formData.workOrderIndicatedDate" placeholder="작업지시일자를 선택해 주세요."/>
          </b-form-group>
          <!-- 작업 납기일자 -->
          <b-form-group label-cols="4" label-cols-lg="2" label="작업납기일" label-for="workOrderDueDate">
            <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
            <input class="form-control form-control-sm w-75" type="datetime-local" id="workOrderDueDate"
                   v-model="formData.workOrderDueDate" placeholder="목표납기일자를 선택해 주세요."/>
          </b-form-group>
          <!-- 생산공장 -->
          <b-form-group label-cols="4"  label-cols-lg="2" label-size="default" label="생산공장" label-for="warehouseSeq">
            <b-form-select class="w-75" size="sm" id="warehouseSeq" v-model="formData.warehouseSeq">
              <option value="">선택하세요</option>
              <option v-for="warehouse in warehouses" :key="warehouse.warehouseSeq"
                      :value="warehouse.warehouseSeq">{{ warehouse.warehouseName }}</option>
            </b-form-select>
          </b-form-group>
          <!-- 작업 지시수량 -->
          <b-form-group v-if="props.isEditMode" label-cols="4" label-cols-lg="2" label="작업지시수량" label-for="workOrderIndicatedQuantity">
            <input class="form-control form-control-sm w-75" type="number" id="workOrderIndicatedQuantity"
                   v-model="formData.workOrderIndicatedQuantity" placeholder="작업지시수량을 입력해 주세요."/>
          </b-form-group>
          <!-- 작업 지시수량 -->
          <b-form-group v-if="props.isEditMode" label-cols="4" label-cols-lg="2" label="작업완료수량" label-for="workOrderWorkQuantity">
            <input class="form-control form-control-sm w-75" type="number" id="workOrderWorkQuantity"
                   v-model="formData.workOrderWorkQuantity" readonly="readonly"/>
          </b-form-group>
          <!-- 작업지시서 비고 -->
          <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="작업지시서 비고" label-for="workOrderNote">
            <b-form-input class="w-75" size="sm" type="text" id="workOrderNote" v-model="formData.workOrderNote" placeholder="작업지시서 비고를 입력해주세요."/>
          </b-form-group>

      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button @click="submitWorkOrder" variant="light" size="sm" class="button ms-2">
          {{ props.isEditMode  ? '작업지시서 수정' : '작업지시서 등록' }}
        </b-button>
      </div>

    </div>
  </div>

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
              <div v-for="(salesOrder) in salesOrderList"
                   :key="salesOrder.salesOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="selectSalesOrder(salesOrder)">
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
