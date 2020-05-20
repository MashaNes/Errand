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

        <div v-if="task.picture_required">
          <span v-text="isSerbian ? 'Dostavljene slike' : 'Pictures taken'" class="inner-text-title" v-if="pictures.length > 0"></span>
          <span v-else v-text="isSerbian ? 'Još uvek nije dostavljena nijedna slika' : 'No pictures have been taken so far'"></span>
          <b-card-text class="inner-text" v-if="pictures.length > 0">
            <div class="images-wrapper" >
              <img 
                class="expandable-image" 
                :src="'data:;base64,' + pictures[ind]"
                @click="expandPicture(picture)"
                v-for="(picture, ind) in pictures" 
                :key="ind"
              />
            </div>
          </b-card-text>
        </div>
      </div>
    </b-collapse>
    <ModalPicture :picture="clickedPicture" v-if="pictureExpanded" @shrinkPicture="pictureExpanded = false" />
  </b-card-text>
</template>

<script>
import ModalPicture from "@/components/ModalPicture"

export default {
  components: {
    ModalPicture
  },
  props: {
    task: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      opened: false,
      clickedPicture: null,
      pictureExpanded: false
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    pictures() {
      if(this.task.picture_required)
        return this.$store.state.testPictures //kad bude moguce dodati sliku u task, zameniti sa "return this.task.pictures"
      else return []
    }
  },
  methods: {
    expandPicture(picture) {
      this.clickedPicture = picture
      this.pictureExpanded = true
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

  .images-wrapper {
    display: flex;
    flex-wrap: wrap;
  }

  .expandable-image {
    margin: 5px;
    width: 100px;
    height: 100px;
    border: 1px solid grey;
    border-radius: 5px;
  }

  .expandable-image:hover {
    cursor: pointer;
    border-color: black;
  }

</style>