<template>
  <Spinner v-if="!$store.state.isDataLoaded" />
  <div class = "wrapper" v-else>
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
import Spinner from "@/components/Spinner"

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
    Spinner
  },
  data() {
    return {
      componentToShow: "Info",
      computedUser: null
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isMyProfile() {
      return this.$route.params.id == this.$store.state.authUser.id
    }
  },
  methods: {
    editSaved() {
      this.componentToShow = "Info"
      this.computedUser = this.$store.state.authUser
    },
    changedRoute() {
      //fetch-ovati sve korisnikove home adrese
      let vm = this
      function callback() {
        if(vm.$store.state.isDataLoaded)
          vm.computedUser = vm.$store.state.user
        else 
          setTimeout(callback, 200)
      }
      if(!this.user)
      {
        const routeId = this.$route.params.id
        if(routeId == this.$store.getters['getAuthUserId'])
        {
          this.computedUser = this.$store.state.authUser
        }
        else {
          this.$store.dispatch('getUserInfo', routeId)
          callback()
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