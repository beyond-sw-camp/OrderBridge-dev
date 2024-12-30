<script setup>
import dayjs from 'dayjs';

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
            <button class="btn-print" @click="printPage">출력</button>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" @click="closeModal" ></button>
          </div>

          <div class="container mt-4">

            <h2 class="text-center">발주서</h2>
            <br/><br/>
            <table class="info-table-eft" style="float: left;">
              <tbody>
              <tr>
                <td class="to-column" style="height: 30px;">발주일자 &nbsp; : &nbsp;</td>
                <td colspan="5" style="height: 30px;">&nbsp;20&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;년 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;월 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;일</td>
              </tr>
              <tr>
                <td style="height: 30px;">거래처명 &nbsp; : &nbsp;</td>
                <td colspan="5" style="height: 30px;"> &nbsp; {{ purchaseOrder?.clientName!=null ? purchaseOrder.clientName : '' }}</td>
              </tr>
              </tbody>
            </table>

            <table class="info-table-right" style="float: right; width: 50%; text-align: center; border-collapse: collapse;" border="1">
              <tbody>
              <tr>
                <td class="approval-header color-column" rowspan="2" colspan="4">결재란</td>
                <td class="color-column" colspan="5">담당</td>
                <td class="color-column" colspan="5">승인</td>
              </tr>
              <tr>
                <td colspan="5" style="height: 30px;"></td>
                <td colspan="5" style="height: 30px;"></td>
              </tr>
              </tbody>
            </table>
            <br/><br/><br/>

            <table class="table first-table left" style="height: 140px">
              <tbody v-if="purchaseOrder">
              <tr>
                <td class="color-column align-content-center">발주서명</td>
                <td class="align-content-center">{{ purchaseOrder.purchaseOrderName }}</td>
                <td class="color-column align-content-center">담당사</td>
                <td class="align-content-center">Order Bridge</td>
              </tr>
              <tr>
                <td class="color-column align-content-center">담당자</td>
                <td class="align-content-center">{{ purchaseOrder.userName }}</td>
                <td class="color-column align-content-center">연락처</td>
                <td class="align-content-center">{{ purchaseOrder.userPhoneNo }}</td>
              </tr>
              <tr>
                <td class="color-column align-content-center">주소</td>
                <td class="align-content-center" colspan="3">서울특별시 동작구 보라매로 87 플레이데이터 3층</td>
              </tr>
              <tr>
                <td class="color-column align-content-center">계약 납기일</td>
                <td class="align-content-center" colspan="3">{{ dayjs(purchaseOrder.purchaseOrderDueDate).format('YYYY-MM-DD') }}</td>
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
                <td>{{ purchaseOrderItem.purchaseOrderItemQuantity ? purchaseOrderItem.purchaseOrderItemQuantity.toLocaleString() : 0 }}</td>
                <td>{{ purchaseOrderItem.purchaseOrderItemPrice ? purchaseOrderItem.purchaseOrderItemPrice.toLocaleString() : 0 }}</td>
                <td>{{ (purchaseOrderItem.purchaseOrderItemQuantity * purchaseOrderItem.purchaseOrderItemPrice).toLocaleString() }}</td>
              </tr>
              </tbody>
              <tfoot>
              <tr>
                <td>합계</td>
                <td>{{ purchaseOrder?.purchaseOrderTotalQuantity ? purchaseOrder.purchaseOrderTotalQuantity.toLocaleString() : 0 }}</td>
                <td> - </td>
                <td>{{ purchaseOrder?.purchaseOrderExtendedPrice ? purchaseOrder.purchaseOrderExtendedPrice.toLocaleString() : 0 }}</td>
              </tr>
              </tfoot>
            </table>

            <ul class="notes">
              <li>상기 자재를 발주하오니 납기를 준수하여 입고 바랍니다.</li>
              <li>기타 의문사항이나 관련사항시 사전 관련부서에 통보하여 주십시오.</li>
            </ul>

            <div class="footer-line"></div>
            <span style="float:right; padding-top:7px;"> (&nbsp;주&nbsp;) &nbsp; OrderBridge &nbsp;</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.footer-line {
  border-bottom: 2px solid black; /* 테이블 하단에 두꺼운 테두리 추가 */
}

.approval-header {
  width: 10%;
  writing-mode: horizontal-tb;
  text-orientation: upright;
  font-size: 12px;
  vertical-align: middle;
}

.info-table-right td {
  border: 1px solid black;
}

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

.notes {
  margin-top: 20px;
  padding-left: 20px;
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
    display: none;
  }

  .btn-print {
    display: none;
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
