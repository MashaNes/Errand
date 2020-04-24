<template>
  <div>
    <div class="main-container">
      <div>
        <div class="media-center">
          <p class="image is-128x128">
            <img class="rounded-image" :src="user.picture">
          </p>
        </div>
        <div class="content">
          {{ fullUserName }}
        </div>
        <div class="nav-buttons">
          <b-button 
            variant="success" 
            size="lg"
            class="ocene-dostignuca"
            :to="'/achAndRatings'"
          >
            <span v-if="isSerbian"> Ocene i dostignuća </span>
            <span v-else> Ratings and achievements </span>
          </b-button>
        </div>
      </div>
      <div class="personal-info">
        <b-list-group style="width:100%;">
          <b-list-group-item>
            <span v-if="isSerbian"
              class="info-title" 
            > Lični podaci </span>
            <span v-else
              class="info-title" 
            > Personal info </span>
            <div style="display:flex; flex-direction:row;">
              <b-button 
                class="button is-primary" 
                style="margin: 5px;"
                @click="goToProfileEdit()"
              >
                <strong v-if="isSerbian">Izmeni</strong>
                <strong v-else>Edit</strong>
              </b-button>
              <b-button 
                class="button is-primary"
                style="margin: 5px; width:100%;"
              >
                <strong v-if="isSerbian">Podešavanja</strong>
                <strong v-else>Settings</strong>
              </b-button>
            </div>
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/email.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <span class="list-value">{{ user.email }}</span>
          </b-list-group-item>
          <b-list-group-item>
            <img 
              src="@/assets/call.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <div class="list-value">
              <div class="phones" v-for="p in firstElements('phone')" :key="p">{{ p }}</div>
              <div v-text="lastElement('phone')"></div>
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
              <div class="addr-or-phone-item" v-for="a in firstElements('homeAddress')" :key="a">{{ a }}</div>
              <div v-text="lastElement('homeAddress')"></div>
            </div>
          </b-list-group-item>
        </b-list-group>
        <b-list-group style="width:85%">
          <b-list-group-item>
            <strong style="font-size:20px" v-if="isSerbian"> Prosečna ocena: </strong>
            <strong style="font-size:20px" v-else> Average rating: </strong>
            <b-progress class="mt-2" max="5.0" height="30px">
                <b-progress-bar 
                  :value="user.rating"
                  :variant="progressBarVariant"
                > 
                  <span style="font-size:20px; font-weight: 900; color:black;"> {{formattedRating}}</span> 
                </b-progress-bar>
            </b-progress>
          </b-list-group-item>
        </b-list-group>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    user: {
      required: true,
      type: Object
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
      return this.user.rating < 2.5 ? 'danger' : 
             this.user.rating < 4.5 ? 'warning' : 'success'
    },
    formattedRating() {
      return this.user.rating.toFixed(2);
    }
  },
  methods: {
    goToProfileEdit() {
      this.$emit("editProfile");
    },
    firstElements(resource) {
      const lastIndex = this.user[resource].length;
      const arrayCopy = [...this.user[resource]];
      arrayCopy.splice(lastIndex-1, 1);
      return arrayCopy;
    },
    lastElement(resource) {
      return this.user[resource][this.user[resource].length-1];
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
    margin-left: 15%;
    margin-right: 15%;
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

  .nav-buttons {
    display:flex;
    width: 100%;
    justify-content: center;
  }

  .ocene-dostignuca {
    margin: 40px 5px 0 0; 
    width: 100%;
    font-size: 20px;
    font-weight: bold;
  }

  .addr-or-phone-item {
    border-bottom:dotted 2px lightgray;
    margin-bottom: 7px;
    padding-bottom: 7px;
  }

  .phones {
    margin-bottom: 10px;
  }


</style>