<template>
  <div 
    class = "box-wrapper" 
    :class="color"
  >
    <div class="request-top">
      <div class="request-btns">
        <b-button v-if="(request.status > 1)" class=" req-btn" variant="danger" @click="showModalReport = true">
          <div>
            <img src="../assets/report.png" class="btn-image">
            <span v-text="isSerbian ? 'Prijavi' : 'Report'"></span>
          </div>
        </b-button>
        <b-button v-if="(request.status > 1) && canRate" class=" req-btn" variant="success" @click="showModalRate = true">
          <div>
            <img src="../assets/rate.png" class="btn-image">
            <span v-text="isSerbian ? 'Oceni' : 'Rate'"></span>
          </div>
        </b-button>
      </div>
      <div class = "request-name">
          {{request.name}}
      </div>
    </div>
    <div class="request-bottom">
      <div class = "bottom-left">
        <div v-if="!isSerbian" class = "request-date"> {{request.date | showTime}} </div>
        <div v-else class = "request-date">{{dateAndTime}}</div>
        <div v-if="isSerbian" class = "tagovi">
          <div v-for="tag in tags" :key="tag.id" class = "request-tag">{{tag.service_type_sr}}</div>
        </div>
        <div v-else class = "tagovi">
          <div v-for="tag in tags" :key="tag.id" class = "request-tag">{{tag.service_type_en}}</div>
        </div>
      </div>
      <div class="status-div">
        <img v-if="request.status == 2" src = "../assets/finished.svg">
        <img v-if="request.status == 3" src = "../assets/failed.svg">
        <span class = "request-status"> {{status}} </span>
      </div>
    </div>
    <ModalReportUser 
      v-if="showModalReport" @close="showModalReport = false" 
      :userToReport="user" @setMessages="setMessagesReport" :request="request"
    />
    <ModalRateUser 
      v-if="showModalRate" @close="showModalRate = false" 
      :userToRate="user" @setMessages="setMessagesRating" :request="request"
    />
    <ModalSuccess v-if="success" :textS="textMessageS" :textE="textMessageE" @close="closeModalSuccess"/>
  </div>
</template>

<script>
import ModalReportUser from "@/components/ModalReportUser"
import ModalSuccess from "@/components/ModalSuccess"
import ModalRateUser from "@/components/ModalRateUser"


export default {
  components: {
    ModalReportUser,
    ModalSuccess,
    ModalRateUser
  },
  props: {
    request: {
      type: Object, 
      required: true
    },
    user: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      showModalAreYouSure: false,
      showModalReport: false,
      showModalRate: false,
      textMessageS: "",
      textMessageE: ""
    }
  },
  computed: {
    color()
    {
      if (this.request.status == 2)
          return "zeleno"
      else 
          return "crveno"
    },
    dateAndTime() {
      var date = new Date(this.request.time)
      
      var day = date.getUTCDate()
      var month = date.getUTCMonth() + 1
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
    status() {
      let returnValue = ""
      switch(this.request.status)
      {
        case 0: 
          if(this.isSerbian)
              returnValue = "na čekanju"
          else
              returnValue = "pending"
          break
        case 1: 
          if(this.isSerbian)
              returnValue = "u izvršenju"
          else
              returnValue = "running"
          break
        case 2: 
          if(this.isSerbian)
              returnValue = "završen"
          else
              returnValue = "finished"
          break
        case 3: 
          if(this.isSerbian)
              returnValue = "otkazan"
          else
              returnValue = "failed"
      }
      return returnValue
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    tags() {
      let returnValue = []
      var ids = []
      this.request.tasklist.forEach(task => 
      {
        var contains = false
        ids.forEach(element =>
        {
          if(element == task.service_type.id)
              contains = true
        })

        if(!contains)
        {
          ids.push(task.service_type.id)
          returnValue.push(task.service_type)
        }
      })
      return returnValue
    },
    success() {
      return this.$store.state.success
    },
    canRate() {
      if(!this.request.created_by || this.request.created_by.id == this.$store.state.authUser.id)
        return !this.request.rated_working_with
      else 
        return !this.request.rated_created_by
    }
  },
  methods: {
    rateUser() {
      console.log("rate")
    },
    dismissRate() {
      console.log("dismiss")
    },
    closeModalSuccess() {
      this.$store.state.success = false
    },
    setMessagesReport() {
      this.textMessageS = "Uspešno prijavljen problem sa korisnikom."
      this.textMessageE = "User successfully reported."
    },
    setMessagesRating() {
      this.textMessageS = "Uspešno ocenjen korisnik."
      this.textMessageE = "User successfully rated."
    }
  }
}
</script>

<style scoped>
  .box-wrapper {
    border: 1px solid rgb(43, 41, 41);
    border-radius: 15px;
    min-height: 100px;
    padding:5px;
    display:flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    max-width: 100%;
    margin: 40px 15% 40px 15%;
  }

  .request-top {
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
  }

  .request-btns {
    display: flex;
    flex-direction:row;
    flex-grow:1;
    justify-content: flex-end;
  }

  .req-btn {
    margin: 5px 5px 5px 5px;
    font-size:14px;
    font-weight: bold;
    border:hidden;
    display: flex;
    align-items: center;
  }

  .request-name {
    font-weight: bold;
    font-size: 25px;
    color:rgb(11, 97, 126);
    text-align: left;
    flex-grow: 1;
    margin-left: 10px;
    margin-top:2px;
  }

  .request-bottom {
    display: flex;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;
    width: 100%;
    margin-top: 30px;
  }

  .status-div {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-items: flex-end;
    margin-right: 10px;
    font-size: 18px;
  }

  .request-status {
    margin-left:7px;
  }

  .bottom-left {
    flex-grow: 1;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
  }

  .request-date {
    padding: 10px;
  }

  .tagovi {
    display: flex;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;
  }

  .request-tag {
    border-radius: 5px;
    padding-left:7px;
    padding-right:7px;
    padding-top:5px;
    margin:5px;
    background-color: rgb(15, 170, 221);
    color: white;
    font-weight: bold;
  }

  .btn-image {
    height: 18px;
    width: 18px;
    margin-right: 5px;
    margin-bottom: 3px;
  }

  @media only screen and (max-width:900px) {
    .box-wrapper {
      margin: 40px 10% 40px 10%;
    }
  }

  @media only screen and (max-width:600px) {
    .box-wrapper {
      margin: 30px 5% 30px 5%;
    }

    .request-top {
      flex-direction: column-reverse;
      align-items: flex-start;
    }

    .request-bottom {
      margin-top: 10px;
    }

    .bottom-left {
      flex-direction: column;
      align-items: flex-start;
    }

    .request-date {
      padding: 2px;
      padding-left: 10px;
    }

    .tagovi {
      margin-bottom: 5px;
      margin-left: 5px;
    }
  }
</style>