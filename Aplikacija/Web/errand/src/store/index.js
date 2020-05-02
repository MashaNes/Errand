import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

import {fetchRequests} from "@/api/requests.js"
import {fetchUsers} from "@/api/users.js"
import {fetchRatings} from "@/api/ratings.js"
import {fetchAchievements} from "@/api/achievements.js"
import {fetchBenefitUsers} from "@/api/benefitUsers.js"
import {fetchEmails} from "@/api/email.js"

export default new Vuex.Store({
    state:{
        requests: {},
        user: {},
        authUser: {},
        userAchievements: {},
        userRatings: {},
        isSerbian: true,
        allUsers: {},
        logedIn: true,
        usersPortion: {},
        usersWithBenefit: {},
        emails: null
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
        fillUsersPortion({commit}, {startingIndex, numOfElements}) {
            const users = fetchUsers();
            const filteredUsers = Object.values(users).filter((user, ind) => ind >= startingIndex && ind < startingIndex + numOfElements)
            const toCommit = {
                totalCount: Object.values(users).length,
                currentCount: filteredUsers.length,
                users: filteredUsers
            }
            // console.log(toCommit + " " + startingIndex + " " + numOfElements)
            // console.log(toCommit)
            // console.log(users)
            commit('setUsersPortion', toCommit)
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
        }
    }
})