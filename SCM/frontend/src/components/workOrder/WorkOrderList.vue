<script setup>

import axios from "axios";
import {onMounted, ref, watch} from "vue";
import searchIcon from "@/assets/searchIcon.svg";
import dayjs from "dayjs";

const workOrderList = ref([]);
const workOrderStatusList = ref([]);

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchFactory = ref(null);
const searchStatus = ref(new Set([]));

const searchPage = ref(1);
const searchSize = ref(10);
const currentPage = ref(1);
const totalPage = ref(1);
const totalCount = ref(0);

const fetchWorkOrderList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/workOrder`, {
      params: {
        page: searchPage.value,
        size: searchSize.value,
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        warehouseName: searchFactory.value,
        workOrderStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value),
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });
    console.log(response.data);
    workOrderList.value = response.data.workOrderList;
    workOrderStatusList.value = response.data.workOrderStatusList;

    totalCount.value = response.data.totalItems;
    currentPage.value = response.data.currentPage;
    totalPage.value = response.data.totalPages;
    console.log(workOrderList.value);
  } catch (error) {
    console.log("작업지시서 불러오기 실패: ", error);
  }
}

// 상태 포맷
const formatStatus = (status) => {
  if (status === 'BEFORE') {
    return '결재 전';
  } else if (status === 'AFTER') {
    return '결재 후';
  } else if (status === 'ONGOING') {
    return '진행중';
  } else if (status === 'COMPLETE') {
    return '완료';
  } else if (status === 'STOP') {
    return '중단';
  }
  return status; // 상태가 다른 경우 그대로 반환
};

onMounted(() => {
  fetchWorkOrderList();
});

watch([searchStartDate, searchEndDate], () => {
  search();
});

watch(searchPage, () => {
  fetchWorkOrderList();
});

function check(status) {
  if(searchStatus.value.has(status)) {
    searchStatus.value.delete(status);
  } else {
    searchStatus.value.add(status);
  }

  search();
}

function search() {
  searchPage.value = 1;
  fetchWorkOrderList();
}

// // 선택한 Item 확장 | 축소
// function itemExtend(event) {
//   // 선택한 list-line의 id 추출
//   let listLine = event.target;
//   for (let i = 0; i < 5; i++) {
//     if (listLine.classList[0] === "list-line") { break; }
//     else { listLine = listLine.parentNode; }
//   }
//   let id = listLine.getElementsByClassName("col-6")[0].innerHTML.split("<br")[0];
//
//   console.log(id);
//   console.log(listLine);
//
//   // 찾은 id 기반으로 API 호출
//
//   // API 응답값으로 list-line 확장 | 축소
// }
//
// // 품목 만드는 반복문 추가
// function addItemCard() {
//
// }
</script>

<template>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">작업지시일</p>
          <input type="date"  v-model="searchStartDate"/> ~
          <input type="date"  v-model="searchEndDate"/>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산공장명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchFactory"></b-form-input>
            <b-button variant="light" class="button" @click="search()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">작업지시서 상태</p>
          <template v-for="workOrderStatus in workOrderStatusList">
            <b-form-checkbox v-if="workOrderStatus.key !== 'DELETE'"
                             @click="check(workOrderStatus.key)">{{workOrderStatus.value}}</b-form-checkbox>
          </template>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div>
        <div class="d-flex justify-content-between">
          <div>검색결과: {{ totalCount }}개</div>
          <div class="d-flex justify-content-end mt-3">
            <b-button @click="excelDown" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
            <b-button variant="light" size="sm" class="button ms-2">작업지시서 등록</b-button>
          </div>
        </div>
        <div class="list-headline row">
          <div class="list-head col-5">작업지시서</div>
          <div class="list-head col-2">생산공장명</div>
          <div class="list-head col-3">작업지시일</div>
          <div class="list-head col-2">상태</div>
        </div>
        <template v-if="workOrderList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="workOrder in workOrderList" :key="workOrder.workOrderSeq" class="list-line row" @click="itemExtend">
              <div class="list-body col-5 left">
                {{ workOrder.workOrderName }}
                <div v-if="!workOrder.itemName"><br></div>
                <div v-else>{{ workOrder.itemName }}</div></div>
              <div class="list-body col-2">{{ workOrder.warehouseName }}</div>
              <div class="list-body col-3">{{ dayjs(workOrder.workOrderIndicatedDate).format('YYYY-MM-DD HH:mm:ss') }}</div>
              <div class="list-body col-2">{{ formatStatus(workOrder.workOrderStatus) }}</div>
            </div>
          </div>

          <div class="pagination">
            <b-pagination
                v-model="searchPage"
                :totalRows="totalCount"
                :perPage="searchSize">
            </b-pagination>
          </div>
        </template>
        <template v-else>
          <b-card-text class="no-list-text">해당 검색조건에 부합한 작업지시서 존재하지 않습니다.</b-card-text>
        </template>
      </div>

    </div>
  </div>
</template>

<style scoped>

div {
  font-size: small;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.side-box {
  min-height: 100px;
  margin-top: 20px;
}

.card-title {
  font-size: medium;
  font-weight: bold;
}

.list-headline {
  border-bottom: 1px solid black;
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
}

.list-head {
  text-align: center;
}

.list-line {
  width: 99%;
  border: 1px solid Silver;
  border-radius: 8px;
  margin-left:1px;
  margin-top: 20px;
  padding: 10px 5px 10px 5px;
}

.list-body {
  text-align: center;
  margin: auto 0;
}

.pagination {
  justify-content: center; /* 가로 중앙 정렬 */
  margin-top: 20px;
}

.left {
  text-align: left;
}

.no-list-text {
  text-align: center;
  margin-top: 10%;
}

.icon {
  width: 20px;
  height: 20px;
}


</style>
