<template>
  <Spinner v-if="!computedUser || (isMyProfile && !addresses)" />
  <div class = "wrapper" v-else>
    <ProfileInfo 
      v-if="componentToShow == 'Info'"
      :user="computedUser" 
      :isMyProfile="isMyProfile"
      :RequestSelect="RequestSelect"
      :RequestView="RequestView"
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
    <RateOrReportUser
      v-if="componentToShow == 'Rate'"
      :user="computedUser"
      @cancelRate="componentToShow = 'Info'"
    />
  </div>
</template>

<script>

import ProfileInfo from "@/components/ProfileInfo"
import EditProfileInfo from "@/components/EditProfileInfo"
import RateOrReportUser from "@/components/RateOrReportUser"
import Spinner from "@/components/Spinner"

export default {
  props: {
    user: {
      required: false
    },
    RequestSelect:
    {
      type: Boolean,
      required: false,
      default: false
    },
    RequestView: {
      type: Object,
      required: false,
      default: null
    }
  },
  components: {
    ProfileInfo,
    EditProfileInfo,
    RateOrReportUser,
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
        vm.$store.state.userAddresses = null
        this.$store.dispatch('fillUserAddresses', routeId)
        callbackAddresses()
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
  
  /* @media only screen and (min-width: 499px)
  {
    .wrapper
    {
      padding-top: 10px;
    }
  } */

</style>