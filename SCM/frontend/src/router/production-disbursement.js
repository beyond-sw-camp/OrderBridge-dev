export default [
    {
        path: "/production-disbursement",
        component: () => import("@/views/productionDisbursement/ProductionDisbursementListView.vue")
    },
    {
        path: "/production-disbursement/situation",
        component: () => import("@/views/productionDisbursement/ProductionDisbursementSituationView.vue")
    },

]