<script setup>
import ShippingSlipList from "@/components/shippingSlip/ShippingSlipList.vue";
import {onMounted, ref} from "vue";
import axios from "@/axios"
import ShippingSlipSituation from "@/components/shippingSlip/ShippingSlipSituation.vue";
import ShippingInstructionList from "@/components/shippingInstruction/ShippingInstructionList.vue";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const shippingSlipList = ref([]);
const shippingAddressList = ref([]);

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const expandData = ref({});
const itemDivisionList = ref([]);

// 출하전표 목록 요청
const fetchShippingSlipList = async () => {
  try {
    const response = await axios.get(`shipping-slip`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        clientName: searchName.value,
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

    shippingSlipList.value = response.data.shippingSlipList;
    totalCount.value = response.data.totalItems;

  } catch (error) {
    console.error("출하전표 목록 불러오기 실패 :", error);
  }
};

// 상세 출하전표 요청
const fetchShippingSlip = async (seq) => {
  try {
    const response = await axios.get(`shipping-slip/${seq}`, {});

    expandData.value[seq] = response.data; // ref 값에 추가

  } catch (error) {
    console.error("상세 출하전표 불러오기 실패 :", error);
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

// 품목 분류 요청
const fetchItemDivision = async () => {
  try {
    const response = await axios.get(`item/item-division`);

    itemDivisionList.value = response.data;
  } catch (error) {
    console.log(`품목 분류 요청 실패 ${error}`);
  }
}

// 엑셀 다운 요청
const excelDown = async () => {
  try {
    const response = await axios.get(`shipping-slip/excel`, {
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
    console.error("출하전표 엑셀다운로드 실패 :", error);
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
  await fetchShippingSlipList();

  await fetchShippingAddressList();
  await fetchItemDivision();
});

// 페이지 이동
const handlePage = (newPageNumber) => {
  pageNumber.value = Number(newPageNumber.value);
  fetchShippingSlipList();
};

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
  pageNumber.value = 1;

  fetchShippingSlipList();
}

// 상세 정보 확장
const handleExtendItem = (seq) => {
  if (expandData.value[seq]) {
    // 이미 확장된 상태면 축소
    delete expandData.value[seq];
  } else {
    // API로 데이터를 가져와서 저장
    fetchShippingSlip(seq);
  }
}
</script>

<template>
  <h4 class="title">영업관리 > 출하전표 조회</h4>
  <ShippingSlipList :searchStartDate="searchStartDate"
                    :searchEndDate="searchEndDate"
                    :searchName="searchName"
                    :shippingSlipList="shippingSlipList"
                    :shippingAddressList="shippingAddressList"
                    :totalCount="totalCount"
                    :pageNumber="pageNumber"
                    :pageSize="pageSize"
                    :expandData="expandData"
                    :itemDivisionList="itemDivisionList"
                    :clientHintList="clientHintList"
                    @pageEvent="handlePage"
                    @clientEvent="handleClient"
                    @searchEvent="handleSearch"
                    @extendItemEvent="handleExtendItem"
                    @excelEvent="excelDown"
  />
</template>

<style scoped>

.title {
  padding-bottom: 20px;
}

</style>
