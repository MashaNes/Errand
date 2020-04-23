<template>
    <div>
      <div class = "top-div" v-if="isSerbian"> Profil </div>
      <div class = "top-div" v-else> Profile </div>
      <ProfileInfo 
        v-if="!showProfileEdit"
        :user="user" 
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
      showProfileEdit: false
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
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
  }
}
</script>

<style scoped>
  
  

</style>