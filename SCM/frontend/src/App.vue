<script setup>
import Header from '@/components/common/Header.vue';
import SideMenuBar from "@/components/common/SideMenuBar.vue";
import Main from "@/views/main/MainView.vue";
import { ref } from 'vue';

const isMainPage = ref(true);
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
