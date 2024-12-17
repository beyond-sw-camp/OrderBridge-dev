<script setup>
import {onMounted, ref} from "vue";
import searchIcon from '@/assets/searchIcon.svg'
import dayjs from "dayjs";
import axios from "axios";
import router from "@/router/index.js";

const workOrderTotalCount = ref(0);
const workOrderPageSize = ref(10);
const workOrderPageNumber = ref(1);
const workOrderList = ref([]);

const fetchWorkOrderList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/workOrder`, {
      params: {
        startDate: null,
        endDate: null,
        warehouseName: null,
        workOrderStatus: 'COMPLETE',
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

    console.log(response);
    workOrderList.value = response.data.workOrderList;
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

const productionWarehouseList = ref([]);
const productionWarehouseTotalCount = ref(0);
const productionWarehousePageSize = ref(10);
const productionWarehousePageNumber = ref(1);

const fetchProductionWarehouseList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/warehouse`, {
      params: {
        warehouseName: null,
        warehouseType: 'PRODUCTION',
        page: productionWarehousePageNumber.value,
        size: productionWarehousePageSize.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response);
    productionWarehouseList.value = response.data.warehouseList;
    productionWarehouseTotalCount.value = response.data.totalCount;

  } catch (error) {
    console.error("생산 창고 불러오기 실패 :", error);
  }
};

// 생산창고 페이지 이동
const productionHandlePage = (pageNumber) => {
  productionWarehousePageNumber.value = Number(pageNumber.value);
  fetchProductionWarehouseList();
};

const storeWarehouseList = ref([]);
const storeWarehouseTotalCount = ref(0);
const storeWarehousePageSize = ref(10);
const storeWarehousePageNumber = ref(1);

const fetchStoreWarehouseList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/warehouse`, {
      params: {
        warehouseName: null,
        warehouseType: 'STORE',
        page: storeWarehousePageNumber.value,
        size: storeWarehousePageSize.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response);
    storeWarehouseList.value = response.data.warehouseList;
    storeWarehouseTotalCount.value = response.data.totalCount;

  } catch (error) {
    console.error("보관 창고 불러오기 실패 :", error);
  }
};

// 보관창고 페이지 이동
const storeHandlePage = (pageNumber) => {
  storeWarehousePageNumber.value = Number(pageNumber.value);
  fetchStoreWarehouseList();
};

onMounted(() => {
  fetchWorkOrderList();
  fetchProductionWarehouseList();
  fetchStoreWarehouseList();
});

const formData = ref({
  productionWarehouseSeq: 0,
  storeWarehouseSeq: 1,
  workOrderSeq: 0,
  productionReceivingExtendedPrice: 0,
  productionReceivingNote: '',
  productionReceivingReceiptDate: ''
});

const productionWarehouseName = ref("");
const addProductionWarehouse = (index) => {
  formData.value.productionWarehouseSeq = productionWarehouseList.value[index].warehouseSeq;
  productionWarehouseName.value = productionWarehouseList.value[index].warehouseName;
}

const storeWarehouseName = ref("");
const addStoreWarehouse = (index) => {
  formData.value.storeWarehouseSeq = storeWarehouseList.value[index].warehouseSeq;
  storeWarehouseName.value = storeWarehouseList.value[index].warehouseName;
}

const addWorkOrder = (index) => {
  formData.value.productionWarehouseSeq = workOrderList.value[index].warehouseSeq;
  formData.value.workOrderSeq = workOrderList.value[index].workOrderSeq;
  productionWarehouseName.value = workOrderList.value[index].warehouseName;
  fetchWorkOrder(formData.value.workOrderSeq);
}

const workOrderDetail = ref();
const workOrderItem = ref();
const workOrderName = ref("");
const itemQuantity = ref(0);
const itemUnitPrice = ref(0);
const itemNote = ref('');

const fetchWorkOrder = async (workOrderSeq) => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/workOrder/${workOrderSeq}`);

    console.log(response);
    workOrderDetail.value = response.data.workOrderDetail;
    workOrderItem.value = response.data.workOrderItem;
    workOrderName.value = response.data.workOrderDetail.workOrderName;
    itemQuantity.value = response.data.workOrderDetail.workOrderIndicatedQuantity;
  } catch (error) {
    console.error("보관 창고 불러오기 실패 :", error);
  }
};

