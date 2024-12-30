<script setup>
import { ref, watch } from "vue";
import { useUserStore } from "@/stores/UserStore.js";
import plusIcon from '@/assets/plus.svg'
import searchIcon from "@/assets/searchIcon.svg";
import axios from "@/axios"
import dayjs from "dayjs";

const salesOrder = ref();
const username = useUserStore().$id;
const client = ref();

const quotationItemList = ref([]);

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
        if (clientHintList.value.length === 1 && clientHintList.value[0] === client.value) {
            clientHintList.value = null;
        }
    }
}



watch(client, () => {
    fetchClientHint(client);
});
</script>

<template>
    <div class="d-flex justify-content-end mt-3">
        <RouterLink to="/quotation">
            <b-button variant="light" size="sm" class="button ms-2">목록</b-button>
        </RouterLink>
    </div>
    <div class="d-flex justify-content-center">
        <div class="col-6 d-flex flex-column">
            <b-form-group label-cols="4" label-cols-lg="2" label="견적일시">
                <input class="form-control form-control-sm w-75" type="datetime-local" id="shippingInstructionDate"
                    v-model="quotataionDate">
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처">
                <b-input-group class="w-75">
                    <b-form-input type="text" size="sm" id="client" v-model="client" placeholder="거래처" />
                    <b-input-group-text>
                        <searchIcon class="icon" />
                    </b-input-group-text>
                </b-input-group>
                <div class="clientHint" style="position: absolute; z-index: 5;">
                    <ul class="list-group">
                        <template v-for="hint in clientHintList">
                            <li class="list-group-item list-group-item-action" @click="client = hint">{{ hint }}</li>
                        </template>
                    </ul>
                </div>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="담당자">
                <b-form-input class="w-75" size="sm" id="user" v-model="username" disabled="false">
                </b-form-input>
            </b-form-group>

            <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="비고">
                <b-form-input type="text" size="sm" id="note" v-model="note" placeholder="내용을 입력해 주세요.">
                </b-form-input>
            </b-form-group>
        </div>
    </div>

    <div class="px-4 d-flex flex-column align-items-center">
        <hr class="col-md-10 d-flex flex-column">
    </div>

    <h5 class="px-4">견적서 품목</h5>
    <div v-for="quotationItem in quotationItemList" class="mx-5 my-3">
        <div class="d-flex flex-row border border-secondary rounded p-3 position-relative">
            <div class="col-md-8">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h6 class="fw-bold">{{ quotationItem.itemName }}</h6>
                </div>
                <ul class="d-flex flex-wrap align-items-start">
                    <li class="mb-3 col-md-6">· 품목명 : {{ quotationItem.itemName }}</li>
                    <li class="mb-3 col-md-6">· 품목 단가 : ₩ {{ quotationItem.quotationItemItemPrice != null ?
                        quotationItem.quotationItemItemPrice : 0 }} </li>
                    <li class="mb-3 col-md-6 d-flex align-items-center">· 주문 개수 :<input type="number"
                            class="form-control form-control-sm ms-2" style="width: 70px;"
                            v-model.number="quotationItem.quotationItemItemQuantity"
                            @input="updatePrice(quotationItem.itemSeq)"
                            :placeholder="quotationItem.quotationItemItemQuantity ? '' : '수량 입력'" :min="1" /></li>
                    <li class="mb-3 col-md-6">· 품목 총 가격 : ₩ {{ quotationItem.calculatePrice != null ?
                        quotationItem.calculatePrice : quotationItem.quotationItemItemPrice }} </li>
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
            <h6 class="fw-bold" style="margin-top: 17px; float:right;">총 가격 : ₩ {{ calculatePrice }}
            </h6>
        </div>
    </div> <br />

    <span data-bs-toggle="modal" data-bs-target="#openItemModal">
        <b-input-group-text
            class="mx-5 my-3 d-flex justify-content-center align-items-center border border-secondary rounded p-3"
            style="cursor: pointer;">
            <plusIcon class="icon" />
        </b-input-group-text>
    </span>

    <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button @click="createPurchaseOrder" variant="light" size="sm" class="button ms-2">등록</b-button>

    </div>
    <div class="d-flex justify-content-center">

    </div>
    <div class="modal fade" id="openItemModal" tabindex="-1" aria-labelledby="OrderModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5">품목 선택</h1>
                    <div class="ms-5">검색결과: {{ totalCount }}개</div>
                    <button type="button" @click="closeItemModal" class="button btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div style="max-height: 500px; overflow-y: scroll"
                        class="d-flex row justify-content-center align-items-center">

                        <div class="list-headline row">
                            <div class="list-head col-5">품목명</div>
                            <div class="list-head col-2">가격</div>
                            <div class="list-head col-3">유효기간</div>
                            <div class="list-head col-2">품목 단위</div>
                        </div>
                        <template v-if="quotationItemList.length > 0">
                            <div v-for="Item in quotationItemList" :key="Item.itemSeq"
                                class="list-line row" @click="addToOrderList(Item)">
                                <div class="list-body col-5 left">{{ Item.itemName }}</div>
                                <div class="list-body col-2 left">{{ Item.itemPrice.toLocaleString() }} 원</div>
                                <div class="list-body col-3 left">{{ Item.itemExpirationHour }} 시간</div>
                                <div class="list-body col-2">{{ formatStatus(Item.itemDivision) }}</div>
                            </div>
                        </template>
                        <template v-else>
                            <b-card-text class="no-list-text">해당 검색조건에 부합한 품목이 존재하지 않습니다.</b-card-text>
                        </template>
                    </div>
                </div>
                <div class="modal-footer pagination">
                    <b-pagination v-model="pageNumber" :totalRows="totalCount" :perPage="pageSize" />
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
    /* 가로 중앙 정렬 */
    margin-top: 20px;
}

.left {
    text-align: left;
}
</style>
