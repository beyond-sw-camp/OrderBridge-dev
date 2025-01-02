<script setup>
import { onMounted, ref, watch } from 'vue';
import axios from '@/axios'; // axios 인스턴스 사용
import { BInputGroup, BFormInput, BFormCheckbox, BButton, BInputGroupText, BPagination } from 'bootstrap-vue-3';
import { useRouter } from "vue-router";
import dayjs from "dayjs";
import editIcon from "@/assets/editIcon.svg";
import trashIcon from "@/assets/trashIcon.svg";

const router = useRouter();
const items = ref([]);
const selectedDivisions = ref([]);
const searchName = ref("");
const rows = ref(0);
const perPage = ref(4);
const currentPage = ref(1);
const loadingImages = ref(new Set());

const minExpiration = ref(null);
const maxExpiration = ref(null);

const itemDivisionMap = {
  FINISHED: "완제품",
  PART: "부재료",
  SUB: "원재료",
};

const itemDivisionList = [
  { key: 'FINISHED', value: '완제품' },
  { key: 'PART', value: '부재료' },
  { key: 'SUB', value: '원재료' },
];

const findItemsByFilter = async () => {
  itemDTO.value = null;
  try {
    const minHour = minExpiration.value != null ? minExpiration.value * 24 : null;
    const maxHour = maxExpiration.value != null ? maxExpiration.value * 24 : null;

    const response = await axios.get("item", {
      params: {
        page: currentPage.value,
        size: perPage.value,
        itemName: searchName.value || null,
        itemDivisions: selectedDivisions.value.length > 0 ? selectedDivisions.value : null,
        minExpirationHour: minHour,
        maxExpirationHour: maxHour,
        sort: "itemSeq,desc"
      },
      paramsSerializer: (params) => {
        const searchParams = new URLSearchParams();
        for (const key in params) {
          const value = params[key];
          if (Array.isArray(value)) {
            value.forEach((v) => {
              if (v != null) searchParams.append(key, v);
            });
          } else if (value != null) {
            searchParams.append(key, value);
          }
        }
        return searchParams.toString();
      }
    });


    if (response.data && response.data.content) {
      rows.value = response.data.totalElements;
      items.value = response.data.content;
      loadingImages.value = new Set(items.value.map((item) => item.itemSeq));
    }
  } catch (error) {
    console.error("데이터 불러오기 실패:", error);
  }
};

const handleImageLoad = (itemSeq) => {
  loadingImages.value.delete(itemSeq);
};

const handleImageError = (itemSeq) => {
  loadingImages.value.delete(itemSeq);
};

const getImageUrl = (url) => {
  return url || '/no-image.png';
};

function checkItemDivision(key) {
  const index = selectedDivisions.value.indexOf(key);
  if (index > -1) {
    selectedDivisions.value.splice(index, 1);
  } else {
    selectedDivisions.value.push(key);
  }
  currentPage.value = 1;
  findItemsByFilter();
}

const childItemList = ref([]);
const itemInventoryList = ref([]);
const itemDTO = ref();
const itemInventory = ref();
const itemInventoryCurrentPage = ref(0);

const itemDetail = async (itemSeq) => {
  itemInventoryCurrentPage.value = 0;
  try {
    const response = await axios.get(`item/${itemSeq}`);
    childItemList.value = response.data.childItemList;
    itemInventoryList.value = response.data.itemInventoryList;
    itemDTO.value = response.data.itemDTO;
    itemInventory.value = itemInventoryList.value[0];
  } catch (error) {
    console.error("품목 상세보기 불러오기 실패:", error);
  }
};

watch(itemInventoryCurrentPage, () => {
  itemInventory.value = itemInventoryList.value[itemInventoryCurrentPage.value - 1];
});

watch(searchName, () => {
  currentPage.value = 1;
  findItemsByFilter();
});
watch(currentPage, findItemsByFilter);

watch([minExpiration, maxExpiration], () => {
  currentPage.value = 1;
  findItemsByFilter();
});

onMounted(() => {
  findItemsByFilter();
});

const handleItemUpdate = (itemSeq) => {
  router.push(`/item/update/${itemSeq}`);
};

