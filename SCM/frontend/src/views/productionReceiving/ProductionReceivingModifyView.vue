<script setup>
import axios from "@/axios.js";
import ProductionReceivingInputForm from "@/components/productionReceiving/ProductionReceivingInputForm.vue";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
const route = useRoute();

const productionReceivingDetail = ref();
const productionReceivingItemList = ref([]);
const workOrderList = ref([]);
const fetchProductionReceiving = async (productionReceivingSeq) => {
  try {
    const response = await axios.get(`productionReceiving/${productionReceivingSeq}`, {});
    console.log(response.data);
    productionReceivingDetail.value = response.data.productionReceivingDTO;
    productionReceivingItemList.value = response.data.productionReceivingItemList;
    workOrderList.value = response.data.workOrderList;

  } catch (error) {
    console.error("상세 출하지시서 불러오기 실패 :", error);
  }
};

onMounted(() => {
  fetchProductionReceiving(route.params.productionReceivingSeq);
})

</script>

<template>
  <h4 class="title">생산관리 > 생산입고 수정</h4>
  <ProductionReceivingInputForm
      :productionReceivingDetail="productionReceivingDetail"
      :productionReceivingItemList="productionReceivingItemList"
      :workOrderList="workOrderList"/>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>