<template>

  <transition name="modal">
    <div class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">

          <div class="modal-header">
            <h3 name="header" v-if="isSerbian">
              Ocenite korisnika
            </h3>
            <h3 name="header" v-else>
              Rate user
            </h3>
          </div>

          <div class="modal-body">
            <slot name="body">

              <span class="grade-label" v-text="isSerbian ? 'Ocena:' : 'Grade:'"></span>
              
              <vue-slider
                :interval="1"
                :min="1"
                :max="5"
                :marks="true"
                tooltip="active"
                v-model="rating.grade"
              />

              <b-form-textarea 
                class="txt-area"
                id="textarea"
                :placeholder="isSerbian ? 'Unesite kratak komentar...': 'Add a short comment...'"
                rows="3"
                no-resize
                v-model="rating.comment"
                :maxlength="maxChar"
                @blur="$v.rating.comment.$touch()"
              >
              </b-form-textarea>

              <span 
                v-if="$v.rating.comment.$error" 
                class='text-danger'
                v-text="isSerbian ? 'Morate uneti komentar' : 'You must enter a comment'"
              ></span>

              <span 
                v-if="rating.comment.length == maxChar" 
                class='text-danger max-char'
                v-text="isSerbian ? 'Uneli ste maksimalan broj karaktera' : 'You entered the maximum number of characters'"
              >
              </span>
            </slot>
          </div>

          <div class="modal-footer">
            <slot name="footer">
              <button 
                type="button" 
                @click="tryToRate"
                class="btn btn-primary"
                v-text="isSerbian ? 'Oceni' : 'Rate'"
                :disabled="isInvalid"
              >
              </button>

              <button 
                type="button" 
                class="btn btn-secondary" 
                @click="$emit('close')"
                v-text="isSerbian ? 'Odustani' : 'Cancel'"
              >
              </button>
            </slot>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>

import {required} from "vuelidate/lib/validators"
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/default.css'


export default {
  components: {
    VueSlider
  },
  props: {
    userName: {
      type: String
    }
  },
  data() {
    return {
      rating: {
        grade:3,
        comment: ""
      },
      maxChar: 100,
      showModalAreYouSure: false
    }
  },
  validations: {
    rating: {
      comment: {required}
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
    },
    isInvalid() {
      return this.$v.rating.$invalid
    }
  },
  methods: {
    tryToRate() {
      this.$emit('tryToRateUser', this.rating)
    }
  }
}
</script>

<style scoped>

  .modal-mask {
    position: fixed;
    z-index: 9998;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: table;
    transition: opacity 0.3s ease;
  }

  .modal-wrapper {
    display: table-cell;
    vertical-align: middle;
  }

  .modal-container {
    width: 500px;
    margin: 0px auto;
    padding: 20px 30px;
    background-color: #fff;
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
    transition: all 0.3s ease;
    font-family: Helvetica, Arial, sans-serif;
  }

  @media only screen and (max-width: 600px)
  {
      .modal-mask 
      {
          height: 110%;
      }

      .modal-container {
        padding: 5px 10px;
        width:90%;
        min-width: 300px;
      }
  }

  .modal-header{
    margin-top: 0;
    color: rgb(41, 80, 207);
    font-size: 18px;
  }

  .modal-body {
    margin: 20px 0;
  }

  .modal-default-button {
    float: right;
  }

  .modal-enter {
    opacity: 0;
  }

  .modal-leave-active {
    opacity: 0;
  }

  .modal-enter .modal-container,
  .modal-leave-active .modal-container {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
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
    width:100%;
  }

  .btns {
    display: flex;
    flex-direction: row-reverse;
    margin: 10px 10px 0 0;
  }

  .action-btn {
    margin: 0 0 0 5px;
  }

  .vue-slider {
    margin-top: 30px;
  }

</style>