<script setup>
  import { onMounted, ref, watch } from 'vue';
  import axios from 'axios';
  import { BInputGroup, BFormInput, BFormCheckbox, BButton, BInputGroupText, BPagination } from 'bootstrap-vue-3';

  const items = ref([]);
  const selectedDivisions = ref([]);
  const searchName = ref("");
  const rows = ref(0);
  const perPage = ref(8);
  const currentPage = ref(1);

  // 유통기한 필터용 상태 (일 단위)
  const minExpiration = ref(null);
  const maxExpiration = ref(null);

  const itemDivisionList = [
  { key: 'FINISHED', value: '완제품' },
  { key: 'PART', value: '부재료' },
  { key: 'SUB', value: '원재료' },
  ];

  const itemDivisionMap = {
  FINISHED: "완제품",
  PART: "부재료",
  SUB: "원재료",
};

  const findItemsByFilter = async () => {
  console.log("현재 검색어:", searchName.value);
  console.log("현재 선택된 품목 구분:", selectedDivisions.value);
  console.log("현재 페이지:", currentPage.value);
  console.log("유통기한 범위:", minExpiration.value, "~", maxExpiration.value);

  if (currentPage.value < 1) currentPage.value = 1;

  const minHour = (minExpiration.value != null && minExpiration.value >= 0) ? minExpiration.value * 24 : null;
  const maxHour = (maxExpiration.value != null && maxExpiration.value >= 0) ? maxExpiration.value * 24 : null;

  try {
  const response = await axios.get("http://localhost:8090/api/v1/item", {
  params: {
  page: currentPage.value,
  size: perPage.value,
  itemName: searchName.value || null,
  itemDivisions: selectedDivisions.value.length > 0 ? selectedDivisions.value : null,
  minExpirationHour: minHour,
  maxExpirationHour: maxHour,
},
  paramsSerializer: (params) => {
  const searchParams = new URLSearchParams();
  for (const key in params) {
  const value = params[key];
  if (Array.isArray(value)) {
  value.forEach(v => {
  if (v !== null && v !== undefined) {
  searchParams.append(key, v);
}
});
} else if (value !== null && value !== undefined) {
  searchParams.append(key, value);
}
}
  return searchParams.toString();
}
});

  console.log("서버 응답 데이터:", response.data);

  rows.value = response.data.totalElements;
  items.value = response.data.content;
} catch (error) {
  console.error("데이터 불러오기 실패:", error);
}
};

  // 검색어 변경 시 자동 검색
  watch(searchName, (newValue) => {
  console.log("검색어 변경됨:", newValue);
  findItemsByFilter();
});

  // 페이지 변경 시 자동 검색
  watch(currentPage, () => {
  findItemsByFilter();
});

  // 유통기한 변경 시 자동 검색
  watch([minExpiration, maxExpiration], () => {
  findItemsByFilter();
});

  onMounted(() => {
  findItemsByFilter();
});

  function checkItemDivision(key) {
  const index = selectedDivisions.value.indexOf(key);
  if (index > -1) {
  selectedDivisions.value.splice(index, 1);
} else {
  selectedDivisions.value.push(key);
}
  findItemsByFilter();
}
</script>


<template>
  <h4 class="title">품목관리 > 품목 조회</h4>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">품목명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchName" placeholder="품목명을 입력하세요"></b-form-input>
            <b-input-group-text @click="findItemsByFilter" style="cursor: pointer;">
              <svg width="1em" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
                <path d="M344.5,298c15-23.6,23.8-51.6,23.8-81.7c0-84.1-68.1-152.3-152.1-152.3C132.1,64,64,132.2,64,216.3
                  c0,84.1,68.1,152.3,152.1,152.3c30.5,0,58.9-9,82.7-24.4l6.9-4.8L414.3,448l33.7-34.3L339.5,305.1L344.5,298z
                  M301.4,131.2  c22.7,22.7,35.2,52.9,35.2,85c0,32.1-12.5,62.3-35.2,85c-22.7,22.7-52.9,35.2-85,35.2c-32.1,0-62.3-12.5-85-35.2
                  c-22.7-22.7-35.2-52.9-35.2-85c0-32.1,12.5-62.3,35.2-85c22.7-22.7,52.9-35.2,85-35.2C248.5,96,278.7,108.5,301.4,131.2z" />
              </svg>
            </b-input-group-text>
          </b-input-group>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">품목 구분</p>
          <template v-for="itemDivision in itemDivisionList" :key="itemDivision.key">
            <b-form-checkbox @click="checkItemDivision(itemDivision.key)">
              {{ itemDivision.value }}
            </b-form-checkbox>
          </template>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">품목 유통기한</p>
          <div class="d-flex align-items-center">
            <div class="input-group" style="width: 100px;">
              <!-- 유통기한 최소값 입력 -->
              <input type="number" class="form-control text-center" placeholder=""
                     v-model.number="minExpiration" />
              <span class="input-group-text">일</span>
            </div>
            <span class="mx-2">~</span>
            <div class="input-group" style="width: 100px;">
              <!-- 유통기한 최대값 입력 -->
              <input type="number" class="form-control text-center" placeholder=""
                     v-model.number="maxExpiration" />
              <span class="input-group-text">일</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 리스트 출력 부분 -->
    <div class="col-md-9">
      <div style="width: 90%;">
        <div class="d-flex justify-content-between">
          <div class="d-flex justify-content-between">
            <div>검색결과: {{ rows }}개</div>
          </div>
          <b-button variant="light" size="sm" class="button">품목 등록</b-button>
        </div>
        <div class="list-headline row"></div>
        <div style="max-height: 600px; overflow-y: auto;">
          <div class="item-container">
            <div v-for="item in items" :key="item.id" class="item-card">
              <div class="item-image">
                <img :src="item.itemImageUrl" alt="품목 이미지" />
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
      <div class="pagenation">
        <b-pagination
            v-model="currentPage"
            :total-rows="rows"
            :per-page="perPage"
            @change="findItemsByFilter"
        ></b-pagination>
      </div>
    </div>
  </div>
</template>

<style scoped>
.item-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-between;
}

.item-card {
  flex: 1 1 calc(25% - 20px);
  max-width: calc(25% - 20px);
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  text-align: center;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 120px;
  object-fit: cover; /* 이미지 비율 유지 */
}

.item-content {
  padding: 10px;
}

.item-name {
  font-size: 1.1em;
  font-weight: bold;
  margin-bottom: 8px;
}

.item-content ul {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 0.9em;
  text-align: left;
}

.item-content li {
  margin: 5px 0;
}

div {
  font-size: small;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.side-box {
  min-height: 100px;
  margin-top: 20px;
}

.card-title {
  font-size: medium;
  font-weight: bold;
}

.list-headline {
  border-bottom: 1px solid black;
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
}

.pagenation {
  justify-items: center;
  margin-top: 20px;
}
</style>
