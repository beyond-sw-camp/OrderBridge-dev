export default [
    {
        path: "/production-disbursement",
        component: () => import("@/views/productionDisbursement/ProductionDisbursementListView.vue")
    },
    {
        path: "/production-disbursement/situation",
        component: () => import("@/views/productionDisbursement/ProductionDisbursementSituationView.vue")
    },
    {
        path: "/production-disbursement/write",
        component: () => import("@/views/productionDisbursement/ProductionDisbursementWriteView.vue")
    },
    {
        path: "/production-disbursement/edit/:productionDisbursementSeq",
        component: () => import("@/views/productionDisbursement/ProductionDisbursementEditView.vue")
    }

]