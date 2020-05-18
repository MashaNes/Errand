<template>
  <b-card-text class="task-div">
    <b-card-title 
      :class="opened ? 'open-task-bottom-border' : 'open-task-div'" 
      v-b-toggle="String(task.id)" 
      @click="opened = !opened"
    >
      <span style="margin-right:5px; max-width:90%;" v-text=task.name ></span>
      <img v-if="!opened" src="@/assets/down-chevron.svg" height="15" width="15">
      <img v-else src="@/assets/up-chevron.svg" height="15" width="15">

    </b-card-title>
    <b-collapse :id="String(task.id)">
      <div style="padding:15px">
        <div v-if="task.address" style="margin-bottom: 20px;">
          <span v-text="isSerbian ? 'Adresa' : 'Address'" class="inner-text-title"></span>
          <b-card-text class="inner-text">
            <span>{{task.address.name}}</span>
            <span v-text="isSerbian ? 'Obiđena: ' + (task.address.arrived ? 'da' : 'ne') : 'Arrived: ' + (task.address.arrived ? 'yes' : 'no')"></span>
          </b-card-text>
        </div>

        <span v-text="isSerbian ? 'Opis':'Description'" class="inner-text-title"></span>
        <b-card-text class="inner-text" style="margin-bottom: 20px;">
          {{task.description}}
        </b-card-text>

        <div v-if="task.checklist && task.checklist.length" style="margin-bottom: 20px;">
          <span v-text="isSerbian ? 'Spisak' : 'Checklist'" class="inner-text-title"></span>
          <b-card-text class="inner-text">
            <span v-for="(item, ind) in task.checklist" :key="item.id" class="chklist-item">{{ind+1}}. {{item.check_list}}</span>
          </b-card-text>
        </div>
      </div>
    </b-collapse>
  </b-card-text>
</template>

<script>
export default {
  props: {
    task: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      opened: false
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    }
  }
}
</script>

<style scoped>
  .task-div {
    margin-top: 20px;
    
    border:1px solid black;
    border-radius:5px;
  }

  .open-task-div {
    font-size:25px;
    margin-bottom: 0px;
    color: rgb(71, 71, 71);
    background-color: rgb(255, 209, 109);
    padding:10px;
    border-radius:4px;
    border: 1px solid grey;
    flex-wrap: nowrap;
  }

  .open-task-div:hover {
    cursor: pointer;
    flex-wrap: nowrap;
  }

  .open-task-bottom-border {
    border-bottom: 1px solid black;
    font-size:25px;
    margin-bottom: 0px;
    color: rgb(71, 71, 71);
    background-color: rgb(255, 209, 109);
    padding:10px;
    border-radius:4px;
    border:1px solid grey;
  }

  .open-task-bottom-border:hover {
    cursor: pointer;
  }

  .chklist-item {
    margin-bottom: 10px;
    font-style: italic;
    font-family: cursive;
    width: fit-content;
  }

  .inner-text-title {
    font-size: 18px;
    font-weight: 600;
  }

  .inner-text {
    border: 1px solid rgb(139, 136, 136);
    font-size:16px;
    padding: 10px;
    display: flex;
    flex-direction: column;
    background-color: rgb(250, 210, 125);
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
</style>