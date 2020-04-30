import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

import {fetchRequests} from "@/api/requests.js"
import {fetchUsers} from "@/api/users.js"
import {fetchAchievements, fetchAchievementDetails} from "@/api/achievements.js"
import {fetchRatings} from "@/api/ratings.js"

export default new Vuex.Store({
    state:{
        requests: {},
        user: {},
        userAchievements: {},
        userRatings: {},
        isSerbian: true,
        allUsers: {},
        logedIn: true,
        usersPortion: {}
    },
    getters:{
    },
    actions:{
        fillRequests(){
            this.state.requests = fetchRequests();
        },
        getUser({commit}) {
            commit('setUser', fetchUsers()["1"])
        },
        getAllUsers({commit}) {
            commit('setAllUsers', fetchUsers())
        },
        getUserAchievements({commit}) {
            const allAch = fetchAchievements();
            const allAchDetails = fetchAchievementDetails();
            const userAch = {}
            const objectToCommit = {}
            this.state.user.achievements.forEach(achKey => {
                Vue.set(userAch, achKey, allAch[achKey])
            })
            Object.values(userAch).forEach((ach, ind) => {
                const details = allAchDetails[ach.AchievementDetails]
                Vue.set(objectToCommit, `${ind}`, {id: ach.id, achievementLevel: ach.level, achievementDetails: details})
            })
            commit('setUserAchievements', objectToCommit)
        },
        getUserRatings({commit}) {
            const allRatings = fetchRatings();
            const rtgs = {}
            this.state.user.ratings.forEach(rating => {
                Vue.set(rtgs, rating, allRatings[rating])
            })
            commit('setUserRatings', rtgs)
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
            Vue.set(state, 'user', newUser)
        },
        setAllUsers(state, users) {
            state.allUsers = users
        },
        setUsersPortion(state, users) {
            state.usersPortion = users
            //console.log(state.usersPortion)
        }
    }
})