<template>
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
    </div>
    <div class="personal-info">
      <div class="grade-title">
        <span style="margin-right:10px;" v-text="isSerbian ? 'Ocenite korisnika ': 'Rate user '"></span>
        <span> {{fullUserName}} </span>
      </div>
      <span class="grade-label" v-text="isSerbian ? 'Ocena:' : 'Grade:'"></span>

      <vue-slider
        :interval="1"
        :min="1"
        :max="5"
        :marks="true"
        v-model="rating.grade"
      />

      <b-form-textarea 
        class="txt-area"
        id="textarea"
        :placeholder="isSerbian ? 'Unesite kratak komentar...': 'Add a short comment...'"
        rows="3"
        max-rows="6"
        no-resize
        v-model="rating.comment"
        tooltip="active"
        :maxlength="maxChar"
      >
      </b-form-textarea>

      <span 
        v-if="rating.comment.length == maxChar" 
        class='text-danger max-char'
        v-text="isSerbian ? 'Uneli ste maksimalan broj karaktera' : 'You entered the maximum number of characters'"
      >
      </span>

      <div class="btns">
        <b-button   
          class="button is-primary action-btn"
          @click="$emit('cancelRate')"
        > <strong v-text="isSerbian ? 'Odustani' : 'Cancel'"></strong>  </b-button>
        <b-button   
          class="button is-primary action-btn"
          @click="showModalAreYouSure = true"
        > <strong v-text="isSerbian ? 'Potvrdi' : 'Confirm'"></strong> </b-button>
        <b-button   
          class="button is-primary action-btn"
          @click="showModalChooseRequest = true"
        > <strong v-text="isSerbian ? 'Izaberi zahtev' : 'Choose request'"></strong> </b-button>
      </div>
    </div>
    <ModalAreYouSure 
      :naslovS="'Potvrda ocene'" 
      :tekstS="'Da li ste sigurni da želite da ocenite korisnika?'" 
      :naslovE="'Confirm rating'"
      :tekstE="'Are you sure you want to rate this user?'"
      @close="showModalAreYouSure = false" @yes="rateUser" v-if="showModalAreYouSure"
    />
  </div>
</template>

<script>

import {maxLength, required} from "vuelidate/lib/validators"
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/default.css'
import ModalAreYouSure from "@/components/ModalAreYouSure"


export default {
  components: {
    VueSlider,
    ModalAreYouSure
  },
  props: {
    user: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      rating: {
        grade:1,
        comment: ""
      },
      maxChar: 20,
      numOfMaxEnters: 0,
      showMaxEnterWarning: false,
      showModalAreYouSure: false,
      showModalChooseRequest: false
    }
  },
  validations: {
    rating: {
      comment: {required, maxLength: maxLength(50)}
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    requests() {
      return this.$store.state.specificRequests
    }
  },
  methods: {
    cancel() {
      this.$emit('cancelRate')
    },
    rateUser() {
      this.$store.dispatch('')
    }
  },
  created() {
    const userName = this.user.firstName
    console.log(userName)
    this.$store.dispatch('fillSpecificRequests', userName)
  }
}
</script>

<style scoped>

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
    word-break: break-all;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
  }

  .personal-info {
    display:flex;
    flex-direction: column;
    flex-grow:1;
    margin: 30px 15px 0 15px;
  }

  .picture-side {
    flex-grow:1;
    margin-bottom: 30px;
  }

  .media-center {
    display:flex;
    flex-direction:column;
    align-items:center;
  }

  .txt-area {
    margin-top:40px;
  }

  .grade-title {
    font-size: 25px;
    font-weight: bold;
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    word-break:break-all;
    width:fit-content;
  }

  .grade-label {
    width:fit-content;
    margin-bottom: 40px;
  }

  .max-char {
    margin-top:10px;
  }

  .form-control {
    width:auto;
  }

  .btns {
    display: flex;
    flex-direction: row-reverse;
    margin: 10px 10px 0 0;
  }

  .action-btn {
    margin: 0 0 0 5px;
  }

  .requests-div {
    flex-grow:1;
    display:flex;
    flex-direction: column;
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

    .personal-info {
      align-self:stretch;
      margin: 10px 10px 40px 10px;
    }

    .slide {
      margin: 0 10px 0 10px;
    }

    .grade-title {
      margin: 0 10px 20px 10px;
    }

    .grade-label {
      margin: 0 10px 40px 10px;
    }

    .vue-slider {
      margin: 0 10px 0 10px;
    }



    .txt-area {
      margin: 40px 10px 0 10px;
      align-self:stretch;
    }

    .max-char {
      margin: 10px 10px 0 10px;
    }
  }


</style>