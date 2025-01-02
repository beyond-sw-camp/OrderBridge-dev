<script setup>
import { ref, onMounted, watch } from 'vue';
import { sConfirm } from "@/common/salert";
import searchIcon from "@/assets/searchIcon.svg"
import trashIcon from "@/assets/trashIcon.svg";
import editIcon from "@/assets/editIcon.svg";
import printIcon from "@/assets/printIcon.svg";
import axios from "@/axios"
import dayjs from 'dayjs';
import SalesOrderPrintPreview from "@/components/salesOrder/SalesOrderPrintPreview.vue";

const salesOrderDetail = ref({});
const salesOrderExtended = ref({});
const salesOrderList = ref([]);
const salesOrderStatusList = ref([]);
const itemDivisionList = ref([])
const currentPage = ref(1);
const totalPage = ref(1);
const totalSalesOrder = ref(0);
const searchPage = ref(1);
const searchSize = ref(10);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchClient = ref(null);
const searchStatus = ref(new Set());

// 주문서 목록 요청
const fetchSalesOrderList = async () => {
    try {
        const response = await axios.get(`sales-order`, {
            params: {
                page: searchPage.value,
                size: searchSize.value,
                startDate: searchStartDate.value,
                endDate: searchEndDate.value,
                clientName: searchClient.value,
                salesOrderStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value).join(",")
            }
        });

        salesOrderList.value = response.data.salesOrder;
        currentPage.value = response.data.currentPage;
        totalPage.value = response.data.totalPages;
        totalSalesOrder.value = response.data.totalSalesOrder;
    } catch (error) {
        console.log(`주문서 목록 요청 실패`, error);
    }
}

// 주문서 상세 요청
const fetchSalesOrder = async (salesOrderSeq) => {
    try {
        const response = await axios.get(`sales-order/${salesOrderSeq}`);

        salesOrderDetail.value[salesOrderSeq] = response.data;
        salesOrderExtended.value[salesOrderSeq] = true;
    } catch (error) {
        console.log(`주문서 상세 요청 실패 ${error}`);
    }
}

// 주문서 상태 분류 목록 요청
const fetchSalesOrderStatus = async () => {
    try { 
        const response = await axios.get(`sales-order/status`);
        
        salesOrderStatusList.value = response.data;
    } catch (error) {
        console.log(`주문서 상태 분류 목록 요청 실패`, error);
    }
}

// 품목 분류 요청
const fetchItemDivision = async () => {
    try {
        const response = await axios.get(`item/item-division`);

        itemDivisionList.value = response.data;
    } catch (error) {
        console.log(`품목 분류 요청 실패 ${error}`);
    }
}

