<template>
  <div>
    <div class="main-container">
      <div>
        <div class="media-center">
          <p class="image is-128x128">
            <img class="rounded-image" :src="user.picture">
          </p>
        </div>
        <label 
          style="margin-top: 15px;" 
          class = "register-label" v-if="isSerbian"> *Ime: </label>
        <label 
          style="margin-top: 15px;" 
          class = "register-label" v-else> *Name: </label>
        <div class="field">
          <input
            v-if="isSerbian" 
            class="input is-medium"
            type="text"
            placeholder="Ime"
            v-model="changedUser.firstName"
          >
          <input
            v-else 
            class="input is-medium"
            type="text"
            placeholder="Name"
            v-model="changedUser.firstName"
          >
        </div>

        <label 
          style="margin-top: 15px;" 
          class = "register-label" v-if="isSerbian"> *Prezime: </label>
        <label 
          style="margin-top: 15px;" 
          class = "register-label" v-else> *Last name: </label>
        <div class="field">
          <input 
            v-if="isSerbian"
            class="input is-medium"
            type="text"
            placeholder="Prezime"
            v-model="changedUser.lastName"
          >
          <input 
            v-else
            class="input is-medium"
            type="text"
            placeholder="Last name"
            v-model="changedUser.lastName"
          >
        </div>
      </div>
      <div class="personal-info">
        <b-list-group >
          <b-list-group-item>
            <span v-if="isSerbian"
              class="info-title" 
            > Lični podaci </span>
            <span v-else
              class="info-title" 
            > Personal info </span>
            <div>
              <b-button class="button is-primary" style="margin-right:5px;" @click="saveChanges()">
                <strong v-if="isSerbian">Sačuvaj izmene</strong>
                <strong v-else>Save changes</strong>
              </b-button>
            </div>
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/call.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <div class="list-value">
              <div class="phones" v-for="p in firstElements('phone')" :key="p">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('phone', p)"> 
                {{ p }}
              </div>
              <div>
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('phone', lastElement('phone'))"> 
                <span v-text="lastElement('phone')"></span>
              </div>
              <div class = "control" style="margin-top: 10px">
                <label class = "register-label" v-if="isSerbian"> Dodajte broj telefona: </label>
                <label class = "register-label" v-else> Add a phone number: </label>
                <div class="flex-row-elements">
                  <VuePhoneNumberInput v-model="newPhoneNumber"/>
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
              style = "margin-right: 15px"
            />
            <div class="list-value">
              <div class="address" v-for="a in firstElements('homeAddress')" :key="a">
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('homeAddress', a)">
                {{ a }}
              </div>
              <div>
                <img src="@/assets/remove.svg" height="15" width="15" class="acc-or-remove-icon" @click="removeElement('homeAddress', lastElement('homeAddress'))"> 
                <span v-text="lastElement('homeAddress')"> </span>
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
      changedUser: {...this.user},
      newPhoneNumber: "",
      newAddress: ""
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
    }
  },
  methods: {
    removeElement(resource, element) {
      const index = this.changedUser[resource].findIndex(e => e === element);
      this.changedUser[resource].splice(index, 1);
    },
    addElement(resource, element) {
      if(element != "")
        this.user[resource].push(element)
      if(resource == 'homeAddress')
        this.newAddress = ""
      else this.newPhone = ""
    },
    firstElements(resource) {
      const lastIndex = this.user[resource].length;
      const arrayCopy = [...this.user[resource]];
      arrayCopy.splice(lastIndex-1, 1);
      return arrayCopy;
    },
    lastElement(resource) {
      return this.user[resource][this.user[resource].length-1];
    },
    saveChanges() {
      console.log(this.changedUser)
      this.$store.dispatch('editUser', this.changedUser)
      this.$emit('saveEditChanges')
    }
  }
}
</script>

<style scoped>
  
  .list-group-item {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    border: hidden;
    border-bottom: 1px solid lightgray;
    border-radius: 0px;
    align-items: center;
    min-width: 30px;
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
    margin-left: 30px;
  }

  .main-container {
    margin: 30px;
    margin-left: 150px;
    margin-right: 150px;
    display: flex;
    align-items: flex-start;
    flex-direction: row;
    align-items:flex-start;
    border-radius: 10px;
    flex-wrap: wrap;
    background-color: white;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
  }

  .content {
    background-color: black;
    font-size: 20px;
    color:white;
    text-align: center;
    margin-top: 15px;
    margin-bottom: 2px;
    border-radius: 5px;
  }

  .info-title {
    font-size: 25px;
    font-weight: bold;
    color: black;
    margin-bottom: 5px;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
  }

  .personal-info {
    display:flex;
    flex-direction: column;
    min-width: 50%;
    margin-left: 15px;
  }

  .flex-row-elements {
    display: flex;
  }

  .acc-or-remove-icon {
    margin: 0 10px 0 10px; 
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

</style>