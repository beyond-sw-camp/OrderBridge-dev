<script setup>
import {ref, onMounted, watch} from 'vue';
import axios from 'axios';
import {BButton, BPagination} from "bootstrap-vue-3";
import plusIcon from "@/assets/plus.svg";
import router from "@/router/index.js";
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
  if (!itemName.value) {
    alert('품목명을 입력해주세요.');
    return;
  } else if(!itemDivision.value) {
    alert('품목 구분을 선택해주세요.');
    return;
  } else if(!itemExpiration.value) {
    alert('품목 유통기한을 입력해주세요.')
    return;
  } else if(!itemPrice.value) {
    alert('품목 단가를 입력해주세요.')
    return;
  } else if(!itemUnitSeq.value) {
    alert('품목 단위를 선택해주세요.')
    return;
  } else if(!warehouseSeq.value) {
    alert('보관 창고를 입력해주세요.')
    return;
  }

  for(const bomItem of bomItems.value) {
    console.log(bomItem.bomChildItemQuantity);
    if(!bomItem.bomChildItemQuantity > 0) {
      alert(`${bomItem.itemName} 품목의 수량을 입력해주세요.`);
      return;
    }
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

    bomItemList: bomItems.value
  };

  isLoading.value = true;
  try {
    await axios.post('http://localhost:8090/api/v1/item', payload, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    alert('품목이 성공적으로 등록되었습니다.');
    await router.push("/item");
  } catch (error) {
    console.error('품목 등록 실패:', error.response || error.message);
    alert('품목 등록에 실패했습니다.');
  } finally {
    isLoading.value = false;
  }
};

// 초기 데이터 로드
onMounted(() => {
  fetchItemDivisions();
  fetchItemUnits();
  fetchAllWarehouses();
  fetchItems();
});

const items = ref([]);
const itemTotalCount = ref(0);
const itemPageSize = ref(8);
const itemPageNumber = ref(1);

watch(itemPageNumber, () => {
  fetchItems();
});

