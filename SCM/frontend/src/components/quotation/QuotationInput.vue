<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { sSuccess, sWarning } from '@/common/salert';
import plusIcon from '@/assets/plus.svg'
import searchIcon from "@/assets/searchIcon.svg";
import router from "@/router/index.js";
import axios from "@/axios"
import dayjs from "dayjs";

const clientName = ref();

const requestClient = ref();
const quotationDate = ref();
const quotationNote = ref();
const quotationItemList = ref([]);

const calculatePrice = computed(() => {
  return quotationItemList.value.reduce((total, item) => {
    return total + (item.quotationItemPrice || 0) * (item.quotationItemQuantity || 1);
  }, 0);
});

// 거래처 목록 요청
const clientList = ref([]);
const clientPageSize = ref(10);
const clientPageNumber = ref(1);
const clientTotalCount = ref();

const fetchclientList = async () => {
    try {
        const response = await axios.get(`client`, {
            params: {
                page: clientPageNumber.value,
                size: clientPageSize.value
            }
        });

        clientList.value = response.data.clients;
        clientTotalCount.value = response.data.totalCount;
    } catch (error) {
        console.log(`거래처 목록 요청 실패 ${error}`);
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
        console.error(`품목 목록 요청 실패 ${error}`);
    }
};

// 품목 분류 요청
const itemDivisionList = ref();

const fetchItemDivision = async () => {
    try {
        const response = await axios.get(`item/item-division`);

        itemDivisionList.value = response.data;
    } catch (error) {
        console.log(`품목 분류 요청 실패 ${error}`);
    }
}

// 견적서 등록 요청
const createQuotation = async () => {
    try {
        const response = await axios.post('quotation',
            {
                quotationQuotationDate: quotationDate.value,
                clientSeq: requestClient.value,
                quotationNote: quotationNote.value,
                quotationItem: quotationItemList.value
            });

        await sSuccess('견적서가 등록되었습니다');
        await router.push('/quotation');
    } catch (error) {
        console.error(`견적서 등록 실패 ${error}`);
        throw error;
    }
}


watch(clientPageNumber, () => {
    fetchclientList();
})

watch(itemPageNumber, () => {
    fetchItemList()
});

onMounted(() => {
    fetchItemDivision();
});

// 모달에서 거래처 선택
function setClient(object) {
    clientName.value = object.clientName;
    requestClient.value = object.clientSeq;
    document.getElementById(`closeClientModal`).click();
}

// 모달에서 품목 선택
async function addItemList(selectedItem) {

    const existingItem = quotationItemList.value.find(
        (item) => item.itemSeq === selectedItem.itemSeq
    );
    if (!existingItem) {
        quotationItemList.value.push({
            itemSeq: selectedItem.itemSeq,
            itemName: selectedItem.itemName,
            itemImageUrl: selectedItem.itemImageUrl,
            quotationItemPrice: selectedItem.itemPrice || 0,
            quotationItemQuantity: 1,
            quotationItemNote: selectedItem.quotationItemNote
        });
    } else {
        await sWarning(`이미 추가된 품목입니다.`);
    }

    document.getElementById(`closeItemModal`).click();
}

// 품목 삭제
const removeItem = (quotationItemSeq) => {
    quotationItemList.value = quotationItemList.value.filter(
        (item) => item.itemSeq !== quotationItemSeq
    );

    delete quotationItemList.value[quotationItemSeq];
};

