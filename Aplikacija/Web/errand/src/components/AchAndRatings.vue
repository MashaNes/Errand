<template>
  <div class="main-wrapper">
    <div class="side-info">
      <AsideProfileInfo :user="user" :forWideScreen="false" />
      <div class="btns">
        <router-link
          class="button is-primary " 
          style="width:100%;"
          :to="tab=='Ratings' ? goToAchievements() : goToRatings() "
        >
          <strong v-if="isSerbian && tab=='Ratings'"> Dostignuća </strong>
          <strong v-else-if="tab=='Ratings'"> Achievements </strong>
          <strong v-else-if="isSerbian"> Ocene </strong>
          <strong v-else> Ratings </strong>
        </router-link>
        <router-link :to = "goToProfile()" class="button is-primary" style="width:100%;">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </router-link>
      </div>
    </div>   

    <div class = "content-wrapper">
      <Spinner v-if="(!shouldFilter && !dataLoaded) || (shouldFilter && !$store.state.allRatings)" />
      <b-pagination 
        v-model="currentPage" :total-rows="tab == 'Achievements' ? achievements.count : ratingsCount()" 
        :per-page="perPage" align="center" class="pag-top"
        @input="getAnotherPortion" v-if="(!shouldFilter && dataLoaded) || (shouldFilter && $store.state.allRatings)"
      ></b-pagination>
      <!-- <div 
        class="filter-wrapper" 
        v-if="tab=='Ratings' && ((!shouldFilter && $store.state.isDataLoaded && ratings.results.length > 0) ||
        (shouldFilter && filteredRatings && filteredRatings.length > 0))"
      >  -->
      <div 
        class="filter-wrapper" 
        v-if="tab=='Ratings' && shouldShowFilter"
      > 
        <div class="filter-inner-wrap">
          <span class="filter-span" v-text="isSerbian ? 'Prikaži ocene u opsegu' : 'Show grades in range'"></span>
          <div class="filter-input-and-span">
            <span 
              class="filter-span" 
              v-text="isSerbian ? 'od:' : 'from:'"
            ></span> 
            <input 
              type="number" 
              :class="{'filter-input':true, 'ne-valja':isFilterLowerInvalid}" 
              v-model="filterValueLower"
              max="5"
              min="1"
              @blur="resetFilterLower"
            />
            <span 
              class="filter-span" 
              v-text="isSerbian ? 'do:' : 'to:'"
            ></span> 
            <input 
              type="number" 
              :class="{'filter-input':true, 'ne-valja':isFilterHigherInvalid}" 
              v-model="filterValueHigher"
              max="5"
              min="1"
              @blur="resetFilterHigher"
            />
            <b-button class="button is-primary search" @click="searchUsers" :disabled="isFilterLowerInvalid || isFilterHigherInvalid || !filterValueHigher || !filterValueLower || filterValueLower > filterValueHigher">
              <img class="btn-img" src="@/assets/search.svg">
            </b-button>
          </div>
          <span 
            v-if="isFilterLowerInvalid"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Vrednost \'od\' mora biti između 1 i 5' : 'The \'from\' value must be between 1 and 5'"
          ></span>
          <span 
            v-if="isFilterHigherInvalid"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Vrednost \'do\' mora biti između 1 i 5' : 'The \'to\' value must be between 1 and 5'"
          ></span>
          <span 
            v-if="!isFilterLowerInvalid && !isFilterHigherInvalid && filterValueHigher && filterValueLower && filterValueLower > filterValueHigher"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Vrednost \'od\' mora biti manja ili jednaka vrednosti \'do\'' : 'The \'from\' value must be lower or equal to the \'to\' value'"
          ></span>
        </div>
      </div>
      <div v-if="tab=='Achievements' && dataLoaded && achievements.results.length > 0" class="ach-wrap"> 
        <Achievement 
          v-for="achievement in achievements.results"
          :key="achievement.id"
          :achievement="achievement"
        />
      </div>
      <div 
        v-if="tab=='Ratings' && ((!shouldFilter && dataLoaded && ratings.results.length > 0) || 
        (shouldFilter && filteredRatings && filteredRatings.length > 0))" class="rating-wrap"> 
        <Rating 
          v-for="rating in ratingsCollection"
          :key="rating.id"
          :rating="rating"
        />
      </div>
      <span class="no-results" v-if="(tab == 'Ratings' && shouldFilter && filteredRatings && filteredRatings.length == 0)">
        <i v-text="isSerbian ? 'Nema ocena koje odgovaraju unetim granicama.' : 'There are no ratings that fit the entered limits.'"></i>
      </span>
      <span class="no-results" v-if="noResults && user.id != $store.state.authUser.id">
        <i v-if="tab == 'Achievements'" v-text="isSerbian ? 'Korisnik nema dostignuća za sada.' : 'This user has no achievements yet.'"></i>
        <i v-if="tab == 'Ratings'" v-text="isSerbian ? 'Korisnik nije bio ocenjivan do sada.' : 'This user has not been rated yet.'"></i>
      </span>
      <span class="no-results" v-if="noResults && user.id == $store.state.authUser.id">
        <i v-if="tab == 'Achievements'" v-text="isSerbian ? 'Za sada nemate dostignuća.' : 'You have no achievements yet.'"></i>
        <i v-if="tab == 'Ratings'" v-text="isSerbian ? 'Niste bili ocenjivani do sada.' : 'You have not been rated yet.'"></i>
      </span>
      <b-pagination 
        v-model="currentPage" :total-rows="tab == 'Achievements' ? achievements.count : ratingsCount()" 
        :per-page="perPage" align="center" class="pag-bottom" v-if="(!shouldFilter && dataLoaded) || (shouldFilter && $store.state.allRatings)"
      ></b-pagination>
    </div>
  </div>
