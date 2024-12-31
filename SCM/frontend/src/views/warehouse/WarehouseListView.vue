<script setup>
import { onMounted, ref, watch } from 'vue';
import axios from "@/axios";
import { BInputGroup, BFormInput, BFormCheckbox, BButton } from 'bootstrap-vue-3';
import { useRouter } from "vue-router";
import dayjs from "dayjs";
import trashIcon from "@/assets/trashIcon.svg";
import editIcon from "@/assets/editIcon.svg";

const router = useRouter();
const warehouses = ref([]);
const searchName = ref("");
const selectedType = ref("");
const currentPage = ref(1);
const perPage = ref(10);
const totalCount = ref(0);
const expandedIndex = ref(null);

const warehouseTypeList = [
  { key: 'FACTORY', value: '생산' },
  { key: 'WAREHOUSE', value: '보관' },
];

const warehouseTypeMap = {
  FACTORY: "생산",
  WAREHOUSE: "보관",
};

const findWarehousesByFilter = async () => {
  try {
    const response = await axios.get("warehouse", { // 상대 경로 사용
      params: {
        page: currentPage.value,
        size: perPage.value,
        warehouseName: searchName.value || null,
        warehouseType: selectedType.value || null
      }
    });

    warehouses.value = response.data.warehouses;
    totalCount.value = response.data.totalCount;
  } catch (error) {
    console.error("창고 목록 조회 실패:", error);
  }
};

watch([searchName, selectedType], () => {
  currentPage.value = 1; // 검색 조건 변경 시 첫 페이지로 이동
  findWarehousesByFilter();
});

watch(currentPage, () => {
  findWarehousesByFilter();
});

onMounted(() => {
  findWarehousesByFilter();
});

function checkWarehouseType(key) {
  selectedType.value = selectedType.value === key ? "" : key;
}

const toggleDetails = async (index, warehouse) => {
  if (expandedIndex.value === index) {
    expandedIndex.value = null; // 이미 확장된 항목은 닫기
  } else {
    try {
      const response = await axios.get(`warehouse/${warehouse.warehouseSeq}`); // 상대 경로 사용
      expandedIndex.value = index;
      const warehouseIndex = warehouses.value.findIndex(w => w.warehouseSeq === warehouse.warehouseSeq);
      if (warehouseIndex !== -1) {
        warehouses.value[warehouseIndex] = { ...warehouses.value[warehouseIndex], ...response.data }; // 상세 정보 추가
      }
    } catch (error) {
      console.error("창고 상세 정보 조회 실패:", error);
    }
  }
};

const deleteWarehouse = async (seq) => {
  const result = confirm("정말 삭제하시겠습니까?");
  if (result) {
    try {
      await axios.delete(`warehouse/${seq}`); // 상대 경로 사용
      alert('창고가 삭제되었습니다.');
      findWarehousesByFilter(); // 삭제 후 목록 갱신
    } catch (error) {
      console.error("삭제 요청 실패:", error);
      alert('삭제에 실패했습니다. 다시 시도해주세요.');
    }
  }
};
</script>

