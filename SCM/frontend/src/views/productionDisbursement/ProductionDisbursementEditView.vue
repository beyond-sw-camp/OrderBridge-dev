<script setup>
import ProductionDisbursementInputForm from "@/components/productionDisbursement/ProductionDisbursementInputForm.vue";
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import axios from "@/axios.js";

const route = useRoute();

const productionDisbursementDetail = ref();
const productionDisbursementItem = ref();
const isEditMode = ref(true); // 수정모드


// 날짜,시간 포맷 함수
const formatDateToInputDateTime = (dateString) => {
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

const fetchProductionDisbursementDetail = async (productionDisbursementSeq) => {
  try {
    const response = await axios.get(`productionDisbursement/${productionDisbursementSeq}`, {});
    productionDisbursementDetail.value = {
      ...response.data.productionDisbursementDetail,
      productionDisbursementDepartureDate: formatDateToInputDateTime(response.data.productionDisbursementDetail.productionDisbursementDepartureDate),
      workOrderIndicatedDate: formatDateToInputDateTime(response.data.productionDisbursementDetail.workOrderIndicatedDate),
    };

    productionDisbursementItem.value = response.data.itemList.map(item => ({
      childItemSeq: item.itemSeq,
      childItemName: item.itemName,
      storeName: item.storeName,
      bomChildItemQuantity: item.productionDisbursementQuantity,
      note: item.productionDisbursementNote,
      itemImageUrl: item.itemImageUrl
    }));


    if (productionDisbursementDetail.value.workOrderSeq) {
    }
  } catch (error) {
  }
};

onMounted(() => {
  fetchProductionDisbursementDetail(route.params.productionDisbursementSeq);
});

</script>

<template>
  <h4 class="title">생산관리 > 생산불출 {{ isEditMode ? '수정' : '등록' }}</h4>
  <ProductionDisbursementInputForm
      :productionDisbursementDetail="productionDisbursementDetail"
      :productionDisbursementItem="productionDisbursementItem"
      :isEditMode="isEditMode"/>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>