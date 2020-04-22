import Vue from "vue"
import Router from "vue-router"

import PageRequests from "@/pages/PageRequests"
import PageLogin from "@/pages/PageLogin"
import PageRegister from "@/pages/PageRegister"
import PageViewProfile from "@/pages/PageViewProfile"
import PageHelp from "@/pages/PageHelp"
import PageAchAndRatings from "@/pages/PageAchAndRatings"

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
            },
            {
                path: "/help",
                name: "PageHelp",
                component: PageHelp
            },
            {
                path: "/achAndRatings",
                name: "PageAchAndRatings",
                component: PageAchAndRatings
            }
        ],
        mode: "history"
    }
)

export default router