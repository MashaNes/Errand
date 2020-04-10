import Vue from "vue"
import Router from "vue-router"

import PageRequests from "@/pages/PageRequests"
import PageLogin from "@/pages/PageLogin"
import PageRegister from "@/pages/PageRegister"

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
            }
        ],
        mode: "history"
    }
)

export default router