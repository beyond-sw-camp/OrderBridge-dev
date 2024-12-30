export default [
    {
        path: "/client",
        component: () => import("@/views/client/ClientListView.vue"),
    },
    {
        path: "/client/input", // 등록 페이지
        component: () => import("@/views/client/ClientInputView.vue"),
    },
    {
        path: "/client/update/:clientSeq", // 수정 페이지
        component: () => import("@/views/client/ClientModifyView.vue"),
    },
];
