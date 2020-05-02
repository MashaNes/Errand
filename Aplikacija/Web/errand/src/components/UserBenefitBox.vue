<template>
    <b-card :border-variant="progressBarVariant" no-body>
      <b-card-body>
        <div class="telo">
          <div class = "ikonice">
            <a @click="isEdit = true" class="ikonica-link">
              <img src = "../assets/edit.svg" class = "slika desno" v-b-popover.hover.top="popoverEditText">
            </a>
            <a @click="showModal = true" class="ikonica-link">
              <img src = "../assets/remove.svg" class = "slika dodatno-desno" v-b-popover.hover.top="popoverDeleteText">
            </a>
          </div>
          <div class="ostatak">
            <div class="media-center">
              <p class="image is-96x96">
                <img class="rounded-image" :src="user.picture">
              </p>
              <div class="discount">
                <span v-if="!isEdit">{{userBenefit.benefit}} % </span>
                <div class="edit-wrapper" v-else>
                  <div class="edit-element"> 
                    <input class = "benefitEdit" type="number" min="1" max="100" v-model="newBenefit"> % 
                  </div>
                  <div class="edit-element"> 
                    <button type="button" class="btn btn-success dugme" @click="saveBenefit">
                      <img src="../assets/finished.svg"> 
                    </button>
                    <button type="button" class="btn btn-danger dugme" @click="discardBenefit">
                      <img src="../assets/failed.svg"> 
                    </button>
                  </div>
                </div>
              </div>
            </div>
              
            <div class="info">
              <div class="info-element">
                <img 
                  src="@/assets/signature.svg" 
                  height = "20" 
                  width = "20"
                  style = "margin-right: 15px"
                />
                <span>{{fullUserName}}</span>
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
              
                <b-progress class="mt-2" max="5.0" height="20px">
                  <b-progress-bar 
                    :value="user.rating"
                    :variant="progressBarVariant"
                  > 
                    <span class="rating-grade"> {{user.rating}} </span> 
                  </b-progress-bar>
                </b-progress>
              </div>
            </div>
          </div>
        </div>
      </b-card-body>
      <ModalAreYouSure :naslovS="'Uklanjanje korisnika'" 
                       :tekstS="'Da li ste sigurni da želite da uklonite ovog korisnika?'" 
                       :naslovE="'Removing a user'"
                       :tekstE="'Are you sure you want to remove this user?'"
                       @close="showModal = false" @yes="removeUser" v-if="showModal"/>
    </b-card>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    props: {
      userBenefit: {
        required: true, 
        type: Object
      }
    },
    components:
    {
      ModalAreYouSure
    },
    data()
    {
        return{
            user: this.userBenefit.user,
            showModal : false,
            isEdit: false,
            newBenefit: this.userBenefit.benefit
        }
    },
    computed: {
      isSerbian() {
        return this.$store.state.isSerbian
      },
      progressBarVariant() {
        return this.user.rating < 2.5 ? 'danger' : 
               this.user.rating < 4.5 ? 'warning' : 'success'
      },
      fullUserName() {
        return this.user.firstName + " " +this.user.lastName
      },
      popoverEditText()
      {
        if(this.isSerbian)
          return "Izmenite povlastice ovom korisniku"
        else
          return "Edit this user's benefits"
      },
      popoverDeleteText()
      {
        if(this.isSerbian)
          return "Uklonite ovog korisnika iz Vaše liste"
        else
          return "Remove this user from your list"
      }
    },
    methods:
    {
      removeUser()
      {
        console.log(this.user.email)
        this.showModal = false
      },
      saveBenefit()
      {
        this.userBenefit.benefit = this.newBenefit
        this.discardBenefit()
      },
      discardBenefit()
      {
        this.newBenefit = this.userBenefit.benefit
        this.isEdit = false
      }
    }
}
</script>

<style scoped>
  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:96px;
    width:96px;
    object-fit:cover;
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
    margin: 10px 5% 10px 5%;
    border-radius: 15px;
  }

  .rating-grade {
    font-size:15px; 
    font-weight: 700; 
    color:black;
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

  .discount
  {
    text-align: center;
    font-size: 24px;
    font-weight: 800;
    color: rgb(72, 72, 155);
    margin-top: 5px;
  }

  .benefitEdit
  {
    font-size: 18px;
    width: 50px;
    height: 25px;
    margin-right:5px;
  }

  .edit-wrapper
  {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .edit-element
  {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .dugme
  {
    padding:1px;
    margin: 5px;
  }

  .slika
  {
    width: 25px;
    height: 25px;
  }

  .telo
  {
    display: flex;
    flex-direction: row-reverse;
    width: 100%;
  }

  .ostatak
  {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .ikonice
  {
    display: flex;
    flex-direction: row;
  }

  .ikonica-link
  {
    height:fit-content
  }

  .desno
  {
    margin-right: 15px;
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

    .ostatak{
      flex-direction: column;
    }

    .dodatno-desno
    {
      margin-right:10px;
    }
  }

</style>