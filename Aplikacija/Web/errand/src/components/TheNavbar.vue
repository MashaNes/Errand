<template>
  <nav class="navbar is-spaced" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <router-link :to = "'/'" class="navbar-item">
        <h1 class="title is-4">Errand</h1>
      </router-link>
      <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div id="navbarBasicExample" class="navbar-menu">
      <div class="navbar-start">
        <div class="navbar-item has-dropdown is-hoverable">
            <img src = "../assets/menu.svg">
          <div class="navbar-dropdown">
            <a class="navbar-item">
                <img src = "../assets/notifications.svg">
                <div v-if="isSerbian" class = "ikonica"> Obaveštenja </div>
                <div v-else class = "ikonica"> Notifications </div>
            </a>
            <router-link :to = "'/'" class="navbar-item">
                <img src = "../assets/requests.svg">
                <div v-if="isSerbian" class = "ikonica"> Zahtevi </div>
                <div v-else class = "ikonica"> Requests </div>
            </router-link>
            <router-link :to = "'/profile'" class="navbar-item">
                <img src = "../assets/profile.svg">
                <div v-if="isSerbian" class = "ikonica"> Profil </div>
                <div v-else class = "ikonica"> Profile </div>
            </router-link>
            <a class="navbar-item">
                <img src = "../assets/settings.svg">
                <div v-if="isSerbian" class = "ikonica"> Podešavanja </div>
                <div v-else class = "ikonica"> Settings </div>
            </a>
          </div>
        </div>
      </div>

      <div class="navbar-end">
        <div class="navbar-item">
          <a class = "language" @click="prebaciNaEngleski"> EN </a>
          |
          <a class = "language" @click="prebaciNaSrpski"> SR </a>
        </div>
        <div class="navbar-item">
          <div class="buttons">
            <router-link :to = "'/register'" class="button is-primary">
              <strong v-if="isSerbian">Registruj se</strong>
              <strong v-else>Sign up</strong>
            </router-link>
            <router-link :to = "'/login'" class="button is-light">
              <span v-if="isSerbian"> Prijavi se </span>
              <span v-else> Log in </span>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
  export default {
    computed:
    {
      isSerbian(){
        return this.$store.state.isSerbian
      }
    },
    methods:
    {
      prebaciNaEngleski()
      {
        this.$store.state.isSerbian = false
      },
      prebaciNaSrpski()
      {
        this.$store.state.isSerbian = true
      },
    }
  }
</script>

<style scoped>
  .ikonica
  {
      margin-left: 10px
  }

  .language
  {
    margin:5px;
  }
</style>