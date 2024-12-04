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
  window.print();
}

onMounted(() => {
  fetchData();
});

</script>

<template>
  <div>
    <!-- 인쇄 미리보기 버튼 -->
    <button @click="printPage">인쇄 미리보기</button>

    <div class="container mt-4">

      <h2 style="text-align: center">{{ tableTitle }}</h2>
      <br /><br />

      <table class="table first-table" style="height: 140px">
        <tbody  v-for="fileInfo in fileInfoTableData">
          <tr>
            <td>{{ fileInfo.fileName }}</td>
            <td class="color-column">{{ fileInfo.fileNameData }}</td>
            <td>{{ fileInfo.date }}</td>
            <td class="color-column">{{ fileInfo.dateData }}</td>
          </tr>
          <tr>
            <td>{{ fileInfo.client }}</td>
            <td class="color-column">{{ fileInfo.clientData }}</td>
            <td>{{ fileInfo.user }}</td>
            <td class="color-column">{{ fileInfo.userData }}</td>
          </tr>
          <tr>
            <td>{{ fileInfo.price }}</td>
            <td class="color-column" colspan="3">{{ fileInfo.priceData.toLocaleString() }}</td>
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
          <tr v-for="itemInfo in itemInfoTableData">
            <td>{{ itemInfo.name }}</td>
            <td>{{ itemInfo.quantity.toLocaleString() }}</td>
            <td>{{ itemInfo.unitPrice.toLocaleString() }}</td>
            <td>{{ itemInfo.price.toLocaleString() }}</td>
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
  body {
    font-family: 'Abhaya Libre', serif;
    font-size: 12px;
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
