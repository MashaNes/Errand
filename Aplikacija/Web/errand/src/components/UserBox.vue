<template>
    <b-card 
      :border-variant="progressBarVariant"
      no-body
    >
      <b-card-body>
        <div class="media-center">
          <p class="image is-96x96" @click="goToProfile">
            <img class="rounded-image" :src="user.picture ? 'data:;base64,' + user.picture : require('../assets/no-picture.png')">
          </p>
          <p class = "dodaj-dugme"> 
            <button type="button" class="btn btn-success dugme" v-if="BenefitList && isSerbian" @click="showModal = true"> Dodaj</button> 
            <button type="button" class="btn btn-success dugme" v-if="BenefitList && !isSerbian" @click="showModal = true"> Add </button>
          </p>
          <p class = "dodaj-dugme" v-if="RequestSelect"> 
            <button type="button" class="btn btn-success dugme" v-if="isSerbian" @click="showAreYouSure = true"> Izaberi</button> 
            <button type="button" class="btn btn-success dugme" v-else @click="showAreYouSure = true"> Choose </button>
          </p>
        </div>
          
        <div class="info">
          <div class="info-element">
            <img 
              src="@/assets/signature.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <span @click="goToProfile" class="full-name-span">{{fullUserName}}</span>
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
          <div class="info-element grade">
            <span v-if="isSerbian" class="grade-label">
              Prosečna ocena:
            </span>
            <span v-else class="grade-label">
              Average rating:
            </span>
          
            <b-progress class="mt-2" max="5.0" height="20px" v-if="user.avg_rating">
              <b-progress-bar 
                :value="user.avg_rating"
                :variant="progressBarVariant"
              > 
                <span class="rating-grade"> {{user.avg_rating}} </span> 
              </b-progress-bar>
            </b-progress>
            <span v-else class="rating-message" v-text="isSerbian ? 'Korisnik do sada nije bio ocenjivan' : 'This user has not been rated yet'"></span>
          </div>
        </div>
      </b-card-body>
      <ModalAddBenefit v-if="showModal" @close="showModal = false" :user="user"/>
      <ModalAreYouSure v-if="showAreYouSure==true"
                         :naslovS="'Odabir korisnika'"
                         :naslovE="'Choosing a user'"
                         :tekstS="'Da li ste sigurni da želite da odabere ovog korisnika?'"
                         :tekstE="'Are you sure you want to choose this user?'"
                         @yes="yes"
                         @close="showAreYouSure = false"/>
    </b-card>
</template>

<script>
import ModalAddBenefit from "@/components/ModalAddBenefit"
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    props: {
      user: {
        required: true, 
        type: Object
      },
      BenefitList:
      {
        type: Boolean,
        required: false,
        default: false
      },
      RequestSelect:
      {
        type: Boolean,
        required: true
      }
    },
    data()
    {
      return{
        showModal:false,
        showAreYouSure: false
      }
    },
    computed: {
      isSerbian() {
        return this.$store.state.isSerbian
      },
      progressBarVariant() {
        return this.user.avg_rating < 2.5 ? 'danger' : 
               this.user.avg_rating < 4.5 ? 'warning' : 'success'
      },
      fullUserName() {
        return this.user.first_name + " " +this.user.last_name
      }
    },
    methods: {
      goToProfile() {
        //prepraviti da se stranici kao prop prosledi user
        this.$router.push({name: "PageViewProfile", params: {id: this.user.id, user: this.user, RequestSelect:this.RequestSelect}})
      },
      yes()
      {
        this.$store.state.requestInCreation.direct_user = this.user
        this.$router.push({ name: 'PageNewRequest', params: {requestProp: this.$store.state.requestInCreation, stepProp:7}})
      }
    },
    components:
    {
      ModalAddBenefit,
      ModalAreYouSure
    }
}
</script>

<style scoped>
  .dugme
  {
    padding: 1px;
    padding-right: 5px;
    padding-left: 5px;
    margin-top: 7px;
  }

  .dodaj-dugme
  {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:96px;
    width:96px;
    object-fit:cover;
    cursor:pointer;
  }

  .card-body {
    display:flex;
    flex-direction: row;
  }

  .card-header {
    font-size: 15px;
    font-weight: bold;
    border-top-left-radius: 15px;
    border-top-right-radius: 15px;
  }

  .card-footer {
    font-size: 15px;
    font-weight: bold;
    border-bottom-left-radius: 15px;
    border-bottom-right-radius: 15px;
    display:flex; 
    flex-direction:row;
  }

  .card {
    margin: 10px 15% 10px 15%;
    border-radius: 15px;
  }

  .rating-grade {
    font-size:15px; 
    font-weight: 700; 
    color:black;
  }

  .rating-message {
    padding-top:5px;
    font-size:18px;
    font-weight: bold;
  }

  .mt-2 {
    width:60%; 
    min-width:70px;
  }

  .grade-label {
    margin: 5px 10px 0 0;
  }

  .info {
    display:flex;
    flex-direction: column;
    margin-left: 10px;
    width:100%;
  }

  .info-element {
    display:flex;
    flex-direction: row;
    word-break:break-all;
    margin: 5px 0 0 5px;
    align-items: center;
  }

  .grade {
    word-break:normal;
  }

  .full-name-span:hover {
    cursor: pointer !important;
    text-decoration: underline;
    color:lightseagreen;
  }

  @media only screen and (max-width:650px)
  {
    .card-footer {
      flex-direction: column;
    }

    .card-body {
      flex-direction:column;
      align-items:center;
      padding:10px;
    }

    .card {
      margin:10px 10% 0 10%;
      font-size: 15px;
      
    }

    .grade-label {
      margin: 5px 5px 0 0;
    }
    
    .info {
      margin-left: 0;
    }

    .info-element {
      flex-direction: column;
      align-items: flex-start;
      margin-top:15px;
    }

    .mt-2 {
      width:80%;
    }

  }

  /* @media only screen and (max-width:500px)
  {
    .card {
      margin: 40px 10px 40px 10px;
    }

  } */

</style>