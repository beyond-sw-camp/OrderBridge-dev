<script setup>
import searchIcon from "@/assets/searchIcon.svg"
import {computed, ref} from "vue";
import axios from "@/axios"
import robotIcon from "@/assets/robotIcon.svg"
import humanIcon from "@/assets/humanIcon.svg"

// 상태 관리
const questionTest = ref({
  role: 'user',
  message: '',
}); // 입력 필드 값
const items = ref([]); // 리스트 항목

const fetchChatbot = async () => {
  try {
    const response = await axios.get(`chatbot`, {
      params: {
        message: messageData.value,
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
      items.value.push(response.data);
    }

    prepareScroll();
  } catch (error) {
    if (error.response) {
      console.error(`챗봇 호출 실패 : 메세지 ${error.response.message}`);
    }
  }
}

// 질문인지 답변인지 체크
const canQuestion = computed(() => {
  // items 배열의 마지막 role이 'model'인지 확인
  return items.value.length === 0 || (items.value.length > 0 && items.value[items.value.length - 1].role === 'model');
});

// 질문은 답변이후에만 다시 할 수 있음
function checkAndQuestion() {
  if ((canQuestion.value || items.value.length === 0) && questionTest.value.message.trim()) {
    question();
  }
}

// 질문
const messageData = ref('');
function question() {
  items.value.push({...questionTest.value});
  messageData.value = questionTest.value.message;
  questionTest.value.message = ""; // 입력 필드 초기화
  prepareScroll();
  fetchChatbot();
}

// 스크롤바 설정
function scrollBar() {
  const chatbotBody = document.getElementById("chatbotBody");
  console.log(chatbotBody.scrollTop, chatbotBody.offsetHeight);
  chatbotBody.scrollTop = chatbotBody.scrollHeight;
}

// 준비 함수, 약간의 시간을 두어 scroll 함수를 호출하기
function prepareScroll() {
  window.setTimeout(scrollBar, 50);
}

</script>

<template>
  <div id="chatbotBody">
    <ul id="chatbotText">
      <template v-for="(item, index) in items" :key="index">
        <template v-if="item.role === 'user'">
          <li>
            <div class="icon-div" style="background-color: antiquewhite; justify-self: self-end;"><humanIcon class="icon"/></div>
            <div class="chat" style="background-color: bisque; justify-self: self-end;">{{ item.message }}</div>
          </li>
        </template>
        <template v-else>
          <li>
            <div class="icon-div" style="background-color: bisque; justify-self: self-start;"><robotIcon class="icon"/></div>
            <div class="chat" style="justify-self: self-start;">{{ item.message }}</div>
          </li>
        </template>
      </template>
    </ul>
  </div>
  <div style="width: 100%; height: 20%;">
    <b-input-group class="mt-3">
      <b-form-input v-model="questionTest.message" placeholder="질문을 입력하세요." @keyup.enter="checkAndQuestion()"></b-form-input>
      <b-button variant="light" class="button" :disabled="!canQuestion || !questionTest.message.trim()" @click="question()">
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

.answer {
  background-color: #FFF8E7;
  border: 1px solid;
}

.icon-div{
  width: fit-content;
  border: 1px solid;
  border-radius: 10px;
}

.icon {
  width: 30px;
  height: 30px;
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
