<script setup>

import router from "@/router/index.js";
import {useUserStore} from "@/stores/UserStore.js";
const userStore = useUserStore();
</script>

<template>
    <div class="hero-container">
      <div class="hero-left">
        <img src="../../images/img.png" alt="Clouds" class="hero-image" />
      </div>
      <div class="hero-right">
        <span class="hero-title">
          You have a <br />
          reason, We have a <br />
          <span class="highlight">
            <span class="char" style="color: #2b3a83;">s</span>
            <span class="char" style="color: #e20000;">o</span>
            <span class="char" style="color: #ff5ad0;">l</span>
            <span class="char" style="color: #f5871f;">u</span>
            <span class="char" style="color: #3fcb2c;">t</span>
            <span class="char" style="color: #5b3df1;">i</span>
            <span class="char" style="color: #b0cffd;">o</span>
            <span class="char" style="color: #5b3df1;">n</span>
          </span>
        </span>
        <p class="hero-description">
          오더브릿지 주문 관리 시스템은 <br />
          안전하고 차별적인 서비스를 제공합니다.
        </p>
        <button v-if="!userStore.isAuthenticated" class="hero-button" @click="router.push('/login')">Log In</button>
      </div>
    </div>
</template>

<style scoped>

.hero-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff8e7;
  height: 100vh;
  padding: 0 4% 0 0;
}

.hero-left {
  flex: 1.8;
  display: flex;
  height: 100%;
  width: 100%;
  align-items: stretch; /* 자식 요소를 세로로도 채우기 */
  justify-content: stretch; /* 자식 요소를 가로로도 채우기 */
}

.hero-image {
  width: 100%; /* 컨테이너 너비 채우기 */
  height: 100%; /* 컨테이너 높이 채우기 */
  object-fit: cover; /* 비율을 유지하며 컨테이너를 완전히 채우기 */
}

.hero-right {
  flex: 1;
  text-align: right;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  line-height: 1.4;
  font-family: 'Abhaya Libre', serif; /* 글씨체 적용 */
}

.highlight .char {
  font-family: 'Abhaya Libre', serif;
}

.hero-description {
  font-size: 1.2rem;
  color: #555;
  margin-bottom: 2rem;
  font-family: 'Alkalami', serif;
}

.hero-button {
  font-family: 'Abhaya Libre', serif;
  background-color: #D7E5FF;
  height: 50px;
  width: 150px;
  border-radius: 20px;
  font-size: 1.2rem;
}

.hero-button:hover {
  background-color: #FFFDF8;
}

</style>
