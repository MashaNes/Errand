<template>
    <b-card 
      :border-variant="progressBarVariant"
      no-body
      id="scroll-card"
    >
  
      <b-card-body>
        <div class="user-info">
          <div class="media-center">
            <p class="image" @click="goToProfile">
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
                  <span class="rating-grade"> {{formattedRating}} </span> 
                </b-progress-bar>
              </b-progress>
              <span v-else class="bold-message" v-text="isSerbian ? 'Korisnik do sada nije bio ocenjivan' : 'This user has not been rated yet'"></span>
            </div>
          </div>

          <img 
            v-if="!opened" src="@/assets/down-chevron.svg" height="22" width="22" 
            class="toggle-image" @click="toggleOfferBox"
          >
          <img 
            v-else src="@/assets/up-chevron.svg" height="22" width="22"  
            class="toggle-image" @click="toggleOfferBox"
          > 
        </div>
        <div :class="offer.edit ? 'offer' : 'offer-no-edits'" v-if="opened">
          <div class="offer-details">
            <span v-text="(isSerbian ? 'Tip naplate: ' : 'Payment type: ') + paymentType" class="bold-message"></span>
            <span v-text="(isSerbian ? 'Cena: ' : 'Price: ') + offer.payment_ammount + ' din'" class="bold-message"></span>
            <div v-if="offer.edit" class="edit">
              <span v-text="isSerbian ? 'Zahtevane izmene' : 'Required edits'" class="edit-title"></span>
              <div class="edit-details">
                <div v-if="offer.edit.time">
                  <span v-text="isSerbian ? 'Datum i vreme' : 'Date and time'" style="font-size: 20px;"></span>
                  <div class="left-padding">
                    <div v-if="isSerbian" class="red-color"> Trenutni datum i vreme: {{dateAndTime(oldDateAndTime)}}</div>
                    <div v-else class="red-color"> Current date and time: {{oldDateAndTime | showTime}}</div>
                    <div v-if="isSerbian" class="green-color"> Novi datum i vreme: {{dateAndTime(offer.edit.time)}}</div>
                    <div v-else class="green-color"> New date and time: {{offer.edit.time | showTime}}</div>
                  </div>
                </div>
                <div v-if="offer.edit && offer.edit.tasks.length > 0" :style="offer.edit.time ? 'margin-top:15px;' : ''">
                  <span v-text="isSerbian ? 'Nove adrese' : 'New addresses'" style="font-size: 20px;"></span>
                  <div v-for="item in offer.edit.tasks" :key="item.task" class="left-padding">
                    <div v-text='(isSerbian ? "Zadatak " : "Task ") + "\"" + getOldTask(item.task).name + "\":"'></div>
                    <div class="left-padding" style="margin-top: 5px !important;">
                      <div v-text='(isSerbian ? "Trenutna adresa: " : "Current address: ") + getOldTask(item.task).address.name' class="red-color"></div>
                      <div v-text='(isSerbian ? "Nova adresa: " : "New address: ") + item.address.name' class="green-color"></div>
                    </div>
                  </div>
                </div>
                <b-button style="margin-top: 10px;" v-if="offer.edit && offer.edit.tasks.length > 0" @click="isMapOpened = !isMapOpened">
                  <span v-if="!isMapOpened" v-text="isSerbian ? 'Prikaži izmene na mapi' : 'Show changes on map'"></span>
                  <span v-if="isMapOpened" v-text="isSerbian ? 'Zatvori mapu' : 'Close map'"></span>
                </b-button>

                <b-card-text  v-if="offer.edit && offer.edit.tasks.length > 0" :class="isMapOpened ? 'visible' : 'invisible'">
                  <Map />
                </b-card-text>

              </div>
            </div>
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
import Map from "@/components/Map"
import {mapState} from 'vuex'

