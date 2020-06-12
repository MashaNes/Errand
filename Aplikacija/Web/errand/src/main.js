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

import * as firebase from "firebase/app";
import "firebase/messaging";

var config = {
    apiKey: "AIzaSyAC5XfZ2fgQ2TLPTrrmrvkKRhfd0DWondI",
    authDomain: "swe-errand.firebaseapp.com",
    databaseURL: "https://swe-errand.firebaseio.com",
    projectId: "swe-errand",
    storageBucket: "swe-errand.appspot.com",
    messagingSenderId: "1000133791657",
    appId: "1:1000133791657:web:3145b2aaee12cf1e4fbeb8",
    measurementId: "G-NY85XKGTMN"

};
firebase.initializeApp(config)

Vue.prototype.$messaging = firebase.messaging()

// navigator.serviceWorker.register('public/firebase-messaging-sw.js')
//   .then((registration) => {
//     Vue.prototype.$messaging.useServiceWorker(registration)
//   }).catch(err => {
//     console.log(err)
//   })


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
