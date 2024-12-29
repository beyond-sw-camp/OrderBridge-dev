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
    scrollBar();
  }catch (error) {
    if (error.response) {
      console.error(`챗봇 호출 실패 : 메세지 ${error.response.message}`);
    }
  }
}

// 질문
function question() {
  items.value.push(questionTest.value);
  scrollBar();
  fetchChatbot();
}

// 스크롤바 설정
function scrollBar() {
  const chatbotBody = document.getElementById("chatbotBody");
  console.log(chatbotBody.scrollTop, chatbotBody.offsetHeight);
  chatbotBody.scrollTop = chatbotBody.scrollHeight;
  // chatbotBody.scrollTo({top: chatbotBody.scrollHeight, behavior: "smooth"});
}
</script>

<template>
  <div id="chatbotBody">
    <ul id="chatbotText">
      <template v-for="(item, index) in items" :key="index">
        <template v-if="index % 2 === 0">
          <li><div class="chat" style="justify-self: self-end;">{{ item }}</div></li>
        </template>
        <template v-else>
          <li><div class="chat" style="justify-self: self-start;">{{ item }}</div></li>
        </template>
      </template>
    </ul>
  </div>
  <div style="width: 100%; height: 20%;" >
    <b-input-group class="mt-3">
      <b-form-input v-model="questionTest" placeholder="질문을 입력하세요." @keyup.enter="question()"></b-form-input>
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

#chatbotBody {
  width: 100%; 
  height: 80%; 
  overflow-y: auto;
  border: solid 1px silver;
  border-radius: 10px;
}

#chatbotText {
  list-style: none;
  padding: 0px 5px 0px 5px;
}

.chat {
  width: fit-content;
  max-width: 300px;
  margin-top: 10px;
  padding: 5px;
  border: solid 1px silver;
  border-radius: 10px;
  background-color: white;
}

</style>
