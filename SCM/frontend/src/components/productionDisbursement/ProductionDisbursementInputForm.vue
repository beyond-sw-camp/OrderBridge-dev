<script setup>
import {ref, onMounted, watch, defineProps} from 'vue';
import axios from '@/axios';
import searchIcon from '@/assets/searchIcon.svg';
import dayjs from 'dayjs';
import { Modal } from 'bootstrap';
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  productionDisbursementDetail: {type: Object, required: false},       // 생산불출 목록
  productionDisbursementItem: {type: Array, required: false},       // 생산불출 목록
  isEditMode: {type: Boolean, required: false},       // 수정모드 여부
});

const workOrderList = ref([]); // 작업지시서 목록
const totalCount = ref(0); // 총 작업지시서 수
const pageSize = ref(10); // 페이지 크기
const pageNumber = ref(1); // 페이지 번호

const workOrderStatusList = ref([]); // 작업지시서 상태 목록
const workOrderDueDate = ref(''); // 작업지시서 납기일
const workOrderIndicatedDate = ref(''); // 작업지시서 지시일

const bomItems = ref([]);

// 폼 데이터
const formData = ref({
  workOrder: '',
  workOrderSeq: '',
  factoryName: '',

  storeName: '',
  productionDisbursementDepartureDate: '',
  productionDisbursementNote: '',
  productionDisbursementItemSeq: '',
  productionDisbursementItemQuantity: '',
  productionDisbursementItemNote: '',
  itemRequests: []
});

watch(pageNumber, () => {
  fetchWorkOrderList();
});

watch(
    [() => props.productionDisbursementDetail, () => props.productionDisbursementItem],
    ([newDetail, newItems]) => {
      if (newDetail) {
        formData.value.workOrder = newDetail.workOrderName || '';
        formData.value.workOrderSeq = newDetail.workOrderSeq || '';
        workOrderDueDate.value = newDetail.workOrderDueDate || '';
        formData.value.factoryName = newDetail.factoryName || '';
        formData.value.storeName = newItems?.[0]?.storeName || '';
        workOrderIndicatedDate.value = newDetail.workOrderIndicatedDate || '';
        formData.value.productionDisbursementDepartureDate = newDetail.productionDisbursementDepartureDate || '';
        formData.value.productionDisbursementNote = newDetail.productionDisbursementNote || '';
        formData.value.productionDisbursementItemSeq = newDetail.productionDisbursementItemSeq || '';
        formData.value.productionDisbursementItemQuantity = newDetail.productionDisbursementItemQuantity || '';
        formData.value.productionDisbursementItemNote = newItems?.[0]?.note || '';
      }

      if (newItems && Array.isArray(newItems)) {
        // productionDisbursementItem -> formData.itemRequests 매핑
        formData.value.itemRequests = newItems.map(item => ({
          itemSeq: item.childItemSeq || item.itemSeq ,
          itemName: item.childItemName || item.itemName ,
          storeName: item.storeName,
          bomChildItemQuantity: item.bomChildItemQuantity,
          note: item.note ,
          itemImageUrl: item.itemImageUrl
        }));

        // productionDisbursementItem -> bomItems 매핑
        bomItems.value = newItems.map(item => ({
          childItemSeq: item.childItemSeq || item.itemSeq ,
          childItemName: item.childItemName || item.itemName ,
          storeName: item.storeName ,
          bomChildItemQuantity: item.bomChildItemQuantity ,
          note: item.note ,
          itemImageUrl: item.itemImageUrl
        }));
      }
    },
    { immediate: true }
);

