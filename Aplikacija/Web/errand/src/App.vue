<template>
  <div id="app">
    <TheNavbar class="please"/>
    <div class = "page-wrapper bg-light">
      <router-view />
    </div>
    <TheFooter/>
  </div>
</template>

<script>
import TheFooter from "@/components/TheFooter"
import TheNavbar from "@/components/TheNavbar"
import fontawesome from '@fortawesome/fontawesome'
import faBrands from '@fortawesome/fontawesome-free-brands'

export default {
  name: 'App',
  components: {
    TheFooter,
    TheNavbar
  },
  data()
  {
    return{
      user:
      {
        id: -1,
        token: "",
        first_name: "",
        last_name: ""
      }
    }
  },
  computed: {
    firebaseNotification() {
      return this.$store.state.firebaseNotification
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    showMoreText() {
      return this.isSerbian ? 'prikaži više' : 'show more'
    },
    closeText() {
      return this.isSerbian ? 'zatvori' : 'close'
    }
  },
  watch: {
    // eslint-disable-next-line no-unused-vars
    firebaseNotification(newVal, oldVal) {
      if(newVal!=null && this.$store.state.logedIn && !this.$store.state.isAdmin) {
        console.log(true)
        const type = parseInt(newVal.data.notification_type)
        const showAndr = (type == 0 || type == 3 || type == 4) ? true : false
        if(showAndr) {
          this.$toasted.show(`${fontawesome.icon(faBrands.faAndroid).html} ${this.isSerbian ? newVal.data.body_sr : newVal.data.body_en}`, {
            duration: 7000,
            position: "bottom-left",
            theme: "toasted-primary",
            action: this.toastedActions({
              type: type, 
              type_id: newVal.data.type_id, 
              notif_id: newVal.data.id
            })
          })
        }
        else {
          this.$toasted.info(this.isSerbian ? newVal.data.body_sr : newVal.data.body_en, {
            duration: 7000,
            position: "bottom-left",
            action: this.toastedActions({
              type: type, 
              type_id: newVal.data.type_id, 
              notif_id: newVal.data.id
            })
          })
        }
      }
    }
  },
  methods: {
    toastedActions({type, type_id, notif_id}) {
      const currentPath = this.$route.path
      let shouldSetSeen = true
      let shouldSetOpen = true
      if(((type == 1 || type == 6 || type == 7 || type == 10 || type == 2 || type == 5) && currentPath == "/viewRequest/" + type_id) ||
          (type == 8 && currentPath == "/ratings/" + this.$store.state.authUser.id) || 
          (type == 9 && currentPath == "/achievements/" + this.$store.state.authUser.id)) {
            shouldSetSeen = false
            shouldSetOpen = false
          }
      if(currentPath == "/notifications") {
        shouldSetSeen = false
      }
      if(type == 0 || type == 3 || type == 4) {
        shouldSetOpen = false
      }

      let actions = null

      if(shouldSetSeen) {
        actions = [{
          text: this.closeText,
          onClick: (e, toastObj) => {
            this.$store.state.notificationNumber -= 1
            this.$store.dispatch("setNotificationFlag", {ids : [notif_id], opened: false, seen: true})
            this.setFlagsInStore(notif_id, false, true)
            toastObj.goAway(0)
          }
        }]
      }
      else {
        actions = [{
          text: this.closeText,
          onClick: (e, toastObj) => {
            toastObj.goAway(0)
          }
        }]
      }

      let functionForRouter = null
      if(type == 1 || type == 6 || type == 7 || type == 10) {
        functionForRouter = () => {
          this.$router.push({ name: "PageViewRequest", params: { id: type_id }})
        }
      }
      else if (type == 8) {
        functionForRouter = () => {
          this.$router.push({ name: "PageRatings", params: { id: this.$store.state.authUser.id, user: this.$store.state.authUser }})
        }
      }
      else if (type == 9) {
        functionForRouter = () => {
          this.$router.push({ name: "PageAchievements", params: { id: this.$store.state.authUser.id, user: this.$store.state.authUser }})
        }
      }
      else if(type == 2 || type == 5) {
        functionForRouter = () => {
          this.$router.push({
            name: "PageViewRequest",
            params: {
              id: type_id,
              startingView: (type == 2) ? 'Offers' : 'Edits'
            }
          })
        }
      }

      if(shouldSetOpen) {
        actions.push({
          text: this.showMoreText,
          onClick: (e, toastObj) => {
            this.$store.state.notificationNumber -= 1
            functionForRouter()
            this.$store.dispatch("setNotificationFlag", {ids: [notif_id], opened: true, seen: true})
            this.setFlagsInStore(notif_id, true, true)
            toastObj.goAway(0)
          }
        })
      }
      return actions
    },
    setFlagsInStore(id, opened, seen) {
      if(this.$store.state.notifications != null) {
        let ind = -1
        ind = this.$store.state.notifications.findIndex(not => not.id == id)
        if(ind != -1) {
          this.$store.state.notifications[ind].seen = seen
          this.$store.state.notifications[ind].opened = opened
        }
      }
    },
    shouldSetFlags(type, type_id) {
      let ret = {}
      const currentRoute = this.$route.path
      if(type == 0 || type == 3 || type == 4) {
        if(currentRoute == "/notifications") {
          this.$store.state.notificationNumber -= 1
          ret.setSeen = true
          ret.setOpened = false
        }
        else ret = null
        return ret
      }
      else if(((type == 1 || type == 6 || type == 7 || type == 10 || type == 2 || type == 5) && currentRoute == "/viewRequest/" + type_id) ||
              (type == 8 && currentRoute == "/ratings/" + this.$store.state.authUser.id) ||
              (type == 9 && currentRoute == "/achievements/" + this.$store.state.authUser.id)) {
                this.$store.state.notificationNumber -= 1
                ret.setSeen = true
                ret.setOpened = true
                return ret
              }
      if(currentRoute == "/notifications") {
        this.$store.state.notificationNumber -= 1
        ret.setSeen = true
        ret.setOpened = false
        return ret
      }
      return null
    }
  },
  created()
  {
    this.user.first_name = this.$cookie.get('ime');
    this.user.last_name = this.$cookie.get('prezime');
    var firebase = this.$cookie.get('firebaseToken')
    if(this.user.first_name != null && this.user.last_name != null)
    {
      this.$store.state.authUser = this.user
      this.$store.state.logedIn = true
      this.$store.state.isAdmin = this.$cookie.get('admin')
      if(!this.$store.state.isAdmin || this.$store.state.isAdmin == "false") 
      {
        const store = this.$store
        const vm = this
        this.$store.state.firebaseToken = this.$cookie.get('firebaseToken')
        this.$store.state.registeredOnFirebase = true
        this.$store.state.firebaseOnMessageFunction = this.$messaging.onMessage(function(payload) {
          if(payload.data.notification_type == 11) {
            vm.$toasted.clear()
            store.state.logedIn = false
            vm.$router.push('/login')
            if(vm.isAdmin)
                store.dispatch("logoutUser")
            else
                store.dispatch("unregisterFromFirebase")
            store.state.showModalBan = payload.data
          }
          else {
            store.state.notificationNumber += 1
            store.dispatch('fillFirebaseNotification', payload)
            store.dispatch('notificationReaction', payload.data)
            const flags = vm.shouldSetFlags(payload.data.notification_type, payload.data.type_id)
            if(store.state.notifications != null)
              store.dispatch('fetchSpecificNotification', {id: payload.data.id, flags: flags})
            if(flags != null)
              store.dispatch("setNotificationFlag", {ids: [payload.data.id], opened: flags.setOpened, seen: flags.setSeen})
            console.log("app.vue notification")
            console.log(payload)
          }
        })
      }
      this.user.token = this.$cookie.get('token');
      this.$store.state.token =  this.$cookie.get('token');
      this.user.id = this.$cookie.get('id');
      this.$store.dispatch("getUserById", this.user)
      if(!this.$store.state.isAdmin || this.$store.state.isAdmin == "false")
        this.$store.dispatch("getNotificationNumber")
    }
    else if(firebase != null)
    {
      this.$store.dispatch("unregisterLogedoutFromFirebase", {token: this.$cookie.get('token'), firebaseToken: firebase})
    }
  }
}
</script>

<style>
#app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }

  .bold {
    font-weight: bold;
  }

  .cover {
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }

  .hero {
    position: relative;
  }

  .hero-body {
    padding: 3rem 1.5rem;
  }

  .hero-bg {
    background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1531263060782-b024de9b9793?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1650&q=80');
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }

  .page-wrapper
  {
    min-height: 93vh;
  }

  .top-div
  {
    background-color: rgba(138, 141, 145, 0.767);
    height: 12vh;
    color: white;
    padding: 27px;
    padding-left:100px;
    font-size: 35px;
    font-weight: bold;
  }

  .please
  {
    position: sticky;
    top: 0;
    width: 100%;
    background-color: white;
    z-index: 1000000000;
  }

  .input-padding
  {
    padding:5px;
  }

  .toaster-cl {
    max-width: 500px !important;
    font-size: 15px;
    word-wrap: break-word;
    z-index: 999999999999999 !important;
  }

  @media only screen and (max-width: 600px) {
    .toaster-cl {
      top: 72px !important;
    }
  }

  @media only screen and (max-width: 499px) {
    .toaster-cl {
      top: 87px !important;
    }
  }
</style>
