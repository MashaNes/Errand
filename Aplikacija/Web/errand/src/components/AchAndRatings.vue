<template>
  <div>
    <!-- <div class="main-window">
      <div class="bordered-container">
        <div class="aside"> -->
      <div class="main-wrapper">
        <div class="side-info">
          <div class="media-center">
            <p class="image is-96x96">
              <img class="rounded-image" :src="user.picture">
            </p>
          </div>        
          <div class="info-plus-btn">
            <div class="info-only">
              <div class="info-element">
                <img 
                  src="@/assets/signature.svg" 
                  height = "20" 
                  width = "20"
                  style = "margin-right: 15px"
                />
                <span>{{fullUserName}}</span>
              </div>
              <div class="info-element">
                <img 
                  src="@/assets/email.svg" 
                  height = "20" 
                  width = "20"
                  style = "margin-right: 15px"
                />
                <span>{{user.email}}</span>
              </div>
              <div class="info-element">
                <img 
                  src="@/assets/call.svg" 
                  height = "20" 
                  width = "20"
                  style = "margin-right: 10%"
                />
                <div class="phone-number">
                  <div v-for="p in user.phone" :key="p">{{ p }}</div>
                </div>
              </div>
            </div>
            <div class="buttons;">
              <div>
                <b-button 
                  @click="changeTab()" 
                  style="" 
                  variant="success"
                  size="lg" 
                >
                  <span v-if="isSerbian && tab=='Ratings'"> Dostignuća </span>
                  <span v-else-if="tab=='Ratings'"> Achievements </span>
                  <span v-else-if="isSerbian"> Ocene </span>
                  <span v-else> Ratings </span>
                </b-button>
              </div>
              <router-link :to = "'/profile'" class="button is-primary" style="width:100%;">
                <strong v-if="isSerbian">Detalji profila</strong>
                <strong v-else>Profile details</strong>
              </router-link>
            </div>
          </div>
        </div>
          <!-- </div>
        </div>
      </div> -->
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

export default {
  components: {
    Achievement,
    Rating
  },
  props: {
    tab: {
      type: String,
      required: true
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
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
      return this.$store.state.userRatings
    }
  },
  methods: {
    changeTab() {
      if(this.tab === "Ratings")
        this.tab = "Achievements";
      else this.tab = "Ratings";
    }
  },
  created() {
    this.$store.dispatch('getUserAchievements')
    this.$store.dispatch('getUserRatings')
    this.$store.dispatch('getAllUsers')
  }

}
</script>

<style scoped>

 .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
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
    /* display: flex;
    flex-direction: column;
    align-items: center;
    position:sticky;
    margin: 30px 15% 20px 40%;
    top:100px; */
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
  }

  .bordered-container {
    margin-top:10px;
    border-right:1px solid lightgray;
    
  }

  .ach-wrap {
    width:100%;
    display:flex;
    flex-direction:row;
    flex-wrap:wrap;
    margin: 40px 5% 0 5%;
  }

  .rating-wrap {
    width:100%;
  }

  .btn {
    margin:10px 0 10px 0; 
    width:100%;
  }

  @media only screen and (max-width:500px)
  {
    .main-wrapper {
      flex-direction: column;
    }
    .side-info {
      margin:0 10px 0 10px;
      align-self:center;
      border-right: 1px solid black;
      border-left: 1px solid black;
      border-bottom: 1px solid black;
      top:85px;
      font-size: 10px;
      background-color:white;
      border-radius: 0 0 10px 10px;
      justify-items: baseline;
      word-break: break-all;
    }
    .rounded-image {
      height: 40px;
      width:40px;
      align-self: flex-start;
    }

    .media-center {
      margin: 5px 0 0 0;
      height:40px;
      width:40px;
    }

    .info-element {
      flex-direction: row;
    }

    .button {
      font-size: 15px;
      height: 30px;
    }

    .info-plus-btn {
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
  }
  
  @media only screen and (max-width:330px)
  {
    .ach-wrap {
      margin: 40px 2% 0 2%;
    }
  }
</style>