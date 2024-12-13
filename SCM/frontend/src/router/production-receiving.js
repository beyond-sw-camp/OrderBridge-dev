export default [
    {
        path: "/productionReceiving",
        component: () => import("@/views/productionReceiving/ProductionReceivingListView.vue")
    },
    {
        path: "/productionReceiving/situation",
        component: () => import("@/views/productionReceiving/ProductionReceivingSituationView.vue")
    }

]