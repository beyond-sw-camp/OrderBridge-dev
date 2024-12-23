<script setup>
import {defineProps, onMounted, ref, watch} from "vue";
import searchIcon from '@/assets/searchIcon.svg'
import dayjs from "dayjs";
import axios from "@/axios.js";
import router from "@/router/index.js";
import PurchaseList from "@/components/purchase/PurchaseList.vue";

const pageSize = ref(10);
const pageNumber = ref(1);
const purchaseList = ref([]);
const purchaseItemList = ref([]);

const totalCount = ref(null);

const formData = ref({
  salesOrderSeq: 0,
  clientSeq: 0,
  purchaseOrderDueDate: '',
  purchaseOrderTargetDueDate: '',
  purchaseOrderExtendedPrice: '',
  purchaseOrderTotalQuantity: '',
  purchaseOrderNote: '',
  purchaseOrderItemList: {}
});

const fetchPurchaseList = async () => {
  try {
    const response = await axios.get(`purchase`, {
      params: {
        startDate: null,
        endDate: null,
        warehouseName: null,
        workOrderStatus: 'ONGOING',
        page: pageNumber.value,
        size: pageSize.value
      }, paramsSerializer: (params) => {
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    purchaseList.value = response.data.workOrderList;
    purchaseList.value.forEach((workOrder) => {
      purchaseItemList.value[workOrder.purchaseOrderSeq] = false;
    });
    totalCount.value = response.data.totalCount;

  } catch (error) {
    console.error("주문서 불러오기 실패 :", error);
  }
};

const formatStatus = (status) => {
  if (status === 'PRODUCTION_COMPLETE') {
    return '생산완료';
  } else if (status === 'BEFORE') {
    return '결재전'
  }  else if (status === 'AFTER') {
    return '결재후'
  } else if (status === 'PRODUCTION') {
    return '생산중'
  } else if (status === 'SHIPMENT_COMPLETE') {
    return '출하완료'
  } else if (status === 'DELETE') {
    return '삭제'
  }
  return status; // 상태가 다른 경우 그대로 반환
};

onMounted(() => {
  console.log()
  fetchPurchaseList();
});

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

const removeItem = (salesOrderSeq) => {
  purchaseOrderItemList.value = purchaseOrderItemList.value.filter(
      (item) => item.itemSeq !== salesOrderSeq
  );

  delete purchaseOrderItemList.value[itemSeq];
};

const addToOrderList = (selectedOrder) => {
  const existingItem = purchaseOrderItemList.value.find(
      (item) => item.itemSeq === selectedOrder.itemSeq
  );
  if (!existingItem) {
    purchaseOrderItemList.value.push({
      itemName: selectedOrder.itemName,
      salesOrderItemPrice: selectedOrder.salesOrderItemPrice || 0,
      salesOrderItemQuantity: selectedOrder.salesOrderItemQuantity || 0,
      salesOrderItemNote: selectedOrder.salesOrderItemNote || ""
    });
  } else {
    alert("This order has already been added.");
  }
  closeItemModal();
};

function validationCheck() {
  if(document.getElementById('purchaseOrderDueDate').value === '') {
    alert("계약 납기일을 선택해주세요.");
    return false;
  }
  if(document.getElementById('purchaseOrderTargetDueDate').value === '') {
    alert("목표 납기일을 선택해주세요.")
    return false;
  }

  return true;
}

const createPurchaseOrder = async () => {
  if (validationCheck()) {
    try {
      const response = await axios.post('productionReceiving',
          {
            purchaseOrderDueDate: formData.value.purchaseOrderDueDate,
            purchaseOrderTargetDueDate: formData.value.purchaseOrderTargetDueDate,
            purchaseOrderExtendedPrice: formData.value.purchaseOrderExtendedPrice,
            purchaseOrderNote: formData.value.purchaseOrderNote,
            purchaseOrderItemDtoList: formData.value.purchaseOrderItemList
          });

      console.log(response);
      alert('발주서가 등록되었습니다!');
      // 조회 페이지 이동
      await router.push('/purchaseOrder')

    } catch (error) {
      console.error('발주서 등록 실패', error);
      throw error;
    }
  }
};


</script>

<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/purchaseOrder')" variant="light" size="sm" class="button ms-2">목록</b-button>
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-6 d-flex flex-column">
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="발주서" label-for="workOrder">
        <b-input-group class="w-75" size="sm" @click="openModal" data-bs-toggle="modal" data-bs-target="#workOrderModal">
          <b-form-input type="text" id="workOrder" placeholder="발주서를 선택해 주세요." readonly/>
          <b-input-group-text>
            <searchIcon class="icon"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="계약 납기일" label-for="purchaseOrderDueDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="purchaseOrderDueDate" v-model="formData.purchaseOrderDueDate" placeholder="계약 납기일을 선택해 주세요."/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="목표 납기일" label-for="purchaseOrderTargetDueDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="purchaseOrderTargetDueDate" v-model="formData.purchaseOrderTargetDueDate" placeholder="목표 납기일을 선택해 주세요."/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="구매서 비고" label-for="purchaseOrderNote">
        <b-form-input class="w-75" size="sm" type="text" id="purchaseOrderNote" v-model="formData.purchaseOrderNote" placeholder="비고를 입력해주세요."/>
      </b-form-group>
    </div>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">구매서 품목
        <!--        <span class="w-75" size="sm" @click="openOrderModal" data-bs-toggle="modal" data-bs-target="#openOrderModal">
                <b-button variant="light" size="sm" class="button ms-2" style="float: right">주문서 불러오기</b-button>
                </span>-->
      </h5>
      <div v-for="purchaseOrder in purchaseOrderItemList" :key="purchaseOrder.itemSeq" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
          <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h6 class="fw-bold">{{ purchaseOrder.itemName }}</h6>
            </div>
            <ul class="d-flex flex-wrap align-items-start">
              <li class="mb-3 col-md-6">· 품목명 : {{ purchaseOrder.itemName }}</li>
              <li class="mb-3 col-md-6">· 품목 가격 : ₩ {{ purchaseOrder.salesOrderItemPrice != null ? purchaseOrder.salesOrderItemPrice.toLocaleString() : 0 }} </li>
              <li class="mb-3 col-md-6">· 주문 개수 : {{ purchaseOrder.salesOrderItemQuantity!= null ? purchaseOrder.salesOrderItemQuantity.toLocaleString() : 0 }}</li>
              <li class="mb-3 col-md-6">· 비고 : {{ purchaseOrder.salesOrderItemNote }} </li>
            </ul>
          </div>
          <div class="col-md-4 d-flex justify-content-center align-items-center">
            <img :src="purchaseOrder.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
          </div>

          <b-button
              @click="removeItem(purchaseOrder.salesOrderSeq)"
              variant="light"
              size="sm"
              class="position-absolute btn-close"
              style="top: 10px; right: 10px;"
          ></b-button>
        </div>
      </div>

      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button v-if="props" @click="updatePurchaseOrder" variant="light" size="sm" class="button ms-2">수정</b-button>
        <b-button v-else @click="createPurchaseOrder" variant="light" size="sm" class="button ms-2">등록</b-button>

      </div>
    </div>
  </div>
  <!-- workOrderModal -->
  <div class="modal fade" id="workOrderModal" tabindex="-1" aria-labelledby="workOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">작업 지시서 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" @click="closeModal" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll" class="d-flex row justify-content-center align-items-center">

            <div class="list-headline row">
              <div class="list-head col-5">작업지시서명</div>
              <div class="list-head col-2">생산공장명</div>
              <div class="list-head col-2">작업지시일</div>
              <div class="list-head col-2">상태</div>
            </div>
            <template v-if="purchaseList.length > 0">
              <div v-for="purchase in purchaseList" :key="purchase.purchaseSeq" class="list-line row" @click="addToOrderList(purchase.purchaseSeq)">
                <div class="list-body col-5 left">{{ purchase.purchaseName }}</div>
                <div class="list-body col-2">{{ purchase.purchaseRegDate }}</div>
                <div class="list-body col-2">{{ dayjs(purchase.purchaseModDate).format('YYYY/MM/DD') }}</div>
                <div class="list-body col-2">{{ purchase.purchaseStatus }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 작업지시서가 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination v-model="pageNumber" :totalRows="totalCount" :perPage="pageSize"/>
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
