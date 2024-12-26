<script setup>

import ItemInputForm from "@/components/item/ItemInputForm.vue";
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import axios from "@/axios.js";

const route = useRoute();

const itemDTO = ref();
const childItemList = ref([]);

const fetchItem = async (itemSeq) => {
  try {
    const response = await axios.get(`item/${itemSeq}`, {});
    console.log(response.data);

    itemDTO.value = response.data.itemDTO;
    childItemList.value = response.data.childItemList;

  } catch (error) {
    console.error("품목 상세 불러오기 실패 :", error);
  }
};

onMounted(() => {
  fetchItem(route.params.itemSeq);
})
</script>

<template>
  <h4 class="title">품목관리 > 품목 수정</h4>
  <ItemInputForm :itemDTO="itemDTO"
                 :childItemList="childItemList"/>
</template>

<style scoped>

</style>