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
    {
        path: "/purchaseOrder",
        component: () => import("@/views/purchaseOrder/PurchaseOrderListView.vue")
    },
    {
        path: "/purchaseOrder/input",
        component: () => import("@/views/purchaseOrder/PurchaseOrderInputView.vue")
    },
    {
        path: "/purchase",
        component: () => import("@/views/purchase/PurchaseListView.vue")
    },
    {
        path: "/purchase/input",
        component: () => import("@/views/purchase/PurchaseInputView.vue")
    },

    ...shippingInstructionRoutes,
    ...productionReceivingRoutes,
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
