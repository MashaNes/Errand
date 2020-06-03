<template>
  <Spinner v-if="savingChanges" />
  <div v-else>
    <div class="main-container">
      <div class="picture-side">
        <div v-if="newPicture" @click="removePicture" class="remove-pic-div">
          <img src="@/assets/remove.svg" height="20" width="20" class="remove-pic"/>
          <span v-text="isSerbian ? 'Ukloni sliku' : 'Remove picture'"></span>
        </div>
        <div class="media-center">
          <p class="image is-128x128"
            @drop.prevent="onDrop"
            @dragenter.prevent="onDragEnter"
            @dragleave.prevent="onDragLeave" 
            @dragover.prevent
            @mouseenter.prevent="onDragEnter"
            @mouseleave.prevent="onDragLeave"
            @click="$refs.file.click()"
          >
            <img class="rounded-image" :src="newPicture ? 'data:;base64,'+ picture : require('../assets/no-picture.png')">
            <img 
              v-show="isDragged" 
              :class="['rounded-image', 'semi-transparent']" 
              src="@/assets/camera.png"
            >
          </p>
        </div>
          <div :class="[isDragged ? 'shown' : 'hidden', 'picture-msg']">
            <span v-if="isSerbian">Kliknite na sliku ili prevucite novu</span>
            <span v-else>Click the picture or drop a new one</span>
          </div>
        <div style="display:flex; flex-direction:column; align-items:center">
            <input type="file" ref="file" style="display: none" accept="image/*" @change="pictureSelected"/>
          <div>
            <label 
              style="margin-top: 15px;" 
              class = "register-label" v-if="isSerbian"> Ime: </label>
            <label 
              style="margin-top: 15px;" 
              class = "register-label" v-else> Name: </label>
            <div class="field">
              <input
                v-if="isSerbian" 
                class="input is-medium"
                type="text"
                placeholder="Ime"
                @blur="$v.changedUser.first_name.$touch()"
                v-model="changedUser.first_name"
              >
              <input
                v-else 
                class="input is-medium"
                type="text"
                placeholder="Name"
                @blur="$v.changedUser.first_name.$touch()"
                v-model="changedUser.first_name"
              >
            </div>
            <div v-if="$v.changedUser.first_name.$error" class = "form-error">
              <span 
                v-if="!$v.changedUser.first_name.required"
                class = "help is-danger"
              > 
                <span v-if="isSerbian">Morate uneti ime</span>
                <span v-else>Name is required </span>  
              </span>
            </div>

            <label 
              style="margin-top: 15px;" 
              class = "register-label" v-if="isSerbian"> Prezime: </label>
            <label 
              style="margin-top: 15px;" 
              class = "register-label" v-else> Last name: </label>
            <div class="field">
              <input 
                v-if="isSerbian"
                class="input is-medium"
                type="text"
                placeholder="Prezime"
                @blur="$v.changedUser.last_name.$touch()"
                v-model="changedUser.last_name"
              >
              <input 
                v-else
                class="input is-medium"
                type="text"
                placeholder="Last name"
                @blur="$v.changedUser.last_name.$touch()"
                v-model="changedUser.last_name"
              >
            </div>
            <div v-if="$v.changedUser.last_name.$error" class = "form-error">
              <span 
                v-if="!$v.changedUser.last_name.required"
                class = "help is-danger"
              > 
                <span v-if="isSerbian">Morate uneti prezime</span>
                <span v-else>Last name is required </span>  
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="personal-info">
        <b-list-group >
          <b-list-group-item class="l-group-title">
            <span v-if="isSerbian"
              class="info-title" 
            > Lični podaci </span>
            <span v-else
              class="info-title" 
            > Personal info </span>

            <div class="l-group-btns">
              <b-button 
                :disabled="isFormInvalid"
                class="button is-primary title-btn"
                @click="openModalSave = true"
              >
                <strong v-if="isSerbian">Sačuvaj izmene</strong>
                <strong v-else>Save changes</strong>
              </b-button>
              <b-button 
                class="button is-primary title-btn"
                @click="openModalCancel = true"
              >
                <strong v-if="isSerbian">Odbaci izmene</strong>
                <strong v-else>Dismiss changes</strong>
              </b-button>
            </div>
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/call.svg" 
              height = "20" 
              width = "20"
              
            />
            <div class="list-value">
              <div class = "control">
                <div class="flex-row-elements">
                  <VuePhoneNumberInput style="min-width: 250px" no-example v-model="changedUser.phone"/>
                </div>
              </div>
            </div>
            
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/address.svg" 
              height = "20" 
              width = "20"
              
            />
            <div class="list-value">
              <div class="address over-flow" v-for="(a, ind) in firstAddresses" :key="ind">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeAddress(ind)">
                <span >{{ a.name }}</span>
              </div>
              <div v-if="addressArrayLength" class="over-flow" style="margin-bottom: 20px; ">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeAddress(addressArrayLength-1)"> 
                <span >{{lastAddress.name}} </span>
              </div>
              <div class="field flex-row-elements">
                <b-button 
                  class="button is-primary"
                  style="font-weight: bold;"
                  v-text="isSerbian ? 'Dodaj adresu' : 'Add an address'"
                  v-if="!showMapView"
                  @click="showAddrMap"
                ></b-button>
              </div>
            </div>
          </b-list-group-item>
        </b-list-group>
      </div>
    </div>
    <div class="map-wrap" :class="showMapView ? 'visible' : 'invisible'">
      <AddAddressMap 
        :HasCloseButton="true" 
        :StartingAddress="{name: 'Sokogradska 9, Sokobanja', latitude: 43.639696, longitude: 21.878703}"  
        @close="handleMapClosing" 
      />
    </div>
    <ModalAreYouSure 
      :naslovS="'Sačuvaj izmene'" 
      :tekstS="'Da li ste sigurni da želite da sačuvate sve izmene?'" 
      :naslovE="'Save changes'"
      :tekstE="'Are you sure you want to save all the changes?'"
      @close="openModalSave = false" @yes="saveChanges" v-if="openModalSave"
    />
    <ModalAreYouSure 
      :naslovS="'Odbaci izmene'" 
      :tekstS="'Da li ste sigurni da želite da odbacite izmene?'" 
      :naslovE="'Dismiss changes'"
      :tekstE="'Are you sure you want to dismiss all the changes?'"
      @close="openModalCancel = false" @yes="$emit('cancelChanges')" v-if="openModalCancel"
    />
  </div>
