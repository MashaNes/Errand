<template>
  <Spinner v-if="(!userLoaded || !ratings) && !initialFetchDone"/>
  <div v-else>
    <div>
      <AchAndRatings 
        :tab="'Ratings'" 
        :user="isMyProfile ? $store.state.authUser : computedUser"
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
      initialFetchDone: false
    }
  },
  computed: {
    isMyProfile() {
      return this.$route.params.id == this.$store.getters['getAuthUserId']
    },
    computedUser() {
      return this.$store.state.user
    },
    ratings() {
      if(this.isMyProfile)
        return this.$store.state.authUserRatings
      else
        return this.$store.state.userRatings
    },
    userLoaded() {
      if(!this.isMyProfile)
        return this.computedUser != null
      else
        return true
    }
  },
  components: {
    AchAndRatings,
    Spinner
  },
  methods: {
    changedRoute() {
      this.$store.state.userRatings = null
      this.$store.state.authUserRatings = null
      const routeId = this.$route.params.id

      if(!this.user && !this.isMyProfile)
      {
          this.$store.dispatch('getUserInfo', routeId)
      }
      else if(!this.isMyProfile) 
        this.$store.state.user = this.user
      this.$store.dispatch('fillUserRatings', {
        userId: routeId,
        endpoint: "http://localhost:8000/api/v1/user_info_filtered/?paginate=true"
      })

      this.initialFetchDone = true
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