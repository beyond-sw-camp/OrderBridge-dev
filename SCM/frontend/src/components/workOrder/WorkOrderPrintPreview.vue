<script setup>
import dayjs from 'dayjs';
import axios from "@/axios"
import { ref, watch } from 'vue';
import Swal from "sweetalert2";

const props = defineProps({
  isVisible: Boolean,
  workOrder: Object,
  isList: Boolean,
});

const seq = ref(null);

// 부모에서 넘어오는 작업지시서 시퀀스로 이미지를 가져옴
const notification  = ref('');
const fetchImages = async () => {

  try {
    // const response = await axios.get( `notification/workOrder/${props.workOrder.workOrderDetail.workOrderSeq}`);
    const response = await axios.get( `notification/workOrder/${seq.value}`);

    notification.value = response.data;
  } catch (error) {
    console.error("결재 서류 데이터를 가져오는 중 오류 발생:", error);
  }
}

// 작업지시서 데이터가 조회된 후 fetchImages 실행
watch(() => props.workOrder, (newVal) => {
  if (newVal && newVal.workOrderDetail?.workOrderSeq) {
    // if (newVal && newVal.ProductionDisbursementDetailDTO?.productionDisbursementSeq) {
    seq.value = props.workOrder.workOrderDetail.workOrderSeq
  // if (newVal && newVal.workOrderDetail.workOrderSeq) {
    fetchImages();
  }

}, { immediate: true });

const emit = defineEmits(['close']);

const closePrintModal = () => {
  emit('close');
};

const printPage = () => {
  const printContent = document.getElementById('print-area-workOrder').innerHTML;
  const originalContent = document.body.innerHTML; // 현재 페이지 내용 저장

  document.body.innerHTML = printContent;

  window.print();

  document.body.innerHTML = originalContent;

  location.reload();
}

const isModalOpen = ref(false);
const selectedNotification = ref(null);

// 드로잉 관련 변수
const canvasRef = ref(null);
const context = ref(null);
let isDrawing = false;
const openModal = (notification) => {
  selectedNotification.value = notification;
  isModalOpen.value = true;

  // 드로잉 캔버스 초기화
  setTimeout(() => {
    const canvas = canvasRef.value;
    context.value = canvas.getContext("2d");
    context.value.strokeStyle = "black";
    context.value.lineWidth = 2;
  });
};

const closeModal = () => {
  isModalOpen.value = false;
  selectedNotification.value = null;
};

const saveCanvas = async (selectedNotification) => {
  const canvas = canvasRef.value;
  if (!canvas) {
    throw new Error("캔버스를 찾을 수 없습니다.");
  }

  if(confirm("결재를 승인하시겠습니까?")) {
    const imageData = canvas.toDataURL("signImage/jpg");

    await axios.post("notification", {
      notificationSeq: selectedNotification.notificationSeq,
      notificationImageUrl: imageData
    });

    isModalOpen.value = false;
    emit('close');

    await Swal.fire({
      position: "center",
      icon: "success",
      title: "결재 승인이 완료되었습니다.",
      showConfirmButton: false,
      timer: 1500
    });

  }

  // 결재서류 상태 변경
  // console.log('상태변경',props.workOrder.workOrderDetail.workOrderSeq)
  console.log('결재변경:seq.value',seq.value)
  // await axios.put(`workOrder/approval/${props.workOrder.workOrderDetail.workOrderSeq}`);
  await axios.put(`workOrder/approval/${seq.value}`);
};

// 드로잉 시작
const startDrawing = (event) => {
  const canvas = canvasRef.value;
  const rect = canvas.getBoundingClientRect();
  isDrawing = true;
  context.value.beginPath();
  context.value.moveTo(event.clientX - rect.left, event.clientY - rect.top);
};

// 드로잉 중
const draw = (event) => {
  if (!isDrawing) return;
  const canvas = canvasRef.value;
  const rect = canvas.getBoundingClientRect();
  context.value.lineTo(event.clientX - rect.left, event.clientY - rect.top);
  context.value.stroke();
};

// 드로잉 종료
const stopDrawing = () => {
  if (!isDrawing) return;
  isDrawing = false;
  context.value.closePath();
};

// 캔버스 초기화
const clearCanvas = () => {
  const canvas = canvasRef.value;
  context.value.clearRect(0, 0, canvas.width, canvas.height);
};

</script>

