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
        this.$toasted.show(this.isSerbian ? newVal.data.body_sr : newVal.data.body_en, {
          duration: 5000,
          position: "bottom-left",
          theme: "toasted-primary",
          // containerClass: "toaster-cl",
          // className: "toaster-cl",
          action: this.toastedActions({
            type: newVal.data.notification_type, 
            type_id: newVal.data.type_id, 
            notif_id: newVal.data.id
          })
        })
      }
    }
  },
  methods: {
    toastedActions({type, type_id, notif_id}) {
      //const currentPath = this.$store.getters["getCurrentRoute"]
      const actions = [{
        text: this.closeText,
        onClick: (e, toastObj) => {
          this.$store.state.notificationNumber -= 1
          this.$store.dispatch("setNotificationFlag", {ids : [notif_id], opened: false, seen: true})
          toastObj.goAway(0)
        }
      }]

      if(type != 0 && type != 3 && type != 4) {
        actions.push({
          text: this.showMoreText,
          onClick: (e, toastObj) => {
            this.$store.state.notificationNumber -= 1
            this.$store.dispatch("setNotificationFlag", {ids : [notif_id], opened: true, seen: true})
            if(type == 1 || type == 6 || type == 7 || type == 10)
                this.$router.push({ name: "PageViewRequest", params: { id: type_id }})
            else if(type == 8)
                this.$router.push({ name: "PageRatings", params: { id: type_id, user: this.$store.state.authUser }})
            else if(type == 9)
                this.$router.push({ name: "PageAchievements", params: { id: type_id, user: this.$store.state.authUser }})
            else if(type == 2 || type == 5)
                this.$router.push({ name: "PageViewRequest", params: { id: type_id }})
            toastObj.goAway(0)
          }
        })
      }
      return actions
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
        this.$store.state.firebaseToken = this.$cookie.get('firebaseToken')
        this.$store.state.registeredOnFirebase = true
        this.$store.state.firebaseOnMessageFunction = this.$messaging.onMessage(function(payload) {
          store.state.notificationNumber += 1
          store.dispatch('fillFirebaseNotification', payload)
          store.dispatch('notificationReaction', payload.data)
          console.log("app.vue notification")
          console.log(payload)
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