// 작업지시서 목록 조회
const fetchWorkOrderList = async () => {
  try {
    const response = await axios.get('workOrder', {
      params: {
        workOrderStatus: 'AFTER',
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

    workOrderList.value = response.data.workOrderList;
    totalCount.value = response.data.totalItems;
  } catch (error) {
    console.error('작업지시서 불러오기 실패:', error);
  }
};

// 작업지시서 상태 목록 요청
const fetchWorkOrderStatusList = async () => {
  try {
    const response = await axios.get(`workOrder/status`, {});

    workOrderStatusList.value = response.data;

  } catch (error) {
    console.error("작업지시서 상태 목록 불러오기 실패 :", error);
  }
};

// 작업지시서 선택
const selectWorkOrder = (workOrder) => {
  selectWorkOrder.value = workOrder;

  // 폼 데이터에 작업지시서 정보 저장
  formData.value.workOrderSeq = workOrder.workOrderSeq;
  formData.value.workOrder = workOrder.workOrderName;
  formData.value.factoryName = workOrder.warehouseName;
  workOrderIndicatedDate.value = workOrder.workOrderIndicatedDate;

  if (workOrder && workOrder.workOrderDueDate) {
    workOrderDueDate.value = workOrder.workOrderDueDate  // 납기일 설정
  }
  console.log('선택된 작업지시서:', selectWorkOrder.value);

  fetchBomItems(workOrder.itemSeq);
};

const openWorkOrderModal = async () => {
  await fetchWorkOrderList(); // 작업지시서 목록 불러오기
  await fetchWorkOrderStatusList(); //상태 불러오기

  const modalElement = document.getElementById('WorkOrderModal');
  if (modalElement) {
    const modalInstance = new Modal(modalElement);
    modalInstance.show();
  }
};

onMounted(() => {
  fetchWorkOrderList();
  fetchWorkOrderStatusList();
});

// 상태 키로 값 반환
function findStatusValue(array, key) {
  for (const item of array) {
    if (item.key === key) {
      return item.value
    }
  }
}

// bom 품목 조회
const fetchBomItems = async (itemSeq) => {
  try {
    const response = await axios.get(`item/bom-item/${itemSeq}`, {});
    console.log('BOM 품목 데이터:', response.data);

    const fetchedItems = response.data.map(item => ({
      childItemSeq: item.childItemSeq,
      childItemName: item.childItemName,
      bomChildItemQuantity: item.bomChildItemQuantity,
      parentItemName: item.parentItemName,
      storeName: item.storeName,
      note: ''
    }));

    // 기존 bomItems 배열에 추가
    bomItems.value.push(...fetchedItems);

    // formData에 반영
    formData.value.itemRequests.push(...fetchedItems.map(item => ({
      itemSeq: item.childItemSeq,
      itemName: item.childItemName,
      storeName: item.storeName,
      bomChildItemQuantity: item.bomChildItemQuantity,
      note: item.note
    })));

    console.log('bomItems:', bomItems.value);

  } catch (error) {
    console.error("bom 품목 불러오기 실패 :", error);
  }
};

// 비고 입력 시 formData 업데이트
const updateNote = (index, note) => {
  formData.value.itemRequests[index].note = note;
};

// 생산불출 등록
const createProductionDisbursement = async () => {
  try {
    console.log('폼 데이터:', formData.value);

    const response = await axios.post(`productionDisbursement`, {
      workOrderSeq: formData.value.workOrderSeq,
      ingredientsWarehouseSeq: formData.value.storeName,
      productionDisbursementDepartureDate: formData.value.productionDisbursementDepartureDate,
      productionDisbursementNote: formData.value.productionDisbursementNote,
      itemRequests: formData.value.itemRequests // BOM 기반 품목 리스트
    });
    console.log(response.data);
    alert('생산불출이 등록되었습니다!');
    await router.push('/production-disbursement')

  } catch (error) {
    console.error("생산불출 등록 실패 :", error);

    if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_004') {
      alert('불출일을 입력해주세요');
    } else if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_003') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert('불출일은 작업지시일 이후고, 작업목표일 전이어야 합니다.');
    } else if (error.response.data.errorCode === 'STOCK_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'WORK_ORDER_STATE_BAD_REQUEST') {
      alert('작업지시서의 상태가 결재 후일때만 등록 가능합니다.');
    } else {
      alert('등록에 실패했습니다. 다시 시도해주세요.');
    }
  }
};

// 생산불출 수정
const updateProductionDisbursement = async (productionDisbursementSeq) => {
  if (!productionDisbursementSeq) {
    console.warn('productionDisbursementSeq 값이 존재하지 않습니다.');
    return;
  }
  console.log("수정요청 생산불출 번호", productionDisbursementSeq);
  console.log("보내는 데이터:", {
    workOrderSeq: formData.value.workOrderSeq,
    ingredientsWarehouseSeq: formData.value.storeName,
    productionDisbursementDepartureDate: formData.value.productionDisbursementDepartureDate,
    productionDisbursementNote: formData.value.productionDisbursementNote,
    itemRequests: formData.value.itemRequests
  });

  try {
    const response = await axios.put(`productionDisbursement/${productionDisbursementSeq}`, {
      workOrderSeq: formData.value.workOrderSeq,
      ingredientsWarehouseSeq: formData.value.storeName,
      productionDisbursementDepartureDate: formData.value.productionDisbursementDepartureDate,
      productionDisbursementNote: formData.value.productionDisbursementNote,
      itemRequests: formData.value.itemRequests // BOM 기반 품목 리스트
    });
    console.log('itemRequests: ',formData.value.itemRequests);
    alert('생산불출이 수정되었습니다!');
    await router.push('/production-disbursement')

  } catch (error) {
    console.error("생산불출 수정 실패 :", error);
    if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_002') {
      alert('생산불출의 상태가 불출 전일때만 수정 가능합니다.');
    } else if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_003') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_004') {
      alert('불출일을 입력해주세요');
    } else if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert('불출일은 납기일보다 전이고, 작업지시일 이후여야 합니다.');
    }else if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert('불출일은 납기일보다 전이고, 작업지시일 이후여야 합니다.');
    } else if (error.response.data.errorCode === 'STOCK_ERROR_001') {
      alert(error.response.data.message);
    } else if (error.response.data.errorCode === 'SECURITY_ERROR_001') {
      alert('작성자만 수정 가능합니다.');
    } else {
      alert('수정에 실패했습니다. 다시 시도해주세요.');
    }
  }
};

