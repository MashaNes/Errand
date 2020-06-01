<template>
  <div>
    <div class="main-container">
      <div class="picture-side">
        <div class="media-center">
          <p class="image is-128x128">
            <img class="rounded-image" :src="user.picture ? 'data:;base64,' + user.picture : require('../assets/no-picture.png')">
          </p>
        </div>
        <div class="content">
          {{ fullUserName }}
        </div>
        <div class="nav-buttons">
          <b-button
            size="lg"
            class="ocene-dostignuca button is-primary"
            @click="goToAchievements"
          >
            <span v-if="isSerbian"> Ocene i dostignuća </span>
            <span v-else> Ratings and achievements </span>
          </b-button>
        </div>
        <div class="nav-buttons" v-if="!isMyProfile">
          <b-button
            size="lg"
            class="services button is-primary"
            @click="goToOfferedServices"
          >
            <span v-if="isSerbian"> Lista ponuđenih usluga </span>
            <span v-else> List of offered services </span>
          </b-button>
        </div>
        <div class="nav-buttons" v-if="RequestSelect">
          <button type="button" class="btn btn-info dugmeRequest" @click="nazadNaPretragu">
            <img class="slika-dugme" src="../assets/back.png">
            <span v-if="isSerbian"> Nazad na izbor korisnika </span>
            <span v-else> Back to choosing a user </span>
          </button>
        </div>
        <div class="nav-buttons" v-if="RequestView">
          <button type="button" class="btn btn-info dugmeRequest" @click="goToViewRequest">
            <img class="slika-dugme" src="../assets/back.png">
            <span v-if="isSerbian"> Nazad na pregled zahteva </span>
            <span v-else> Back to request details </span>
          </button>
        </div>
      </div>
      <div class="personal-info">
        <b-list-group>
          <b-list-group-item class="l-group-title">
            <span v-if="isSerbian"
              class="info-title" 
            > Lični podaci </span>
            <span v-else
              class="info-title" 
            > Personal info </span>
            <div class="l-group-btns" v-if="isMyProfile && !isAdminLoggedIn">
              <b-button 
                class="button is-primary title-btn"
                @click="goToProfileEdit()"
              >
                <img class="slika-dugme" src="../assets/edit.png">
                <strong v-if="isSerbian">Izmeni</strong>
                <strong v-else>Edit</strong>
              </b-button>
              <router-link :to="'/settings'"
                class="button is-primary title-btn"
              >
                <img class="slika-dugme" src="../assets/settings-white.png">
                <strong v-if="isSerbian">Podešavanja</strong>
                <strong v-else>Settings</strong>
              </router-link>
            </div>
            <div class="l-group-btns" v-if="!isMyProfile && !isAdminLoggedIn">
              <b-button 
                class="button is-primary title-btn"
                @click="$emit('rateUser')"
              >
                <img class="slika-dugme" src="../assets/rate.png">
                <strong v-if="isSerbian">Oceni korisnika</strong>
                <strong v-else>Rate user</strong>
              </b-button>
              <b-button 
                class="button is-primary title-btn"
                @click="showModalReport = true"
              >
                <img class="slika-dugme" src="../assets/report.png">
                <strong v-if="isSerbian">Prijavi korisnika</strong>
                <strong v-else>Report user</strong>
              </b-button>
              <b-button 
                class="button is-primary title-btn"
                @click="showModal = true"
                :disabled="isInBenefitList || idHelp > 0 || idHelp == undefined"
              >
                <img class="slika-dugme" src="../assets/benefit.png">
                <strong v-if="isSerbian">Dodeli povlastice</strong>
                <strong v-else>Give benefits</strong>
              </b-button>
            </div>
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/email.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <span class="list-value">{{ user.email }}</span>
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/call.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <div class="list-value">
              <div class="phones">{{user.phone}}</div>
            </div>
          </b-list-group-item>
          <b-list-group-item v-if="isMyProfile && addresses && addresses.length > 0">
            <img 
              src="@/assets/address.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <div class="list-value">
              <div class="addr-or-phone-item" v-for="a in firstAddresses" :key="a">{{ a }}</div>
              <div v-text="lastAddress"></div>
            </div>
          </b-list-group-item>
        </b-list-group>
        <b-list-group class="l-item-progress">
          <b-list-group-item>
            <strong style="font-size:20px" v-if="isSerbian"> Prosečna ocena: </strong>
            <strong style="font-size:20px" v-else> Average rating: </strong>
            <b-progress class="mt-2" max="5.0" height="30px" v-if="user.avg_rating">
                <b-progress-bar 
                  :value="user.avg_rating"
                  :variant="progressBarVariant"
                > 
                  <span style="font-size:20px; font-weight: 900; color:black;"> {{formattedRating}}</span> 
                </b-progress-bar>
            </b-progress>
            <span v-else class="list-value" style="word-break:normal;" v-text="isSerbian ? 'Korisnik do sada nije bio ocenjivan' : 'This user has not been rated yet'"></span>
          </b-list-group-item>
        </b-list-group>
      </div>
    </div>
    <ModalAddBenefit v-if="showModal" @close="showModal = false" :user="user"/>
    <ModalReportUser v-if="showModalReport" @close="showModalReport = false" :userToReport="user"/>
    <ModalBenefitAdded v-if="benefitAdded" @close="closeModal"/>
    <ModalSuccess 
      v-if="reportCreated" :textS="'Uspešno prijavljen problem sa korisnikom'" 
      :textE="'User successfuly reported'" @close="closeModalSuccess"
    />
  </div>
