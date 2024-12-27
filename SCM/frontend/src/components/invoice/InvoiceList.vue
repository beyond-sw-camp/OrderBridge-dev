<script setup>
import { ref, onMounted, watch } from 'vue';
import searchIcon from "@/assets/searchIcon.svg"
import trashIcon from "@/assets/trashIcon.svg";
import editIcon from "@/assets/editIcon.svg";
import printIcon from "@/assets/printIcon.svg";
import axios from "@/axios"
import dayjs from 'dayjs';

const invoiceDetail = ref({});
const invoiceExtended = ref({});
const invoiceList = ref([]);
const itemDivisionList = ref([])
const currentPage = ref(1);
const totalPage = ref(1);
const totalInvoice = ref(0);
const searchPage = ref(1);
const searchSize = ref(10);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchClient = ref(null);

// 거래 명세서 목록 요청
const fetchInvoiceList = async () => {
    try {
        const response = await axios.get(`invoice`, {
            params: {
                page: searchPage.value,
                size: searchSize.value,
                startDate: searchStartDate.value,
                endDate: searchEndDate.value,
                clientName: searchClient.value,
            }
        });

        invoiceList.value = response.data.invoice;
        currentPage.value = response.data.currentPage;
        totalPage.value = response.data.totalPages;
        totalInvoice.value = response.data.totalInvoice;
    } catch (error) {
        console.log(`거래 명세서 목록 요청 실패`, error);
    }
}

// 거래 명세서 상세 요청
const fetchInvoice = async (invoiceSeq) => {
    try {
        const response = await axios.get(`invoice/${invoiceSeq}`);

        invoiceDetail.value[invoiceSeq] = response.data;
        invoiceExtended.value[invoiceSeq] = true;
    } catch (error) {
        console.log(`거래 명세서 상세 요청 실패 ${error}`);
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

// 거래 명세서 목록 엑셀 다운로드
const excelDown = async () => {
    const response = await axios.get(`invoice/excel`, {
        params: {
            startDate: searchStartDate.value,
            endDate: searchEndDate.value,
            clientName: searchClient.value,
            invoiceStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value).join(",")
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

// 거래 명세서 삭제 요청
const deleteInvoice = async (invoiceSeq) => {
    try {
        const deleteConfirm = confirm(`선택한 거래 명세서를 삭제하시겠습니까?`);
        if (deleteConfirm) {
            const response = await axios.delete(`invoice/${invoiceSeq}`);
            fetchInvoiceList();
        }
    } catch (error) {
        console.log(`거래 명세서 삭제 요청 실패 ${error}`);
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

onMounted(() => {
    fetchInvoiceList();
    fetchItemDivision();
});

watch([searchStartDate, searchEndDate], () => {
    search();
});

watch(searchPage, () => {

    fetchInvoiceList();
});

watch(searchClient, () => {
    fetchClientHint(searchClient);
});

// 검색
function search() {
    searchPage.value = 1;

    fetchInvoiceList();
}

// 상태 키로 값 반환
function findStatusValue(array, key) {
    for (const item of array) {
        if (item.key === key) {
            return item.value
        }
    }
}

// 거래 명세서 클릭
function invoiceClick(invoiceSeq) {
    if (invoiceExtended.value[invoiceSeq]) {
        delete invoiceExtended.value[invoiceSeq];
        delete invoiceDetail.value[invoiceSeq];
    } else {
        fetchInvoice(invoiceSeq);
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
                    <p class="card-title">판매일</p>
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
        </div>
        <div class="col-md-9">
            <div>
                <div class="d-flex justify-content-between">
                    <div>검색결과: {{ totalInvoice }}개</div>
                    <div class="d-flex justify-content-end mt-3">
                        <b-button @click="excelDown()" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
                        <b-button variant="light" size="sm" class="button ms-2">거래 명세서 등록</b-button>
                    </div>
                </div>
                <div class="list-headline row">
                    <div class="list-headvalue col-8">거래 명세서</div>
                    <div class="list-headvalue col-2">거래처</div>
                    <div class="list-headvalue col-2">판매일</div>
                </div>
                <template v-if="invoiceList.length > 0">
                <div style="max-height: 600px; overflow-y: auto;">
                    <div v-for="invoice in invoiceList" :key="invoice.invoiceSeq" class="list-line row" @click="invoiceClick(invoice.invoiceSeq)">
                        <div class="col-8">
                            <b>{{ invoice.invoiceName }}</b>
                            <div v-if="!invoiceExtended[invoice.invoiceSeq]">{{ invoice.itemName }}</div>
                        </div>
                        <div class="list-value col-2">{{ invoice.clientName }}</div>
                        <div class="list-value col-2">{{ invoice.invoiceSaleDate }}</div>

                        <div class="d-flex justify-content-center">
                            <div v-if="invoiceExtended[invoice.invoiceSeq]" class="col-md-11 mt-3">
                                <b>총 수량</b>: {{ numberThree(invoiceDetail[invoice.invoiceSeq].invoiceTotalQuantity) }} 개<br>
                                <b>총 금액</b>: {{ `₩ ` + numberThree(invoiceDetail[invoice.invoiceSeq].invoiceExtendedPrice) }}<br>
                                <b>담당자</b>: {{ invoiceDetail[invoice.invoiceSeq].userName }}<br>
                                <b>판매일시</b>: {{ dayjs(invoiceDetail[invoice.invoiceSeq].invoiceSalesDate).format(`YYYY/MM/DD HH:mm:ss`) }}<br>
                                <b>비고</b>: {{ invoiceDetail[invoice.invoiceSeq].invoiceNote }}<br>
                                <div style="display:flex; flex-wrap: wrap;">
                                <template v-for="invoiceItem in invoiceDetail[invoice.invoiceSeq].invoiceItem">
                                    <div class="card item-card">
                                        <img :src=invoiceItem.itemImageUrl class="card-img-top">
                                        <div style="margin: 5px;">
                                            <small>{{ findStatusValue(itemDivisionList, invoiceItem.itemDivision) }}</small>
                                            <div style="display: flex; justify-content: space-between;">
                                                <b style="font-size: medium;">{{ invoiceItem.itemName }}</b>
                                                <small>{{ numberThree(invoiceItem.invoiceItemQuantity * invoiceItem.invoiceItemPrice) }} 원</small>
                                            </div>
                                            <small>{{ numberThree(invoiceItem.invoiceItemQuantity) }}개 / 개당 {{ numberThree(invoiceItem.invoiceItemPrice) }}원</small><br><br>
                                            <small style="margin-top: 20px;">비고: {{ invoiceItem.invoiceItemNote }}</small>
                                        </div>
                                    </div>
                                </template>
                                </div>
                                
                                <div class="d-flex justify-content-end align-items-center">
                                    <printIcon class="me-3 icon" @click.stop="printInvoice(invoice.invoiceSeq)"/>
                                    <editIcon class="me-3 icon" @click.stop="modifyInvoice(invoice.invoiceSeq)"/>
                                    <trashIcon class="icon" @click.stop="deleteInvoice(invoice.invoiceSeq)"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </template>
                <template v-else>
                    <b-card-text class="no-list-text">해당 검색 조건에 부합하는 거래 명세서가 존재하지 않습니다.</b-card-text>
                </template>
            </div>
            <div class="pagenation">
                <b-pagination
                    v-model="searchPage"
                    :total-rows="totalInvoice"
                    :per-page="searchSize">
                </b-pagination>
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
