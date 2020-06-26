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
      <nav class="tabovi">
        <a v-if="tab == 'Requested'" class="navbar-item aktivan" href="#">
          <span v-if="isSerbian">Zahtevali ste</span>
          <span v-else>Requested by you</span>
        </a>
        <a v-else class="navbar-item" href="#" @click="tabRequested">
          <span v-if="isSerbian">Zahtevali ste</span>
          <span v-else>Requested by you</span>
        </a>
        <a v-if="tab == 'Runner'" class="navbar-item aktivan" href="#">
          <span v-if="isSerbian">Izvršavali ste</span>
          <span v-else>Done by you</span>
        </a>
        <a v-else class="navbar-item" href="#" @click="tabRunner">
          <span v-if="isSerbian">Izvršavali ste</span>
          <span v-else>Done by you</span>
        </a>
      </nav>
      <div class="request-side-top-wrap">
        <div class="request-side-top">
          <div class="report-top">
            <b-button class="report-top-btn" variant="danger" @click="showModalReport = true">
              <div>
                <img src="../assets/report.png" class="btn-image">
                <span v-text="isSerbian ? 'Prijavi' : 'Report'"></span>
              </div>
            </b-button>
            <img src="../assets/info.svg" v-b-popover.hover.bottom="popoverMessage" />
          </div>
          <div class="chk-box-div">
            <input type="checkbox" v-model="showAll" class="chk-box" />
            <span v-text="isSerbian ? 'Prikaži i ocenjene zahteve' : 'Show rated requests too'"></span>
          </div>
        </div>
      </div>
      <Spinner v-if="!requests" />
      <!-- <b-pagination 
        v-if="requests" v-model="currentPage" :total-rows="requests == null ? 1 : requests.count" 
        :per-page="10" align="center" class="pag-top"  @input="getAnotherPortion"
      >
      </b-pagination> -->
      <div class="req-wrap" v-if="requests && requests.results && requests.results.length > 0">
        <RateOrReportBox v-for="request in requests.results" :key="request.id" :request="request" :user="user" @setMessages="setMessages" />
      </div>
      <span class="no-results" v-else>
        <i v-text="noResultsText"></i>
      </span>
      <!-- <b-pagination 
        v-if="requests" v-model="currentPage" 
        :total-rows="requests == null ? 1 : requests.count" 
        :per-page="10" align="center" class="pag-bottom" 
      >
      </b-pagination> -->
    </div>
    <ModalSuccess v-if="success && messagesSet" :textS="textMessageS" :textE="textMessageE" @close="closeModalSuccess"/>
    <ModalReportUser 
      v-if="showModalReport" @close="showModalReport = false" 
      :userToReport="user" @setMessages="setMessagesReport"
    />
  </div>
</template>

<script>

import AsideProfileInfo from "@/components/AsideProfileInfo"
import Spinner from "@/components/Spinner"
import RateOrReportBox from "@/components/RateOrReportBox"
import ModalSuccess from "@/components/ModalSuccess"
import ModalReportUser from "@/components/ModalReportUser"

