import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: "/",
        component: () => import("@/views/main/MainView.vue")
    },
    {
        path: "/currentSituation",
        component: () => import("@/components/common/currentSituation/CurrentSituation.vue")
    }

];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
