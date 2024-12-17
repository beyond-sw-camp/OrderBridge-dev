<script setup>
import {defineProps, ref, watch} from "vue";

const props = defineProps({
  itemList: {type: Array, required: true},       // 주문서 품목 리스트
  selectedSalesOrder: {type: Boolean },       // 주문서 선택 여부
});

const emit = defineEmits(['registerEvent']);

// itemList의 각 아이템에 대해 itemData 초기화
const itemData = ref(props.itemList.map(item => ({
  originalQuantity: Number(item.salesOrderItemQuantity) || 0,
  Seq: Number(item.itemSeq) || 0,
  quantity: Number(item.salesOrderItemQuantity) || 0,
  note: item.salesOrderItemNote || '',
})));

// itemList 변경 시 itemData 업데이트
watch(() => props.itemList, (newItemList) => {
  // itemData 배열을 새 itemList에 맞게 재설정
  itemData.value = newItemList.map(item => ({
    originalQuantity: Number(item.salesOrderItemQuantity) || 0,
    Seq: Number(item.itemSeq) || 0,
    quantity: Number(item.salesOrderItemQuantity) || 0,
    note: item.salesOrderItemNote || '',
  }));
});

// // 가격과 수량을 변경할 때마다 총금액 계산하기 위한 watch
// watch(itemData, (newItemData) => {
//   newItemData.forEach((data, index) => {
//     // 각 itemData에 대해 total 계산
//     data.total = data.quantity * data.price;
//   });
// }, { deep: true });  // 자동 total 갱신

const addShippingInstruction = () => {
  emit('registerEvent', itemData);
}

// 품목 포맷 함수
const formatDivision = (divisionString) => {
  if (divisionString === 'FINISHED') {
    return '생산완료';
  }else if(divisionString === 'RAW'){
    return '구성품';
  }else if(divisionString === 'PART'){
    return '부재료';
  }else if(divisionString === 'SUB'){
    return '원재료';
  }
  return divisionString; // 품목가 다른 경우 그대로 반환
}
</script>

<template>
  <template v-if="props.selectedSalesOrder">
    <div class="col-md-10 d-flex flex-column">
      <h5 class="px-4">물품 등록</h5>
      <div style="max-height: 250px; overflow-y: auto;">
        <div style="max-height: 200px;" v-for="(item, index) in itemList" :key="item.salesOrderItemSeq"
             class="mx-5 my-3 d-flex flex-row border border-secondary rounded">
          <b-img class="p-2 col-md-4" src="https://picsum.photos/200/200" fluid alt="Responsive image"></b-img>
          <div class="p-2 col-md-8">
            <div class="mb-4 d-flex justify-content-between">
              <span class="fw-bold">{{ item.itemName }}</span>
            </div>
            <div class="d-flex">
              <ul class="col-md-4">
                <li class="mb-3">
                  · 품목 구분 : {{ formatDivision(item.itemDivision) }}
                </li>
                <li class="mb-3 d-flex flex-row">
                  · 수량 :
                  <b-form-input class="ms-2 me-2 w-50" type="text" v-model="itemData[index].quantity" size="sm"></b-form-input>
                  개
                </li>
              </ul>
              <ul class="col-md-8 d-flex flex-column justify-content-between">
                <li class="d-flex flex-row">
                  · 비고 :
                  <b-form-input class="ms-2 w-75" type="text" v-model="itemData[index].note" size="sm"></b-form-input>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="mx-5 my-3 d-flex justify-content-end">
        <b-button class="button mx-3" variant="light" size="sm" @click="addShippingInstruction">확인</b-button>
      </div>
    </div>
  </template>
</template>

<style scoped>
li {
  list-style: none;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}
</style>