export default {
  components: {
    ModalAreYouSure,
    Map
  },
  props: {
    offer: {
      required: true, 
      type: Object
    },
    oldTasklist: {
      required: false,
      type: Array
    },
    oldDateAndTime: {
      required: false,
      type: String
    },
    myIndex: {
      required: true,
      type: Number
    },
    request: {
      required: true,
      type: Object
    }
  },
  data()
  {
    return{
      showModal:false,
      showAreYouSure: false,
      accept: false,
      isMapOpened: false,
      opened: false
    }
  },
  computed: {
    ...mapState(['openedOffersOrEdits']),
    isSerbian() {
      return this.$store.state.isSerbian
    },
    progressBarVariant() {
      return this.offer.created_by.avg_rating < 2.5 ? 'danger' : 
              this.offer.created_by.avg_rating < 4.5 ? 'warning' : 'success'
    },
    formattedRating() {
      return this.offer.created_by.avg_rating ? this.offer.created_by.avg_rating.toFixed(2) : null
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
      if(this.accept) {
        if(this.offer.edit)
          return "Da li ste sigurni da želite da prihvatite ovu ponudu i sve zahtevane izmene? Nakon prihvatanja, nećete moći da promenite odluku."
        else
          return "Da li ste sigurni da želite da prihvatite ovu ponudu? Nakon prihvatanja, nećete moći da promenite odluku."
      }
      else 
        return "Da li ste sigurni da želite da odbijete ovu ponudu?"
    },
    tekstE() {
      if(this.accept) {
        if(this.offer.edit)
          return "Are you sure you want to accept this offer and all the requested changes? After accepting, you will not be able to change your decission."
        else
          return "Are you sure you want to accept this offer? After accepting, you will not be able to change your decission."
      }
      else
        return "Are you sure you want to decline this offer?"
    }
  },
  watch: {
    // eslint-disable-next-line no-unused-vars
    openedOffersOrEdits(newVal, oldVal) {
      if(newVal[this.myIndex] == true) {
        this.opened = true
      }
      else {
        this.opened = false
        this.isMapOpened = false
      }
    }
  },
  methods: {
    goToProfile() {
      this.$router.push({
        name: "PageViewProfile", 
        params: {
          id: this.offer.created_by.id, 
          user: this.offer.created_by, 
          RequestSelect: false,
          RequestView: {
            request: this.request,
            view: "Offers"
          }
        }
      })
    },
    makeDecission() {
      if(this.accept)
        this.$emit('acceptOffer', this.offer)
      else
        this.$emit('rejectOffer', this.offer)
      this.showAreYouSure = false
    },
    dismissDecission() {
      this.showAreYouSure = false
    },
    getOldTask(taskId) {
      return this.oldTasklist.find(task => task.id == taskId)
    },
    dateAndTime(timeString) {
      var date = new Date(timeString)
            
      var day = date.getUTCDate()
      var month = date.getUTCMonth()+1
      var year = date.getUTCFullYear()
      var hours = date.getUTCHours()
      var minutes = date.getUTCMinutes()
      
      var monthString = ""
      var hoursString = hours
      var minutesString = minutes
      if(hours < 10)
        hoursString = "0" + hours
      if(minutes < 10)
        minutesString = "0" + minutes

      switch(month) {
        case 1: 
          monthString = "Januar"
          break
        case 2: 
          monthString = "Februar"
          break
        case 3: 
          monthString = "Mart"
          break
        case 4: 
          monthString = "April"
          break
        case 5: 
          monthString = "Maj"
          break
        case 6: 
          monthString = "Jun"
          break
        case 7: 
          monthString = "Jul"
          break 
        case 8: 
          monthString = "Avgust"
          break
        case 9: 
          monthString = "Septembar"
          break
        case 10: 
          monthString = "Oktobar"
          break
        case 11: 
          monthString = "Novembar"
          break
        default:
          monthString = "Decembar"
          break
      }

      return day + ". " + monthString + " " + year + "." + "  " + hoursString + ":" + minutesString + "h"
    },
    setMapMarkers() {
      const markerPositions = [];
      this.offer.edit.tasks.forEach((newTask, ind) => {
        const oldTaskInd = this.oldTasklist.findIndex(task => task.id == newTask.task)
        const oldTask = this.oldTasklist[oldTaskInd]

        const oldPosition = {
          showEditsOnMap: (ind==0) ? true : false,
          pos: {
            lat: oldTask.address.latitude,
            lng: oldTask.address.longitude
          },
          lab: String(oldTaskInd + 1),
          info: oldTask.address.name
        }

        const newPosition = {
          pos: {
            lat: newTask.address.latitude,
            lng: newTask.address.longitude
          },
          lab: String(oldTaskInd + 1),
          info: newTask.address.name,
          icon: true
        }
        markerPositions.push(oldPosition)
        markerPositions.push(newPosition)
      })
      this.$store.dispatch('setMarkerPositions', markerPositions)
    },
    toggleOfferBox() {
      if(!this.opened) {
        if(!this.$store.getters['getOpenedOffersOrEdits'][this.myIndex]) {
          const newArray = new Array(this.$store.getters['getOpenedOffersOrEdits'].length).fill(false)
          newArray[this.myIndex] = true
          this.$store.dispatch('fillOpenedOffersOrEdits', newArray)
          this.setMapMarkers()
        }
        else {
          this.opened = true
        }
      }
      else
        this.opened = false
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
    align-items: center;
  }

  .offer {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    border-top: 1px dashed lightgray;
  }

  .offer-no-edits {
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
    margin-top: 10px;
    align-self: flex-end;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:60px;
    width:60px;
    object-fit:cover;
    cursor:pointer;
  }

  .image {
    height:60px;
    width:60px;
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

  .edit {
    margin-top: 10px;
  }

  .edit-title {
    font-size: 22px;
    font-weight: 600;
  }

  .edit-details {
    padding: 5px;
    border: 1px solid black;
    border-radius: 5px;
    font-weight: 600;
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

  .visible {
    visibility: visible;
    margin-top: 20px;
  }

  .invisible {
    visibility: hidden;
    height:0px;
  }

  .toggle-image {
    margin-left: 5px;
    padding: 2px;
    border-radius: 2px;
  }

  .toggle-image:hover {
    cursor: pointer;
    height: 23px;
    width:23px;
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

    .offer-no-edits {
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

  .green-color {
    color: rgb(2, 97, 2);
  }

  .red-color {
    color: rgb(235, 16, 16);
  }

  .left-padding {
    padding-left: 5px;
    margin-top: 10px;
  }

</style>