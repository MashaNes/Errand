<template>
  <Spinner v-if="!computedUser || !fetchedServices"/>
  <div class="main-wrapper" v-else>
    <div class="side-info">
      <AsideProfileInfo :user="computedUser" :forWideScreen="true" />
      <div class="btns">
        <router-link :to="goToProfile()" class="button is-primary" style="width:100%;">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </router-link>
      </div>
    </div>
    <div class="services-div">
      <ServiceList :editable="false" />
    </div>
  </div>
</template>

<script>
import AsideProfileInfo from "@/components/AsideProfileInfo"
import ServiceList from "@/components/ServiceList"
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
    }
  },
  components: {
    AsideProfileInfo,
    ServiceList,
    Spinner
  },
  data() {
    return {
      computedUser: null,
      fetchedServices: false
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    }
  },
  methods: {
    onCreate() {
      let vm = this
      function callBackServices() {
        if(!vm.$store.state.isDataLoaded)
          setTimeout(callBackServices, 200)
        else
        {
          vm.fetchedServices = true
        }
      }
      function callbackUser() {
        if(vm.$store.state.isDataLoaded)
        {
          vm.computedUser = vm.$store.state.user
          vm.$store.dispatch('fillNotAuthUserServices', vm.computedUser.id)
          callBackServices()
        }
        else 
          setTimeout(callbackUser, 200)
      }

      if(!this.user)
      {
        const routeId = this.$route.params.id
        this.$store.dispatch('getUserInfo', {userId: routeId, onlyRating: false, userToSet: this.user})
        callbackUser()
      }
      else 
      {
        this.computedUser = this.user
        this.$store.dispatch('fillNotAuthUserServices', this.computedUser.id)
        callBackServices()
      }
    },
    goToProfile() {
      return {
        name: "PageViewProfile",
        params: {
          id: this.computedUser.id,
          user: this.computedUser,
          RequestSelect:this.RequestSelect
        }
      }
    }
  },
  created() {
    this.onCreate()
  },
  watch: {
    $route() {
      this.onCreate()
    }
  }
}
</script>

<style scoped>

  .main-wrapper {
    display:flex; 
    flex-direction:row;
    min-height:83vh;
  }

  .side-info {
    display:flex;
    flex-direction: column;
    align-items: center;
    position: sticky;
    top:20%;
    align-self:flex-start;
    margin: 15px 1% 15px 2%;
    z-index: 1;
    word-break: break-all;
    font-size:15px;
    max-width: 250px;
  }

  .btns {
    display:flex;
    flex-direction:column;
    width:100%;
  }

  .button {
    width:100%;
    margin: 10px 5px 10px 5px;
  }

  .services-div {
    width:100%;
  }

  @media only screen and (max-width: 599px)
  {
    .main-wrapper {
      flex-direction: column;
    }
    .side-info {
      flex-direction: column;
      margin:0 10px 0 10px;
      align-self:left;
      border-right: 1px solid black;
      border-left: 1px solid black;
      border-bottom: 1px solid black;
      top: 70px;
      font-size: 13px;
      background-color:white;
      border-radius: 0 0 10px 10px;
      justify-items: baseline;
      word-break: break-all;
      max-width:initial;
    }

    .btns {
      flex-direction:row;
    }

    .button {
      font-size: 15px;
      height: 30px;
      width:100%;
      margin: 5px 5px 5px 5px;
    }
  }

  @media only screen and (max-width: 499px)
  {
    .side-info{
      top:85px;
    }
  }

</style>