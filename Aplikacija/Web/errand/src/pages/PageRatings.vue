<template>
  <Spinner v-if="!computedUser || !ratings"/>
  <div v-else>
    <div>
      <AchAndRatings 
        :tab="'Ratings'" 
        :user="computedUser"
        :ratings="ratings"
        :RequestSelect="RequestSelect"
      />
    </div>
  </div>
</template>

<script>
import AchAndRatings from "@/components/AchAndRatings"
import Spinner from "@/components/Spinner"

export default {
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
      computedUser: null,
      ratings: null
    }
  },
  components: {
    AchAndRatings,
    Spinner
  },
  methods: {
    changedRoute() {
      let vm = this
      this.ratings = null
      function callBackRatings() {
        if(vm.$store.state.isDataLoaded)
        {
          vm.ratings = vm.$store.state.userRatings
        }
        else 
          setTimeout(callBackRatings, 200)
      }
      function callbackUser() {
        if(vm.$store.state.isDataLoaded)
        {
          vm.computedUser = vm.$store.state.user
          vm.$store.dispatch('fillUserRatings', vm.computedUser.id)
          callBackRatings()
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
          vm.$store.dispatch('fillUserRatings', vm.computedUser.id)
          callBackRatings()
        }
        else {
          this.$store.dispatch('getUserInfo', routeId)
          callbackUser()
        }
      }
      else 
      {
        this.computedUser = this.user
        vm.$store.dispatch('fillUserRatings', vm.computedUser.id)
        callBackRatings()
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