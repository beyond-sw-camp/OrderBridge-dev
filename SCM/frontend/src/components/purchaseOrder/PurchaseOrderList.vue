<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import searchIcon from "@/assets/searchIcon.svg";
import dayjs from "dayjs";

const totalCount = ref(0);
const purchaseOrderResponseList = ref([]);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const purchaseOrderStatus = ref("APPROVAL_BEFORE");
const pageNumber = ref(1);
const pageSize = ref(10);

const fetchPurchaseOrderList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/purchaseOrder`, {
      params: {
        searchStartDate: searchStartDate.value ? dayjs(searchStartDate.value).toISOString() : null,
        searchEndDate: searchEndDate.value ? dayjs(searchEndDate.value).toISOString() : null,
        clientName: searchName.value,
        purchaseOrderStatus: purchaseOrderStatus.value || null,
        pageNo: pageNumber.value-1,
      }
    });

    console.log(response.data);
    purchaseOrderResponseList.value = response.data.purchaseOrderResponseList.content;
    totalCount.value = response.data.pagination.totalCount;

  } catch (error) {
    console.error("발주서 불러오기 실패 :", error);
  }
};

onMounted(() => {
  fetchPurchaseOrderList();
});

watch([searchStartDate, searchEndDate], () => {
  search();
})

watch(pageNumber, () => {
  fetchPurchaseOrderList();
})

function search() {
  pageNumber.value = 1;

  fetchPurchaseOrderList();
}

const excelDown = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/purchaseOrder/excelDown`, {
      params: {
        searchStartDate: searchStartDate.value ? dayjs(searchStartDate.value).toISOString() : null,
        searchEndDate: searchEndDate.value ? dayjs(searchEndDate.value).toISOString() : null,
        clientName: searchName.value,
        purchaseOrderStatus: purchaseOrderStatus.value || null,
      }
    });

  } catch (error) {
    console.error("발주서 엑셀다운로드 실패 :", error);
  }
}
</script>

<template>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">목표 납기일</p>
          <input type="date" v-model="searchStartDate"/> ~ <input type="date" v-model="searchEndDate"/>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">거래처명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchName"></b-form-input>
            <b-button variant="light" class="button" @click="search()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">발주서 상태</p>
            <b-form-checkbox v-model="purchaseOrderStatus" value="APPROVAL_BEFORE">서명전</b-form-checkbox>
            <b-form-checkbox v-model="purchaseOrderStatus" value="APPROVAL_AFTER">서명후</b-form-checkbox>
            <b-form-checkbox v-model="purchaseOrderStatus" value="APPROVAL_REFUSAL">반려</b-form-checkbox>
            <b-form-checkbox v-model="purchaseOrderStatus" value="APPROVAL_COMPLETE">구매완료</b-form-checkbox>
            <b-form-checkbox v-model="purchaseOrderStatus" value="DELETE">삭제</b-form-checkbox>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div>
        <div class="d-flex justify-content-between">
          <div>검색결과: {{ totalCount }}개</div>
          <div class="d-flex justify-content-end mt-3">
            <b-button @click="excelDown" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
            <b-button variant="light" size="sm" class="button ms-2">생산입고 등록</b-button>
          </div>
        </div>
        <div class="list-headline row">
          <div class="list-head col-7">발주서명</div>
          <div class="list-head col-1">거래처명</div>
          <div class="list-head col-3">목표 납기일</div>
          <div class="list-head col-1">상태</div>
        </div>
        <template v-if="purchaseOrderResponseList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="purchaseOrder in purchaseOrderResponseList" :key="purchaseOrder.purchaseOrderSeq" class="list-line row" @click="itemExtend">
              <div class="list-body col-4 left">
                {{ purchaseOrder.purchaseOrderName }}
                <div v-if="purchaseOrder.purchaseOrderItemResponseList.length > 0">
                  <template v-for="(purchaseOrderItem, index) in purchaseOrder.purchaseOrderItemResponseList" :key="purchaseOrderItem.purchaseOrderItemSeq">
                    <template v-if="index === purchaseOrder.purchaseOrderItemResponseList - 1">
                      {{purchaseOrderItem.itemName}}
                    </template>
                    <template v-else>
                      {{purchaseOrderItem.itemName + ", "}}
                    </template>
                  </template>
                </div>
              </div>
              <div class="list-body col-2">{{ purchaseOrder.clientName }}</div>
              <div class="list-body col-3">{{ dayjs(purchaseOrder.purchaseOrderTargetDueDate).format('YYYY-MM-DD HH:mm:ss') }}</div>
              <div class="list-body col-1">{{ purchaseOrder.purchaseOrderStatus }}</div>
            </div>
          </div>


          <div class="pagination">
            <b-pagination
                v-model="pageNumber"
                :totalRows="totalCount"
                :perPage="pageSize">
            </b-pagination>
          </div>
        </template>
        <template v-else>
          <b-card-text class="no-list-text">해당 검색조건에 부합한 발주서가 존재하지 않습니다.</b-card-text>
        </template>
      </div>

    </div>
  </div>
</template>

<style scoped>

div {
  font-size: small;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.side-box {
  min-height: 100px;
  margin-top: 20px;
}

.card-title {
  font-size: medium;
  font-weight: bold;
}

.list-headline {
  border-bottom: 1px solid black;
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
}

.list-head {
  text-align: center;
}

.list-line {
  width: 99%;
  border: 1px solid Silver;
  border-radius: 8px;
  margin-left:1px;
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

.no-list-text {
  text-align: center;
  margin-top: 10%;
}

.icon {
  width: 20px;
  height: 20px;
}

</style>
