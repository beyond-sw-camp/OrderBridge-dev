<script setup>
import PurchaseInputForm from "@/components/purchaseOrder/PurchaseOrderInputForm.vue";
import PurchaseInputItems from "@/components/purchaseOrder/PurchaseOrderInputItems.vue";
import {onMounted, ref} from "vue";
import axios from "axios";
import router from "@/router/index.js";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const salesOrderList = ref([]);
const registerListener = ref(false);

const itemList = ref([]);
const selectedSalesOrder = ref(false);

// 자식으로 부터 데이터 받아옴
const childRef = ref(null);

const fetchSalesOrderList = async () => {
  try {
    const response = await axios.post(`http://localhost:8090/api/v1/purchaseOrder`, {
      params: {
        startDate: null,
        endDate: null,
        clientName: null,
        purchaseOrderStatus: 'APPROVAL_BEFORE',
        page: pageNumber.value,
        size: pageSize.value
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response);
    salesOrderList.value = response.data.salesOrder;
    totalCount.value = response.data.totalCount;

  } catch (error) {
    console.error("주문서 불러오기 실패 :", error);
  }
};

const fetchSalesOrder = async (salesOrderSeq) => {
  try {
    const response = await axios.get(`http://localhost:8090/api/v1/sales-order/${salesOrderSeq}`, {
      paramsSerializer: (salesOrderSeq) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(salesOrderSeq).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response.data.salesOrderItem);
    itemList.value = response.data.salesOrderItem;

  } catch (error) {
    console.error("주문서 상세 품목 불러오기 실패 :", error);
  }
};

const createShippingInstruction = async (formData, itemData) => {
  try {
    const response = await axios.post('http://localhost:8090/api/v1/shipping-instruction',
        {
          shippingInstructionScheduledShipmentDate: formData.value.shippingInstructionDate,
          salesOrderSeq: formData.value.salesOrderSeq,
          shippingInstructionAddress: formData.value.address,
          shippingInstructionNote: formData.value.note,
          shippingInstructionItems: itemData.map(item => ({
            itemSeq: item.Seq,
            shippingInstructionItemQuantity: item.quantity,
            shippingInstructionItemNote: item.note,
          })),
        }, {
        });

    console.log(response);
    alert('발주서가 등록되었습니다!');
    // 조회 페이지 이동
    await router.push("/purchaseOrder");

  } catch (error) {
    console.error('실패', error);
    throw error;
  }
};

onMounted(() => {
  fetchSalesOrderList();
});

// 페이지 이동
const handlePage = (newPageNumber) => {
  pageNumber.value = Number(newPageNumber.value);
  fetchSalesOrderList();
};

// 주문서 전달
const handleSalesOrder = (formData) => {
  fetchSalesOrder(formData.value.salesOrderSeq);
  selectedSalesOrder.value = true;
}

// 등록 핸들러
const handleRegister = async (itemList) => {
  if (childRef.value) {
    const formData = childRef.value.getData();
    const itemData = itemList.value;

    // formData 값이 널 또는 빈 값일 경우 각각 알림창 출력
    if (!formData.value.salesOrderSeq) {
      alert("주문서를 선택해주세요.");
      return;
    }
    if (!formData.value.shippingInstructionDate) {
      alert("출하지시일를 입력해주세요.");
      return;
    }
    if (!formData.value.address) {
      alert("주소를 입력해주세요.");
      return;
    }

    // itemData의 각 아이템에 대해 quantity가 0 이하이거나 원래 값보다 큰 경우 알림창 출력
    const invalidItem = itemData.find(item => {
      const originalQuantity = parseInt(item.originalQuantity); // 원래 아이템 수량
      const currentQuantity = parseInt(item.quantity);    // 현재 입력된 수량

      // 수량이 0 이하이거나 원래 수량보다 큰 경우
      return !currentQuantity || currentQuantity <= 0 || currentQuantity > originalQuantity;
    });

    if (invalidItem) {
      alert(`품목 수량을 확인해 주세요.`);
      return;
    }

    // 체크 후 등록 실행
    await createShippingInstruction(formData, itemData);

  } else {
    alert("자식 컴포넌트가 준비되지 않았습니다.");
  }
}
</script>

<template>
  <h4 class="title">주문관리 > 구매서 등록</h4>
  <div class="d-flex justify-content-center">
    <PurchaseInputForm ref="childRef"
                                  :salesOrderList="salesOrderList"
                                  :totalCount="totalCount"
                                  :pageNumber="pageNumber"
                                  :pageSize="pageSize"
                                  :registerListener="registerListener"
                                  @pageEvent="handlePage"
                                  @salesOrderEvent="handleSalesOrder"/>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div class="d-flex justify-content-center">
    <PurchaseInputItems :itemList="itemList"
                                   :selectedSalesOrder="selectedSalesOrder"
                                   @registerEvent="handleRegister"/>
  </div>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}
</style>