</template>

<script>
import ModalBenefitAdded from "@/components/ModalBenefitAdded"
import ModalAddBenefit from "@/components/ModalAddBenefit"
import ModalReportUser from "@/components/ModalReportUser"
import ModalSuccess from "@/components/ModalSuccess"


export default {
  props: {
    user: {
      type: Object,
      required: true
    },
    isMyProfile: {
      type: Boolean,
      required: true
    },
    addresses: {
      required: false
    },
    RequestSelect:
    {
      type: Boolean,
      required: true
    },
    RequestView: {
      type: Object,
      required: false,
      default: null
    }
  },
  components:
  {
    ModalAddBenefit,
    ModalReportUser,
    ModalBenefitAdded,
    ModalSuccess
  },
  data()
  {
    return{
      showModal : false,
      showModalReport: false,
      idHelp: null
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.first_name + " " +this.user.last_name
    },
    progressBarVariant() {
      return this.user.avg_rating < 2.5 ? 'danger' : 
             this.user.avg_rating < 4.5 ? 'warning' : 'success'
    },
    formattedRating() {
      return this.user.avg_rating ? this.user.avg_rating.toFixed(2) : null
    },
    isUserBenefitListLoaded()
    {
      return this.$store.state.usersWithBenefit != null
    },
    isInBenefitList()
    {
      if(!this.isUserBenefitListLoaded)
        return true
      else
      {
        var id = -1
        this.$store.state.usersWithBenefit.forEach((item, index) =>
        {
            if(item.benefit_user.id == this.user.id)
              this.idHelp = id = item.benefit_user.id
            if(index == this.$store.state.usersWithBenefit.length - 1 && id == -1)
              this.idHelp = id = -2
        })
        return id > 0
      }
    },
    benefitAdded(){
      return this.$store.state.userAdded
    },
    reportCreated() {
      return this.$store.state.success
    },
    firstAddresses() {
      const lastIndex = this.addresses.length
      const arrayCopy = this.addresses.map(addr => addr.name)
      arrayCopy.splice(lastIndex-1, 1)
      return arrayCopy
    },
    lastAddress() {
      if(!this.addresses || this.addresses.length == 0)
        return ""
      return this.addresses[this.addresses.length-1].name
    },
    isAdminLoggedIn()
    {
      return this.$store.state.isAdmin
    }
  },
  methods: {
    goToProfileEdit() {
      this.$emit("editProfile");
    },
    goToAchievements() {
      this.$router.push({
        name: "PageAchievements",
        params: {
          id: this.user.id,
          user: this.user,
          RequestSelect: this.RequestSelect
        }
      })
    },
    goToOfferedServices()
    {
      this.$router.push({
        name: "PageOfferedServices",
        params: {
          id: this.user.id,
          RequestSelect: this.RequestSelect
        }
      })
    },
    closeModal(){
      this.$store.state.userAdded = false  
    },
    closeModalSuccess() {
      this.$store.state.success = false
    },
    nazadNaPretragu()
    {
      this.$router.push({ name: 'PageBrowseUsers', params: {benefitList: "noBenefit", servicesList:this.$store.servicesRequired}})
    },
    goToViewRequest() {
      this.$router.push({
        name: "PageViewRequest",
        params: {
          id: this.RequestView.request.id,
          request: this.RequestView.request,
          startingView: this.RequestView.view
        }
      })
    }
  },
  created()
  {
    if(this.$store.state.usersWithBenefit == null)
      this.$store.dispatch("fillUsersWithBenefit")
  }
}
</script>

