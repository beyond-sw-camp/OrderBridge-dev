<script setup>
import { ref, onMounted, computed } from 'vue';

const tableTitle = ref('견적서');

const fileInfoTableData = ref([
  { fileName: '견적서명', fileNameData: '2024/12/03 - 01', date: '견적일' ,dateData: '2024/12/13'
    , client: '거래처', clientData: '대한항공씨앤디', user: '담당자', userData: '박지훈',
  price: '총액', priceData: '300,000원' }
]);

const itemInfoTableData = ref([
  { name: "김치볶음밥", quantity: 1597, unitPrice: 1092000, price: 109200015},
  { name: "치즈볶음밥", quantity: 3525, unitPrice: 9056030, price: 905603035},
  { name: "그냥볶음밥", quantity: 459831, unitPrice: 10002000, price: 1000200031},
]);

// 본인 데이터 직접 불러오기
const fetchData = async () => {
  try {
    const response = await fetch('https://localhost:8080/printPreview'); // URL 연결
    const data = await response.json();
    console.log(data);

    // 받아오는 데이터로 출력물 구성
    /*if (data.length > 0) {
      tableHeaders.value = Object.keys(data[0]);
      tableData.value = data;
    }*/
  } catch (error) {
    console.error('데이터를 가져오는 데 실패했습니다:', error);
  }
};

// 빈칸을 계산하는 computed 속성
const emptyRows = computed(() => {
  const totalRows = 10; // 최소 10행을 유지
  const currentDataCount = itemInfoTableData.value.length;
  return currentDataCount < totalRows ? totalRows - currentDataCount : 0;
});

const totalQuantity = computed(() => {
  return itemInfoTableData.value.reduce((sum, product) => sum + product.quantity, 0);
});

const totalUnitPrice = computed(() => {
  return itemInfoTableData.value.reduce((sum, product) => sum + product.price, 0);
});

const totalPrice = computed(() => {
  return itemInfoTableData.value.reduce((sum, product) => sum + product.price * product.quantity, 0);
});

const printPage = () => {
  const printContent = document.getElementById('print-area').innerHTML; // 특정 영역 가져오기
  const originalContent = document.body.innerHTML; // 현재 페이지 내용 저장

  // 페이지 내용을 특정 영역으로 교체
  document.body.innerHTML = printContent;

  // 인쇄
  window.print();

  // 원래 내용 복원
  document.body.innerHTML = originalContent;

  // SPA일 경우 Vue의 리렌더링 강제 호출
  location.reload(); // 상태를 새로고침하여 업데이트
}

onMounted(() => {
  fetchData();
});
</script>

<template>
  <!-- print Modal bootstrap -->
  <div class="modal fade" id="PrintModal" tabindex="-1" aria-labelledby="PrintModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-body" id="print-area">
          <!-- 인쇄 미리보기 버튼 -->
          <div class="d-flex justify-content-between">
            <button @click="printPage">인쇄 미리보기</button>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>

          <div class="container mt-4">

            <h2 class="text-center">{{ tableTitle }}</h2>
            <br/><br/>

            <table class="table first-table" style="height: 140px">
              <tbody v-for="fileInfo in fileInfoTableData">
              <tr>
                <td class="align-content-center">{{ fileInfo.fileName }}</td>
                <td class="color-column align-content-center">{{ fileInfo.fileNameData }}</td>
                <td class="align-content-center">{{ fileInfo.date }}</td>
                <td class="color-column align-content-center">{{ fileInfo.dateData }}</td>
              </tr>
              <tr>
                <td class="align-content-center">{{ fileInfo.client }}</td>
                <td class="color-column align-content-center">{{ fileInfo.clientData }}</td>
                <td class="align-content-center">{{ fileInfo.user }}</td>
                <td class="color-column align-content-center">{{ fileInfo.userData }}</td>
              </tr>
              <tr>
                <td class="align-content-center">{{ fileInfo.price }}</td>
                <td class="color-column align-content-center" colspan="3">{{ fileInfo.priceData.toLocaleString() }}</td>
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
              <tbody>
              <tr v-for="(itemInfo, index) in itemInfoTableData" :key="'data-' + index">
                <td>{{ itemInfo.name }}</td>
                <td>{{ itemInfo.quantity.toLocaleString() }}</td>
                <td>{{ itemInfo.unitPrice.toLocaleString() }}</td>
                <td>{{ itemInfo.price.toLocaleString() }}</td>
              </tr>
              <tr v-for="index in emptyRows" :key="'empty-' + index">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              </tbody>
              <tfoot>
              <tr>
                <td>합계</td>
                <td>{{ totalQuantity.toLocaleString() }}</td>
                <td>{{ totalUnitPrice.toLocaleString() }}</td>
                <td>{{ totalPrice.toLocaleString() }}</td>
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
.table {
  text-align: center;
  border-collapse: collapse;
}

.color-column {
  background-color: #f5f5f5;
  border-radius: 15px;
}

/* 인쇄 시 적용되는 스타일 */
@media print {
  @page {margin:0 1.3cm}

  body {
    font-family: 'Abhaya Libre', serif;
    font-size: 12px;
    color-adjust: exact;
  }

  .content-to-print {
    padding: 0;
    border: none;
  }

  /* 인쇄 미리보기에서 버튼 숨기기 - 꼭 있어야함!!!! */
  button {
    display: none;
  }
}
</style>
