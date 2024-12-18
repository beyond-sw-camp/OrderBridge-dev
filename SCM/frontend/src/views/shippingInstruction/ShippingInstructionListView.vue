<script setup>
import ShippingInstructionList from "@/components/shippingInstruction/ShippingInstructionList.vue";
import {onMounted, reactive, ref, watch} from "vue";
import axios from "axios";
import router from "@/router/index.js";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const shippingInstructionList = ref([]);
const shippingInstructionStatusList = ref([]);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const searchStatus = ref(new Set([]));
const expandShippingInstruction = ref({});
const expandItemList = ref({});

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

    shippingInstructionList.value = response.data.shippingInstructionList;
    totalCount.value = response.data.totalItems;

  } catch (error) {
    console.error("출하지시서 불러오기 실패 :", error);
  }
};

const fetchShippingInstructionStatusList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/shipping-instruction/status`, {});

    shippingInstructionStatusList.value = response.data;

  } catch (error) {
    console.error("출하지시서 상태 리스트 불러오기 실패 :", error);
  }
};

const fetchShippingInstruction = async (seq) => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/shipping-instruction/${seq}`, {});

    expandShippingInstruction.value[seq] = response.data.shippingInstructionDTO; // ref 값에 추가
    expandItemList.value[seq] = response.data.itemList;

  } catch (error) {
    console.error("상세 출하지시서 불러오기 실패 :", error);
  }
};

const deleteShippingInstruction = async (seq) => {
  console.log(seq);
  try {
    const response = await axios.delete(`http://localhost:8090/api/v1/shipping-instruction/${seq}`, {});

    alert("출하지시서가 삭제되었습니다.");

  } catch (error) {
    if (error.response) {
      // 서버에서 반환된 상태 코드에 따른 처리
      if (error.response.status === 400) {
        console.error("상세 출하지시서 삭제 실패 : 결재후");
        alert("이미 결재 후이므로 삭제하실 수 없습니다.");
      } else {
        console.error(`상세 출하지시서 삭제 실패 : 상태 코드 ${error.response.status}`);
      }
    }
  }
};

const excelDown = async () => {
  const excelName = "출하지시서_" + new Date().getFullYear() + (new Date().getMonth() + 1) + new Date().getDay();
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/shipping-instruction/excelDown`, {
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
    console.error("출하지시서 엑셀다운로드 실패 :", error);
  }
}

onMounted(async () => {
  await fetchShippingInstructionList();
  await fetchShippingInstructionStatusList();
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
};

// 삭제 수행
const handleDelete = async (seq) => {
  if (seq != null) {
    await deleteShippingInstruction(seq);
  }
  await fetchShippingInstructionList();
};

function search() {
  pageNumber.value = 1;

  fetchShippingInstructionList();
}

// 상세 정보 확장
const handleExtendItem = (seq) => {
  if (expandShippingInstruction.value[seq] && expandItemList.value[seq]) {
    // 이미 확장된 상태면 축소
    delete expandShippingInstruction.value[seq];
    delete expandItemList.value[seq];
  } else {
    // API로 데이터를 가져와서 저장
    fetchShippingInstruction(seq);
  }
}
</script>

<template>
  <h4 class="title">영업관리 > 출하지시서 조회</h4>
  <ShippingInstructionList :searchStartDate="searchStartDate"
                           :searchEndDate="searchEndDate"
                           :searchName="searchName"
                           :shippingInstructionList="shippingInstructionList"
                           :shippingInstructionStatusList="shippingInstructionStatusList"
                           :totalCount="totalCount"
                           :pageNumber="pageNumber"
                           :pageSize="pageSize"
                           :expandShippingInstruction="expandShippingInstruction"
                           :expandItemList="expandItemList"
                           @pageEvent="handlePage"
                           @searchEvent="handleSearch"
                           @checkStatusEvent="handleStatus"
                           @extendItemEvent="handleExtendItem"
                           @registerEvent="router.push('/shipping-instruction/input')"
                           @itemDeleteEvent="handleDelete"
                           @excelEvent="excelDown"
  />
</template>

<style scoped>

.title {
  padding-bottom: 20px;
}

</style>
