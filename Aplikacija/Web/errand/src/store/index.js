/* eslint-disable no-unused-vars */
import Vue from "vue"
import Vuex from "vuex"
import router from "@/router"

Vue.use(Vuex)

import {fetchRequests} from "@/api/requests.js"
import {fetchUsers} from "@/api/users.js"
import {fetchRatings} from "@/api/ratings.js"
import {fetchAchievements} from "@/api/achievements.js"

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
        specificRequests: {},
        services: null,
        notAuthUserServices: null,
        userServices: null,
        allServices: null,
        token: null,
        isDataLoaded: true,
        isAdmin: false,
        mapMarkerPositions: [],
        userAdded:false,
        userAddresses: null,
        addressDeleteCount: 0,
        addressAddCount: 0,
        requestInCreation: null,
        servicesRequired: []
    },
    getters:{
        getAuthUserId(state) {
            return state.authUser.id
        }
    },
    actions:{
        setMarkerPositions({commit}, positions) {
            commit('setMarkers', positions)
        },
        fillRequests({commit}, {filters, objectToFill}) {
            //this.state.requests = fetchRequests();
            fetch("http://127.0.0.1:8000/api/v1/filtered_requests/", {
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
                    "unrated" : filters.unrated
                })
            }).then(p => {
                if(p.ok) {
                    p.json().then(data => {
                        this.state[objectToFill] = data
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
        // eslint-disable-next-line no-unused-vars
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
                            Vue.cookie.set('token',data['token'], { expires: '1h' });
                            Vue.cookie.set('id',this.state.authUser.id, { expires: '1h' });
                            Vue.cookie.set('ime',this.state.authUser.first_name, { expires: '1h' });
                            Vue.cookie.set('prezime',this.state.authUser.last_name, { expires: '1h' });
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
        // eslint-disable-next-line no-unused-vars
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
                            Vue.cookie.set('token',data['token'], { expires: '1h' });
                            Vue.cookie.set('id',this.state.authUser.id, { expires: '1h' });
                            Vue.cookie.set('ime',this.state.authUser.first_name, { expires: '1h' });
                            Vue.cookie.set('prezime',this.state.authUser.last_name, { expires: '1h' });
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
                        if(data["success"] == true)
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
        addRating({commit}, rating) {
            const newRating = {
                id: Object.values(fetchRatings()).length + 1,
                grade: rating.grade,
                comment: rating.comment,
                createdBy: this.state.authUser,
                request: rating.request,
                ratedUser: this.state.user.id
            }
            commit('addRatingToUser', newRating)
        },
        addReport({commit}, report) {
            const newReport = {
                //id = ...
                comment: report.comment,
                createdBy: this.state.authUser,
                user: this.state.user,
                reportType: report.reportType
            }
            commit('addNewReport', newReport)
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
        // eslint-disable-next-line no-unused-vars
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
                            if(router.currentRoute["path"] == "/")
                                router.push("/requests");
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
                        Vue.cookie.delete('id');
                        Vue.cookie.delete('token');
                        Vue.cookie.delete('ime');
                        Vue.cookie.delete('prezime');
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
                    //"picture_required" : payload.picture_required,
                    "picture_required" : false,
                    "note" : payload.note,
                    "max_dist" : payload.max_dist,
                    "min_rating" : payload.min_rating,
                    "destination" : payload.address,
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
                            //this.dispatch("pribavljanje skupa zahteva (makar ove prve "strane")")
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
                        // eslint-disable-next-line no-debugger
                        debugger
                        console.log(data)
                        let isParticipant = false
                        if(data.request.created_by.id == this.state.authUser.id)
                            isParticipant = true
                        if(data.request.working_with && data.request.working_with.id == this.state.authUser.id)
                            isParticipant = true
                        if(!isParticipant)
                            this.state.specificRequest = -1
                        else
                            this.state.specificRequest = data
                    })
                }
                else {
                    console.log("Error")
                }
            }) 
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
        }
    }
})