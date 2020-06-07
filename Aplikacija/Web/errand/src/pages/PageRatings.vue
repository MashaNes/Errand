<template>
  <Spinner v-if="!userLoaded || !ratings"/>
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
      const routeId = this.$route.params.id

      if(!this.user && !this.isMyProfile)
      {
          this.$store.dispatch('getUserInfo', routeId)
      }
      else if(!this.isMyProfile) 
        this.$store.state.user = this.user

      if((this.isMyProfile && !this.$store.state.authUserRatings) || (!this.isMyProfile))
        this.$store.dispatch('fillUserRatings', routeId)
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