export default [
    {
        path: "/warehouse",
        component: () => import("@/views/warehouse/WarehouseListView.vue")
    },
    {
        path: "/warehouse/input",
        component: () => import("@/views/warehouse/WarehouseinputView.vue")
    },
    {
        path: '/warehouse/Modfiy/:id',
        component: () => import('@/views/warehouse/WarehouseModifyView.vue')
    },
]