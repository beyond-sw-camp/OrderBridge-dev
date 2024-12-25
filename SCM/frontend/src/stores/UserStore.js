import { defineStore } from 'pinia';
import axios from '@/axios';
import router from "@/router/index.js";

// JWT 토큰 디코딩 함수
function parseJwt(token) {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    isAuthenticated: false,
    error: null,
    auth: null,
  }),

  actions: {
    async login(employeeNo, pwd) {
      this.error = null;
      try {
        const response = await axios.post('login', {
          userEmployeeNo: employeeNo,
          userPwd: pwd,
        });

        const accessToken = response.headers["Authorization"];
        const refreshToken = response.headers["Refresh-Token"];

        if (accessToken) {
          const decodedToken = parseJwt(accessToken);

          this.auth = decodedToken.auth;
          this.user = { id: decodedToken.sub };
          this.isAuthenticated = true;

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

    logout() {
      this.user = null;
      this.isAuthenticated = false;
      this.auth = null;
      localStorage.removeItem('authToken');

      router.push("/");
    },

    // 새로고침 시 상태 복원
    initialize() {
      const token = localStorage.getItem('authToken');
      if (token) {
        try {
          const decodedToken = parseJwt(token);

          // 토큰에서 사용자 정보 복원
          this.auth = decodedToken.auth;
          this.user = { id: decodedToken.sub };
          this.isAuthenticated = true;
        } catch (error) {
          console.error('Token parsing error:', error);
          this.logout(); // 토큰이 유효하지 않으면 로그아웃 처리
        }
      }
    },
  },
});