<style scoped>
  
  .list-group {
    background-color:inherit;
  }

  .list-group-item {
    display: flex;
    justify-content: space-between;
    border: hidden;
    border-bottom: 1px solid lightgray;
    border-radius: 0px;
    align-items: center;
    background-color:inherit;
  }

  .list-key {
    font-weight: bold;
    font-size: 14px;
    flex-grow: 1;
    flex-shrink: 1;
    text-align: left;
  }

  .list-value {
    flex-grow: 2;
    flex-shrink: 1;
    text-align: left;
    font-size: 20px;
    margin-left: 30px;
    word-break: break-all;
  }

  .main-container {
    margin-top: 30px;
    margin-left: 10%;
    margin-right: 10%;
    display: flex;
    flex-direction: row;
    align-items:flex-start;
    border-radius: 10px;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height: 250px;
    width:250px;
    object-fit:cover;
  }

  .content {
    background-color: black;
    font-size: 20px;
    color:white;
    text-align: center;
    margin-top: 15px;
    margin-bottom: 2px;
    border-radius: 5px;
    word-break: break-all;
  }

  .info-title {
    font-size: 25px;
    font-weight: bold;
    color: black;
    flex-grow:1;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
  }

  .personal-info {
    display:flex;
    flex-direction: column;
    margin-left: 15px;
    flex-grow:2;
    margin-bottom: 30px;
  }

  .picture-side {
    flex-grow:1;
    margin-bottom: 30px;
  }

  .nav-buttons {
    display:flex;
    width: 100%;
    justify-content: center;
  }

  .ocene-dostignuca {
    margin: 10% 0 20px 0; 
    width: 100%;
    font-size: 20px;
    font-weight: bold;
  }

  .services {
    width:100%;
    font-size: 20px;
    font-weight: bold;
  }

  .addr-or-phone-item {
    border-bottom:dotted 2px lightgray;
    margin-bottom: 7px;
    padding-bottom: 7px;
  }

  .l-group-title {
    display:flex;
    flex-direction:row;
  }

  .title-btn {
    margin:5px 5px 0 5px;
    text-decoration: none;
  }

  .media-center {
    display:flex;
    flex-direction:column;
    align-items:center;
  }

  .l-group-btns {
    display:flex;
    flex-direction: column;
    margin-left:30px;
  }

  .l-item-progress {
    width:85%;
  }

  .slika-dugme
  {
    width: 22px;
    height: 22px;
    margin-right: 5px;
  }

  .dugmeRequest
  {
    margin-top:25px;
  }

  @media only screen and (max-width: 750px)
  {
    .main-container {
      flex-direction: column;
      margin-top: 30px;
      margin-left: 1%;
      margin-right: 1%;
      align-items:center;
      border-radius: 10px;
      font-size: 10px;
    }

    .list-value {
      flex-grow: 2;
      flex-shrink: 1;
      text-align: left;
      font-size: 17px;
      margin-left: 2%;
    }

    .l-group-title {
      border-top:1px solid lightgray;
    }

    .l-group-btns {
      display:flex;
      flex-direction: column;
      flex-grow:1;
    }

    .list-group-item {
      padding-left:1%;
      padding-right:1%;
    }

    .l-item-progress {
      width:100%;
    }

  }


</style>