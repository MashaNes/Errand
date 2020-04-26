<template>
  <div>
    <div class="main-container">
      <div class="picture-side">
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
            size="lg"
            class="ocene-dostignuca button is-primary"
            :to="'/achAndRatings'"
          >
            <span v-if="isSerbian"> Ocene i dostignuća </span>
            <span v-else> Ratings and achievements </span>
          </b-button>
        </div>
      </div>
      <div class="personal-info">
        <b-list-group>
          <b-list-group-item class="l-group-title">
            <span v-if="isSerbian"
              class="info-title" 
            > Lični podaci </span>
            <span v-else
              class="info-title" 
            > Personal info </span>
            <div class="l-group-btns">
              <b-button 
                class="button is-primary title-btn"
                @click="goToProfileEdit()"
              >
                <strong v-if="isSerbian">Izmeni</strong>
                <strong v-else>Edit</strong>
              </b-button>
              <b-button 
                class="button is-primary title-btn"
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
  
  .list-group {
    background-color:inherit;
  }

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
    margin-left: 30px;
  }

  .main-container {
    margin-top: 30px;
    margin-left: 10%;
    margin-right: 10%;
    display: flex;
    flex-direction: row;
    align-items:flex-start;
    border-radius: 10px;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height: 250px;
    width:250px;
    object-fit:cover;
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
    flex-grow:1;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
  }

  .personal-info {
    display:flex;
    flex-direction: column;
    margin-left: 15px;
    flex-grow:2;
    margin-bottom: 30px;
  }

  .picture-side {
    flex-grow:1;
    margin-bottom: 30px;
  }

  .nav-buttons {
    display:flex;
    width: 100%;
    justify-content: center;
  }

  .ocene-dostignuca {
    margin: 10% 0 20px 0; 
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

  .l-group-title {
    display:flex;
    flex-direction:row;
    justify-content: space-between;
  }

  .title-btn {
    margin:5px 5px 0 5px;
  }

  .media-center
  {
    display: flex;
    flex-direction: row;
    justify-content: center;
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
      font-size: 10px;
    }

    .list-value {
      flex-grow: 2;
      flex-shrink: 1;
      text-align: left;
      font-size: 17px;
      margin-left: 2%;
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

    @media only screen and (max-width:600px)
    {
      .list-value {
        word-break:break-all;
      }
    }
  }


</style>