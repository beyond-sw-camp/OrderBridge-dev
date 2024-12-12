import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

import { createApp } from 'vue'

import App from './App.vue'
import router from '@/router'; // Vue Router
import BootstrapVue3 from "bootstrap-vue-3";
import {createPinia} from "pinia";

const pinia = createPinia();

const app = createApp(App);

app.use(router); // Vue Router 사용
app.use(BootstrapVue3);
app.use(pinia);
app.mount('#app')
