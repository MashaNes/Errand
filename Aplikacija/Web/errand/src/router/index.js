/* eslint-disable no-unused-vars */
import Vue from "vue"
import Router from "vue-router"
import store from "@/store"

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
import PageServiceList from "@/pages/PageServiceList"
import PageOfferedServices from "@/pages/PageOfferedServices"
import PageViewRequest from "@/pages/PageViewRequest"
import PageNotAuthenticated from "@/pages/PageNotAuthenticated"
import PageNotFound from "@/pages/PageNotFound"
import PageNewRequest from "@/pages/PageNewRequest"
import PageStatistics from "@/pages/PageStatistics"
import PageForbidden from "@/pages/PageForbidden"
import PageAdminServices from "@/pages/PageAdminServices"
import PageReports from "@/pages/PageReports"
import PageUncategorizedTasks from "@/pages/PageUncategorizedTasks"
import PageViewRequestAdmin from "@/pages/PageViewRequestAdmin"
import PageNewAchievement from "@/pages/PageNewAchievement"
import PageNotifications from "@/pages/PageNotifications"

Vue.use(Router)

const router = new Router(
    {
        routes:[
            {
                path: "/",
                name: "PageWelcome",
                component: PageWelcome,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn && !store.state.isAdmin)
                        next({name: 'PageRequests'})
                    else if(store.state.logedIn && store.state.isAdmin)
                        next({name: 'PageStatistics'})
                    else
                        next()
                }
            },
            {
                path: "/requests",
                name: "PageRequests",
                component: PageRequests,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/login",
                name: "PageLogin",
                component: PageLogin,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn && !store.state.isAdmin)
                        next({name: 'PageRequests'})
                    else if(store.state.logedIn && store.state.isAdmin)
                        next({name: 'PageStatistics'})
                    else
                        next()
                }
            },
            {
                path: "/register",
                name: "PageRegister",
                component: PageRegister,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn && !store.state.isAdmin)
                        next({name: 'PageRequests'})
                    else if(store.state.logedIn && store.state.isAdmin)
                        next({name: 'PageStatistics'})
                    else
                        next()
                }
            },
            {
                path: "/profile/:id",
                name: "PageViewProfile",
                component: PageViewProfile,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                        next()
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/help",
                name: "PageHelp",
                component: PageHelp,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/achievements/:id",
                name: "PageAchievements",
                component: PageAchievements,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                        next()
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/ratings/:id",
                name: "PageRatings",
                component: PageRatings,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                        next()
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/loginAdmin",
                name: "PageLoginAdmin",
                component: PageLoginAdmin,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn && !store.state.isAdmin)
                        next({name: 'PageRequests'})
                    else if(store.state.logedIn && store.state.isAdmin)
                        next({name: 'PageStatistics'})
                    else
                        next()
                }
            },
            {
                path: "/registerAdmin",
                name: "PageRegisterAdmin",
                component: PageRegisterAdmin,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn && !store.state.isAdmin)
                        next({name: 'PageRequests'})
                    else if(store.state.logedIn && store.state.isAdmin)
                        next({name: 'PageStatistics'})
                    else
                        next()
                }
            },
            {
                path: "/faq",
                name: "PageFAQ",
                component: PageFAQ,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/settings",
                name: "PageSettings",
                component: PageSettings,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/users/:benefitList",
                name: "PageBrowseUsers",
                component: PageBrowseUsers,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/benefitList",
                name: "PageBenefitList",
                component: PageBenefitList,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/serviceList",
                name: "PageServiceList",
                component: PageServiceList,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/offeredServices/:id",
                name: "PageOfferedServices",
                component: PageOfferedServices,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                        next()
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/viewRequest/:id",
                name: "PageViewRequest",
                component: PageViewRequest,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/newRequest",
                name: "PageNewRequest",
                component: PageNewRequest,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/statistics",
                name: "PageStatistics",
                component: PageStatistics,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/addService",
                name: "PageAdminServices",
                component: PageAdminServices,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/reports",
                name: "PageReports",
                component: PageReports,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/uncategorizedTasks",
                name: "PageUncategorizedTasks",
                component: PageUncategorizedTasks,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/requestAdmin/:editable/:id",
                name: "PageViewRequestAdmin",
                component: PageViewRequestAdmin,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/addAchievement",
                name: "PageNewAchievement",
                component: PageNewAchievement,
                props: true,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
                path: "/notifications",
                name: "PageNotifications",
                component: PageNotifications,
                beforeEnter(to,from,next)
                {
                    if(store.state.logedIn)
                    {
                        if(!store.state.isAdmin)
                            next()
                        else
                        next({name: 'PageForbidden'})
                    }
                    else
                        next({name: 'PageNotAuthenticated'})
                }
            },
            {
              path: '/401',
              name: 'PageNotAuthenticated',
              component: PageNotAuthenticated
            },
            {
                path: '/403',
                name: 'PageForbidden',
                component: PageForbidden
            },
            {
              path: '*',
              name: 'PageNotFound',
              component: PageNotFound
            }
        ],
        mode: "history",
        scrollBehavior (to, from, savedPosition) {
            return { x: 0, y: 0 };
          }
    }
)

export default router