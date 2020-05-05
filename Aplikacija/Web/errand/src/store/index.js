import Vue from "vue"
import Vuex from "vuex"

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
        logedIn: true,
        usersPortion: {},
        usersWithBenefit: {},
        emails: null,
        specificRequests: {},
        services: null,
        userServices: null,
        token: null,
        isDataLoaded: true
    },
    getters:{
        getAuthUserId(state) {
            return state.authUser.id
        }
    },
    actions:{
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
        fillAuthUser({commit}) {
            commit('setAuthUser', fetchUsers()["1"])
        },
        // eslint-disable-next-line no-unused-vars
        fillAuthUser2({commit}, payload) {
            this.state.isDataLoaded = false
            fetch("http://localhost:8000/api/v1/login/",
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
                            // eslint-disable-next-line no-debugger
                            console.log(data)
                            this.state.authUser = data['user']['user']
                            this.state.logedIn = true
                            this.state.token = data['token']
                            this.state.isDataLoaded = true
                            //this.$router.push('/requests')
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
        editUser({commit}, newUser) {

            commit('setChangedUser', newUser)
        },
        fillUsersPortion({commit}, {startingIndex, numOfElements, filterName, filterRating}) {
            const users = fetchUsers();
            const filteredUsers = Object.values(users).filter((user) => {
                
                const fullName = user.firstName + " " + user.lastName
                const okFilterName = !filterName ? true : fullName.toLowerCase().includes(filterName.toLowerCase())
                const okFilterRating = user.rating > filterRating
                
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
            this.state.userServices = fetchUserServices();
        },
        fillServices()
        {
            this.state.services = fetchServices();
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
        }
    }
})