</template>

<script>
import {required} from "vuelidate/lib/validators"
import VuePhoneNumberInput from 'vue-phone-number-input';
import 'vue-phone-number-input/dist/vue-phone-number-input.css';
import AddAddressMap from "@/components/AddAddressMap"
import ModalAreYouSure from "@/components/ModalAreYouSure"
import Spinner from "@/components/Spinner"

export default {
  components: {
    VuePhoneNumberInput,
    AddAddressMap,
    ModalAreYouSure,
    Spinner
  },
  props: {
    user: {
      required: true,
      type: Object
    },
    addresses: {
      type: Array,
      required: true
    }
  },
  created() {
    
  },
  data() {
    return {
      changedUser: JSON.parse(JSON.stringify(this.user)),
      changedAddresses: JSON.parse(JSON.stringify(this.addresses)),
      newPhoneNumber: "",
      newAddress: "",
      newPicture: this.user.picture,
      isDragged: false,
      dragCounter: 0,
      showMapView: false,
      pictureChanged: false,
      openModalSave: false,
      openModalCancel: false,
      savingChanges: false
    }
  },
  validations: {
    changedUser: {
      last_name: { required },
      first_name: { required }
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    picture() {
      if(this.newPicture)
      {
        const splitted = this.newPicture.split(',')
        if (splitted.length == 2)
          return splitted[1]
        else 
          return this.newPicture
      }
      return null
    },
    addressArrayLength() {
      return this.changedAddresses.length
    },
    isFormInvalid(){
      return this.$v.changedUser.$invalid
    },
    firstAddresses() {
      const lastIndex = this.changedAddresses.length;
      if(lastIndex < 2)
        return null;
      const arrayCopy = this.changedAddresses.map(addr => addr);
      arrayCopy.splice(lastIndex-1, 1);
      return arrayCopy;
    },
    lastAddress() {
      const length = this.changedAddresses.length;
      if(length == 0)
        return null;
      return this.changedAddresses[length-1];
    },
  },
  methods: {
    showAddrMap() {
      this.showMapView = true
      window.scroll({
        top: 700,
        behavior: 'smooth'
      })
    },
    removeAddress(index) {
      this.changedAddresses.splice(index, 1);
    },
    addElement(resource, element) {
      if(element != "")
        this.changedUser[resource].push(element)
      if(resource == 'homeAddress')
        this.newAddress = ""
      else this.newPhoneNumber = ""
    },
    handleMapClosing(newAddr) {
      console.log(newAddr)
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth'
      })
      if(newAddr) {
        if(!this.changedAddresses.find(addr => addr.name === newAddr.name)) {
          this.changedAddresses.push({
            "created_by": this.user.id,
            "name": newAddr.name,
            "longitude": newAddr.longitude,
            "latitude": newAddr.latitude,
            "home": true,
            "arrived": false
          })
        }
        else {
          this.$toasted.error(this.isSerbian ? 'Ne možete imati dve identične adrese!' : 'You can not have two identical addresses!', {
            duration: 3000
          })
        }
      }
      this.showMapView = false
    },
    saveChanges() {
      this.savingChanges = true
      this.openModalSave = false
      this.changedUser.picture = this.picture
      let deleteCount = 0
      let addCount = 0
      this.$store.state.addressAddCount = 0
      this.$store.state.addressDeleteCount = 0

      this.addresses.forEach(addr => {
        if(!this.changedAddresses.find(a => a.name === addr.name))
        {
          this.$store.dispatch('deleteAddress', addr.id)
          deleteCount++
        }
      })

      this.changedAddresses.forEach(addr => {
        if(!addr.id)
        {
          this.$store.dispatch('addAddress', {
            "created_by": this.user.id,
            "name": addr.name,
            "longitude": addr.longitude,
            "latitude": addr.latitude,
            "home": true,
            "arrived": false
          })
          addCount++
        }
      })



      this.$store.state.authUser = this.changedUser
      this.$store.dispatch('editUser', this.pictureChanged)

      let vm = this
      function callback() {
        if(vm.$store.state.addressAddCount == addCount && vm.$store.state.addressDeleteCount == deleteCount) {
          vm.$store.state.addressAddCount = 0
          vm.$store.state.addressDeleteCount = 0
          console.log(vm.$store.state.userAddresses)
          vm.$emit('saveEditChanges')
        }
        else {
          setTimeout(callback, 200);
        }
      }
      
      callback()
    },
    pictureSelected(e) {

      const file = e.target.files[0];
      e.target.value = null
      this.addImage(file);
    },
    onDrop(e) {
      e.stopPropagation();

      const file = e.dataTransfer.files[0];
      this.addImage(file);
    },
    addImage(file) {

      if(!file) {
        this.isDragged = false;
        this.dragCounter = 0;
        return 
      }

      if(!file.type.match('image*')) {
        console.log('not an image!');

      }
      else {
        const reader = new FileReader();
        reader.onload = (e) => this.newPicture = e.target.result;

        reader.readAsDataURL(file);
      }
      this.isDragged = false;
      this.dragCounter = 0;
      this.pictureChanged = true
    },
    onDragEnter() {
      this.dragCounter ++;
      this.isDragged = true;
    },
    onDragLeave() {
      if(this.dragCounter>0)
        this.dragCounter --;
      if(!this.dragCounter)
        this.isDragged = false;
    },
    removePicture() {
      this.newPicture = null
    }
  }
}
</script>

