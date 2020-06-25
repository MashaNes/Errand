<template>
  <Spinner v-if="!computedRequest || !this.$store.state.isRequestInfoLoaded" />
  <div class="wrapper" v-else>
    <b-card style="min-width: 240px;">
      <b-card-title class="main-title">
        <div class="title-content">
          <div class="basic-info">
            <div class="title-start">
              <span style="margin-right: 10px;">{{computedRequest.name}}</span>
              <div class="dropdown-and-important ">
                <img v-if="(finishedOtherUser || finishedThisUser) && computedRequest.status == 1" 
                    src="@/assets/exclamation.png" class="important-img"
                    v-b-popover.hover.bottom='popoverText'/>
                <!-- <img v-if="!isSerbian && finishedOtherUser && !isRunner && computedRequest.status == 1"
                    src="@/assets/exclamation.png" class="important-img"
                    v-b-popover.hover.bottom='popoverText'/>
                <img v-if="isSerbian && finishedThisUser && !isRunner && computedRequest.status == 1" 
                    src="@/assets/exclamation.png" class="important-img"
                    v-b-popover.hover.bottom='popoverText'/>
                <img v-if="!isSerbian && finishedThisUser && !isRunner && computedRequest.status == 1"
                    src="@/assets/exclamation.png" class="important-img"
                    v-b-popover.hover.bottom='popoverText'/> -->
                <b-dropdown id="dropdown-1" variant="info" right class="drop-down">
                  <template v-slot:button-content>
                    <span v-text="isSerbian ? 'Akcije' : 'Actions'"></span>
                    <b-badge 
                      v-if="computedRequest.status == 1 && !isRunner && showView == 'Details' && filteredEdits && filteredEdits.length > 0
                           && !finishedThisUser && !finishedOtherUser"
                      variant="danger" class="big-notification-badge" >{{editsBadge}}</b-badge>
                    <b-badge 
                      v-if="computedRequest.status == 0 && !isRunner && showView == 'Details' && filteredOffers && filteredOffers.length > 0
                           && !finishedThisUser && !finishedOtherUser"
                      variant="danger" class="big-notification-badge" >{{offersBadge}}</b-badge>
                  </template>
                  <b-dropdown-item 
                    v-if="computedRequest.status == 1 && !isRunner && !computedRequest.finished_created_by" 
                    @click="purpose = 'finish'; showModalAreYouSure = true;"
                    v-b-popover.hover.bottom='isSerbian ? "Kliknite da biste okončali zahtev i označili da je bio uspešan." 
                                                        : "Click to end the request and mark it as successful."'
                  >
                    <img src="@/assets/checkmark.png" class="slika-dugme check" />
                    <span v-text="isSerbian ? 'Uspešno okončaj zahtev' : 'Successfully end request'">
                  </span></b-dropdown-item>
                  <b-dropdown-item v-if="computedRequest.status > 1 && canRate" @click="showModalRate = true">
                    <img src="@/assets/rate.png" class="slika-dugme check" />
                    <span v-text="isSerbian ? 'Oceni korisnika' : 'Rate user'"></span>
                  </b-dropdown-item>
                  <b-dropdown-item 
                    v-if="computedRequest.status == 1 && !isRunner && !computedRequest.finished_created_by"
                    @click="purpose = 'cancel'; showModalAreYouSure = true;"
                    v-b-popover.hover.bottom='isSerbian ? "Kliknite da biste prekinuli zahtev i označili da je bio neuspešan." 
                                                        : "Click to end the request and mark it as successful."' 
                  >
                    <img src="@/assets/shutdown.svg" class="slika-dugme shutdown" />
                    <span v-text="isSerbian ? 'Prekini zahtev' : 'Cancel request'"></span>
                  </b-dropdown-item>
                  <b-dropdown-item v-if="computedRequest.status > 1"  @click="showModalReport = true">
                    <img src="@/assets/report.png" class="slika-dugme xmark" />
                    <span v-text="isSerbian ? 'Prijavi problem' : 'Report user'"></span>
                  </b-dropdown-item>
                  <b-dropdown-item 
                    v-if="computedRequest.status == 0"
                    @click="purpose = 'delete'; showModalAreYouSure = true;"
                  >
                    <img src="@/assets/xmark.png" class="slika-dugme xmark" />
                    <span v-text="isSerbian ? 'Obriši zahtev' : 'Delete request'"></span>
                  </b-dropdown-item>
                  <b-dropdown-item 
                    v-if="computedRequest.status == 0 && !isRunner && showView == 'Details'"
                    :disabled="!filteredOffers || filteredOffers.length == 0" @click="changeViewFromDetails('Offers')"
                    v-b-popover.hover.top="ponudeTekst"
                  >
                    <img src="@/assets/handshake.png" class="slika-dugme wrench" />
                    <b-badge variant="danger" class="notification-badge" >{{offersBadge}}</b-badge>
                    <span class="notification-span" v-text="isSerbian ? 'Vidi ponude' : 'See offers'"></span>
                  </b-dropdown-item>
                  <b-dropdown-item 
                    v-if="computedRequest.status == 1 && !isRunner && showView == 'Details' && !computedRequest.finished_created_by && !computedRequest.finished_working_with"
                    :disabled="!filteredEdits || filteredEdits.length == 0" @click="changeViewFromDetails('Edits')"
                    v-b-popover.hover.top="zahteviTekst" 
                  >
                    <img src="@/assets/wrench.svg" class="slika-dugme wrench" />
                    <b-badge variant="danger" class="notification-badge" >{{editsBadge}}</b-badge>
                    <span class="notification-span" v-text="isSerbian ? 'Vidi zahteve za izmenama' : 'See edit requests'"></span>
                  </b-dropdown-item>
                  

                  <b-dropdown-item v-if="isRunner && computedRequest.status == 1">
                    <img src="@/assets/info.svg" class="slika-dugme"/>
                    <span v-if="isSerbian" class="dropdown-runner"> Za pristup akcijama u ulozi izvršioca, skinite Android aplikaciju </span>
                    <span v-else class="dropdown-runner"> For accessing ruuner's actions, download the Android application </span>
                  </b-dropdown-item>

                  <b-dropdown-divider></b-dropdown-divider>
                  
                  <b-dropdown-item @click="goToPageRequests">
                    <img class="slika-dugme back" src="../assets/back.png">
                    <span v-text="isSerbian ? 'Nazad na pregled svih zahteva' : 'Back to all requests'"></span> 
                  </b-dropdown-item>
                </b-dropdown>
              </div>
            </div>
            <div class="clock pic-and-span">
              <img src="@/assets/clock.svg" class="title-pic">
              <span v-if="!isSerbian">{{computedRequest.time | showTime}}</span>
              <span v-else>{{dateAndTime}}</span>
            </div>
            <div class="pic-and-span">
              <img v-if="computedRequest.status == 0" src="@/assets/running.svg" class="title-pic">
              <img v-if="computedRequest.status == 1" src="@/assets/pending.svg" class="title-pic">
              <img v-if="computedRequest.status == 2" src="@/assets/finished.svg" class="title-pic">
              <img v-if="computedRequest.status == 3" src="@/assets/failed.svg" class="title-pic">
              <span>{{status}}</span>
            </div>
            <div class="note">
              <b-card-title class="side-info-title">
                  <span style="margin-right:5px; padding-top: 3px;" v-text="isSerbian ? 'Napomena: ' : 'Note: '"></span>
              </b-card-title>
              <b-form-textarea
                :class="[!computedRequest.note ? 'no-info-txt' : '', 'txt-area']"
                rows="3"
                id="textarea"
                no-resize
                :readonly="true"
                v-model="computedNote"
              >
              </b-form-textarea>
            </div>
            <b-button v-if="showView != 'Details'" @click="showDetails">
              <span v-text="isSerbian ? 'Detalji zahteva' : 'Request details'"></span>
            </b-button>
          </div>
        </div>
      </b-card-title>
      <div class="no-detail-view" v-if="computedRequest.status == 0 && !isRunner && showView == 'Offers'" >
        <b-card-title v-text="isSerbian ? 'Ponude' : 'Offers'" class="no-details-title"></b-card-title>
        <OfferBox 
          v-for="(offer, ind) in filteredOffers" :key="offer.id" :offer="offer" 
          :oldTasklist="(offer.edit && offer.edit.tasks.length > 0) ? computedRequest.tasklist : null"
          :oldDateAndTime="(offer.edit && offer.edit.time) ? computedRequest.time : null"
          :myIndex="ind" :request="computedRequest" @acceptOffer="acceptOffer" @rejectOffer="rejectOffer"
        />
      </div>
      <div class="no-detail-view" v-if="computedRequest.status == 1 && !isRunner && showView == 'Edits'">
        <div v-text="isSerbian ? 'Zahtevi za izmenama' : 'Edit requests'" class="no-details-title"></div>
        <EditBox
          v-for="(edit, ind) in filteredEdits" :key="edit.id" :edit="edit" 
          :oldTasklist="edit.request_edit.tasks.length > 0 ? computedRequest.tasklist : null"
          :oldDateAndTime="edit.request_edit.time ? computedRequest.time : null"
          :myIndex="ind" :request="computedRequest" @acceptEdit="acceptEdit" @rejectEdit="rejectEdit"
        />
      </div>
      <div v-if="showView == 'Details'" class="details">
        <div class="request-info">
          <div v-if="computedRequest.status != 0 && otherUser != null" class="other-user">
            <b-card-title class="side-info-title" :style="isRunner ? 'margin-bottom: 0px !important' : ''">
              <div class="offer-title">
                <span class="smaller-title-pic-span" style="flex-wrap: wrap;">
                  <div class="media-center">
                    <p class="image" @click="goToProfile">
                      <img class="rounded-image" :src="otherUser.picture ? 'data:;base64,' + otherUser.picture : require('../assets/no-picture.png')">
                    </p>
                  </div>
                  <span 
                    v-text="(isSerbian ? 'Prihvaćena ponuda od korisnika ' : 'Accepted offer from ') + fullUserName"
                    style="margin-right: 5px" v-if="!isRunner"
                  ></span>
                  <span 
                    v-text="(isSerbian ? 'Zahtev kreirao/la ' : 'Request created by ') + fullUserName"
                    style="margin-right: 5px" v-if="isRunner"
                  ></span>
                </span>
              </div>
            </b-card-title >
            <div class="inner-text" v-if="otherUser != null && !isRunner">
              <span v-text="(isSerbian ? 'Tip naplate: ' : 'Payment type: ') + paymentType"> </span>
              <span v-text="(isSerbian ? 'Cena: ' : 'Price: ') + filteredInfo.acceptedOffer.payment_ammount + ' din'"></span>
            </div>
          </div>
          <div v-if="computedRequest.status == 0 && computedRequest.direct_user != null" class="other-user">
            <b-card-title class="side-info-title" :style="isRunner ? 'margin-bottom: 0px !important' : ''">
              <div class="offer-title">
                <span class="smaller-title-pic-span" style="flex-wrap: wrap;">
                  <div class="media-center">
                    <p class="image" @click="goToProfile">
                      <img class="rounded-image" :src="computedRequest.direct_user.picture ? 'data:;base64,' + computedRequest.direct_user.picture : require('../assets/no-picture.png')">
                    </p>
                  </div>
                  <span 
                    v-text="(isSerbian ? 'Zahtev poslat direktno korisniku ' : 'Request sent directly to ') + fullDirectUserName"
                    style="margin-right: 5px"
                  ></span>
                </span>
              </div>
            </b-card-title >
            <div class="inner-text" v-if="computedRequest.broadcast">
              <span 
                v-text="(isSerbian ? 'Zahtev je dostupan i svim korisnicima koji ispunjavaju kriterijume navedene pri kreiranju ovog zahteva  ' 
                        : 'Request will also be visible to all the users that fit the parameters specified in the creation of this request')"
              ></span>
            </div>
          </div>
          <div class="destination">
            <b-card-title class="side-info-title">
              <span class="smaller-title-pic-span">
                <img src="@/assets/address.svg" height="25" width="25" class="title-pic" />
                <span v-text="isSerbian ? 'Finalno odredište' : 'Final destination'"></span>
              </span>
            </b-card-title>
            <div :class="computedRequest.destination ? 'inner-text' : 'no-info'">
              <span v-if="computedRequest.destination">{{computedRequest.destination.name}}</span>
              <span v-if="computedRequest.destination" v-text="isSerbian ? 'Obiđena: ' + (computedRequest.destination.arrived ? 'da' : 'ne') : 'Arrived: ' + (computedRequest.destination.arrived ? 'yes' : 'no')"></span>
              <span v-else v-text="isSerbian ? 'Finalno odredište nije navedeno' : 'Final destination was not sepcified'"></span>
            </div>

            <b-card-text  class="pictures-div" v-if="computedRequest.picture_required && computedRequest.status > 0">
              <div>
                <span class="smaller-title-pic-span">
                  <img src="@/assets/photos.svg" height="25" width="25" class="title-pic" />
                  <span v-text="isSerbian ? 'Slike sa odredišta' : 'Pictures from the destination'" class="inner-text-title"></span>
                </span>
                <b-card-text :class="pictures.length > 0 ? 'inner-text' : 'no-info'">
                  <div v-if="pictures.length > 0" class="images-wrapper" >
                    <img 
                      class="expandable-image" 
                      :src="picture.src"
                      @click="expandPicture(ind)"
                      v-for="(picture, ind) in pictures" 
                      :key="picture.id"
                    />

                    <LightBox :media="pictures" v-if="pictureExpanded" :startAt="clickedPicture" @onClosed="pictureExpanded = false"></LightBox>
                  
                  </div>
                  <span v-else v-text="isSerbian ? 'Nije dostavljena nijedna slika' : 'No pictures have been taken'"></span>
                </b-card-text>
              </div>
            </b-card-text>
            <div v-else-if="computedRequest.picture_required" class='pic-required'>
              <span v-text="isSerbian ? 'Potrebno je dostaviti slike sa odredišta' : 'Pictures from the destination are required'"></span>
            </div>
          </div>
        </div>
        <b-card-text v-if="hasAddresses">
          <b-button @click="toggleMap">
            <span v-if="!isMapOpened" v-text="isSerbian ? 'Prikaži adrese na mapi' : 'Show addresses on map'"></span>
            <span v-if="isMapOpened" v-text="isSerbian ? 'Zatvori mapu' : 'Close map'"></span>
          </b-button>
        </b-card-text> 
        <b-card-text v-if="hasAddresses" :class="isMapOpened ? 'visible' : 'invisible'">
          <Map />
        </b-card-text>
        <div class="tasks">
          <span class='task-title'>
            <span v-text="isSerbian ? 'Zadaci' : 'Tasks'"></span>
          </span>
          <Task 
            v-for="task in filteredInfo.tasklist" 
            :key="task.id" 
            :task="task" 
            :myRequestStatus="computedRequest.status" 
          />
        </div>
      </div>
      <div class="button-div">
        <button type="button" class="btn btn-info" @click="kreirajZahtev">
          <span v-if="isSerbian"> Kreiraj novi zahtev na osnovu ovog </span>
          <span v-else> Create a new request based on this one </span>
        </button>
      </div>
    </b-card>
    <ModalSureScrollable 
      v-if="showModalAreYouSure"
      :naslovS="'Da li ste sigurni?'"
      :naslovE="'Are you sure?'"
      :tekstE="tekstE"
      :tekstS="tekstS"
      @close="showModalAreYouSure = false"
      @yes="endRequest"
    />
    <ModalSuccess 
      v-if="requestFinished" :textS="successMessageS" 
      :textE="successMessageE" @close="closeModalSuccess"
    />
    <ModalReportUser 
      v-if="showModalReport" @setMessages="setReportMessages" 
      @close="showModalReport = false" :userToReport="otherUser" :request="computedRequest"
    />
    <ModalRateUser 
      v-if="showModalRate" @setMessages="setRateMessages" 
      @close="showModalRate = false" :userToRate="otherUser" :request="computedRequest"
    />
  </div>
