<script setup>
import searchIcon from "@/assets/searchIcon.svg"
import {ref} from "vue";
import axios from "@/axios"

// 상태 관리
const questionTest = ref(""); // 입력 필드 값
const items = ref([]); // 리스트 항목

const fetchChatbot = async () =>{
  try{
    const response = await axios.get(`chatbot`, {
      params: {
        message: questionTest.value,
      }, paramsSerializer: (params) => {
        // null이나 undefined 값을 필터링
        const filteredParams = Object.fromEntries(
            Object.entries(params).filter(([_, value]) => value !== null && value !== undefined)
        );
        return new URLSearchParams(filteredParams).toString();
      }
    });


    // 요청 성공 시 새로운 항목 추가
    if (response.status === 200) {
      console.log(response.data.message);
      items.value.push(response.data.message);
      questionTest.value = ""; // 입력 필드 초기화
    }

    console.log(items.value);
  }catch (error) {
    if (error.response) {
      console.error(`챗봇 호출 실패 : 메세지 ${error.response.message}`);
    }
  }
}

// 질문
function question() {
  items.value.push(questionTest.value);
  fetchChatbot();
}
</script>

<template>
  <div style="width: 100%; height: 200px; overflow-y: auto;">
    <ul id="chatbotText" >
      <li v-for="(item, index) in items" :key="index">
        {{ item }}
      </li>
    </ul>
  </div>
  <div style="width: 100%; height: 50px;" >
    <b-input-group class="mt-3">
      <b-form-input v-model="questionTest" placeholder="질문을 입력하세요."></b-form-input>
      <b-button variant="light" class="button" :disabled="!questionTest.trim()" @click="question()">
        <searchIcon class="icon"/>
      </b-button>
    </b-input-group>
  </div>
</template>

<style scoped>
.button {
  background-color: #FFF8E7;
  border: 1px solid;
}

.answer{
  background-color: #FFF8E7;
  border: 1px solid;
}

.icon {
  width: 20px;
  height: 20px;
}

</style>