const productionReceivingItemList = ref([]);
const createProductionReceiving = async () => {

  if(formData.value.productionWarehouseSeq === 0) {
    alert("생산공장을 선택해주세요.")
    return;
  }
  if(formData.value.storeWarehouseSeq === 0) {
    alert("보관창고를 선택해주세요.")
    return;
  }
  if(formData.value.workOrderSeq === 0) {
    alert("작업지시서를 선택해주세요.")
    return;
  }
  if(formData.value.productionReceivingReceiptDate === 0) {
    alert("생산입고일을 지정해주세요.")
    return;
  }

  const productionReceivingItem = {
    itemSeq: workOrderItem.itemSeq,
    productionReceivingItemQuantity: itemQuantity.value,
    productionReceivingUnitPrice: itemUnitPrice.value,
    productionReceivingItemNote: itemNote.value,
  }
  productionReceivingItemList.value.push(productionReceivingItem);

  console.log(productionWarehouseList.value);
  try {
    const response = await axios.post('http://localhost:8090/api/v1/productionReceiving',
        {
          productionWarehouseSeq: formData.value.productionWarehouseSeq,
          storeWarehouseSeq: formData.value.storeWarehouseSeq,
          workOrderSeq: formData.value.workOrderSeq,
          productionReceivingExtendedPrice: itemQuantity.value * itemUnitPrice.value,
          productionReceivingNote: formData.value.productionReceivingNote,
          productionReceivingReceiptDate: formData.value.productionReceivingReceiptDate,

          productionReceivingItemList: productionReceivingItemList.value.push(workOrderItem => ({
            itemSeq: workOrderItem.itemSeq,
            productionReceivingItemQuantity: itemQuantity.value,
            productionReceivingUnitPrice: itemUnitPrice.value,
            productionReceivingItemNote: itemNote.value,
          })),
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
</script>

<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/productionReceiving')" variant="light" size="sm" class="button ms-2">목록</b-button>
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-6 d-flex flex-column">
      <!-- 생산공장 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="생산 공장" label-for="productionWarehouse">
        <b-input-group class="w-75" size="sm">
          <b-form-input
              type="text"
              id="productionWarehouse"
              placeholder="생산 공장을 선택해 주세요."
              v-model="productionWarehouseName"
              readonly="true">
          </b-form-input>
          <b-input-group-text>
            <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#productionWarehouseModal"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
      <!-- 보관창고 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="보관 창고" label-for="storeWarehouse">
        <b-input-group class="w-75" size="sm">
          <b-form-input
              type="text"
              id="storeWarehouse"
              placeholder="보관 창고를 선택해 주세요."
              v-model="storeWarehouseName"
              readonly="true">
          </b-form-input>
          <b-input-group-text>
            <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#storeWarehouseModal"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
      <!-- 작업지시서 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="작업지시서" label-for="workOrder">
        <b-input-group class="w-75" size="sm">
          <b-form-input
              type="text"
              id="workOrder"
              placeholder="작업 지시서를 선택해 주세요."
              v-model="workOrderName"
              readonly="true">
          </b-form-input>
          <b-input-group-text>
            <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#workOrderModal"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
      <!-- 생산입고일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="생산입고일" label-for="productionReceivingReceiptDate">
        <!--   b-form-input 에서  datetime-local 을 사용할 수 없음  -->
        <input
            class="form-control form-control-sm w-75"
            type="datetime-local"
            id="productionReceivingReceiptDate"
            v-model="formData.productionReceivingReceiptDate"
            placeholder="생산입고일자를 선택해 주세요.">
      </b-form-group>
      <!-- 생산입고 총액 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="생산입고 총액" label-for="productionReceivingExtendedPrice">
        <b-form-input
            class="w-75"
            size="sm"
            type="number"
            id="productionReceivingExtendedPrice"
            v-model="formData.productionReceivingExtendedPrice"
            placeholder="생산입고 총액을 입력해주세요."
            readonly="">
        </b-form-input>
      </b-form-group>
      <!-- 생산입고 비고 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="생산입고 비고" label-for="productionReceivingNote">
        <b-form-input
            class="w-75"
            size="sm"
            type="text"
            id="productionReceivingNote"
            v-model="formData.productionReceivingNote"
            placeholder="생산입고 비고를 입력해주세요."
            readonly="">
        </b-form-input>
      </b-form-group>
    </div>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div v-if="workOrderItem" class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">물품 등록</h5>
      <div style="max-height: 250px; overflow-y: auto;">
        <div style="max-height: 200px;" class="mx-5 my-3 d-flex flex-row border border-secondary rounded">
          <b-img class="p-2 col-md-4" :src="workOrderItem.itemImageUrl" fluid alt="Responsive image"></b-img>
          <div class="p-2 col-md-8">
            <div class="mb-4 d-flex justify-content-between">
              <span class="fw-bold">{{ workOrderItem.itemName }}</span>
            </div>
            <div class="d-flex">
              <ul class="col-md-4">
                <li class="mb-3">
                  · 품목 구분 : {{ workOrderItem.itemDivision }}
                </li>
                <li class="mb-3">
                  · 품목 가격 :
                  <b-form-input class="ms-2 me-2 w-50" type="text" v-model="unitPrice" size="sm"></b-form-input>
                </li>
                <li class="mb-3 d-flex flex-row">
                  · 수량 :
                  <b-form-input class="ms-2 me-2 w-50" type="text" v-model="itemQuantity" size="sm"></b-form-input>
                  {{ workOrderItem.itemUnitTitle}}
                </li>
              </ul>
              <ul class="col-md-8 d-flex flex-column justify-content-between">
                <li class="d-flex flex-row">
                  · 비고 :
                  <b-form-input class="ms-2 w-75" type="text" v-model="itemNote" size="sm"></b-form-input>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button @click="createProductionReceiving" variant="light" size="sm" class="button ms-2">확인</b-button>
      </div>
    </div>
  </div>
  <!-- productionWarehouseModal -->
  <div class="modal fade" id="productionWarehouseModal" tabindex="-1" aria-labelledby="productionWarehouseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">생산 공장 선택</h1>
          <div class="ms-5">검색결과: {{ productionWarehouseTotalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">생산공장명</div>
              <div class="list-head col-2">구분</div>
              <div class="list-head col-2">공장 담당자</div>
              <div class="list-head col-2">등록일</div>
            </div>
            <template v-if="productionWarehouseList.length > 0">
              <div v-for="(productionWarehouse, index) in productionWarehouseList" :key="productionWarehouse.warehouseSeq" class="list-line row" data-bs-dismiss="modal" @click="addProductionWarehouse(index)">
                <div class="list-body col-6 left">{{ productionWarehouse.warehouseName }}</div>
                <div class="list-body col-2">{{ productionWarehouse.warehouseType }}</div>
                <div class="list-body col-2">{{ productionWarehouse.userName }}</div>
                <div class="list-body col-2">{{ dayjs(productionWarehouse.warehouseRegDate).format('YYYY/MM/DD') }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 생산공장이 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination
              v-model="productionWarehousePageNumber"
              :totalRows="productionWarehouseTotalCount"
              :perPage="productionWarehousePageSize">
          </b-pagination>
        </div>
      </div>
    </div>
  </div>
  <!-- storeWarehouseModal -->
  <div class="modal fade" id="storeWarehouseModal" tabindex="-1" aria-labelledby="storeWarehouseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">보관 창고 선택</h1>
          <div class="ms-5">검색결과: {{ storeWarehouseTotalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">보관창고명</div>
              <div class="list-head col-2">구분</div>
              <div class="list-head col-2">창고 담당자</div>
              <div class="list-head col-2">등록일</div>
            </div>
            <template v-if="storeWarehouseList.length > 0">
              <div v-for="(storeWarehouse, index) in storeWarehouseList" :key="storeWarehouse.warehouseSeq" class="list-line row" data-bs-dismiss="modal" @click="addStoreWarehouse(index)">
                <div class="list-body col-6 left">{{ storeWarehouse.warehouseName }}</div>
                <div class="list-body col-2">{{ storeWarehouse.warehouseType }}</div>
                <div class="list-body col-2">{{ storeWarehouse.userName }}</div>
                <div class="list-body col-2">{{ dayjs(storeWarehouse.warehouseRegDate).format('YYYY/MM/DD') }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 보관창고가 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination
              v-model="productionWarehousePageNumber"
              :totalRows="productionWarehouseTotalCount"
              :perPage="productionWarehousePageSize">
          </b-pagination>
        </div>
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
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">작업지시서명</div>
              <div class="list-head col-2">생산공장명</div>
              <div class="list-head col-2">작업지시일</div>
              <div class="list-head col-2">상태</div>
            </div>
            <template v-if="workOrderList.length > 0">
              <div v-for="(workOrder, index) in workOrderList" :key="workOrder.workOrderSeq" class="list-line row" data-bs-dismiss="modal" @click="addWorkOrder(index)">
                <div class="list-body col-6 left">{{ workOrder.workOrderName }}</div>
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
          <b-pagination
              v-model="workOrderPageNumber"
              :totalRows="workOrderTotalCount"
              :perPage="workOrderPageSize">
          </b-pagination>
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
