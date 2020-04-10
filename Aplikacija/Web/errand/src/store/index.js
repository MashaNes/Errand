import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

import {fetchRequests} from "@/api"

export default new Vuex.Store({
    state:{
        requests:{},
        isSerbian: true
    },
    getters:{
    },
    actions:{
        fillRequests(){
            this.state.requests = fetchRequests();
        }
    },
    mutations:{}
})