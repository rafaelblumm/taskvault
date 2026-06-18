import { createRouter, createWebHistory } from "vue-router";
import TokenService from "@/service/token";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            redirect: "/login"
        },
        {
            name: "login",
            path: "/login",
            component: () => import("@/views/LoginView")
        },
        {
            name: "tasks",
            path: "/tasks",
            component: () => import("@/views/TaskListView"),
            meta: { requiresAuth: true }
        },
        {
            name: "task",
            path: "/task/:id",
            component: () => import("@/views/TaskView"),
            props: true,
            meta: { requiresAuth: true }
        },
        {
            name: "createTask",
            path: "/createTask",
            component: () => import("@/views/CreateTaskView"),
            meta: { requiresAuth: true }
        },
        {
            name: "user",
            path: "/user",
            component: () => import("@/views/UserView"),
            meta: { requiresAuth: true }
        },
        {
            name: "createUser",
            path: "/createUser",
            component: () => import("@/views/CreateUserView"),
            meta: { requiresAuth: true }
        },
        {
            name: "getUser",
            path: "/getUser",
            component: () => import("@/views/GetUserView"),
            meta: { requiresAuth: true }
        },
        {
            name: "updateUser",
            path: "/updateUser",
            component: () => import("@/views/UpdateUserView"),
            meta: { requiresAuth: true }
        },
        {
            name: "deleteUser",
            path: "/deleteUser",
            component: () => import("@/views/DeleteUserView"),
            meta: { requiresAuth: true }
        }
    ]
});

router.beforeEach((to) => {
    const authRequired = to.matched.some(record => record.meta.requiresAuth);
    const isAuthenticated = !!TokenService.getToken();
    if (authRequired && !isAuthenticated) {
        return '/login';
    }
})

export default router;
