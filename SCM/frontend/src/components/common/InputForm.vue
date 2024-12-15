<script setup>
import {ref} from "vue";
import searchIcon from '@/assets/searchIcon.svg'

const clients = ref(
    {
      clientName: '대한항공 씨앤디서비스 (주)',
      clientNumber: '723-86-02017',
      clientManager: '최덕자',
      clientDate: '2024/11/28',
    },
);
const rows = ref(123);
const perPage = ref(10);
const currentPage = ref(1);

const quoteDate = ref("");
const client = ref("");
const manager = ref("");
const note = ref("");

const addClient = () => {
  // 임시
  client.value =  clients.value.clientName;
  manager.value = clients.value.clientManager;
}
</script>

<template>
  <div class="col-6 d-flex flex-column">
    <b-form-group label-cols="4" label-cols-lg="2" label="견적일자" label-for="quote-date">
      <b-form-input
          class="w-75"
          size="sm"
          type="date"
          id="quote-date"
          v-model="quoteDate"
          placeholder="견적일자를 입력해 주세요.">
      </b-form-input>
    </b-form-group>

    <!--    거래처 모달창 구현 필요 -->
    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="거래처" label-for="client">
      <b-input-group class="w-75" size="sm">
        <b-form-input
            type="text"
            id="client"
            v-model="client"
            placeholder="거래처를 선택해 주세요.">
        </b-form-input>
        <b-input-group-text>
          <searchIcon class="icon" data-bs-toggle="modal" data-bs-target="#ClientModal"/>
        </b-input-group-text>
      </b-input-group>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="담당자" label-for="manager">
      <b-form-input
          class="w-75"
          size="sm"
          type="text"
          id="manager"
          v-model="manager"
          placeholder="담당자">
      </b-form-input>
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="default" label="비고" label-for="note">
      <b-form-input
          type="text"
          size="sm"
          id="note"
          v-model="note"
          placeholder="내용을 입력해 주세요.">
      </b-form-input>
    </b-form-group>
  </div>

  <!-- client Modal bootstrap -->
  <div class="modal fade" id="ClientModal" tabindex="-1" aria-labelledby="ClientModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">물품 선택</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div style="max-height: 500px; overflow-y: scroll"
               class="d-flex row justify-content-center align-items-center">
            <div class="list-headline row">
              <div class="list-head col-6">회사명</div>
              <div class="list-head col-2">사업자번호</div>
              <div class="list-head col-2">담당자</div>
              <div class="list-head col-2">등록일</div>
            </div>
            <div v-for="n in 10" :key="n" class="list-line row" data-bs-dismiss="modal" @click="addClient">
              <div class="list-body col-6">{{ clients.clientName }}</div>
              <div class="list-body col-2">{{ clients.clientNumber }}</div>
              <div class="list-body col-2">{{ clients.clientManager }}</div>
              <div class="list-body col-2">{{ clients.clientDate }}</div>
            </div>
          </div>
        </div>
        <div class="modal-footer pagination">
          <b-pagination
              v-model="currentPage"
              :total-rows="rows"
              :per-page="perPage">
          </b-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
div {
  font-size: small;
}

.icon {
  width: 20px;
  height: 20px;
}

.list-headline {
  width: 90%;
  border-bottom: 1px solid black;
  margin-bottom: 10px;
  padding: 20px 40px 20px 20px;
}

.list-head {
  text-align: center;
}

.list-line {
  width: 90%;
  border: 1px solid Silver;
  border-radius: 8px;
  margin-left: 1px;
  margin-top: 20px;
  padding: 20px 40px 20px 20px;
}

.list-body {
  text-align: center;
  margin: auto 0;
}

.pagination {
  justify-content: center; /* 가로 중앙 정렬 */
  margin-top: 20px;
}
</style>