import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: "/",
        component: () => import("@/views/main/MainView.vue")
    },
    {
        path: "/print",
        name: 'PrintView',
        component: () => import("@/views/print/PrintView.vue")
    },
    {
        path: "/quotation",
        component: () => import("@/views/quotation/QuotationListView.vue")
    },
    {
        path: "/quotation/input",
        component: () => import("@/views/quotation/InputView.vue")
    },
    {
        path: "/currentSituation",
        component: () => import("@/components/common/CurrentSituation.vue")
    },
    {
        path: "/productionReceiving",
        component: () => import("@/views/productionReceiving/ProductionReceivingListView.vue")
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
