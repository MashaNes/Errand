<template>
  <Spinner v-if="!userLoaded || !achievements"/>
  <AchAndRatings 
    :tab="'Achievements'" 
    :user="isMyProfile ? $store.state.authUser : computedUser"
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
    achievements() {
      if(this.isMyProfile)
        return this.$store.state.authUserAchievements
      else
        return this.$store.state.userAchievements
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
      this.$store.state.userAchievements
      const routeId = this.$route.params.id

      if(!this.user && !this.isMyProfile)
      {
          this.$store.dispatch('getUserInfo', routeId)
      }
      else if(!this.isMyProfile) 
        this.$store.state.user = this.user

      if((this.isMyProfile && !this.$store.state.authUserAchievements) || (!this.isMyProfile))
        this.$store.dispatch('getUserAchievements', routeId)
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