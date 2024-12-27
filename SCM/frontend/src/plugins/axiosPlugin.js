import axios from '@/axios';

export default {
    install(app) {
        axios.interceptors.response.use(
            (response) => response,
            (error) => {
                if (error.response && error.response.status === 401) {
                    console.log('Unauthorized - Redirecting to login');

                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('refreshToken');

                    location.reload();
                    
                    alert('로그인 이후 이용할 수 있습니다.');
                    // Vue Router를 사용하여 로그인 페이지로 이동
                    app.config.globalProperties.$router.push({
                        path: '/login',
                        query: {
                            redirect: app.config.globalProperties.$router.currentRoute.value.fullPath, // 현재 경로 저장
                        },
                    });
                }
                return Promise.reject(error);
            }
        );

        app.config.globalProperties.$axios = axios;
    },
};