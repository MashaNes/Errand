<template>
  <Spinner v-if="!computedRequest || !filteredInfo" />
  <div class="wrapper" v-else>
    <b-card>
      <b-card-title class="main-title">
        <div class="title-start">{{computedRequest.name}}</div>
        <div class="title-end">
          <div class="clock pic-and-span">
            <img src="@/assets/clock.svg" height="25" width="25" class="title-pic">
            <span>{{computedRequest.time | showTime}}</span>
          </div>
          <div class="pic-and-span">
            <img v-if="computedRequest.status == 0" src="@/assets/running.svg" height="25" width="25" class="title-pic">
            <img v-if="computedRequest.status == 1" src="@/assets/pending.svg" height="25" width="25" class="title-pic">
            <img v-if="computedRequest.status == 2" src="@/assets/finished.svg" height="25" width="25" class="title-pic">
            <img v-if="computedRequest.status == 3" src="@/assets/failed.svg" height="25" width="25" class="title-pic">
            <span>{{status}}</span>
          </div>
          <b-button 
            variant="secondary"
            v-b-popover.hover.top="isSerbian ? 'Imate ' + filteredInfo.offers.length + ' ponuda' : 'You have ' + filteredInfo.offers.length + ' offers'"
            v-if="computedRequest.status == 0 && !isRunner && showView == 'Details'"
            :disabled="filteredInfo.offers.length == 0"
            @click="showView = 'Offers'"
          >
            <strong 
              class="notification-span" 
              v-text="isSerbian ? 'Ponude' : 'Offers'"
            ></strong>
            <b-badge variant="danger" class="notification-badge" >{{filteredInfo.offers.length}}</b-badge>
          </b-button>
          <b-button 
            variant="secondary"
            v-b-popover.hover.top="isSerbian ? 'Imate ' + filteredInfo.edits.length + ' zahteva za izmenama' : 'You have ' + filteredInfo.edits.length + ' edit requests'"
            v-if="computedRequest.status == 1 && !isRunner && showView == 'Edits'"
            :disabled="filteredInfo.edits.length == 0"
            @click="showView = 'Edits'"
          >
            <strong 
              class="notification-span" 
              v-text="isSerbian ? 'Zahtevi za izmenama' : 'Edit requests'"
            ></strong>
            <b-badge variant="danger" class="notification-badge" >{{filteredInfo.edits.length}}</b-badge>
          </b-button>
          <b-button 
            variant="secondary"
            v-if="showView != 'Details'"
            @click="showView = 'Details'"
          >
            <strong 
              class="notification-span" 
              v-text="isSerbian ? 'Detalji zahteva' : 'Request details'"
            ></strong>
          </b-button>
        </div>
      </b-card-title>

      <div style="display: flex; flex-direction: column" v-if="computedRequest.status == 0 && !isRunner && showView == 'Offers'" >
        <OfferBox 
          v-for="offer in filteredInfo.offers" :key="offer.id" :offer="offer" 
          @acceptOffer="acceptOffer" @rejectOffer="rejectOffer"
        />
      </div>


      <div v-if="showView == 'Details'">
        <b-card-text class="request-note">
          <b-card-title>
              <span style="margin-right:5px;" v-text="isSerbian ? 'Napomena' : 'Note'"></span>
          </b-card-title>
          <b-card-text class="inner-text" v-if="computedRequest.note">
            {{computedRequest.note}}
          </b-card-text>
          <b-card-text 
            class="inner-text" 
            v-else 
            v-text="isSerbian ? 'Nema napomena za ovaj zahtev' : 'There are no notes about this request'"
          >
          </b-card-text>
        </b-card-text>

        <div v-if="computedRequest.status != 0 && otherUser != null" class="offer">
          <b-card-title>
            <div class="offer-title">
              <span 
                v-text="(isSerbian ? 'Prihvaćena ponuda od ' : 'Accepted offer from ') + fullUserName"
                style="margin-right: 5px"
                v-if="!isRunner"
              ></span>
              <span 
                v-text="(isSerbian ? 'Zahtev kreirao/la ' : 'Request created by ') + fullUserName"
                style="margin-right: 5px"
                v-if="isRunner"
              ></span>
              <div class="media-center">
                <p class="image">
                  <img class="rounded-image" :src="otherUser.picture ? 'data:;base64,' + otherUser.picture : require('../assets/no-picture.png')">
                </p>
              </div>
            </div>
          </b-card-title>
          <div class="inner-text" v-if="otherUser != null && !isRunner">
            <span v-text="(isSerbian ? 'Tip naplate: ' : 'Payment type: ') + paymentType"> </span>
            <span v-text="(isSerbian ? 'Cena: ' : 'Price: ') + filteredInfo.acceptedOffer.payment_ammount + ' din'"></span>
          </div>
        </div>

        <b-card-text class="request-note">
          <b-card-title v-text="isSerbian ? 'Finalno odredište' : 'Final destination'"></b-card-title>
          <div class="inner-text" v-if="computedRequest.destination">
            <span>{{computedRequest.destination.name}}</span>
          </div>
          <div v-else class="inner-text" v-text="isSerbian ? 'Finalno odredište nije navedeno' : 'Final destination was not sepcified'"></div>
        
          <div v-if="computedRequest.picture_required && status > 0" style="margin-top: 10px">
            <span v-text="isSerbian ? 'Dostavljene slike' : 'Pictures taken'" class="inner-text-title" v-if="pictures.length > 0"></span>
            <span 
              v-else 
              v-text="isSerbian ? 'Još uvek nije dostavljena nijedna slika' : 'No pictures have been taken so far'" 
              class="inner-text-title"
              style="margin-top: 10px;"
            ></span>
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

          <ModalPicture :picture="clickedPicture" v-if="pictureExpanded" @shrinkPicture="pictureExpanded = false" />
        </b-card-text>



        <b-card-text v-if="hasAddresses">
          <b-button @click="toggleMap">
            <span v-if="!isMapOpened" v-text="isSerbian ? 'Prikaži adrese na mapi' : 'Show addresses on map'"></span>
            <span v-if="isMapOpened" v-text="isSerbian ? 'Zatvori mapu' : 'Close map'"></span>
          </b-button>
        </b-card-text>

        <b-card-text style="margin-top: 20px;" v-if="hasAddresses" :class="isMapOpened ? 'visible' : 'invisible'">
          <Map />
        </b-card-text>

        <Task 
          v-for="task in computedRequest.tasklist" 
          :key="task.id" 
          :task="task" 
          :myRequestStatus="computedRequest.status" 
        />
        
        <div class="button-div">
          <button type="button" class="btn btn-info" @click="kreirajZahtev">
            <span v-if="isSerbian"> Kreiraj novi zahtev na osnovu ovog </span>
            <span v-else> Create a new request based on this one </span>
          </button>
        </div>
      </div>
    </b-card>
  </div>
