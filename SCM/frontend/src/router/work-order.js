export default [
    {
        path: "/work-order",
        component: () => import("@/views/workOrder/WorkOrderListView.vue")
    },
    {
        path: "/work-order/situation",
        component: () => import("@/views/workOrder/WorkOrderSituationView.vue")
    },
    {
        path: "/work-order/write",
        component: () => import("@/views/workOrder/WorkOrderWriteView.vue")
    },
    {
        path: "/work-order/edit/:workOrderSeq",
        component: () => import("@/views/workOrder/WorkOrderEditView.vue")
    }
]