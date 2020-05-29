<template>
    <b-card 
      no-body
      id="scroll-card"
    >
  
      <b-card-body>
        <div :class="opened ? 'edit-title' : 'edit-title-closed'" @click="toggleEditBox">
          <span v-if="opened" v-text="isSerbian ? 'Zatvori' : 'Close'"></span>
          <span v-else v-text="String(myIndex + 1) + '. ' + (isSerbian ? 'Prikaži detalje o izmenama' : 'Show details about edits')"></span>
          <img v-if="!opened" src="@/assets/down-chevron.svg" height="22" width="22" class="toggle-image">
          <img v-else src="@/assets/up-chevron.svg" height="22" width="22" class="toggle-image"> 
        </div>
        <div class="edit" v-if="opened">
          <div class="edit-details">
            <div v-if="edit.request_edit.time">
                <span v-text="isSerbian ? 'Datum i vreme' : 'Date and time'" style="font-size: 20px;"></span>
                <div class="left-padding">
                <div v-if="isSerbian" class="red-color"> Trenutni datum i vreme: {{oldDateAndTime | showTime}}</div>
                <div v-else class="red-color"> Current date and time: {{oldDateAndTime | showTime}}</div>
                <div v-if="isSerbian" class="green-color"> Novi datum i vreme: {{edit.request_edit.time | showTime}}</div>
                <div v-else class="green-color"> New date and time: {{edit.request_edit.time | showTime}}</div>
                </div>
            </div>
            <div v-if="edit.request_edit.tasks.length > 0" :style="edit.request_edit.time ? 'margin-top:15px;' : ''">
                <span v-text="isSerbian ? 'Nove adrese' : 'New addresses'" style="font-size: 20px;"></span>
                <div v-for="item in edit.request_edit.tasks" :key="item.task" class="left-padding">
                <div v-text='(isSerbian ? "Zadatak " : "Task ") + "\"" + getOldTask(item.task).name + "\":"'></div>
                <div class="left-padding" style="margin-top: 5px !important;">
                    <div v-text='(isSerbian ? "Trenutna adresa: " : "Current address: ") + getOldTask(item.task).address.name' class="red-color"></div>
                    <div v-text='(isSerbian ? "Nova adresa: " : "New address: ") + item.address.name' class="green-color"></div>
                </div>
                </div>
            </div>
            <b-button style="margin-top: 10px;" v-if="edit.request_edit.tasks.length > 0" @click="isMapOpened = !isMapOpened">
                <span v-if="!isMapOpened" v-text="isSerbian ? 'Prikaži izmene na mapi' : 'Show changes on map'"></span>
                <span v-if="isMapOpened" v-text="isSerbian ? 'Zatvori mapu' : 'Close map'"></span>
            </b-button>

            <b-card-text  v-if="edit.request_edit.tasks.length > 0" :class="isMapOpened ? 'visible' : 'invisible'">
                <Map />
            </b-card-text>

          </div>
          <div class="edit-buttons">
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
    edit: {
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
      return this.request.working_with.avg_rating < 2.5 ? 'danger' : 
              this.request.working_with.avg_rating < 4.5 ? 'warning' : 'success'
    },
    fullUserName() {
      return this.request.working_with.first_name + " " +this.request.working_with.last_name
    },
    tekstS() {
      if(this.accept) {
        return "Da li ste sigurni da želite da prihvatite ovaj zahtev za izmenama? Nakon prihvatanja, nećete moći da promenite odluku."
      }
      else 
        return "Da li ste sigurni da želite da odbijete ovaj zahtev za izmenama?"
    },
    tekstE() {
      if(this.accept) {
        return "Are you sure you want to accept this edit request? After accepting, you will not be able to change your decission."
      }
      else
        return "Are you sure you want to decline this edit request?"
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
    makeDecission() {
      if(this.accept)
        this.$emit('acceptEdit', this.edit)
      else
        this.$emit('rejectEdit', this.edit)
      this.showAreYouSure = false
    },
    dismissDecission() {
      this.showAreYouSure = false
    },
    getOldTask(taskId) {
      return this.oldTasklist.find(task => task.id == taskId)
    },
    setMapMarkers() {
      const markerPositions = [];
      this.edit.request_edit.tasks.forEach((newTask, ind) => {
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
    toggleEditBox() {
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

  .edit {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .edit-details {
    padding: 5px;
    border: 1px solid black;
    border-radius: 5px;
    font-weight: 600;
    margin: 10px 10px 0px 10px;
  }

  .edit-buttons {
    display:flex;
    align-items: flex-end;
    margin: 10px 10px 10px 0px;
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
    padding: 0px;
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
    border: 1px solid black;
    padding: 0px !important;
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

  .edit-title {
    padding: 5px 10px 5px 10px;
    font-size: 22px;
    font-weight: 600;
    border-bottom: 1px solid black;
    border-radius: 15px;
    width: 100%;
    background-color: rgb(255, 209, 109);
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .edit-title-closed {
    padding: 5px 10px 5px 10px;
    font-size: 22px;
    font-weight: 600;
    border-radius: 15px;
    width: 100%;
    background-color: rgb(255, 209, 109);
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .edit-title:hover {
    cursor: pointer;
  }

  .edit-title-closed:hover {
    cursor: pointer;
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
  
    .edit {
      flex-direction: column;
      width: 100%;
      padding-left: 5px;
      padding-right: 5px;
    }

    .edit-details {
      align-self: flex-start;
      margin-bottom: 10px;
    }

    .edit-buttons {
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