<style scoped>
  
  .list-group-item {
    display: flex;
    justify-content: space-between;
    border: hidden;
    border-bottom: 1px solid lightgray;
    border-radius: 0px;
    align-items: center;
    background-color:inherit;
  }

  .list-key {
    font-weight: bold;
    font-size: 14px;
    flex-grow: 1;
    flex-shrink: 1;
    text-align: left;
  }

  .list-value {
    flex-grow: 2;
    flex-shrink: 1;
    text-align: left;
    font-size: 20px;
    margin-right:1%;
    margin-left:20px;
    width:100%;
  }

  .over-flow {
    width:100%;
    word-break: normal;
    overflow-wrap:break-word;
  }

  .main-container {
    padding-top: 30px;
    margin-left: 10%;
    margin-right: 10%;
    display: flex;
    flex-direction: row;
    border-radius: 10px;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height: 250px;
    width:250px;
    object-fit:cover;
    position: relative;
    top:0;
    left:0; 
    cursor:pointer;
  }

  .transparent {
    position: absolute;
    opacity: 0;
  }

  .semi-transparent {
    position: absolute;
    opacity: 0.5;
  }


  .content {
    background-color: black;
    font-size: 20px;
    color:white;
    text-align: center;
    margin-top: 15px;
    margin-bottom: 2px;
    border-radius: 5px;
    word-break: break-all;
  }

  .info-title {
    font-size: 25px;
    font-weight: bold;
    color: black;
    flex-grow:1;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
    border-radius: 60px;
  }

  .personal-info {
    display:flex;
    flex-direction: column;
    margin-left: 15px;
    flex-grow:2;
    margin-bottom:30px;
  }

  .picture-side {
    flex-grow:1;
    display:flex;
    flex-direction:column;
    align-items:center;
    margin-right:15px;
    margin-bottom:30px;
  }

  .remove-pic-div {
    width: fit-content;
    align-self: flex-start;
    margin-bottom: 5px;
    display: flex;
    align-items: center;
    font-weight: 600;
  }

  .remove-pic-div:hover {
    cursor: pointer;
    color: blue;
  }

  .remove-pic {
    margin-right: 5px;
    border-radius: 10px;
  }

  .flex-row-elements {
    display: flex;
  }

  .acc-or-remove-icon {
    margin: 0 10px 0 5px; 
    cursor:pointer;
  }

  .address {
    border-bottom:dotted 2px lightgray;
    margin-bottom: 7px;
    padding-bottom: 7px;
  }

  .phones {
    margin-bottom: 10px;
  }

  .title-btn {
    margin:5px 5px 0 5px;
  }

  .l-group-title {
    display:flex;
    flex-direction:row;
  }

  .shown {
    visibility:visible;
  }

  .hidden {
    visibility:hidden;
  }

  .l-group-btns {
    display:flex;
    flex-direction: column;
    margin-left:30px;
  }

  .picture-msg {
    width:250px;
    font: size 12px;
    text-align:center;
  }

  .visible {
    visibility: visible;
  }

  .invisible {
    visibility: hidden;
    height:0px;
    position: absolute;
    top: 0px;
  }

  .map-wrap {
    padding-bottom: 100px;
  }

@media only screen and (max-width: 750px)
  {
    .main-container {
      flex-direction: column;
      padding-top: 30px;
      margin-left: 1%;
      margin-right: 1%;
      align-items:center;
      border-radius: 10px;
    }

    .list-value {
      flex-grow: 2;
      flex-shrink: 1;
      text-align: left;
      font-size: 17px;
      margin-left: 3%;
    }

    .l-group-title {
      border-top:1px solid lightgray;
    }

    .l-group-btns {
      display:flex;
      flex-direction: column;
      flex-grow:1;
    }

    .list-group-item {
      padding-left:1%;
      padding-right:1%;
    }

    .picture-side {
      margin-right:0;
    }

  }

</style>