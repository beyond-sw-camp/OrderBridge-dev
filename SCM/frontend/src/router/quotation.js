export default [
    {
        path: "/quotation",
        name: "Quotation",
        component: () => import("@/views/quotation/QuotationListView.vue")
    },
    {
        path: "/quotation/input",
        name: "QuotationInput",
        component: () => import("@/views/quotation/QuotationInputView.vue")
    },
]
