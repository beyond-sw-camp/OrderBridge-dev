export default [
    {
        path: "/shipping-slip",
        component: () => import("@/views/shippingSlip/ShippingSlipListView.vue")
    },
    {
        path: "/shipping-slip/situation",
        component: () => import("@/views/shippingSlip/ShippingSlipSituationView.vue")
    }
]