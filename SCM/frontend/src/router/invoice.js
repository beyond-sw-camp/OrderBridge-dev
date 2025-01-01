export default [
    {
        path: "/invoice",
        name: "Invoice",
        component: () => import("@/views/invoice/InvoiceListView.vue")
    },
    {
        path: "/invoice/input",
        name: "InvoiceInput",
        component: () => import("@/views/invoice/InvoiceInputView.vue")
    }
]
