<script setup>
import {ref} from "vue";
import trashIcon from '@/assets/trashIcon.svg'

const items = ref([
  {
    itemName: '김치볶음밥',
    itemDivision: '제품',
    itemQuantity: 1200,
    itemPrice: 3000,
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
  {
    itemName: '치즈볶음밥',
    itemDivision: '제품',
    itemQuantity: 1100,
    itemPrice: 31000,
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
  {
    itemName: '김치볶음밥',
    itemDivision: '제품',
    itemQuantity: 200,
    itemPrice: 3000,
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
  {
    itemName: '김치볶음밥',
    itemDivision: '제품',
    itemQuantity: 100,
    itemPrice: 3000,
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
]);
const rows = ref(123);
const perPage = ref(10);
const currentPage = ref(1);


const name = ref(0);
const division = ref(0);
const quantity = ref(0);
const price = ref(0);
const note = ref(null);
const total = ref(0);

const additionalItems = ref(false);
const addItem = (index) => {
  name.value = items.value[index].itemName;
  division.value = items.value[index].itemDivision;
  quantity.value = items.value[index].itemQuantity;
  price.value = items.value[index].itemPrice;
  note.value = items.value[index].itemNote;
  total.value = quantity.value * price.value;
  additionalItems.value = true;
}
const deleteItem = () => {
  additionalItems.value = false;
}
</script>

<template>
  <div class="col-md-10 d-flex flex-column">
    <h5 class="px-4">물품 등록</h5>
    <div style="max-height: 250px; overflow-y: auto;">
      <div style="max-height: 200px;" class="mx-5 my-3 d-flex flex-row border border-secondary rounded"
           v-if="additionalItems">
        <!--    모달에서 물품 선택시 내용을 가져오도록 변경 필요 -->
        <b-img class="p-2 col-md-4" src="https://picsum.photos/200/200" fluid alt="Responsive image"></b-img>
        <div class="p-2 col-md-8">
          <div class="mb-4 d-flex justify-content-between">
            <span class="fw-bold">{{ name }}</span>
            <trashIcon class="icon" @click="deleteItem"/>
          </div>
          <div class="d-flex">
            <ul class="col-md-4">
              <li class="mb-3">
                · 품목 : {{ division }}
              </li>
              <li class="mb-3 d-flex flex-row">
                · 수량 :
                <b-form-input class="ms-2 me-2 w-50" type="text" v-model="quantity" size="sm"></b-form-input>
                개
              </li>
              <li class="mb-3 d-flex flex-row">
                · 단가 :
                <b-form-input class="ms-2 me-2 w-50" type="text" v-model="price" size="sm"></b-form-input>
                ₩
              </li>
            </ul>
            <ul class="col-md-8 d-flex flex-column justify-content-between">
              <li class="d-flex flex-row">
                · 비고 :
                <b-form-input class="ms-2 w-75" type="text" v-model="note" size="sm"></b-form-input>
              </li>
              <li>
                <span class="fw-bold float-end">총금액 {{ total }} ₩</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="mx-5 my-3">
        <b-button class="w-100" size="lg" variant="outline-dark" data-bs-toggle="modal" data-bs-target="#itemModal">+
        </b-button>
      </div>
    </div>

    <div class="mx-5 my-3 d-flex justify-content-end">
      <b-button class="mx-3" pill variant="primary">확인</b-button>
      <b-button class="mx-3" pill>목록</b-button>
    </div>
  </div>

    <!--   item Modal bootstrap 1번 -->
<!--      <div class="modal fade" id="itemModal" tabindex="-1" aria-labelledby="itemModalLabel" aria-hidden="true">-->
<!--        <div class="modal-dialog modal-xl">-->
<!--          <div class="modal-content">-->
<!--            <div class="modal-header">-->
<!--              <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>-->
<!--              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>-->
<!--            </div>-->
<!--            <div style="max-height: 500px; overflow-y: scroll" class="modal-body">-->
<!--              <div class="d-flex justify-content-center">-->
<!--                <div class="item-div" v-for="item in items" data-bs-dismiss="modal" @click="addItem">-->
<!--                  <p class="fw-bold">{{ item.itemName }}</p>-->
<!--                  <b-img class="mb-4" src="https://picsum.photos/200/200" fluid alt="Responsive image"></b-img>-->
<!--                  <ul>-->
<!--                    <li class="mb-3">품목 : {{ item.itemDivision }}</li>-->
<!--                    <li class="mb-3">수량 : {{ item.itemQuantity }} 개</li>-->
<!--                    <li class="mb-3">단가 : {{ item.itemPrice }} ₩</li>-->
<!--                    <li class="mb-3">총금액 {{ items[0].totalQuantity }} ₩</li>-->
<!--                    <li class="mb-3">비고 : {{ item.itemNote }}</li>-->
<!--                  </ul>-->
<!--                </div>-->
<!--              </div>-->
<!--            </div>-->
<!--            <div class="modal-footer pagination">-->
<!--              <b-pagination-->
<!--                  v-model="currentPage"-->
<!--                  :total-rows="rows"-->
<!--                  :per-page="perPage">-->
<!--              </b-pagination>-->
<!--            </div>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->

  <!-- item Modal bootstrap 2번 -->
  <div class="modal fade" id="itemModal" tabindex="-1" aria-labelledby="itemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div style="max-height: 500px; overflow-y: scroll" class="modal-body">
          <div v-for="n in 2" :key="n">
            <div class="d-flex justify-content-center">
              <div style="max-height: 300px" class="item-div" v-for="(item, index) in items" key="index"
                   data-bs-dismiss="modal" @click="addItem(index)">
                <p class="fw-bold">{{ item.itemName }}</p>
                <b-img style="max-height: 150px" class="mb-1" src="https://picsum.photos/200/200" fluid
                       alt="Responsive image"></b-img>
                <ul>
                  <li class="mb-1">품목 : {{ item.itemDivision }}</li>
                  <li class="mb-1">재고 : {{ item.itemQuantity }} 개</li>
                  <li class="mb-1">단가 : {{ item.itemPrice }} ₩</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination
              v-model="currentPage"
              :total-rows="rows"
              :per-page="perPage">
          </b-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.icon {
  width: 20px;
  height: 20px;
}

li {
  list-style: none;
}

.item-div {
  display: flex;
  flex-direction: column;
  width: 20%;
  height: 500px;
  border: 1px solid black;
  border-radius: 10px;
  margin: 20px;
  padding: 5px;
}

.pagination {
  justify-content: center; /* 가로 중앙 정렬 */
  margin-top: 20px;
}
</style>