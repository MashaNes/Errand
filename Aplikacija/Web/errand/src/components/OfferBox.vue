<template>
    <b-card 
      :border-variant="progressBarVariant"
      no-body
    >
      <b-card-body>
        <div class="user-info">
          <div class="media-center">
            <!-- <p class="image is-96x96" @click="goToProfile"> -->
            <p class="image">
              <img class="rounded-image" :src="offer.created_by.picture ? 'data:;base64,' + offer.created_by.picture : require('../assets/no-picture.png')">
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
              <!-- <span @click="goToProfile" class="full-name-span">{{fullUserName}}</span> -->
              <span class="full-name-span">{{fullUserName}}</span>
            </div>
            <div class="info-element grade">
              <span v-if="isSerbian" class="grade-label">
                Prosečna ocena:
              </span>
              <span v-else class="grade-label">
                Average rating:
              </span>
            
              <b-progress class="mt-2" max="5.0" height="20px" v-if="offer.created_by.avg_rating">
                <b-progress-bar 
                  :value="offer.created_by.avg_rating"
                  :variant="progressBarVariant"
                > 
                  <span class="rating-grade"> {{offer.created_by.avg_rating}} </span> 
                </b-progress-bar>
              </b-progress>
              <span v-else class="bold-message" v-text="isSerbian ? 'Korisnik do sada nije bio ocenjivan' : 'This user has not been rated yet'"></span>
            </div>
          </div>
        </div>
        <div class="offer">
          <div class="offer-details">
            <span v-text="(isSerbian ? 'Tip naplate: ' : 'Payment type: ') + paymentType" class="bold-message"></span>
            <span v-text="(isSerbian ? 'Cena: ' : 'Price: ') + offer.payment_ammount + ' din'" class="bold-message"></span>
          </div>
          <div class="offer-buttons">
            <button 
              type="button" class="btn btn-info" 
              style="margin-right: 5px;" v-text="isSerbian ? 'Prihvati' : 'Accept'"
              @click="accept=true; showAreYouSure=true">
            </button>
            <button 
              type="button" class="btn btn-info" 
              v-text="isSerbian ? 'Odbij' : 'Decline'"
              @click="accept=false; showAreYouSure=true">
            </button>
          </div>
        </div>
      </b-card-body>

      <ModalAreYouSure 
          :naslovS="'Da li ste sigurni?'"
          :naslovE="'Are you sure?'"
          :tekstS="tekstS"
          :tekstE="tekstE"
          @yes="makeDecission"
          @close="dismissDecission"
          v-if="showAreYouSure"
        />

    </b-card>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"

export default {
  components: {
    ModalAreYouSure
  },
  props: {
    offer: {
      required: true, 
      type: Object
    }
  },
  data()
  {
    return{
      showModal:false,
      showAreYouSure: false,
      accept: false
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    progressBarVariant() {
      return this.offer.created_by.avg_rating < 2.5 ? 'danger' : 
              this.offer.created_by.avg_rating < 4.5 ? 'warning' : 'success'
    },
    fullUserName() {
      return this.offer.created_by.first_name + " " +this.offer.created_by.last_name
    },
    paymentType() {
      let returnValue = ""
      switch(this.offer.payment_type) {
        case "0":
          if(this.isSerbian)
            returnValue = "po satu"
          else 
            returnValue = "per hour"
          break
        case "1":
          if(this.isSerbian)
            returnValue = "po kilometru"
          else
            returnValue = "per kilometer"
          break
        case "2":
          if(this.isSerbian)
            returnValue = "fiksno"
          else
            returnValue = "fixed"
          break
        case "3":
          if(this.isSerbia)
            returnValue = "početna cena"
          else
            returnValue = "starting price"
      }
      return returnValue
    },
    tekstS() {
      if(this.accept)
        return "Da li ste sigurni da želite da prihvatite ovu ponudu? Nakon prihvatanja, nećete moći da promenite odluku."
      else 
        return "Da li ste sigurni da želite da odbijete ovu ponudu?"
    },
    tekstE() {
      if(this.accept)
        return "Are you sure you want to accept this offer? After accepting, you will not be able to change your decission."
      else
        return "Are you sure you want to decline this offer?"
    }
  },
  methods: {
    // goToProfile() {
    //   //prepraviti da se stranici kao prop prosledi user
    //   this.$router.push({name: "PageViewProfile", params: {id: this.user.id, user: this.user, RequestSelect:this.RequestSelect}})
    // },
    makeDecission() {
      if(this.accept)
        this.$emit('acceptOffer', this.offer)
      else
        this.$emit('rejectOffer', this.offer)
      this.showAreYouSure = false
    },
    dismissDecission() {
      this.showAreYouSure = false
    }
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

  .user-info {
    display: flex;
    flex-direction: row;
    padding-bottom: 5px;
  }

  .offer {
    display: flex;
    justify-content: space-between;
    border-top: 1px dashed lightgray;
  }

  .offer-details {
    display: flex;
    flex-direction: column;
  }

  .offer-buttons {
    display:flex;
    align-items: flex-end;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:60px;
    width:60px;
    object-fit:cover;
    cursor:pointer;
  }

  .card-body {
    display:flex;
    flex-direction: column;
    padding: 10px;
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
    margin: 10px 0 10px 0 !important;
    border-radius: 15px;
    width:100% !important;
  }

  .rating-grade {
    font-size:15px; 
    font-weight: 700; 
    color:black;
  }

  .bold-message {
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

    .user-info {
      flex-direction: column;
      align-items:center;
      width:100%;
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
  
    .offer {
      flex-direction: column;
      width: 100%;
      padding-left: 5px;
      padding-right: 5px;
    }

    .offer-details {
      align-self: flex-start;
      margin-bottom: 10px;
    }

    .offer-buttons {
      align-self: flex-end;
    }

  }

  /* @media only screen and (max-width:500px)
  {
    .card {
      margin: 40px 10px 40px 10px;
    }

  } */

</style>