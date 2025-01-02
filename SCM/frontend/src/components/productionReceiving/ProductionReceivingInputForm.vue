<script setup>
import {defineProps, onMounted, ref, watch} from "vue";
import searchIcon from '@/assets/searchIcon.svg'
import dayjs from "dayjs";
import axios from "@/axios";
import router from "@/router/index.js";

const props = defineProps({
  productionReceivingDetail: {type: Object, required: false},       // 출하지시서 목록
  productionReceivingItemList: {type: Array, required: false},       // 검색 결과 총 개수
  workOrderList: {type: Array, required: false}
});

const workOrderTotalCount = ref(0);
const workOrderPageSize = ref(10);
const workOrderPageNumber = ref(1);
const workOrderList = ref([]);
const checkedItemList = ref([]);

watch(workOrderPageNumber, () => {
  fetchWorkOrderList();
})

watch(props, () => {

  formData.value.productionReceivingReceiptDate = props.productionReceivingDetail.productionReceivingReceiptDate;
  formData.value.productionReceivingExtendedPrice = props.productionReceivingDetail.productionReceivingExtendedPrice;
  formData.value.productionReceivingNote = props.productionReceivingDetail.productionReceivingNote;

  for(const workOrder of props.workOrderList) {
    checkWorkOrder(workOrder.workOrderSeq);
  }
})

