<template>
  <div class="main-wrapper">
    <div class="side-info">
      <AsideProfileInfo :user="user" />
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
          <div class="filter-input-and-span">
            <span 
              class="filter-span" 
              v-text="isSerbian ? 'Prikaži ocene veće od:' : 'Show grades greater than:'"
            ></span> 
            <input 
              type="number" 
              :class="{'filter-input':true, 'ne-valja':isFilterInvalid}" 
              v-model="filterValue"
              max="4"
              min="0"
              @blur="resetFilter"
            />
          </div>
          <span 
            v-if="isFilterInvalid"
            class="filter-invalid-span" 
            v-text="isSerbian ? 'Između 0 i 4' : 'Between 0 and 4'"
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
      filterValue: 0
    }
  },
  validations: {
    filterValue: {between: between(0, 4)}
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
      if(this.filterValue < 1 || this.filterValue > 4)
        return allRatings
      return Object.values(allRatings).filter(rat => rat.grade > this.filterValue)
    },
    isFilterInvalid() {
      return this.$v.filterValue.$invalid
    }
  },
  methods: {
    changeTab() {
      if(this.tab === "Ratings")
        this.tab = "Achievements";
      else this.tab = "Ratings";
    },
    resetFilter() {
      if(this.filterValue > 4 || this.filterValue < 0 || !this.filterValue)
        this.filterValue = 0
    }
  },
  created() {
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
    display: flex; 
    align-items: center;
  }

  .filter-span {
    flex-grow: 1; 
    margin-right: 10px; 
    font-weight: 600; 
    color: #4a4a4a;
  }

  .filter-input {
    flex-grow: 1;
    width: 40px;
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