<template>
  <Spinner v-if="!requests" />
  <div class="main-wrapper" v-else>
    <div class="side-info">
      <AsideProfileInfo :user="user" :forWideScreen="true" />
      <div class="btns">
        <b-button @click="$emit('cancelRate')" class="button is-primary" style="width:100%;">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </b-button>
      </div>  
    </div>
    <div class="request-side">
      <div class="chk-rated-wrap">
        <div class="chk-rated-div">
          <input type="checkbox" v-model="showAll" class="chk-box" />
          <span v-if="!showAll" v-text="isSerbian ? 'Prikaži i ocenjene zahteve' : 'Show rated requests too'"></span>
          <span v-else v-text="isSerbian ? 'Prikaži samo neocenjene zahteve' : 'Show only unrated requests'"></span> 
        </div>
      </div>
      <div class="req-wrap">
        <RateOrReportBox v-for="request in requests.results" :key="request.id" :request="request" :user="user" />
      </div>
    </div>
  </div>
</template>

<script>

import AsideProfileInfo from "@/components/AsideProfileInfo"
import Spinner from "@/components/Spinner"
import RateOrReportBox from "@/components/RateOrReportBox"

export default {
  
  components: {
    AsideProfileInfo,
    Spinner,
    RateOrReportBox
  },
  props: {
    user: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      showModalAreYouSure: false,
      rating: {},
      showAll: false
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
      if(!this.$store.state.unratedRequestsCreated || !this.$store.state.unratedRequestsDoneBy)
        return null
      else {
        return {
          count: this.$store.state.unratedRequestsCreated.count + this.$store.state.unratedRequestsDoneBy.count,
          results: this.$store.state.unratedRequestsCreated.results.concat(this.$store.state.unratedRequestsDoneBy.results)
        }
      }
    }
  },
  created() {
    this.$store.state.specificRequestsCreated = null
    this.$store.state.specificRequestsDoneBy = null
    window.scrollTo(0, 0)
    const filtersCreated = {
      created_by : this.user.id,
      done_by : this.$store.state.authUser.id,
      created_or_done_by: null,
      statuses : [2, 3],
      unrated_created_by : null,
      unrated_done_by : true
    }
    this.$store.dispatch("fillRequests", {filters: filtersCreated, objectToFill: "unratedRequestsCreated"})

    const filtersDone = {
      created_by : this.$store.state.authUser.id,
      done_by : this.user.id,
      created_or_done_by: null,
      statuses : [2, 3],
      unrated_created_by : true,
      unrated_done_by : null
    }
    this.$store.dispatch("fillRequests", {filters: filtersDone, objectToFill: "unratedRequestsDoneBy"})
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

  .request-side {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .chk-rated-wrap {
    width: 100%;
    display: flex;
  }

  .chk-rated-div {
    margin: 30px 15% 10px 15%;
    border: 1px solid black;
    display: flex;
    align-items: center;
    padding: 10px;
    background-color: white;
    border-radius: 10px;
  }

  .req-wrap {
    width:100%;
  }

  .request-btns {
    display: flex;
    flex-direction:row;
    flex-grow:1;
    justify-content: flex-end;
  }

  .req-btn {
    margin: 5px 5px 5px 5px;
    font-size:12px;
    font-weight: bold;
    background-color: rgb(15, 170, 221);
    border:hidden;
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

  .image {
    height: 22px;
    width: 22px;
    margin-right: 10px;
  }

  .chk-box {
    height: 15px;
    width: 15px;
    margin-right: 10px;
    margin-bottom: 2px;
  }

  @media only screen and (max-width:900px) {
    .chk-rated-div {
      margin: 30px 10% 10px 10%;
    }
  }

  @media only screen and (max-width:600px)
  {
    .chk-rated-div {
      margin: 20px 5% 10px 5%;
    }

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
      top:70px;
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

  @media only screen and (max-width: 499px) {
    .side-info {
      top: 85px;
    }
  }
</style>