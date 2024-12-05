<script setup>

import InputForm from "@/components/common/InputForm.vue";
import InputItems from "@/components/common/inputItems.vue";
</script>

<template>
  <h4 class="title">영업관리 > 견적서 등록</h4>
  <div class="d-flex justify-content-center">
    <InputForm />
  </div>
  <div class="px-4 d-flex flex-column justify-content-center">
    <hr>
  </div>
  <div>
    <InputItems />
  </div>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>