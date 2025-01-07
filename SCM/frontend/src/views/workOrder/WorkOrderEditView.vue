<script setup>
import axios from "@/axios.js";

import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import WorkOrderInputForm from "@/components/workOrder/WorkOrderInputForm.vue";
const route = useRoute();

const workOrderDetail = ref();
const workOrderItem = ref();
const salesOrder = ref();
const stockStatusList = ref([]);
const isEditMode = ref(true); // 수정모드

// 날짜 포맷 변환 함수
const formatDateToInput = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return '';

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  return `${year}-${month}-${day}T${hours}:${minutes}`;
};

const fetchWorkOrderDetail = async (workOrderSeq) => {
  try {
    const response = await axios.get(`workOrder/${workOrderSeq}`, {});
    workOrderDetail.value = {
      ...response.data.workOrderDetail,
      workOrderIndicatedDate: formatDateToInput(response.data.workOrderDetail.workOrderIndicatedDate),
      workOrderDueDate: formatDateToInput(response.data.workOrderDetail.workOrderDueDate),
    };
    workOrderItem.value = response.data.workOrderItem;

    // 주문서 상세 정보 조회
    if (workOrderDetail.value.salesOrderSeq) {
      await fetchSalesOrderById(workOrderDetail.value.salesOrderSeq);
    }
  } catch (error) {
  }
};

// 주문서 상세 조회
const fetchSalesOrderById = async (salesOrderSeq) => {
  try {
    const response = await axios.get(`sales-order/${salesOrderSeq}`);

    salesOrder.value = {
      ...response.data,
      salesOrderDueDate: formatDateToInput(response.data.salesOrderDueDate),
    };
    stockStatusList.value = response.data.salesOrderItem; // 주문서 품목 목록 저장
  } catch (error) {
  }
};

onMounted(() => {
  fetchWorkOrderDetail(route.params.workOrderSeq);
});

</script>

<template>
  <h4 class="title">생산관리 > 작업지시서 {{ isEditMode ? '수정' : '등록' }}</h4>
  <WorkOrderInputForm
    :workOrderDetail="workOrderDetail"
    :workOrderItem="workOrderItem"
    :salesOrder="salesOrder"
    :stockStatusList="stockStatusList"
    :isEditMode="isEditMode" />
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>