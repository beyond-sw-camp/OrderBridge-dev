<script setup>
import { ref } from 'vue';

// 임시 데이터 셋
const rows = ref(123);
const perPage = ref(10);
const currentPage = ref(1);
const items = ref([
{ id: '2024/12/03 - 2', item: '김치볶음밥, 치즈볶음밥, 그냥볶음밥', client: '대한항공', quotation_date: '2024/1/1', status: '주문 전' },
{ id: '2024/12/03 - 1', item: '김치볶음밥, 치즈볶음밥, 그냥볶음밥', client: '아시아나항공', quotation_date: '2024/1/1', status: '주문 후' },
{ id: '2024/12/03 - 1', item: null, client: '아시아나항공', quotation_date: '2024/1/1', status: '주문 후' },
{ id: '2024/12/03 - 1', item: null, client: '아시아나항공', quotation_date: '2024/1/1', status: '주문 후' },
{ id: '위에', client: '거래처', quotation_date: '2024/1/1', status: '반려' },
{ id: '위에', client: '거래처', quotation_date: '2024/1/1', status: '반려' },
{ id: '위에', client: '거래처', quotation_date: '2024/1/1', status: '반려' },
{ id: '위에', client: '거래처', quotation_date: '2024/1/1', status: '반려' },
{ id: '위에', client: '거래처', quotation_date: '2024/1/1', status: '반려' },
{ id: '위에', client: '거래처', quotation_date: '2024/1/1', status: '반려' }
]);

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

</script>

<template>
    <div class="row">
        <div class="col-md-3">
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">견적일</p>
                    <input type="date" style="max-width: 40%;"/> ~ <input type="date" style="max-width: 40%;"/>
                </div>
            </div>
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">거래처명</p>
                    <b-input-group class="mt-3">
                        <b-form-input></b-form-input>
                        <b-input-group-text><svg width="1em" id="Layer_1" style="enable-background:new 0 0 512 512;" version="1.1" viewBox="0 0 512 512" xml:space="preserve" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"><path d="M344.5,298c15-23.6,23.8-51.6,23.8-81.7c0-84.1-68.1-152.3-152.1-152.3C132.1,64,64,132.2,64,216.3  c0,84.1,68.1,152.3,152.1,152.3c30.5,0,58.9-9,82.7-24.4l6.9-4.8L414.3,448l33.7-34.3L339.5,305.1L344.5,298z M301.4,131.2  c22.7,22.7,35.2,52.9,35.2,85c0,32.1-12.5,62.3-35.2,85c-22.7,22.7-52.9,35.2-85,35.2c-32.1,0-62.3-12.5-85-35.2  c-22.7-22.7-35.2-52.9-35.2-85c0-32.1,12.5-62.3,35.2-85c22.7-22.7,52.9-35.2,85-35.2C248.5,96,278.7,108.5,301.4,131.2z"/></svg></b-input-group-text>
                    </b-input-group>
                </div>
            </div>
            <div class="side-box card">
                <div class="card-body">
                    <p class="card-title">견적서 상태</p>
                    <b-form-checkbox>주문 전</b-form-checkbox>
                    <b-form-checkbox>주문 후</b-form-checkbox>
                    <b-form-checkbox>반려</b-form-checkbox>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div style="width: 90%;">
                <div class="d-flex justify-content-between">
                    <div>검색결과: {{ rows }}개</div>
                    <b-button variant="light" size="sm" class="button">견적서 등록</b-button>
                </div>
                <div class="list-headline row">
                    <div class="list-headvalue col-6">견적서</div>
                    <div class="list-headvalue col-2">거래처</div>
                    <div class="list-headvalue col-2">견적일</div>
                    <div class="list-headvalue col-2">상태</div>
                </div>
                <div style="max-height: 600px; overflow-y: auto;">
                    <div v-for="item in items" :key="item.id" class="list-line row" @click="itemExtend">
                        <div class="col-6">{{ item.id }}<br>
                            <div v-if="!item.item"><br></div>
                            <div v-else>{{ item.item }}</div></div>
                        <div class="list-value col-2">{{ item.client }}</div>
                        <div class="list-value col-2">{{ item.quotation_date }}</div>
                        <div class="list-value col-2">{{ item.status }}</div>
                    </div>
                </div>
            </div>
            <div class="pagenation">
                <b-pagination
                    v-model="currentPage"
                    :total-rows="rows"
                    :per-page="perPage">
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

</style>
