<template>
  <div class="wrapper">
    <b-card>
      <b-card-title class="main-title">
        <div class="title-start">{{computedRequest.name}}</div>
        <div class="title-end">
          <div class="clock pic-and-span">
            <img src="@/assets/clock.svg" height="25" width="25" class="title-pic">
            <span>21:03</span>
          </div>

          <div class="pic-and-span">
            <img v-if="computedRequest.status == 'running'" src="@/assets/running.svg" height="25" width="25" class="title-pic">
            <img v-if="computedRequest.status == 'pending'" src="@/assets/pending.svg" height="25" width="25" class="title-pic">
            <img v-if="computedRequest.status == 'finished'" src="@/assets/finished.svg" height="25" width="25" class="title-pic">
            <img v-if="computedRequest.status == 'failed'" src="@/assets/failed.svg" height="25" width="25" class="title-pic">
            <span>{{computedRequest.status}}</span>
          </div>
          <b-button 
            variant="secondary"
            v-b-popover.hover.top="isSerbian ? 'Imate 10 ponuda' : 'You have 10 offers'"
            v-if="computedRequest.status == 'pending' && computedRequest.created_by == null"
          >
            <strong 
              class="notification-span" 
              v-text="isSerbian ? 'Ponude' : 'Offers'"
            ></strong>
          </b-button>
          <b-badge variant="danger" class="notification-badge" v-if="computedRequest.status == 'pending' && computedRequest.created_by == null">1</b-badge>
          
          <b-button 
            variant="secondary"
            v-b-popover.hover.top="isSerbian ? 'Imate zahtev za izmenom' : 'You have an edit request'"
            v-if="computedRequest.status == 'running' && computedRequest.created_by == null"
          >
            <strong 
              class="notification-span" 
              v-text="isSerbian ? 'Zahtev za izmenom' : 'Edit request'"
            ></strong>
          </b-button>
          <b-badge variant="danger" class="notification-badge" v-if="computedRequest.status == 'running' && computedRequest.created_by == null">!</b-badge>
        </div>
      </b-card-title>
      <b-card-text class="request-note">
        <b-card-title>
            <span style="margin-right:5px;" v-text="isSerbian ? 'Napomena' : 'Note'"></span>
        </b-card-title>
        <b-card-text class="inner-text">
          {{computedRequest.note}}
        </b-card-text>
      </b-card-text>

      <div v-if="computedRequest.status != 'pending' && computedRequest.created_by == null" class="offer">
        <b-card-title>
          <div class="offer-title">
            <span 
              v-text="(isSerbian ? 'Prihvaćena ponuda od ' : 'Accepted offer from ') + fullUserName"
              style="margin-right: 5px"
            ></span>
            <div class="media-center">
              <p class="image">
                <img class="rounded-image" :src="computedRequest.user.picture">
              </p>
            </div>
          </div>
        </b-card-title>
        <div class="inner-text">
          <span v-text="isSerbian ? 'Tip naplate: po satu' : 'Payment type: per hour'"> </span>
          <span v-text="isSerbian ? 'Cena: 200 din' : 'Price: 200din'"></span>
        </div>      
      </div>

      <b-card-text>
        <b-button class="button is-primary" @click="openMap">
          <strong v-if="!isMapOpened" v-text="isSerbian ? 'Pogledajte mapu zadataka' : 'See task-list map'"></strong>
          <strong v-if="isMapOpened" v-text="isSerbian ? 'Zatvori mapu zadataka' : 'Close task-list map'"></strong>
        </b-button>
      </b-card-text>

      <b-card-text style="margin-top: 20px;" :class="isMapOpened ? 'visible' : 'invisible'">
        <Map />
      </b-card-text>

      <b-card-text v-for="(task, ind) in computedRequest.tasklist" :key="task.id" class="task-div">
        <b-card-title 
          :class="taskOpened[ind].value ? 'open-task-bottom-border' : 'open-task-div'" 
          v-b-toggle="'collapse' + ind" 
          @click="taskOpened[ind].value = !taskOpened[ind].value"
        >
          <span style="margin-right:5px;" v-text=task.name ></span>
          <img v-if="!taskOpened[ind].value" src="@/assets/down-chevron.svg" height="15" width="15">
          <img v-else src="@/assets/up-chevron.svg" height="15" width="15">

        </b-card-title>
        <b-collapse :id="'collapse' + ind">
          <div style="padding:15px">
            <div v-if="task.address">
              <span v-text="isSerbian ? 'Adresa' : 'Address'"></span>
              <b-card-text class="inner-text">
                <span>{{task.address.name}}</span>
                <span v-text="isSerbian ? 'Obiđena: ' + (task.address.arrived ? 'da' : 'ne') : 'Arrived: ' + (task.address.arrived ? 'yes' : 'no')"></span>
              </b-card-text>
              
            </div>
            <span v-text="isSerbian ? 'Opis':'Description'"></span>
            <b-card-text class="inner-text">
              {{task.description}}
            </b-card-text>
          </div>
        </b-collapse>
      </b-card-text>
      
    </b-card>

  </div>
</template>

<script>
import Map from "@/components/Map"