export default {
  
  components: {
    AsideProfileInfo,
    Spinner,
    RateOrReportBox,
    ModalSuccess,
    ModalReportUser
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
      showModalReport: false,
      rating: {},
      showAll: false,
      fetchedAll: false,
      textMessageS: "",
      textMessageE: "",
      messagesSet: false,
      tab: "Requested",
      currentPage: 1,
      pageRatedDoneBy: 0,
      pageRatedCreated: 0,
      pageUnratedDoneBy: 0,
      pageUnratedCreated: 0
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    popoverMessage() {
      if(this.isSerbian) {
        return "Prijavite korisnika nevezano za konkretan zahtev"
      }
      else return "Report user without specifying a request"
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    requests() {
      if(this.tab == 'Requested') {
        if(this.showAll) {
          return this.$store.state.ratedRequestsDoneBy
        }
        else
          return this.$store.state.unratedRequestsDoneBy
      }
      else {
        if(this.showAll) {
          return this.$store.state.ratedRequestsCreated
        }
        else
          return this.$store.state.unratedRequestsCreated
      }
    },
    noResultsText() {
      if(this.isSerbian) 
        return 'Nema rezultata pretrage. (Pokušajte da promenite vrednost polja "Prikaži i neocenjene korisnike", ili da promenite tab).'
      else
        return 'The search gave no results. (Try changing the value of the "Show rated requests too" checkbox, or chaning the current tab).'
    },
    success() {
      return this.$store.state.success
    },
  },
  watch: {
    // eslint-disable-next-line no-unused-vars
    showAll(newVal, oldVal) {
      if(newVal) {
        if(this.tab == 'Requested' && this.pageRatedDoneBy != 1) {
          const filtersDone = {
            created_by : this.$store.state.authUser.id,
            done_by : this.user.id,
            created_or_done_by: null,
            statuses : [2, 3],
            unrated_created_by : null,
            unrated_done_by : false
          }
          this.$store.dispatch("fillRequests", {filters: filtersDone, objectToFill: {object: "ratedRequestsDoneBy", page: 1 }})
          this.pageRatedDoneBy = 1
          this.currentPage = 1
        }
        else if(this.pageRatedCreated !=1) {
          const filtersCreated = {
            created_by : this.user.id,
            done_by : this.$store.state.authUser.id,
            created_or_done_by: null,
            statuses : [2, 3],
            unrated_created_by : false,
            unrated_done_by : null
          }
          this.$store.dispatch("fillRequests", {filters: filtersCreated, objectToFill: {object: "ratedRequestsCreated", page: 1}})
          this.pageRatedCreated = 1
          this.currentPage = 1
        }
        this.fetchedAll = true
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
    },
    setMessagesReport() {
      this.setMessages({
        textMessageS: "Uspešno prijavljen problem sa korisnikom.",
        textMessageE: "User successfully reported."
      })
    },
    tabRequested() {
      this.showAll = false
      if(this.pageUnratedDoneBy != 1) { 
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
      this.tab = 'Requested'
      this.pageUnratedDoneBy = 1
      this.currentPage = 1
    },
    tabRunner() {
      this.showAll = false
      if(this.pageUnratedCreated != 1) {
        const filtersCreated = {
          created_by : this.user.id,
          done_by : this.$store.state.authUser.id,
          created_or_done_by: null,
          statuses : [2, 3],
          unrated_created_by : true,
          unrated_done_by : null
        }
        this.$store.dispatch("fillRequests", {filters: filtersCreated, objectToFill: {object: "unratedRequestsCreated", page: 1}})
      }
      this.tab = 'Runner'
      this.pageUnratedCreated = 1
      this.currentPage = 1
    },
    getAnotherPortion() {
      let unrated_created_by;
      let unrated_done_by;
      let toFill;
      if(this.tab == 'Runner') {
        unrated_created_by = this.showAll ? false : true
        unrated_done_by = null
        toFill = this.showAll ? "ratedRequestsCreated" : "unratedRequestsCreated"
        if(this.showAll)
          this.pageRatedCreated = this.currentPage
        else
          this.pageUnratedCreated = this.currentPage
      }
      else {
        unrated_created_by = null
        unrated_done_by = this.showAll ? false : true
        toFill = this.showAll ? "ratedRequestsDoneBy" : "unratedRequestsDoneBy"
        if(this.showAll)
          this.pageUnratedDoneBy = this.currentPage
        else
          this.pageUnratedDoneBy = this.currentPage
      }

      const filters = {
        created_by: this.tab == 'Runner' ? this.user.id : this.$store.state.authUser.id,
        done_by: this.tab == 'Runner' ? this.$store.state.authUser.id : this.user,
        created_or_done_by: null,
        statuses : [2, 3],
        unrated_created_by: unrated_created_by,
        unrated_done_by: unrated_done_by
      }
      this.$store.dispatch("fillRequests", {filters: filters, objectToFill:{object: toFill, page: this.currentPage}})
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
    const filtersDone = {
      created_by : this.$store.state.authUser.id,
      done_by : this.user.id,
      created_or_done_by: null,
      statuses : [2, 3],
      unrated_created_by : null,
      unrated_done_by : true
    }
    this.$store.dispatch("fillRequests", {filters: filtersDone, objectToFill: {object: "unratedRequestsDoneBy", page: 1 }})
    this.pageUnratedDoneBy = 1
    this.currentPage = 1
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

  .tabovi {
    background-color: white;
    padding: 7px;
    min-height: 50px;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    margin-bottom: 30px;
  }

  .navbar-menu {
    display:flex;
  }

  .navbar-item {
    flex-grow: 1;
    flex-shrink: 1;
    justify-content: center;
    font-weight: 400;
    font-size: 20px;
    height: 100%;
    text-align: center;
  }

  .aktivan {
    background-color: rgb(233, 233, 233);
    text-decoration: underline;
  }

  .request-side-top-wrap {
    width: 100%;
    display: flex;
  }

  .request-side-top {
    margin: 30px 15% 10px 15%;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  .report-top {
    margin-bottom: 10px;
    display:flex;
    align-items: center;
  }

  .report-top-btn {
    margin-right: 5px;
  }

  .chk-box-div {
    border: 1px solid black;
    background-color: white;
    display: flex;
    align-items: center;
    border-radius: 10px;
    padding: 10px;
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

  .btn-image {
    height: 18px;
    width: 18px;
    margin-right: 5px;
    margin-bottom: 3px;
  }

  .pag-top {
    margin-bottom: 40px;
    margin-top: 20px;
    z-index:0;
  }

  .pag-bottom {
    margin: 40px 0 0px 0;
    z-index:0;
    padding-bottom: 40px;
  }

  @media only screen and (max-width:900px) {
    .request-side-top {
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

    .request-side-top {
      margin: 20px 5% 10px 5%;
    }
    
  }

  @media only screen and (max-width: 499px) {
    .side-info {
      top: 85px;
    }
  }

  .no-results {
    font-weight: bold;
    font-size: 18px;
    word-break: break-word;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    margin-top: 20px;
  }
</style>