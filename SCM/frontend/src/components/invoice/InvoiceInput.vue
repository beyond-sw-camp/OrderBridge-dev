<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { sSuccess, sError } from "@/common/salert";
import searchIcon from "@/assets/searchIcon.svg";
import router from "@/router/index.js";
import axios from "@/axios"

const clientName = ref();
const salesOrderName = ref();

const requestSalesOrder = ref();
const requestClient = ref();
const invoiceSaleDate = ref();
const invoiceNote = ref();
const invoiceItemList = ref([]);

const calculatePrice = computed(() => {
    return invoiceItemList.value.reduce((total, item) => {
        return total + (item.invoiceItemPrice || 0) * (item.invoiceItemQuantity || 1);
    }, 0);
});

// 주문서 목록 요청
const salesOrderList = ref([]);
const salesOrderPageSize = ref(10);
const salesOrderPageNumber = ref(1);
const salesOrderTotalCount = ref();

// 거래처 목록 요청
const fetchSalesOrderList = async () => {
    try {
        const response = await axios.get(`sales-order`, {
            params: {
                page: salesOrderPageNumber.value,
                size: salesOrderPageSize.value
            }
        });

        salesOrderList.value = response.data.salesOrder;
        salesOrderTotalCount.value = response.data.totalSalesOrder;
    } catch (error) {
        await sError(error);
        console.error(`거래처 목록 요청 실패`, error);
    }
}

// 주문서 상세 요청
const salesOrderItemList = ref();

const fetchSalesOrderItemList = async (salesOrderSeq) => {
    try {
        const response = await axios.get(`sales-order/${salesOrderSeq}`);

        salesOrderItemList.value = response.data.salesOrderItem;
        requestClient.value = response.data.clientSeq;
    } catch (error) {
        await sError(error);
        console.error(`주문서 상세 요청 실패`, error);
    }
}

// 품목 목록 요청
const itemList = ref([]);
const itemPageSize = ref(10);
const itemPageNumber = ref(1);
const itemTotalCount = ref();

const fetchItemList = async () => {
    try {
        const response = await axios.get(`item`, {
            params: {
                page: itemPageNumber.value,
                size: itemPageSize.value
            }
        });

        itemList.value = Array.isArray(response.data.content) ? response.data.content : [];
        itemTotalCount.value = response.data.totalElements;
    } catch (error) {
        await sError(error);
        console.error(`품목 목록 요청 실패`, error);
    }
};

// 품목 상세 요청
const itemDetail = ref();

const fetchItem = async (itemSeq) => {
    try {
        const response = await axios.get(`item/${itemSeq}`);

        itemDetail.value = response.data.itemDTO;
    } catch (error) {
        await sError(error);
        console.error(`품목 상세 요청 실패`, error);
    }
}

// 품목 분류 요청
const itemDivisionList = ref();

const fetchItemDivision = async () => {
    try {
        const response = await axios.get(`item/item-division`);

        itemDivisionList.value = response.data;
    } catch (error) {
        await sError(error);
        console.error(`품목 분류 요청 실패`, error);
    }
}

// 거래 명세서 등록 요청
const createInvoice = async () => {
    try {
        const response = await axios.post('invoice',
            {
                salesOrderSeq: requestSalesOrder.value,
                invoiceSaleDate: invoiceSaleDate.value,
                invoiceNote: invoiceNote.value,
                createInvoiceItemRequestList: invoiceItemList.value
            });

        sSuccess(`거래 명세서가 등록되었습니다.`);
        await router.push('/invoice');
    } catch (error) {
        await sError(error);
        console.error(`거래 명세서 등록 요청 실패`, error);
    }
}

// 주문서 상태 분류 목록 요청
const salesOrderStatusList = ref();

const fetchInvoiceStatus = async () => {
    try {
        const response = await axios.get(`sales-order/status`);

        salesOrderStatusList.value = response.data;
    } catch (error) {
        await sError(error);
        console.error(`주문서 상태 분류 목록 요청 실패`, error);
    }
}


watch(salesOrderPageNumber, () => {
    fetchSalesOrderList();
})

watch(itemPageNumber, () => {
    fetchItemList()
});