</script>

<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/work-order')" variant="light" size="sm" class="button ms-2">목록</b-button>
  </div>
  <div class="d-flex justify-content-center">
    <div class="col-6 d-flex flex-column">
      <!-- 작업지시서 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="작업지시서" label-for="workOrder">
        <b-input-group class="w-75" size="sm"  @click="openWorkOrderModal"  data-bs-toggle="modal" data-bs-target="#workOrderModal">
          <b-form-input type="text" id="salesOrder" placeholder="작업지시서를 선택해 주세요."  v-model="formData.workOrder" readonly=""/>
          <b-input-group-text>
            <searchIcon class="icon"/>
          </b-input-group-text>
        </b-input-group>
      </b-form-group>
      <!-- 작업지시일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="작업지시일" label-for="workOrderIndicatedDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="workOrderIndicatedDate"
               v-model="workOrderIndicatedDate" readonly=""/>
      </b-form-group>
      <!-- 작업목표일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="작업목표일" label-for="workOrderDueDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="workOrderDueDate"
               v-model="workOrderDueDate" readonly=""/>
      </b-form-group>
      <!-- 생산불출일자 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="생산불출일" label-for="productionDisbursementDepartureDate">
        <input class="form-control form-control-sm w-75" type="datetime-local" id="productionDisbursementDepartureDate"
               v-model="formData.productionDisbursementDepartureDate"/>
      </b-form-group>
      <!-- 생산공장명 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="생산공장명" label-for="workOrderWarehouse">
        <input class="form-control form-control-sm w-75" type="text" id="workOrderWarehouse"
               v-model="formData.factoryName" readonly=""/>
      </b-form-group>
      <!-- 비고 -->
      <b-form-group label-cols="4" label-cols-lg="2" label="비고" label-for="productionDisbursementNote">
        <input class="form-control form-control-sm w-75" type="text" id="productionDisbursementNote"
               v-model="formData.productionDisbursementNote" />
      </b-form-group>
      <!-- 작업지시번호 -->
      <b-form-group label-cols="4" label-cols-lg="2" label-for="workOrderSeq">
        <input class="form-control form-control-sm w-75" type="hidden" id="workOrderSeq"
               v-model="formData.workOrderSeq" readonly=""/>
      </b-form-group>
    </div>
  </div>

  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>

  <!-- 생산불출 bom 품목 표시-->
  <div v-if="bomItems.length > 0" class="d-flex justify-content-center">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">불출 품목</h5>
      <!-- 품목 반복 -->
      <div v-for="(item, index) in bomItems" :key="item.childItemSeq" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
          <div class="col-md-8">
            <ul class="d-flex flex-wrap align-items-start">
              <li class="mb-3 col-md-12 d-flex align-items-center">· 품목명 : {{ item.childItemName  }}</li>
              <li class="mb-3 col-md-12 d-flex align-items-center">· 공장명 : {{ item.storeName }}</li>
              <li class="mb-3 col-md-6">· BOM 수량 : {{ item.bomChildItemQuantity }}</li>
              <li class="mb-3 col-md-12 d-flex align-items-center">
                <span class="me-3 text-nowrap">· 품목 비고 : </span>
                <input v-model="item.note" type="text" placeholder="비고를 입력해주세요" @input="updateNote(index, item.note)"
                       class="form-control form-control-sm"/>
              </li>
            </ul>
          </div>
          <!-- 이미지 -->
          <div class="col-md-4 d-flex flex-column justify-content-around align-items-center">
            <img :src="item.itemImageUrl" alt="Item Image" class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 생산불출 등록 버튼 (화면 하단 고정) -->
    <div class="mx-5 my-3 d-flex justify-content-end">
      <b-button v-if="props.isEditMode"
                @click="updateProductionDisbursement(props.productionDisbursementDetail.productionDisbursementSeq)"
                variant="light" size="sm" class="button ms-2">수정</b-button>
      <b-button v-else @click="createProductionDisbursement" variant="light" size="sm" class="button ms-2">등록</b-button>
    </div>

  <!-- 작업지시서 Modal -->
  <div class="modal fade" id="WorkOrderModal" tabindex="-1" aria-labelledby="WorkOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">작업지시서 선택</h1>
          <div class="ms-5">검색결과: {{ totalCount }}개</div>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: auto"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-md-4">작업지시서명</div>
              <div class="list-head col-md-2">생산공장명</div>
              <div class="list-head col-md-2">지시일</div>
              <div class="list-head col-md-2">납기일</div>
              <div class="list-head col-md-2">상태</div>
            </div>
            <template v-if="workOrderList.length > 0">
              <div v-for="(workOrder) in workOrderList"
                   :key="workOrder.workOrderSeq"
                   class="list-line row" data-bs-dismiss="modal" @click="selectWorkOrder(workOrder)">
                <div class="list-body col-md-4 left">
                  {{ workOrder.workOrderName }}
                  <br>
                  <div v-if="!workOrder.itemName"><br></div>
                  <div v-else>{{ workOrder.itemName }}</div>
                </div>
                <div class="list-body col-md-2">
                    {{ workOrder.warehouseName }}
                </div>
                <div class="list-body col-md-2">{{
                    dayjs(workOrder.workOrderIndicatedDate).format('YYYY/MM/DD')
                  }}
                </div>
                <div class="list-body col-md-2">{{
                    dayjs(workOrder.workOrderDueDate).format('YYYY/MM/DD')
                  }}
                </div>
                <div class="list-body col-md-2">{{ findStatusValue(workOrderStatusList, workOrder.workOrderStatus) }}</div>
              </div>
            </template>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 작업지시서가 존재하지 않습니다.</b-card-text>
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