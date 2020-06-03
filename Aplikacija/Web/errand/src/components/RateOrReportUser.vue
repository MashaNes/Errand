<template>
  <div class="main-wrapper">
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
      <Spinner v-if="!requests" />
      <div class="req-wrap" v-else>
        <RateOrReportBox v-for="request in requests.results" :key="request.id" :request="request" :user="user" @setMessages="setMessages" />
      </div>
    </div>
    <ModalSuccess v-if="success && messagesSet" :textS="textMessageS" :textE="textMessageE" @close="closeModalSuccess"/>
  </div>
</template>

<script>

import AsideProfileInfo from "@/components/AsideProfileInfo"
import Spinner from "@/components/Spinner"
import RateOrReportBox from "@/components/RateOrReportBox"
import ModalSuccess from "@/components/ModalSuccess"

export default {
  
  components: {
    AsideProfileInfo,
    Spinner,
    RateOrReportBox,
    ModalSuccess
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
      showAll: false,
      fetchedAll: false,
      textMessageS: "",
      textMessageE: "",
      messagesSet: false
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
      if(!this.showAll) {
        if(!this.$store.state.unratedRequestsCreated || !this.$store.state.unratedRequestsDoneBy)
          return null
        else {
          return {
            count: this.$store.state.unratedRequestsCreated.count + this.$store.state.unratedRequestsDoneBy.count,
            results: this.$store.state.unratedRequestsCreated.results.concat(this.$store.state.unratedRequestsDoneBy.results)
          }
        }
      }
      else {
        if(!this.$store.state.ratedRequestsCreated || !this.$store.state.ratedRequestsDoneBy)
            return null
        else {
          return {
            count: this.$store.state.ratedRequestsCreated.count + this.$store.state.ratedRequestsDoneBy.count,
            results: this.$store.state.ratedRequestsCreated.results.concat(this.$store.state.ratedRequestsDoneBy.results)
          }
        }
      }
    },
    success() {
      return this.$store.state.success
    },
  },
  watch: {
    // eslint-disable-next-line no-unused-vars
    showAll(newVal, oldVal) {
      if(!this.fetchedAll) {
        if(newVal) {
          const filtersCreated = {
            created_by : this.user.id,
            done_by : this.$store.state.authUser.id,
            created_or_done_by: null,
            statuses : [2, 3],
            unrated_created_by : false,
            unrated_done_by : null
          }
          this.$store.dispatch("fillRequests", {filters: filtersCreated, objectToFill: {object: "ratedRequestsCreated", page: 1}})

          const filtersDone = {
            created_by : this.$store.state.authUser.id,
            done_by : this.user.id,
            created_or_done_by: null,
            statuses : [2, 3],
            unrated_created_by : null,
            unrated_done_by : false
          }
          this.$store.dispatch("fillRequests", {filters: filtersDone, objectToFill: {object: "ratedRequestsDoneBy", page: 1 }})
          this.fetchedAll = true
        }
      }
    }
  },
  methods: {
    closeModalSuccess() {
      this.$store.state.success = false
      this.messagesSet = false
    },
    setMessages({textMessageS, textMessageE}) {
      this.textMessageS = textMessageS
      this.textMessageE = textMessageE
      this.messagesSet = true
    }
  },
  created() {
    this.$store.state.unratedRequestsCreated = null
    this.$store.state.unratedRequestsDoneBy = null
    this.$store.state.ratedRequestsCreated = null
    this.$store.state.ratedRequestsDoneBy = null

    //Ako ne bude previše komplikovano, zameniti da se ove stvari ne postavljaju na null, nego da se proveri
    //na kog korisnika se odnose request-ovi koji već postoje u store-u, pa na osnovu toga da se šalje fetch ili ne.
    //(Zahteva da se manipuliše store-om isto kao u slučaju svih request-ova, da se menjaju podaci, splice-uju i slično.
    //Obratiti pažnju da treba da bude pogodno za rad sa notifikacijama.)

    window.scrollTo(0, 0)
    const filtersCreated = {
      created_by : this.user.id,
      done_by : this.$store.state.authUser.id,
      created_or_done_by: null,
      statuses : [2, 3],
      unrated_created_by : true,
      unrated_done_by : null
    }
    this.$store.dispatch("fillRequests", {filters: filtersCreated, objectToFill: {object: "unratedRequestsCreated", page: 1}})

    const filtersDone = {
      created_by : this.$store.state.authUser.id,
      done_by : this.user.id,
      created_or_done_by: null,
      statuses : [2, 3],
      unrated_created_by : null,
      unrated_done_by : true
    }
    this.$store.dispatch("fillRequests", {filters: filtersDone, objectToFill: {object: "unratedRequestsDoneBy", page: 1 }})
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

    .chk-rated-div {
      margin: 20px 5% 10px 5%;
    }
    
  }

  @media only screen and (max-width: 499px) {
    .side-info {
      top: 85px;
    }
  }
</style>