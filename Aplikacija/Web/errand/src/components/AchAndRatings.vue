<template>
  <div class="main-wrapper">
    <div class="side-info">
      <AsideProfileInfo :user="user" :forWideScreen="false" />
      <div class="btns">
        <router-link
          class="button is-primary " 
          style="width:100%;"
          :to="tab=='Ratings' ? '/achievements/'+user.id : '/ratings/'+user.id "
        >
          <strong v-if="isSerbian && tab=='Ratings'"> Dostignuća </strong>
          <strong v-else-if="tab=='Ratings'"> Achievements </strong>
          <strong v-else-if="isSerbian"> Ocene </strong>
          <strong v-else> Ratings </strong>
        </router-link>
        <router-link :to = "'/profile/' + user.id" class="button is-primary" style="width:100%;">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </router-link>
      </div>
    </div>   

    <div class = "content-wrapper">
      <div class="filter-wrapper" v-if="tab=='Ratings'"> 
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
              max="4"
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
              min="2"
              @blur="resetFilterHigher"
            />
          </div>
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
            v-if="!isFilterLowerInvalid && !isFilterHigherInvalid && filterValueHigher && filterValueLower && filterValueLower >= filterValueHigher"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Vrednost \'od\' mora biti manja od vrednosti \'do\'' : 'The \'to\' value must be lower than the \'from\' value'"
          ></span>
        </div>
      </div>
      <div v-if="tab=='Achievements'" class="ach-wrap"> 
        <Achievement 
          v-for="achievement in achievements"
          :key="achievement.id"
          :achievement="achievement"
        />
      </div>
      <div v-if="tab=='Ratings'" class="rating-wrap"> 
        <Rating 
          v-for="rating in ratings"
          :key="rating.id"
          :rating="rating"
        />
      </div>
    </div>
  </div>
</template>

<script>

import Achievement from "@/components/Achievement.vue"
import Rating from "@/components/Rating.vue"
import AsideProfileInfo from "@/components/AsideProfileInfo"
import {between} from "vuelidate/lib/validators"

export default {
  //izbaciti isMyProfile

  components: {
    Achievement,
    Rating,
    AsideProfileInfo
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
    isMyProfile: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      filterValueLower: 1,
      filterValueHigher: 5
    }
  },
  validations: {
    filterValueLower: {between: between(1, 4)},
    filterValueHigher: {between: between(2, 5)}
  },
  computed: {
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    achievements() {
      return this.$store.state.userAchievements
    },
    ratings() {
      const allRatings = this.$store.state.userRatings
      if(this.isFilterLowerInvalid || this.isFilterHigherInvalid || !this.filterValueLower || !this.filterValueHigher || this.filterValueHigher <= this.filterValueLower)
        return allRatings
      return Object.values(allRatings).filter(rat => rat.grade >= this.filterValueLower && rat.grade <= this.filterValueHigher)
    },
    isFilterLowerInvalid() {
      return this.$v.filterValueLower.$invalid
    },
    isFilterHigherInvalid() {
      return this.$v.filterValueHigher.$invalid
    }
  },
  methods: {
    changeTab() {
      if(this.tab === "Ratings")
        this.tab = "Achievements";
      else this.tab = "Ratings";
    },
    resetFilterLower() {
      if(this.filterValueLower > 4 || this.filterValueLower < 1 || !this.filterValueLower || this.filterValueLower >= this.filterValueHigher)
        this.filterValueLower = 1
    },
    resetFilterHigher() {
      if(this.filterValueHigher > 5 || this.filterValueHigher < 2 || !this.filterValueHigher || this.filterValueLower >= this.filterValueHigher)
        this.filterValueHigher = 5
    }
  },
  created() {
    //izbaciti
    this.$store.dispatch('getAllUsers')
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
    min-height:73vh;
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