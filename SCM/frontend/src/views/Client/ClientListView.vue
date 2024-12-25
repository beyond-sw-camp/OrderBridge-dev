<script setup>
import { onMounted, ref, watch } from 'vue';
import axios from 'axios';
import { BInputGroup, BFormInput, BFormCheckbox, BButton } from 'bootstrap-vue-3';
import { useRouter } from "vue-router";
import dayjs from "dayjs";
import trashIcon from "@/assets/trashIcon.svg";
import editIcon from "@/assets/editIcon.svg";

const searchBusinessNumber = ref("");
const router = useRouter();
const clients = ref([]);
const searchName = ref("");
const currentPage = ref(1);
const perPage = ref(10);
const totalCount = ref(0);
const selectedClient = ref(null);

const findClientsByFilter = async () => {
  try {
    const response = await axios.get("http://localhost:8090/api/v1/client", {
      params: {
        page: currentPage.value,
        size: perPage.value,
        clientName: searchName.value || null,
        clientRegistrationNo: searchBusinessNumber.value || null
      }
    });

    console.log("API Response:", response.data);  // 응답 데이터 확인

    if (response.data.clients) {
      clients.value = response.data.clients;
      totalCount.value = response.data.totalCount;
    } else {
      console.error("Unexpected response format:", response.data);
    }
  } catch (error) {
    console.error("거래처 목록 조회 실패:", error);
  }
};

watch([searchName, searchBusinessNumber], () => {
  currentPage.value = 1;
  findClientsByFilter();
});

watch(currentPage, () => {
  findClientsByFilter();
});

onMounted(() => {
  findClientsByFilter();
});

const toggleDetail = async (client) => {
  if (selectedClient.value === client.clientSeq) {
    selectedClient.value = null;
  } else {
    selectedClient.value = client.clientSeq;
  }
};



const deleteClient = async (seq) => {
  const result = confirm("정말 삭제하시겠습니까?");
  if (result) {
    try {
      await axios.delete(`http://localhost:8090/api/v1/client/${seq}`);
      alert('거래처가 삭제되었습니다.');
      findClientsByFilter(); // 목록 갱신
    } catch (error) {
      console.error("삭제 실패:", error);
      alert('삭제에 실패했습니다.');
    }
  }
};
</script>

<template>
  <h4 class="title">거래처관리 > 거래처조회</h4>
  <div class="row">
      <!-- 거래처명 검색 -->
      <div class="col-md-3">
        <div class="side-box card">
          <div class="card-body">
            <p class="card-title">거래처명</p>
            <b-input-group class="mt-3">
              <b-form-input v-model="searchName" placeholder="거래처명을 입력하세요"></b-form-input>
              <b-button variant="light" class="button" @click="findClientsByFilter">
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

        <!-- 사업자등록번호 검색 -->
        <div class="side-box card">
          <div class="card-body">
            <p class="card-title">사업자등록번호</p>
            <b-input-group class="mt-3">
              <b-form-input v-model="searchBusinessNumber" placeholder="사업자등록번호를 입력하세요"></b-form-input>
              <b-button variant="light" class="button" @click="findClientsByFilter">
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
      </div>
    <div class="col-md-9">
      <div style="width: 90%;">
        <div class="d-flex justify-content-between">
          <div>검색결과: {{ totalCount }}개</div>
          <RouterLink to="/client/input">
            <b-button variant="light" size="sm" class="button">
              거래처 등록
            </b-button>
          </RouterLink>
        </div>
        <div class="list-headline row">
          <div class="list-headvalue col-md-3">거래처명</div>
          <div class="list-headvalue col-md-3">사업자번호</div>
          <div class="list-headvalue col-md-3">대표자</div>
          <div class="list-headvalue col-md-3">등록일</div>
        </div>
        <template v-if="clients.length > 0">
          <div style="max-height: 600px; overflow-y: auto;">
            <div v-for="client in clients"
                 :key="client.clientSeq"
                 class="list-line row"
                 @click="toggleDetail(client)">
              <div class="col-md-3">
                <b>{{ client.clientName }}</b>
              </div>
              <div class="list-value col-md-3">
                {{ client.clientRegistrationNo }}
              </div>
              <div class="list-value col-md-3">
                {{ client.clientRepresentative }}
              </div>
              <div class="list-value col-md-3">
                {{ dayjs(client.clientRegDate).format('YYYY/MM/DD') }}
              </div>
              <div v-if="selectedClient === client.clientSeq"
                   class="client-detail col-12 mt-3">
                <div class="row">
                  <div class="col-md-6">
                    <ul class="detail-list">
                      <li>거래처명: {{ client.clientName }}</li>
                      <li>사업자번호: {{ client.clientRegistrationNo }}</li>
                      <li>대표자: {{ client.clientRepresentative || '-' }}</li>
                      <li>연락처: {{ client.clientPhoneNo }}</li>
                      <li>이메일: {{ client.clientEmail || '-' }}</li>
                      <li>등록일: {{ dayjs(client.clientRegDate).format('YYYY/MM/DD HH:mm:ss') }}</li>
                      <li>수정일: {{ client.clientModDate ? dayjs(client.clientModDate).format('YYYY/MM/DD HH:mm:ss') : '-' }}</li>
                    </ul>
                  </div>
                  <div class="d-flex justify-content-end">
                    <RouterLink :to="`/client/update/${client.clientSeq}`">
                      <editIcon class="icon me-2" />
                    </RouterLink>
                    <trashIcon
                        class="icon"
                        @click.stop="deleteClient(client.clientSeq)">
                    </trashIcon>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
        <template v-else>
          <b-card-text class="no-list-text">해당 검색조건에 부합한 거래처가 존재하지 않습니다.</b-card-text>
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

.no-list-text {
  text-align: center;
  margin-top: 100px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.client-detail {
  padding: 15px;
  border-radius: 4px;
  margin-top: 10px;
}

.detail-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

 .icon {
   width: 20px;
   height: 20px;
   cursor: pointer;
 }

.detail-list li {
  margin-bottom: 5px;
}
</style>