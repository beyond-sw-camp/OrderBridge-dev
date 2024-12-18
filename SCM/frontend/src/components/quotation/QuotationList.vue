<script setup>
import { ref, onMounted, watch } from 'vue';
import searchIcon from "@/assets/searchIcon.svg"
import axios from "@/axios"

const quotationList = ref([]);
const quotationStatusList = ref([]);
const currentPage = ref(1);
const totalPage = ref(1);
const totalQuotation = ref(0);
const searchPage = ref(1);
const searchSize = ref(10);
const searchStartDate = ref(null);
const searchEndDate = ref(null);
const searchClient = ref(null);
const searchStatus = ref(new Set());

// 견적서 목록 요청
const fetchQuotationList = async () => {
    try {
        const response = await axios.get(`quotation`, {
            params: {
                page: searchPage.value,
                size: searchSize.value,
                startDate: searchStartDate.value,
                endDate: searchEndDate.value,
                clientName: searchClient.value,
                quotationStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value).join(",")
            }
        });

        quotationList.value = response.data.quotation;
        currentPage.value = response.data.currentPage;
        totalPage.value = response.data.totalPages;
        totalQuotation.value = response.data.totalQuotation;
    } catch (error) {
        console.log(`견적서 목록 요청 실패`, error);
    }
}

// 견적서 상태 분류 목록 요청
const fetchQuotationStatus = async () => {
    try { 
        const response = await axios.get(`quotation/status`);
        
        quotationStatusList.value = response.data;
    } catch (error) {
        console.log(`견적서 상태 분류 목록 요청 실패`, error);
    }
}

// 견적서 목록 엑셀 다운로드
const excelDown = async () => {
    const response = await axios.get(`quotation/excel`, {
        params: {
            startDate: searchStartDate.value,
            endDate: searchEndDate.value,
            clientName: searchClient.value,
            quotationStatus: searchStatus.value.size === 0 ? null : Array.from(searchStatus.value).join(",")
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

// 선택한 Item 확장 | 축소
function itemExtend(event) {
    // 선택한 list-line의 id 추출
    let listLine = event.target;
    for (let i = 0; i < 5; i++) {
        if (listLine.classList[0] === "list-line") { break; }
        else { listLine = listLine.parentNode; }
    }
    let id = listLine.getElementsByClassName("col-6")[0].innerHTML.split("<br")[0];

    console.log(id);
    console.log(listLine);

    // 찾은 id 기반으로 API 호출

    // API 응답값으로 list-line 확장 | 축소
}

// 품목 만드는 반복문 추가
function addItemCard() {

}

onMounted(() => {
    fetchQuotationList();
    fetchQuotationStatus();
});

watch([searchStartDate, searchEndDate, searchStatus.value], () => {
    search();
});

watch(searchPage, () => {

    fetchQuotationList();
});

function statusCheck(status) {
    searchStatus.value.has(status) ? searchStatus.value.delete(status)
                                   : searchStatus.value.add(status);
}

function search() {
    searchPage.value = 1;

    fetchQuotationList();
}

</script>

<template>
    <div class="row">
        <div class="col-md-3">
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">견적일</p>
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
                </div>
            </div>
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">견적서 상태</p>
                    <template v-for="quotationStatus in quotationStatusList">
                        <b-form-checkbox @click="statusCheck(quotationStatus.key)">{{ quotationStatus.value }}</b-form-checkbox>
                    </template>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div style="width: 90%;">
                <div class="d-flex justify-content-between">
                    <div>검색결과: {{ totalQuotation }}개</div>
                    <div class="d-flex justify-content-end mt-3">
                        <b-button @click="excelDown()" variant="light" size="sm" class="button">엑셀 다운로드</b-button>
                        <b-button variant="light" size="sm" class="button ms-2">견적서 등록</b-button>
                    </div>
                </div>
                <div class="list-headline row">
                    <div class="list-headvalue col-6">견적서</div>
                    <div class="list-headvalue col-2">거래처</div>
                    <div class="list-headvalue col-2">견적일</div>
                    <div class="list-headvalue col-2">상태</div>
                </div>
                <template v-if="quotationList.length > 0">
                <div style="max-height: 600px; overflow-y: auto;">
                    <div v-for="quotation in quotationList" :key="quotation.quotationSeq" class="list-line row" @click="itemExtend">
                        <div class="col-6">{{ quotation.quotationName }}<br>
                            <div v-if="!quotation.itemName"><br></div>
                            <div v-else>{{ quotation.itemName }}</div></div>
                        <div class="list-value col-2">{{ quotation.clientName }}</div>
                        <div class="list-value col-2">{{ quotation.quotationQuotationDate }}</div>
                        <div class="list-value col-2">{{ quotation.quotationStatus }}</div>
                    </div>
                </div>
                </template>
                <template v-else>
                    <b-card-text class="no-list-text">해당 검색 조건에 부합하는 견적서가 존재하지 않습니다.</b-card-text>
                </template>
            </div>
            <div class="pagenation">
                <b-pagination
                    v-model="searchPage"
                    :total-rows="totalQuotation"
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

</style>
