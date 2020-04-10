import Vue from 'vue'
import App from './App.vue'
import router from "@/router"
import store from "@/store"
import moment from "moment"
import vuelidate from "vuelidate"

import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(faUserSecret)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.filter("showTime", function(datum)
{
  return moment(datum).format("LLL")
})

Vue.use(vuelidate)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuelidate,
  render: h => h(App),
}).$mount('#app')
