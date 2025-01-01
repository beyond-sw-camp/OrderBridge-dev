<script setup>
import { onMounted, ref, watch } from 'vue';
import { sError } from "@/common/salert";
import axios from "@/axios.js";
import dayjs from "dayjs";
import searchIcon from "@/assets/searchIcon.svg";

const searchStartDate = ref('');
const searchEndDate = ref('');
const searchClient = ref('');
const quotationSituationList = ref([]);
const quotationSituationTotal = ref([]);

// 견적서 현황 요청
const fetchQuotationSituationList = async () => {
    try {
        const response = await axios.get(`quotation/situation`, {
            params: {
                startDate: searchStartDate.value,
                endDate: searchEndDate.value,
                clientName: searchClient.value,
            }
        });

        quotationSituationTotal.value = response.data.pop();
        quotationSituationList.value = response.data;
    } catch (error) {
        console.error(`견적서 현황 불러오기 실패`, error);
        await sError(error);
    }
};

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
    fetchQuotationSituationList();
});

watch([searchStartDate, searchEndDate], () => {
    fetchQuotationSituationList();
})

watch(searchClient, () => {
    fetchClientHint(searchClient);
});

// 영역 프린트
const printTable = () => {
    const printContent = document.getElementById('print-area-quotation-situation').innerHTML;
    const originalContent = document.body.innerHTML;

    document.body.innerHTML = printContent;
    window.print();
    document.body.innerHTML = originalContent;
    location.reload();
}

// 엑셀 다운로드
const excelDown = async () => {
    const response = await axios.get(`quotation/situation/excel`, {
        params: {
            startDate: searchStartDate.value,
            endDate: searchEndDate.value,
            clientName: searchClient.value
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

function search() {
    fetchQuotationSituationList();
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
        <div class="d-flex justify-content-end mt-3">
            <b-button @click="excelDown()" variant="light" size="sm" class="button ms-2 mb-3">엑셀 다운로드</b-button>
            <b-button @click="printTable()" variant="light" size="sm" class="button ms-2 mb-3">인쇄</b-button>
        </div>
        <div id="print-area-quotation-situation" class="content">
            <div class="table-container">
            <!-- 테이블 -->
            <table>
                <thead>
                <tr>
                <th>번호</th>
                <th>견적일</th>
                <th>이름</th>
                <th>총 수량</th>
                <th>총 가격</th>
                <th>거래처</th>
                <th>비고</th>
                </tr>
                </thead>
                <tbody v-if="quotationSituationList.length > 0">
                <!-- 필터링된 결과 및 월별 합계 출력 -->
                <template v-for="(quotationSituation, index) in quotationSituationList" :key="index">
                <tr v-if="quotationSituation.quotationQuotationDate">
                    <td>{{ index + 1 }}</td>
                    <td>{{ dayjs(quotationSituation.quotationQuotationDate).format('YYYY/MM/DD') }}</td>
                    <td>{{ quotationSituation.quotationName }}</td>
                    <td>{{ quotationSituation.quotationTotalQuantity !== null ? quotationSituation.quotationTotalQuantity.toLocaleString() : '0' }}</td>
                    <td> ￦ {{ quotationSituation.quotationExtendedPrice !== null ? quotationSituation.quotationExtendedPrice.toLocaleString() : '0' }}</td>
                    <td>{{ quotationSituation.clientName }}</td>
                    <td>{{ quotationSituation.quotationNote !== null ? quotationSituation.quotationNote : '-' }}</td>
                </tr>
                <tr v-else class="monthly-total">
                    <td></td>
                    <td>{{ quotationSituation.quotationQuotationMonth }}</td>
                    <td></td>
                    <td>{{ quotationSituation.quotationMonthQuantity.toLocaleString() }}</td>
                    <td> ￦ {{ quotationSituation.quotationMonthPrice.toLocaleString() }}</td>
                    <td></td>
                    <td></td>
                </tr>
                </template>

                </tbody>
                <tbody v-else>
                <tr>
                <td colspan="6">해당 검색조건에 부합한 견적서가 존재하지 않습니다</td>
                </tr>
                </tbody>
                <!-- 총합 -->
                <tfoot v-if="quotationSituationTotal">
                <tr>
                <td colspan="5">총합</td>
                <td colspan="2">￦ {{ quotationSituationTotal.quotationMonthPrice ? quotationSituationTotal.quotationMonthPrice.toLocaleString() : null }}</td>
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

.content {
    display: flex;
    justify-content: space-around;
    max-height: 400px; /* 스크롤바가 나타날 최대 높이 */
    overflow-y: auto; /* 수직 스크롤바 */
}


.table-container {
    width: 100%;
    overflow-x: auto; /* 가로 스크롤바 */
}

.search-input > input {
    width: 100%;
    border: 1px solid #D9D9D9;
    border-radius: 8px;
    padding: 10px 12px;
    font-size: 14px;
}

.search-input > img {
    position: absolute;
    width: 17px;
    top: 10px;
    right: 12px;
    margin: 0;
}

.table-container {
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

/* 인쇄 스타일 */
@media print {
    /* 인쇄할 영역만 표시 */
    body * {
        visibility: hidden; /* 전체 요소 숨기기 */
    }

    #print-area, #print-area * {
        visibility: visible; /* 특정 영역만 표시 */
    }

    #print-area {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
    }

    /* 버튼 숨기기 */
    button {
        display: none;
    }
}
</style>