const fetchItems = async () => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/item`, {
      params: {
        page: itemPageNumber.value,
        size: itemPageSize.value,
        itemDivisions: ["PART", "SUB"]
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });
    console.log(response.data)
    items.value = response.data.content;
    itemTotalCount.value = response.data.totalElements;
  } catch (error) {
    console.log(`품목 목록 조회 실패`, error);
  }
}

function openModal() {
  const modal = document.getElementById('bomItemModal');
  modal.removeAttribute('aria-hidden'); // 모달 활성화 시 aria-hidden 제거
  modal.style.display = 'block';
  modal.focus(); // 포커스를 모달로 이동
}

function closeModal() {
  const modal = document.getElementById('bomItemModal');
  modal.setAttribute('aria-hidden', 'true'); // 모달 비활성화 시 aria-hidden 추가
  modal.style.display = 'none';
}

const itemDivisionMap = {
  FINISHED: "완제품",
  PART: "부재료",
  SUB: "원재료",
};

// 선택된 아이템 저장
const selectedItems = ref([]);
const bomItems = ref([]);

// 선택 상태 토글
const toggleSelection = (itemSeq) => {
  if (selectedItems.value.includes(itemSeq)) {
    selectedItems.value = selectedItems.value.filter((seq) => seq !== itemSeq);
    bomItems.value = items.value.filter((item) => selectedItems.value.includes(item.itemSeq));
  } else {
    selectedItems.value.push(itemSeq);
    // 선택된 itemSeq와 일치하는 항목을 items에서 찾기
    const matchedItem = items.value.find((item) => item.itemSeq === itemSeq);

    bomItems.value.push(matchedItem);
  }
};

</script>

<template>
  <div>
    <h4 class="title">품목관리 > 품목 등록</h4>
    <div class="d-flex justify-content-end mt-3">
      <b-button @click="router.push('/item')" variant="light" size="sm" class="button">목록</b-button>
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
              <b-form-input type="text" id="itemName" v-model="itemName" placeholder="품목명을 입력하세요"/>
            </b-input-group>
          </b-form-group>
          <!-- 품목 구분 -->
          <b-form-group label-cols="3" label-size="default" label="품목 구분" label-for="itemDivision">
            <b-form-select size="sm" id="itemDivision" v-model="itemDivision">
              <option value="">선택하세요</option>
              <option v-for="division in itemDivisions" :key="division.key" :value="division.key">{{ division.value }} </option>
            </b-form-select>
          </b-form-group>
          <!-- 품목 유통기한 -->
          <b-form-group label-cols="3" label-size="default" label="품목 유통기한" label-for="itemExpiration">
            <b-input-group size="sm">
              <b-form-input type="number" id="itemExpiration" v-model="itemExpiration" placeholder="유통기한 입력"/>
              <b-input-group-append>
                <b-input-group-text>시간</b-input-group-text>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
          <!-- 품목 단가 -->
          <b-form-group label-cols="3" label-size="default" label="품목 단가" label-for="itemPrice">
            <b-input-group size="sm">
              <b-form-input type="number" id="itemPrice" v-model="itemPrice" placeholder="단가 입력"/>
              <b-input-group-append>
                <b-input-group-text>₩</b-input-group-text>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
          <!-- 품목 단위 -->
          <b-form-group label-cols="3" label-size="default" label="품목 단위" label-for="itemUnitSeq">
            <b-form-select size="sm" id="itemUnitSeq" v-model="itemUnitSeq">
              <option value="">선택하세요</option>
              <option v-for="unit in itemUnits" :key="unit.itemUnitSeq" :value="unit.itemUnitSeq">{{ unit.itemUnitTitle }}</option>
            </b-form-select>
          </b-form-group>
          <!-- 창고 -->
          <b-form-group label-cols="3" label-size="default" label="창고" label-for="warehouseSeq">
            <b-form-select size="sm" id="warehouseSeq" v-model="warehouseSeq">
              <option value="">선택하세요</option>
              <option v-for="warehouse in warehouses" :key="warehouse.warehouseSeq" :value="warehouse.warehouseSeq">{{ warehouse.warehouseName }}</option>
            </b-form-select>
          </b-form-group>
          <!-- 이미지 URL -->
          <b-form-group label-cols="3" label-size="default" label="이미지 URL" label-for="itemImageUrl">
            <b-input-group size="sm">
              <b-form-input type="text" id="itemImageUrl" v-model="itemImageUrl" @input="updatePreviewImage" placeholder="이미지 URL을 입력하세요"/>
            </b-input-group>
          </b-form-group>
          <!-- 품목 비고 -->
          <b-form-group label-cols="3" label-size="default" label="품목 비고" label-for="itemNote">
            <b-form-textarea size="sm" id="itemNote" v-model="itemNote" placeholder="비고를 입력하세요" rows="3"/>
          </b-form-group>
        </div>
      </div>
    </div>
    <div class="px-4 d-flex flex-column align-items-center">
      <hr class="col-md-10 d-flex flex-column">
    </div>
    <!-- BOM 품목 등록 -->
    <div class="d-flex justify-content-center mt-4">
      <div class="col-md-9 ms-3"> <!-- ms-3로 들여쓰기 -->
        <h5 class="title">BOM 등록</h5>
        <div class="table-responsive">
          <table class="table table-bordered">
            <thead>
              <tr v-if="bomItems.length > 0">
                <th class="text-center header-image">품목 이미지</th>
                <th class="text-center header-text">품목명</th>
                <th class="text-center header-text">품목 구분</th>
                <th class="text-center header-text">유통기한</th>
                <th class="text-center header-text">품목 단가</th>
                <th class="text-center header-text small-input">수량</th>
                <th class="text-center header-text">단위</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(bomItem, index) in bomItems" :key="index">
                <td><div class="item-image"><b-img :src="bomItem.itemImageUrl"/></div></td>
                <td class="text-center">{{ bomItem.itemName }}</td>
                <td class="text-center">{{ itemDivisionMap[bomItem.itemDivision] }}</td>
                <td class="text-center">{{ bomItem.itemExpirationHour }} 시간</td>
                <td class="text-center">{{ bomItem.itemPrice.toLocaleString() }} ₩</td>
                <td style="white-space: nowrap;">
                  <b-form-input class="small-input" size="sm" type="number" v-model="bomItem.bomChildItemQuantity" placeholder="수량 입력"/>
                </td>
                <td class="text-center">{{ bomItem.itemUnit}}</td>
              </tr>
              <tr>
                <td colspan="7" class="text-center" @click="openModal" data-bs-toggle="modal" data-bs-target="#bomItemModal">
                  <plusIcon class="icon"/>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 버튼 그룹 -->
    <div class="d-flex justify-content-end mt-3">
      <b-button :disabled="isLoading" @click="registerItems" variant="light" size="sm" class="button ms-2">등록</b-button>
    </div>
  </div>

  <!-- bomItemModal -->
  <div class="modal fade" id="bomItemModal" tabindex="-1" aria-labelledby="bomItemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5">BOM 품목 선택</h1>
          <div class="ms-5">검색결과: {{ itemTotalCount }}개</div>
          <button type="button" @click="closeModal" class="button btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 600px; overflow-y: auto;">
            <div  v-if="items.length > 0" class="col-md-12 container-fluid">
              <div class="row g-3">
                <div  v-for="item in items" :key="item.itemSeq"  class="col-12 col-md-6 col-lg-3">
                  <div class="item-container">
                    <div class="item-card" :class="{ selected: selectedItems.includes(item.itemSeq) }" @click="toggleSelection(item.itemSeq)">
                      <div class="item-image">
                        <img :src="item.itemImageUrl" alt="품목 이미지"/>
                      </div>
                      <div class="item-content">
                        <p class="item-name">{{ item.itemName }}</p>
                        <ul>
                          <li>품목: {{ itemDivisionMap[item.itemDivision] || item.itemDivision }}</li>
                          <li>유통기한: {{ (item.itemExpirationHour / 24).toFixed(0) }} 일</li>
                          <li>단가: {{ item.itemPrice.toLocaleString() }} ₩</li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-footer pagination">
                <b-pagination v-model="itemPageNumber" :total-rows="itemTotalCount" :per-page="itemPageSize"></b-pagination>
              </div>
            </div>
            <template v-else>
              <b-card-text class="no-list-text">해당 검색조건에 부합한 품목이 존재하지 않습니다.</b-card-text>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container-fluid {
  overflow-x: hidden; /* 가로 스크롤 제거 */
}

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

.icon {
  margin-right: 8px; /* 아이콘과 텍스트 간 간격 */
  width: 30px;
  height: 30px;
  fill: #333; /* 아이콘 색상 */
}

.item-card {
  cursor: pointer;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  padding: 10px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  background-color: #fff;
  height: 100%;
}

.item-card.selected {
  border: 2px solid #007bff; /* 선택된 카드 테두리 */
  background-color: #e9f5ff; /* 선택된 배경 강조 */
  transform: scale(1.00); /* 선택된 카드 확대 */
  box-shadow: 0 5px 15px rgba(0, 123, 255, 0.3); /* 선택된 카드 그림자 */
}

.item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.item-content {
  margin-top: 10px;
}

.item-name {
  font-weight: bold;
  margin-bottom: 5px;
  color: #343a40;
}

.item-image img {
  max-width: 100%;
  max-height: 100px;
  object-fit: cover; /* 이미지 크기와 컨테이너 비율 유지 */
}

.pagination {
  display: flex;
  justify-content: center;
}

/* thead 크기를 조정 */
thead th.header-image {
  width: 150px; /* 이미지 열 크기 고정 */
  text-align: center;
}

thead th.header-text {
  vertical-align: middle; /* 텍스트 세로 정렬 */
}

tbody td {
  vertical-align: middle; /* tbody의 텍스트와 이미지 세로 정렬 */
}

.small-input {
  width: 100px; /* 너비를 줄임 */
  height: 30px; /* 높이를 줄임 */
  font-size: 12px; /* 글꼴 크기 줄임 */
  padding: 2px 5px; /* 내부 여백 최소화 */
  text-align: center; /* 텍스트 가운데 정렬 */
}

</style>
