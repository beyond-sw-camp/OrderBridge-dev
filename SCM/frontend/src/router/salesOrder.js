export default [
    {
        path: "/sales-order",
        name: "SalesOrder",
        component: () => import("@/views/salesOrder/SalesOrderListView.vue")
    },
    {
        path: "/sales-order/input",
        name: "SalesOrderInput",
        component: () => import("@/views/salesOrder/SalesOrderInputView.vue")
    },
    {
        path: "/sales-order/situation",
        name: "SalesOrderSituation",
        component: () => import("@/views/salesOrder/SalesOrderSituationView.vue")
    }
]
