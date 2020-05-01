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
import PageFAQ from "@/pages/PageFAQ"
import PageSettings from "@/pages/PageSettings"
import PageBrowseUsers from "@/pages/PageBrowseUsers"
import PageBenefitList from "@/pages/PageBenefitList"

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
                path: "/profile/:id",
                name: "PageViewProfile",
                component: PageViewProfile
            },
            {
                path: "/help",
                name: "PageHelp",
                component: PageHelp
            },
            {
                path: "/achievements/:id",
                name: "PageAchievements",
                component: PageAchievements
            },
            {
                path: "/ratings/:id",
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
            },
            {
                path: "/faq",
                name: "PageFAQ",
                component: PageFAQ
            },
            {
                path: "/settings",
                name: "PageSettings",
                component: PageSettings
            },
            {
                path: "/users",
                name: "PageBrowseUsers",
                component: PageBrowseUsers
            },
            {
                path: "/benefitList",
                name: "PageBenefitList",
                component: PageBenefitList
            }
        ],
        mode: "history",
        scrollBehavior (to, from, savedPosition) {
            return { x: 0, y: 0 };
          }
    }
)

export default router