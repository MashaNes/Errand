<template>

  <transition name="modal">
    <div class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">

          <div class="modal-header">
            <h3 name="header" v-if="isSerbian">
              Prijavite korisnika
            </h3>
            <h3 name="header" v-else>
              Report user
            </h3>
          </div>

          <div class="modal-body">
            <slot name="body">

              <span class="report-label" v-text="isSerbian ? 'Tip prijave:' : 'Report type:'"></span>
              <v-select :options="tmpTypes" label="reportType" v-model="report.reportType" class="select-type"></v-select> 
              
              
              <b-form-textarea 
                v-if="report.reportType"
                class="txt-area"
                id="textarea"
                :placeholder="isSerbian ? 'Unesite obrazloženje...': 'Add an explanation...'"
                rows="4"
                no-resize
                v-model="report.comment"
                :maxlength="maxChar"
                @blur="$v.report.comment.$touch()"
              >
              </b-form-textarea>

              <span 
                v-if="$v.report.comment.$error && report.reportType" 
                class='text-danger'
                v-text="isSerbian ? 'Morate uneti obrazloženje' : 'You must enter an explanation'"
              ></span>

              <span 
                v-if="report.comment.length == maxChar && report.reportType" 
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
                @click="tryToReport"
                class="btn btn-primary"
                v-text="isSerbian ? 'Prijavi' : 'Report'"
                :disabled="isInvalid || !report.reportType"
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
import vSelect from "vue-select"

export default {
  components: {
    vSelect
  },
  props: {
    userName: {
      type: String
    }
  },
  data() {
    return {
      report: {
        reportType: null,
        comment: ""
      },
      maxChar: 300,
      tmpTypes: ['tip1', 'tip2', 'tip3']
    }
  },
  validations: {
    report: {
      comment: {required},
      reportType: {required}
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    isInvalid() {
      return this.$v.report.$invalid
    }
  },
  methods: {
    tryToReport() {
      this.$emit('tryToReportUser', this.report)
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
    margin: 10px;
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
    margin-top:20px;
  }

  .grade-label {
    width:fit-content;
    font-size: 18px;
  }

  .text-danger {
    margin-top:10px;
    font-size:12px;
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

  .select-type {
    width:100%;
    margin-top:5px;
  }

</style>