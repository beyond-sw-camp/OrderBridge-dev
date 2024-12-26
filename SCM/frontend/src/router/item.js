export default [
    {
        path: "/item",
        component: () => import("@/views/item/ItemListView.vue")
    },
    {
        path: "/item/input",
        component: () => import("@/views/item/ItemRegisterView.vue")
    },
    {
        path: "/item/update/:itemSeq",
        component: () => import("@/views/item/ItemModifyView.vue")
    },
]