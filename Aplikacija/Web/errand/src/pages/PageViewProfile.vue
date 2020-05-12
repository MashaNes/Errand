<template>
  <Spinner v-if="!computedUser || !addresses" />
  <div class = "wrapper" v-else>
    <ProfileInfo 
      v-if="componentToShow == 'Info'"
      :user="computedUser" 
      :isMyProfile="isMyProfile"
      :addresses="addresses"
      @editProfile="componentToShow = 'Edit'"
      @rateUser="componentToShow = 'Rate'"
    />
    <EditProfileInfo
      v-if="componentToShow == 'Edit'"
      :user="computedUser"
      :addresses="addresses"
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
      computedUser: null,
      addresses: null
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isMyProfile() {
      return this.$route.params.id == this.$store.getters['getAuthUserId']
    }
  },
  methods: {
    editSaved() {
      console.log("SAVED")
      this.componentToShow = "Info"
      this.computedUser = this.$store.state.authUser
      this.addresses = this.$store.state.userAddresses
    },
    changedRoute() {
      //fetch-ovati sve korisnikove home adrese
      let vm = this
      function callbackUser() {
        if(vm.$store.state.isDataLoaded)
          vm.computedUser = vm.$store.state.user
        else 
          setTimeout(callbackUser, 200)
      }
      function callbackAddresses() {
        if(vm.$store.state.userAddresses)
          vm.addresses = vm.$store.state.userAddresses
        else
          setTimeout(callbackAddresses, 200)
      }
      
      const routeId = this.$route.params.id

      if(!this.user)
      {
        if(this.isMyProfile)
        {
          this.computedUser = this.$store.state.authUser
        }
        else {
          this.$store.dispatch('getUserInfo', routeId)
          callbackUser()
        }
        this.componentToShow = 'Info'
      }
      else this.computedUser = this.user
      if(this.isMyProfile) {
        this.$store.dispatch('fillUserAddresses', routeId)
        callbackAddresses()
      }
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