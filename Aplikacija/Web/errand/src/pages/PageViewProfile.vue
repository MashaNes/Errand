<template>
    <div class = "wrapper">
      <ProfileInfo 
        v-if="!showProfileEdit"
        :user="user" 
        :isMyProfile="isMyProfile"
        @editProfile="showProfileEdit=true"
      />
      <EditProfileInfo
        v-if="showProfileEdit"
        :user="user"
        @saveEditChanges="showProfileEdit=false"
      />
    </div>
</template>

<script>

import ProfileInfo from "@/components/ProfileInfo"
import EditProfileInfo from "@/components/EditProfileInfo"

export default {
  components: {
    ProfileInfo,
    EditProfileInfo
  },
  data() {
    return {
      showProfileEdit: false,
      user: {},
      isMyProfile: true
    }
  },
  computed: {
    // user() {
    //   // eslint-disable-next-line no-debugger
    
    //   return this.$store.state.user
    // },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    progressBarVariant() {
      return this.user.rating < 2 ? 'danger' : 
             this.user.rating < 5 ? 'warning' : 'success'
    }
  },
  created() {
    const routeId = this.$route.params.id
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
  
  .wrapper
  {
    padding-top: 10px;
  }

</style>