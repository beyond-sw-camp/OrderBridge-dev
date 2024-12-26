export default [
    {
        path: "/workOrder",
        component: () => import("@/views/workOrder/WorkOrderListView.vue")
    },
    {
        path: "/workOrder/situation",
        component: () => import("@/views/workOrder/WorkOrderSituationView.vue")
    },
    {
        path: "/workOrder/write",
        component: () => import("@/views/workOrder/WorkOrderWriteView.vue")
    },
    {
        path: "/workOrder/edit/:workOrderSeq",
        component: () => import("@/views/workOrder/WorkOrderEditView.vue")
    }
]