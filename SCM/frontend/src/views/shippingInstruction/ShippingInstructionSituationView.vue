<script setup>
import ShippingInstructionSituation from "@/components/shippingInstruction/ShippingInstructionSituation.vue";
import axios from "@/axios"
import {onMounted, ref} from "vue";
import ShippingInstructionList from "@/components/shippingInstruction/ShippingInstructionList.vue";

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const shippingInstructionSituationList = ref([]);
const shippingInstructionSituationTotal = ref(null);
const shippingAddressList = ref([]);

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

// 출하주소 목록 요청
const fetchShippingAddressList = async () => {
  try {
    const response = await axios.get(`shipping-instruction/address`, {});

    shippingAddressList.value = response.data;

  } catch (error) {
    console.error("출하주소 목록 불러오기 실패 :", error);
  }
};

// 엑셀 다운 요청
const excelDown = async () => {
  try {
    const response = await axios.get(`shipping-instruction/excel`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        clientName: searchName.value,
        shippingInstructionStatus: null,
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      },
      responseType: "blob", // 중요: blob 형식으로 설정
    });

    const blob = new Blob([response.data], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    });

    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = decodeURIComponent(response.headers["content-disposition"].split('filename=')[1]);
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("출하지시서 현황 엑셀다운로드 실패 :", error);
  }
}

// 거래처 힌트 요청
const clientHintList = ref(null);
let clientSearchCount = 0;

const fetchClientHint = async (clientName) => {
  if (clientName.value === "") {
    clientHintList.value = null;
  } else {
    try {
      const response = await axios.get(`client/hint`, {
        params: {
          keyword: clientName.value
        }
      });
      if (response.data.length > 0) {
        clientHintList.value = response.data;
        clientSearchCount = 0;
      } else if (clientSearchCount > 2) {
        clientHintList.value = null;
      } else { clientSearchCount++; }
    } catch (error) {
      console.log(`거래처 힌트 요청 실패 ${error}`)
    }
  }
  if (clientHintList.value) {
    if (clientHintList.value.length === 1 && clientHintList.value[0] === searchClient.value) {
      clientHintList.value = null;
    }
  }
}

onMounted(async () => {
  await fetchShippingInstructionSituationList();
  await fetchShippingAddressList()
});

// 거래처 추천
const handleClient = (newClient) => {
  fetchClientHint(newClient);
}

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
                                :shippingAddressList="shippingAddressList"
                                :clientHintList="clientHintList"
                                @searchEvent="handleSearch"
                                @clientEvent="handleClient"
                                @excelEvent="excelDown"/>
</template>

<style scoped>

.title {
  padding-bottom: 20px;
}

</style>
