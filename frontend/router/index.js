import { createRouter, createWebHistory } from "vue-router";
import ComeAndLogin from "../views/ComeAndLogin.vue";
import LoginPage from "@/views/LoginPage.vue";
import HomePage from "../views/HomePage.vue";
import ArticlePage from "../views/ArticlePage.vue";
import SendPage from "@/views/SendPage.vue";
import PersonAl from "@/views/PersonAl.vue";
import SettinG from "@/views/SettinG.vue";

const routes = [{
        path: "/",
        redirect: "/welcome", // 添加中间重定向路径
    },
    {
        path: "/welcome",
        name: "ComeAndLogin",
        component: ComeAndLogin,
    },
    {
        path: "/login",
        name: "LoginPage",
        component: LoginPage,
    },
    {
        path: "/home",
        name: "HomePage",
        component: HomePage,
    },
    {
        path: "/article/:id",
        name: "ArticlePage",
        component: ArticlePage,
        props: true,
    },
    {
        path: "/new",
        name: "SendPage",
        component: SendPage,
    },
    {
        path: "/profile",
        name: "PersonAl",
        component: PersonAl,
    },
    {
        path: "/setting",
        name: "SettinG",
        component: SettinG,
    },
];

const router = createRouter({
    history: createWebHistory("/"),
    routes,
});

export default router;