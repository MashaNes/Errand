<template>
  <div class="wrapper">
    <div class="filter-sticky">
      <div class="filter-parameters">
        <div class="filter-with-warning">
          <div class="filter-rating-wrap">
            <span v-text="isSerbian ? 'Prosečna ocena od:' : 'Average rating from:'"></span>
            <input 
              v-model="filterRatingLowerInput" 
              :class="{'filter-rating':true, 'ne-valja':isFilterLowerInvalid}" 
              type="number" 
              :disabled="showUnratedChk"
              min="1" 
              max="5"
              @blur="resetRatingLower"
            >
            <div class="filter-rating-wrap">
              <span v-text="isSerbian ? 'do:' : 'to:'"></span>
              <input 
                v-model="filterRatingHigherInput" 
                :class="{'filter-rating':true, 'ne-valja':isFilterHigherInvalid}" 
                :disabled="showUnratedChk"
                type="number" 
                min="1" 
                max="5"
                @blur="resetRatingHigher"
              >
            </div>
          </div>
        </div>
        <div class="filter-sort-wrap">
          <span v-text="isSerbian ? 'Prikaži neocenjene korisnike' : 'Show users with no rating'"></span>
          <input type="checkbox" class="filter-sort" v-model="showUnratedChk">
        </div>
        <div class="filter-name-wrap">
          <input 
            v-model="filterNameInput"
            class="filter-name" 
            type="text"
            :placeholder="isSerbian ? 'Tražite po imenu...' : 'Search by name...'"
          >
        </div>
        <div class="btn-div">
          <b-button class="button is-primary" @click="searchUsers" :disabled="isFilterLowerInvalid || isFilterHigherInvalid || !filterRatingLowerInput || !filterRatingHigherInput || filterRatingLowerInput > filterRatingHigherInput">
            <img class="btn-img" src="@/assets/search.svg">
          </b-button>
        </div>
      </div>
      <div class="warnings">
        <span 
          v-if="isFilterLowerInvalid"
          class="filter-invalid-span" 
          v-text="isSerbian ? 'Vrednost \'od\' mora biti između 1 i 4' : 'The \'from\' value must be between 1 and 4'"
        ></span>
        <span 
          v-if="isFilterHigherInvalid"
          class="filter-invalid-span" 
          v-text="isSerbian ? 'Vrednost \'do\' mora biti između 2 i 5' : 'The \'to\' value must be between 2 and 5'"
        ></span>
        <span 
          v-if="!isFilterLowerInvalid && !isFilterHigherInvalid && filterRatingHigherInput && filterRatingLowerInput && filterRatingLowerInput > filterRatingHigherInput"
          class="filter-invalid-span" 
          v-text="isSerbian ? 'Vrednost \'od\' ne sme biti veća od vrednosti \'do\'' : 'The \'from\' value must not be higher than the \'to\' value'"
        ></span>
      </div>
    </div>

    <b-pagination 
      v-model="currentPage" 
      :total-rows="usersPortion.count" 
      :per-page="perPage" 
      align="center"
      class="pag-top"
      @input="getAnotherPortion"
    ></b-pagination>
    <div class="activeUsers" v-if="services != null && isBroadCast"> 
      <button type="button" class="btn btn-danger" @click="showModal = true">
        <span v-if="isSerbian"> Odustani od odabira </span>
        <span v-else> Abort choosing process </span>
      </button>
    </div>
    <div class="activeUsers" v-if="services != null"> 
      <input type="checkbox" v-model="active" class="cekiranje" @input="PromenaCekiranosi"/>
      <span v-if="isSerbian"> Prikaži samo aktivne korisnike</span>
      <span v-else> Show only active users</span>
    </div>
    <Spinner v-if="!this.$store.state.isDataLoaded"/>
    <div class="users" v-else-if="usersPortion.results.length > 0">
      <UserBox
        :user="user"
        v-for="user in usersPortion.results" 
        :key="user.id"
        :BenefitList="$route.params.benefitList == 'benefit'"
        :RequestSelect="services != null"
      />
    </div>
    <span class="no-results" v-else>
      <i v-text="isSerbian ? 'Nema korisnika koji odgovaraju unetim parametrima za pretragu.' : 'No users fit the specified search parameters.'"></i>
    </span>
    <b-pagination 
      v-model="currentPage"
      :total-rows="usersPortion.count" 
      :per-page="perPage"
      align="center"
      class="pag-bottom"
    ></b-pagination>
    <ModalBenefitAdded v-if="benefitAdded" @close="closeModal"/>
    <ModalAreYouSure v-if="showModal==true"
                     :naslovS="'Odustajanje od odabira'"
                     :naslovE="'Abort choosing process'"
                     :tekstS="'Da li ste sigurni da ne želite da odaberete korisnika? Nećete moći ponovo da se vratite na ovu mogućnost.'"
                     :tekstE="'Are you sure you don\'t want to select a user? You will not be able to come back to this possibility.'"
                     @yes="yes"
                     @close="showModal = false"/>
  </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
