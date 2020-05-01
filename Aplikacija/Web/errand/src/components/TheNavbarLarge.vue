<template>
    <nav class="navigacija">
        <div class = "navigacija-beginning">
            <router-link :to = "'/'" class="navbar-item">
                <h1 class="title is-4 naslov">Errand</h1>
            </router-link>
            <div class="navbar-start" v-if="isLogedIn">
                <li class="nav-item dropdown">
                    <a class="nav-link svetli" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <img  class = "slika" src = "../assets/menu.svg">
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item">
                            <div>
                                <img src = "../assets/notifications.svg">
                                <span v-if="isSerbian" class = "ikonica"> Obaveštenja </span>
                                <span v-else class = "ikonica"> Notifications </span>
                            </div>
                        </a>
                        <router-link :to = "'/requests'" class="dropdown-item">
                            <img src = "../assets/requests.svg">
                            <span v-if="isSerbian" class = "ikonica"> Zahtevi </span>
                            <span v-else class = "ikonica"> Requests </span>
                        </router-link>
                        <router-link :to = "'/users'" class="dropdown-item">
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
                    </div>
                </li>
            </div>
        </div>
        <div class="navigacija-end">
            <div class = "fleksa">
                <img src = "../assets/uk.svg" class="zastava" @click="prebaciNaEngleski">
                <a class = "language" @click="prebaciNaEngleski"> EN </a>
                <span class = "crtka"> | </span>
                <img src = "../assets/serbia.svg" class="zastava" @click="prebaciNaSrpski">
                <a class = "language" @click="prebaciNaSrpski"> SR </a>
                <div v-if="!isLogedIn"> 
                    <router-link :to = "'/register'" class="button is-primary dugme">
                        <strong v-if="isSerbian">Registruj se</strong>
                        <strong v-else>Sign up</strong> 
                    </router-link>
                    <router-link :to = "'/login'" class="button is-light dugme">
                        <span v-if="isSerbian"> Prijavi se </span>
                        <span v-else> Log in </span>
                    </router-link>
                </div>
                <div class="navbar-item has-dropdown is-hoverable" v-else>
                    <span class="nav-item dropdown">
                        <button class="nav-link dropdown-toggle title is-6 btn btn-link ime svetli" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Jovan Jovanovic
                        </button>
                        <div class="dropdown-menu profil-meni" aria-labelledby="navbarDropdownMenuLink">
                            <router-link :to = "'/profile'" class="dropdown-item">
                                <img src = "../assets/profile.svg">
                                <span v-if="isSerbian" class = "ikonica"> Profil </span>
                                <span v-else class = "ikonica"> Profile </span>
                            </router-link>
                            <router-link :to="'/achievements'" class="dropdown-item">
                                <img src = "../assets/achievements.svg" class="ikonica-uvucena">
                                <span v-if="isSerbian" class = "ikonica"> Dostignuća </span>
                                <span v-else class = "ikonica"> Achievements </span>
                            </router-link>
                            <router-link :to="'/ratings'" class="dropdown-item">
                                <img src = "../assets/ratings.svg" class="ikonica-uvucena">
                                <span v-if="isSerbian" class = "ikonica"> Ocene </span>
                                <span v-else class = "ikonica"> Ratings </span>
                            </router-link>
                            <div class="dropdown-divider"></div>
                            <a @click="odjaviSe" class="dropdown-item">
                                <img src = "../assets/logout.svg">
                                <span v-if="isSerbian" class = "ikonica"> Odjavi se </span>
                                <span v-else class = "ikonica"> Log out </span>
                            </a>
                        </div>
                    </span>
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
      },
      isLogedIn()
      {
          return this.$store.state.logedIn
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
      }
    }
}
</script>

<style scoped>
    .navigacija
    {
        padding: 5px;
        min-height: 70px;
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        position:sticky;
    }

    .navigacija-beginning
    {
        display: flex;
        flex-direction: row;
        flex-grow: 1;
        flex-shrink: 1;
    }

    .navigacija-end
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
        padding-top: 12px;
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

    .meni
    {
        margin-top: 17px;
        margin-left: 10px;
        padding-bottom:0px;
        height: 28px;
    }

    .lista
    {
        margin-top: 0px;
    }

    .dugme
    {
        margin-left: 5px;
        margin-right: 5px;
    }

    .ime
    {
        font-size: 18px;
        padding-top: 5px;
    }

    .profil-meni
    {
        width: 10px;
    }

    .slika
    {
        margin-top: 7px;
    }

    .svetli:hover 
    {
        background-color:rgb(243, 242, 242);
    }

    .naslov
    {
        padding-left: 10px;
        font-weight: bolder;
    }
</style>