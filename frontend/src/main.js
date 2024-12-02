import { createApp } from 'vue';

import App from '@/App.vue';
import { createPinia } from 'pinia'; // Pinia
import router from '@/router'; // Vue Router

const app = createApp(App);
const pinia = createPinia();

app.use(pinia); // Pinia 사용
app.use(router); // Vue Router 사용

app.mount('#app'); // 앱을 마운트