<script setup>
import Header from '@/components/common/Header.vue';
import SideMenuBar from "@/components/common/SideMenuBar.vue";
</script>

<template>
  <Header />
  <section class="flex-shrink-0">
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