const itemDelete = async (itemSeq) => {
  try {
    const token = localStorage.getItem('accessToken');
    await axios.delete(`item/${itemSeq}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    // 성공 알림
    alert('삭제되었습니다.');

    // 현재 items 배열에서 삭제된 아이템 제거
    items.value = items.value.filter(item => item.itemSeq !== itemSeq);

    // 전체 아이템 수 감소
    rows.value = rows.value - 1;

    currentPage.value = 1;
    await findItemsByFilter();


  } catch (error) {
    if (error.response?.status === 401) {
      alert('세션이 만료되었습니다. 다시 로그인해주세요.');
      localStorage.removeItem('accessToken');
      router.push('/login');
    } else {
      console.error('삭제 실패:', error);
      alert('삭제에 실패했습니다.');
    }
  }
};
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
              <input type="number" class="form-control text-center" placeholder=""
                     v-model.number="minExpiration" />
              <span class="input-group-text">일</span>
            </div>
            <span class="mx-2">~</span>
            <div class="input-group" style="width: 100px;">
              <input type="number" class="form-control text-center" placeholder=""
                     v-model.number="maxExpiration" />
              <span class="input-group-text">일</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div style="width: 90%;">
        <div class="d-flex justify-content-between">
          <div class="d-flex justify-content-between">
            <div>검색결과: {{ rows }}개</div>
          </div>
          <RouterLink to="/item/input">
            <b-button variant="light" size="sm" class="button">
              품목 등록
            </b-button>
          </RouterLink>
        </div>
        <div class="list-headline row"></div>
        <div style="max-height: 600px; overflow-y: auto;">
          <div class="item-container">
            <div v-for="item in items" :key="item.itemSeq" class="item-card" @click="itemDetail(item.itemSeq)">
              <div class="item-image">
                <div v-if="loadingImages.has(item.itemSeq)" class="image-loading">
                  <div class="spinner"></div>
                </div>
                <img
                    :src="getImageUrl(item.itemImageUrl)"
                    :alt="item.itemName"
                    @load="handleImageLoad(item.itemSeq)"
                    @error="handleImageError(item.itemSeq)"
                    class="item-img"
                    :class="{ 'hidden': loadingImages.has(item.itemSeq) }"
                />
              </div>
              <div class="item-content">
                <p class="item-name">{{ item.itemName }}</p>
                <ul>
                  <li>품목: {{ itemDivisionMap[item.itemDivision] || item.itemDivision }}</li>
                  <li>유통기한: {{ item.itemExpirationHour }} 시간</li>
                  <li>단가: {{ item.itemPrice.toLocaleString() }} ₩</li>
                </ul>
                <div class="d-flex justify-content-end mt-3">
                  <editIcon @click.stop="handleItemUpdate(item.itemSeq)" class="icon"/>
                  <trashIcon @click.stop="itemDelete(item.itemSeq)" class="icon"/>
                </div>
              </div>
            </div>
          </div>
          <div v-if="itemDTO" class="mt-4 p-4 border rounded shadow-sm bg-light">
            <div class="row">
              <div class="col-md-2">
                <img
                    :src="getImageUrl(itemDTO.itemImageUrl)"
                    class="img-fluid rounded detail-img"
                    :alt="itemDTO.itemName"
                />
              </div>
              <div class="col-md-3">
                <h5 class="item-name">{{ itemDTO.itemName }} BOM</h5>
                <ul class="list-unstyled">
                  <li v-for="childItem in childItemList" :key="childItem.itemSeq">
                    · {{ childItem.itemName }} : {{ childItem.bomChildItemQuantity }} {{ childItem.itemUnitTitle }}
                  </li>
                </ul>
              </div>
              <div v-if="itemInventoryList.length > 0" class="col-md-6">
                <h5 class="item-name">재고 조회</h5>
                <ul class="list-unstyled">
                  <li>입고일: {{ dayjs(itemInventory.itemInventoryReceiptDate).format('YYYY년 MM월 DD일 HH시') }}</li>
                  <li>유통기한: {{ dayjs(itemInventory.itemInventoryExpirationDate).format('~ YYYY년 MM월 DD일 HH시') }}</li>
                  <li>잔량 / 입고수량: {{ itemInventory.itemInventoryRemainAmount }} / {{ itemInventory.itemInventoryQuantityReceived }}</li>
                  <li v-if="itemInventory.itemInventoryNote">비고: {{ itemInventory.itemInventoryNote }}</li>
                </ul>
                <div class="page-center">
                  <b-pagination
                      v-model="itemInventoryCurrentPage"
                      :total-rows="itemInventoryList.length"
                      :per-page="1"
                  ></b-pagination>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="page-center">
        <b-pagination v-model="currentPage" :total-rows="rows" :per-page="perPage" @change="findItemsByFilter"></b-pagination>
      </div>
    </div>
  </div>
</template>

<style scoped>
.item-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: flex-start;
}

.item-card {
  flex: 0 0 calc(25% - 20px);
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.item-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.item-image {
  position: relative;
  width: 100%;
  height: 200px;
  background-color: #f8f9fa;
  overflow: hidden;
}

.item-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  transition: opacity 0.3s ease;
}

.image-loading {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
}

.spinner {
  width: 30px;
  height: 30px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.hidden {
  opacity: 0;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.item-content {
  padding: 15px;
}

.item-name {
  font-size: 1.1em;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-content ul {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 0.9em;
}

.item-content li {
  margin: 5px 0;
  color: #666;
}

.detail-img {
  width: 100%;
  height: auto;
  max-height: 200px;
  object-fit: contain;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
}

.icon {
  width: 20px;
  height: 20px;
  margin-left: 10px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.icon:hover {
  transform: scale(1.1);
}

div {
  font-size: small;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
  transition: background-color 0.2s ease;
}

.button:hover {
  background-color: #FFE4B5;
}

.side-box {
  min-height: 100px;
  margin-top: 20px;
  border: 1px solid #dee2e6;
}

.card-title {
  font-size: medium;
  font-weight: bold;
  color: #333;
}

.list-headline {
  border-bottom: 1px solid #dee2e6;
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
}

.page-center {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

</style>