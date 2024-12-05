<script setup>
import {computed, ref} from 'vue';

const startDate = ref('');
const endDate = ref('');
const searchText = ref('');

// 테이블 데이터
const items = ref([
  { date: '2024-10-24', document: '2024/10/24 - 1', quantity: 200, unitPrice: 10000, total: 2000000, category: '제품', client: '대한항공', remarks: '피자맛나요' },
  { date: '2024-10-24', document: '2024/10/24 - 2', quantity: 300, unitPrice: 10000, total: 3000000, category: '제품', client: '대한항공', remarks: '치킨맛나요' },
  { date: '2024-11-24', document: '2024/11/24 - 1', quantity: 100, unitPrice: 3000, total: 300000, category: '제품', client: '대한항공', remarks: '김치와 밥의 조화' },
  { date: '2024-11-24', document: '2024/11/24 - 2', quantity: 50, unitPrice: 5000, total: 250000, category: '제품', client: '대한항공', remarks: '치즈와 밥의 조화' },
  { date: '2024-11-24', document: '2024/11/24 - 3', quantity: 500, unitPrice: 5000, total: 250000, category: '제품', client: '대한항공', remarks: '치즈와 밥의 조화' },
  { date: '2024-11-27', document: '2024/11/27 - 1', quantity: 500, unitPrice: 2000, total: 1000000, category: '제품', client: '아시아나항공', remarks: '소시지' },
  { date: '2024-11-27', document: '2024/11/27 - 2 ', quantity: 500, unitPrice: 2000, total: 1000000, category: '제품', client: '아시아나항공', remarks: '맛있다' },
]);

// 필터링된 데이터 계산
const filteredItems = computed(() => {
  return items.value.filter(item => {
    const inDateRange =
        (!startDate.value || new Date(item.date) >= new Date(startDate.value)) &&
        (!endDate.value || new Date(item.date) <= new Date(endDate.value));
    const matchesText = !searchText.value || item.client.includes(searchText.value);
    return inDateRange && matchesText;
  });
});

// 월별 합계 계산
const monthlyItems = computed(() => {
  const grouped = {}; // 월별 그룹화
  const result = [];  // 최종 결과 배열

  // 데이터를 월별로 그룹화
  filteredItems.value.forEach(item => {
    const month = item.date.slice(0, 7); // "YYYY-MM" 형식
    if (!grouped[month]) grouped[month] = {
      items: [], quantity: 0, total: 0
    };
    grouped[month].items.push(item);
    grouped[month].quantity += item.quantity;
    grouped[month].total += item.total;
  });

  // 그룹화된 데이터를 출력 형식으로 재구성
  Object.keys(grouped).forEach(month => {
    result.push(...grouped[month].items);
    result.push({
      monthlyTotal: true, // 합계 여부
      date: month, // 월
      quantity: grouped[month].quantity,
      total: grouped[month].total,
    });
  });

  return result;
});

// 총합 계산
const grandTotal = computed(() => {
  return filteredItems.value.reduce(
      (acc, item) => {
        acc.quantity += item.quantity;
        acc.total += item.total;
        return acc;
      },
      { quantity: 0, total: 0 }
  );
});
</script>

<template>
  <div class="container">
    <h1>현황</h1>
    <br><br>
    <div class="content">
      <div class="searchContent">
        <div class="searchDate">
          <h3>○○일</h3>
          <!-- 날짜 선택 -->
          <!--      <label>시작 날짜:</label>-->
          <input type="date" v-model="startDate" />
          ~
          <!--      <label>종료 날짜:</label>-->
          <input type="date" v-model="endDate" />
        </div>
        <br>
        <div class="searchClient">
          <h3>거래처명</h3>
          <div class="search-input">
            <input type="search" v-model="searchText">
            <img src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png" alt="search-icon">
          </div>
        </div>
      </div>
      <div class="table-container">
        <div class="selectedData">
          <p> {{ startDate }} ~ {{ endDate }}</p>
          <p>검색어: {{ searchText }}</p>
        </div>

        <div>
          <!-- 테이블 -->
          <table>
            <thead>
            <tr>
              <th>번호</th>
              <th>○○일</th>
              <th>○○내역(품목)</th>
              <th>총수량</th>
              <th>단가</th>
              <th>총금액</th>
              <th>구분(○○일)</th>
              <th>거래처명</th>
              <th>비고</th>
            </tr>
            </thead>
            <tbody>
            <!-- 필터링된 결과 및 월별 합계 출력 -->
            <tr
                v-for="(item, index) in monthlyItems"
                :key="index"
                :class="{ 'monthly-total': item.monthlyTotal }"
            >
                <td>{{ index+1 }}</td>
                <td>{{ item.date }}</td>
                <td>{{ item.document }}</td>
                <td>{{ item.quantity }}</td>
                <td>{{ item.unitPrice }}</td>
                <td>{{ item.total }}</td>
                <td>{{ item.category }}</td>
                <td>{{ item.client }}</td>
                <td>{{ item.remarks }}</td>
              </tr>
            </tbody>
            <!-- 총합 -->
            <tfoot>
            <tr>
              <td colspan="3">총합</td>
              <td>{{ grandTotal.quantity }}</td>
              <td colspan="2">{{ grandTotal.total.toLocaleString() }} 원</td>
              <td colspan="3"></td>
            </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.content{
  display: flex;
  justify-content: space-evenly;  /*내부 균등하게 분배*/
}

.searchContent{
  width: 25%;
  padding: 10px;
  border-radius: 5px;
}

.table-container {
  width: 70%;
  overflow-x: auto; /* 가로 자동 스크롤 */
}

.selectedData{
  text-align: right;
}

.searchDate{
  border: solid 1px #AAAAAA;
  border-radius: 10px;
  padding: 15px;
}

.searchClient{
  border: solid 1px #AAAAAA;
  border-radius: 10px;
  padding: 15px;
}

.search-input {
  position: relative;
  width: 200px;
}

.search-input>input {
  width: 100%;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  padding: 10px 12px;
  font-size: 14px;
}

.search-input>img {
  position : absolute;
  width: 17px;
  top: 10px;
  right: 12px;
  margin: 0;
}

.table-container{
  border: 1px solid #000000;
}

table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}

th, td {
  border: 1px solid #ccc;
  padding: 10px;
}

tfoot {
  font-weight: bold;
}

.monthly-total {
  font-weight: bold;
  background-color: #f9f9f9;
}

</style>