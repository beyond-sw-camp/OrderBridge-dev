<script setup>
import Header from '@/components/common/Header.vue';
import SideMenuBar from "@/components/common/SideMenuBar.vue";
import Main from "@/views/main/MainView.vue";
import {onMounted, ref, watch} from 'vue';
import { useRoute } from 'vue-router';
import {useUserStore} from "@/stores/UserStore.js";
const userStore = useUserStore();

const route = useRoute();
const isMainPage = ref(route.path === '/');

const isLogin = localStorage.getItem('accessToken');

watch(() => route.path, (newPath) => {
  isMainPage.value = newPath === '/';
});

// 컴포넌트가 마운트될 때 상태 복원 처리
onMounted(() => {
  userStore.initialize(); // Store에서 초기화 로직 호출
});
</script>

<template>
  <Header v-if="isLogin"/>
  <Main v-if="isMainPage" />
  <section class="flex-shrink-0" v-else>
    <b-container>
      <router-view />
    </b-container>
  </section>
  <SideMenuBar v-if="isLogin"/>
</template>

<style scoped>

section {
  margin-top: 100px;
}

</style>
