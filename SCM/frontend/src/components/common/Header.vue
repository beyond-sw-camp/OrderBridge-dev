<script setup>
import chatbotIcon from '@/assets/chatbotIcon.svg';
import notificationIcon from '@/assets/notificationIcon.svg';
import myPageIcon from '@/assets/myPageIcon.svg';
import basicIcon from '@/assets/basicIcon.svg';
import salesIcon from '@/assets/salesIcon.svg';
import productionIcon from '@/assets/productionIcon.svg';
import orderIcon from '@/assets/orderIcon.svg';
import statisticsIcon from '@/assets/statisticsIcon.svg'
import logoutIcon from '@/assets/logoutIcon.svg';
import {useUserStore} from "@/stores/UserStore.js";

const userStore = useUserStore();
</script>

<template>
  <nav class="navbar header">
    <div class="container-fluid">
      <RouterLink to="/" class="navbar-brand">
        <img src="@/images/logo.png" alt="OrderBridge Logo" class="logo-img" />
      </RouterLink>
        <ul class="navbar-nav me-auto mb-2 mb-lg-0 d-flex flex-row left-item">
          <li class="nav-item dropdown">
            <a class="nav-link" href="" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <basicIcon class="icon"/>기본등록
            </a>
            <ul class="dropdown-menu">
              <li><RouterLink to="warehouse" class="dropdown-item">창고 관리</RouterLink></li>
              <li><RouterLink to="client" class="dropdown-item">거래처 관리</RouterLink></li>
              <li><RouterLink to="/item" class="dropdown-item">품목 관리</RouterLink></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link" href="" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <salesIcon class="icon"/>영업관리
            </a>
            <ul class="dropdown-menu">
              <li><RouterLink to="quotation" class="dropdown-item">견적 관리</RouterLink></li>
              <li><RouterLink to="sales-order" class="dropdown-item">주문서 관리</RouterLink></li>
              <li><RouterLink to="invoice" class="dropdown-item">판매 관리</RouterLink></li>
              <li><RouterLink to="/shipping-instruction" class="dropdown-item">출하지시서 관리</RouterLink></li>
              <li><RouterLink to="/shipping-slip" class="dropdown-item">출하 관리</RouterLink></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link" href="" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <orderIcon class="icon"/>주문관리
            </a>
            <ul class="dropdown-menu">
              <li><RouterLink to="/purchaseOrder" class="dropdown-item">발주서 관리</RouterLink></li>
              <li><RouterLink to="/purchase" class="dropdown-item">구매서 관리</RouterLink></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link" href="" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <productionIcon class="icon"/>생산관리
            </a>
            <ul class="dropdown-menu">
              <li><RouterLink to="/work-order" class="dropdown-item">작업지시서 관리</RouterLink></li>
              <li><RouterLink to="/production-disbursement" class="dropdown-item">생산불출 관리</RouterLink></li>
              <li><RouterLink to="/productionReceiving" class="dropdown-item">생산입고 관리</RouterLink></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link" href="" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <statisticsIcon class="icon"/>통계
            </a>
            <ul class="dropdown-menu">
              <li><RouterLink to="" class="dropdown-item">재고관리 현황</RouterLink></li>
              <li><RouterLink to="" class="dropdown-item">영업관리 현황</RouterLink></li>
              <li><RouterLink to="" class="dropdown-item">구매관리 현황</RouterLink></li>
              <li><RouterLink to="/productionReceiving/situation" class="dropdown-item">생산입고 현황</RouterLink></li>
              <li><RouterLink to="" class="dropdown-item">기타 현황</RouterLink></li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav mb-lg-0 d-flex flex-row">
          <li class="nav-item"><RouterLink to="#" class="nav-link"><chatbotIcon class="icon-right"/></RouterLink></li>
          <li class="nav-item"><RouterLink to="#" class="nav-link"><notificationIcon class="icon-right"/></RouterLink></li>
          <li class="nav-item"><RouterLink to="#" class="nav-link"><myPageIcon class="icon-right"/></RouterLink></li>
          <li class="nav-item" @click="userStore.logout()"><RouterLink to="#" class="nav-link"><!--<logoutIcon class="icon-right"/>-->로그아웃</RouterLink></li>
        </ul>
      </div>
  </nav>
</template>
<style scoped>
@media (max-width: 768px) {
  .left-item {
    display: none !important;
  }
}

.dropdown {
  padding-left: 20px;
}

.dropdown-menu {
  position: absolute;
}

.logo-img {
  height: 36px; /* 로고 크기 */
}

.icon {
  margin-right: 8px; /* 아이콘과 텍스트 간 간격 */
  width: 20px;
  height: 20px;
  fill: #333; /* 아이콘 색상 */
}

.icon-right {
  margin-right: 8px; /* 아이콘과 텍스트 간 간격 */
  width: 30px;
  height: 30px;
  fill: #333; /* 아이콘 색상 */
}

.header {
  background-color: #FFF9EA;
  border-bottom: solid 1px silver;
  width: 100%;
  position: fixed;
  top: 0px;
  z-index: 6;
}
</style>
