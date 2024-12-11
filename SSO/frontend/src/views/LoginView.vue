<script setup>
import {ref} from "vue";
import { useUserStore } from "@/stores/UserStore.js";
import { useRouter } from 'vue-router'; // Vue Router 사용

const id = ref("");
const pwd = ref("");
const userStore = useUserStore();
const router = useRouter(); // useRouter로 라우터 객체 사용

const handleLogin = async() => {
  await userStore.login(id.value, pwd.value)

  if(userStore.isAuthenticated) {
    await router.push('/main');
  } else {
    alert(userStore.error)
  }
}
</script>

<template>
  <div class="d-flex justify-content-center align-items-center vh-100 bg-light">
    <div class="card shadow p-4" style="width: 360px;">
      <h3 class="text-center mb-4">업무망 로그인</h3>
      <b-form @submit.prevent="handleLogin">
        <!-- 아이디 입력 -->
        <b-form-group label-for="id">
          <b-input-group>
            <b-form-input id="id" type="text" v-model="id" placeholder="아이디"/>
          </b-input-group>
        </b-form-group>

        <!-- 비밀번호 입력 -->
        <b-form-group label-for="pwd">
          <b-input-group>
            <b-form-input id="pwd" type="password" v-model="pwd" placeholder="비밀번호"/>
          </b-input-group>
        </b-form-group>

        <!-- 로그인 버튼 -->
        <div class="d-grid gap-2">
          <b-button type="submit" variant="primary" class="mt-3">
            LOGIN
          </b-button>
        </div>
      </b-form>
    </div>
  </div>
</template>