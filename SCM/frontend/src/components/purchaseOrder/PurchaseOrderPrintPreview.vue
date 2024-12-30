<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  isVisible: Boolean,
  purchaseOrder: Object,
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};

const printPage = () => {
  const printContent = document.getElementById('print-area').innerHTML;
  const originalContent = document.body.innerHTML; // 현재 페이지 내용 저장

  document.body.innerHTML = printContent;

  window.print();

  document.body.innerHTML = originalContent;

  location.reload();
}

</script>

<template>
  <!-- print Modal bootstrap -->
    <div v-show="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-body" id="print-area">
          <div class="d-flex justify-content-between">
            <button @click="printPage">출력</button>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal" ></button>
          </div>

          <div class="container mt-4">

            <h2 class="text-center">발주서</h2>
            <br/><br/>

            <table class="table first-table" style="height: 140px">
              <tbody v-if="purchaseOrder">
              <tr>
                <td class="align-content-center">발주서명</td>
                <td class="color-column align-content-center">{{ purchaseOrder.purchaseOrderName }}</td>
                <td class="align-content-center">purchaseOrder.date</td>
                <td class="color-column align-content-center">purchaseOrder.dateData</td>
              </tr>
              <tr>
                <td class="align-content-center">거래처</td>
                <td class="color-column align-content-center">{{ purchaseOrder.clientName }}</td>
                <td class="align-content-center">담당자</td>
                <td class="color-column align-content-center">{{ purchaseOrder.userName }}</td>
              </tr>
              <tr>
                <td class="align-content-center">가격</td>
                <td class="color-column align-content-center" colspan="3">{{ purchaseOrder.purchaseOrderExtendedPrice.toLocaleString() }}</td>
              </tr>
              </tbody>
            </table>

            <table class="table table-bordered second-table">
              <thead>
              <tr>
                <th>품목</th>
                <th>수량</th>
                <th>단가</th>
                <th>금액</th>
              </tr>
              </thead>
              <tbody v-if="purchaseOrder?.purchaseOrderItemResponseList?.length > 0">
              <tr v-for="(purchaseOrderItem, idx) in purchaseOrder.purchaseOrderItemResponseList"
                  :key="purchaseOrderItem.itemSeq || idx">
                <td>{{ purchaseOrderItem.itemName }}</td>
                <td>{{ purchaseOrderItem.purchaseOrderItemQuantity.toLocaleString() }}</td>
                <td>{{ purchaseOrderItem.purchaseOrderItemPrice.toLocaleString() }}</td>
                <td>{{ (purchaseOrderItem.purchaseOrderItemQuantity * purchaseOrderItem.purchaseOrderItemPrice).toLocaleString() }}</td>
              </tr>
              </tbody>
              <tfoot>
              <tr>
                <td>합계</td>
                <td>{{ purchaseOrder.purchaseOrderTotalQuantity.toLocaleString() }}</td>
                <td> - </td>
                <td>{{ purchaseOrder.purchaseOrderExtendedPrice.toLocaleString() }}</td>
              </tr>
              </tfoot>
            </table>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 모달 오버레이 */
.modal-overlay {
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

/* 모달 컨텐츠 */
.modal-content {
  background: #fff;
  border-radius: 2px;
  max-width: 900px; /* 넓이 확장 */
  width: 90%;
  padding: 30px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  position: relative;
  animation: fadeIn 0.3s ease-in-out;
}

/* 모달 헤더 */
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid #ddd; /* 더 두꺼운 구분선 */
  margin-bottom: 20px;
  padding-bottom: 10px;
}

.modal-header h5 {
  font-size: 1.5rem;
  font-weight: bold;
}

.modal-close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #888;
}

.modal-close-btn:hover {
  color: #000;
}

/* 버튼 스타일 */
button {
  padding: 10px 20px;
  font-size: 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 테이블 */
.table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  text-align: center;
  font-size: 0.9rem;
}

.table th {
  background-color: #f8f9fa;
  color: #333;
  padding: 10px;
  border: 1px solid #ddd;
}

.table td {
  border: 1px solid #ddd;
  padding: 8px;
  word-wrap: break-word; /* 내용이 긴 경우 줄바꿈 */
}

.color-column {
  background-color: #f5f5f5;
  border-radius: 8px;
  font-weight: bold;
}

/* 인쇄 시 적용되는 스타일 */
@media print {
  @page { margin: 0.5cm; }

  body {
    font-family: 'Abhaya Libre', serif;
    font-size: 12px;
  }

  .modal-overlay,
  .modal-header button {
    display: none; /* 버튼 숨기기 */
  }

  .modal-content {
    box-shadow: none;
    border: none;
    padding: 0;
  }

  .table th, .table td {
    border: 1px solid #000; /* 인쇄 시 더 선명한 테두리 */
  }
}

/* 애니메이션 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
