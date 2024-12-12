<script setup>
import ShippingInstructionList from "@/components/shippingInstruction/ShippingInstructionList.vue";
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import router from "@/router/index.js";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const shippingInstructionList = ref([]);
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

// 페이지 이동
const handlePage = (newPageNumber) => {
  pageNumber.value = Number(newPageNumber.value);
  fetchShippingInstructionList();
};

// 검색
const handleSearch = (payload) => {
  searchStartDate.value = payload.startDate;
  searchEndDate.value = payload.endDate;
  searchName.value = payload.clientName;

  search();
};

// 상태 검색
const handleStatus = (payload) => {
  if (searchStatus.value.has(payload)) {
    searchStatus.value.delete(payload);
  } else {
    searchStatus.value.add(payload);
  }

  search();
}

// 등록 페이지 이동
const handleRegister = () => {
  router.push("/shipping-instruction/input");
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
                           :pageSize="pageSize"
                           @pageEvent="handlePage"
                           @searchEvent="handleSearch"
                           @checkStatusEvent="handleStatus"
                           @extendItemEvent=""
                           @registerEvent="handleRegister"
  />
</template>

<style scoped>

.title {
  padding-bottom: 20px;
}

</style>