onMounted(() => {
    fetchItemDivision();
    fetchInvoiceStatus();
});

// 모달에서 주문서 선택
async function setSalesOrder(object) {
    salesOrderName.value = object.salesOrderName;
    requestSalesOrder.value = object.salesOrderSeq;
    clientName.value = object.clientName;

    invoiceItemList.value = [];
    await fetchSalesOrderItemList(object.salesOrderSeq);

    for (const salesOrderItem of salesOrderItemList.value) {
        await fetchItem(salesOrderItem.itemSeq);
        
        invoiceItemList.value.push({
            itemSeq: salesOrderItem.itemSeq,
            itemName: itemDetail.value.itemName,
            itemImageUrl: itemDetail.value.itemImageUrl,
            invoiceItemPrice: salesOrderItem.salesOrderItemPrice,
            invoiceItemQuantity: salesOrderItem.salesOrderItemQuantity
        });
    }
    
    document.getElementById(`closeClientModal`).click();
}

// 품목 삭제
const removeItem = (invoiceItemSeq) => {
    invoiceItemList.value = invoiceItemList.value.filter(
        (item) => item.itemSeq !== invoiceItemSeq
    );

    delete invoiceItemList.value[invoiceItemSeq];
};

// 가격 갱신
function updatePrice(itemSeq) {
    const item = invoiceItemList.value.find((invoiceItem) => invoiceItem.itemSeq === itemSeq);
    if (item) {
        item.calculatePrice = (item.invoiceItemPrice || 0) * (item.invoiceItemQuantity || 1);
        calculatePrice.value = item.calculatePrice;
    }
}

// 상태 키로 값 반환
function findStatusValue(array, key) {
    for (const item of array) {
        if (item.key === key) {
            return item.value
        }
    }
}

// 숫자 쉼표 삽입
function numberThree(number) {
    return `${number.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}`;
}
</script>

