<template>
  <div>
    <div class = "top-div" v-if="isSerbian"> Profil </div>
    <div class = "top-div" v-else> Profile </div>
    <nav class="tabovi">
      <div class="container">
        <div class="navbar-menu">
          <a
            :class="['navbar-item', (tab=='Personal info')?'aktivan':'']" 
            @click="tabPersonalInfo"
            href="#"
          >
              <span v-if="isSerbian">Lični podaci</span>
              <span v-else>Personal info</span>
          </a>
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
    <PersonalInfo 
      v-if="tab == 'Personal info'" 
      :user="user"
    />
    <Achievements 
      v-if="tab == 'Achievements'"
      :achievements="achievements"
    />
  </div>
</template>

<script>
import PersonalInfo from '@/components/PersonalInfo'
import Achievements from '@/components/Achievements'

export default {
  components: {
    PersonalInfo,
    Achievements
  },
  data() {
    return {
      tab: "Personal info"
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
    achievements() {
      return this.$store.state.userAchievements
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
    console.log(this.achievements)
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
</style>