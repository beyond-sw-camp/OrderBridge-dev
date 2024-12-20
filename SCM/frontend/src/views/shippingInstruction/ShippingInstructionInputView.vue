<script setup>
import ShippingInstructionInputForm from "@/components/shippingInstruction/ShippingInstructionInputForm.vue";
import ShippingInstructionInputItems from "@/components/shippingInstruction/ShippingInstructionInputItems.vue";
import {onMounted, ref} from "vue";
import axios from "@/axios"
import router from "@/router/index.js";

const totalCount = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);
const salesOrderList = ref([]);
const salesOrderStatusList = ref([]);
const registerListener = ref(false);
const addressList = ref([]);
const itemList = ref([]);
const itemDivisionList = ref([]);
const selectedSalesOrder = ref(false);

// 자식으로 부터 데이터 받아옴
const childRef = ref(null);

// 주문서 목록 요청
const fetchSalesOrderList = async () => {
  try {
    const response = await axios.get(`sales-order`, {
      params: {
        startDate: null,
        endDate: null,
        clientName: null,
        salesOrderStatus: 'PRODUCTION_COMPLETE',
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
    totalCount.value = response.data.totalSalesOrder;

  } catch (error) {
    console.error("주문서 불러오기 실패 :", error);
  }
};

// 상세 주문서 목록 요청 및 수량 업데이트
const fetchSalesOrder = async (salesOrderSeq) => {
  try {
    const response = await axios.get(`sales-order/${salesOrderSeq}`, {
      paramsSerializer: (salesOrderSeq) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(salesOrderSeq).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    const quantityResponse = await axios.get(`shipping-instruction/quantity/${salesOrderSeq}`, {
      paramsSerializer: (salesOrderSeq) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(salesOrderSeq).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });

    console.log(response.data.salesOrderItem);

    if (Array.isArray(quantityResponse.data) && response.data.salesOrderItem.length === quantityResponse.data.length) {
      // 수량 업데이트 및 필터링
      const updatedItems = response.data.salesOrderItem
          .map((item, index) => {
            item.salesOrderItemQuantity = quantityResponse.data[index]; // 각 아이템의 quantity를 업데이트
            return item; // 업데이트된 아이템 반환
          })
          .filter(item => item.salesOrderItemQuantity > 0); // 수량이 0인 항목은 제외

      console.log(updatedItems);
      itemList.value = updatedItems; // 필터링된 리스트를 itemList에 저장
    } else {
      console.warn("수량 데이터와 아이템 수가 일치하지 않습니다.");
    }

  } catch (error) {
    if (error.response) {
      // 서버에서 반환된 상태 코드에 따른 처리
      if (error.response.status === 400) {
        console.error(`주문서 상세 품목 불러오기 실패 : ${error.response.data.message}`);
        alert(`${error.response.data.message}`);
      } else {
        console.error(`주문서 상세 품목 불러오기 : 상태 코드 ${error.response.status}`);
      }
    }
  }
};

// 주문서 상태 목록 요청
const fetchSalesOrderStatusList = async () => {
  try {
    const response = await axios.get(`sales-order/status`, {});

    salesOrderStatusList.value = response.data;

  } catch (error) {
    console.error("출하지시서 상태 목록 불러오기 실패 :", error);
  }
};

// 출하지시서 주소 목록 요청
const fetchShippingInstructionAddressList = async () => {
  try {
    const response = await axios.get(`shipping-instruction/address`, {});

    addressList.value = response.data;

  } catch (error) {
    console.error("출하지시서 주소 리스트 불러오기 실패 :", error);
  }
};

// 품목 분류 요청
const fetchItemDivision = async () => {
  try {
    const response = await axios.get(`item/item-division`);

    itemDivisionList.value = response.data;
  } catch (error) {
    console.log(`품목 분류 요청 실패 ${error}`);
  }
}

// 출하지시서 등록 요청
const createShippingInstruction = async (formData, itemData) => {
  try {
    const response = await axios.post('shipping-instruction',
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
        }, {});

    console.log(response);
    alert('출하지시서가 등록되었습니다!');
    // 조회 페이지 이동
    await router.push("/shipping-instruction");

  } catch (error) {
    if (error.response) {
      // 서버에서 반환된 상태 코드에 따른 처리
      if (error.response.status === 400) {
        console.error(`출하지시서 등록 실패 : ${error.response.data.message}`);
        alert(`${error.response.data.message}`);
      } else {
        console.error(`출하지시서 등록 실패 : 상태 코드 ${error.response.status}`);
      }
    }
  }
};

onMounted(async () => {
  await fetchSalesOrderList();

  await fetchSalesOrderStatusList();
  await fetchShippingInstructionAddressList();
  await fetchItemDivision();
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

// 품목 리스트 갱신
const handleUpdateItemList = (updatedList) => {
  itemList.value = updatedList; // 자식에서 전달된 새 itemList로 갱신
};

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
      alert("주소를 선택해주세요.");
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
      alert(`품목 수량을 확인해 주세요. 0이하거나 원래 수량보다 많습니다.`);
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
  <h4 class="title">영업관리 > 출하지시서 등록</h4>
  <div class="d-flex justify-content-end mt-3">
    <b-button @click="router.push('/shipping-instruction')" variant="light" size="sm" class="button ms-2">목록</b-button>
  </div>
  <div class="d-flex justify-content-center">
    <ShippingInstructionInputForm ref="childRef"
                                  :salesOrderList="salesOrderList"
                                  :salesOrderStatusList="salesOrderStatusList"
                                  :totalCount="totalCount"
                                  :pageNumber="pageNumber"
                                  :pageSize="pageSize"
                                  :registerListener="registerListener"
                                  :addressList="addressList"
                                  @pageEvent="handlePage"
                                  @salesOrderEvent="handleSalesOrder"/>
  </div>
  <div class="px-4 d-flex flex-column align-items-center">
    <hr class="col-md-10 d-flex flex-column">
  </div>
  <div class="d-flex justify-content-center">
    <ShippingInstructionInputItems :itemList="itemList"
                                   :itemDivisionList="itemDivisionList"
                                   :selectedSalesOrder="selectedSalesOrder"
                                   @registerEvent="handleRegister"
                                   @updateItemListEvent="handleUpdateItemList"/>
  </div>
</template>

<style scoped>
.title {
  padding-bottom: 20px;
}

.button {
  background-color: #FFF8E7;
  border: 1px solid;
}
</style>