<template>
  <h4 class="title">창고관리 > 창고조회</h4>
  <div class="row">
    <div class="col-md-3">
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">창고명</p>
          <b-input-group class="mt-3">
            <b-form-input v-model="searchName" placeholder="창고명을 입력하세요"></b-form-input>
            <b-button variant="light" class="button" @click="findWarehousesByFilter">
              <svg width="1em" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
                <path d="M344.5,298c15-23.6,23.8-51.6,23.8-81.7c0-84.1-68.1-152.3-152.1-152.3C132.1,64,64,132.2,64,216.3
                  c0,84.1,68.1,152.3,152.1,152.3c30.5,0,58.9-9,82.7-24.4l6.9-4.8L414.3,448l33.7-34.3L339.5,305.1L344.5,298z
                  M301.4,131.2  c22.7,22.7,35.2,52.9,35.2,85c0,32.1-12.5,62.3-35.2,85c-22.7,22.7-52.9,35.2-85,35.2c-32.1,0-62.3-12.5-85-35.2
                  c-22.7-22.7-35.2-52.9-35.2-85c0-32.1,12.5-62.3,35.2-85c22.7-22.7,52.9-35.2,85-35.2C248.5,96,278.7,108.5,301.4,131.2z" />
              </svg>
            </b-button>
          </b-input-group>
        </div>
      </div>
      <div class="side-box card">
        <div class="card-body">
          <p class="card-title">창고 구분</p>
          <template v-for="type in warehouseTypeList" :key="type.key">
            <b-form-checkbox
                :checked="selectedType === type.key"
                @click="checkWarehouseType(type.key)">
              {{ type.value }}
            </b-form-checkbox>
          </template>
        </div>
      </div>
    </div>
    <div class="col-md-9">
      <div style="width: 90%;">
        <div class="d-flex justify-content-between">
          <div>검색결과: {{ totalCount }}개</div>
          <RouterLink to="/warehouse/input">
            <b-button variant="light" size="sm" class="button">
              창고 등록
            </b-button>
          </RouterLink>
        </div>
        <div class="list-headline row">
          <div class="list-headvalue col-md-4">창고명</div>
          <div class="list-headvalue col-md-2">구분</div>
          <div class="list-headvalue col-md-3">등록자</div>
          <div class="list-headvalue col-md-3">등록일</div>
        </div>
        <template v-if="warehouses.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="(warehouse, index) in warehouses"
                 :key="warehouse.warehouseSeq"
                 class="list-line row"
                 @click="toggleDetails(index, warehouse)">
              <div class="col-md-4">
                <b>{{ warehouse.warehouseName }}</b>
              </div>
              <div class="list-value col-md-2">
                {{ warehouseTypeMap[warehouse.warehouseType] }}
              </div>
              <div class="list-value col-md-3">
                {{ warehouse.userSeq }} {{ warehouse.userName ? `(${warehouse.userName})` : '' }}
              </div>
              <div class="list-value col-md-3">
                {{ dayjs(warehouse.warehouseRegDate).format('YYYY/MM/DD') }}
              </div>

              <!-- 상세 정보 영역 -->
              <div v-if="expandedIndex === index" class="col-md-12 mt-3">
                <div class="detail-content">
                  <div class="row mb-2">
                    <div class="col-md-6">
                      <p><strong>창고 번호:</strong> {{ warehouse.warehouseSeq }}</p>
                      <p><strong>창고명:</strong> {{ warehouse.warehouseName }}</p>
                      <p><strong>창고 구분:</strong> {{ warehouseTypeMap[warehouse.warehouseType] }}</p>
                    </div>
                    <div class="col-md-6">
                      <p><strong>등록일:</strong> {{ dayjs(warehouse.warehouseRegDate).format('YYYY/MM/DD HH:mm:ss') }}</p>
                      <p v-if="warehouse.warehouseModDate"><strong>수정일:</strong> {{ dayjs(warehouse.warehouseModDate).format('YYYY/MM/DD HH:mm:ss') }}</p>
                      <p><strong>담당자:</strong> {{ warehouse.userName }}</p>
                    </div>
                  </div>
                  <div class="row mb-3">
                    <div class="col-md-12">
                      <p><strong>비고:</strong> {{ warehouse.warehouseNote }}</p>
                    </div>
                  </div>
                  <div class="d-flex justify-content-end">
                    <RouterLink :to="`/warehouse/Modfiy/${warehouse.warehouseSeq}`">
                      <editIcon class="icon me-2" />
                    </RouterLink>
                    <trashIcon
                        class="icon"
                        @click.stop="deleteWarehouse(warehouse.warehouseSeq)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
        <template v-else>
          <b-card-text class="no-list-text">해당 검색조건에 부합한 창고가 존재하지 않습니다.</b-card-text>
        </template>
      </div>
    </div>
  </div>
  <div class="pagination">
    <b-pagination
        v-model="currentPage"
        :total-rows="totalCount"
        :per-page="perPage"
        align="center">
    </b-pagination>
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

.list-headvalue {
  text-align: center;
}

.list-line {
  width: 99%;
  border: 1px solid Silver;
  border-radius: 8px;
  margin-left: 1px;
  margin-top: 20px;
  padding: 10px 5px 10px 5px;
  cursor: pointer;
}

.list-value {
  text-align: center;
  margin: auto 0;
  overflow: hidden;
  word-break: keep-all;
}

.detail-content {
  padding: 15px;
  background-color: transparent;
  border-radius: 4px;
  margin-top: 10px;
}

.no-list-text {
  text-align: center;
  margin-top: 100px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.icon {
  width: 20px;
  height: 20px;
  cursor: pointer;
}
</style>