export default [
    {
        path: "/item",
        component: () => import("@/views/item/ItemListView.vue")
    },
    {
        path: "/item/input",
        component: () => import("@/views/item/IteminputView.vue")
    },
    {
        path: "/item/update/:id",
        component: () => import("@/views/item/ItemUpdateView.vue")
    },
]