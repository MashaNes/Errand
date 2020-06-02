<template>
  <transition name="modal">
    <div :class="[pictureExpanded ? 'big-z-index' : '', 'modal-mask-report']">
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
              <b-form-textarea
                class="txt-area"
                id="textarea"
                :placeholder="isSerbian ? 'Unesite obrazloženje...': 'Add an explanation...'"
                rows="4"
                no-resize
                v-model="comment"
                @blur="$v.comment.$touch()"
              >
              </b-form-textarea>

              <span 
                v-if="$v.comment.$error" 
                class='text-danger'
                v-text="isSerbian ? 'Morate uneti obrazloženje' : 'You must enter an explanation'"
              ></span>

              <div class="upload-pic-div">
                <span class="media-center" v-if="pictures.length > 0">
                  <p class="image is-128x128" v-for="(picture, ind) in pictures" :key="ind">
                    <img @click="expandPicture(ind)" class="rounded-image" :src="picture">
                    <img :src="require('@/assets/remove.svg')" class="remove-pic" @click="removePicture(ind)">
                  </p>
                </span>
                <span 
                  :class="['picture-msg', isDragged ? 'picture-msg-dragged' : '']"
                  @drop.prevent="onDrop"
                  @dragenter.prevent="onDragEnter"
                  @dragleave.prevent="onDragLeave" 
                  @dragover.prevent
                  @mouseenter.prevent="onDragEnter"
                  @mouseleave.prevent="onDragLeave"
                  @click="$refs.file.click()"
                >
                  <span v-if="isSerbian">Kliknite ovde da biste dodali sliku ili prevucite novu</span>
                  <span v-else>Click here to add a picture or drop a new one</span>
                </span>
              </div>
              
              <input type="file" ref="file" style="display: none" accept="image/*" @change="pictureSelected"/>
              
              <strong v-if="isSerbian"> Napomena: 
                <span> ako želite da prijavite problem vezan za konkretan zahtev, kliknite na opciju "Zahtevi" u meniju. Zatim, izaberite konkretan zahtev.  </span>
              </strong>
              <strong v-else> Note: 
                <span> if you want to report a problem about a specific request, select "Requests" from the menu. After that, choose a specific request.  </span>
              </strong>
            </slot>
          </div>

          <div class="modal-footer">
            <slot name="footer">
              <button 
                type="button" 
                @click="tryToReport"
                class="btn btn-primary"
                v-text="isSerbian ? 'Prijavi' : 'Report'"
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
      <LightBox :showThumbs="true" :media="mediaPictures" v-if="pictureExpanded && pictures.length > 0"  :startAt="clickedPicture" @onClosed="pictureExpanded = false"></LightBox>
      <ModalAreYouSure 
        :naslovS = "'Da li ste sigurni?'"
        :naslovE = "'Are you sure?'"
        :tekstS = "'Prijavljujete korisnika ' + fullUserName + '. Da li želite da potvrdite?'"
        :tekstE = "'You are reporting user ' + fullUserName + '. Do you wish to confirm?'"
        @yes="reportUser()"
        @close="showModalAreYouSure = false"
        v-if="showModalAreYouSure" 
      />
    </div>
  </transition>
  
</template>

<script>

import {required} from "vuelidate/lib/validators"
import LightBox from 'vue-image-lightbox'
import ModalAreYouSure from "@/components/ModalAreYouSure"

export default {
  components: {
    ModalAreYouSure,
    LightBox
  },
  props: {
    userToReport: {
      type: Object,
      required: true
    },
    request: {
      type: Object,
      required: false,
      default: null
    }
  },
  data() {
    return {
      comment: "",
      pictures: [],
      isDragged: false,
      dragCounter: 0,
      pictureExpanded: false,
      clickedPicture: 0,
      showModalAreYouSure: false
      //show: true
      //maxChar: 300
    }
  },
  validations: {
    comment: {required}
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.userToReport.first_name + " " +this.userToReport.last_name
    },
    isInvalid() {
      return this.$v.comment.$invalid
    },
    mediaPictures() {
      const retValue = []
      this.pictures.forEach(picture => {
        retValue.push({
          thumb: picture,
          src: picture
        })
      })
      return retValue
    }
  },
  methods: {
    tryToReport() {
      this.showModalAreYouSure = true
    },
    reportUser() {
      this.$emit('setMessages')
      const picsToSend = this.pictures.map(pic => {
        const splitted = pic.split(',')
        return {picture: splitted[1]}
      })

      const filters = {
        "reported_user" : this.userToReport.id,
        "comment" : this.comment,
        "request" : this.request ? this.request.id : null,
        "pictures" : picsToSend
      }
      this.$store.dispatch('addReport', filters)
      this.showModalAreYouSure = false
      this.$emit('close')
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
        reader.onload = (e) => this.pictures.push(e.target.result);

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
    },
    removePicture(ind) {
      this.pictures.splice(ind, 1);
      //this.picture = null
    },
    expandPicture(ind) {
      this.clickedPicture = ind
      this.pictureExpanded = true
    }
  }
}
</script>

<style scoped>

  .modal-mask-report {
    position: fixed;
    z-index: 9998;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    transition: opacity 0.3s ease;
    justify-content: center;
  }

  .big-z-index {
    z-index: 99999999999999999;
  }

  .modal-wrapper {
    display: table-cell;
    vertical-align: middle;
    display: flex;
    padding-top: 80px;
    height: 100%;
    align-items: center;
    justify-content: center;
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
    max-height: 90%;
    overflow-y: scroll;
  }

  @media only screen and (max-width: 600px)
  {

      .modal-container {
        padding: 5px 10px;
        width:90%;
        min-width: 300px;
      }
  }

  @media only screen and (max-width: 499px)
  {
      .modal-wrapper {
        padding-top: 95px;
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

  .is-128x128 {
    width: 70px;
    height: 70px;
    border-radius: 5px;
    margin: 0px 5px 5px 0px;
  }

  .rounded-image {
    border-radius: 5px;
    border: 1px solid black;
    height: 70px;
    width: 70px;
    object-fit:cover;
    position: relative;
    top:0;
    left:0; 
    cursor:pointer
  }

  .no-pointer {
    cursor: auto;
  }

  .transparent {
    position: absolute;
    opacity: 0;
  }

  .semi-transparent {
    position: absolute;
    opacity: 0.5;
  }

  .shown {
    visibility:visible;
  }

  .hidden {
    visibility:hidden;
    height: 0px;
  }

  .picture-msg {
    font-size: 16px;
    width: 100%;
    border: 2px solid transparent;
    border-radius: 5px;
    padding: 2px;
    margin: 0px -5px 0px -5px;
  }

  .picture-msg-dragged {
    border: 2px solid lightgrey;
    cursor: pointer;
    background-color: rgb(235, 230, 230);
    font-size: 16px;
    width: 100%;
    border-radius: 5px;
    padding: 2px;
  }

  .upload-pic-div {
    display: flex;
    flex-direction: column;
    width: fit-content;
    margin-bottom: 5px;
    margin-top: 15px;
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
    height:20px;
    width: 20px;
    margin: 2px 0 0 2px;
    top: 0;
    left:0;
    position: absolute;
    border:1px solid transparent;
    border-radius: 20px;
    background-color: red;
  }

  .remove-pic:hover {
    cursor: pointer;
    border: 1px solid grey;
  }

  .media-center {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    margin-bottom: 5px;
  }

</style>