import ModalBenefitAdded from "@/components/ModalBenefitAdded"
import Spinner from "@/components/Spinner"
import UserBox from "@/components/UserBox"
import {between} from "vuelidate/lib/validators"

export default {
  props:
  {
    // benefitList:
    // {
    //   type: Boolean,
    //   required: false,
    //   default: false
    // }
    servicesList:
    {
      type:Array,
      required: false
    },
    locationsList:
    {
      type: Array,
      required: false
    }
  },
  components: {
    UserBox,
    Spinner,
    ModalBenefitAdded,
    ModalAreYouSure
  },
  data() {
    return {
      currentPage: 1,
      perPage: 10,
      lastPage: 1,
      filterNameInput: "",
      filterName: "",
      filterRatingLowerInput: 1,
      filterRatingHigherInput: 5,
      filterRatingLower: 1,
      filterRatingHigher: 5,
      showUnratedChk: true,
      showUnrated: true,
      active: false,
      showModal : false
    }
  },
  validations: {
    filterRatingLowerInput: {between: between(1, 5)},
    filterRatingHigherInput: {between: between(1, 5)}
  },
  computed: {
    usersPortion() {
      const ret = this.$store.state.usersPortion
      return ret
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isFilterLowerInvalid() {
      return this.$v.filterRatingLowerInput.$invalid
    },
    isFilterHigherInvalid() {
      return this.$v.filterRatingHigherInput.$invalid
    },
    benefitAdded(){
      return this.$store.state.userAdded
    },
    services()
    {
      if(this.servicesList == undefined)
        return null
      else
        return this.servicesList
    },
    locations()
    {
      if(this.locationsList == undefined)
        return null
      else
        return this.locationsList
    },
    isBroadCast()
    {
      return this.$store.state.requestInCreation.broadcast
    }
  },
  methods: {
    yes()
    {
      this.showModal = false
      this.$router.push({ name: 'PageNewRequest', params: {requestProp: this.$store.state.requestInCreation, stepProp:7}})
    },
    getAnotherPortion() {
      if(this.lastPage != this.currentPage)
      {
        window.scrollTo(0, 0)
        this.$store.dispatch('fillUsersPortion', {
          endpoint: "http://" + this.$store.state.host + ":8000/api/v1/filtered_users/?paginate=true&page=" + this.currentPage,
          sort_rating: true,
          sort_rating_asc: true,
          rating_limit_up: this.showUnrated ? null : parseInt(String(this.filterRatingHigher)),
          rating_limit_down: this.showUnrated ? null : parseInt(String(this.filterRatingLower)),
          services: this.services,
          locations: this.locations,
          no_rating: this.showUnrated,
          name: this.filterName,
          not_in_benefit: this.$route.params.benefitList == "benefit",
          active: false
        })
        this.lastPage = this.currentPage
      }
    },
    resetRatingLower() {
      if(this.filterRatingLowerInput > 5 || this.filterRatingLowerInput < 1 || !this.filterRatingLowerInput || this.filterRatingHigherInput < this.filterRatingLowerInput)
        this.filterRatingLowerInput = 1
    },
    resetRatingHigher() {
      if(this.filterRatingHigherInput > 5 || this.filterRatingHigherInput < 1 || !this.filterRatingHigherInput || this.filterRatingHigherInput < this.filterRatingLowerInput)
        this.filterRatingHigherInput = 5
    },
    searchUsers() {
      this.showUnrated = this.showUnratedChk
      this.filterRatingHigher = this.filterRatingHigherInput
      this.filterRatingLower = this.filterRatingLowerInput
      this.filterName = this.filterNameInput
      this.currentPage = 1
      this.lastPage = 1
      this.$store.dispatch('fillUsersPortion', {
        endpoint: "http://" + this.$store.state.host + ":8000/api/v1/filtered_users/?paginate=true",
        sort_rating: true,
        sort_rating_asc: true,
        rating_limit_up: this.showUnrated ? null : parseInt(String(this.filterRatingHigher)),
        rating_limit_down: this.showUnrated ? null : parseInt(String(this.filterRatingLower)),
        services: this.services,
        locations: this.locations,
        no_rating: this.showUnrated,
        name: this.filterName,
        not_in_benefit: this.$route.params.benefitList == "benefit",
        active: false
      })
    },
    closeModal(){
      this.$store.state.userAdded = false
      this.$store.dispatch('fillUsersPortion', {
        endpoint: "http://" + this.$store.state.host + ":8000/api/v1/filtered_users/?paginate=true",
        sort_rating: true,
        sort_rating_asc: true,
        rating_limit_up: null,
        rating_limit_down: null,
        services: this.services,
        locations: this.locations,
        no_rating: true,
        name: "",
        not_in_benefit: this.$route.params.benefitList == "benefit",
        active: false
      })
    },
    onCreate() {
        this.$store.dispatch('fillUsersPortion', {
        endpoint: "http://" + this.$store.state.host + ":8000/api/v1/filtered_users/?paginate=true",
        sort_rating: true,
        sort_rating_asc: true,
        rating_limit_up: null,
        rating_limit_down: null,
        services: this.services,
        locations: this.locations,
        no_rating: true,
        name: "",
        not_in_benefit: this.$route.params.benefitList == "benefit",
        active: false
      })
    },
    PromenaCekiranosi()
    {
      this.$store.dispatch('fillUsersPortion', {
        endpoint: "http://" + this.$store.state.host + ":8000/api/v1/filtered_users/?paginate=true",
        sort_rating: true,
        sort_rating_asc: true,
        rating_limit_up: null,
        rating_limit_down: null,
        services: this.services,
        locations: this.locations,
        no_rating: true,
        name: "",
        not_in_benefit: this.$route.params.benefitList == "benefit",
        active: !this.active
      })
    }
  },
  created() {
    // this.$store.dispatch('fillUsersPortion', {
    //   endpoint: "http://" + this.$store.state.host + ":8000/api/v1/filtered_users/?paginate=true",
    //   sort_rating: true,
    //   sort_rating_asc: true,
    //   rating_limit_up: null,
    //   rating_limit_down: null,
    //   services: this.services,
    //   locations: this.locations,
    //   no_rating: true,
    //   name: "",
    //   not_in_benefit: this.benefitList,
    //   active: false
    // })
    this.onCreate()
    //ako je benefitList true ide zahtev sa filterom da se prikazu samo korisnici koji nisu beneficirani
    //ako je false ide get zahtev bez filtera
  },
  watch: {
    $route() {
      this.onCreate()
    }
  }
}
</script>


<style scoped>
 
  .wrapper
  {
    display: flex;
    flex-direction: column;
    align-items: stretch;
    padding-top: 20px;
    padding-left:10px;
    padding-right:10px;
  }
  
  .body {
    background-color: #f8f9fa!important;
  }

  .users {
    display: flex; 
    flex-direction:column; 
    align-items:stretch;
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

  .filter-sticky {
    display:flex;
    flex-direction: column;
    border-radius: 5px;
    border: 1px solid black;
    background-color: white;
    margin: 0 8% 0 8%;
  }

  .filter-parameters {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items:center;
    padding:10px;
    font-size: 14px;
    border-radius: 5px;
    justify-content: space-around;
  }

  .filter-with-warning {
    margin-right:3%;
    margin-top:5px;
    display: flex;
    align-items: center;
  }

  .filter-rating-wrap {
    display:flex;
    font-weight: 600;
    align-items: center;

  }

  .filter-name-wrap {
    display:flex;
    font-weight: 600;
    align-items: center;
    margin-right: 3%;
    margin-top: 5px;
  }


  .filter-sort-wrap {
    display:flex;
    font-weight: 600;
    align-items: center;
    margin-right: 3%;
    margin-top:5px;
  }

  .filter-rating {
    width: 40px;
    margin-left:10px;
    margin-right:10px;
    border: 1px solid grey;
    border-radius: 5px;
    padding-left: 2px;
  }

  .filter-name {
    max-width:200px;
    border: 1px solid grey;
    border-radius: 5px;
    padding-left: 2px;
    width:100%;
  }

  .filter-sort {
    margin-left: 10px;
  }

  .btn-div {
    margin-left:30px;
    margin-right:10px;
  }

  .button {
    height:30px;
    width:30px;
    padding:0px;
    margin-top: 5px;
  }

  .btn-img {
      height: 20px;
      width: 20px;
    }

  .filter-invalid-span {
    color: red;
    font-size: 12px;
    padding:5px;
  }

  .ne-valja
  {
    background-color: rgb(255, 212, 212);
  }

  .activeUsers
  {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    margin:5px;
  }

  .cekiranje
  {
    margin-left: 15px;
    margin-right: 15px;
    height:20px;
    width:20px;
  }

  .no-results {
    font-weight: bold;
    font-size: 18px;
    word-break: break-word;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  @media only screen and (max-width:650px)
  {
    .filter-sticky {
      align-self: center;
    }

    .filter-parameters
    {
      padding: 10px;
      flex-direction: column;
      align-items: flex-start;
      align-self:stretch;
      align-items: stretch;
    }

    .btn-img {
      height: 20px;
      width: 20px;
    }

    .btn-div {
      margin-left: 5px;
      align-self:center;
    }

    .button {
      width:100px !important;
    }

    .filter-name-wrap {
      width:100%;
    }

    .filter-name {
      max-width: unset;
      width:100%;
    }
  }

  @media only screen and (max-width: 499px)
  {

    .filter-rating {
      width: 30px;
    }

    .filter-invalid-span {
      font-size:12px;
    }

  }

</style>