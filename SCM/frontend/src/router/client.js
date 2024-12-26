export default [
    {
        path: "/client",
        component: () => import("@/views/Client/ClientListView.vue"),
    },
    {
        path: "/client/input", // 등록 페이지
        component: () => import("@/views/Client/ClientInputView.vue"),
    },
    {
        path: "/client/update/:clientSeq", // 수정 페이지
        component: () => import("@/views/Client/ClientModifyView.vue"),
    },
];
