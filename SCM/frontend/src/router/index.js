import { createRouter, createWebHistory } from 'vue-router';
import shippingInstructionRoutes from './shipping-instruction.js'

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
        path: "/currentSituation",
        component: () => import("@/components/common/CurrentSituation.vue")
    },
    {
        path: "/productionReceiving",
        component: () => import("@/views/productionReceiving/ProductionReceivingListView.vue")
    },
    ...shippingInstructionRoutes
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
