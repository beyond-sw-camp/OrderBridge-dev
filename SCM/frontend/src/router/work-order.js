export default [
    {
        path: "/workOrder",
        component: () => import("@/views/workOrder/WorkOrderListView.vue")
    },
    {
        path: "/workOrder/situation",
        component: () => import("@/views/workOrder/WorkOrderSituationView.vue")
    },
]