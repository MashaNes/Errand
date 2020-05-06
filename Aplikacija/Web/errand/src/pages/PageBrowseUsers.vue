<template>
  <div class="wrapper">
    <div class="filter-sticky">
      <div class="filter-parameters">
        <div class="filter-with-warning">
          <div class="filter-rating-wrap">
            <span v-text="isSerbian ? 'Prosečna ocena od:' : 'Average rating from:'"></span>
            <input 
              v-model="filterRatingLower" 
              :class="{'filter-rating':true, 'ne-valja':isFilterLowerInvalid}" 
              type="number" 
              min="1" 
              max="4"
              @blur="resetRatingLower"
            >
            <div class="filter-rating-wrap">
              <span v-text="isSerbian ? 'do:' : 'to:'"></span>
              <input 
                v-model="filterRatingHigher" 
                :class="{'filter-rating':true, 'ne-valja':isFilterHigherInvalid}" 
                type="number" 
                min="2" 
                max="5"
                @blur="resetRatingHigher"
              >
            </div>
          </div>
          <!-- <span 
            v-if="isFilterLowerInvalid"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Između 0 i 4' : 'Between 0 and 4'"
          ></span> -->
        </div>
        <!--<div class="filter-with-warning">
          
           <span 
            v-if="isFilterLowerInvalid"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Između 0 i 4' : 'Between 0 and 4'"
          ></span> 
        </div>-->
        <div class="filter-sort-wrap">
          <span v-text="isSerbian ? 'Sortiraj po prosečnoj oceni' : 'Sort by average rating'"></span>
          <input type="checkbox" class="filter-sort" v-model="sortByRating">
        </div>
        <div class="filter-name-wrap">
          <!-- <span v-text="isSerbian ? 'Tražite po imenu:' : 'Search by name:'"></span> -->
          <input 
            v-model="filterName"
            class="filter-name" 
            type="text"
            :placeholder="isSerbian ? 'Tražite po imenu...' : 'Search by name...'"
          >
        </div>
        <div class="btn-div">
          <b-button class="button is-primary" @click="searchUsers">
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
          v-if="!isFilterLowerInvalid && !isFilterHigherInvalid && filterRatingHigher && filterRatingLower && filterRatingLower >= filterRatingHigher"
          class="filter-invalid-span" 
          v-text="isSerbian ? 'Vrednost \'od\' mora biti manja od vrednosti \'do\'' : 'The \'to\' value must be lower than the \'from\' value'"
        ></span>
      </div>
    </div>

    <b-pagination 
      v-model="currentPage" 
      :total-rows="users.totalCount" 
      :per-page="perPage" 
      align="center"
      class="pag-top"
      @input="getNextPortion"
    ></b-pagination>
    <div class="users">
      <!-- <strong> {{user.firstName}} </strong> -->
      <UserBox
        :user="user"
        v-for="user in users.users" 
        :key="user.id"
        :BenefitList="benefitList"
      />
    </div>
    <b-pagination 
      v-model="currentPage" 
      :per-page="perPage" 
      :total-rows="users.totalCount" 
      align="center"
      class="pag-bottom"
    ></b-pagination>
  </div>
</template>

<script>
import UserBox from "@/components/UserBox"
import {between} from "vuelidate/lib/validators"

export default {
  props:
  {
    benefitList:
    {
      type: Boolean,
      required: false,
      default: false
    }
  },
  components: {
    UserBox
  },
  data() {
    return {
      currentPage: 1,
      perPage: 3,
      lastPage: 1,
      filterName: "",
      filterRatingLower: 1,
      filterRatingHigher: 5,
      sortByRating: false
    }
  },
  validations: {
    filterRatingLower: {between: between(1, 4)},
    filterRatingHigher: {between: between(2, 5)}
  },
  computed: {
    users() {
      const ret = JSON.parse(JSON.stringify(this.$store.state.usersPortion))

      if(this.sortByRating)
      {
        ret.users.sort((userA, userB) => userB.rating - userA.rating)
      }
      return ret
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isFilterLowerInvalid() {
      return this.$v.filterRatingLower.$invalid
    },
    isFilterHigherInvalid() {
      return this.$v.filterRatingHigher.$invalid
    }
  },
  methods: {
    getNextPortion() {
      if(this.lastPage != this.currentPage)
      {
        console.log(this.currentPage + " " + this.perPage)
        this.$store.dispatch('fillUsersPortion', {
          startingIndex: (this.currentPage - 1) * this.perPage,
          numOfElements: this.perPage,
          filterName: this.filterName,
          filterRatingLower: this.filterRatingLower,
          filterRatingHigher: this.filterRatingHigher
        })
        this.lastPage = this.currentPage
      }
    },
    resetRatingLower() {
      if(this.filterRatingLower > 4 || this.filterRatingLower < 1 || !this.filterRatingLower || this.filterRatingHigher <= this.filterRatingLower)
        this.filterRatingLower = 1
    },
    resetRatingHigher() {
      if(this.filterRatingHigher > 4 || this.filterRatingHigher < 1 || !this.filterRatingHigher || this.filterRatingHigher <= this.filterRatingLower)
        this.filterRatingHigher = 5
    },
    searchUsers() {
      this.currentPage = 1
      this.lastPage = 1
      this.$store.dispatch('fillUsersPortion', {
        startingIndex: 0,
        numOfElements: this.perPage,
        filterName: this.filterName,
        filterRatingLower: this.filterRatingLower,
        filterRatingHigher: this.filterRatingHigher
      })
    }
  },
  created() {
    this.$store.dispatch('fillUsersPortion', {
      startingIndex: 0, 
      numOfElements: 3,
      filterName: this.filterName,
      filterRatingLower: this.filterRatingLower,
      filterRatingHigher: this.filterRatingHigher
    })
    //ako je benefitList true ide zahtev sa filterom da se prikazu samo korisnici koji nisu beneficirani
    //ako je false ide get zahtev bez filtera
  }
}
</script>


<style scoped>
 
  .wrapper
  {
    display: flex;
    flex-direction: column;
    align-items: stretch;
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