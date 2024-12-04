<script setup>
import Header from '@/components/common/Header.vue';
</script>

<template>
  <Header />
  <section class="flex-shrink-0">
    <b-container>
      <router-view />
    </b-container>
  </section>
</template>

<style scoped>
</style>