const fetchWorkOrderList = async () => {
  try {
    const response = await axios.get(`workOrder`, {
      params: {
        startDate: null,
        endDate: null,
        warehouseName: null,
        workOrderStatus: 'ONGOING',
        page: workOrderPageNumber.value,
        size: workOrderPageSize.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    workOrderList.value = response.data.workOrderList;
    // 초기화: 모든 작업 지시서의 체크박스를 false로 설정
    workOrderList.value.forEach((workOrder) => {
      checkedItemList.value[workOrder.workOrderSeq] = false;
    });
    workOrderTotalCount.value = response.data.totalItems;

  } catch (error) {
    console.error("주문서 불러오기 실패 :", error);
  }
};

// 페이지 이동
const workOrderHandlePage = (pageNumber) => {
  workOrderPageNumber.value = Number(pageNumber.value);
  fetchWorkOrderList();
};

onMounted(() => {
  console.log()
  fetchWorkOrderList();
});

const checkWorkOrderList = ref([]);
const fetchWorkOrder = async (workOrderSeq) => {
  try {
    const response = await axios.get(`workOrder/${workOrderSeq}`);

    const checkWorkOrder = {
      workOrderSeq: response.data.workOrderDetail.workOrderSeq,
      workOrderName: response.data.workOrderDetail.workOrderName, // 작업지시서명
      userName: response.data.workOrderDetail.userName, // 작업지시서명
      clientName: response.data.workOrderDetail.clientName, // 거래처명
      workOrderDueDate: response.data.workOrderDetail.workOrderDueDate, // 작업 납기일
      workOrderEndDate: response.data.workOrderDetail.workOrderEndDate, // 작업완료일
      workOrderIndicatedDate: response.data.workOrderDetail.workOrderIndicatedDate, // 작업지시일
      workOrderIndicatedQuantity: response.data.workOrderDetail.workOrderIndicatedQuantity, // 작업지시서 주문 수량
      workOrderPrice: response.data.workOrderDetail.workOrderPrice, // 작업지시서 주문 금액
      itemSeq: response.data.workOrderItem.itemSeq,
      itemImageUrl: response.data.workOrderItem.itemImageUrl, // 품목 이미지
      itemName: response.data.workOrderItem.itemName, // 품목명
      itemPrice: response.data.workOrderItem.itemPrice, // 품목 가격
      itemUnitTitle: response.data.workOrderItem.itemUnitTitle, // 품목 단위
      itemNote: response.data.workOrderDetail.workOrderNote
    }

    formData.value.productionReceivingItemList.push({
      itemSeq: checkWorkOrder.itemSeq,
      productionReceivingItemQuantity: checkWorkOrder.workOrderIndicatedQuantity,
      productionReceivingUnitPrice: checkWorkOrder.workOrderPrice,
      productionReceivingItemNote: checkWorkOrder.itemNote
    });
    formData.value.productionReceivingExtendedPrice += checkWorkOrder.workOrderPrice;
    checkWorkOrderList.value.push(checkWorkOrder);
  } catch (error) {
    console.error("보관 창고 불러오기 실패 :", error);
  }
};

const checkWorkOrderSeqSet = ref(new Set([]));
const checkWorkOrder = (workOrderSeq) => {
  checkedItemList.value[workOrderSeq] = !checkedItemList.value[workOrderSeq];

  if(checkWorkOrderSeqSet.value.has(workOrderSeq)) {
    checkWorkOrderSeqSet.value.delete(workOrderSeq);
  } else {
    checkWorkOrderSeqSet.value.add(workOrderSeq);
  }
}

watch(checkWorkOrderSeqSet.value, () => {
  checkWorkOrderList.value = [];
  formData.value.productionReceivingItemList = [];
  formData.value.productionReceivingExtendedPrice = 0;
  for (const checkWorkOrderSeq of checkWorkOrderSeqSet.value) {
    fetchWorkOrder(checkWorkOrderSeq);

  }
})

const formData = ref({
  productionReceivingReceiptDate: '',
  productionReceivingExtendedPrice: 0,
  productionReceivingNote: '',
  workOrderSeqList: [],
  productionReceivingItemList: []
});

const createProductionReceiving = async () => {
  formData.value.workOrderSeqList = Array.from(checkWorkOrderSeqSet.value);
  if(formData.value.workOrderSeqList.length === 0) {
    alert("작업지시서를 선택해주세요.")
    return;
  }
  console.log(formData.value.productionReceivingReceiptDate);
  if(formData.value.productionReceivingReceiptDate === '') {
    alert("생산입고일을 지정해주세요.")
    return;
  }
  if(formData.value.productionReceivingExtendedPrice === 0) {
    alert("생산입고 총액을 입력해주세요.")
    return;
  }
  try {
    const response = await axios.post('productionReceiving',
        {
          workOrderSeqList: formData.value.workOrderSeqList,
          productionReceivingExtendedPrice: formData.value.productionReceivingExtendedPrice,
          productionReceivingNote: formData.value.productionReceivingNote,
          productionReceivingReceiptDate: formData.value.productionReceivingReceiptDate,
          productionReceivingItemList: formData.value.productionReceivingItemList
        }, {
        });

    console.log(response);
    alert('생산입고가 등록되었습니다!');
    // 조회 페이지 이동
    await router.push('/productionReceiving')

  } catch (error) {
    console.error('생산입고 등록 실패', error);
    throw error;
  }
};

function openModal() {
  const modal = document.getElementById('workOrderModal');
  modal.removeAttribute('aria-hidden'); // 모달 활성화 시 aria-hidden 제거
  modal.style.display = 'block';
  modal.focus(); // 포커스를 모달로 이동
}

function closeModal() {
  const modal = document.getElementById('workOrderModal');
  modal.setAttribute('aria-hidden', 'true'); // 모달 비활성화 시 aria-hidden 추가
  modal.style.display = 'none';
}

const updateProductionReceiving = async () => {
  formData.value.workOrderSeqList = Array.from(checkWorkOrderSeqSet.value);
  if(formData.value.workOrderSeqList.length === 0) {
    alert("작업지시서를 선택해주세요.")
    return;
  }
  console.log(formData.value.productionReceivingReceiptDate);
  if(formData.value.productionReceivingReceiptDate === '') {
    alert("생산입고일을 지정해주세요.")
    return;
  }
  if(formData.value.productionReceivingExtendedPrice === 0) {
    alert("생산입고 총액을 입력해주세요.")
    return;
  }
  try {
    const response = await axios.put(`productionReceiving/${props.productionReceivingDetail.productionReceivingSeq}`,
        {
          workOrderSeqList: formData.value.workOrderSeqList,
          productionReceivingExtendedPrice: formData.value.productionReceivingExtendedPrice,
          productionReceivingNote: formData.value.productionReceivingNote,
          productionReceivingReceiptDate: formData.value.productionReceivingReceiptDate,
          productionReceivingItemList: formData.value.productionReceivingItemList
        }, {
        });

    console.log(response);
    alert('생산입고가 수정되었습니다!');
    // 조회 페이지 이동
    await router.push('/productionReceiving')

  } catch (error) {
    console.error('생산입고 수정 실패', error);
    throw error;
  }
};
</script>

<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/productionReceiving')" variant="light" size="sm" class="button ms-2">목록</b-button>
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-6 d-flex flex-column">
      <!-- 작업지시서 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="작업지시서" label-for="workOrder">
        <b-input-group class="w-75" size="sm" @click="openModal" data-bs-toggle="modal" data-bs-target="#workOrderModal">
          <b-form-input type="text" id="workOrder" placeholder="작업 지시서를 선택해 주세요." readonly/>
          <b-input-group-text>
            <searchIcon class="icon"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
      <!-- 생산입고일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="생산입고일" label-for="productionReceivingReceiptDate">
        <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
        <input class="form-control form-control-sm w-75" type="datetime-local" id="productionReceivingReceiptDate" v-model="formData.productionReceivingReceiptDate" placeholder="생산입고일자를 선택해 주세요."/>
      </b-form-group>
      <!-- 생산입고 총액 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="생산입고 총액" label-for="productionReceivingExtendedPrice">
        <b-form-input class="w-75" size="sm" type="number" id="productionReceivingExtendedPrice" v-model="formData.productionReceivingExtendedPrice" placeholder="생산입고 총액을 입력해주세요."/>
      </b-form-group>
      <!-- 생산입고 비고 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="생산입고 비고" label-for="productionReceivingNote">
        <b-form-input class="w-75" size="sm" type="text" id="productionReceivingNote" v-model="formData.productionReceivingNote" placeholder="생산입고 비고를 입력해주세요."/>
      </b-form-group>
    </div>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div v-if="checkWorkOrderList.length > 0" class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">작업지시서 목록</h5>
      <!-- 작업 지시서 반복 -->
      <div v-for="workOrder in checkWorkOrderList" :key="workOrder.workOrderSeq" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
          <!-- 텍스트 정보 -->
          <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h6 class="fw-bold">{{ workOrder.workOrderName }}</h6>
            </div>
            <ul class="d-flex flex-wrap align-items-start">
              <li class="mb-3 col-md-6">· 납품처 : {{ workOrder.clientName }}</li>
              <li class="mb-3 col-md-6">· 담당자명 : {{ workOrder.userName }}</li>
              <li class="mb-3 col-md-6">· 생산창고명 : {{ workOrder.warehouseName }}</li>
              <li class="mb-3 col-md-6">· 입고창고명 : {{ workOrder.warehouseName }}</li>
              <li class="mb-3 col-md-6">· 품목명 : {{ workOrder.itemName }}</li>
              <li class="mb-3 col-md-6">· 품목 가격 : {{ workOrder.itemPrice.toLocaleString() }} ₩</li>
              <li class="mb-3 col-md-6">· 주문 개수 : {{ workOrder.workOrderIndicatedQuantity.toLocaleString() }}</li>
              <li class="mb-3 col-md-6">· 주문 총액 : {{ workOrder.workOrderPrice }} ₩</li>
              <li class="mb-3 col-md-12 d-flex align-items-center">
                <span class="me-3 text-nowrap">· 품목 비고 : {{workOrder.itemNote}}</span>
              </li>
            </ul>
          </div>
          <!-- 이미지 -->
          <div class="col-md-4 d-flex justify-content-center align-items-center">
            <img :src="workOrder.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
          </div>
          <!-- x 버튼 -->
          <b-button @click="checkWorkOrder(workOrder.workOrderSeq)" variant="light" size="sm" class="position-absolute btn-close" style="top: 10px; right: 10px;"></b-button>
        </div>
      </div>
      <!-- 확인 버튼 -->
      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button v-if="props.productionReceivingDetail" @click="updateProductionReceiving" variant="light" size="sm" class="button ms-2">수정</b-button>
        <b-button v-else @click="createProductionReceiving" variant="light" size="sm" class="button ms-2">등록</b-button>

      </div>
    </div>
  </div>
  <!-- workOrderModal -->
  <div class="modal fade" id="workOrderModal" tabindex="-1" aria-labelledby="workOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">작업 지시서 선택</h1>
          <div class="ms-5">검색결과: {{ workOrderTotalCount }}개</div>
          <button type="button" @click="closeModal" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll" class="d-flex row justify-content-center align-items-center">

            <div class="list-headline row">
              <div class="list-head col-1">선택</div>
              <div class="list-head col-5">작업지시서명</div>
              <div class="list-head col-2">생산공장명</div>
              <div class="list-head col-2">작업지시일</div>
              <div class="list-head col-2">상태</div>
            </div>
            <template v-if="workOrderList.length > 0">
              <div v-for="workOrder in workOrderList" :key="workOrder.workOrderSeq" class="list-line row" @click="checkWorkOrder(workOrder.workOrderSeq)">
                <!-- 체크박스 -->
                <div class="list-body col-1">
                  <input type="checkbox" v-model="checkedItemList[workOrder.workOrderSeq]"/>
                </div>
                <div class="list-body col-5 left">{{ workOrder.workOrderName }}</div>
                <div class="list-body col-2">{{ workOrder.warehouseName }}</div>
                <div class="list-body col-2">{{ dayjs(workOrder.workOrderIndicatedDate).format('YYYY/MM/DD') }}</div>
                <div class="list-body col-2">{{ workOrder.workOrderStatus }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 작업지시서가 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination v-model="workOrderPageNumber" :totalRows="workOrderTotalCount" :perPage="workOrderPageSize"/>
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
