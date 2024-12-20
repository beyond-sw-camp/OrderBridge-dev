<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {BButton} from "bootstrap-vue-3";

// 폼 데이터 관리
const itemName = ref('');
const itemDivision = ref('');
const itemDivisions = ref([]);
const itemExpiration = ref('');
const itemPrice = ref(0);
const itemUnitSeq = ref('');
const itemUnits = ref([]);
const itemNote = ref('');
const itemImageUrl = ref('');
const previewImageUrl = ref('');
const warehouses = ref([]);
const warehouseSeq = ref('');
const isLoading = ref(false);

// 이미지 미리보기 업데이트
const updatePreviewImage = () => {
  previewImageUrl.value = itemImageUrl.value || '';
};

// 품목 구분 데이터 가져오기
const fetchItemDivisions = async () => {
  try {
    const response = await axios.get('http://localhost:8090/api/v1/item/division');
    itemDivisions.value = response.data;
  } catch (error) {
    console.error('품목 구분 데이터 가져오기 실패:', error);
    alert('품목 구분 데이터를 불러오는 데 실패했습니다.');
  }
};

// 품목 단위 데이터 가져오기
const fetchItemUnits = async () => {
  try {
    const response = await axios.get('http://localhost:8090/api/v1/item/unit');
    itemUnits.value = response.data;
  } catch (error) {
    console.error('품목 단위 목록 가져오기 실패:', error);
    alert('품목 단위 목록을 불러오는 데 실패했습니다.');
  }
};

// 창고 데이터 가져오기
const fetchAllWarehouses = async () => {
  try {
    const response = await axios.get('http://localhost:8090/api/v1/warehouse/all');
    warehouses.value = response.data;
  } catch (error) {
    console.error('창고 목록 가져오기 실패:', error);
    alert('창고 목록을 불러오는 데 실패했습니다.');
  }
};

