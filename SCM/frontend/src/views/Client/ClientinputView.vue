<script setup>
import { ref } from 'vue';
import {createRouter as $router, useRouter} from "vue-router";
import axios from 'axios';
import {BButton} from "bootstrap-vue-3";

const router = useRouter();
const formData = ref({
  clientName: '',
  clientRegistrationNo: '',
  clientRepresentative: '',
  clientPhoneNo: '',
  clientEmail: '',
  userSeq: 1
});

const registerClient = async () => {
  if (!formData.value.clientName.trim()) {
    alert('거래처명을 입력해주세요.');
    return;
  }
  if (!formData.value.clientRegistrationNo.trim()) {
    alert('사업자등록번호를 입력해주세요.');
    return;
  }

  try {
    await axios.post('http://localhost:8090/api/v1/client', formData.value);
    alert('거래처가 등록되었습니다.');
    router.push('/client');
  } catch (error) {
    console.error('거래처 등록 실패:', error);
    alert('거래처 등록에 실패했습니다.');
  }
};
</script>

<template>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/client')" variant="light" size="sm" class="button">목록</b-button>
  </div>
  <div class="container-fluid">
    <h4 class="title">거래처관리 > 거래처등록</h4>

    <div class="row justify-content-center">
      <div class="col-6 d-flex flex-column">
        <b-form-group label-cols="4" label-cols-lg="2" label="거래처명" label-for="clientName">
          <b-form-input
              class="w-75"
              size="sm"
              id="clientName"
              v-model="formData.clientName"
              placeholder="거래처명을 입력해 주세요."
              required
          />
        </b-form-group>

        <b-form-group label-cols="4" label-cols-lg="2" label="사업자등록번호" label-for="clientRegistrationNo">
          <b-form-input
              class="w-75"
              size="sm"
              id="clientRegistrationNo"
              v-model="formData.clientRegistrationNo"
              placeholder="사업자등록번호를 입력해 주세요. (-) "
              required
          />
        </b-form-group>

        <b-form-group label-cols="4" label-cols-lg="2" label="대표자" label-for="clientRepresentative">
          <b-form-input
              class="w-75"
              size="sm"
              id="clientRepresentative"
              v-model="formData.clientRepresentative"
              placeholder="대표자명을 입력해 주세요."
          />
        </b-form-group>

        <b-form-group label-cols="4" label-cols-lg="2" label="연락처" label-for="clientPhoneNo">
          <b-form-input
              class="w-75"
              size="sm"
              id="clientPhoneNo"
              v-model="formData.clientPhoneNo"
              placeholder="연락처를 입력해 주세요. (-) "
          />
        </b-form-group>

        <b-form-group label-cols="4" label-cols-lg="2" label="이메일" label-for="clientEmail">
          <b-form-input
              class="w-75"
              size="sm"
              id="clientEmail"
              v-model="formData.clientEmail"
              placeholder="이메일을 입력해 주세요."
          />
        </b-form-group>

        <div class="d-flex justify-content-center mt-4">
          <b-button variant="light" class="me-2 button" @click="$router.push('/client')">
            취소
          </b-button>
          <b-button variant="light" class="button" @click="registerClient">
            등록
          </b-button>
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

label {
  font-weight: bold;
}
</style>