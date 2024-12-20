<script setup>
import Header from '@/components/common/Header.vue';
import SideMenuBar from "@/components/common/SideMenuBar.vue";
import Main from "@/views/main/MainView.vue";
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const isMainPage = ref(route.path === '/');

watch(() => route.path, (newPath) => {
  isMainPage.value = newPath === '/';
});
</script>

<template>
  <Header />
  <Main v-if="isMainPage" />
  <section class="flex-shrink-0" v-else>
    <b-container>
      <router-view />
    </b-container>
  </section>
  <SideMenuBar />
</template>

<style scoped>

section {
  margin-top: 40px;
}

</style>
