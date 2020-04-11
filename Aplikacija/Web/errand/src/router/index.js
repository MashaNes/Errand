import Vue from "vue"
import Router from "vue-router"

import PageRequests from "@/pages/PageRequests"
import PageLogin from "@/pages/PageLogin"
import PageRegister from "@/pages/PageRegister"
import PageViewProfile from "@/pages/PageViewProfile"

Vue.use(Router)

const router = new Router(
    {
        routes:[
            {
                path: "/",
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
            }
        ],
        mode: "history"
    }
)

export default router