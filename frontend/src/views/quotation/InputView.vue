<script setup>

import InputForm from "@/components/common/InputForm.vue";
import InputItems from "@/components/common/inputItems.vue";
</script>

<template>
  <div class="main">
    <h1>입력폼 초안 페이지</h1>
    <div class="input-form-div">
      <InputForm />
    </div>
    <div>
      <InputItems />
    </div>
  </div>
</template>

<style scoped>
.input-form-div {
  display: flex;
  max-width: 600px;
}
</style>