// 품목 등록 처리
const registerItems = async () => {
  if (!itemName.value || !itemDivision.value || !itemExpiration.value || !itemPrice.value || !itemUnitSeq.value || !warehouseSeq.value || !itemImageUrl.value) {
    alert('필수 항목을 모두 입력해주세요.');
    return;
  }

  const payload = {
    userSeq: 1,
    itemUnitSeq: itemUnitSeq.value,
    itemName: itemName.value,
    itemDivision: itemDivision.value,
    itemExpirationHour: itemExpiration.value,
    itemImageUrl: itemImageUrl.value,
    itemPrice: itemPrice.value,
    itemNote: itemNote.value,
    warehouseSeq: warehouseSeq.value,
  };

  isLoading.value = true;
  try {
    await axios.post('http://localhost:8090/api/v1/item', payload, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    alert('품목이 성공적으로 등록되었습니다.');
    resetForm();
  } catch (error) {
    console.error('품목 등록 실패:', error.response || error.message);
    alert('품목 등록에 실패했습니다.');
  } finally {
    isLoading.value = false;
  }
};

// 폼 초기화
const resetForm = () => {
  itemName.value = '';
  itemDivision.value = '';
  itemExpiration.value = '';
  itemPrice.value = 0;
  itemUnitSeq.value = '';
  itemNote.value = '';
  itemImageUrl.value = '';
  previewImageUrl.value = '';
  warehouseSeq.value = '';
};

// 초기 데이터 로드
onMounted(async () => {
  await Promise.all([fetchItemDivisions(), fetchItemUnits(), fetchAllWarehouses()]);
});
</script>

# Script 부분은 그대로 유지하고 template과 style만 변경합니다

# Script 부분은 그대로 유지

<template>
  <div>
    <h4 class="title">품목관리 > 품목 등록</h4>
    <div class="d-flex justify-content-end mt-3">
      <RouterLink to="/item">
        <b-button variant="light" size="sm" class="button">
          목록
        </b-button>
      </RouterLink>
    </div>
    <div class="d-flex justify-content-center mt-3">
      <div class="col-md-10 d-flex">
        <!-- 왼쪽 이미지 미리보기 -->
        <div class="col-md-4 pe-4">
          <div class="preview-image border border-secondary rounded d-flex justify-content-center align-items-center">
            <img v-if="previewImageUrl" :src="previewImageUrl" alt="이미지 미리보기" class="img-fluid"/>
            <span v-else>이미지를 추가하세요</span>
          </div>
        </div>

        <!-- 오른쪽 폼 필드들 -->
        <div class="col-md-8">
          <!-- 품목명 -->
          <b-form-group label-cols="3" label-size="default" label="품목명" label-for="itemName">
            <b-input-group size="sm">
              <b-form-input
                  type="text"
                  id="itemName"
                  v-model="itemName"
                  placeholder="품목명을 입력하세요">
              </b-form-input>
            </b-input-group>
          </b-form-group>

          <!-- 품목 구분 -->
          <b-form-group label-cols="3" label-size="default" label="품목 구분" label-for="itemDivision">
            <b-form-select
                size="sm"
                id="itemDivision"
                v-model="itemDivision">
              <option value="">선택하세요</option>
              <option v-for="division in itemDivisions"
                      :key="division.key"
                      :value="division.key">
                {{ division.value }}
              </option>
            </b-form-select>
          </b-form-group>

          <!-- 품목 유통기한 -->
          <b-form-group label-cols="3" label-size="default" label="품목 유통기한" label-for="itemExpiration">
            <b-input-group size="sm">
              <b-form-input
                  type="number"
                  id="itemExpiration"
                  v-model="itemExpiration"
                  placeholder="유통기한 입력">
              </b-form-input>
              <b-input-group-append>
                <b-input-group-text>시간</b-input-group-text>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>

          <!-- 품목 단가 -->
          <b-form-group label-cols="3" label-size="default" label="품목 단가" label-for="itemPrice">
            <b-input-group size="sm">
              <b-form-input
                  type="number"
                  id="itemPrice"
                  v-model="itemPrice"
                  placeholder="단가 입력">
              </b-form-input>
              <b-input-group-append>
                <b-input-group-text>₩</b-input-group-text>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>

          <!-- 품목 단위 -->
          <b-form-group label-cols="3" label-size="default" label="품목 단위" label-for="itemUnitSeq">
            <b-form-select
                size="sm"
                id="itemUnitSeq"
                v-model="itemUnitSeq">
              <option value="">선택하세요</option>
              <option v-for="unit in itemUnits"
                      :key="unit.itemUnitSeq"
                      :value="unit.itemUnitSeq">
                {{ unit.itemUnitTitle }}
              </option>
            </b-form-select>
          </b-form-group>

          <!-- 창고 -->
          <b-form-group label-cols="3" label-size="default" label="창고" label-for="warehouseSeq">
            <b-form-select
                size="sm"
                id="warehouseSeq"
                v-model="warehouseSeq">
              <option value="">선택하세요</option>
              <option v-for="warehouse in warehouses"
                      :key="warehouse.warehouseSeq"
                      :value="warehouse.warehouseSeq">
                {{ warehouse.warehouseName }}
              </option>
            </b-form-select>
          </b-form-group>

          <!-- 이미지 URL -->
          <b-form-group label-cols="3" label-size="default" label="이미지 URL" label-for="itemImageUrl">
            <b-input-group size="sm">
              <b-form-input
                  type="text"
                  id="itemImageUrl"
                  v-model="itemImageUrl"
                  @input="updatePreviewImage"
                  placeholder="이미지 URL을 입력하세요">
              </b-form-input>
            </b-input-group>
          </b-form-group>

          <!-- 품목 비고 -->
          <b-form-group label-cols="3" label-size="default" label="품목 비고" label-for="itemNote">
            <b-form-textarea
                size="sm"
                id="itemNote"
                v-model="itemNote"
                placeholder="비고를 입력하세요"
                rows="3">
            </b-form-textarea>
          </b-form-group>
        </div>
      </div>
    </div>

    <!-- 버튼 그룹 -->
    <div class="d-flex justify-content-center mt-3">
      <b-button
          :disabled="isLoading"
          @click="registerItems"
          variant="light"
          size="sm"
          class="button ms-2">
        등록
      </b-button>
      <b-button
          :disabled="isLoading"
          @click="resetForm"
          variant="light"
          size="sm"
          class="button ms-2">
        초기화
      </b-button>
    </div>
  </div>
</template>

<style scoped>
.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

div {
  font-size: small;
}

.preview-image {
  width: 100%;
  height: 300px;
  background-color: #f8f9fa;
}

.preview-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.preview-image span {
  color: #6c757d;
}

li {
  list-style: none;
}
</style>
