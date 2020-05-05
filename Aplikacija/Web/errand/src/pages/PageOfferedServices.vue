<template>
  <div class="main-wrapper">
    <div class="side-info">
      <AsideProfileInfo :user="user" :forWideScreen="true" />
      <div class="btns">
        <router-link :to = "'/profile/' + user.id" class="button is-primary" style="width:100%;">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </router-link>
      </div>
    </div>
    <div>
      <ServiceList :editable="false" />
    </div>
  </div>
</template>

<script>
import AsideProfileInfo from "@/components/AsideProfileInfo"
import ServiceList from "@/components/ServiceList"

export default {
  components: {
    AsideProfileInfo,
    ServiceList
  },
  data() {
    return {
      user: {}
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    }
  },
  created() {
    const routeId = this.$route.params.id
    this.$store.dispatch('getUser', routeId)
    this.user = this.$store.state.user
    this.isMyProfile = false
    this.$store.dispatch('fillUserServices')
  }
}
</script>

<style scoped>

  .main-wrapper {
    display:flex; 
    flex-direction:row;
    min-height:73vh;
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