</template>

<script>
import Map from "@/components/Map"
import Task from "@/components/Task"
import Spinner from "@/components/Spinner"
import OfferBox from "@/components/OfferBox"
import EditBox from "@/components/EditBox"
import LightBox from 'vue-image-lightbox'
import ModalSureScrollable from "@/components/ModalSureScrollable"
import ModalSuccess from "@/components/ModalSuccess"
import ModalReportUser from "@/components/ModalReportUser"
import ModalRateUser from "@/components/ModalRateUser"

export default {
  components: {
    Map,
    Task,
    Spinner,
    OfferBox,
    EditBox,
    LightBox,
    ModalSureScrollable,
    ModalSuccess,
    ModalReportUser,
    ModalRateUser
  },
  props: {
    request: {
      required: false
    },
    startingView: {
      required: false,
      type: String,
      default: "Details"
    }
  },
  data() {
    return {
      computedRequest: null,
      isMapOpened: false,
      hasAddresses: false,
      clickedPicture: null,
      pictureExpanded: false,
      showView: this.startingView,
      showModalAreYouSure: false,
      showModalReport: false,
      showModalRate: false,
      purpose: "",
      successMessageS: "",
      successMessageE: ""
    }
  },
  computed: {
    filteredInfo() {
      return this.$store.state.requestFilteredInfo
    },
    filteredOffers() {
      return this.$store.state.offers
    },
    filteredEdits() {
      return this.$store.state.edits
    },
    isSerbian() {
      return this.$store.state.isSerbian
    },
    newFinishedRequest() {
      return this.$store.state.newFinishedRequest
    },
    newSuccessfullyFinishedRequest() {
      return this.$store.state.newSuccessfullyFinishedRequest
    },
    computedNote() {
      if(!this.computedRequest.note || this.computedRequest.note == "")
        return this.isSerbian ? 'Nema napomena za ovaj zahtev' : 'There are no notes about this request'
      else 
        return this.computedRequest.note
    },
    fullUserName() {
      return this.otherUser.first_name + " " + this.otherUser.last_name
    },
    fullDirectUserName() {
      return this.computedRequest.direct_user.first_name + " " + this.computedRequest.direct_user.last_name
    },
    otherUser() {
      if(this.computedRequest.created_by && this.computedRequest.created_by.id == this.$store.state.authUser.id)
        return this.computedRequest.working_with
      else
        return this.computedRequest.created_by
    },
    finishedOtherUser() {
      if(this.computedRequest.created_by && this.computedRequest.created_by.id == this.$store.state.authUser.id)
        return this.computedRequest.finished_working_with
      else
        return this.computedRequest.finished_created_by
    },
    finishedThisUser() {
      if(this.computedRequest.created_by && this.computedRequest.created_by.id == this.$store.state.authUser.id)
        return this.computedRequest.finished_created_by
      else
        return this.computedRequest.finished_working_with
    },
    tekstE() {
      if(this.purpose == 'finish') {
        if(this.finishedOtherUser) {  
          return 'If you choose "yes", this request will be finished and marked as successful. ' + 
                 'After this, you will not be able to make any action concerning the execution of this request.' +
                 'You will be able to see it in the "Finished" tab, and you will have the options to report ' +
                 'and rate the user you have been working with. Do you wish to proceed?'
        }
        else {
          return 'If you choose "yes", you will start the process of successfully ending the request. ' +
                 'After this, you will not be able to make any action concerning the execution of this request, ' +
                 'and you will have to wait for the other user to confirm the successful ending of this request. ' + 
                 'After he/she confirms, you will be able to see this request in the "Finished" tab, and you will have the options to report ' +
                 'and rate the user you have been working with. Do you wish to proceed?' 
        }
      }
      else if(this.purpose == 'cancel') {
        return 'If you choose "yes", this request will be finished and marked as unsuccessful. ' +
               'After this, you will not be able to make any action concerning the execution of this request. ' +
               'You will be able to see it in the "Finished" tab, and you will have the options to report ' +
               'and rate the user you have been working with. Do you wish to proceed?'
      }
      else {
        return 'Are you sure you want to delete this request?'
      }
    },
    tekstS() {
      if(this.purpose == 'finish') {
        if(this.finishedOtherUser) {  
          return 'Ako izaberete "da", ovaj zahtev će biti okončan i označen kao uspešan. ' + 
                 'Nakon ovoga, nećete moći da preduzimate nikakve akcije u vezi sa izvršenjem ovog zahteva. ' +
                 'Moći ćete da vidite zahtev u kartici "Završeni", i imaćete opcije prijavljivanja problema sa saradnikom, ' +
                 'kao i ocenjivanje saradnika. Da li želite da nastavite?'
        }
        else {
          return 'Ako izaberete "da", započećete proces uspešnog okončanja zahteva. ' +
                 'Nakon ovoga , nećete moći da preduzimate nikakve akcije u vezi sa izvršenjem ovog zahteva, ' +
                 'i moraćete da sačekate da Vaš saradnik na ovom zahtevu potvrdi uspešan završetak. ' + 
                 'Nakon što potvrdi, moći ćete da vidite zahtev u kartici "Završeni", i imaćete opcije prijavljivanja problema sa saradnikom, ' +
                 'kao i ocenjivanje saradnika. Da li želite da nastavite?' 
        }
      }
      else if(this.purpose == 'cancel') {
        return 'Ako izaberete "da", ovaj zahtev će biti okončan i označen kao neuspešan. ' + 
               'Nakon ovoga, nećete moći da preduzimate nikakve akcije u vezi sa izvršenjem ovog zahteva. ' +
               'Moći ćete da vidite zahtev u kartici "Završeni", i imaćete opcije prijavljivanja problema sa saradnikom, ' +
               'kao i ocenjivanje saradnika. Da li želite da nastavite?'
      }
      else {
        return 'Da li ste sigurni da želite da obrišete ovaj zahtev?'
      }
    },
    requestFinished() {
      return this.$store.state.success
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
      const retValue = []
      if(this.computedRequest.picture_required) {
        this.filteredInfo.pictures.forEach(pic => {
          retValue.push({
             thumb: 'data:;base64,' + pic.picture,
             src: 'data:;base64,' + pic.picture
          })
        })
      }
      return retValue
    },
    isRunner() {
      if(!this.computedRequest.created_by || this.computedRequest.created_by.id == this.$store.state.authUser.id)
        return false
      else return true
    },
    popoverText() {
      if(this.finishedThisUser) {
        if(this.isSerbian)
          return "Pokrenuli ste proces okončanja zahteva. " + 
                 "Da bi se ovaj proces dovršio i da bi zahtev bio označen kao uspešan, morate sačekati " 
                 + "da Vaš saradnik na ovom zahtevu potvrdi uspešno okončanje."
        else
          return "You have started the process of ending the request. " + 
                 "In order for this proccess to finish and mark the collaboration as successful, you must wait " +  
                 "for your partner to confirm the successful ending of this request."
      }

      else if(this.finishedOtherUser) {
        if(!this.isRunner) {
          if(this.isSerbian) {
              return "Vaš saradnik na ovom zahtevu pokrenuo je proces okončanja zahteva. " + 
                    "Izaberite opciju \"Uspešno okončaj zahtev\" u akcijama kako biste dovršili ovaj " 
                    + "proces i označili uspešan kraj saradnje."
          }
          else {
              return "Your partner in this request has started the procces of ending the request. " + 
                  "Choose the \"Successfully end request\" option in the actions dropdown " +  
                  "to finish the process and mark the collaboration as successful."
          }
        }
        else {
          if(this.isSerbian) {
            return "Vaš saradnik na ovom zahtevu pokrenuo je proces okončanja zahteva. " + 
                    "Da biste dovršili ovaj proces i označili uspešan kraj saradnje kao izvršilac, " +
                    "morate preuzeti Android aplikaciju."
          }
          else {
            return "Your partner in this request has started the procces of ending the request. " + 
                  "To finish the process and, as a runner in this requests, mark the collaboration as successful, " + 
                  "you have to download the Android application."
          }
        }
      }

      else return ""

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
            if(this.isSerbian)
              returnValue = "početna cena"
            else
              returnValue = "starting price"
        }
      }
      return returnValue
    },
    dateAndTime()
    {
      var date = new Date(this.computedRequest.time)
            
      var day = date.getDate()
      var month = date.getMonth()+1
      var year = date.getFullYear()
      var hours = date.getHours()
      var minutes = date.getMinutes()
      
      var monthString = ""
      var hoursString = hours
      var minutesString = minutes
      if(hours < 10)
          hoursString = "0" + hours
      if(minutes < 10)
          minutesString = "0" + minutes

      switch(month)
      {
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
    ponudeTekst()
    {
      if(this.isSerbian)
      {
        if(this.filteredOffers == null)
          return ""
        var jedinice = this.filteredOffers.length - (Math.round(this.filteredOffers.length/10).toFixed(0))*10
        var string = ""
        switch(jedinice)
        {
          case 0: 
            string = "ponuda"
            break
          case 1: 
            string = "ponudu"
            break
          case 2: 
            string = "ponude"
            break
          case 3: 
            string = "ponude"
            break
          case 4: 
            string = "ponude"
            break
          default: 
            string = "ponuda"
            break
        }
        if(this.filteredOffers.length == 11 || this.filteredOffers.length == 12  || this.filteredOffers.length == 13 || this.filteredOffers.length == 14)
          string = "ponuda"
        return 'Imate ' + this.filteredOffers.length + ' ' + string
      }
      else
      {
        return this.filteredOffers.length == 1? 'You have ' + this.filteredOffers.length + ' offer' : 'You have ' + this.filteredOffers.length + ' offers'
      }
    },
    zahteviTekst()
    {
      if(this.filteredEdits == null)
        return ""
      if(this.isSerbian)
      {
        var jedinice = this.filteredEdits.length - (Math.round(this.filteredEdits.length/10).toFixed(0))*10
        var string = ""
        switch(jedinice)
        {
            case 0: 
                    string = "zahteva"
                    break
            case 1: 
                    string = "zahtev"
                    break
            default: 
                    string = "zahteva"
                    break
        }
        if(this.filteredEdits.length == 11)
          string = "zahteva"
        return 'Imate ' + this.filteredEdits.length + ' ' + string + ' za izmenama'
      }
      else
      {
        return this.filteredEdits.length == 1? 'You have ' + this.filteredEdits.length + ' edit request' : 'You have ' + this.filteredEdits.length + ' edit requests'
      }
    },
    canRate() {
      if(this.isRunner) 
        return !this.computedRequest.rated_created_by
      else
        return !this.computedRequest.rated_working_with
    },
    offersBadge() {
      if(!this.filteredOffers) 
        return 0
      else return this.filteredOffers.length
    },
    editsBadge() {
      if(!this.filteredEdits) 
        return 0
      else return this.filteredEdits.length
    }
  },
  methods: {
    routeChanged() {
      let fetchedById = false
      this.$store.state.specificRequest = null
      this.$store.state.requestFilteredInfo = {}
      this.$store.state.offers = null
      this.$store.state.edits = null
      this.$store.state.isRequestInfoLoaded = false
      const routeId = this.$route.params.id

      const vm = this
      function callbackFetchedById() {
        if(vm.$store.state.specificRequest != null)
        {
          vm.computedRequest = vm.$store.state.specificRequest
          if(vm.$store.state.specificRequest == -1)
            vm.$router.push({name: "PageRequests"})
        }
        else
          setTimeout(callbackFetchedById, 200)
      }

      function callbackMarkers() {
        if(vm.$store.state.requestFilteredInfo.destination || vm.$store.state.requestFilteredInfo.tasklist) {
          vm.setMapMarkers()
        }
        else
          setTimeout(callbackMarkers, 200)
      }

      let filters = {
        request: routeId,
        "offers" : false,
        "edits" : false, 
        "accepted_offer" : false,
        "rating_created_by" : false,
        "rating_working_with" : false,
        "tasklist" : true,
        "destination" : true,
        "pictures" : true
      }

      if(!this.request) {
        if(this.$store.state.createdAuthRequests)
          this.computedRequest = this.$store.state.createdAuthRequests.results.find(req => req.id == routeId)
        if(!this.computedRequest && this.$store.state.runnerAuthRequests)
          this.computedRequest = this.$store.state.runnerAuthRequests.results.find(req => req.id == routeId)
        if(!this.computedRequest && this.$store.state.overAuthRequests)
          this.computedRequest = this.$store.state.overAuthRequests.results.find(req => req.id == routeId)
        if(!this.computedRequest)
        {
          this.$store.dispatch('getRequestById', routeId)
          fetchedById = true
          callbackFetchedById()
        }
      }
      else {
        this.computedRequest = this.request
      }
      
      if(!fetchedById) {
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
          if(this.computedRequest.rated_created_by)
            filters["rating_created_by"] = true
          if(this.computedRequest.rated_working_with)
            filters["rating_working_with"] = true
        }
      }

      this.$store.dispatch("fillFilteredRequestInfo", {filters, requestId: filters.request, dataLoaded: false})
      callbackMarkers()
    },
    setMapMarkers() {
      const markerPositions = [];
      if(this.filteredInfo.destination) {
        this.hasAddresses = true
        const newPosition = {
          pos: {
            lat: this.filteredInfo.destination.latitude,
            lng: this.filteredInfo.destination.longitude
          },
          lab: "F",
          info: this.filteredInfo.destination.name
        }
        markerPositions.push(newPosition)
      }

      if(this.filteredInfo.tasklist)
      {
        this.filteredInfo.tasklist.forEach((task, ind) => {
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
    expandPicture(index) {
      this.clickedPicture = index
      this.pictureExpanded = true
    },
    acceptOffer(offer) {
      this.filteredInfo.acceptedOffer = offer
      this.computedRequest.working_with = offer.created_by
      this.computedRequest.status = 1
      let index = -1
      if(this.$store.state.createdAuthRequests)
        index = this.$store.state.createdAuthRequests.results.findIndex(req => req.id == this.computedRequest.id)
      
      if(index != -1) {
        this.$store.state.createdAuthRequests.results[index].status = 1
        this.$store.state.createdAuthRequests.results[index].working_with = offer.created_by
      }
      if(offer.edit) {
        if(offer.edit.time) {
          if(index != -1)
            this.$store.state.createdAuthRequests.results[index].time = offer.edit.time
          this.computedRequest.time = offer.edit.time
        }
        if(offer.edit.tasks && offer.edit.tasks.length > 0) {
          offer.edit.tasks.forEach(newTask => {
            const oldTask = this.filteredInfo.tasklist.find(task => task.id == newTask.task)
            if(oldTask)
              oldTask.address = newTask.address
          })
        }
      }
      this.$store.dispatch('acceptOffer', {offerId: offer.id, requestId: this.computedRequest.id})
      this.setMapMarkers()
      this.showView = 'Details'
    },
    rejectOffer(offer) {
      const index = this.filteredOffers.findIndex(off => off.id == offer.id)
      this.$store.state.offers.splice(index, 1)
      if(this.filteredOffers.length == 0) {
        this.showDetails()
      }
      this.$store.dispatch('rejectOffer', offer.id)
    },
    acceptEdit(edit) {
      let index = -1
      if(this.$store.state.createdAuthRequests)
        index = this.$store.state.createdAuthRequests.results.findIndex(req => req.id == this.computedRequest.id)
      
      if(edit.request_edit.time) {
        if(index != -1)
          this.$store.state.createdAuthRequests.results[index].time = edit.request_edit.time
        this.computedRequest.time = edit.request_edit.time
      }
      if(edit.request_edit.tasks.length > 0) {
        edit.request_edit.tasks.forEach(newTask => {
          const oldTask = this.filteredInfo.tasklist.find(task => task.id == newTask.task)
          if(oldTask)
            oldTask.address = newTask.address
        })
      }
      this.$store.dispatch('acceptEdit', {requestId: this.computedRequest.id, editId: edit.id})
      const editIndex = this.filteredEdits.findIndex(ed => ed.id == edit.id)
      this.$store.state.edits.splice(editIndex, 1)
      if(this.filteredEdits.length == 0) {
        this.showDetails()
      }
    },
    rejectEdit(edit) {
      const index = this.filteredEdits.findIndex(ed => ed.id == edit.id)
      this.$store.state.edits.splice(index, 1)
      if(this.filteredEdits.length == 0) {
        this.showDetails()
      }
      this.$store.dispatch('rejectEdit', edit.id)
    },
    endRequest() {
      if(this.purpose == 'finish') {
        if(this.finishedOtherUser) {
          this.computedRequest.status = 2
          this.successMessageS = "Zahtev uspešno okončan."
          this.successMessageE = "Request successfully finished."
        }
        else {
          this.successMessageS = "Pokrenuto uspešno okončanje zahteva."
          this.successMessageE = "The process of successfully finishing the request has been started."
        }
        this.computedRequest.finished_created_by = true
        this.$store.dispatch('finishRequest', this.computedRequest)
      }
      else if(this.purpose == 'cancel') {
        this.computedRequest.status = 3
        this.$store.dispatch('cancelRequest', this.computedRequest)
        this.successMessageS = "Zahtev uspešno prekinut."
        this.successMessageE = "The request has successfully been canceled."
      }
      else {
        if(this.$store.state.createdAuthRequests) {
          this.$store.state.createdAuthRequests.results.forEach((element,index) => {
              if(element.id == this.computedRequest.id)
                  this.$store.state.createdAuthRequests.results.splice(index,1)
          })
        }
        this.$store.dispatch("deleteRequest", this.computedRequest.id)
        this.goToPageRequests()
      }
      this.showModalAreYouSure = false
    },
    setReportMessages() {
      this.successMessageS = "Uspešno prijavljen problem sa korisnikom."
      this.successMessageE = "User successfuly reported."
    },
    setRateMessages() {
      this.successMessageS = "Uspešno ocenjen korisnik."
      this.successMessageE = "User successfuly rated."
    },
    closeModalSuccess() {
      this.$store.state.success = false
    },
    showDetails() {
      this.setMapMarkers()
      this.showView = 'Details'
    },
    changeViewFromDetails(view) {
      const len = view == 'Edits' ? this.filteredEdits.length : this.filteredOffers.length
      this.$store.state.openedOffersOrEdits = new Array(len).fill(false)
      this.showView = view
    },
    goToProfile() {
      this.$router.push({
        name: "PageViewProfile",
        params: {
          id: this.computedRequest.status != 0 ? this.otherUser.id : this.computedRequest.direct_user.id,
          user: this.computedRequest.status != 0 ? this.otherUser : this.computedRequest.direct_user,
          RequestSelect: false,
          RequestView: {
            request: this.computedRequest,
            view: "Details"
          }
        }
      })
    },
    goToPageRequests() {
      this.$router.push({name: "PageRequests"})
    }
  },
  created() {
    this.routeChanged()
  },
  watch: {
    $route() {
      this.routeChanged()
    },
    // eslint-disable-next-line no-unused-vars
    newFinishedRequest(newVal, oldVal) {
      if(newVal.type_id == this.$route.params.id) {
        this.computedRequest.status = 3
        this.showView = 'Details'
      }
    },
    // eslint-disable-next-line no-unused-vars
    newSuccessfullyFinishedRequest(newVal, oldVal) {
      if(newVal.type_id == this.$route.params.id) {
        if(!this.isRunner) {
          this.computedRequest.finished_working_with = true
        }
        else
          this.computedRequest.finished_created_by = true
        if(this.finishedThisUser) {
          this.computedRequest.status = 2
        }
          this.showView = 'Details'
      }
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
    align-self:stretch;
    padding-left:3%;
    padding-right:3%;
  } 

  .title-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    flex-wrap: wrap-reverse;
    width: 100%;
  }

  .details {
    display:flex;
    flex-direction: column;
  }

  .request-info {
    align-self: stretch;
    display: flex;
    margin-bottom: 30px;
    padding-bottom: 15px;
    border-bottom: 1px solid lightgrey;
  }

  .note {
    display: flex;
    align-items: flex-start;
    margin-bottom: 5px;
  }

  .txt-area {
    background-color: rgb(250, 210, 125);
    font-size: 15px;
    padding-top: 1px;
  }

  .tasks {
    align-self:stretch;
    margin-top: 20px;
  }

  .destination {
    width: 60%;
    padding-top: 5px;
  }

  .other-user {
    margin-right: 30px;
    border-right: 1px solid lightgray;
    padding-right: 30px;
    width: 40%;
    padding-top: 5px;
  }

  /* #dropdown-1 {
    margin-bottom: 15px;
  } */

  .important-img {
    background-color: green;
    height: 30px;
    width: 30px;
    margin-right: 10px;
    border-radius: 30px;
    border: 1px solid green;
  }

  .card-title {
    font-size: 25px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
  }

  .pic-required {
    font-size: 14px;
    font-weight: 600;
    margin-top: 5px;
    color: rgb(175, 4, 4);
  }

  .side-info-title {
    font-size: 16px;
    font-weight: bold;
  }

  .dropdown-and-important {
    display: flex;
    align-items: center;
    flex-wrap: nowrap;
    margin-bottom: 15px;
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

  .title-start {
    flex-grow: 1;
    width: 100%;
    font-weight: bold;
    font-size: 30px;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap-reverse;
  }

  .pic-and-span {
    margin: 10px 40px 10px 0;
    display:flex;
    align-items: center;
    font-size: 16px;
  }

  .slika-dugme
  {
    width: 23px;
    height: 23px;
    margin-right: 10px;
  }

  .basic-info {
    margin-right: 15px;
    width: 100%;
  }

  .check {
    background-color: green;
    padding: 3px;
    border-radius: 3px;
  }

  .shutdown {
    padding: 2px;
    background-color: rgb(240, 6, 6);
    border-radius: 3px;
  }

  .back {    
    background-color: gray;
    padding: 2px;
    border-radius: 3px;
  }

  .xmark {
    padding: 3px;
    background-color: rgb(240, 6, 6);
    border-radius: 3px;
  }

  .wrench {
    padding: 2px;
    background-color: rgb(81, 166, 194);
    border-radius: 3px;
  }

  .title-pic {
    margin: 0px 10px 0px 0px;
    height: 18px;
    width: 18px;
  }

  .notification-span {
    margin-left: -17px;
  }

  .notification-badge {
    font-size: 85%;
    position: relative;
    top: -13px;
    right: 17px;
  }

  .big-notification-badge {
    font-size: 80%;
    position: relative;
    top: -19px;
    left: 34px;
    z-index: 1;
  }

  .pictures-div {
    border: 1px solid black;
    padding: 10px;
    border-radius: 5px;
    margin-top: 10px;
  }

  .smaller-title-pic-span {
    height: fit-content;
    margin-bottom: 5px;
    display: flex;
    align-items: center;
    word-break: break-word;
  }

  .no-detail-view {
    display: flex; 
    flex-direction: column;
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

  .no-info-txt {
    font-weight: 600;
    color: rgb(175, 4, 4);
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

  .image {
    border-radius: 60px;
    width: 40px;
    height: 40px;
    margin-right: 5px;
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

  .visible {
    visibility: visible;
    margin-top: 20px;
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
    width: 60px;
    height: 60px;
    border: 1px solid grey;
    border-radius: 5px;
  }

  .expandable-image:hover {
    cursor: pointer;
    border-color: black;
  }

  .task-title {
    display: flex;
    font-size: 35px;
    font-weight: 600;
  }

  .no-details-title {
    font-size: 30px;
    font-weight: bold;
  }

  .dropdown-runner {
    white-space: normal !important;
  }

  @media only screen and (max-width: 749px) {
    .card {
      margin: 0px 5% 50px 5%;
    }
  }

  @media only screen and (max-width: 599px) {
    .card {
      margin: 0px 2% 50px 2%;
    }

    .request-info {
      flex-direction: column;
    }

    .other-user {
      margin-bottom: 15px;
      padding-bottom: 15px;
      border-bottom: 1px solid lightgray;
      width: 100%;
      border-right: hidden;
    }

    .destination {
      width: unset;
    }
  }

</style>