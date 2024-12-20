<script setup>
import ShippingInstructionSituation from "@/components/shippingInstruction/ShippingInstructionSituation.vue";
import axios from "@/axios"
import {onMounted, ref} from "vue";

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const shippingInstructionSituationList = ref([]);
const shippingInstructionSituationTotal = ref(null);

// 출하지시서 현황 요청
const fetchShippingInstructionSituationList = async () => {
  try {
    const response = await axios.get(`shipping-instruction/situation`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        clientName: searchName.value,
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });
    console.log(response.data);
    shippingInstructionSituationTotal.value = response.data.pop();
    shippingInstructionSituationList.value = response.data;
    console.log(response.data);
  } catch (error) {
    console.error("출하지시서 현황 불러오기 실패 :", error);
  }
};

// 현황 엑셀 다운 요청
const excelDown = async () => {
  const excelName = "출하지시서현황_" + new Date().getFullYear() + (new Date().getMonth() + 1) + new Date().getDay();
  try {
    const response = await axios.get(`shipping-instruction/situation/excelDown`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        clientName: searchName.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      },
      responseType: "blob", // 중요: blob 형식으로 설정
    });

    // Blob 객체 생성 및 다운로드 처리
    const blob = new Blob([response.data], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = decodeURIComponent(excelName); // 파일명 디코딩
    link.click();

    // Blob URL 해제
    URL.revokeObjectURL(link.href);

  } catch (error) {
    console.error("출하지시서 현황 엑셀다운로드 실패 :", error);
  }
}

onMounted(() => {
  fetchShippingInstructionSituationList();
});

// 검색
const handleSearch = (payload) => {
  searchStartDate.value = payload.startDate;
  searchEndDate.value = payload.endDate;
  searchName.value = payload.clientName;

  search();
};

function search() {
  fetchShippingInstructionSituationList();
}
</script>

<template>
  <h4 class="title">영업관리 > 출하지시서 현황 조회</h4>
  <ShippingInstructionSituation :searchStartDate="searchStartDate"
                                :searchEndDate="searchEndDate"
                                :searchName="searchName"
                                :shippingInstructionSituationList="shippingInstructionSituationList"
                                :shippingInstructionSituationTotal="shippingInstructionSituationTotal"
                                @searchEvent="handleSearch"
                                @excelEvent="excelDown"/>
</template>

<style scoped>

.title {
  padding-bottom: 20px;
}

</style>
