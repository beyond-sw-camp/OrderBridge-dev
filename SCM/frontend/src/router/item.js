export default [
    {
        path: "/item",
        component: () => import("@/views/item/ItemListView.vue")
    },
    {
        path: "/item/input",
        component: () => import("@/views/item/IteminputView.vue")
    },
]