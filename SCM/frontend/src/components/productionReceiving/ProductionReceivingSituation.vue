<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import dayjs from "dayjs";
import searchIcon from "@/assets/searchIcon.svg";

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const productionReceivingSituationList = ref([]);
const productionReceivingSituationTotal = ref(null);

const fetchProductionReceivingSituationList = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/productionReceiving/situation`, {
      params: {
        searchStartDate: searchStartDate.value,
        searchEndDate: searchEndDate.value,
        searchName: searchName.value,
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });
    console.log(response.data);
    productionReceivingSituationTotal.value = response.data.pop();
    productionReceivingSituationList.value = response.data;
    console.log(response.data);
  } catch (error) {
    console.error("생산입고 현황 불러오기 실패 :", error);
  }
};

onMounted(() => {
  fetchProductionReceivingSituationList();
});

watch([searchStartDate, searchEndDate], () => {
  fetchProductionReceivingSituationList();
})
</script>

<template>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산입고일</p>
          <input type="date" v-model="searchStartDate"/> ~ <input type="date" v-model="searchEndDate"/>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">거래처명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchName"></b-form-input>
            <b-button variant="light" class="button" @click="fetchProductionReceivingSituationList()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div class="d-flex justify-content-end mt-3">
        <b-button variant="light" size="sm" class="button ms-2 mb-3">인쇄</b-button>
      </div>
      <div class="content">
        <div class="table-container">
          <!-- 테이블 -->
          <table>
            <thead>
              <tr>
                <th>번호</th>
                <th>생산입고일</th>
                <th>생산입고명</th>
                <th>총 금액</th>
                <th>거래처명</th>
                <th>비고</th>
              </tr>
            </thead>
            <tbody v-if="productionReceivingSituationList.length > 0">
              <!-- 필터링된 결과 및 월별 합계 출력 -->
              <template v-for="(productionReceivingSituation, index) in productionReceivingSituationList" :key="index">
                <tr v-if="productionReceivingSituation.productionReceivingRegDate">
                  <td>{{ index+1 }}</td>
                  <td>{{ dayjs(productionReceivingSituation.productionReceivingRegDate).format('YYYY-MM-DD HH:mm:ss') }}</td>
                  <td>{{ productionReceivingSituation.productionReceivingName }}</td>
                  <td>{{ productionReceivingSituation.productionReceivingExtendedPrice }} ￦</td>
                  <td>{{ productionReceivingSituation.clientName}}</td>
                  <td>{{ productionReceivingSituation.productionReceivingNote }}</td>
                </tr>
                <tr v-else class="monthly-total">
                  <td>{{ index+1 }}</td>
                  <td>{{ productionReceivingSituation.productionReceivingRegMonth }}</td>
                  <td> - </td>
                  <td>{{ productionReceivingSituation.productionReceivingSum }} ￦</td>
                  <td> - </td>
                  <td> - </td>
                </tr>
              </template>

            </tbody>
            <tbody v-else>
              <tr>
                <td colspan="6">해당 검색조건에 부합한 생산입고가 존재하지 않습니다</td>
              </tr>
            </tbody>
            <!-- 총합 -->
            <tfoot v-if="productionReceivingSituationTotal">
              <tr>
                <td colspan="3">총합</td>
                <td colspan="1">{{productionReceivingSituationTotal.productionReceivingSum}} ￦</td>
              </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.content{
  display: flex;
  justify-content: space-around;
  max-height: 400px; /* 스크롤바가 나타날 최대 높이 */
  overflow-y: auto; /* 수직 스크롤바 */
}


.table-container {
  width: 100%;
  overflow-x: auto; /* 가로 스크롤바 */
}

.search-input>input {
  width: 100%;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  padding: 10px 12px;
  font-size: 14px;
}

.search-input>img {
  position : absolute;
  width: 17px;
  top: 10px;
  right: 12px;
  margin: 0;
}

.table-container{
  border: 1px solid #000000;
}

table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}

th, td {
  padding: 10px;
  border: none;
}

thead th {
  border-bottom: 4px solid #AAAAAA; /* 헤더 아래쪽만 선 표시 */
}

tfoot tr {
  border-top: 4px solid #AAAAAA;
}

tfoot {
  font-weight: bold;
}

.monthly-total {
  font-weight: bold;
  border-top: 2px solid #AAAAAA;
  border-bottom: 1px solid #AAAAAA;
}

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

.icon {
  width: 20px;
  height: 20px;
}

</style>