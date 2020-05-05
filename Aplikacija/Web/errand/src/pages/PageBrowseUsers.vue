<template>
  <div class="wrapper">
    <div class="filter-sticky">
      <div class="filter-parameters">
        <div class="filter-with-warning">
          <div class="filter-rating-wrap">
            <span v-text="isSerbian ? 'Minimalna prosečna ocena:' : 'Minimal average rating:'"></span>
            <input 
              v-model="filterRating" 
              :class="{'filter-rating':true, 'ne-valja':isFilterInvalid}" 
              type="number" 
              min="0" 
              max="4"
              @blur="resetRating"
            >
          </div>
          <span 
            v-if="isFilterInvalid"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Između 0 i 4' : 'Between 0 and 4'"
          ></span>
        </div>
        <div class="filter-sort-wrap">
          <span v-text="isSerbian ? 'Sortiraj po prosečnoj oceni' : 'Sort by average rating'"></span>
          <input type="checkbox" class="filter-sort" v-model="sortByRating">
        </div>
        <div class="filter-name-wrap">
          <span v-text="isSerbian ? 'Tražite po imenu:' : 'Search by name:'"></span>
          <input 
            v-model="filterName"
            class="filter-name" 
            type="text"
          >
        </div>
      </div>
      <div class="btn-div">
        <b-button class="button is-primary" @click="searchUsers">
          <img class="btn-img" src="@/assets/search.svg">
        </b-button>
        <div>
          
        </div>
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
      filterRating: 0,
      sortByRating: false
    }
  },
  validations: {
    filterRating: {between: between(0, 4)}
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
    isFilterInvalid() {
      return this.$v.filterRating.$invalid
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
          filterRating: this.filterRating
        })
        this.lastPage = this.currentPage
      }
    },
    resetRating() {
      if(this.filterRating > 4 || this.filterRating < 0 || !this.filterRating)
        this.filterRating = 0
    },
    searchUsers() {
      this.currentPage = 1
      this.lastPage = 1
      this.$store.dispatch('fillUsersPortion', {
        startingIndex: 0,
        numOfElements: this.perPage,
        filterName: this.filterName,
        filterRating: this.filterRating
      })
    }
  },
  created() {
    this.$store.dispatch('fillUsersPortion', {
      startingIndex: 0, 
      numOfElements: 3,
      filterName: this.filterName,
      filterRating: this.filterRating
    })
  }
}
</script>


<style scoped>
 
  .wrapper
  {
    display: flex;
    flex-direction: column;
    align-items: stretch;
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
  }

  .filter-sticky {
    position: sticky;
    top:70px;
    align-self:center;
    z-index: 1;
    padding: 1px;
    padding-top:0px;
    margin: 20px;
    margin-top: 0px;
  }

  .filter-parameters {
    display: flex;
    flex-direction: row;
    padding:10px;
    border-bottom-right-radius:5px;
    border: 1px solid black;
    border-top: hidden;
    background-color: white;
  }

  .filter-with-warning {
    margin: 5px;
    margin-right:20px;
  }

  .filter-rating-wrap {
    display:flex;
    font-weight: 600;
    align-items: center;

  }

  .filter-name-wrap {
    display:flex;
    margin: 5px;
    margin-right: 10px;
    font-weight: 600;
    align-items: center;
    padding: 0px;
  }


  .filter-sort-wrap {
    display:flex;
    font-weight: 600;
    align-items: center;
    margin: 5px;
    margin-right: 10px;
    margin-right: 20px;
  }

  .filter-rating {
    width: 40px;
    margin-left:10px;
    border: 1px solid grey;
    border-radius: 5px;
    padding-left: 2px;
  }

  .filter-name {
    max-width:200px;
    margin-left:10px;
    border: 1px solid grey;
    border-radius: 5px;
  }

  .filter-sort {
    margin-left: 10px;
  }

  .btn-div {
    border-radius: 0px;
  }

  .button {
    border-top-left-radius: 0px;
    border-top-right-radius: 0px;
    width: fit-content;
    height: fit-content;
  }

  .filter-invalid-span {
    color: red;
    font-size: 14px;
  }

  .ne-valja
  {
    background-color: rgb(255, 212, 212);
  }

  @media only screen and (max-width:599px)
  {
    .filter-sticky {
      align-self: flex-start;
    }

    .filter-parameters
    {
      padding: 5px;
      font-size: 12px;
      flex-direction: column;
    }

    .btn-img {
      height: 20px;
      width: 20px;
    }
  }

  @media only screen and (max-width: 499px)
  {
    .filter-sticky {
      top:85px;
      margin-left:10px;
      margin-right: 10px;
    }

    .filter-rating {
      width: 30px;
    }

    .filter-invalid-span {
      font-size:12px;
    }

  }

</style>