<template>
  <!-- print Modal bootstrap -->
  <div v-show="isVisible" class="modal-overlay" @click.self="closePrintModal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-body" id="print-area-workOrder">
          <div class="d-flex justify-content-between">
            <button class="btn-print" @click="printPage">출력</button>
            <button type="button" class="btn-close btn-print" data-bs-dismiss="modal" aria-label="Close" @click="closePrintModal" ></button>
          </div>

          <div class="container mt-4">

            <h2 class="text-center">작업지시서 전표</h2>
            <br/><br/>
            <table class="info-table-eft" style="float: left;">
              <tbody>
              <tr>
                <td class="to-column" style="height: 30px;">작업지시일자 &nbsp; : &nbsp;</td>
                <td colspan="5" style="height: 30px;">{{ dayjs(workOrder?.workOrderDetail.workOrderIndicatedDate).format('YYYY-MM-DD') }}</td>

              </tr>
              <tr>
                <td style="height: 30px;">거래처명 &nbsp; : &nbsp;</td>
                <td colspan="5" style="height: 30px;"> &nbsp; {{ workOrder?.workOrderDetail.clientName!=null ? workOrder.workOrderDetail.clientName : '' }}</td>
              </tr>
              </tbody>
            </table>

            <table class="info-table-right" style="float: right; width: 50%; text-align: center; border-collapse: collapse;" border="1">
              <tbody>
              <tr>
                <td class="approval-header color-column" rowspan="2" colspan="4">결<br/>재<br/>란</td>
                <td class="color-column" colspan="5">담당</td>
                <td class="color-column" colspan="5">승인</td>
              </tr>
              <tr>
                <td colspan="5" style="height: 30px;">
                  {{ workOrder?.workOrderDetail.userName }}
                </td>
                <td colspan="5" style="height: 30px;" class="image-gallery">
                  <img class="image-item" v-if="notification.notificationImageUrl != undefined" :src="notification.notificationImageUrl" alt="승인자 서명" style="width: 100px; height: auto;" />
                  <span v-else>
                    (서명 또는 인)
                    <template v-if="!isList">
                     <input type="button" class="btn-print" @click="openModal(notification)" value="결재하기">
                    </template>
                  </span>


                </td>
              </tr>
              </tbody>
            </table>
            <br/><br/><br/>

            <table class="table first-table left" style="height: 140px">
              <tbody v-if="workOrder">
              <tr>
                <td class="color-column align-content-center">작업지시서명</td>
                <td class="align-content-center">{{ workOrder.workOrderDetail.workOrderName }}</td>
                <td class="color-column align-content-center">담당사</td>
                <td class="align-content-center">Order Bridge</td>
              </tr>
              <tr>
                <td class="color-column align-content-center">담당자</td>
                <td class="align-content-center">{{ workOrder.workOrderDetail.userName }}</td>
                <td class="color-column align-content-center">연락처</td>
                <td class="align-content-center">{{ workOrder.workOrderDetail.userPhoneNo }}</td>
              </tr>
              <tr>
                <td class="color-column align-content-center">작업 지시일</td>
                <td class="align-content-center">{{ dayjs(workOrder.workOrderDetail.workOrderIndicatedDate).format('YYYY-MM-DD') }}</td>
                <td class="color-column align-content-center">작업 목표일</td>
                <td class="align-content-center">{{ dayjs(workOrder.workOrderDetail.workOrderDueDate).format('YYYY-MM-DD') }}</td>
              </tr>
              <tr>
                <td class="color-column align-content-center">생산공장명</td>
                <td class="align-content-center" colspan="3">{{ workOrder.workOrderDetail.warehouseName }}</td>
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
              <tbody v-if="workOrder?.workOrderItem">
              <tr>
<!--              <tr v-for="(item, idx) in workOrder.workOrderItem"-->
<!--                  :key="item.itemSeq || idx">-->
                <td>{{ workOrder.workOrderItem.itemName }}</td>
                <td>{{ workOrder.workOrderDetail.workOrderIndicatedQuantity ? workOrder.workOrderDetail.workOrderIndicatedQuantity.toLocaleString() : 0 }}</td>
                <td>{{ workOrder.workOrderItem.itemPrice ? workOrder.workOrderItem.itemPrice.toLocaleString() : 0 }}</td>
                <td>{{ (workOrder.workOrderItem.itemPrice * workOrder.workOrderDetail?.workOrderIndicatedQuantity).toLocaleString() }}</td>
              </tr>
              </tbody>
            </table>

            <ul class="notes">
              <li>상기 품목을 지시하오니 목표를 준수하여 작업 바랍니다.</li>
              <li>기타 의문사항이나 관련사항시 사전 관련부서에 통보하여 주십시오.</li>
            </ul>

            <div class="footer-line"></div>
            <span style="float:right; padding-top:7px;"> (&nbsp;주&nbsp;) &nbsp; OrderBridge &nbsp;</span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 결재 싸인 모달  -->
  <div v-if="isModalOpen" class="signModal-overlay" @click.self="closeModal">
    <div class="signModal">
      <h3 style="text-align: center;">{{ selectedNotification?.notificationTitle }}</h3>

      <canvas
          ref="canvasRef"
          width="400"
          height="200"
          class="drawing-canvas"
          @mousedown="startDrawing"
          @mousemove="draw"
          @mouseup="stopDrawing"
          @mouseleave="stopDrawing"
      ></canvas>
      <div class="modal-buttons">
        <button @click="clearCanvas">초기화</button>
        <button @click="closeModal">닫기</button>
        <button @click="saveCanvas(selectedNotification)">저장</button>
      </div>
    </div>
  </div>

</template>

<style scoped>

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.footer-line {
  border-bottom: 2px solid black; /* 테이블 하단에 두꺼운 테두리 추가 */
}

.image-gallery {
  flex-wrap: wrap;
  gap: 10px;
}

.image-item {
  width: 100%;
  height: auto;
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
  overflow-y: scroll;
  z-index: 1000;
}

/* 모달 컨텐츠 */
.modal-content {
  background: #fff;
  border-radius: 2px;
  max-width: 900px; /* 넓이 확장 */
  width: 120%;
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


.signModal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001;
}

.signModal {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  max-width: 500px;
  width: 100%;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.drawing-canvas {
  border: 1px solid #ddd;
  display: block;
  margin: 20px auto;
  cursor: crosshair;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
}

</style>
