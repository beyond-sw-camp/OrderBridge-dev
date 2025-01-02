<script setup>
import axios from "@/axios"
import {onMounted, ref, watch} from "vue";
import searchIcon from "@/assets/searchIcon.svg";
import dayjs from "dayjs";
import printIcon from "@/assets/printIcon.svg"
import editIcon from "@/assets/editIcon.svg"
import trashIcon from "@/assets/trashIcon.svg"
import router from "@/router/index.js";
import ProductionDisbursementPrintPreview
  from "@/components/productionDisbursement/ProductionDisbursementPrintPreview.vue";

const productionDisbursementList = ref([]);
const productionDisbursementStatusList = ref([]);
const productionDisbursementDetail = ref([]);
const selectedProductionDisbursement = ref({});
const itemDivisionList = ref([]);

const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchFactory = ref(null);
const searchStatus = ref(new Set([]));

const searchPage = ref(1);
const searchSize = ref(10);
const currentPage = ref(1);
const totalPage = ref(1);
const totalCount = ref(0);

const isModalVisible = ref(false);
const selectedProductionDisbursement2 = ref(null);
const fullDetail = ref({});

const openPrintPreview = (productionDisbursement) => {
  if (!productionDisbursement) {
    console.error('선택된 생산불출이 없습니다.');
    return;
  }
  console.log('openPrintPreview 호출됨, 선택된 생산불출:', productionDisbursement);
  selectedProductionDisbursement2.value = productionDisbursement;
  isModalVisible.value = true;
};

const closePrintPreview = () => {
  isModalVisible.value = false;
  selectedProductionDisbursement2.value = null;
};

// 목록조회
const fetchProductionDisbursementList = async () => {
  productionDisbursementDetail.value = [];
  try {
    const response = await axios.get(`productionDisbursement`, {
      params: {
        page: searchPage.value,
        size: searchSize.value,
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        factoryName: searchFactory.value,
        productionDisbursementStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value),
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });
    console.log(response.data);
    productionDisbursementList.value = response.data.productionDisbursementList;
    productionDisbursementStatusList.value = response.data.productionDisbursementStatusList;

    totalCount.value = response.data.totalItems;
    currentPage.value = response.data.currentPage;
    totalPage.value = response.data.totalPages;
    console.log(productionDisbursementList.value);
  } catch (error) {
    if (error.response.data.errorCode === 'COMMON_ERROR_002') {
      alert(error.response.data.message);
    }
    console.log("생산불출 불러오기 실패: ", error);
  }
};

// 상태 키로 값 반환
function findStatusValue(array, key) {
  for (const item of array) {
    if (item.key === key) {
      return item.value
    }
  }
}

// 상세보기
const fetchProductionDisbursementDetail = async (productionDisbursementSeq) => {
  if(productionDisbursementDetail.value[productionDisbursementSeq] === undefined) {
    try {
      const response = await axios.get(`productionDisbursement/${productionDisbursementSeq}`, {});
      console.log(response.data);

      productionDisbursementDetail.value[productionDisbursementSeq] = {
        productionDisbursementSeq : response.data.productionDisbursementDetail.productionDisbursementSeq,
        workOrderSeq : response.data.productionDisbursementDetail.workOrderSeq,
        workOrderName : response.data.productionDisbursementDetail.workOrderName,
        factorySeq : response.data.productionDisbursementDetail.factorySeq,
        factoryName : response.data.productionDisbursementDetail.factoryName,
        clientName : response.data.productionDisbursementDetail.clientName,
        userName: response.data.productionDisbursementDetail.userName,
        userPhoneNo: response.data.productionDisbursementDetail.userPhoneNo,
        productionDisbursementName : response.data.productionDisbursementDetail.productionDisbursementName,
        productionDisbursementTotalQuantity : response.data.productionDisbursementDetail.productionDisbursementTotalQuantity,
        productionDisbursementDepartureDate : response.data.productionDisbursementDetail.productionDisbursementDepartureDate,
        productionDisbursementStatus : response.data.productionDisbursementDetail.productionDisbursementStatus ,
        workOrderDueDate : response.data.productionDisbursementDetail.workOrderDueDate,
        productionDisbursementNote : response.data.productionDisbursementDetail.productionDisbursementNote,
        itemList: response.data.itemList
      }
      selectedProductionDisbursement.value = productionDisbursementDetail.value[productionDisbursementSeq];

      fullDetail.value[productionDisbursementSeq] = response.data;
      console.log('fullDetail',fullDetail.value[productionDisbursementSeq]);

      console.log(response.data.productionDisbursementDetail);
      console.log(response.data.itemList);
    } catch (error) {
      if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_001') {
        alert(error.response.data.message);
      }

      console.error("생산불출 상세 불러오기 실패 :", error);
    }
  } else {
    productionDisbursementDetail.value[productionDisbursementSeq] = undefined;
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

// 숫자 포맷
function numberFormating(number) {
  return `${number.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}`;
}

// 생산불출 목록 엑셀 다운로드
  const excelDown = async () => {
    try{
    const response = await axios.get(`productionDisbursement/excelDownload`, {
      params: {
        startDate: searchStartDate.value,
        endDate: searchEndDate.value,
        factoryName: searchFactory.value,
        productionDisbursementStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value),
      }, responseType: "blob"
    });
    const blob = new Blob([response.data], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;

    // 헤더 인식이 안될때 대비
    let fileName = '생산불출 download.xlsx';
    const disposition = response.headers['content-disposition'];

    if (disposition) {
      const fileNameMatch = disposition.match(/filename="?([^"]+)"?/);
      if (fileNameMatch) {
        fileName = decodeURIComponent(fileNameMatch[1]);
      }
    }

    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(url);
  } catch (error) {
      console.error('Excel 다운로드 실패:', error);

      if (error.response.data.errorCode === 'EXCEL_DOWN_ERROR_001') {
        alert(error.response.data.message);
      }
    }
};

