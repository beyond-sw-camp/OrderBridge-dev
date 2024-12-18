<script setup>
import ProductionReceivingInputForm from "@/components/productionReceiving/ProductionReceivingInputForm.vue";
import {ref} from "vue";
import axios from "axios";

const itemList = ref([]);
const selectedWorkOrder = ref(false);

// 주문서 전달
const handleWorkOrder = (formData) => {
  fetchWorkOrder(formData.value.salesOrderSeq);
  selectedWorkOrder.value = true;
}

const fetchWorkOrder = async (workOrderSeq) => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/workOrder/${workOrderSeq}`, {
      paramsSerializer: (workOrderSeq) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(workOrderSeq).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response.data);
    itemList.value = response.data.salesOrderItem;

  } catch (error) {
    console.error("주문서 상세 품목 불러오기 실패 :", error);
  }
};

</script>

<template>
  <h4 class="title">생산관리 > 생산입고 등록</h4>
  <ProductionReceivingInputForm/>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>