</template>

<script>

import Achievement from "@/components/Achievement.vue"
import Rating from "@/components/Rating.vue"
import AsideProfileInfo from "@/components/AsideProfileInfo"
import {between} from "vuelidate/lib/validators"
import Spinner from "@/components/Spinner"

export default {

  components: {
    Achievement,
    Rating,
    AsideProfileInfo,
    Spinner
  },
  props: {
    tab: {
      type: String,
      required: true
    },
    user: {
      type: Object, 
      required: true
    },
    ratings: {
      type: Object,
      required: false
    },
    achievements: {
      type: Object, 
      required: false
    },
    RequestSelect:
    {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      currentPage: 1,
      perPage: 10,
      lastPage: 1,
      filterValueLower: 1,
      filterValueHigher: 5,
      previousFilterLow: 1,
      previousFilterHigh: 5,
      realFilterLow: 1,
      realFilterHigh: 5
    }
  },
  validations: {
    filterValueLower: {between: between(1, 5)},
    filterValueHigher: {between: between(1, 5)}
  },
  computed: {
    fullUserName() {
      return this.user.first_name + " " +this.user.last_name
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isFilterLowerInvalid() {
      return this.$v.filterValueLower.$invalid
    },
    isFilterHigherInvalid() {
      return this.$v.filterValueHigher.$invalid
    },
    noResults() {
      if(this.tab == 'Achievements')
        return this.achievements ? this.achievements.results.length == 0 : false
      else {
        if(!this.shouldFilter)
          return this.ratings ? this.ratings.results.length == 0 : false
        else
          return this.$store.state.allRatings ? false : true
      }
    },
    shouldShowFilter() {
      if(!this.shouldFilter)
        return this.ratings ? this.ratings.results.length > 0 : false
      else
        return this.$store.state.allRatings ? true : false
    },
    filteredRatings() {
      if(!this.$store.state.allRatings) 
        return null
      const allRatings = this.$store.state.allRatings.results
      
      let retValue = allRatings.filter(rtg => rtg.grade >= this.realFilterLow && rtg.grade <= this.realFilterHigh)
      retValue = retValue.slice((this.currentPage - 1) * 10, (this.currentPage -1 ) * 10 + 10)
      return retValue
    },
    shouldFilter() {
      if(this.realFilterLow != 1 || this.realFilterHigh != 5)
        return true
      return false
    },
    ratingsCollection() {
      if(this.shouldFilter) 
        return this.filteredRatings
      else 
        return this.ratings.results
    },
    
    dataLoaded() {
      if(this.tab == "Achievements") {
        if(this.achievements)
          return true
        else return false
      }
      else {
        if(this.ratings) 
          return true
        else return false
      }
    },
    newRating() {
      return this.$store.state.newRating
    },
    newAchievement() {
      return this.$store.state.newAchievement
    }
  },
  watch: {
    newRating() {
      if(this.tab == 'Ratings') {
        window.scrollTo(0, 0)
        //this.setNullToCollection(this.tab)
        this.$store.dispatch('fillUserRatings', {
          userId: this.user.id,
          endpoint: "http://localhost:8000/api/v1/user_info_filtered/?paginate=true",
          updateAllRatings: true
        })
        this.currentPage = 1
        this.lastPage = 1
        this.filterValueLower = 1,
        this.filterValueHigher = 5,
        this.previousFilterLow = 1,
        this.previousFilterHigh = 5,
        this.realFilterLow = 1,
        this.realFilterHigh = 5
      }
    },
    newAchievement() {
      if(this.tab == 'Achievements') {
        window.scrollTo(0, 0)
        //this.setNullToCollection(this.tab)
        this.$store.dispatch('getUserAchievements', {
          userId: this.user.id,
          endpoint: "http://localhost:8000/api/v1/user_info_filtered/?paginate=true"
        })
        this.currentPage = 1
        this.lastPage = 1
        this.filterValueLower = 1,
        this.filterValueHigher = 5,
        this.previousFilterLow = 1,
        this.previousFilterHigh = 5,
        this.realFilterLow = 1,
        this.realFilterHigh = 5
      }
    }
  },
  methods: {
    changeTab() {
      if(this.tab === "Ratings")
        this.tab = "Achievements";
      else {
       this.tab = "Ratings";
       this.currentPage = 1
      }
    },
    ratingsCount() {
      if(this.shouldFilter)
        if(this.$store.state.allRatings) {
          const allRtgs = this.$store.state.allRatings.results
          return allRtgs.filter(rtg => rtg.grade >= this.realFilterLow && rtg.grade <= this.realFilterHigh).length
        }
        else
          return 1
      else
        return this.ratings.count
    },
    resetFilterLower() {
      if(this.filterValueLower > 5 || this.filterValueLower < 1 || !this.filterValueLower || this.filterValueLower > this.filterValueHigher)
        this.filterValueLower = 1
    },
    resetFilterHigher() {
      if(this.filterValueHigher > 5 || this.filterValueHigher < 1 || !this.filterValueHigher || this.filterValueLower > this.filterValueHigher)
        this.filterValueHigher = 5
    },
    getAnotherPortion() {
      if(this.lastPage != this.currentPage) {
        window.scrollTo(0, 0)
        if(!this.shouldFilter) {
          let payload = null
          const storeFunction = this.tab == 'Achievements' ? 'getUserAchievements' : 'fillUserRatings'
          this.setNullToCollection(this.tab)
          if(this.tab == 'Achievements') {
            payload = {
              userId: this.user.id,
              endpoint: "http://localhost:8000/api/v1/user_info_filtered/?paginate=true&page=" + this.currentPage
            }
          }
          else 
            payload = {
              userId: this.user.id,
              endpoint: "http://localhost:8000/api/v1/user_info_filtered/?paginate=true&page=" + this.currentPage,
              updateAllRatings: false
            }
          this.$store.dispatch(storeFunction, payload)
        }
        this.lastPage = this.currentPage
      }
    },
    setNullToCollection(collection) {
      if(collection == 'Achievements') {
        if(this.user.id == this.$store.state.authUser.id)
          this.$store.state.authUserAchievements = null
        else this.$store.state.userAchievements = null
      }
      else
        if(this.user.id == this.$store.state.authUser.id)
          this.$store.state.authUserRatings = null
        else this.$store.state.userRatings = null
    },
    goToProfile()
    {
      return {
        name: "PageViewProfile", 
        params: {
          id: this.user.id,
          user: this.user,
          RequestSelect: this.RequestSelect
        }
      }
    },
    goToAchievements() {
      return {
        name: "PageAchievements",
        params: {
          id: this.user.id,
          user: this.user,
          RequestSelect: this.RequestSelect
        }
      }
    },
    goToRatings() {
      return {
        name: "PageRatings",
        params: {
          id: this.user.id,
          user: this.user,
          RequestSelect: this.RequestSelect
        }
      }
    },
    searchUsers() {
      this.currentPage = 1
      this.realFilterLow = parseInt(this.filterValueLower)
      this.realFilterHigh = parseInt(this.filterValueHigher)
      if(!this.shouldFilter) {
        this.lastPage = 0
        this.getAnotherPortion()
      }
    }
  },
  created() {
    if(this.tab == "Ratings") {
      this.$store.state.allRatings = null
      this.$store.dispatch('fillAllUserRatings', this.user.id)
    }
  }
}
</script>

<style scoped>

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:96px;
    width:96px;
    object-fit:cover;
  }

  .info-element {
    display:flex;
    flex-direction:column;
    margin-top: 8px;
    border-bottom: 1px solid lightgray;
  }

  .phone-number {
    display: flex;
    flex-direction: column;
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

  .pic-plus-info {
    display:flex;
    flex-direction:column;
    align-items: center;
  }

  .flex-profile-info {
    margin-left:30px;
    display: flex;
    flex-direction:column;
    align-items: center;
  }

  .main-wrapper {
    display:flex; 
    flex-direction:row;
    min-height:85vh;
  }

  .bordered-container {
    margin-top:10px;
    border-right:1px solid lightgray;
    
  }

  .ach-wrap {
    width:100%;
    display: flex;
    flex-direction: row;
    flex-wrap:wrap;
    margin-left: 2%;
    margin-right: 5px;
    height:fit-content;
  }

  .rating-wrap {
    width:100%;
  }

  .btns {
    display:flex;
    flex-direction:column;
    width:100%;
  }

  .is-secondary {
    width:100%;
  }

  .button {
    width:100%;
    margin-bottom:10px;
    margin-top:10px;
  }

  .search {
    height: 30px;
    width: 30px;
    padding: 0px;
  }

  .btn-img {
    height: 20px;
    width: 20px;
  }

  .content-wrapper {
    width:100%; 
    display:flex; 
    flex-direction:column; 
    align-items:center;
  }

  .filter-wrapper {
    width: 100%; 
    display: flex;
  }

  .filter-inner-wrap {
    margin: 30px 20% 10px 20%; 
    border: 1px solid black;
    display:flex;
    flex-direction: column;
    padding: 10px;
    border-radius: 5px;
    background-color: white
  }

  .filter-input-and-span {
    margin-top: 10px;
    display: flex; 
    align-items: center;
  }

  .filter-span {
    /* flex-grow: 1;  */
    margin-right: 5px; 
    font-weight: 600; 
    color: #4a4a4a;
  }

  .filter-input {
    /* flex-grow: 1; */
    width: 40px;
    margin-right: 20px;
    border: 1px solid grey;
    border-radius:5px;
  }

  .filter-invalid-span {
    color: red;
    font-size: 14px;
  }

  .ne-valja
  {
    background-color: rgb(255, 212, 212);
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

  .no-results {
    font-weight: bold;
    font-size: 18px;
    word-break: break-word;
  }

  @media only screen and (max-width:499px)
  {
    .filter-inner-wrap {
      margin: 30px 10px 10px 10px;
    }

    .to-hide {
      visibility: hidden;
      height: 0px;
      width: 0px;
      margin:0px;
    }

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

    .rounded-image {
      height: 60px;
      width:60px;
      align-self: flex-start;
    }

    .media-center {
      margin: 10px 10px 0 10px;
      height:60px;
      width:60px;
      align-self:baseline;
    }

    .info-element {
      flex-direction: row;
    }

    .button {
      font-size: 15px;
      height: 30px;
      width:100%;
      margin: 5px 5px 5px 5px;
    }

    .pic-plus-info {
      margin: 5px 5px 5px 0;
      display: flex;
      flex-direction: row;
    }

    .info-only {
      display:flex;
      flex-direction: column;
      margin: 0 5px 0 5px;
    }

    .btn {
      margin:5px 0 5px 0;
      font-size:15px;
    }

    .ach-wrap {
      align-items:center;
      flex-direction: column;
      margin:0px;
    }
  }
  
  @media only screen and (max-width:330px)
  {
    .ach-wrap {
      margin: 40px 2% 0 2%;
    }
  }
</style>