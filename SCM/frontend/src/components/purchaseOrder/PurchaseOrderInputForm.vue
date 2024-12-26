<script setup>
import {defineProps, onMounted, ref, computed} from "vue";
import plusIcon from '@/assets/plus.svg'
import dayjs from "dayjs";
import axios from "@/axios.js";
import router from "@/router/index.js";
import { Modal } from 'bootstrap';


const pageSize = ref(10);
const pageNumber = ref(1);
const purchaseItemList = ref([]);

const addPurchaseOrderItemList = ref([]);
const totalCount = ref(null);

const calculatePrice = computed(() => {
  return addPurchaseOrderItemList.value.reduce((total, item) => {
    return total + (item.purchaseOrderItemPrice || 0) * (item.purchaseOrderItemQuantity || 1);
  }, 0);
});

const formData = ref({
  salesOrderSeq: 0,
  clientSeq: 0,
  purchaseOrderDueDate: '',
  purchaseOrderTargetDueDate: '',
  purchaseOrderExtendedPrice: '',
  purchaseOrderTotalQuantity: '',
  purchaseOrderNote: ''
});

const fetchItems = async (filters = {}) => {
  try {
    const response = await axios.get(`item`, {
      params: filters,
      paramsSerializer: (params) => {
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      },
    });

    console.log(response.data.content);
    purchaseItemList.value = Array.isArray(response.data.content) ? response.data.content : [];
    openItemModal();
  } catch (error) {
    console.error("Failed to fetch items:", error);
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
  fetchItems();
});

const updatePrice = (itemSeq) => {
  const item = addPurchaseOrderItemList.value.find((order) => order.itemSeq === itemSeq);
  if (item) {
    item.calculatePrice = (item.purchaseOrderItemPrice || 0) * (item.purchaseOrderItemQuantity || 1);
    calculatePrice.value = item.calculatePrice;
  }
};

function openItemModal() {
  const modal = document.getElementById('openItemModal');
  if (modal) {
    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
  } else {
    console.error("Modal not found.");
  }
}

function closeItemModal() {
  const modal = document.getElementById('openItemModal');
  if (modal) {
    const bootstrapModal = Modal.getInstance(modal);
    if (bootstrapModal) {
      bootstrapModal.hide();
    } else {
      console.error("Bootstrap Modal instance not found.");
    }
  } else {
    console.error("Modal not found.");
  }
}

const removeItem = (itemSeq) => {
  addPurchaseOrderItemList.value = addPurchaseOrderItemList.value.filter(
      (item) => item.itemSeq !== itemSeq
  );
};

