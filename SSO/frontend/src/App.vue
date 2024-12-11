<script setup>
import {onMounted} from "vue";
import {useUserStore} from "@/stores/UserStore.js";
const userStore = useUserStore();

onMounted(() => {
  userStore.initialize(); // Store에서 초기화 로직 호출
});
</script>

<template>
  <router-view />
</template>

<style scoped>
</style>
