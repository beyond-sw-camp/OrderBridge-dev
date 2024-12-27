import { createRouter, createWebHistory } from 'vue-router';
import shippingInstructionRoutes from './shipping-instruction.js'
import productionReceivingRoutes from './production-receiving.js'
import item from "@/router/item.js";
import workOrderRoutes from "@/router/work-order.js";
import shippingSlipRoutes from "@/router/shipping-slip.js";
import warehouse from "@/router/warehouse.js";
import client from "@/router/client.js";
import quotationRoutes from "@/router/quotation.js";
import salesOrder from '@/router/salesOrder.js';
import invoice from '@/router/invoice.js'
import {useUserStore} from "@/stores/UserStore.js";

const routes = [
    {
        path: "/",
        component: () => import("@/views/main/MainView.vue")
    },
    {
        path: "/login",
        component: () => import("@/views/user/LoginView.vue")
    },
    {
        path: "/print",
        name: 'PrintView',
        component: () => import("@/views/print/PrintView.vue")
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
        path: "/purchaseOrder/situation",
        component: () => import("@/views/purchaseOrder/PurchaseOrderSituationView.vue")
    },
    {
        path: "/purchaseOrder/stock/situation",
        component: () => import("@/views/purchaseOrder/PurchaseOrderStockSituationView.vue")
    },
    {
        path: "/purchase",
        component: () => import("@/views/purchase/PurchaseListView.vue")
    },
    {
        path: "/purchase/input",
        component: () => import("@/views/purchase/PurchaseInputView.vue")
    },
    {
        path: "/purchase/situation",
        component: () => import("@/views/purchase/PurchaseSituationView.vue")
    },

    ...shippingInstructionRoutes,
    ...productionReceivingRoutes,
    ...item,
    ...workOrderRoutes,
    ...shippingSlipRoutes,
    ...warehouse,
    ...client,
    ...quotationRoutes,
    ...salesOrder,
    ...invoice
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('accessToken');

    if (useUserStore().isLoggingOut) {
        // 로그아웃 중일 때는 검증을 생략
        next();
        return;
    }

    if(!token && (to.path !== '/' && to.path !== '/login')) {
        alert('로그인 이후 이용할 수 있습니다.');
        next('/login');
    } else {
        next();
    }
})

export default router;