const addToOrderList = (selectedOrder) => {
  console.log("itemSeq : " + selectedOrder.itemSeq);
  const existingItem = addPurchaseOrderItemList.value.find(
      (item) => item.itemSeq === selectedOrder.itemSeq
  );
  if (!existingItem) {
    addPurchaseOrderItemList.value.push({
      itemSeq: selectedOrder.itemSeq,
      itemName: selectedOrder.itemName,
      itemImageUrl: selectedOrder.itemImageUrl,
      purchaseOrderItemPrice: selectedOrder.itemPrice || 0,
      purchaseOrderItemQuantity: selectedOrder.salesOrderItemQuantity || 1,
      calculatePrice: (selectedOrder.itemPrice || 0) * (selectedOrder.salesOrderItemQuantity || 1) ,
    });
  } else {
    alert("이미 추가된 품목입니다.");
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
      const response = await axios.post('purchaseOrder',
          {
            userSeq: 1,
            clientSeq:1,
            purchaseOrderDueDate: formData.value.purchaseOrderDueDate,
            purchaseOrderTargetDueDate: formData.value.purchaseOrderTargetDueDate,
            purchaseOrderExtendedPrice: calculatePrice.value,
            purchaseOrderNote: formData.value.purchaseOrderNote,
            purchaseOrderItemDtoList: addPurchaseOrderItemList.value
          });

      console.log(response);
      alert('발주서가 등록되었습니다');
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
      <b-form-group label-cols="4" label-cols-lg="2" label="계약 납기일" label-for="purchaseOrderDueDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="purchaseOrderDueDate" v-model="formData.purchaseOrderDueDate" placeholder="계약 납기일을 선택해 주세요."/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label="목표 납기일" label-for="purchaseOrderTargetDueDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="purchaseOrderTargetDueDate" v-model="formData.purchaseOrderTargetDueDate" placeholder="목표 납기일을 선택해 주세요."/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="발주서 비고" label-for="purchaseOrderNote">
        <b-form-input class="w-75" size="sm" type="text" id="purchaseOrderNote" v-model="formData.purchaseOrderNote" placeholder="비고를 입력해주세요."/>
      </b-form-group>
    </div>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
<div class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">발주서 품목
<!--        <span class="w-75" size="sm" @click="openOrderModal" data-bs-toggle="modal" data-bs-target="#openOrderModal">
        <b-button variant="light" size="sm" class="button ms-2" style="float: right">주문서 불러오기</b-button>
        </span>-->
      </h5>
      <div v-for="purchaseOrder in addPurchaseOrderItemList" :key="purchaseOrder.itemSeq" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
          <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h6 class="fw-bold">{{ purchaseOrder.itemName }}</h6>
            </div>
            <ul class="d-flex flex-wrap align-items-start">
              <li class="mb-3 col-md-6">· 품목명 : {{ purchaseOrder.itemName }}</li>
              <li class="mb-3 col-md-6">· 품목 단가 : ₩ {{ purchaseOrder.purchaseOrderItemPrice != null ? purchaseOrder.purchaseOrderItemPrice.toLocaleString() : 0 }} </li>
              <li class="mb-3 col-md-6 d-flex align-items-center">· 주문 개수 :<input
                  type="number"
                  class="form-control form-control-sm ms-2" style="width: 70px;"
                  v-model.number="purchaseOrder.purchaseOrderItemQuantity"
                  @input="updatePrice(purchaseOrder.itemSeq)"
                  :placeholder="purchaseOrder.purchaseOrderItemQuantity ? '' : '수량 입력'"
                  :min="1"
              /></li>
              <li class="mb-3 col-md-6">· 품목 총 가격 : ₩ {{ purchaseOrder.calculatePrice != null ? purchaseOrder.calculatePrice.toLocaleString() : purchaseOrder.purchaseOrderItemPrice }} </li>
            </ul>
          </div>
          <div class="col-md-4 d-flex justify-content-center align-items-center">
            <img :src="purchaseOrder.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
          </div>

          <b-button
              @click="removeItem(purchaseOrder.itemSeq)"
              variant="light"
              size="sm"
              class="position-absolute btn-close"
              style="top: 10px; right: 10px;"
          ></b-button>
        </div>
      </div>

      <div v-if="calculatePrice != 0" class="line-container mx-5">
        <div class="custom-line d-flex justify-content-end">
            <h6 class="fw-bold" style="margin-top: 17px; float:right;">총 가격 : ₩ {{ calculatePrice }}</h6>
        </div>
      </div> <br />

      <span data-bs-toggle="modal" data-bs-target="#openItemModal">
      <b-input-group-text class="mx-5 my-3 d-flex justify-content-center align-items-center border border-secondary rounded p-3"
                          style="cursor: pointer;">
        <plusIcon class="icon"/>
      </b-input-group-text>
      </span>

      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button v-if="props" @click="updatePurchaseOrder" variant="light" size="sm" class="button ms-2">수정</b-button>
        <b-button v-else @click="createPurchaseOrder" variant="light" size="sm" class="button ms-2">등록</b-button>

      </div>
    </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="openItemModal" tabindex="-1" aria-labelledby="OrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">품목 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll" class="d-flex row justify-content-center align-items-center">

            <div class="list-headline row">
              <div class="list-head col-5">품목명</div>
              <div class="list-head col-2">가격</div>
              <div class="list-head col-2">품목 단위</div>
            </div>
            <template v-if="purchaseItemList.length > 0">
              <div v-for="purchaseItem in purchaseItemList" :key="purchaseItem.itemSeq" class="list-line row" @click="addToOrderList(purchaseItem)">
                <div class="list-body col-5 left">{{ purchaseItem.itemName }}</div>
                <div class="list-body col-2">{{ dayjs(purchaseItem.itemPrice).format('YYYY/MM/DD') }}</div>
                <div class="list-body col-2">{{ purchaseItem.itemUnitTitle }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 품목이 존재하지 않습니다.</b-card-text>
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

.line-container {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.custom-line {
  width: 100%;
  height: 1px;
  background-color: #828282;
  border-radius: 1px;
}

</style>
