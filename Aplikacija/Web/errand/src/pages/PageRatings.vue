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
  //prebaciti da user bude properti i da ga salje roditelj
  //izbaciti isMyProfile
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
      //fetch-ovati sve korisnikove ocene; id izvuci iz user-a
      const routeId = this.$route.params.id
      this.$store.dispatch('getUserRatings', routeId)
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