// 주문서 목록 엑셀 다운로드
const excelDown = async () => {
    const response = await axios.get(`sales-order/excel`, {
        params: {
            startDate: searchStartDate.value,
            endDate: searchEndDate.value,
            clientName: searchClient.value,
            salesOrderStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value).join(",")
        }, responseType: "blob"
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
}

// 주문서 삭제 요청
const deleteSalesOrder = async (salesOrderSeq) => {
    try {
        const deleteConfirm = await sConfirm(`선택한 주문서를 삭제하시겠습니까?`);
        if (deleteConfirm) {
            const response = await axios.delete(`sales-order/${salesOrderSeq}`);
            fetchSalesOrderList();
        }
    } catch (error) {
        console.log(`주문서 삭제 요청 실패 ${error}`);
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

const isModalVisible = ref(false);
const selectedSalesOrder = ref(null);

const openPrintPreview = (salesOrder) => {
  if (!salesOrder) {
    console.error('선택된 주문서가 없습니다.');
    return;
  }

  selectedSalesOrder.value = salesOrder;
  isModalVisible.value = true;
};

const closePrintPreview = () => {
  isModalVisible.value = false;
  selectedSalesOrder.value = null;
};

onMounted(() => {
    fetchSalesOrderList();
    fetchSalesOrderStatus();
    fetchItemDivision();
});

watch([searchStartDate, searchEndDate, searchStatus.value], () => {
    search();
});

watch(searchPage, () => {

    fetchSalesOrderList();
});

watch(searchClient, () => {
    fetchClientHint(searchClient);
});

// 상태 체크박스
function statusCheck(status) {
    searchStatus.value.has(status) ? searchStatus.value.delete(status)
                                   : searchStatus.value.add(status);
}

// 검색
function search() {
    searchPage.value = 1;

    fetchSalesOrderList();
}

// 상태 키로 값 반환
function findStatusValue(array, key) {
    for (const item of array) {
        if (item.key === key) {
            return item.value
        }
    }
}

// 주문서 클릭
function salesOrderClick(salesOrderSeq) {
    if (salesOrderExtended.value[salesOrderSeq]) {
        delete salesOrderExtended.value[salesOrderSeq];
        delete salesOrderDetail.value[salesOrderSeq];
    } else {
        fetchSalesOrder(salesOrderSeq);
    }
}

// 숫자 쉼표 삽입
function numberThree(number) {
    return `${number.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}`;
}

</script>

<template>
    <div class="row">
        <div class="col-md-3">
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">주문일</p>
                    <input type="date" v-model="searchStartDate" style="max-width: 40%;"/> ~ <input type="date" v-model="searchEndDate" style="max-width: 40%;"/>
                </div>
            </div>
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">거래처명</p>
                    <b-input-group class="mt-3">
                        <b-form-input v-model="searchClient"></b-form-input>
                        <b-button variant="light" class="button" @click="search()"><searchIcon class="icon"/></b-button>
                    </b-input-group>
                    <div class="clientHint" style="position: absolute; z-index: 5;">
                        <ul class="list-group">
                            <template v-for="hint in clientHintList">
                                <li class="list-group-item list-group-item-action" @click="searchClient = hint">{{ hint }}</li>
                            </template>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">주문서 상태</p>
                    <template v-for="salesOrderStatus in salesOrderStatusList">
                        <b-form-checkbox @click="statusCheck(salesOrderStatus.key)">{{ salesOrderStatus.value }}</b-form-checkbox>
                    </template>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div>
                <div class="d-flex justify-content-between">
                    <div>검색결과: {{ totalSalesOrder }}개</div>
                    <div class="d-flex justify-content-end mt-3">
                        <b-button @click="excelDown()" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
                        <RouterLink to="/sales-order/input"><b-button variant="light" size="sm" class="button ms-2">주문서 등록</b-button></RouterLink>
                    </div>
                </div>
                <div class="list-headline row">
                    <div class="list-headvalue col-6">주문서</div>
                    <div class="list-headvalue col-2">거래처</div>
                    <div class="list-headvalue col-2">주문일</div>
                    <div class="list-headvalue col-2">상태</div>
                </div>
                <template v-if="salesOrderList.length > 0">
                <div style="max-height: 600px; overflow-y: auto;">
                    <div v-for="salesOrder in salesOrderList" :key="salesOrder.salesOrderSeq" class="list-line row" @click="salesOrderClick(salesOrder.salesOrderSeq)">
                        <div class="col-6">
                            <b>{{ salesOrder.salesOrderName }}</b>
                            <div v-if="!salesOrderExtended[salesOrder.salesOrderSeq]">{{ salesOrder.itemName }}</div>
                        </div>
                        <div class="list-value col-2">{{ salesOrder.clientName }}</div>
                        <div class="list-value col-2">{{ salesOrder.salesOrderOrderDate }}</div>
                        <div class="list-value col-2">{{ findStatusValue(salesOrderStatusList, salesOrder.salesOrderStatus) }}</div>

                        <div class="d-flex justify-content-center">
                            <div v-if="salesOrderExtended[salesOrder.salesOrderSeq]" class="col-md-11 mt-3">
                                <b>총 수량</b>: {{ numberThree(salesOrderDetail[salesOrder.salesOrderSeq].salesOrderTotalQuantity) }} 개<br>
                                <b>총 금액</b>: {{ `₩ ` + numberThree(salesOrderDetail[salesOrder.salesOrderSeq].salesOrderExtendedPrice) }}<br>
                                <b>담당자</b>: {{ salesOrderDetail[salesOrder.salesOrderSeq].userName }}<br>
                                <b>견적일시</b>: {{ dayjs(salesOrderDetail[salesOrder.salesOrderSeq].salesOrderOrderDate).format(`YYYY/MM/DD HH:mm:ss`) }}<br>
                                <b>유효일시</b>: {{ dayjs(salesOrderDetail[salesOrder.salesOrderSeq].salesOrderEffectiveDate).format(`YYYY/MM/DD HH:mm:ss`) }}<br>
                                <b>비고</b>: {{ salesOrderDetail[salesOrder.salesOrderSeq].salesOrderNote }}<br>
                                <div style="display:flex; flex-wrap: wrap;">
                                <template v-for="salesOrderItem in salesOrderDetail[salesOrder.salesOrderSeq].salesOrderItem">
                                    <div class="card item-card">
                                        <img :src=salesOrderItem.itemImageUrl class="card-img-top">
                                        <div style="margin: 5px;">
                                            <small>{{ findStatusValue(itemDivisionList, salesOrderItem.itemDivision) }}</small>
                                            <div style="display: flex; justify-content: space-between;">
                                                <b style="font-size: medium;">{{ salesOrderItem.itemName }}</b>
                                                <small>{{ numberThree(salesOrderItem.salesOrderItemQuantity * salesOrderItem.salesOrderItemPrice) }} 원</small>
                                            </div>
                                            <small>{{ numberThree(salesOrderItem.salesOrderItemQuantity) }}개 / 개당 {{ numberThree(salesOrderItem.salesOrderItemPrice) }}원</small><br><br>
                                            <small style="margin-top: 20px;">비고: {{ salesOrderItem.salesOrderItemNote }}</small>
                                        </div>
                                    </div>
                                </template>
                                </div>
                                
                                <div class="d-flex justify-content-end align-items-center">
                                    <printIcon class="me-3 icon" @click.stop="openPrintPreview(salesOrderDetail[salesOrder.salesOrderSeq])"/>
                                    <editIcon class="me-3 icon" @click.stop="modifySalesOrder(salesOrder.salesOrderSeq)"/>
                                    <trashIcon class="icon" @click.stop="deleteSalesOrder(salesOrder.salesOrderSeq)"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </template>
                <template v-else>
                    <b-card-text class="no-list-text">해당 검색 조건에 부합하는 주문서가 존재하지 않습니다.</b-card-text>
                </template>
            </div>
            <div class="pagenation">
                <b-pagination
                    v-model="searchPage"
                    :total-rows="totalSalesOrder"
                    :per-page="searchSize">
                </b-pagination>
            </div>
        </div>
    </div>

  <SalesOrderPrintPreview
      :isVisible="isModalVisible"
      :salesOrder="selectedSalesOrder"
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

.list-headvalue {
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

.list-value {
    text-align: center;
    margin: auto 0;
    overflow: hidden;
    word-break: keep-all;
}

.pagenation {
    justify-items: center;
    margin-top: 20px;
}

.no-list-text {
    text-align: center;
    margin-top: 100px;
}

.icon {
  width: 20px;
  height: 20px;
}

.icon:hover {
    cursor: pointer;
}

.item-card {
    width: 220px;
    margin: 10px;
}

.card-img-top {
    max-height: 80px;
    object-fit: cover;
}

</style>
