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
        </div>
      </b-card-title>
      <b-card-text class="request-note">
        <b-card-title>
            <span style="margin-right:5px;" v-text="isSerbian ? 'Napomena' : 'Note'"></span>
        </b-card-title>
        <b-card-text class="inner-text">
          Request.note!!!
        </b-card-text>
      </b-card-text>

      <div v-if="computedRequest.status != 'pending'" class="offer">
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
        <div class="payment-info inner-text">
          <span v-text="isSerbian ? 'Tip naplate: po satu' : 'Payment type: per hour'"> </span>
          <span v-text="isSerbian ? 'Cena: 200 din' : 'Price: 200din'"></span>
        </div>      
      </div>

      <b-card-text>
        <b-button class="button is-primary">
          <strong v-text="isSerbian ? 'Pogledajte mapu zadataka' : 'See task-list map'"></strong>
        </b-button>
      </b-card-text>

      <b-card-text v-for="(task, ind) in tasks" :key="ind" class="task-div">
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
          <b-card-text class="task-desc">
            {{task.description}}
          </b-card-text>
        </b-collapse>
      </b-card-text>
      
    </b-card>

  </div>
</template>

<script>
export default {

  props: {
    request: {
      required: false
    }
  },
  data() {
    return {
      taskOpened: [{value: false}, {value: false}, {value:false}],
      tasks: [
        {
          name: 'Task1',
          description: 'Ovo je test opis za zadatak Task1'
        },
        {
          name: 'Task2',
          description: 'Ovo je test opis za zadatak Task2'
        },
        {
          name: 'Task3',
          description: 'Ovo je test opis za zadatak Task3'
        }
      ],
      computedRequest: null
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
    }
  },
  created() {
    this.routeChanged()
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
    margin: 10px 0 10px 0;
    display:flex;
    align-items: center;
  }

  .clock {
    margin-right: 20px;
  }

  .title-pic {
    margin: 0px 10px 0px 0px;
  }

  .request-note {
    border: 1px solid black;
    padding: 15px;
    border-radius: 5px;
  }

  .inner-text {
    border: 1px solid lightgray;
    font-size:18px;
    padding: 10px;
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

</style>