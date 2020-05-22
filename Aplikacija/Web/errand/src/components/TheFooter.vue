<template>
  <footer class="futer">
    <div class="futer-top">
      <div class = "footer-main">
        <div class="divLista deo" v-if="isLogedIn">
          <ul class="lista" v-if="!isAdmin">
            <!--<router-link>-->
              <li class="stavka-navigacije" v-if="isSerbian"> Obaveštenja </li>
              <li class="stavka-navigacije" v-else> Notifications </li>
            <!--</router-link>-->
            <router-link :to="goToProfile()" >
              <li class="stavka-navigacije" v-if="isSerbian"> Profil </li>
              <li class="stavka-navigacije" v-else> Profile </li>
            </router-link>
            <router-link :to="goToAchievements()">
              <li class="stavka-navigacije" v-if="isSerbian">Dostignuća </li>
              <li class="stavka-navigacije" v-else> Achievements </li>
            </router-link>
            <router-link :to="goToRatings()">
              <li class="stavka-navigacije" v-if="isSerbian"> Ocene </li>
              <li class="stavka-navigacije" v-else> Ratings </li>
            </router-link>
             <router-link :to="'/newRequest'" >
              <li class="stavka-navigacije" v-if="isSerbian"> Novi zahtev </li>
              <li class="stavka-navigacije" v-else> New Request </li>
            </router-link>
            <router-link :to="'/requests'" >
              <li class="stavka-navigacije" v-if="isSerbian"> Zahtevi </li>
              <li class="stavka-navigacije" v-else> Requests </li>
            </router-link>
            <router-link :to="goToUsers()" >
              <li class="stavka-navigacije" v-if="isSerbian"> Pretraži korisnike </li>
              <li class="stavka-navigacije" v-else> Search for users </li>
            </router-link>
            <router-link :to="'/settings'">
              <li class="stavka-navigacije" v-if="isSerbian"> Podešavanja </li>
              <li class="stavka-navigacije" v-else> Settings </li>
            </router-link>
            <router-link :to="'/help'">
              <li class="stavka-navigacije" v-if="isSerbian"> Pomoć </li>
              <li class="stavka-navigacije" v-else> Help </li>
            </router-link>
            <li class="stavka-navigacije" v-if="isSerbian" @click="odjaviSe"> Odjavi se </li>
            <li class="stavka-navigacije" v-else @click="odjaviSe"> Log out </li>
          </ul>
          <ul class="lista" v-else>
            <router-link :to="'/statistics'">
              <li class="stavka-navigacije" v-if="isSerbian"> Pregled statistike </li>
              <li class="stavka-navigacije" v-else> View statistics </li>
            </router-link>
            <!--<router-link>-->
              <li class="stavka-navigacije" v-if="isSerbian"> Upravljanje prijavama </li>
              <li class="stavka-navigacije" v-else> Handle reports </li>
            <!--</router-link>-->
            <!--<router-link>-->
              <li class="stavka-navigacije" v-if="isSerbian"> Nekategorizovani zadaci </li>
              <li class="stavka-navigacije" v-else> Uncategorized tasks </li>
            <!--</router-link>-->
            <router-link :to = "'/addService'">
              <li class="stavka-navigacije" v-if="isSerbian"> Nova usluga </li>
              <li class="stavka-navigacije" v-else> New service </li>
            </router-link>
            <!--<router-link>-->
              <li class="stavka-navigacije" v-if="isSerbian"> Novo dostignuće </li>
              <li class="stavka-navigacije" v-else> New achievement </li>
            <!--</router-link>-->
            <li class="stavka-navigacije" v-if="isSerbian" @click="odjaviSe"> Odjavi se </li>
            <li class="stavka-navigacije" v-else @click="odjaviSe"> Log out </li>
          </ul>
        </div>
        <div class="divErrand deo">
          <h2 class="title is-3 belo"> Errand </h2>
          <div class="tekst" v-if="isSerbian"> Obavljanje svakodnevnih poslova nikada nije bilo lakše</div>
          <div class="tekst" v-else> Running errnads has never been easier</div>
        </div>
        <div class="divLista deo" v-if="!isLogedIn">
          <ul class="login">
            <router-link :to="'/login'" >
              <li class="stavka-navigacije" v-if="isSerbian">Prijavi se </li>
              <li class="stavka-navigacije" v-else>Log in </li>
            </router-link>
            <router-link :to="'/register'" >
              <li class="stavka-navigacije" v-if="isSerbian"> Registruj se </li>
              <li class="stavka-navigacije" v-else> Sign up </li>
            </router-link>
          </ul>
        </div>
      </div>
      <div class = "footer-ikonice">
        <div class="glavne-ikonice">
          <img class="ikonica" src = "../assets/footer1.png">
          <img class="ikonica" src = "../assets/footer2.png">
          <img class="ikonica3" src = "../assets/footer3.png">
        </div>
        <div class="nav-back">
          <a @click="scrollToTop">
            <img class="ikonica skrol" src = "../assets/footerUp.png">
          </a>
        </div>
      </div>
    </div>
    <div class="futer-bottom">
      <span> © 2020 Copyright: Errand by Runners </span>
    </div>
  </footer>