<template>
    <div class="d-flex justify-content-end mt-3">
        <RouterLink to="/invoice">
            <b-button variant="light" size="sm" class="button ms-2">목록</b-button>
        </RouterLink>
    </div>
    <div class="d-flex justify-content-center">
        <div class="col-6 d-flex flex-column">
            <b-form-group label-cols="4" label-cols-lg="2" label="판매일시">
                <input class="form-control form-control-sm w-75" type="datetime-local" v-model="invoiceSaleDate">
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="주문서">
                <b-input-group class="w-75">
                    <b-form-input type="text" size="sm" id="salesOrder" v-model="salesOrderName" placeholder="주문서" :disabled=true />
                    <b-input-group-text data-bs-toggle="modal" data-bs-target="#openSalesOrderModal" @click="fetchSalesOrderList">
                        <searchIcon class="icon" />
                    </b-input-group-text>
                </b-input-group>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처">
                <b-input-group class="w-75">
                    <b-form-input type="text" size="sm" id="client" v-model="clientName" :disabled=true />
                </b-input-group>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="비고">
                <b-form-input type="text" size="sm" id="note" v-model="invoiceNote" placeholder="내용을 입력해 주세요.">
                </b-form-input>
            </b-form-group>
        </div>
    </div>

    <div class="px-4 d-flex flex-column align-items-center">
        <hr class="col-md-10 d-flex flex-column">
    </div>

    <h5 class="px-4">거래 명세서 품목</h5>
    <div v-for="invoiceItem in invoiceItemList" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
            <div class="col-md-8">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h6 class="fw-bold">{{ invoiceItem.itemName }}</h6>
                </div>
                <ul class="d-flex flex-wrap align-items-start">
                    <li class="mb-3 col-md-6">· 품목명: {{ invoiceItem.itemName }}</li>
                    <li class="mb-3 col-md-6 d-flex align-items-center">· 품목 단가: ₩ <input type="number"
                            class="form-control form-control-sm ms-2" style="width: 100px;"
                            v-model.number="invoiceItem.invoiceItemPrice"
                            @input="updatePrice(invoiceItem.itemSeq)"
                            :placeholder="invoiceItem.invoiceItemPrice ? '' : '가격 입력'" :min="1" /></li>
                    <li class="mb-3 col-md-6 d-flex align-items-center">· 판매 개수:<input type="number"
                            class="form-control form-control-sm ms-2" style="width: 100px;"
                            v-model.number="invoiceItem.invoiceItemQuantity"
                            @input="updatePrice(invoiceItem.itemSeq)"
                            :placeholder="invoiceItem.invoiceItemQuantity ? '' : '수량 입력'" :min="1" /></li>
                    <li class="mb-3 col-md-6">· 품목 총 가격: ₩ {{ numberThree(invoiceItem.invoiceItemPrice *
                        invoiceItem.invoiceItemQuantity) }} </li>
                    <li class="mb-3 col-md-6 d-flex">· 품목 비고: <b-form-input type="text" size="sm"
                            v-model="invoiceItem.invoiceItemNote" style="width: auto;" /></li>
                </ul>
            </div>
            <div class="col-md-4 d-flex justify-content-center align-items-center">
                <img :src="invoiceItem.itemImageUrl" alt="Item Image"
                    class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
            </div>

            <b-button @click="removeItem(invoiceItem.itemSeq)" variant="light" size="sm"
                class="position-absolute btn-close" style="top: 10px; right: 10px;"></b-button>
        </div>
    </div>

    <div v-if="calculatePrice != 0" class="line-container mx-5">
        <div class="custom-line d-flex justify-content-end">
            <h6 class="fw-bold" style="margin-top: 17px; float:right;">총 가격 : ₩ {{ numberThree(calculatePrice) }}
            </h6>
        </div>
    </div> <br>

    <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button @click="createInvoice()" variant="light" size="sm" class="button ms-2">등록</b-button>

    </div>
    <div class="d-flex justify-content-center">

    </div>
    <div class="modal fade" id="openSalesOrderModal" tabindex="-1" aria-labelledby="OrderModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5">주문서 선택</h1>
                    <div class="ms-5">검색결과: {{ salesOrderTotalCount }}개</div>
                    <button id="closeClientModal" type="button" class="button btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div style="max-height: 500px; overflow-y: scroll"
                        class="d-flex row justify-content-center align-items-center">

                        <div class="list-headline row">
                            <div class="list-head col-6">주문서</div>
                            <div class="list-head col-2">거래처</div>
                            <div class="list-head col-2">주문일</div>
                            <div class="list-head col-2">상태</div>
                        </div>
                        <template v-if="salesOrderList.length > 0">
                            <div v-for="salesOrder in salesOrderList" class="list-line row"
                                @click="setSalesOrder(salesOrder)">
                                <div class="list-body col-6">{{ salesOrder.salesOrderName }}</div>
                                <div class="list-body col-2">{{ salesOrder.clientName }}</div>
                                <div class="list-body col-2">{{ salesOrder.salesOrderOrderDate }}</div>
                                <div class="list-body col-2">{{ findStatusValue(salesOrderStatusList,
                                    salesOrder.salesOrderStatus) }}
                                </div>
                            </div>
                        </template>
                        <template v-else>
                            <b-card-text class="no-list-text">해당 검색조건에 부합한 주문서가 존재하지 않습니다.</b-card-text>
                        </template>
                    </div>
                </div>
                <div class="modal-footer pagination">
                    <b-pagination v-model="salesOrderPageNumber" :totalRows="salesOrderTotalCount"
                        :perPage="salesOrderPageSize" />
                </div>
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

.icon {
    width: 20px;
    height: 20px;
}

.list-headline {
    width: 90%;
    border-bottom: 1px solid black;
    margin-left: 1px;
    margin-bottom: 10px;
    padding: 10px 5px 10px 5px;
}

.list-head {
    text-align: center;
}

.list-line {
    width: 90%;
    border: 1px solid Silver;
    border-radius: 8px;
    margin-left: 1px;
    margin-top: 20px;
    padding: 10px 5px 10px 5px;
}

.list-body {
    text-align: center;
    margin: auto 0;
}

.pagination {
    justify-content: center;
    margin-top: 20px;
}

.left {
    text-align: left;
}

li {
    list-style: none;
}
</style>
