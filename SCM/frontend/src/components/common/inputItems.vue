<script setup>
import {ref} from "vue";
import trashIcon from '@/assets/trashIcon.svg'

const rows = ref(123);
const perPage = ref(10);
const currentPage = ref(1);
const items = ref([
  {
    itemName: '김치볶음밥',
    itemDivision: '제품',
    itemQuantity: '100',
    itemPrice: '3,000',
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
  {
    itemName: '치즈볶음밥',
    itemDivision: '제품',
    itemQuantity: '100',
    itemPrice: '3,000',
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
  {
    itemName: '김치볶음밥',
    itemDivision: '제품',
    itemQuantity: '100',
    itemPrice: '3,000',
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
  {
    itemName: '김치볶음밥',
    itemDivision: '제품',
    itemQuantity: '100',
    itemPrice: '3,000',
    itemNote: '김치와 밥의 조합',
    totalQuantity: '300,000',
  },
]);

const additionalItems = ref(false);
const addItem = () => {
  additionalItems.value = true;
}
const deleteItem = () => {
  additionalItems.value = false;
}
</script>

<template>
  <h5 class="px-4">물품 등록</h5>
  <div style="max-height: 250px; overflow-y: auto;" >
    <div class="mx-5 my-3 d-flex flex-row border border-secondary rounded" v-if="additionalItems">
      <!--    모달에서 물품 선택시 내용을 가져오도록 변경 필요 -->
      <b-img class="p-2 col-md-2" src="https://picsum.photos/200/200" fluid alt="Responsive image"></b-img>
      <div class="p-2 col-md-10">
        <div class="mb-4 d-flex justify-content-between">
          <span class="fw-bold" >{{ items[0].itemName }}</span>
          <trashIcon class="icon" @click="deleteItem"/>
        </div>
        <ul>
          <li class="mb-3 me-5 li-row">품목 : {{ items[0].itemDivision }}</li>
          <li class="mb-3 ms-5">비고 : {{ items[0].itemNote }}</li>
          <li class="mb-3">수량 : {{ items[0].itemQuantity }} 개</li>
          <li class="mb-3">단가 : {{ items[0].itemPrice }} ₩</li>
        </ul>
        <p class="fw-bold float-end">총금액 {{ items[0].totalQuantity }} ₩</p>
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

  <!-- item Modal bootstrap -->
  <div class="modal fade" id="itemModal" tabindex="-1" aria-labelledby="itemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="d-flex justify-content-center">
            <div class="item-div" v-for="item in items" data-bs-dismiss="modal" @click="addItem" >
              <p class="fw-bold">{{ item.itemName }}</p>
              <b-img class="mb-4" src="https://picsum.photos/200/200" fluid alt="Responsive image"></b-img>
              <ul>
                <li class="mb-3">품목 : {{ item.itemDivision }}</li>
                <li class="mb-3">수량 : {{ item.itemQuantity }} 개</li>
                <li class="mb-3">단가 : {{ item.itemPrice }} ₩</li>
                <li class="mb-3">총금액 {{ items[0].totalQuantity }} ₩</li>
                <li class="mb-3">비고 : {{ item.itemNote }}</li>
              </ul>
            </div>
          </div>
        </div>
        <div class="modal-footer justify-content-center mt-1">
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

.li-row {
  float: left;
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
</style>