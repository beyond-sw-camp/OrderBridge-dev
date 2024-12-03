<script setup>
import searchIcon from '@/assets/searchIcon.svg';
import notificationIcon from '@/assets/notificationIcon.svg';
import loginIcon from '@/assets/loginIcon.svg';
import basicIcon from '@/assets/basicIcon.svg';
import salesIcon from '@/assets/salesIcon.svg';
import productionIcon from '@/assets/productionIcon.svg';
import orderIcon from '@/assets/orderIcon.svg';

import { ref } from "vue";

const activeMenu = ref(null); // 현재 활성화된 메뉴

// 메뉴를 보여줄 때 호출
function showSubMenu(menu) {
  activeMenu.value = menu;
}

// 메뉴를 숨길 때 호출
function hideSubMenu() {
  activeMenu.value = null;
}

</script>

<template>
  <nav class="nav">
    <div class="nav-left">
      <!-- 로고 -->
      <div class="navbar-logo">
        <a href="#">
          <img src="@/images/logo.png" alt="OrderBridge Logo" class="logo-img" />
        </a>
      </div>

      <!-- 네비게이션 -->
      <ul class="nav-links">
        <li><a href="#"><basicIcon class="icon" /> 기본등록</a></li>
        <li><a href="#"><salesIcon class="icon" /> 영업관리</a></li>
        <li><a href="#"><orderIcon class="icon" /> 주문관리</a></li>
        <li><a href="#"><productionIcon class="icon" /> 생산관리</a></li>
        <li><a href="#"><basicIcon class="icon" /> 통계</a></li>
      </ul>
    </div>
      <!-- 오른쪽 아이콘 -->
      <ul class="nav-icons">
        <li><a href="#"><searchIcon class="icon" /></a></li>
        <li><a href="#"><notificationIcon class="icon" /></a></li>
        <li><a href="#"><loginIcon class="icon" /></a></li>
      </ul>
  </nav>
</template>
<style scoped>
/* 네비게이션 스타일 */
.nav {
  background-color: #fff8e1; /* 배경 색 */
  border-bottom: 1px solid #e0e0e0;
  padding: 0 20px; /* 기본 여백 제거 */
  display: flex;
  align-items: center; /* 모든 요소를 수직 중앙 정렬 */
  justify-content: space-between; /* 좌우 끝 정렬 */
}

/* 로고와 메뉴를 묶은 컨테이너 */
.nav-left {
  display: flex;
  align-items: center; /* 로고와 메뉴를 수직 중앙 정렬 */
}

/* 로고 스타일 */
.navbar-logo {
  display: flex;
  align-items: center; /* 로고 수직 정렬 */
  margin-right: 20px; /* 로고와 메뉴 사이 간격 */
}

.logo-img {
  height: 60px; /* 로고 크기 */
  margin: 0; /* 여백 제거 */
}

/* 네비게이션 링크 스타일 */
.nav-links {
  display: flex;
  align-items: center; /* 메뉴를 수직 중앙 정렬 */
  list-style: none;
  gap: 15px; /* 메뉴 간 간격 */
  margin: 0; /* 기본 마진 제거 */
  padding: 0; /* 기본 패딩 제거 */
}

.nav-links li a {
  display: flex;
  align-items: center; /* 텍스트와 아이콘 수직 정렬 */
  font-size: 16px;
  color: #333;
  text-decoration: none;
}

.nav-links li a:hover {
  color: #000; /* 호버 효과 */
}

.icon {
  margin-right: 8px; /* 아이콘과 텍스트 간 간격 */
  width: 20px;
  height: 20px;
  fill: #333; /* 아이콘 색상 */
}

/* 오른쪽 아이콘 스타일 */
.nav-icons {
  display: flex;
  align-items: center; /* 아이콘 수직 정렬 */
  list-style: none;
  gap: 15px; /* 아이콘 간 간격 */
  margin: 0; /* 기본 마진 제거 */
  padding: 0; /* 기본 패딩 제거 */
}

.nav-icons li a {
  display: flex;
  align-items: center;
  font-size: 18px;
  color: #333;
  text-decoration: none;
}

.nav-icons li a:hover {
  color: #000;
}
</style>
