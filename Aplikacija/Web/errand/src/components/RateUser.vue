<template>
  <div class="main-wrapper">
    <div class="side-info">
      <AsideProfileInfo :user="user" />
      <div class="btns">
        <router-link :to = "'/profile/' + user.id" class="button is-primary" style="width:100%;">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </router-link>
      </div>  
    </div>
    <div class="req-wrap">
      <div 
        class = "wrapper" 
        :class="color(request)"
        v-for="request in requests"
        :key="request.id"
      >
        <div class="request-top">
          <div class = "request-name">
              {{request.name}}
          </div>
        </div>
        <div class="request-bottom">
          <div class="status-div">
            <img v-if="request.status == 'finished'" src = "../assets/finished.svg">
            <img v-if="request.status == 'failed'" src = "../assets/failed.svg">
            <span class = "request-status"> {{request.status}} </span>
          </div>
          <div class = "bottom-left">
            <div class = "request-date"> {{request.date | showTime}} </div>
            <div class = "tagovi">
              <div v-for="tag in request.tags" :key="tag" class = "request-tag">{{tag}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import AsideProfileInfo from "@/components/AsideProfileInfo"

export default {
  
  components: {
    AsideProfileInfo
  },
  props: {
    user: {
      required: true,
      type: Object
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    requests() {
      return this.$store.state.specificRequests
    }
  },
  methods: {
    cancel() {
      this.$emit('cancelRate')
    },
    rateUser() {
      this.$store.dispatch('')
    },
    color(request)
    {
      if (request.status == "finished")
          return "zeleno"
      else 
          return "crveno"
    }
  },
  created() {
    const userName = this.user.firstName
    console.log(userName)
    this.$store.dispatch('fillSpecificRequests', userName)
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
    margin-bottom:10px;
    margin-top:10px;
  }

  .req-wrap {
    width:100%;
    display: flex;
    flex-direction:column;
    align-items:center;
  }

  .wrapper
  {
    margin:10px;
    border: 1px solid rgb(43, 41, 41);
    border-radius: 15px;
    min-height: 100px;
    padding:5px;
    width: 80%;
    display:flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .request-top
  {
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
  }

  .request-bottom
  {
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    margin-top: 30px;
  }

  .user-div
  {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-items: center;
    margin-right: 10px;
    margin-top:10px;
    font-size: 18px;
  }

  .status-div
  {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-items: flex-end;
    margin-right: 10px;
    font-size: 18px;
  }

  .tagovi
  {
    display: flex;
    flex-direction: row;
  }
    
  .bottom-left
  {
    flex-grow: 1;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
  }
  
  .request-date
  {
    padding: 10px;
    text-align: center;
  }

  .user-name
  {
    margin-right: 10px;
  }

  @media only screen and (max-width:600px)
  {
    .wrapper
    {
      width:95%;
    }

    .request-top
    {
      flex-direction: column-reverse;
      align-items: flex-start;
    }

    .user-div
    {
      margin-left: 15px;
      margin-top: 10px;
      flex-direction: row-reverse;
    }

    .bottom-left
    {
      flex-direction: column;
      align-items: flex-start;
    }

    .request-bottom
    {
      margin-top: 10px;
    }

    .tagovi
    {
      margin-bottom: 5px;
      margin-left: 5px;
    }
    
    .request-date
    {
      padding: 2px;
      padding-left: 10px;
    }

    .user-name
    {
      margin-left: 10px;
    }
  }

  @media only screen and (max-width: 499px)
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
      top:85px;
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

  .request-name
    {
        font-weight: bold;
        font-size: 25px;
        color:rgb(11, 97, 126);
        text-align: left;
        flex-grow: 1;
        margin-left: 10px;
        margin-top:2px;
    }

    .request-tag
    {
      border-radius: 5px;
      padding-left:7px;
      padding-right:7px;
      padding-top:5px;
      margin:5px;
      background-color: rgb(15, 170, 221);
      color: white;
      font-weight: bold;
    }

    .request-status
    {
      margin-left:7px;
    }

    .crveno
    {
      background-color: rgb(255, 212, 212);
    }

    .zeleno
    {
      background-color: rgb(217, 250, 217);
    }


</style>