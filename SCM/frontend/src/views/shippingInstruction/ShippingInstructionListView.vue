<script setup>
import ShippingInstructionList from "@/components/shippingInstruction/ShippingInstructionList.vue";
import {onMounted, ref, watch} from "vue";
import axios from "axios";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const shippingInstructionList = ref([]);
const shippingInstructionStatusList = ref([]);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const searchStatus = ref(new Set([]));

const fetchShippingInstructionList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/shipping-instruction`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        clientName: searchName.value,
        shippingInstructionStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value),
        page: pageNumber.value,
        size: pageSize.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response);
    shippingInstructionList.value = response.data.shippingInstructionList;
    totalCount.value = response.data.totalItems;

  } catch (error) {
    console.error("출하지시서 불러오기 실패 :", error);
  }
};

onMounted(() => {
  fetchShippingInstructionList();
});

watch([searchStartDate, searchEndDate], () => {
  search();
})

watch(pageNumber, () => {
  fetchShippingInstructionList();
})

function check(status) {
  if (searchStatus.value.has(status)) {
    searchStatus.value.delete(status);
  } else {
    searchStatus.value.add(status);
  }

  search();
}

function search() {
  pageNumber.value = 1;

  fetchShippingInstructionList();
}
</script>

<template>
  <h4 class="title">영업관리 > 출하지시서 조회</h4>
  <ShippingInstructionList :searchStartDate="searchStartDate"
                           :searchEndDate="searchEndDate"
                           :searchName="searchName"
                           :shippingInstructionList="shippingInstructionList"
                           :totalCount="totalCount"
                           :pageNumber="pageNumber"
                           :pageSize="pageSize"/>
</template>

<style scoped>

.title {
  padding-bottom: 20px;
}

</style>
