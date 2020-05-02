<template>
  <div>
    <div class="main-container">
      <div class="picture-side">
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
            <img class="rounded-image" :src="picture">
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
                @blur="$v.changedUser.firstName.$touch()"
                v-model="changedUser.firstName"
              >
              <input
                v-else 
                class="input is-medium"
                type="text"
                placeholder="Name"
                @blur="$v.changedUser.firstName.$touch()"
                v-model="changedUser.firstName"
              >
            </div>
            <div v-if = "$v.changedUser.firstName.$error" class = "form-error">
              <span 
                v-if = "!$v.changedUser.firstName.required"
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
                @blur="$v.changedUser.lastName.$touch()"
                v-model="changedUser.lastName"
              >
              <input 
                v-else
                class="input is-medium"
                type="text"
                placeholder="Last name"
                @blur="$v.changedUser.lastName.$touch()"
                v-model="changedUser.lastName"
              >
            </div>
            <div v-if = "$v.changedUser.lastName.$error" class = "form-error">
              <span 
                v-if = "!$v.changedUser.lastName.required"
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
                @click="saveChanges()"
              >
                <strong v-if="isSerbian">Sačuvaj izmene</strong>
                <strong v-else>Save changes</strong>
              </b-button>
              <b-button 
                class="button is-primary title-btn"
                @click="$emit('saveEditChanges')"
              >
                <strong v-if="isSerbian">Odustani</strong>
                <strong v-else>Cancel</strong>
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
              <div class="phones" v-for="p in firstElements('phone')" :key="p">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('phone', p)"> 
                <span>{{ p }}</span>
              </div>
              <div v-if="phoneArrayLength">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('phone', lastElement('phone'))"> 
                <span v-text="lastElement('phone')"></span>
              </div>
              <div class = "control" style="margin-top: 10px">
                <label class = "register-label" v-if="isSerbian"> Dodajte broj telefona: </label>
                <label class = "register-label" v-else> Add a phone number: </label>
                <div class="flex-row-elements">
                  <VuePhoneNumberInput style="min-width: 250px" no-example v-model="newPhoneNumber"/>
                  <img v-if="newPhoneNumber != ''" src="@/assets/confirm.svg" height="23" width="23" class="acc-or-remove-icon" @click="addElement('phone', newPhoneNumber)">  
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
              <div class="address" v-for="a in firstElements('homeAddress')" :key="a">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('homeAddress', a)">
                <span >{{ a }}</span>
              </div>
              <div v-if="addressArrayLength">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('homeAddress', lastElement('homeAddress'))"> 
                <span >{{lastElement('homeAddress')}} </span>
              </div>
              <label 
                style="margin-top: 15px;" 
                class = "register-label" v-if="isSerbian"> Dodajte adresu: </label>
              <label 
                style="margin-top: 15px;" 
                class = "register-label" v-else> Add a new address: </label>
              <div class="field flex-row-elements">
                <input 
                  v-if="isSerbian"
                  class="input is-medium"
                  type="text"
                  placeholder="Adresa"
                  v-model="newAddress"

                >
                <input 
                  v-else
                  class="input is-medium"
                  type="text"
                  placeholder="Address"
                  v-model="newAddress"

                >
                <img v-if="newAddress != ''" src="@/assets/confirm.svg" height="23" width="23" class="acc-or-remove-icon" @click="addElement('homeAddress', newAddress)">
              </div>
            </div>
          </b-list-group-item>
        </b-list-group>
      </div>
    </div>
  </div>
</template>

<script>
  import {required} from "vuelidate/lib/validators"
  import VuePhoneNumberInput from 'vue-phone-number-input';
  import 'vue-phone-number-input/dist/vue-phone-number-input.css';

export default {
  components: {
    VuePhoneNumberInput
  },
  props: {
    user: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      changedUser: JSON.parse(JSON.stringify(this.user)),
      newPhoneNumber: "",
      newAddress: "",
      newPicture: null,
      isDragged: false,
      dragCounter: 0,
      toasterMessage: ""
    }
  },
  validations: {
    changedUser: {
      lastName: { required },
      firstName: { required }
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    progressBarVariant() {
      return this.user.rating < 2 ? 'danger' : 
             this.user.rating < 5 ? 'warning' : 'success'
    },
    picture() {
      return this.newPicture ? this.newPicture : this.user.picture
    },
    phoneArrayLength() {
      return this.changedUser['phone'].length
    },
    addressArrayLength() {
      return this.changedUser['homeAddress'].length
    },
    isFormInvalid(){
      return this.$v.changedUser.$invalid
    },
  },
  methods: {
    removeElement(resource, element) {
      const index = this.changedUser[resource].findIndex(e => e === element);
      this.changedUser[resource].splice(index, 1);
    },
    addElement(resource, element) {
      // eslint-disable-next-line no-debugger
      if(element != "")
        this.changedUser[resource].push(element)
      if(resource == 'homeAddress')
        this.newAddress = ""
      else this.newPhoneNumber = ""
    },
    firstElements(resource) {
      const lastIndex = this.changedUser[resource].length;
      if(lastIndex < 2)
        return null;
      const arrayCopy = [...this.changedUser[resource]];
      arrayCopy.splice(lastIndex-1, 1);
      return arrayCopy;
    },
    lastElement(resource) {
      const length = this.changedUser[resource].length;
      if(length == 0)
        return null;
      return this.changedUser[resource][length-1];
    },
    saveChanges() {
      this.changedUser.picture = this.picture;
      this.$store.dispatch('editUser', this.changedUser)
      this.$emit('saveEditChanges')
    },
    pictureSelected(e) {

      const file = e.target.files[0];
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
    word-break: break-all;
  }

  .main-container {
    margin-top: 30px;
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

@media only screen and (max-width: 750px)
  {
    .main-container {
      flex-direction: column;
      margin-top: 30px;
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