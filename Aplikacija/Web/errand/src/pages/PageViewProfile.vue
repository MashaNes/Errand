<template>
    <div class = "wrapper">
      <ProfileInfo 
        v-if="componentToShow == 'Info'"
        :user="user" 
        :isMyProfile="isMyProfile"
        @editProfile="componentToShow = 'Edit'"
        @rateUser="componentToShow = 'Rate'"
      />
      <EditProfileInfo
        v-if="componentToShow == 'Edit'"
        :user="user"
        @saveEditChanges="editSaved"
        @cancelChanges="componentToShow = 'Info'"
      />
      <RateUser
        v-if="componentToShow == 'Rate'"
        :user="user"
        @cancelRate="componentToShow = 'Info'"
      />
    </div>
</template>

<script>

import ProfileInfo from "@/components/ProfileInfo"
import EditProfileInfo from "@/components/EditProfileInfo"
import RateUser from "@/components/RateUser"

export default {
  components: {
    ProfileInfo,
    EditProfileInfo,
    RateUser
  },
  data() {
    return {
      componentToShow: "Info",
      user: {},
      isMyProfile: true
    }
  },
  computed: {
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
  methods: {
    editSaved() {
      this.isMyProfile = true
      this.componentToShow = "Info"
      this.user = this.$store.state.authUser
    },
    changedRoute() {
      const routeId = this.$route.params.id
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
      this.componentToShow = 'Info'
    }
  },
  created() {
    this.changedRoute()
    // ako nije moj profil proveri da li je popunjena moja beneficirana lista, ako nije posalji get da se popuni
    //(onda se dodaje jedan computed isBenefited koji je true ako je ovaj user u listi beneficiranih, ako nije onda je false i na osnovu njega je dugme za dodavanje disabled ili ne)
  },
  watch: {
    $route() {
      this.changedRoute()
    }
  }
}
</script>

<style scoped>
  
  @media only screen and (min-width: 499px)
  {
    .wrapper
    {
      padding-top: 10px;
    }
  }

</style>