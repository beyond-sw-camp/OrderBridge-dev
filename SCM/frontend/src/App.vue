<script setup>
import Header from '@/components/common/Header.vue';
import SideMenuBar from "@/components/common/SideMenuBar.vue";
import Main from "@/views/main/MainView.vue";
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from "@/stores/UserStore.js";
const userStore = useUserStore();

const route = useRoute();
const isMainPage = ref(route.path === '/');

watch(() => route.path, (newPath) => {
  isMainPage.value = newPath === '/';
});

</script>

<template>
  <Header v-if="userStore.isAuthenticated"/>
  <Main v-if="isMainPage" />
  <section class="flex-shrink-0" v-else>
    <b-container>
      <router-view />
    </b-container>
  </section>
  <SideMenuBar v-if="userStore.isAuthenticated"/>
</template>

<style scoped>

section {
  margin-top: 100px;
}

</style>