// 가격 갱신
function updatePrice(itemSeq) {
    const item = quotationItemList.value.find((quotationItem) => quotationItem.itemSeq === itemSeq);
    if (item) {
        item.calculatePrice = (item.quotationItemPrice || 0) * (item.quotationItemQuantity || 1);
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
    <div class="col-md-10 d-flex justify-content-end mt-3">
        <RouterLink to="/quotation">
            <b-button variant="light" size="sm" class="button ms-2">목록</b-button>
        </RouterLink>
    </div>
    <div class="d-flex justify-content-center">
        <div class="col-6 d-flex flex-column">
            <b-form-group label-cols="4" label-cols-lg="2" label="견적일시">
                <input class="form-control form-control-sm w-75" type="datetime-local" id="shippingInstructionDate"
                    v-model="quotationDate">
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처">
                <b-input-group class="w-75">
                    <b-form-input type="text" size="sm" id="client" v-model="clientName" placeholder="거래처" :disabled=true />
                    <b-input-group-text data-bs-toggle="modal" data-bs-target="#openClientModal"
                        @click="fetchclientList">
                        <searchIcon class="icon" />
                    </b-input-group-text>
                </b-input-group>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="비고">
                <b-form-input type="text" size="sm" id="note" v-model="quotationNote" placeholder="내용을 입력해 주세요.">
                </b-form-input>
            </b-form-group>
        </div>
    </div>

    <div class="px-4 d-flex flex-column align-items-center">
        <hr class="col-md-10 d-flex flex-column">
    </div>

    <div class="col-md-10" style="margin: 0 auto;">
        <h5 class="px-4">견적서 품목</h5>
        
        <div v-for="quotationItem in quotationItemList" class="mx-5 my-3">
            <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
                <div class="col-md-8">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h6 class="fw-bold">{{ quotationItem.itemName }}</h6>
                    </div>
                    <ul class="d-flex flex-wrap align-items-start">
                        <li class="mb-3 col-md-6">· 품목명: {{ quotationItem.itemName }}</li>
                        <li class="mb-3 col-md-6 d-flex align-items-center">· 품목 단가: ₩ <input type="number"
                                class="form-control form-control-sm ms-2" style="width: 100px;"
                                v-model.number="quotationItem.quotationItemPrice"
                                @input="updatePrice(quotationItem.itemSeq)"
                                :placeholder="quotationItem.quotationItemPrice ? '' : '가격 입력'" :min="1" /></li>
                        <li class="mb-3 col-md-6 d-flex align-items-center">· 주문 개수:<input type="number"
                                class="form-control form-control-sm ms-2" style="width: 100px;"
                                v-model.number="quotationItem.quotationItemQuantity"
                                @input="updatePrice(quotationItem.itemSeq)"
                                :placeholder="quotationItem.quotationItemQuantity ? '' : '수량 입력'" :min="1" /></li>
                        <li class="mb-3 col-md-6">· 품목 총 가격: ₩ {{ numberThree(quotationItem.quotationItemPrice * quotationItem.quotationItemQuantity) }} </li>
                        <li class="mb-3 col-md-6 d-flex">· 품목 비고: <b-form-input type="text" size="sm" v-model="quotationItem.quotationItemNote" style="width: auto;"/></li>
                    </ul>
                </div>
                <div class="col-md-4 d-flex justify-content-center align-items-center">
                    <img :src="quotationItem.itemImageUrl" alt="Item Image"
                        class="img-fluid border border-secondary rounded" style="max-width: 150px; height: auto;">
                </div>

                <b-button @click="removeItem(quotationItem.itemSeq)" variant="light" size="sm"
                    class="position-absolute btn-close" style="top: 10px; right: 10px;"></b-button>
            </div>
        </div>

        <div v-if="calculatePrice != 0" class="line-container mx-5">
            <div class="custom-line d-flex justify-content-end">
                <h6 class="fw-bold" style="margin-top: 17px; float:right;">총 가격 : ₩ {{ numberThree(calculatePrice) }}
                </h6>
            </div>
        </div> <br />

        <span data-bs-toggle="modal" data-bs-target="#openItemModal">
            <b-input-group-text
                class="mx-5 my-3 d-flex justify-content-center align-items-center border border-secondary rounded p-3"
                style="cursor: pointer;" @click="fetchItemList()">
                <plusIcon class="icon" />
            </b-input-group-text>
        </span>

        <div class="mx-5 my-3 d-flex justify-content-end">
            <b-button @click="createQuotation()" variant="light" size="sm" class="button ms-2">등록</b-button>

        </div>
    </div>
    <div class="d-flex justify-content-center">

    </div>
    <div class="modal fade" id="openClientModal" tabindex="-1" aria-labelledby="OrderModalLabel">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5">거래처 선택</h1>
                    <div class="ms-5">검색결과: {{ clientTotalCount }}개</div>
                    <button id="closeClientModal" type="button" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div style="max-height: 500px; overflow-y: scroll"
                        class="d-flex row justify-content-center align-items-center">

                        <div class="list-headline row">
                            <div class="list-head col-6">거래처</div>
                            <div class="list-head col-2">사업자번호</div>
                            <div class="list-head col-2">대표자</div>
                            <div class="list-head col-2">등록일</div>
                        </div>
                        <template v-if="clientList.length > 0">
                            <div v-for="client in clientList" class="list-line row" @click="setClient(client)">
                                <div class="list-body col-6">{{ client.clientName }}</div>
                                <div class="list-body col-2">{{ client.clientRegistrationNo }}</div>
                                <div class="list-body col-2">{{ client.clientRepresentative }}</div>
                                <div class="list-body col-2">{{ dayjs(client.clientRegDate).format(`YYYY/MM/DD`) }}
                                </div>
                            </div>
                        </template>
                        <template v-else>
                            <b-card-text class="no-list-text">해당 검색조건에 부합한 거래처가가 존재하지 않습니다.</b-card-text>
                        </template>
                    </div>
                </div>
                <div class="modal-footer pagination">
                    <b-pagination v-model="clientPageNumber" :totalRows="clientTotalCount" :perPage="clientPageSize" />
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="openItemModal" tabindex="-1" aria-labelledby="OrderModalLabel">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5">품목 선택</h1>
                    <div class="ms-5">검색결과: {{ itemTotalCount }}개</div>
                    <button id="closeItemModal" type="button" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div style="max-height: 500px; overflow-y: scroll" class="d-flex row justify-content-center align-items-center">

                        <div class="list-headline row">
                            <div class="list-head col-5">품목명</div>
                            <div class="list-head col-2">가격</div>
                            <div class="list-head col-3">유효기간</div>
                            <div class="list-head col-2">품목 단위</div>
                        </div>
                        <template v-if="itemList.length > 0">
                            <div v-for="Item in itemList" class="list-line row" @click="addItemList(Item)">
                                <div class="list-body col-5">{{ Item.itemName }}</div>
                                <div class="list-body col-2">{{ Item.itemPrice }} 원</div>
                                <div class="list-body col-3">{{ Item.itemExpirationHour }} 시간</div>
                                <div class="list-body col-2">{{ findStatusValue(itemDivisionList, Item.itemDivision) }}
                                </div>
                            </div>
                        </template>
                        <template v-else>
                            <b-card-text class="no-list-text">해당 검색조건에 부합한 품목이 존재하지 않습니다.</b-card-text>
                        </template>
                    </div>
                </div>
                <div class="modal-footer pagination">
                    <b-pagination v-model="itemPageNumber" :totalRows="itemTotalCount" :perPage="itemPageSize" />
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