</template>

<script>
  export default {
    computed:
    {
      isSerbian()
      {
        return this.$store.state.isSerbian
      },
      isLogedIn()
      {
          return this.$store.state.logedIn
      },
      authUserId() 
      {
        return this.$store.getters['getAuthUserId']
      },
      isAdmin()
      {
          return this.$store.state.isAdmin
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
      scrollToTop() {
          //window.scrollTo(0,0);
          window.scroll({
            top: 0,
            left: 0,
            behavior: 'smooth'
          })
      },
      odjaviSe()
      {
        this.$store.state.logedIn = false
        this.$router.push('/');
      },
      goToProfile()
      {
        return {
            name: "PageViewProfile", 
            params: {
                id: this.$store.state.authUser.id, 
                user: this.$store.state.authUser
            }
        }
      },
      goToAchievements() 
      {
        return {
          name: "PageAchievements", 
          params: {
            id: this.$store.state.authUser.id, 
            user: this.$store.state.authUser
          }
        }
      },
      goToRatings() 
      {
        return {
          name: "PageRatings", 
          params: {
            id: this.$store.state.authUser.id, 
            user: this.$store.state.authUser
          }
        }
      },
      goToUsers() 
      {
          return {
              name: "PageBrowseUsers",
              params: {
                  benefitList: "noBenefit"
              }
          }
      }
    }
  }
</script>

<style scoped>

  .futer
  {
    background-color: rgb(62, 69, 81);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color:white;
  }

  .futer-bottom
  {
    background-color: rgb(0, 0, 0, 0.2);
    width: 100%;
    text-align: center;
    height: 50px;
    color: lightgrey;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }

  .futer-top
  {
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .footer-ikonice
  {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 50px;
    justify-content: flex-end;
  }

  .glavne-ikonice
  {
    padding-right: 20%;
    padding-left: 20%;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .ikonica
  {
    width: 24px;
    height: 24px;
  }

  .ikonica3
  {
    height: 34px;
  }

  .nav-back
  {
    display: flex;
    flex-direction: row;
    align-items: center;
    padding-right: 15px;
  }

  .skrol:hover
  {
    background-color: rgb(84, 94, 110);
  }

  .footer-main
  {
    width:100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    padding-top: 20px;
    padding-bottom: 20px;
  }

  .belo
  {
    color: white;
    margin-bottom: 5px;
  }

  .divErrand
  {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .deo
  {
    width: 45%;
    text-align: center;
  }

  .tekst
  {
    text-align: center;
    color: wheat;
    text-decoration: wavy;
  }

  .stavka-navigacije:hover
  {
    text-decoration: underline;
  }

  .stavka-navigacije
  {
    color: white;
    font-size: 16px;
  }

  .divLista
  {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  .lista
  {
    text-align: left;
    list-style-type: square;
  }

  .login
  {
    text-align: center;
  }
</style>