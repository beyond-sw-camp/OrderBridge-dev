<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import searchIcon from "@/assets/searchIcon.svg";
import dayjs from "dayjs";

const totalCount = ref(0);
const pageNumber = ref(1);
const purchaseOrderList = ref([]);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const searchStatus = ref(null);

const fetchPurchaseOrderList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/purchaseOrder`, {
      params: {
        searchStartDate: searchStartDate.value,
        searchEndDate: searchEndDate.value,
        searchName: searchName.value,
        searchStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value),
        page: pageNumber.value - 1, // Spring Pageable에서 0부터 시작
        size: pageSize.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    purchaseOrderList.value = response.data.purchaseOrderResponseList.content;
    totalCount.value = response.data.totalCount;

  } catch (error) {
    console.error("발주서 불러오기 실패 :", error);
  }
};

</script>

<template>
  <template v-if="props.selectedSalesOrder">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">물품 등록</h5>
      <div style="max-height: 250px; overflow-y: auto;">
        <div style="max-height: 200px;" v-for="(item, index) in itemList" :key="item.salesOrderItemSeq"
             class="mx-5 my-3 d-flex flex-row border border-secondary rounded">
          <b-img class="p-2 col-md-4" src="https://picsum.photos/200/200" fluid alt="Responsive image"></b-img>
          <div class="p-2 col-md-8">
            <div class="mb-4 d-flex justify-content-between">
              <span class="fw-bold">{{ item.itemName }}</span>
            </div>
            <div class="d-flex">
              <ul class="col-md-4">
                <li class="mb-3">
                  · 품목 : {{ formatDivision(item.itemDivision) }}
                </li>
                <li class="mb-3 d-flex flex-row">
                  · 수량 :
                  <b-form-input class="ms-2 me-2 w-50" type="text" v-model="itemData[index].quantity" size="sm"></b-form-input>
                  개
                </li>
              </ul>
              <ul class="col-md-8 d-flex flex-column justify-content-between">
                <li class="d-flex flex-row">
                  · 비고 :
                  <b-form-input class="ms-2 w-75" type="text" v-model="itemData[index].note" size="sm"></b-form-input>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button class="mx-3" pill variant="primary" @click="addShippingInstruction">확인</b-button>
        <b-button class="mx-3" pill>목록</b-button>
      </div>
    </div>
  </template>
</template>

<style scoped>
li {
  list-style: none;
}
</style>