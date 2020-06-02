/* eslint-disable no-unused-vars */
import Vue from "vue"
import Vuex from "vuex"
import router from "@/router"

Vue.use(Vuex)

import {fetchRequests} from "@/api/requests.js"
import {fetchUsers} from "@/api/users.js"
import {fetchRatings} from "@/api/ratings.js"
import {fetchAchievements} from "@/api/achievements.js"
import {fetchPictures} from "@/api/pictureTest.js"
import {fetchActiveReports} from "@/api/activeReports.js"
import {fetchHandeledReports} from "@/api/handeledReports.js"
import {fetchRequestsOther} from "@/api/requestsOther.js"

export default new Vuex.Store({
    state:{
        requests: {},
        createdAuthRequests: null,
        runnerAuthRequests: null,
        overAuthRequests: null,
        specificRequest: null,
        user: {},
        authUser: null,
        userAchievements: {},
        userRatings: {},
        isSerbian: true,
        allUsers: {},
        logedIn: false,
        usersPortion: {},
        usersWithBenefit: null,
        unratedRequestsCreated: null,
        unratedRequestsDoneBy: null,
        ratedRequestsCreated: null,
        ratedRequestsDoneBy: null,
        services: null,
        notAuthUserServices: null,
        userServices: null,
        allServices: null,
        token: null,
        isDataLoaded: true,
        isRequestInfoLoaded: false,
        isAdmin: false,
        mapMarkerPositions: [],
        userAdded:false,
        userAddresses: null,
        addressDeleteCount: 0,
        addressAddCount: 0,
        requestInCreation: null,
        servicesRequired: [],
        testPictures: null,
        offers: null,
        edits: null,
        openedOffersOrEdits: null,
        requestFilteredInfo: {},
        filledInfoForRequest: null,
        messageToShow: "",
        activeReports: null,
        handeledReports: null,
        success: false,
        moreRequests: false
    },
    getters:{
        getAuthUserId(state) {
            return state.authUser.id
        },
        getOpenedOffersOrEdits(state) {
            return state.openedOffersOrEdits
        }
    },
    actions:{
        setMarkerPositions({commit}, positions) {
            commit('setMarkers', positions)
        },
        fillRequests({commit}, {filters, objectToFill}) {
            this.state.isDataLoaded = false
            var str = (objectToFill.object == "overAuthRequests" ? "?paginate=true&page=" +  objectToFill.page : "")
            fetch("http://127.0.0.1:8000/api/v1/filtered_requests/" + str, {
                method: "POST",
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : filters.created_by,
                    "done_by" : filters.done_by,
                    "created_or_done_by": filters.created_or_done_by,
                    "statuses" : filters.statuses,
                    "unrated_created_by" : filters.unrated_created_by,
                    "unrated_done_by" : filters.unrated_done_by
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        if(objectToFill.object == "overAuthRequests")
                        {
                            if (this.state.overAuthRequests == null)
                                this.state.overAuthRequests = []
                            this.state.overAuthRequests = this.state.overAuthRequests.concat(data.results)
                            if(data.next != null)
                            {
                                this.state.moreRequests = true
                            }
                        }
                        else
                        {
                            this.state[objectToFill.object] = data.results
                        }
                        this.state.isDataLoaded = true
                        console.log(data)
                    })
                }
            })
        },
        fillUsersWithBenefit()
        {
            fetch("http://127.0.0.1:8000/api/v1/user_info_filtered/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "blocked" : false,
                    "working_hours" : false,
                    "addresses" : false,
                    "services" : false,
                    "offers" : false,
                    "notifications" : false,
                    "ratings" : false,
                    "benefitlist" : true,
                    "achievements" : false,
                    "requests" : false
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.usersWithBenefit = data["benefitlist"]
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        getUser({commit}, userId) {
            const users = fetchUsers()
            const specificUser = Object.values(users).find(u => u.id == userId)
            commit('setUser', specificUser)
        },
        fillAuthUser({commit}, payload) {
            this.state.isDataLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/login/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json"
                },
                body:  JSON.stringify(
                {
                    "username" : payload.email,
                    "password" : payload.password
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.authUser = data['user']
                            this.state.logedIn = true
                            this.state.token = data['token']
                            this.state.isDataLoaded = true
                            this.state.isAdmin = data['user']['is_admin']
                            Vue.cookie.set('token',data['token'], { expires: '1h' });
                            Vue.cookie.set('id',this.state.authUser.id, { expires: '1h' });
                            Vue.cookie.set('ime',this.state.authUser.first_name, { expires: '1h' });
                            Vue.cookie.set('prezime',this.state.authUser.last_name, { expires: '1h' });
                            Vue.cookie.set('admin',this.state.isAdmin, { expires: '1h' });
                            if(!this.state.isAdmin)
                                router.push('/requests')
                            else
                                router.push("/statistics");
                        })
                    }
                    else
                    {
                        console.log("Error")
                        this.state.isDataLoaded = true
                    }
                });
        },
        createUser({commit}, payload)
        {
            this.state.isDataLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/user_create/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json"
                },
                body:  JSON.stringify(
                {
                    "is_admin" : false,
                    "email" : payload.email,
                    "password" : payload.password,
                    "first_name" : payload.name,
                    "last_name" : payload.lastName,
                    "phone" : payload.phone,
                    "picture" : null,
                    "benefit_discount" : null,
                    "benefit_requirement" : null
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.authUser = data['user']
                            this.state.logedIn = true
                            this.state.token = data['token']
                            this.state.isDataLoaded = true
                            this.state.isAdmin = false
                            Vue.cookie.set('token',data['token'], { expires: '1h' });
                            Vue.cookie.set('id',this.state.authUser.id, { expires: '1h' });
                            Vue.cookie.set('ime',this.state.authUser.first_name, { expires: '1h' });
                            Vue.cookie.set('prezime',this.state.authUser.last_name, { expires: '1h' });
                            Vue.cookie.set('admin',this.state.isAdmin, { expires: '1h' });
                            router.push('/requests')
                        })
                    }
                    else
                    {
                        console.log("Error")
                        this.state.isDataLoaded = true
                    }
                });
        },
        createAdminUser({commit}, payload)
        {
            this.state.isDataLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/user_create/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json"
                },
                body:  JSON.stringify(
                {
                    "is_admin" : true,
                    "email" : payload.email,
                    "password" : payload.password,
                    "first_name" : payload.name,
                    "last_name" : payload.lastName,
                    "phone" : null,
                    "picture" : null,
                    "benefit_discount" : null,
                    "benefit_requirement" : null,
                    "admin_key" : payload.superpassword
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            if(data['detail'] == "failed")
                            {
                                this.state.isDataLoaded = true
                                this.state.messageToShow = "password"
                                return
                            }
                            this.state.authUser = data['user']
                            this.state.logedIn = true
                            this.state.token = data['token']
                            this.state.isDataLoaded = true
                            this.state.isAdmin = true
                            Vue.cookie.set('token',data['token'], { expires: '1h' });
                            Vue.cookie.set('id',this.state.authUser.id, { expires: '1h' });
                            Vue.cookie.set('ime',this.state.authUser.first_name, { expires: '1h' });
                            Vue.cookie.set('prezime',this.state.authUser.last_name, { expires: '1h' });
                            Vue.cookie.set('admin',this.state.isAdmin, { expires: '1h' });
                            router.push('/statistics')
                        })
                    }
                    else
                    {
                        console.log("Error")
                        this.state.messageToShow = "error"
                        this.state.isDataLoaded = true
                    }
                });
        },
        getAllUsers({commit}) {
            commit('setAllUsers', fetchUsers())
        },
        getUserAchievements({commit}, userId) {
            const allAch = fetchAchievements();
            const filteredAch = Object.values(allAch).filter(a => a.user == userId)
            commit('setUserAchievements', filteredAch)
        },
        fillUserRatings({commit}, userId) {
            this.state.isDataLoaded = false
            fetch("http://localhost:8000/api/v1/user_info_filtered/", {
                method: "POST",
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : userId,
                    "blocked" : false,
                    "working_hours" : false,
                    "addresses" : false,
                    "services" : false,
                    "offers" : false,
                    "notifications" : false,
                    "ratings" : true,
                    "benefitlist" : false,
                    "achievements" : false,
                    "requests" : false
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        commit('setUserRatings', data.ratings)
                        console.log(this.state.userRatings)
                        this.state.isDataLoaded = true
                    })
                }
                else console.log("Error")
            })
        },
        fillUserAddresses({commit}, userId) {
            this.state.isDataLoaded = false
            fetch("http://localhost:8000/api/v1/user_info_filtered/", {
                method: "POST",
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : userId,
                    "blocked" : false,
                    "working_hours" : false,
                    "addresses" : true,
                    "services" : false,
                    "offers" : false,
                    "notifications" : false,
                    "ratings" : false,
                    "benefitlist" : false,
                    "achievements" : false,
                    "requests" : false
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        commit('setUserAddresses', data.addresses)
                        console.log(this.state.userAddresses)
                        this.state.isDataLoaded = true
                    })
                }
                else console.log("Error")
            })
        },
        addAddress({commit}, address) {
            fetch("http://localhost:8000/api/v1/address_add/", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by": address.created_by,
                    "name": address.name,
                    "longitude": address.longitude,
                    "latitude": address.latitude,
                    "home": address.home,
                    "arrived": address.arrived
                })
            }).then(p => {
                    if(p.ok) {
                        p.json().then(data => {
                            this.state.userAddresses.push(data)
                            this.state.addressAddCount++
                            console.log(this.state.userAddresses)
                        })
                    }
                })
        },
        deleteAddress({commit}, addressId) {
            fetch("http://localhost:8000/api/v1/address_remove/", {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by": this.state.authUser.id,
                    "address": addressId
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        if(data["detail"] == "success")
                        {
                            const ind = this.state.userAddresses.findIndex(addr => addr.id == addressId)
                            this.state.userAddresses.splice(ind, 1)
                            this.state.addressDeleteCount++
                        }
                    })
                }
            })
        },
        editUser({commit}, pictureChanged) {
            fetch('http://localhost:8000/api/v1/user_update/', {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by": this.state.authUser.id,
                    "email": this.state.authUser.email,
                    "first_name": this.state.authUser.first_name,
                    "last_name": this.state.authUser.last_name,
                    "password": null,
                    "phone": this.state.authUser.phone,
                    "picture": pictureChanged ? this.state.authUser.picture : null,
                    "min_rating": this.state.authUser.min_rating,
                    "max_dist": this.state.authUser.max_dist,
                    "benefit_discount": this.state.authUser.benefit_discount,
                    "benefit_requirement": this.state.authUser.benefit_requirement
                })
            }).then(p => {

                if(p.ok) {
                    p.json().then(data=> {
                        console.log(data)
                        console.log(this.state.authUser)
                    })
                }
                else {
                    console.log("error")
                }

            })
        },
        fillUsersPortion({commit}, filters) {
            this.state.isDataLoaded = false
            fetch(filters.endpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by": this.state.authUser.id,
                    "sort_rating": filters.sort_rating,
                    "sort_rating_asc": filters.sort_rating_asc,
                    "rating_limit_up": filters.rating_limit_up,
                    "rating_limit_down": filters.rating_limit_down,
                    "services": filters.services,
                    "no_rating": filters.no_rating,
                    "name": filters.name,
                    "not_in_benefit": filters.not_in_benefit,
                    "active": filters.active
                })
            }).then(p => {

                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        const toCommit = {
                            count: data.count,
                            next: data.next,
                            previous: data.previous,
                            results: data.results
                        }
                        commit('setUsersPortion', toCommit)
                    })
                }
                else console.log("Error")
            })

        },
        fillSpecificRequests({commit}, userId) {
            const requests = fetchRequests()
            const filteredRequests = Object.values(requests).filter(r => {
                return (r.status == 'finished' || r.status == 'failed') && r.user.id == userId 
            })
            commit('setSpecificRequests', filteredRequests)
        },
        addRating({commit}, filters) {
            fetch('http://localhost:8000/api/v1/rate_user/', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "rated_user" : filters.rated_user,
                    "request" : filters.request,
                    "grade" : filters.grade,
                    "comment" : filters.comment
                })
            }).then(p => {

                if(p.ok) {
                    p.json().then(data=> {
                        console.log(data)
                        this.state.success = true
                    })
                }
                else {
                    console.log("error")
                }
            })
        },
        addReport({commit}, filters) {
            fetch("http://127.0.0.1:8000/api/v1/report_create/", {
                method: 'POST',
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "reported_user" : filters.reported_user,
                    "comment" : filters.comment,
                    "request" : filters.request,
                    "pictures" : filters.pictures
                })
            }).then( p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        this.state.success = true
                    })
                }
                else {
                    console.log("Error")
                }
            });
        },
        fillUserServices()
        {
            fetch("http://127.0.0.1:8000/api/v1/user_info_filtered/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "blocked" : false,
                    "working_hours" : false,
                    "addresses" : false,
                    "services" : true,
                    "offers" : false,
                    "notifications" : false,
                    "ratings" : false,
                    "benefitlist" : false,
                    "achievements" : false,
                    "requests" : false
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.userServices = data["services"]
                            this.dispatch("fillServices")
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        fillNotAuthUserServices({commit}, userId) {
            this.state.isDataLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/user_info_filtered/", {
                method: 'POST',
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify({
                    "created_by" : userId,
                    "blocked" : false,
                    "working_hours" : false,
                    "addresses" : false,
                    "services" : true,
                    "offers" : false,
                    "notifications" : false,
                    "ratings" : false,
                    "benefitlist" : false,
                    "achievements" : false,
                    "requests" : false
                })
            }).then( p => {
                    if(p.ok) {
                        p.json().then(data => {
                            console.log(data)
                            commit('setNotAuthUserServices', data.services)
                            this.state.isDataLoaded = true
                        })
                    }
                    else {
                        console.log("Error")
                    }
                });
        }, 
        fillServices()
        {
            fetch("http://127.0.0.1:8000/api/v1/services/",
            {
                method: 'GET',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                }
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            var services = data["results"]
                            
                            var pom = []
                            services.forEach(element =>
                                {
                                    pom.push({...element})
                                })
                            this.state.allServices = pom
                            
                            if(this.state.userServices != null)
                            {
                                this.state.userServices.forEach((item, index) =>
                                {
                                    for(var i = services.length - 1; i >= 0; i--)
                                    {
                                        if(item.service.id == services[i].id || services[i].id == 1)
                                            services.splice(i,1)
                                    }
                                })
                                this.state.services = services
                                console.log(this.state.services)
                            }
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                }); 
        },
        updateUserInfo()
        {
            fetch("http://127.0.0.1:8000/api/v1/user_update/",
            {
                method: 'PUT',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by": this.state.authUser.id,
                    "email": this.state.authUser.email,
                    "first_name": this.state.authUser.first_name,
                    "last_name": this.state.authUser.last_name,
                    "password" : null,
                    "phone": this.state.authUser.phone,
                    "picture": null,
                    "min_rating": this.state.authUser.min_rating,
                    "max_dist": this.state.authUser.max_dist,
                    "benefit_discount" : this.state.authUser.benefit_discount,
                    "benefit_requirement" : this.state.authUser.benefit_requirement
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        getUserById({commit}, payload)
        {
            this.state.isDataLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/users_info/" + payload.id,
            {
                method: 'GET',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + payload.token
                }
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.authUser = data
                            this.state.logedIn = true
                            this.state.token = payload.token
                            this.state.isAdmin = data["is_admin"]
                            if(router.currentRoute["path"] == "/401")
                                router.back();
                            if(router.currentRoute["path"] == "/" || router.currentRoute["path"] == "/login" ||
                            router.currentRoute["path"] == "/loginAdmin" || router.currentRoute["path"] == "/register" ||
                            router.currentRoute["path"] == "/registerAdmin")
                            {
                                if(!this.state.isAdmin)
                                    router.push("/requests");
                                else
                                    router.push("/statistics");
                            }
                            this.state.isDataLoaded = true
                        })
                    }
                    else
                    {
                        this.state.isDataLoaded = true
                        console.log("Error")
                    }
                });
        },
        logoutUser()
        {
            fetch("http://127.0.0.1:8000/api/v1/logout/",
            {
                method: 'PUT',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        console.log("loged out")
                        this.state.authUser = null
                        this.state.token = null
                        this.state.isAdmin = false
                        this.state.userServices = null
                        this.state.services = null
                        this.state.allServices = null
                        this.state.usersWithBenefit = null
                        this.state.createdAuthRequests = null
                        this.state.runnerAuthRequests = null
                        this.state.overAuthRequests = null
                        this.state.specificRequest = null
                        this.state.activeReports = null
                        this.state.handeledReports = null
                        this.state.moreRequests = false
                        Vue.cookie.delete('id');
                        Vue.cookie.delete('token');
                        Vue.cookie.delete('ime');
                        Vue.cookie.delete('prezime');
                        Vue.cookie.delete('admin');
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        updateUserService({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/user_service_update/",
            {
                method: 'PUT',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "user_service" : payload.id,
                    "max_dist": payload.max_dist,
                    "payment_type": payload.payment_type,
                    "payment_ammount": payload.payment_ammount,
                    "min_rating": payload.min_rating
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        removeUserService({commit}, id)
        {
            fetch("http://127.0.0.1:8000/api/v1/user_service_remove/",
            {
                method: 'DELETE',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" :this.state.authUser.id,
                    "service" : id
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        addUserService({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/user_service_add/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "service" : payload.service.id,
                    "max_dist" : payload.max_dist,
                    "payment_type" : payload.payment_type,
                    "payment_ammount" : payload.payment_ammount,
                    "min_rating" : payload.min_rating
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.dispatch("fillUserServices")
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        getUserInfo({commit}, userId) {
            var vm = this
            this.state.isDataLoaded = false
            fetch("http://localhost:8000/api/v1/users_info/" + userId, {
                method:"GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Token " + this.state.token
                }
            }).then(p => {
                
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        commit('setUser', data)
                        vm.state.isDataLoaded = true
                        console.log(vm.state.user)
                    })
                }
                else console.log("Error")
            })
        },
        removeBenefit({commit}, id)
        {
            fetch("http://127.0.0.1:8000/api/v1/benefit_remove/",
            {
                method: 'DELETE',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" :this.state.authUser.id,
                    "benefit_user" : id
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        updateBenefit({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/benefit_update/",
            {
                method: 'PUT',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "benefit" : payload.id,
                    "discount" : payload.discount
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        addBenefit({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/benefit_add/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "benefit_user" : payload.id,
                    "discount" : payload.discount
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.dispatch("fillUsersWithBenefit")
                            this.state.userAdded = true
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        createRequest({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/request_create/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "name" : payload.name,
                    "time" : payload.time,
                    "picture_required" : payload.picture_required,
                    "note" : payload.note,
                    "max_dist" : payload.max_dist,
                    "min_rating" : payload.min_rating,
                    "destination" : payload.destination,
                    "broadcast" : payload.broadcast,
                    "direct_user" : payload.direct_user,
                    "tasklist" : payload.tasklist
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            this.state.userAdded = true
                            console.log(data)
                            this.state.createdAuthRequests.results.unshift(data)
                            const filters = {
                                created_by : this.state.authUser.id,
                                done_by : null,
                                created_or_done_by: null,
                                statuses : [0, 1],
                                unrated : null
                            }
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        getRequestById({commit}, requestId) {
            fetch("http://localhost:8000/api/v1/requests/" + requestId, {
                method: 'GET',
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                }
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        let isParticipant = false
                        if(data.request.created_by.id == this.state.authUser.id)
                            isParticipant = true
                        if(data.request.working_with && data.request.working_with.id == this.state.authUser.id)
                            isParticipant = true
                        if(!isParticipant)
                            this.state.specificRequest = -1
                        else
                        {
                            this.state.requestFilteredInfo.offers = data.offers
                            this.state.offers = data.offers

                            this.state.requestFilteredInfo.edits = data.edits
                            this.state.edits = data.edits

                            this.state.requestFilteredInfo.acceptedOffer = data.accepted_offer
                            this.state.requestFilteredInfo.ratingCreatedBy = data.rating_created_by
                            this.state.requestFilteredInfo.ratingWorkingWith = data.rating_working_with
                            this.state.specificRequest = data.request
                        }
                    })
                }
                else {
                    console.log("Error")
                }
            }) 
        },
        deleteRequest({commit}, requestId)
        { 
            fetch("http://127.0.0.1:8000/api/v1/request_cancel/",
            {
                method: 'DELETE',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" :this.state.authUser.id,
                    "request" : requestId
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        fillFilteredRequestInfo({commit}, {filters, requestId}) {
            this.state.isRequestInfoLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/request_info_filtered/", {
                method: "POST",
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "request" : requestId,
                    "offers" : filters.offers,
                    "edits" : filters.edits,
                    "accepted_offer" : filters.accepted_offer,
                    "rating_created_by" : filters.rating_created_by,
                    "rating_working_with" : filters.rating_working_with,
                    "tasklist" : filters.tasklist,
                    "destination" : filters.destination,
                    "pictures" : filters.pictures
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        this.state.filledInfoForRequest = requestId
                        if(filters.offers) {
                            this.state.offers = data.offers
                            this.state.requestFilteredInfo.offers = data.offers
                        }
                        if(filters.edits) {
                            this.state.edits = data.edits
                            this.state.requestFilteredInfo.edits = data.edits
                        }
                        if(filters.accepted_offer)
                            this.state.requestFilteredInfo.acceptedOffer = data.accepted_offer
                        if(filters.rating_created_by)
                            this.state.requestFilteredInfo.rating_created_by = data.rating_created_by
                        if(filters.rating_working_with)
                            this.state.requestFilteredInfo.rating_working_with = data.rating_working_with
                        if(filters.tasklist)
                            this.state.requestFilteredInfo.tasklist = data.tasklist
                        if(filters.destination)
                            this.state.requestFilteredInfo.destination = data.destination
                        if(filters.pictures)
                            this.state.requestFilteredInfo.pictures = data.pictures
                        this.state.isRequestInfoLoaded = true
                    })
                }
                else console.log("Error")
            })
        },
        rejectOffer({commit}, offerId) {
            fetch("http://127.0.0.1:8000/api/v1/offer_cancel/", {
                method: "DELETE",
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "offer" : offerId
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                    })
                }
                else console.log("Error")
            })
        },
        acceptOffer({commit}, {offerId, requestId}) {
            fetch("http://127.0.0.1:8000/api/v1/offer_accept/", {
                method: "PUT",
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "request" : requestId,
                    "offer" : offerId
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                    })
                }
                else console.log("Error")
            })
        },
        rejectEdit({commit}, offerId) {
            fetch("http://127.0.0.1:8000/api/v1/edit_cancel/", {
                method: "DELETE",
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "edit" : offerId
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                    })
                }
                else console.log("Error")
            })
        },
        acceptEdit({commit}, {requestId, editId}) {
            fetch("http://127.0.0.1:8000/api/v1/edit_accept/", {
                method: "PUT",
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "request" : requestId,
                    "edit" : editId
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                    })
                }
                else console.log("Error")
            })
        },
        createService({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/service_create/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                {
                    "created_by" : this.state.authUser.id,
                    "service_type_sr" : payload.service_type_sr,
                    "service_type_en" : payload.service_type_en,
                    "description_sr" : payload.description_sr,
                    "description_en" : payload.description_en,
                    "picture_required" : false
                })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.dispatch("fillServices")
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        },
        fillActiveReports(){
            //this.state.activeReports = fetchActiveReports()
            fetch("http://127.0.0.1:8000/api/v1/reports/",
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                    {
                        "created_by" : this.state.authUser.id,
                        "reported_by" : null,
                        "reported" : null,
                        "reported_by_or_reported": null,
                        "handled" : false
                    })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.activeReports = data.results
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                }); 
        },
        fillHandeledReports({commit}, payload){
            //this.state.handeledReports = fetchHandeledReports()
            this.state.isDataLoaded = false
            fetch("http://127.0.0.1:8000/api/v1/reports/?paginate=true&page=" + payload.page,
            {
                method: 'POST',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                    {
                        "created_by" : this.state.authUser.id,
                        "reported_by" : payload.reporter,
                        "reported" : payload.reported,
                        "reported_by_or_reported": null,
                        "handled" : true
                    })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                            this.state.handeledReports = data
                            this.state.isDataLoaded = true
                        })
                    }
                    else
                    {
                        console.log("Error")
                        this.state.isDataLoaded = true
                    }
                });
        },
        fillOpenedOffersOrEdits({commit}, array) {
            commit('openOfferOrEdit', array)
        },
        fillOtherRequests(){
            this.state.overAuthRequests = fetchRequestsOther()
        },
        getRequestByIdAdmin({commit}, requestId) {
            fetch("http://localhost:8000/api/v1/requests/" + requestId, {
                method: 'GET',
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                }
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        this.state.specificRequest = data.request
                    })
                }
                else {
                    console.log("Error")
                    this.state.specificRequest = -1
                }
            }) 
        },
        cancelRequest({commit}, request) {
            fetch("http://localhost:8000/api/v1/request_finish/", {
                method: 'PUT',
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "request" : request.id,
                    "status" : 3
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        if(data["detail"] == "success") {
                            let index = -1
                            if(this.state.createdAuthRequests != null)
                                index = this.state.createdAuthRequests.results.findIndex(req => req.id == request.id) 
                            
                            if(index != -1)
                                this.state.createdAuthRequests.results.splice(index, 1)
                            
                            if(this.state.overAuthRequests == null) {
                                this.state.overAuthRequests = {
                                    results: [],
                                    count: 0
                                }
                            }
                            this.state.overAuthRequests.results.push(request)
                            this.state.overAuthRequests.count += 1
                            this.state.success = true
                        }
                    })
                }
                else {
                    console.log("Error")
                    this.state.specificRequest = -1
                }
            })
        },
        finishRequest({commit}, request) {
            fetch("http://localhost:8000/api/v1/request_finish/", {
                method: 'PUT',
                headers: {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body: JSON.stringify({
                    "created_by" : this.state.authUser.id,
                    "request" : request.id,
                    "status" : 2
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        console.log(data)
                        if(data["detail"] == "success") {
                            let index = -1
                            if(this.state.createdAuthRequests != null)
                                index = this.state.createdAuthRequests.results.findIndex(req => req.id == request.id) 
                            

                            if(index != -1) {
                                if(request.status == 2)
                                    this.state.createdAuthRequests.results.splice(index, 1)
                                else
                                    this.state.createdAuthRequests.results[index].finished_created_by = true
                            }
                            
                            if(request.status == 2) {
                                if(this.state.overAuthRequests == null) {
                                    this.state.overAuthRequests = {
                                        results: [],
                                        count: 0
                                    }
                                }
                                this.state.overAuthRequests.results.push(request)
                                this.state.overAuthRequests.count += 1
                            }
                            this.state.success = true
                        }
                    })
                }
                else {
                    console.log("Error")
                    this.state.specificRequest = -1
                }
            })
        },
        handleReport({commit}, payload)
        {
            fetch("http://127.0.0.1:8000/api/v1/report_handle/",
            {
                method: 'PUT',
                headers:
                {
                    "Content-type" : "application/json",
                    "Authorization" : "Token " + this.state.token
                },
                body:  JSON.stringify(
                    {
                        "created_by" : this.state.authUser.id,
                        "id" : payload.id,
                        "status" : payload.status
                    })
            }).then( p => 
                {
                    if(p.ok)
                    {
                        p.json().then(data =>
                        {
                            console.log(data)
                        })
                    }
                    else
                    {
                        console.log("Error")
                    }
                });
        }
    },
    mutations:{
        setUser(state, user) {
            state.user = user
        },
        setUserAchievements(state, achievements) {
            state.userAchievements = achievements
        },
        setUserRatings(state, ratings) {
            state.userRatings = ratings
        },
        setNotAuthUserServices(state, services) {
            state.notAuthUserServices = services
        },
        setUserAddresses(state, addresses) {
            state.userAddresses = addresses
        },
        setChangedUser(state, newUser) {
            Vue.set(state, 'authUser', newUser)
        },
        setAllUsers(state, users) {
            state.allUsers = users
        },
        setUsersPortion(state, users) {
            state.usersPortion = users
            state.isDataLoaded = true
            //console.log(state.usersPortion)
        },
        setAuthUser(state, user) {
            state.authUser = user
        },
        setSpecificRequests(state, requests) {
            state.specificRequests = requests
        },
        addRatingToUser(state, rating) {
            console.log(rating)
        },
        addNewReport(state, report) {
            console.log(report)
        },
        setMarkers(state, positions) {
            state.mapMarkerPositions = positions
        },
        openOfferOrEdit(state, array) {
            state.openedOffersOrEdits = [...array]
        }
    }
})