<template>
    <div class = "wrapper">
      <ProfileInfo 
        v-if="componentToShow == 'Info'"
        :user="computedUser" 
        :isMyProfile="isMyProfile"
        @editProfile="componentToShow = 'Edit'"
        @rateUser="componentToShow = 'Rate'"
      />
      <EditProfileInfo
        v-if="componentToShow == 'Edit'"
        :user="computedUser"
        @saveEditChanges="editSaved"
        @cancelChanges="componentToShow = 'Info'"
      />
      <RateUser
        v-if="componentToShow == 'Rate'"
        :user="computedUser"
        @cancelRate="componentToShow = 'Info'"
      />
    </div>
</template>

<script>

import ProfileInfo from "@/components/ProfileInfo"
import EditProfileInfo from "@/components/EditProfileInfo"
import RateUser from "@/components/RateUser"

export default {
  props: {
    user: {
      required: false
    }
  },
  components: {
    ProfileInfo,
    EditProfileInfo,
    RateUser,
  },
  data() {
    return {
      componentToShow: "Info",
      isMyProfile: true,
      computedUser: null
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    }
  },
  methods: {
    editSaved() {
      this.isMyProfile = true
      this.componentToShow = "Info"
      this.user = this.$store.state.authUser
    },
    changedRoute() {
      //fetch-ovati sve korisnikove home adrese
      // eslint-disable-next-line no-debugger
      //debugger
      if(!this.user)
      {
        const routeId = this.$route.params.id
        if(routeId == this.$store.getters['getAuthUserId'])
        {
          this.computedUser = this.$store.state.authUser
          this.isMyProfile = true
        }
        else {
          this.$store.dispatch('getUser', routeId)
          this.computedUser = this.$store.state.user
          this.isMyProfile = false
        }
        this.componentToShow = 'Info'
      }
      else this.computedUser = this.user
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