const deleteProductionDisbursement = async (productionDisbursementSeq) => {
  const result = confirm("이 생산불출을 삭제하시겠습니까?");
  console.log("삭제요청 생산불출 번호", productionDisbursementSeq);
  if (result) {
    try {
      const response = await axios.delete(`productionDisbursement/${productionDisbursementSeq}`);
      alert('삭제가 완료되었습니다.');

      search(); // 삭제 후 목록 갱신
    } catch (error) {
      console.error("생산불출 삭제 요청 실패:", error);
      if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_001') {
        alert(error.response.data.message);
      } else if (error.response.data.errorCode === 'PRODUCTION_DISBURSEMENT_ERROR_002') {
        alert('불출 전 상태만 삭제 가능합니다.');
      } else if (error.response.data.errorCode === 'SECURITY_ERROR_001') {
        alert('작성자만 삭제 가능합니다.');
      }  else {
        alert('삭제에 실패했습니다. 다시 시도해주세요.');
      }
    }
  }
}

// 등록 페이지로 이동
const goToWriteProductionDisbursement = () => {
  router.push(`/production-disbursement/write`);
}

// 수정 페이지로 이동
const goToEdit = (productionDisbursementSeq) => {
  router.push(`/production-disbursement/edit/${productionDisbursementSeq}`);
}

onMounted(() => {
  fetchProductionDisbursementList();
  fetchItemDivision();

  const startOfMonth = dayjs().startOf('month').format('YYYY-MM-DD'); // 해당 달의 첫 날
  // const today = dayjs().format('YYYY-MM-DD'); // 오늘 날짜 (YYYY-MM-DD 형식)
  const endOfMonth = dayjs().endOf('month').format('YYYY-MM-DD')

  searchStartDate.value = startOfMonth;
  searchEndDate.value = endOfMonth;
});

watch([searchStartDate, searchEndDate], () => {
  search();
});

