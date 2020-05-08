import Vue from 'vue'
import App from './App.vue'
import router from "@/router"
import store from "@/store"
import moment from "moment"
import vuelidate from "vuelidate"
import VueCookie from 'vue-cookie'

import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import { BootstrapVue} from 'bootstrap-vue'


import UploadImage from 'vue-upload-image';
import VuePhoneNumberInput from 'vue-phone-number-input';
import 'vue-phone-number-input/dist/vue-phone-number-input.css';
// import * as VueGoogleMaps from 'vue2-google-maps'

// Vue.use(VueGoogleMaps, {
//   load: {
//     key: 'AIzaSyBc7vAECB9mQ1RbCrySraxt6ve0VxXO7zs'
//   }
// })


Vue.component('upload-image', UploadImage)
Vue.component('vue-phone-number-input', VuePhoneNumberInput);

Vue.use(BootstrapVue)
Vue.use(VueCookie)

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
