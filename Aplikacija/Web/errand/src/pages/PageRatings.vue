<template>
  <div>
    <div>
      <AchAndRatings 
        :tab="'Ratings'" 
        :user="user"
        :isMyProfile="isMyProfile"
      />
    </div>
  </div>
</template>

<script>
import AchAndRatings from "@/components/AchAndRatings"

export default {
  data() {
    return {
      user: {},
      isMyProfile: true
    }
  },
  components: {
      AchAndRatings
  },
  methods: {
    changedRoute() {
      const routeId = this.$route.params.id
      this.$store.dispatch('getUserRatings', routeId)
      if(routeId == this.$store.getters['getAuthUserId'])
        this.user = this.$store.state.authUser
      else {
        this.$store.dispatch('getUser', routeId)
        this.user = this.$store.state.user
        this.isMyProfile = false
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