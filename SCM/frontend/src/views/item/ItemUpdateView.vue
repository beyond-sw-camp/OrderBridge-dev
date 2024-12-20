<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const itemSeq = route.params.id;

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
  previewImageUrl.value = itemImageUrl.value || 'https://via.placeholder.com/150'; // 기본 이미지
};

// 품목 상세 정보 가져오기
const fetchItemDetail = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/item/${itemSeq}`);
    console.log('상세 정보 응답:', response.data);

    const itemData = response.data.itemDTO;

    // 폼 데이터 설정
    itemName.value = itemData.itemName || '';
    itemDivision.value = itemData.itemDivision || '';
    itemExpiration.value = itemData.itemExpirationHour || 0;
    itemPrice.value = itemData.itemPrice || 0;
    itemUnitSeq.value = itemData.itemUnit?.itemUnitSeq || ''; // itemUnit이 없을 경우 처리
    itemImageUrl.value = itemData.itemImageUrl || '';
    warehouseSeq.value = itemData.warehouse?.warehouseSeq || ''; // warehouse가 없을 경우 처리
    itemNote.value = itemData.itemNote || '';
    updatePreviewImage();
  } catch (error) {
    console.error('품목 정보 가져오기 실패:', error);
    alert('품목 정보를 불러오는 데 실패했습니다. 서버 응답을 확인하세요.');
  }
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

// 품목 수정 처리
const updateItem = async () => {
  if (!itemName.value || !itemDivision.value || !itemExpiration.value || !itemPrice.value || !itemUnitSeq.value || !warehouseSeq.value || !itemImageUrl.value) {
    alert('필수 항목을 모두 입력해주세요.');
    return;
  }

  const payload = {
    itemSeq: itemSeq,
    userSeq: 1, // 임의 사용자 ID
    itemUnit: {
      itemUnitSeq: itemUnitSeq.value,
    },
    itemName: itemName.value,
    itemDivision: itemDivision.value,
    itemExpirationHour: itemExpiration.value,
    itemImageUrl: itemImageUrl.value,
    itemPrice: itemPrice.value,
    itemNote: itemNote.value,
    warehouse: {
      warehouseSeq: warehouseSeq.value,
    },
  };

  isLoading.value = true;
  try {
    const response = await axios.put(`http://localhost:8090/api/v1/item/${itemSeq}`, payload);
    alert('품목이 성공적으로 수정되었습니다.');
    router.push('/item');
  } catch (error) {
    console.error('품목 수정 실패:', error.response || error.message);
    alert('품목 수정에 실패했습니다. 요청 데이터를 확인하세요.');
  } finally {
    isLoading.value = false;
  }
};

// 초기 데이터 로드
onMounted(async () => {
  isLoading.value = true;
  try {
    await Promise.all([
      fetchItemDetail(),
      fetchItemDivisions(),
      fetchItemUnits(),
      fetchAllWarehouses()
    ]);
  } catch (error) {
    console.error('초기 데이터 로드 실패:', error);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div>
    <h4 class="title">품목관리 > 품목 수정</h4>
    <div class="d-flex justify-content-end mt-3">
      <b-button variant="light" size="sm" class="button ms-2" @click="router.push('/item')">목록</b-button>
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
          @click="updateItem"
          variant="light"
          size="sm"
          class="button ms-2">
        수정
      </b-button>
      <b-button
          :disabled="isLoading"
          @click="router.push('/item')"
          variant="light"
          size="sm"
          class="button ms-2">
        취소
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

.title {
  font-size: 1.8rem;
  font-weight: normal;
  margin-bottom: 30px;
  text-align: left;
  padding: 20px;
}

li {
  list-style: none;
}
</style>