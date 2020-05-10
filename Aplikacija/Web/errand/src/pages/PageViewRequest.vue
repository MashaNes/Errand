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
        <b-card-title style="font-size:25px;">
            <span style="margin-right:5px;" v-text="isSerbian ? 'Napomena' : 'Note'"></span>
        </b-card-title>
        <!-- <b-collapse id="collapse"> -->
          <b-card-text>
            Request.note!!!
          </b-card-text>
        <!-- </b-collapse> -->
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

  .main-title {
    border-top: lightgray solid 1px;
    border-bottom: lightgray solid 1px;
    padding-top: 10px;
    padding-bottom: 10px;
    margin-bottom: 20px;
    margin-top: 20px;
    overflow-wrap: break-word;
  }

  .card-title {
    font-size: 40px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
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
    border: 1px solid lightgray;
    padding: 10px;
    border-radius: 5px;
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
    border-radius:5px;
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
    border-radius:5px;
  }

  .open-task-bottom-border:hover {
    cursor: pointer;
  }

  .task-desc {
    margin-top: 20px;
  }

</style>