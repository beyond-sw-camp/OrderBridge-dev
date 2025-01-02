<script setup>
import chatbotIcon from '@/assets/chatbotIcon.svg';
import notificationIcon from '@/assets/notificationIcon.svg';
import myPageIcon from '@/assets/myPageIcon.svg';
import basicIcon from '@/assets/basicIcon.svg';
import salesIcon from '@/assets/salesIcon.svg';
import productionIcon from '@/assets/productionIcon.svg';
import orderIcon from '@/assets/orderIcon.svg';
import statisticsIcon from '@/assets/statisticsIcon.svg'
import {useUserStore} from "@/stores/UserStore.js";
import dayjs from 'dayjs';
import { ref } from "vue";
import axios from "@/axios";
import Chatbot from "@/components/common/Chatbot.vue";
import PurchaseOrderPrintPreviewModal from "@/components/purchaseOrder/PurchaseOrderPrintPreview.vue";
import PurchasePrintPreviewModal from "@/components/purchase/PurchasePrintPreview.vue";
import ShippingInstructionPrintPreview from "@/components/shippingInstruction/ShippingInstructionPrintPreview.vue";
import ShippingSlipPrintPreview from "@/components/shippingSlip/ShippingSlipPrintPreview.vue";
import ProductionDisbursementPrintPreview from "@/components/productionDisbursement/ProductionDisbursementPrintPreview.vue";
import WorkOrderPrintPreview from "@/components/workOrder/WorkOrderPrintPreview.vue";

const userStore = useUserStore();
const notificationList = ref([]);
const isNotificationOpen = ref(false);

const fetchNotifications = async () => {
  try {
    const response = await axios.get(`notification`);

    notificationList.value = response.data;
    isNotificationOpen.value = true;

  } catch (error) {
    console.error("알림 데이터를 가져오는 중 오류 발생:", error);
  }
};

const isModalVisible = ref(false);
const selectedData = ref(null);
const selectedNotificationType = ref(null);

const openPrintPreview = async (notification) => {
  try {
    // 엔드포인트 변경을 위해 사용
    if (notification.notificationType === 'shippingInstruction') {
      notification.notificationType = 'shipping-instruction';
    } else if(notification.notificationType === 'shippingSlip') {
      notification.notificationType = 'shipping-slip';
    }

    const response = await axios.get(notification.notificationType + `/${notification.notificationAnotherSeq}`);

    selectedNotificationType.value = notification.notificationType;
    selectedData.value = response.data;
    console.log("response data : ", response.data);
    isModalVisible.value = true;
    isNotificationOpen.value = false;

  } catch (error) {
    console.error("결재 서류 데이터를 가져오는 중 오류 발생:", error);
  }
}

const closePrintPreview = () => {
  isModalVisible.value = false;
  selectedData.value = null;
};

const chatbot = ref(null);

function chatbotOn() {
  if (chatbot.value) {
    chatbot.value = null;
  } else {
    chatbot.value = "display: block";
  }
}
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
              <li><RouterLink to="/warehouse" class="dropdown-item">창고 관리</RouterLink></li>
              <li><RouterLink to="/client" class="dropdown-item">거래처 관리</RouterLink></li>
              <li><RouterLink to="/item" class="dropdown-item">품목 관리</RouterLink></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link" href="" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <salesIcon class="icon"/>영업관리
            </a>
            <ul class="dropdown-menu">
              <li><RouterLink to="/quotation" class="dropdown-item">견적 관리</RouterLink></li>
              <li><RouterLink to="/sales-order" class="dropdown-item">주문서 관리</RouterLink></li>
              <li><RouterLink to="/invoice" class="dropdown-item">판매 관리</RouterLink></li>
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
          <li class="nav-item"><RouterLink to="#" class="nav-link" @click.prevent="fetchNotifications"><notificationIcon class="icon-right"/></RouterLink></li>
          <li class="nav-item" @click="chatbotOn()"><RouterLink to="#" class="nav-link"><chatbotIcon class="icon-right"/></RouterLink></li>
          <li class="nav-item"><RouterLink to="#" class="nav-link"><myPageIcon class="icon-right"/></RouterLink></li>
          <li class="nav-item" @click="userStore.logout()"><RouterLink to="#" class="nav-link"><!--<logoutIcon class="icon-right"/>-->로그아웃</RouterLink></li>
        </ul>
        <div id="chatbot" v-bind:style="chatbot">
          <chatbot />
        </div>
      </div>
  </nav>

