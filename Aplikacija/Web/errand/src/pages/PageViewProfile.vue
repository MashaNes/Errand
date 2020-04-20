<template>
  <div>
    <div class = "top-div" v-if="isSerbian"> Profil </div>
    <div class = "top-div" v-else> Profile </div>
    <PersonalInfo :user="user" />
    <nav class="tabovi">
      <div class="container">
        <div class="navbar-menu">
          <a
            :class="['navbar-item', (tab=='Achievements')?'aktivan':'']" 
            @click="tabAchievements"
            href="#"
          >
              <span v-if="isSerbian">Dostignuća</span>
              <span v-else>Achievements</span>
          </a>
          <a
            :class="['navbar-item', (tab=='Ratings')?'aktivan':'']" 
            @click="tabRatings"
            href="#"
          >
              <span v-if="isSerbian">Ocene</span>
              <span v-else>Ratings</span>
          </a>
        </div>
      </div>
    </nav>
    <div 
      class="basic-profile-structure"
      v-if="tab == 'Achievements'"
    >
      <Achievement 
        v-for="achievement in achievements"
        :achievement="achievement"
        :key="achievement.id"
      />
    </div>
    <div 
      class="basic-profile-structure"
      v-if="tab == 'Ratings'"
    >
      <Ratings
        v-for="rating in ratings"
        :rating="rating"
        :key="rating.id"
      />
    </div>
  </div>
</template>

<script>
import PersonalInfo from '@/components/PersonalInfo'
import Achievement from '@/components/Achievement'
import Ratings from '@/components/Rating'

export default {
  components: {
    PersonalInfo,
    Achievement,
    Ratings
  },
  data() {
    return {
      tab: "Achievements"
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
    achievements() {
      return this.$store.state.userAchievements
    },
    ratings() {
      return this.$store.state.userRatings
    },
    isSerbian() {
      return this.$store.state.isSerbian
    }
  },
  methods: {
    tabPersonalInfo() {
      this.tab = 'Personal info'
    },
    tabAchievements() {
      this.tab = 'Achievements'
    },
    tabRatings() {
      this.tab = 'Ratings'
    }
  },
  created() {
    this.$store.dispatch('getUserAchievements')
    this.$store.dispatch('getUserRatings')
  }
}
</script>

<style scoped>

  .media {
    height: 200px;
    display:flex;
  }

  .is-128x128 {
    width: 300px;
    flex-grow: 1;
    flex-shrink: 1;
  }

  .image {
    margin-left: 25px;
  }

  .rounded-image {
    border-radius: 20px;
  }

 .tabovi
  {
      background-color: white;
      padding: 7px;
  }
  .navbar-menu
  {
      display:flex;
  }

  .navbar-item
  {
      flex-grow: 1;
      flex-shrink: 1;
      justify-content: center;
      font-weight: 400;
      font-size: 20px;
  }

  .aktivan
  {
      background-color: rgb(233, 233, 233);
      text-decoration: underline;
  }

  .basic-profile-structure {
    margin: 30px;
    margin-left: 100px;
    margin-right:100px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }
</style>