/* eslint-disable no-unused-vars */
import Vue from "vue"
import Router from "vue-router"

import PageRequests from "@/pages/PageRequests"
import PageLogin from "@/pages/PageLogin"
import PageRegister from "@/pages/PageRegister"
import PageViewProfile from "@/pages/PageViewProfile"
import PageHelp from "@/pages/PageHelp"
import PageAchievements from "@/pages/PageAchievements"
import PageRatings from "@/pages/PageRatings"
import PageWelcome from "@/pages/PageWelcome"
import PageLoginAdmin from "@/pages/PageLoginAdmin"
import PageRegisterAdmin from "@/pages/PageRegisterAdmin"


Vue.use(Router)

const router = new Router(
    {
        routes:[
            {
                path: "/",
                name: "PageWelcome",
                component: PageWelcome
            },
            {
                path: "/requests",
                name: "PageRequests",
                component: PageRequests
            },
            {
                path: "/login",
                name: "PageLogin",
                component: PageLogin
            },
            {
                path: "/register",
                name: "PageRegister",
                component: PageRegister
            },
            {
                path: "/profile",
                name: "PageViewProfile",
                component: PageViewProfile
            },
            {
                path: "/help",
                name: "PageHelp",
                component: PageHelp
            },
            {
                path: "/achievements",
                name: "PageAchievements",
                component: PageAchievements
            },
            {
                path: "/ratings",
                name: "PageRatings",
                component: PageRatings
            },
            {
                path: "/loginAdmin",
                name: "PageLoginAdmin",
                component: PageLoginAdmin
            },
            {
                path: "/registerAdmin",
                name: "PageRegisterAdmin",
                component: PageRegisterAdmin
            }
        ],
        mode: "history",
        scrollBehavior (to, from, savedPosition) {
            return { x: 0, y: 0 };
          }
    }
)

export default router