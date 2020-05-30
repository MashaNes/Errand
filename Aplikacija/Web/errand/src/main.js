import Vue from 'vue'
import App from './App.vue'
import router from "@/router"
import store from "@/store"
import moment from "moment"
import vuelidate from "vuelidate"
import VueCookie from 'vue-cookie'
import Toasted from 'vue-toasted';
import VueLazyLoad from 'vue-lazyload'

import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import { BootstrapVue} from 'bootstrap-vue'


import UploadImage from 'vue-upload-image';
import VuePhoneNumberInput from 'vue-phone-number-input';
import 'vue-phone-number-input/dist/vue-phone-number-input.css';


Vue.component('upload-image', UploadImage)
Vue.component('vue-phone-number-input', VuePhoneNumberInput);

Vue.use(BootstrapVue)
Vue.use(VueCookie)
Vue.use(Toasted)
Vue.use(VueLazyLoad)

require('vue-image-lightbox/dist/vue-image-lightbox.min.css')
library.add(faUserSecret)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.filter("showTime", function(datum)
{
  return moment.utc(datum).format("LLL")
})

Vue.use(vuelidate)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuelidate,
  render: h => h(App),
}).$mount('#app')
