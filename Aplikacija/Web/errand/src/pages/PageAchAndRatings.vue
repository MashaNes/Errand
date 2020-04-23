<template>
  <div>
    <div class = "top-div" v-if="isSerbian && tab=='Ratings'"> Ocene </div>
    <div class = "top-div" v-else-if="tab=='Ratings'"> Ratings </div>
    <div class = "top-div" v-else-if="isSerbian"> Dostignuća </div>
    <div class = "top-div" v-else> Achievements </div>
    <div class="main-window">
      <div class="bordered-container">
        <div class="aside">
          <div class="media-center">
              <p class="image is-96x96">
                <img class="rounded-image" :src="user.picture">
              </p>
            </div>        
          <div>
            <div class="info-element">
              <img 
                src="@/assets/signature.svg" 
                height = "20" 
                width = "20"
                style = "margin-right: 15px"
              />
              {{fullUserName}}
            </div>
            <div class="info-element">
              <img 
                src="@/assets/email.svg" 
                height = "20" 
                width = "20"
                style = "margin-right: 15px"
              />
              {{user.email}}
            </div>
            <div class="info-element">
              <img 
                src="@/assets/call.svg" 
                height = "20" 
                width = "20"
                style = "margin-right: 15px"
              />
              <div class="phone-number">
                <div v-for="p in user.phone" :key="p">{{ p }}</div>
              </div>
            </div>
            <div>
              <b-button 
                @click="changeTab()" 
                style="margin:10px 0 10px 0; width:100%;" 
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
      <div v-if="tab=='Achievements'" class="ach-and-rating-wrap"> 
        <Achievement 
          v-for="achievement in achievements"
          :key="achievement.id"
          :achievement="achievement"
        />
      </div>
      <div v-if="tab=='Ratings'" class="ach-and-rating-wrap"> 
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
  data() {
    return {
      tab: "Ratings"
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
    flex-direction:row;
    margin-top: 8px;
    border-bottom: 1px solid lightgray;
    flex-wrap: wrap;
  }

  .phone-number {
    display: flex;
    flex-direction: column;
  }

  .aside {
    display: flex;
    flex-direction: column;
    align-items: center;
    position:sticky;
    margin: 30px 20px 20px 30px;
    left:40px;
    top:100px;
  }

  .flex-profile-info {
    margin-left:30px;
    display: flex;
    flex-direction:column;
    align-items: center;
  }

  .main-window {
    display: flex;
  }

  .bordered-container {
    margin-top:10px;
    border-right:1px solid lightgray;
    
  }

  .ach-and-rating-wrap {
    width: 100%; 
    margin-top:20px;
  }
</style>