import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

import { createApp } from 'vue';

import App from '@/App.vue';
import { createPinia } from 'pinia'; // Pinia
import router from '@/router'; // Vue Router
import BootstrapVue3 from "bootstrap-vue-3";

const app = createApp(App);
const pinia = createPinia();

app.use(pinia); // Pinia 사용
app.use(router); // Vue Router 사용
app.use(BootstrapVue3)

app.mount('#app'); // 앱을 마운트