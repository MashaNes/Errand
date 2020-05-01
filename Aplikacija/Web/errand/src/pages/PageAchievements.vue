<template>
  <div>
    <AchAndRatings 
      :tab="'Achievements'" 
      :user="user"
      :isMyProfile="isMyProfile"
    />
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
  created() {
    const routeId = this.$route.params.id
    this.$store.dispatch('getUserAchievements', routeId)
    if(routeId == this.$store.getters['getAuthUserId'])
      this.user = this.$store.state.authUser
    else {
      this.$store.dispatch('getUser', routeId)
      this.user = this.$store.state.user
      this.isMyProfile = false
    }
  }
}
</script>

<style scoped>

</style>