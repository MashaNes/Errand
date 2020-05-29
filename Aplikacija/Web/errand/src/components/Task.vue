<template>
  <b-card-text class="task-div">
    <b-card-title 
      :class="opened ? 'open-task-bottom-border' : 'open-task-div'" 
      v-b-toggle="String(task.id)" 
      @click="opened = !opened"
    >
      <span class="task-title-tag">
        <span v-text="task.name" class="task-name"></span>
        <span class = "tagovi">
            <span v-if="isSerbian" class = "request-tag" v-b-popover.hover.bottom="task.service_type.description_sr">
                {{task.service_type.service_type_sr}}
            </span>
            <span v-else class = "request-tag" v-b-popover.hover.bottom="task.service_type.description_en">
                {{task.service_type.service_type_en}}
            </span>
        </span>
      </span>
      <img v-if="!opened" src="@/assets/down-chevron.svg" height="15" width="15"> 
      <img v-else src="@/assets/up-chevron.svg" height="15" width="15">

    </b-card-title>
    <b-collapse :id="String(task.id)">
      <div style="padding:15px">
        <div style="margin-bottom: 20px;">
          <span class="title-pic-span">
            <img src="@/assets/location.svg" height="25" width="25" class="title-pic" />
            <span v-text="isSerbian ? 'Adresa' : 'Address'" class="inner-text-title"></span>
          </span>
          <b-card-text :class="task.address ? 'inner-text' : 'no-info'">
            <span v-if="task.address">{{task.address.name}}</span>
            <span v-if="task.address" v-text="isSerbian ? 'Obiđena: ' + (task.address.arrived ? 'da' : 'ne') : 'Arrived: ' + (task.address.arrived ? 'yes' : 'no')"></span>
            <span v-else v-text="isSerbian ? 'Nije navedena adresa za ovaj zadatak.' : 'No address was specified for this task.'"></span>
          </b-card-text>
        </div>


        <span class="title-pic-span">
          <img src="@/assets/contract.svg" height="25" width="25" class="title-pic" />
          <span v-text="isSerbian ? 'Opis':'Description'" class="inner-text-title"></span>
        </span>
        <b-card-text :class="task.description && task.description != '' ? 'inner-text' : 'no-info'" style="margin-bottom: 20px;">
          <span v-if="task.description && task.description != ''">{{task.description}}</span>
          <span v-else v-text="isSerbian ? 'Ne postoji opis za ovaj zadatak.' : 'No description was provided for this task.'"></span>
        </b-card-text>

        <div v-if="task.checklist && task.checklist.length" style="margin-bottom: 20px;">
          <span class="title-pic-span">
            <img src="@/assets/to-do-list.svg" height="25" width="25" class="title-pic" />
            <span v-text="isSerbian ? 'Spisak' : 'Checklist'" class="inner-text-title"></span>
          </span>
          <b-card-text class="inner-text">
            <span v-for="(item, ind) in task.checklist" :key="item.id" class="chklist-item">{{ind+1}}. {{item.check_list}}</span>
          </b-card-text>
        </div>

        <div v-if="task.picture_required && myRequestStatus > 0">
          <span class="title-pic-span">
            <img src="@/assets/photos.svg" height="25" width="25" class="title-pic" />
            <span v-text="isSerbian ? 'Dostavljene slike' : 'Pictures taken'" class="inner-text-title"></span>
          </span>
          <b-card-text :class="pictures.length > 0 ? 'inner-text' : 'no-info'">
            <div  v-if="pictures.length > 0" class="images-wrapper" >
              <img 
                class="expandable-image" 
                :src="'data:;base64,' + picture.picture"
                @click="expandPicture(picture.picture)"
                v-for="picture in pictures" 
                :key="picture.id"
              />
            </div>
            <span v-else v-text="isSerbian ? 'Još uvek nije dostavljena nijedna slika' : 'No pictures have been taken so far'"></span>
          </b-card-text>
        </div>
        <div v-else-if="myRequestStatus == 0 && task.picture_required">
          <span 
            v-text="isSerbian ? 'Potrebno je dostaviti slike za ovaj zadatak' : 'Pictures are required for this task'"
            v-if="task.picture_required"
            class="inner-text-title"
          ></span>
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
    },
    myRequestStatus: {
      type: Number,
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
        return this.task.pictures
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
    word-break: break-word;
  }

  .open-task-div {
    font-size:25px;
    margin-bottom: 0px;
    color: rgb(71, 71, 71);
    background-color: rgb(255, 209, 109);
    padding:10px;
    border-radius:4px;
    border: 1px solid grey;
    flex-wrap: nowrap !important;
  }

  .open-task-div:hover {
    cursor: pointer;
    flex-wrap: nowrap !important;
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

  .title-pic {
    margin: 0px 10px 0px 0px;
  }

  .title-pic-span {
    height: fit-content;
    margin-bottom: 5px;
    display: flex;
    align-items: center;
    word-break: break-word;
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

  .no-info {
    font-weight: 600;
    color: rgb(175, 4, 4);
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

  .tagovi
  {
    display: flex;
    flex-direction: row;
    align-items:center;
  }

  .task-name {
    margin-right: 10px;
  }

  .request-tag
  {
    border-radius: 5px;
    padding-left:7px;
    padding-right:7px;
    padding-top:5px;
    padding-bottom:5px;
    margin:5px;
    background-color: rgb(15, 170, 221);
    color: white;
    font-weight: bold;
    font-size: 16px;
    margin-left: 0px;
  }

  .task-title-tag {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    margin-right: 5px;
  }

</style>