export default {
  components: {
    Map
  },
  props: {
    request: {
      required: false
    }
  },
  data() {
    return {
      taskOpened: [],
      tasks: [
        {
          name: 'Task1',
          description: 'Ovo je test opis za zadatak Task1',
          address: {
            latitude: 44.788696,
            longitude: 21.818723
          }
        },
        {
          name: 'Task2',
          description: 'Ovo je test opis za zadatak Task2',
          address: {
            latitude: 49.185616,
            longitude: 20.412323
          }
        },
        {
          name: 'Task3',
          description: 'Ovo je test opis za zadatak Task3',
          address: {
            latitude: 43.455496,
            longitude: 22.122123
          }
        }
      ],
      computedRequest: null,
      isMapOpened: false
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isTaskOpened(ind) {
      return this.taskOpened[ind]
    },
    fullUserName() {
      return this.computedRequest.user.first_name + " " + this.computedRequest.user.last_name
    }
  },
  methods: {
    routeChanged() {
      //izmeniti da se ode obavi fetch za request sa id-jem iz rute (ukoliko nije prosledjen prop)
      if(!this.request) {
        const routeId = this.$route.params.id
        if(!Object.values(this.$store.state.requests).length)
          this.$store.dispatch('fillRequests')
        this.computedRequest = this.$store.state.requests[routeId]
      }
      else this.computedRequest = this.request
      for(let i=0; i<this.computedRequest.tasklist.length; i++) {
        this.taskOpened.push({
          value: false
        })
      }
      const markerPositions = [];
      this.computedRequest.tasklist.forEach((task, ind) => {
        if(task.address)
        {
          const newPosition = {
            pos: {
              lat: task.address.latitude,
              lng: task.address.longitude
            },
            lab: String(ind + 1),
            info: task.description
          }
          markerPositions.push(newPosition)
        }
      })
      this.$store.dispatch('setMarkerPositions', markerPositions)
    },
    openMap() {
      this.isMapOpened = !this.isMapOpened
    }
  },
  created() {
    this.routeChanged()
    console.log('created')
  },
  watch: {
    $route() {
      this.changedRoute()
    }
  }
}
</script>

<style scoped>

  .wrapper {
    padding-top: 2%;
    display:flex;
    flex-direction: column;
    align-items: center;
  }

  .card {
    margin: 0px 10% 50px 10%;
    width:70%;
    align-self:center;
    padding-left:3%;
    padding-right:3%;
  } 

  .card-title {
    font-size: 25px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
  }

  .main-title {
    font-size: 40px;
    border-top: lightgray solid 1px;
    border-bottom: lightgray solid 1px;
    padding-top: 10px;
    padding-bottom: 10px;
    margin-bottom: 20px;
    margin-top: 20px;
    overflow-wrap: break-word;
  }

  .title-start {
    flex-grow:1;  
    width:100%;
    margin-bottom: 10px;
  }

  .title-end {
    font-size: 18px;
    color:grey;
    display:flex;
    align-items:center;
    flex-wrap: wrap;
    flex-grow:1;
    width:100%;
  }

  .pic-and-span {
    margin: 10px 20px 10px 0;
    display:flex;
    align-items: center;
  }

  /* .clock {
    margin-right: 20px;
  } */

  .title-pic {
    margin: 0px 10px 0px 0px;
  }

  .notification-span {
    margin-right: 5px;
    color:white;
    font-size: 20px;
  }

  .notification-badge {
    font-size: 85%;
    margin-left: -12px;
    margin-bottom: 35px;
  }

  .request-note {
    border: 1px solid black;
    padding: 15px;
    border-radius: 5px;
    margin-bottom:20px;
  }

  .inner-text {
    border: 1px solid lightgray;
    font-size:18px;
    padding: 10px;
    display: flex;
    flex-direction: column;
  }

  .offer {
    border: 1px solid black;
    border-radius: 5px;
    margin-top: 10px;
    margin-bottom: 20px;
    padding: 15px;
  }

  .offer-title {
    display: flex;
    flex-wrap: wrap;
    align-items:center;
  }

  .payment-info {
    display: flex;
    flex-direction: column;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:40px;
    width:40px;
    object-fit:cover;
    cursor:pointer;
    margin-right: 10px;
  }

  .button {
    font-size: 20px;
  }

  .task-div {
    margin-top: 20px;
    
    border:1px solid black;
    border-radius:5px;
  }

  .open-task-div {
    font-size:25px;
    margin-bottom: 0px;
    color: white;
    background-color: grey;
    padding:10px;
    border-radius:4px;
    border: 1px solid grey;
  }

  .open-task-div:hover {
    cursor: pointer;
  }

  .open-task-bottom-border {
    border-bottom: 1px solid black;
    font-size:25px;
    margin-bottom: 0px;
    color: white;
    background-color: grey;
    padding:10px;
    border-radius:4px;
    border:1px solid grey;
  }

  .open-task-bottom-border:hover {
    cursor: pointer;
  }

  .task-desc {
    margin-top: 20px;
  }

  .visible {
    visibility: visible;
  }

  .invisible {
    visibility: hidden;
    height:0px;
  }

</style>