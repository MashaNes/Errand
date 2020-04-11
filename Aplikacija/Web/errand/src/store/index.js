import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

import {fetchRequests} from "@/api/requests.js"
import {fetchUsers} from "@/api/users.js"
import {fetchAchievements, fetchAchievementDetails} from "@/api/achievements.js"

export default new Vuex.Store({
    state:{
        requests: {},
        user: {},
        userAchievements: {},
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
        }
    },
    mutations:{
        setUser(state, user) {
            state.user = user
        },
        setUserAchievements(state, achievements) {
            state.userAchievements = achievements
        }
    }
})