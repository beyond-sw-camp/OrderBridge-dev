<script setup>
import {ref} from "vue";

const quoteDate = ref("");
const client = ref("");
const manager = ref("");
const note = ref("");

</script>

<template>
  <div>
    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="견적일자" label-for="quote-date">
      <b-form-input
          type="date"
          id="quote-date"
          v-model="quoteDate"
          placeholder="견적일자를 입력해 주세요.">
      </b-form-input>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처" label-for="client">
      <b-form-input
          type="text"
          id="client"
          v-model="client"
          placeholder="거래처를 입력해 주세요.">
      </b-form-input>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="담당자" label-for="manager">
      <b-form-input
          type="text"
          id="manager"
          v-model="manager"
          placeholder="담당자를 입력해 주세요.">
      </b-form-input>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="비고" label-for="note">
      <b-form-input
          type="text"
          id="note"
          v-model="note"
          placeholder="내용을 입력해 주세요.">
      </b-form-input>
    </b-form-group>
  </div>


</template>

<style scoped>
div {
  width: 100%;
}
</style>