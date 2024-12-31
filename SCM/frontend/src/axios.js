import axios from 'axios';
import router from './router';

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_URL
});

// 요청 인터셉터에서 헤더 값 확인
instance.interceptors.request.use((config) => {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    // Authorization 헤더에 Access Token 추가
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    // Refresh-Token 헤더에 Refresh Token 추가
    if (refreshToken) {
        config.headers['Refresh-Token'] = `Bearer ${refreshToken}`;
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

// 응답 인터셉터
instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && error.response.status === 401) {
        // 401 응답 시 로그아웃 처리
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');

        alert('로그인 이후 사용할 수 있습니다.');
        router.push('/login'); // 로그인 페이지로 이동
      }
      return Promise.reject(error);
    }
);

export default instance;
