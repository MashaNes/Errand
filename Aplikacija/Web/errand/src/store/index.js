/* eslint-disable no-unused-vars */
import Vue from "vue"
import Vuex from "vuex"
import router from "@/router"

Vue.use(Vuex)

import {fetchRequests} from "@/api/requests.js"
import {fetchUsers} from "@/api/users.js"
import {fetchRatings} from "@/api/ratings.js"
import {fetchAchievements} from "@/api/achievements.js"
import {fetchBenefitUsers} from "@/api/benefitUsers.js"
import {fetchEmails} from "@/api/email.js"
import {fetchServices} from "@/api/services.js"
import {fetchUserServices} from "@/api/userServices.js"

export default new Vuex.Store({
    state:{
        requests: {},
        user: {},
        authUser: null,
        userAchievements: {},
        userRatings: {},
        isSerbian: true,
        allUsers: {},
        logedIn: false,
        usersPortion: {},
        usersWithBenefit: {},
        emails: null,
        specificRequests: {},
        services: null,
        userServices: null,
        token: null,
        isDataLoaded: true,
        isAdmin: false,
        mapMarkerPositions: []
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
        fillRequests(){
            this.state.requests = fetchRequests();
        },
        fillUsersWithBenefit()
        {
            this.state.usersWithBenefit = fetchBenefitUsers();
        },
        fillEmails()
        {
            this.state.emails = fetchEmails();
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
        getUserRatings({commit}, userId) {
            const allRatings = fetchRatings();
            const filteredRatings = Object.values(allRatings).filter(r => r.ratedUser == userId)
            commit('setUserRatings', filteredRatings)
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
        fillUsersPortion({commit}, {startingIndex, numOfElements, filterName, filterRatingLower, filterRatingHigher}) {
            
            const users = fetchUsers();
            const filteredUsers = Object.values(users).filter((user) => {
                
                const fullName = user.first_name + " " + user.last_name
                const okFilterName = !filterName ? true : fullName.toLowerCase().includes(filterName.toLowerCase())
                const okFilterRating = user.avg_rating >= filterRatingLower && user.avg_rating <= filterRatingHigher
                
                return (okFilterName && okFilterRating)
            })

            const usersInRange = filteredUsers.filter((user, ind) => ind >= startingIndex && ind < startingIndex + numOfElements)

            const toCommit = {
                totalCount: Object.values(filteredUsers).length,
                currentCount: usersInRange.length,
                users: usersInRange
            }
            commit('setUsersPortion', toCommit)
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
                            this.state.userServices.forEach((item, index) =>
                            {
                                for(var i = services.length - 1; i >= 0; i--)
                                {
                                    if(item.service.id == services[i].id)
                                        services.splice(i,1)
                                }
                            })
                            this.state.services = services
                            console.log(this.state.services)
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
                    "created_by" :this.state.authUser.id,
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
        setChangedUser(state, newUser) {
            Vue.set(state, 'authUser', newUser)
        },
        setAllUsers(state, users) {
            state.allUsers = users
        },
        setUsersPortion(state, users) {
            state.usersPortion = users
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