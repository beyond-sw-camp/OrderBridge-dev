<script setup>
import { ref } from "vue";

const email = ref("");
const password = ref("");

const handleLogin = () => {
  console.log("Login with:", email.value, password.value);
  // 로그인 처리 로직 추가
};
</script>

<template>
  <div class="d-flex justify-content-center" style="padding-top: 50px">
    <div class="card login-card p-4">
      <div class="text-center mb-4">
        <h4 class="fw-bold">로그인</h4>
      </div>
      <form @submit.prevent="handleLogin">
        <div class="mb-3">
          <label for="email" class="form-label">사원번호</label>
          <input type="email" id="email" class="form-control" v-model="email" placeholder="사원번호를 입력하세요." required/>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input type="password" id="password" class="form-control" v-model="password" placeholder="비밀번호를 입력하세요." required/>
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

.btn-primary {
  background-color: #004a9f;
  border-color: #004a9f;
}

.btn-primary:hover {
  background-color: #00397c;
  border-color: #00397c;
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

/* 아이콘 스타일 */
.btn-icon {
  height: 24px;
  width: 24px;
  margin-right: 10px; /* 아이콘과 텍스트 간 간격 */
}

/* 색상별 버튼 스타일 */
.btn-yellow {
  background-color: #ffcc00;
  color: #000;
  border: none;
}

.btn-yellow:hover {
  background-color: #e6b800;
  color: #000;
}

.btn-green {
  background-color: #28a745;
  color: #fff;
  border: none;
}

.btn-green:hover {
  background-color: #218838;
  color: #fff;
}
</style>