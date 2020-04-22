<template>
  <div>
    <div class = "top-div" v-if="isSerbian && tab=='Ratings'"> Ocene </div>
    <div class = "top-div" v-else-if="tab=='Ratings'"> Ratings </div>
    <div class = "top-div" v-else-if="isSerbian"> Dostignuća </div>
    <div class = "top-div" v-else> Achievements </div>
    <aside class="aside">
      <div class="media-center">
        <p class="image is-96x96">
          <img class="rounded-image" :src="user.picture">
        </p>
      </div>
      <div style="margin-top:10px">
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
            style="margin:10px 0 10px 0" 
            variant="success"
            size="lg" 
          >
            <span v-if="isSerbian && tab=='Ratings'"> Dostignuća </span>
            <span v-else-if="tab=='Ratings'"> Achievements </span>
            <span v-else-if="isSerbian"> Ocene </span>
            <span v-else> Ratings </span>
          </b-button>
        </div>
        <router-link :to = "'/profile'" class="button is-primary">
          <strong v-if="isSerbian">Detalji profila</strong>
          <strong v-else>Profile details</strong>
        </router-link>
      </div>
    </aside>
  </div>
</template>

<script>
export default {
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
    }
  },
  methods: {
    changeTab() {
      if(this.tab === "Ratings")
        this.tab = "Achievements";
      else this.tab = "Ratings";
    }
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
    margin: 30px 0 0 30px;
    float: left;
  }
</style>