watch(searchPage, () => {
  fetchProductionDisbursementList();
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
  fetchProductionDisbursementList();
}

</script>

<template>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산불출일</p>
          <input type="date"  v-model="searchStartDate"/> ~
          <input type="date"  v-model="searchEndDate"/>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산공장명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchFactory" @keyup.enter="search()"></b-form-input>
            <b-button variant="light" class="button" @click="search()"><searchIcon class="icon"/></b-button>
          </b-input-group>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">생산불출 상태</p>
          <template v-for="productionDisbursementStatus in productionDisbursementStatusList">
            <b-form-checkbox v-if="productionDisbursementStatus.key !== 'DELETE'"
                             @click="check(productionDisbursementStatus.key)">{{productionDisbursementStatus.value}}</b-form-checkbox>
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
            <b-button @click="goToWriteProductionDisbursement" variant="light" size="sm" class="button ms-2">생산불출 등록</b-button>
          </div>
        </div>
        <div class="list-headline row">
          <div class="list-head col-5">생산불출명</div>
          <div class="list-head col-2">생산공장명</div>
          <div class="list-head col-3">생산불출일</div>
          <div class="list-head col-2">상태</div>
        </div>
        <template v-if="productionDisbursementList.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="productionDisbursement in productionDisbursementList"
                 :key="productionDisbursement.productionDisbursementSeq"
                 @click="fetchProductionDisbursementDetail(productionDisbursement.productionDisbursementSeq)">
              <div class="list-line row" :id="'print-area-' + productionDisbursement.productionDisbursementSeq">
                <div class="list-body col-5 left">
                  <b>{{ productionDisbursement.productionDisbursementName }}</b>
                  <div v-if="!productionDisbursement.itemName"><br></div>
                  <div v-else>{{ productionDisbursement.itemName }}</div>
                </div>
                <div class="list-body col-2">{{ productionDisbursement.factoryName }}</div>
                <div class="list-body col-3">{{ dayjs(productionDisbursement.productionDisbursementDepartureDate).format('YYYY/MM/DD') }}</div>
                <div class="list-body col-2">{{ findStatusValue(productionDisbursementStatusList, productionDisbursement.productionDisbursementStatus) }}</div>

                <!-- 확장된 상세 정보 표시 -->
                <div class="d-flex justify-content-center" v-if="productionDisbursementDetail[productionDisbursement.productionDisbursementSeq]">
                  <div class="col-md-11 mt-3">
                    <b>총 불출량 : </b>{{ selectedProductionDisbursement?.productionDisbursementTotalQuantity }} <br>
                    <b>담당자 : </b>{{ selectedProductionDisbursement?.userName }} <br>
                    <b>불출일 : </b>{{ dayjs(selectedProductionDisbursement?.productionDisbursementDepartureDate).format('YYYY-MM-DD HH:mm:ss') }}<br>
                    <b>작업목표일 : </b>{{ dayjs(selectedProductionDisbursement?.workOrderDueDate).format('YYYY-MM-DD HH:mm:ss') }}<br>
                    <b>생산불출 비고 : </b>{{ selectedProductionDisbursement?.productionDisbursementNote }}<br>

                    <!--  품목정보  -->
                    <div style="display:flex; flex-wrap: wrap;">
                      <template v-for="item in productionDisbursementDetail[productionDisbursement.productionDisbursementSeq].itemList" :key="item.productionDisbursementItemSeq">
                        <div class="card item-card">
                          <img :src=item.itemImageUrl class="card-img-top">
                          <div style="margin: 5px;">
                            <small>{{ findStatusValue(itemDivisionList, item.itemDivision) }}</small>
                            <div style="display: flex; justify-content: space-between;">
                              <b style="font-size: medium;">{{ item.itemName }}</b>
                              <small>{{ item.storeName }} </small>
                            </div>
                            <small  style="display: flex; justify-content: space-between; align-items: center;">
                              <span>{{ numberFormating(item.productionDisbursementQuantity) }} {{ item.itemUnitTitle || '개' }}</span>
                              <span style="margin-left: auto; margin-right: 5px" v-if="item.itemInventoryExpirationDate != null">
                                {{ dayjs(item.itemInventoryExpirationDate).format('YYYY-MM-DD HH:mm:ss') }}</span>
                            </small>
                            <br><br>
                            <small v-if="item.productionDisbursementNote" style="margin-top: 20px;">
                              비고: {{ item.productionDisbursementNote }}</small>
                            <small v-else style="margin-top: 20px;">비고: 없음</small>
                          </div>
                        </div>
                      </template>
                    </div>

                    <div class="d-flex justify-content-end align-items-center">
                      <printIcon class="me-3 icon"  v-if="fullDetail[productionDisbursement.productionDisbursementSeq]"
                                 @click.stop="openPrintPreview(fullDetail[productionDisbursement.productionDisbursementSeq])"/>
                      <editIcon class="me-3 icon" @click.stop="goToEdit(productionDisbursement.productionDisbursementSeq)"/>
                      <trashIcon class="icon" @click.stop="deleteProductionDisbursement(productionDisbursement.productionDisbursementSeq)"/>
                    </div>
                  </div>
                </div>
              </div>
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
          <b-card-text class="no-list-text">해당 검색조건에 부합한 생산불출이 존재하지 않습니다.</b-card-text>
        </template>
      </div>

    </div>
  </div>

  <ProductionDisbursementPrintPreview
      :isVisible="isModalVisible"
      :productionDisbursement="selectedProductionDisbursement2"
      :isList=true
      @close="closePrintPreview"
  />
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

.item-card {
  width: 220px;
  margin: 10px;
}

.item-card img {
  width: 100%;
  object-fit: cover;
}

.card-img-top {
  max-height: 90px;
  object-fit: cover;
}

</style>