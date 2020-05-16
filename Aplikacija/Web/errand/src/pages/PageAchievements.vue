<template>
  <Spinner v-if="!computedUser || !achievements"/>
  <AchAndRatings 
    :tab="'Achievements'" 
    :user="computedUser"
    :achievements="achievements"
    :RequestSelect="RequestSelect"
    v-else
  />
    <!-- :isMyProfile="isMyProfile" -->
</template>

<script>
import AchAndRatings from "@/components/AchAndRatings"
import Spinner from "@/components/Spinner"

export default {
  //prebaciti da 'user' bude properti, i da ga salje roditelj;
  //izbaciti isMyProfile, i ne prosledjivati ga uopste
  props: {
    user: {
      required: false
    },
    RequestSelect:
    {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      //isMyProfile: true
      computedUser: null,
      achievements: null
    }
  },
  components: {
    AchAndRatings,
    Spinner 
  },
  methods: {
    changedRoute() {
      //fetch-ovati sve korisnikove ocene; id se izvlaci iz user-a
      let vm = this
      this.achievements = null
      function callBackAchievements() {
        if(vm.$store.state.isDataLoaded)
        {
          vm.achievements = vm.$store.state.userAchievements
        }
        else 
          setTimeout(callBackAchievements, 200)
      }
      function callbackUser() {
        if(vm.$store.state.isDataLoaded)
        {
          vm.computedUser = vm.$store.state.user
          vm.$store.dispatch('getUserAchievements', vm.computedUser.id)
          callBackAchievements()
        }
        else 
          setTimeout(callbackUser, 200)
      }

      if(!this.user)
      {
        const routeId = this.$route.params.id
        if(routeId == this.$store.getters['getAuthUserId'])
        {
          this.computedUser = this.$store.state.authUser
          vm.$store.dispatch('getUserAchievements', vm.computedUser.id)
          callBackAchievements()
        }
        else {
          this.$store.dispatch('getUserInfo', routeId)
          callbackUser()
        }
      }
      else 
      {
        this.computedUser = this.user
        vm.$store.dispatch('getUserAchievements', vm.computedUser.id)
        callBackAchievements()
      }
    }
  },
  created() {
    this.changedRoute()
  },
  watch: {
    $route() {
      this.changedRoute()
    }
  } 
}
</script>

<style scoped>

</style>