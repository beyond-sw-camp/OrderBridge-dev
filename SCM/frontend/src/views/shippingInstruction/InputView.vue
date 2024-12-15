<script setup>

import InputForm from "@/components/common/InputForm.vue";
import InputItems from "@/components/common/inputItems.vue";
</script>

<template>
  <h4 class="title">영업관리 > 출하지시서 등록</h4>
  <div class="d-flex justify-content-center">
    <InputForm/>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div class="d-flex justify-content-center">
    <InputItems/>
  </div>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>