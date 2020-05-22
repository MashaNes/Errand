<template>
    <nav class="navigacija">
        <div class = "top-navigacija"> 
            <div class="top-navigacija-beginning">
                <router-link :to = "'/'" class="navbar-item" v-if="!isLogedIn">
                    <h1 class="title is-4">Errand</h1>
                </router-link>
                <router-link :to = "'/requests'" class="navbar-item" v-if="isLogedIn && !isAdmin">
                    <h1 class="title is-4">Errand</h1>
                </router-link>
                <router-link :to = "'/statistics'" class="navbar-item" v-if="isLogedIn && isAdmin">
                    <h1 class="title is-4">Errand</h1>
                </router-link>
                <div class="navbar-start" v-if="isLogedIn">
                    <li class="nav-item dropdown">
                        <a class="nav-link" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <img  class = "slika" src = "../assets/menu.svg">
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink"  v-if="!isAdmin">
                            <a class="dropdown-item">
                                <div>
                                    <img src = "../assets/notifications.svg">
                                    <span v-if="isSerbian" class = "ikonica"> Obaveštenja </span>
                                    <span v-else class = "ikonica"> Notifications </span>
                                </div>
                            </a>
                            <router-link :to = "goToProfile()" class="dropdown-item">
                                <img src = "../assets/profile.svg">
                                <span v-if="isSerbian" class = "ikonica"> Profil </span>
                                <span v-else class = "ikonica"> Profile </span>
                            </router-link>
                            <router-link :to="goToAchievements()" class="dropdown-item">
                                <img src = "../assets/achievements.svg" class="ikonica-uvucena">
                                <span v-if="isSerbian" class = "ikonica"> Dostignuća </span>
                                <span v-else class = "ikonica"> Achievements </span>
                            </router-link>
                            <router-link :to="goToRatings()" class="dropdown-item">
                                <img src = "../assets/ratings.svg" class="ikonica-uvucena">
                                <span v-if="isSerbian" class = "ikonica"> Ocene </span>
                                <span v-else class = "ikonica"> Ratings </span>
                            </router-link>
                            <router-link :to = "'/newRequest'" class="dropdown-item">
                                <img src = "../assets/add.svg">
                                <span v-if="isSerbian" class = "ikonica"> Novi zahtev </span>
                                <span v-else class = "ikonica"> New request </span>
                            </router-link>
                            <router-link :to = "'/requests'" class="dropdown-item">
                                <img src = "../assets/requests.svg">
                                <span v-if="isSerbian" class = "ikonica"> Zahtevi </span>
                                <span v-else class = "ikonica"> Requests </span>
                            </router-link>
                            <router-link :to = "goToUsers()" class="dropdown-item">
                                <img src = "../assets/search.svg">
                                <span v-if="isSerbian" class = "ikonica"> Pretraži korisnike </span>
                                <span v-else class = "ikonica"> Search for users </span>
                            </router-link>
                            <router-link :to = "'/settings'" class="dropdown-item">
                                <img src = "../assets/settings.svg">
                                <span v-if="isSerbian" class = "ikonica"> Podešavanja </span>
                                <span v-else class = "ikonica"> Settings </span>
                            </router-link>
                            <router-link :to = "'/help'" class="dropdown-item">
                                <img src = "../assets/help.svg">
                                <span v-if="isSerbian" class = "ikonica"> Pomoć </span>
                                <span v-else class = "ikonica"> Help </span>
                            </router-link>
                            <div class="dropdown-divider"></div>
                            <a @click="odjaviSe" class="dropdown-item">
                                <img src = "../assets/logout.svg">
                                <span v-if="isSerbian" class = "ikonica"> Odjavi se </span>
                                <span v-else class = "ikonica"> Log out </span>
                            </a>
                        </div>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink" v-else>
                            <router-link :to = "'/statistics'" class="dropdown-item">
                                <img src = "../assets/statistics.svg">
                                <span v-if="isSerbian" class = "ikonica"> Pregled statistike </span>
                                <span v-else class = "ikonica"> View statistics </span>
                            </router-link>
                            <a class="dropdown-item">
                                <img src = "../assets/reports.svg">
                                <span v-if="isSerbian" class = "ikonica"> Upravljanje prijavama </span>
                                <span v-else class = "ikonica"> Handle reports </span>
                            </a>
                            <a class="dropdown-item">
                                <img src = "../assets/other.svg" class="slichka">
                                <span v-if="isSerbian" class = "ikonica"> Nekategorizovani zadaci </span>
                                <span v-else class = "ikonica"> Uncategorized tasks </span>
                            </a>
                            <a class="dropdown-item">
                                <img src = "../assets/add-service.svg" class="slichka">
                                <span v-if="isSerbian" class = "ikonica"> Nova usluga </span>
                                <span v-else class = "ikonica"> New service </span>
                            </a>
                            <a class="dropdown-item">
                                <img src = "../assets/achievement-add.svg" class="slichka">
                                <span v-if="isSerbian" class = "ikonica"> Novo dostignuće </span>
                                <span v-else class = "ikonica"> New achievement </span>
                            </a>
                            <div class="dropdown-divider"></div>
                            <a @click="odjaviSe" class="dropdown-item">
                                <img src = "../assets/logout.svg">
                                <span v-if="isSerbian" class = "ikonica"> Odjavi se </span>
                                <span v-else class = "ikonica"> Log out </span>
                            </a>
                        </div>
                    </li>
                </div>
            </div>
            <div class="top-navigacija-end">
                <div class = "fleksa">
                    <img src = "../assets/uk.svg" class="zastava" @click="prebaciNaEngleski">
                    <a class = "language" @click="prebaciNaEngleski"> EN </a>
                    <span class = "crtka"> | </span>
                    <img src = "../assets/serbia.svg" class="zastava" @click="prebaciNaSrpski">
                    <a class = "language" @click="prebaciNaSrpski"> SR </a>
                </div>
            </div>
        </div>
        <div class = "bottom-navigacija" v-if="!isLogedIn"> 
            <router-link :to = "'/register'" class="button is-primary dugme">
              <strong v-if="isSerbian">Registruj se</strong>
              <strong v-else>Sign up</strong>
            </router-link>
            <router-link :to = "'/login'" class="button is-light dugme">
              <span v-if="isSerbian"> Prijavi se </span>
              <span v-else> Log in </span>
            </router-link>
        </div>
    </nav>
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
      odjaviSe()
      {
        this.$store.state.logedIn = false
        this.$router.push('/');
        this.$store.dispatch("logoutUser") 
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
    .navigacija
    {
        padding: 5px;
        min-height: 60px;
        width: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
    .bottom-navigacija
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        width: 100%;
    }

    .dugme
    {
        margin-right: 20px;
        margin-left: 20px;
        margin-bottom: 5px;
        margin-top: 0px;
    }

    .top-navigacija
    {
        display: flex;
        flex-direction: row;
        width:100%;
        justify-content: flex-start;
    }

    .top-navigacija-beginning
    {
        display: flex;
        flex-direction: row;
        flex-grow: 1;
        flex-shrink: 1;
    }

    .top-navigacija-end
    {
        justify-self: flex-end;
        flex-grow: 1;
        flex-shrink: 1;
    }

    .fleksa
    {
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        padding-right: 7px;
    }

    .zastava
    {
        height: 20px;
        margin-top: 7px;
        margin-left: 5px;
    }

    .ikonica
    {
        margin-left: 10px
    }

    .ikonica-uvucena
    {
        margin-left: 20px
    }

    .language
    {
        margin:5px;
    }

    .crtka
    {
        margin-top:5px
    }

    .slichka
    {
        width:24px;
        height:24px;
    }
</style>