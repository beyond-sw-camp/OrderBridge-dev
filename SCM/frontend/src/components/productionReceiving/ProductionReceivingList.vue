<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "@/axios";
import searchIcon from "@/assets/searchIcon.svg";
import dayjs from "dayjs";
import router from "@/router/index.js";
import printIcon from "@/assets/printIcon.svg"
import editIcon from "@/assets/editIcon.svg"
import trashIcon from "@/assets/trashIcon.svg"
import ProductionReceivingPrintPreviewModal from "@/components/productionReceiving/ProductionReceivingPrintPreview.vue";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const productionReceivingList = ref([]);
const productionReceivingStatusList = ref([]);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchName = ref(null);
const searchStatus = ref(new Set([]));

const detailProductionReceiving = ref([]);

const fetchProductionReceivingList = async () => {
  detailProductionReceiving.value = [];
  try {
    const response = await axios.get(`productionReceiving`, {
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

    productionReceivingList.value = response.data.productionReceivingList.content;
    productionReceivingStatusList.value = response.data.productionReceivingStatusList;
    totalCount.value = response.data.productionReceivingList.totalElements;

  } catch (error) {
    console.error("생산입고 불러오기 실패 :", error);
  }
};

const isModalVisible = ref(false);
const selectedProductionReceiving = ref(null);

const openPrintPreview = (productionReceiving) => {
  if (!productionReceiving) {
    console.error('선택된 생산입고가 없습니다.');
    return;
  }

  selectedProductionReceiving.value = productionReceiving;
  isModalVisible.value = true;
};

const closePrintPreview = () => {
  isModalVisible.value = false;
  selectedProductionReceiving.value = null;
};

onMounted(() => {
  fetchProductionReceivingList();
});

watch([searchStartDate, searchEndDate], () => {
  search();
})

watch(pageNumber, () => {
  fetchProductionReceivingList();
})

function check(status) {
  if(searchStatus.value.has(status)) {
    searchStatus.value.delete(status);
  } else {
    searchStatus.value.add(status);
  }

  search();
}

function search() {
  pageNumber.value = 1;

  fetchProductionReceivingList();
}

const excelDown = async () => {
  const excelName = "생산입고_" + new Date().getFullYear() + (new Date().getMonth() + 1) + new Date().getDay();
  try {
    const response = await axios.get(`productionReceiving/excelDown`, {
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
    console.error("생산입고 엑셀다운로드 실패 :", error);
  }
}

// 등록 페이지 이동
const handleRegister = () => {
  router.push("/productionReceiving/register");
}

// 상태 키로 값 반환
function findStatusValue(array, key) {
  for (const item of array) {
    if (item.key === key) {
      return item.value
    }
  }
}

const productionReceivingDetail = async (productionReceivingSeq) => {
  if(detailProductionReceiving.value[productionReceivingSeq] === undefined) {
    try {
      const response = await axios.get(`productionReceiving/${productionReceivingSeq}`);

      detailProductionReceiving.value[productionReceivingSeq] = {
        productionReceivingSeq: response.data.productionReceivingDTO.productionReceivingSeq,
        productionReceivingExtendedPrice: response.data.productionReceivingDTO.productionReceivingExtendedPrice,
        productionReceivingName: response.data.productionReceivingDTO.productionReceivingName,
        productionReceivingNote: response.data.productionReceivingDTO.productionReceivingNote,
        productionReceivingReceiptDate: response.data.productionReceivingDTO.productionReceivingReceiptDate,
        productionReceivingStatus: response.data.productionReceivingDTO.productionReceivingStatus,
        productionReceivingItemList: response.data.productionReceivingItemList,
        productionReceivingData: response.data,
      };
    } catch (error) {
      console.error("생산입고 상세조회 실패 :", error);
    }
  } else {
    detailProductionReceiving.value[productionReceivingSeq] = undefined;
  }

}

// 수정 페이지로 이동
const handleModify = (productionReceivingSeq) => {
  router.push(`/productionReceiving/modify/${productionReceivingSeq}`);
}

const productionReceivingDelete = async (productionReceivingSeq) => {
  if(confirm("해당 생산입고를 삭제하시겠습니까?")) {
    try {
      await axios.delete(`productionReceiving/${productionReceivingSeq}`);

      alert("삭제가 완료되었습니다.");
      search();
    } catch (error) {
      console.error("생산입고 삭제 실패 :", error);
    }
  }
}

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
                    <p class="card-title">생산입고명</p>
                    <b-input-group class="mt-3">
                        <b-form-input v-model="searchName"></b-form-input>
                        <b-button variant="light" class="button" @click="search()"><searchIcon class="icon"/></b-button>
                    </b-input-group>
                </div>
            </div>
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">생산입고 상태</p>
                    <template v-for="productionReceivingStatus in productionReceivingStatusList">
                      <b-form-checkbox @click="check(productionReceivingStatus.key)">{{productionReceivingStatus.value}}</b-form-checkbox>
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
                    <b-button @click="handleRegister" variant="light" size="sm" class="button ms-2">생산입고 등록</b-button>
                  </div>
              </div>
              <div class="list-headline row">
                  <div class="list-head col-6">생산입고명</div>
                  <div class="list-head col-4">입고일</div>
                  <div class="list-head col-2">상태</div>
              </div>
              <template v-if="productionReceivingList.length > 0">
                <div style="max-height: 500px; overflow-y: auto;">
                  <div v-for="productionReceiving in productionReceivingList" :key="productionReceiving.productionReceivingSeq" class="list-line row" @click="productionReceivingDetail(productionReceiving.productionReceivingSeq)">
                    <div class="list-body col-6 left">
                      {{ productionReceiving.productionReceivingName }}
                      <div v-if="productionReceiving.productionReceivingItemList.length > 0">
                        <template v-for="(productionReceivingItem, index) in productionReceiving.productionReceivingItemList" :key="productionReceivingItem.productionReceivingItemSeq">
                          <template v-if="index === productionReceiving.productionReceivingItemList.length - 1">
                            {{productionReceivingItem.itemName}}
                          </template>
                          <template v-else>
                            {{productionReceivingItem.itemName + ", "}}
                          </template>
                        </template>
                      </div>
                    </div>
                    <div class="list-body col-4">{{ dayjs(productionReceiving.productionReceivingReceiptDate).format('YYYY-MM-DD HH:mm:ss') }}</div>
                    <div class="list-body col-2">{{ findStatusValue(productionReceivingStatusList, productionReceiving.productionReceivingStatus) }}</div>
                    <!-- 확장된 상세 정보 표시 -->
                    <div class="d-flex justify-content-center" v-if="detailProductionReceiving[productionReceiving.productionReceivingSeq]">
                      <div class="col-md-11 mt-3">
                        <p>총 금액 : {{ detailProductionReceiving[productionReceiving.productionReceivingSeq].productionReceivingExtendedPrice.toLocaleString()}} ₩</p>
                        <p>생산입고 비고 : {{ detailProductionReceiving[productionReceiving.productionReceivingSeq].productionReceivingNote}}</p>
                        <div class="container">
                          <div class="row">
                            <div v-for="productionReceivingItem in detailProductionReceiving[productionReceiving.productionReceivingSeq].productionReceivingItemList" :key="productionReceivingItem.productionReceivingItemSeq"  class="col-12 col-md-6 col-lg-4 mb-4">
                              <div class="card h-100">
                                <b-img
                                    class="card-img-top"
                                    style="max-height: 100px;"
                                    :src="productionReceivingItem.itemImageUrl"
                                    fluia
                                    alt="Responsive image"
                                ></b-img>
                                <div class="card-body">
                                  <p class="card-text">· 품목 : {{ productionReceivingItem.itemName }}</p>
                                  <p class="card-text">· 수량 : {{ productionReceivingItem.productionReceivingItemQuantity }} 개</p>
                                  <p class="card-text">· 가격 : {{ productionReceivingItem.productionReceivingUnitPrice.toLocaleString() }} ₩</p>
                                  <p class="card-text">· 보관창고 : {{ productionReceivingItem.warehouseName }}</p>
                                  <p v-if="productionReceivingItem.productionReceivingItemNote !== ''" class="card-text">· 비고 : {{ productionReceivingItem.productionReceivingItemNote }}</p>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="d-flex justify-content-end align-items-center">
                          <printIcon class="me-3 icon" @click.stop="openPrintPreview(detailProductionReceiving[productionReceiving.productionReceivingSeq].productionReceivingData)"/>
                          <editIcon class="me-3 icon" @click.stop="handleModify(productionReceiving.productionReceivingSeq)"/>
                          <trashIcon class="icon" @click.stop="productionReceivingDelete(productionReceiving.productionReceivingSeq)"/>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>


                <div class="pagination">
                  <b-pagination
                      v-model="pageNumber"
                      :totalRows="totalCount"
                      :perPage="pageSize">
                  </b-pagination>
                </div>
              </template>
              <template v-else>
                <b-card-text class="no-list-text">해당 검색조건에 부합한 생산입고가 존재하지 않습니다.</b-card-text>
              </template>
            </div>

        </div>
    </div>

  <ProductionReceivingPrintPreviewModal
      :isVisible="isModalVisible"
      :productionReceiving="selectedProductionReceiving"
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

</style>