<!-- 알림 모달  -->
  <div v-if="isNotificationOpen" class="notification-bar">
    <ul v-if="notificationList.length > 0">
      <li v-for="notification in notificationList" :class="{ 'selected-notification': notification.notificationReadYn === 'Y' }" :key="notification.notificationSeq" @click="openPrintPreview(notification)">
        <span>{{ notification.notificationTitle }}</span>
        <span style="float:right;">{{ dayjs(notification.notificationRegDate).format('YYYY/MM/DD HH:mm') }}</span>
        <br/>
        <span v-html="notification.notificationContent"></span>
      </li>
    </ul>
    <ul v-else>
      <li>
      조회된 알림이 없습니다.
      </li>
    </ul>
    <button @click="isNotificationOpen = false">닫기</button>
  </div>

  <template v-if="selectedNotificationType === 'purchaseOrder'">
    <PurchaseOrderPrintPreviewModal
        :isVisible="isModalVisible"
        :purchaseOrder="selectedData"
        :isList=false
        @close="closePrintPreview"
    />
  </template>
  <template v-else-if="selectedNotificationType === 'shipping-instruction'">
    <ShippingInstructionPrintPreview
        :isVisible="isModalVisible"
        :shippingInstruction="selectedData"
        :isList=false
        @close="closePrintPreview"
    />
  </template>
  <template v-else-if="selectedNotificationType === 'shipping-slip'">
    <ShippingSlipPrintPreview
        :isVisible="isModalVisible"
        :shippingSlip="selectedData"
        :isList=false
        @close="closePrintPreview"
    />
  </template>
  <template v-else-if="selectedNotificationType === 'purchase'">
    <PurchasePrintPreviewModal
        :isVisible="isModalVisible"
        :purchase="selectedData"
        @close="closePrintPreview"
    />
  </template>
  <template v-else-if="selectedNotificationType === 'productionDisbursement'">
    <ProductionDisbursementPrintPreview
        :isVisible="isModalVisible"
        :productionDisbursement="selectedData"
        :isList=false
        @close="closePrintPreview"
    />
  </template>
  <template v-else-if="selectedNotificationType === 'workOrder'">
    <WorkOrderPrintPreview
        :isVisible="isModalVisible"
        :workOrder="selectedData"
        :isList=false
        @close="closePrintPreview"
    />
  </template>

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

.selected-notification {
  background-color: #f0f8ff; /* 밝은 파란색 */
  border-left: 4px solid #007bff; /* 강조를 위한 왼쪽 테두리 */
}

.notification-bar {
  position: fixed;
  top: 50px;
  right: 10px;
  width: 300px;
  max-height: 400px;
  background-color: white;
  border: 1px solid #ddd;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  z-index: 1002;
  padding: 10px;
}
.notification-bar ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
.notification-bar li {
  padding: 8px 10px;
  border-bottom: 1px solid #f0f0f0;
}
.notification-bar button {
  display: block;
  margin: 10px auto;
  padding: 5px 10px;
  border: none;
  background-color: #333;
  color: white;
  cursor: pointer;
}

body {
  font-family: "Open Sans", -apple-system, BlinkMacSystemFont, "Segoe UI",
  Roboto, Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", Helvetica, Arial,
  sans-serif;
}

#chatbot {
  display: none;
  position: absolute;
  padding: 10px;
  top: 100px;
  right: 5px;
  width: 400px;
  height: 500px;
  border: solid 2px silver;
  border-radius: 10px;
  backdrop-filter: blur(5px);
}
</style>
