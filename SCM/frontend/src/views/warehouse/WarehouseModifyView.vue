<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from "vue-router";
import axios from '@/axios'; // axios 인스턴스 사용

const router = useRouter();
const route = useRoute();
const warehouseSeq = route.params.id; // URL에서 창고 ID 가져오기

const formData = ref({
  warehouseName: '',
  warehouseType: '',
  warehouseNote: '',
  userSeq: 1 // 사용자 ID, 필요하다면 동적으로 설정
});

const warehouseTypeOptions = [
  { value: 'FACTORY', text: '생산' },
  { value: 'WAREHOUSE', text: '보관' }
];

// 기존 데이터 불러오기
const fetchWarehouseData = async () => {
  try {
    const response = await axios.get(`warehouse/${warehouseSeq}`); // 상대 경로 사용
    formData.value = {
      warehouseName: response.data.warehouseName,
      warehouseType: response.data.warehouseType,
      warehouseNote: response.data.warehouseNote || '',
      userSeq: response.data.userSeq // 사용자 ID 값이 있는 경우 설정
    };
  } catch (error) {
    console.error('창고 정보 조회 실패:', error);
    alert('창고 정보를 불러오는데 실패했습니다.');
    router.push('/warehouse'); // 실패 시 창고 목록으로 이동
  }
};

const updateWarehouse = async () => {
  if (!formData.value.warehouseName.trim()) {
    alert('창고명을 입력해주세요.');
    return;
  }

  try {
    await axios.put(`warehouse/${warehouseSeq}`, formData.value); // 상대 경로 사용
    alert('창고 정보가 수정되었습니다.');
    router.push('/warehouse'); // 수정 후 창고 목록으로 이동
  } catch (error) {
    console.error('창고 수정 실패:', error);
    alert('창고 수정에 실패했습니다.');
  }
};

onMounted(() => {
  fetchWarehouseData();
});
</script>

<template>
  <div class="container-fluid">
    <h4 class="title">창고관리 > 창고수정</h4>

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
              @click="updateWarehouse">
            수정
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