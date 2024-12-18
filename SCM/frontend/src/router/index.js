import { createRouter, createWebHistory } from 'vue-router';
import shippingInstructionRoutes from './shipping-instruction.js'
import productionReceivingRoutes from './production-receiving.js'

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
        name: "Quotation",
        component: () => import("@/views/quotation/QuotationListView.vue")
    },
    {
        path: "/currentSituation",
        component: () => import("@/components/common/CurrentSituation.vue")
    },

    ...shippingInstructionRoutes,
    ...productionReceivingRoutes,
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
