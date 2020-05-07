<template>
    <AchAndRatings 
      :tab="'Achievements'" 
      :user="user"
      :isMyProfile="isMyProfile"
    />
</template>

<script>
import AchAndRatings from "@/components/AchAndRatings"

export default {
  //prebaciti da 'user' bude properti, i da ga salje roditelj;
  //izbaciti isMyProfile, i ne prosledjivati ga uopste
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
      //fetch-ovati sve korisnikove ocene; id se izvlaci iz user-a
      console.log("changed route")
      const routeId = this.$route.params.id
      this.$store.dispatch('getUserAchievements', routeId)
      if(routeId == this.$store.getters['getAuthUserId'])
      {
        this.user = this.$store.state.authUser
        this.isMyProfile = true
      }
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