</template>

<script>
import Map from "@/components/Map"
import Task from "@/components/Task"
import Spinner from "@/components/Spinner"
import OfferBox from "@/components/OfferBox"

export default {
  components: {
    Map,
    Task,
    Spinner,
    OfferBox
  },
  props: {
    request: {
      required: false
    }
  },
  data() {
    return {
      computedRequest: null,
      isMapOpened: false,
      hasAddresses: false,
      clickedPicture: null,
      pictureExpanded: false,
      showView: "Details"
    }
  },
  computed: {
    filteredInfo() {
      return this.$store.state.requestFilteredInfo
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    isTaskOpened(ind) {
      return this.taskOpened[ind]
    },
    fullUserName() {
      return this.otherUser.first_name + " " + this.otherUser.last_name
    },
    otherUser() {
      if(this.computedRequest.created_by && this.computedRequest.created_by.id == this.$store.state.authUser.id)
        return this.computedRequest.working_with
      else
        return this.computedRequest.created_by
    },
    status() {
      let returnValue = ""
      switch(this.computedRequest.status)
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
    pictures() {
      if(this.computedRequest.picture_required)
        return this.$store.state.testPictures //kad bude moguce dodati sliku, zameniti sa "return this.computedRequest.pictures"
      else return []
    },
    isRunner() {
      if(!this.computedRequest.created_by || this.computedRequest.created_by.id == this.$store.state.authUser.id)
        return false
      else return true
    },
    paymentType() {
      let returnValue = ""
      if(this.filteredInfo.acceptedOffer) {
        switch(this.filteredInfo.acceptedOffer.payment_type) {
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
      }
      return returnValue
    }
  },
  methods: {
    routeChanged() {
      //izmeniti da se ode obavi fetch za request sa id-jem iz rute (ukoliko nije prosledjen prop)
      this.$store.state.requestFilteredInfo = null
      const vm = this
      function callback() {
        if(vm.$store.state.specificRequest != null)
        {
          vm.computedRequest = vm.$store.state.specificRequest
          if(vm.$store.state.specificRequest == -1)
            vm.$router.push({name: "PageRequests"})
          else
            vm.setMapMarkers()
        }
        else
          setTimeout(callback, 200)
      }

      if(!this.request) {
        const routeId = this.$route.params.id
        this.$store.dispatch('getRequestById', routeId)
        callback()
      }
      else {
        this.computedRequest = this.request
        let filters = {
          request: this.computedRequest.id,
          "offers" : false,
          "edits" : false, 
          "accepted_offer" : false,
          "rating_created_by" : false,
          "rating_working_with" : false,
          "tasklist" : true,
          "destination" : true,
          "pictures" : true
        }

        const status = this.computedRequest.status

        if(!this.isRunner) {
          if(status == 0) {
            filters["offers"] = true
          }
          else if(status == 1) {
            filters["edits"] = true
          }
          if(status > 0) {
            filters["accepted_offer"] = true
          }
        }
        if(status > 2) {
          if(this.request.rated_created_by)
            filters["rating_created_by"] = true
          if(this.request.rated_working_with)
            filters["rating_working_with"] = true
        }

        this.$store.state.requestFilteredInfo = null
        this.$store.dispatch("fillFilteredRequestInfo", {filters, requestId: filters.request})
        
        this.setMapMarkers()
      }
    },
    setMapMarkers() {
      const markerPositions = [];
      if(this.computedRequest.destination) {
        this.hasAddresses = true
        const newPosition = {
          pos: {
            lat: this.computedRequest.destination.latitude,
            lng: this.computedRequest.destination.longitude
          },
          lab: "F",
          info: this.computedRequest.destination.name
        }
        markerPositions.push(newPosition)
      }

      if(this.computedRequest.tasklist)
      {
        this.computedRequest.tasklist.forEach((task, ind) => {
          if(task.address)
          {
            this.hasAddresses = true
            const newPosition = {
              pos: {
                lat: task.address.latitude,
                lng: task.address.longitude
              },
              lab: String(ind + 1),
              info: task.address.name
            }
            markerPositions.push(newPosition)
          }
        })
        this.$store.dispatch('setMarkerPositions', markerPositions)
      }
    },
    toggleMap() {
      this.isMapOpened = !this.isMapOpened
    },
    kreirajZahtev()
    {
      this.$router.push({ name: 'PageNewRequest', params: {requestProp: this.request}})
    },
    expandPicture(picture) {
      this.clickedPicture = picture
      this.pictureExpanded = true
    },
    acceptOffer(offer) {
      console.log('accepted')
      console.log(offer)
      this.showView = 'Details'
      this.filteredInfo.acceptedOffer = offer
      this.computedRequest.working_with = offer.created_by
      this.computedRequest.status = 1
      const index = this.$store.state.createdAuthRequests.results.findIndex(req => req.id == this.computedRequest.id)
      this.$store.state.createdAuthRequests.results[index].status = 1
      this.$store.dispatch('acceptOffer', {offerId: offer.id, requestId: this.computedRequest.id})
    },
    rejectOffer(offer) {
      console.log('reject')
      console.log(offer)
      const index = this.filteredInfo.offers.findIndex(off => off.id == offer.id)
      this.filteredInfo.offers.splice(index, 1)
      this.$store.dispatch('rejectOffer', offer.id)
    }
  },
  created() {
    this.$store.dispatch('fillTestPictures')
    this.routeChanged()
    //izbaciti kad bude moguce dodavanje slike u request i u taskove
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
    margin: 10px 40px 10px 0;
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
    position: relative;
    top: -15px;
    left: 20px;
  }

  .request-note {
    border: 1px solid black;
    padding: 15px;
    border-radius: 5px;
    margin-bottom:20px;
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
    background-color:  #6c757d !important;
    border-color: #6c757d !important;
  }

  .btn {
    display: flex;
  }

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

  .task-desc {
    margin-top: 20px;
  }

  .chklist-item {
    margin-bottom: 10px;
    font-style: italic;
    font-family: cursive;
    width: fit-content;
  }

  .visible {
    visibility: visible;
  }

  .invisible {
    visibility: hidden;
    height:0px;
  }

  .button-div
  {
    width:100%;
    display:flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
    margin-top:20px;
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