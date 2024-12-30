import { defineStore } from 'pinia';
import axios from 'axios';
import customAxios from '@/axios';
import router from "@/router/index.js";

export const useUserStore = defineStore('user', {
  state: () => ({
    isAuthenticated: false,
    isLoggingOut: false,
    error: null,
    userId: null,
  }),

  actions: {
    async login(employeeNo, pwd) {
      this.error = null;
      try {
        const response = await axios.post('http://localhost:8090/api/v1/login', {
          userEmployeeNo: employeeNo,
          userPwd: pwd,
        });

        const accessToken = response.headers["authorization"];
        const refreshToken = response.headers["refresh-token"];

        if (accessToken) {
          this.isAuthenticated = true;
          // JWT 토큰의 페이로드 추출
          const payload = JSON.parse(atob(accessToken.split('.')[1]));
          this.userId = payload.sub;

          // JWT 토큰 로컬 스토리지에 저장
          localStorage.setItem('accessToken', accessToken);
          localStorage.setItem('refreshToken', refreshToken);
        } else {
          this.error = 'Token not found';
        }
      } catch (error) {
        this.error = error.response?.data?.message || '아이디 또는 비밀번호를 잘못 입력했습니다.';
      }
    },

    async logout() {
      this.error = null;
      this.isLoggingOut = true;
      try {
        await customAxios.post(`user/logout`);

        this.isAuthenticated = false;
        this.userId = null;
        await router.push("/");

        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
      } catch (error) {
        alert(`로그아웃 실패하였습니다.`);
      } finally {
        this.isLoggingOut = false; // 로그아웃 상태 해제
      }
    },
    initialize() {
      const token = localStorage.getItem('accessToken');

      if (token) {
        try {
          this.isAuthenticated = true;
          // JWT 토큰의 페이로드 추출
          const payload = JSON.parse(atob(token.split('.')[1]));
          this.userId = payload.sub;
        } catch (error) {
          console.error('Token parsing error:', error);
          this.logout(); // 토큰이 유효하지 않으면 로그아웃 처리
        }
      }
    },
  },
});
