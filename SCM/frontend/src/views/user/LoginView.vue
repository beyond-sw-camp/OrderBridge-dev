<script setup>
import {onMounted, ref} from "vue";
import { useUserStore } from "@/stores/UserStore.js";
import { useRoute, useRouter } from 'vue-router'; // Vue Router 사용
import { sError } from '@/common/salert';

const userEmployeeNo = ref("");
const userPwd = ref("");
const userStore = useUserStore();
const router = useRouter(); // useRouter로 라우터 객체 사용
const route = useRoute();

const handleLogin = async() => {
  await userStore.login(userEmployeeNo.value, userPwd.value)

  if(userStore.isAuthenticated) {
    const redirect = route.query.redirect || '/'; // 리다이렉트 경로가 없으면 홈으로 이동
    await router.push(redirect);
  } else {
    await sError(userStore.error)
  }
}
</script>

<template>
  <div class="d-flex justify-content-center align-items-center m-0">
    <div class="card login-card p-4">
      <div class="text-center mb-4">
        <h4 class="fw-bold">로그인</h4>
      </div>
      <form @submit.prevent="handleLogin">
        <div class="mb-3">
          <label for="employeeNo" class="form-label">사원번호</label>
          <input type="text" id="employeeNo" class="form-control" v-model="userEmployeeNo" placeholder="사원번호를 입력하세요." required/>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" id="password" class="form-control" v-model="userPwd" placeholder="비밀번호를 입력하세요." required/>
        </div>
        <b-button type="submit"  variant="light" size="sm" class="btn btn custom-btn button w-100 mb-2">로그인</b-button>
      </form>
    </div>
  </div>
</template>

<style scoped>

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.login-card {
  width: 400px;
  border: none;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.text-muted a {
  color: #004a9f;
}

.text-muted a:hover {
  text-decoration: underline;
}
/* 공통 버튼 스타일 */
.custom-btn {
  height: 50px; /* 동일한 높이 설정 */
  padding: 10px 20px; /* 동일한 내부 여백 */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px; /* 글씨 크기 통일 */
  border-radius: 5px; /* 라운드 처리 */
}

</style>