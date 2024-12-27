<script setup>
import {computed, defineProps, onMounted, ref, watch} from "vue";
import searchIcon from '@/assets/searchIcon.svg'
import dayjs from "dayjs";
import axios from "@/axios.js";
import router from "@/router/index.js";
import {Modal} from "bootstrap";

const pageSize = ref(10);
const pageNo = ref(1);
const purchaseList = ref([]);
const purchaseItemList = ref([]);

const totalCount = ref(null);

const calculatePrice = computed(() => {
  return purchaseItemList.value.reduce((total, item) => {
    return total + (item.purchaseItemPrice || 0) * (item.purchaseItemQuantity || 1);
  }, 0);
});

const formData = ref({
  purchaseOrderSeq: 0,
  purchaseName: '',
  purchaseContractDate: '',
  purchaseExtendedPrice: '',
  purchaseTotalQuantity: '',
  purchaseNote: '',
  purchaseItemResponseList: {}
});

const fetchPurchaseList = async () => {
  try {
    const response = await axios.get(`purchaseOrder`, {
      params: {
        searchStartDate: null,
        searchEndDate: null,
        searchName: null,
        searchStatus: '',
        pageNo: pageNo.value
      }, paramsSerializer: (params) => {
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    purchaseList.value = response.data.purchaseOrderResponseList;
    totalCount.value = response.data.pagination.totalCount;

  } catch (error) {
    console.error("주문서 불러오기 실패 :", error);
  }
};

onMounted(() => {
  console.log()
  fetchPurchaseList();
});

function openModal() {
  const modal = document.getElementById('workOrderModal');
  if (modal) {
    const bootstrapModal = Modal.getInstance(modal);
    bootstrapModal.show();
  } else {
    console.error("Modal not found.");
  }
}

function closeModal() {
  const modal = document.getElementById('workOrderModal');
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

const removeItem = (salesOrderSeq) => {
  purchaseItemList.value = purchaseItemList.value.filter(
      (item) => item.itemSeq !== salesOrderSeq
  );

  delete purchaseItemList.value[salesOrderSeq];
};

const addToOrderList = (purchaseOrder) => {
  const existingItem = purchaseList.value.find(
      (item) => item.itemSeq === purchaseOrder.purchaseOrderSeq
  );

  const purchaseOrderInput = document.getElementById('workOrder');
  if (purchaseOrderInput) {
    purchaseOrderInput.value = purchaseOrder.purchaseOrderName;
    formData.value.purchaseOrderSeq = purchaseOrder.purchaseOrderSeq;
  }

  if (!existingItem) {
    purchaseItemList.value = [];
    for (let i=0 ; i<purchaseOrder.purchaseOrderItemResponseList.length ; i++) {
      purchaseItemList.value.push({
        itemSeq: purchaseOrder.purchaseOrderItemResponseList[i].itemSeq,
        itemName: purchaseOrder.purchaseOrderItemResponseList[i].itemName,
        itemImageUrl: purchaseOrder.purchaseOrderItemResponseList[i].itemImageUrl,
        purchaseItemPrice: purchaseOrder.purchaseOrderItemResponseList[i].purchaseOrderItemPrice || 0,
        purchaseItemReceiptDate: '',
        purchaseItemQuantity: purchaseOrder.purchaseOrderItemResponseList[i].purchaseOrderItemQuantity || 0,
        calculatePrice: (purchaseOrder.purchaseOrderItemResponseList[i].purchaseOrderItemPrice || 0) * (purchaseOrder.purchaseOrderItemResponseList[i].purchaseOrderItemQuantity || 1) ,
      });
    };
  } else {
    alert("이미 추가된 주문입니다.");
  }

  closeModal();
};

function validationCheck() {
  if(document.getElementById('workOrder').value === '') {
    alert("발주서를 선택해주세요.");
    return false;
  }
  if(document.getElementById('purchaseContractDate').value === '') {
    alert("구매 계약일을 선택해주세요.")
    return false;
  }

  return true;
}

const createPurchase = async () => {
  if (validationCheck()) {
    try {
      const response = await axios.post('purchase',
          {
            purchaseOrderSeq: formData.value.purchaseOrderSeq,
            userSeq: 1,
            purchaseContractDate: formData.value.purchaseContractDate,
            purchaseExtendedPrice: calculatePrice.value,
            purchaseNote: formData.value.purchaseNote|| '',
            purchaseItemDtoList: purchaseItemList.value,
          });

      alert('구매서가 등록되었습니다!');
      await router.push('/purchase')

    } catch (error) {
      if(error.response.data.errorCode == "PURCHASE_ERROR_001") {
        alert("발주서의 상태가 결재후일 때만 등록이 가능합니다.");
      }
      console.error('구매서 등록 실패', error);
      throw error;
    }
  }
};

const updatePrice = (itemSeq) => {
  const item = purchaseItemList.value.find((order) => order.itemSeq === itemSeq);
  if (item) {
    item.calculatePrice = (item.purchaseItemPrice || 0) * (item.purchaseItemQuantity || 1);
    calculatePrice.value = item.calculatePrice;
  }
};

watch(pageNo, () => {
  fetchPurchaseList();
});


</script>

<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/purchase')" variant="light" size="sm" class="button ms-2">목록</b-button>
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
      <b-form-group label-cols="4" label-cols-lg="2" label="구매 계약일" label-for="purchaseContractDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="purchaseContractDate" v-model="formData.purchaseContractDate" placeholder="계약일을 선택해 주세요."/>
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="구매서 비고" label-for="purchaseOrderNote">
        <b-form-input class="w-75" size="sm" type="text" id="purchaseNote" v-model="formData.purchaseNote" placeholder="비고를 입력해주세요."/>
      </b-form-group>
    </div>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">구매서 품목
      </h5>
      <div v-for="purchase in purchaseItemList" :key="purchase.itemSeq" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
          <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h6 class="fw-bold">{{ purchase.itemName }}</h6>
            </div>
            <ul class="d-flex flex-wrap align-items-start">
              <li class="mb-3 col-md-6">· 품목명 : {{ purchase.itemName }}</li>
              <li class="mb-3 col-md-6">· 품목 가격 : ₩ {{ purchase.purchaseItemPrice != null ? purchase.purchaseItemPrice.toLocaleString() : 0 }} </li>
              <li class="mb-3 col-md-6 d-flex align-items-center">· 주문 개수 :<input
                  type="number"
                  class="form-control form-control-sm ms-2" style="width: 70px;"
                  v-model.number="purchase.purchaseItemQuantity"
                  @input="updatePrice(purchase.itemSeq)"
                  :placeholder="purchase.purchaseItemQuantity ? '' : '수량 입력'"
                  :min="1"
              /></li>
              <li class="mb-3 col-md-6">· 품목 총 가격 : ₩ {{ purchase.calculatePrice != null ? purchase.calculatePrice.toLocaleString() : purchase.purchaseItemPrice }} </li>
              <li class="mb-3 col-md-12 d-flex align-items-center"> · 품목 입고일 :
                <input class="form-control form-control-sm ms-2" v-model="purchase.purchaseItemReceiptDate" type="datetime-local" id="purchaseItemReceiptDate" placeholder="품목 입고일을 선택해 주세요." style="width: 40%;"/>
              </li>
            </ul>
          </div>
          <div class="col-md-4 d-flex justify-content-center align-items-center">
            <img :src="purchase.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
          </div>

          <b-button
              @click="removeItem(purchase.itemSeq)"
              variant="light"
              size="sm"
              class="position-absolute btn-close"
              style="top: 10px; right: 10px;"
          ></b-button>
        </div>
      </div>

      <div v-if="calculatePrice != 0" class="line-container mx-5">
        <div class="custom-line d-flex justify-content-end">
          <h6 class="fw-bold" style="margin-top: 17px; float:right;">총 가격 : ₩ {{ calculatePrice.toLocaleString() }}</h6>
        </div>
      </div> <br />

      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button v-if="props" @click="updatePurchase" variant="light" size="sm" class="button ms-2">수정</b-button>
        <b-button v-else @click="createPurchase" variant="light" size="sm" class="button ms-2">등록</b-button>

      </div>
    </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="workOrderModal" tabindex="-1" aria-labelledby="workOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">발주서 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" @click="closeModal" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll" class="d-flex row justify-content-center align-items-center">

            <div class="list-headline row">
              <div class="list-head col-4">발주서명</div>
              <div class="list-head col-2">수량</div>
              <div class="list-head col-2">금액</div>
              <div class="list-head col-2">등록일</div>
              <div class="list-head col-2">비고</div>
            </div>
            <template v-if="purchaseList.length > 0">
              <div v-for="purchase in purchaseList" :key="purchase.purchaseOrderSeq" class="list-line row" @click="addToOrderList(purchase)">
                <div class="list-body col-4 left">{{ purchase.purchaseOrderName }}</div>
                <div class="list-body col-2">{{ purchase.purchaseOrderTotalQuantity }} 개</div>
                <div class="list-body col-2">{{ purchase.purchaseOrderExtendedPrice }} 원</div>
                <div class="list-body col-2">{{ dayjs(purchase.purchaseRegDate).format('YYYY/MM/DD') }}</div>
                <div class="list-body col-2">{{ purchase.purchaseOrderNote }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 발주서가 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination v-model="pageNo" :totalRows="totalCount" :perPage="pageSize"/>
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
