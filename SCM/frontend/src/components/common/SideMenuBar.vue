<script setup>
import { ref } from 'vue';
import { RouterLink } from 'vue-router';

// 사이드바 열림/닫힘 상태
const isSidebar = ref(false);

// 현재 활성화된 메뉴 상태
const activeMenu = ref("");
const activeMenu2 = ref("");

// 메뉴 열고 닫기 함수
const toggleMenu = (menu) => {
  activeMenu.value = activeMenu.value === menu ? "" : menu;
};
const toggleMenu2 = (menu) => {
  activeMenu2.value = activeMenu2.value === menu ? "" : menu;
};
</script>

<template>
  <aside
      class="side-bar"
      :class="{ open: isSidebar }"
      @mouseover="isSidebar = true"
      @mouseleave="isSidebar = false"
  >
    <ul>
      <!-- 기본등록 -->
      <li>
        <span @click="toggleMenu('basic')">기본등록</span>
        <ul v-if="activeMenu === 'basic'">
          <li>
            <span @click="toggleMenu2('item')">품목</span>
            <ul v-if="activeMenu2 === 'item'">
              <li>
                <RouterLink class="content-item" to="">
                  품목 관리
                </RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('client')">거래처</span>
            <ul v-if="activeMenu2 === 'client'">
              <li>
                <RouterLink class="content-item" to="">
                  거래처 관리
                </RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('warehouse')">창고</span>
            <ul v-if="activeMenu2 === 'warehouse'">
              <li>
                <RouterLink class="content-item" to="">
                  창고 관리
                </RouterLink>
              </li>
            </ul>
          </li>
        </ul>
      </li>
      <!-- 영업관리 -->
      <li>
        <span @click="toggleMenu('sales')">영업관리</span>
        <ul v-if="activeMenu === 'sales'">
          <li>
            <span @click="toggleMenu2('item')">견적서</span>
            <ul v-if="activeMenu2 === 'item'">
              <li>
                <RouterLink class="content-item" to="">견적서 조회</RouterLink>
                <RouterLink class="content-item" to="">견적서 등록</RouterLink>
                <RouterLink class="content-item" to="">견적서 현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('order')">주문서</span>
            <ul v-if="activeMenu2 === 'order'">
              <li>
                <RouterLink class="content-item" to="">주문서 조회</RouterLink>
                <RouterLink class="content-item" to="">주문서 등록</RouterLink>
                <RouterLink class="content-item" to="">주문서 현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('warehouse')">판매</span>
            <ul v-if="activeMenu2 === 'warehouse'">
              <li>
                <RouterLink class="content-item" to="">거래명세서 조회</RouterLink>
                <RouterLink class="content-item" to="">거래명세서 등록</RouterLink>
                <RouterLink class="content-item" to="">거래명세서 현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('warehouse')">출하지시서</span>
            <ul v-if="activeMenu2 === 'warehouse'">
              <li>
                <RouterLink class="content-item" to="">출하지시서 조회</RouterLink>
                <RouterLink class="content-item" to="">출하지시서 등록</RouterLink>
                <RouterLink class="content-item" to="">출하지시서 현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('warehouse')">출하</span>
            <ul v-if="activeMenu2 === 'warehouse'">
              <li>
                <RouterLink class="content-item" to="">출하전표 조회</RouterLink>
                <RouterLink class="content-item" to="">출하전표 등록</RouterLink>
                <RouterLink class="content-item" to="">출하전표 현황</RouterLink>
              </li>
            </ul>
          </li>
        </ul>
      </li>
      <!-- 주문관리 -->
      <li>
        <span @click="toggleMenu('order')">주문관리</span>
        <ul v-if="activeMenu === 'order'">
          <li>
            <span @click="toggleMenu2('item')">발주서</span>
            <ul v-if="activeMenu2 === 'item'">
              <li>
                <RouterLink class="content-item" to="">발주서 조회</RouterLink>
                <RouterLink class="content-item" to="">발주서 입력</RouterLink>
                <RouterLink class="content-item" to="">발주서 현황</RouterLink>
                <RouterLink class="content-item" to="">미입고 현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('client')">구매</span>
            <ul v-if="activeMenu2 === 'client'">
              <li>
                <RouterLink class="content-item" to="">구매서 조회</RouterLink>
                <RouterLink class="content-item" to="">구매서 등록</RouterLink>
                <RouterLink class="content-item" to="">구매 현황</RouterLink>
              </li>
            </ul>
          </li>
        </ul>
      </li>
      <!-- 생산관리 -->
      <li>
        <span @click="toggleMenu('production')">생산관리</span>
        <ul v-if="activeMenu === 'production'">
          <li>
            <span @click="toggleMenu2('item')">작업지시서</span>
            <ul v-if="activeMenu2 === 'item'">
              <li>
                <RouterLink class="content-item" to="">작업지시서 조회</RouterLink>
                <RouterLink class="content-item" to="">작업지시서 등록</RouterLink>
                <RouterLink class="content-item" to="">작업지시서 작업처리</RouterLink>
                <RouterLink class="content-item" to="">작업지시서 진행현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('client')">생산불출</span>
            <ul v-if="activeMenu2 === 'client'">
              <li>
                <RouterLink class="content-item" to="">생산불출 조회</RouterLink>
                <RouterLink class="content-item" to="">생산불출 등록</RouterLink>
                <RouterLink class="content-item" to="">생산불출 현황</RouterLink>
              </li>
            </ul>
          </li>
          <li>
            <span @click="toggleMenu2('warehouse')">생산입고</span>
            <ul v-if="activeMenu2 === 'warehouse'">
              <li>
                <RouterLink class="content-item" to="">생산입고 조회</RouterLink>
                <RouterLink class="content-item" to="">생산입고 입력</RouterLink>
                <RouterLink class="content-item" to="">생산입고 현황</RouterLink>
              </li>
            </ul>
          </li>
        </ul>
      </li>
      <!-- 통계 -->
      <li>
        <span @click="toggleMenu('stats')">통계</span>
        <ul v-if="activeMenu === 'stats'">
          <li>
            <RouterLink class="content-item" to="">재고관리 현황</RouterLink>
          </li>
          <li>
            <RouterLink class="content-item" to="">영업관리 현황</RouterLink>
          </li>
          <li>
            <RouterLink class="content-item" to="">구매관리 현황</RouterLink>
          </li>
          <li>
            <RouterLink class="content-item" to="">기타 현황</RouterLink>
          </li>
        </ul>
      </li>
      </ul>
  </aside>
</template>

<style scoped>
.side-bar {
  position: fixed;
  bottom: 0;
  height: 86%;
  width: 40px;
  background-color: #fff8e7;
  overflow: hidden;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.side-bar.open {
  width: 200px;
}

.side-bar ul {
  margin: 0;
  padding: 0;
  list-style: none;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.side-bar.open ul {
  opacity: 1;
}

.side-bar ul > li > span {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #aaaaaa;
  text-decoration: none;
  padding: 10px 10px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.side-bar ul > li > span:hover {
  color: black;
}

.side-bar ul ul > li > span {
  display: block;
  font-size: 14px;
  font-weight: normal;
  color: #aaaaaa;
  padding: 4px 20px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.side-bar ul ul > li > span:hover {
  color: black;
}

.content-item {
  display: block;
  font-size: 10px;
  color: #aaaaaa;
  padding: 5px 20px;
  text-decoration: none;
  transition: color 0.3s ease;
}

.content-item:hover {
  color: black;
}
</style>
