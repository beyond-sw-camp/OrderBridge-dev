<script setup>
import { ref } from 'vue';
import { useRouter } from "vue-router";
import axios from '@/axios'; // axios 인스턴스 사용

const router = useRouter();

const formData = ref({
  warehouseName: '',
  warehouseType: '',
  warehouseNote: '',
  userSeq: 1 // userSeq 고정값 설정
});

const warehouseTypeOptions = [
  { value: 'FACTORY', text: '생산' },
  { value: 'WAREHOUSE', text: '보관' }
];

const registerWarehouse = async () => {
  try {
    await axios.post('warehouse', formData.value); // 상대 경로 사용
    alert('창고가 등록되었습니다.');
    router.push('/warehouse'); // 창고 목록으로 이동
  } catch (error) {
    console.error('창고 등록 실패:', error);
    alert('창고 등록에 실패했습니다.');
  }
};
</script>
<template>
  <div class="container-fluid">
    <h4 class="title">창고관리 > 창고등록</h4>

    <div class="row justify-content-center">
      <div class="col-6 d-flex flex-column">
        <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label="창고명"
            label-for="warehouseName">
          <b-form-input
              class="w-75"
              size="sm"
              id="warehouseName"
              v-model="formData.warehouseName"
              placeholder="창고명을 입력해 주세요."
          />
        </b-form-group>

        <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label="창고 구분"
            label-for="warehouseType">
          <b-form-select
              class="w-75"
              size="sm"
              id="warehouseType"
              v-model="formData.warehouseType"
              :options="warehouseTypeOptions"
              placeholder="창고 구분을 선택해 주세요."
          />
        </b-form-group>

        <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label="창고 비고"
            label-for="warehouseNote">
          <b-form-input
              type="text"
              size="sm"
              id="warehouseNote"
              v-model="formData.warehouseNote"
              placeholder="비고를 입력해 주세요."
          />
        </b-form-group>

        <div class="d-flex justify-content-center mt-4">
          <b-button
              variant="light"
              class="me-2 button"
              @click="$router.push('/warehouse')">
            취소
          </b-button>
          <b-button
              variant="light"
              class="button"
              @click="registerWarehouse">
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

.form-group {
  margin-bottom: 1rem;
}

label {
  font-weight: bold;
}
</style>
```