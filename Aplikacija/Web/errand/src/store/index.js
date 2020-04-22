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
        isSerbian: true
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
        // removeUserPhoneNumber({commit}, number) {
        //     const phoneNumbers = [...this.state.user.phone];
        //     const phoneIndex = phoneNumbers.findIndex(p => p === number);

        //     const newNumbers = phoneNumbers.splice(phoneIndex, 1);
        //     commit('setUserPhoneNumbers')
        // }
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

    }
})