<template>
  <transition name="modal">
    <div class="modal-mask-report">
      <div class="modal-wrapper">
        <div class="modal-container">

            <div class="modal-header">
                <h3 name="header" v-if="isSerbian">
                    {{naslovS}}
                </h3>
                <h3 name="header" v-else>
                    {{naslovE}}
                </h3>
            </div>

            <div class="modal-body">
                <slot name="body">

                <slot v-if="isSerbian"> {{tekstS}}</slot>
                  <slot v-else> {{tekstE}}</slot>
                </slot>
            </div>

            <div class="modal-footer">
                <slot name="footer">
                    <button type="button" class="btn btn-primary" @click="$emit('yes')" v-if="isSerbian">
                        Da
                    </button>
                    <button type="button" class="btn btn-primary" @click="$emit('yes')" v-else>
                        Yes
                    </button>
                    <button type="button" class="btn btn-secondary" @click="$emit('close')" v-if="isSerbian">
                        Odustani
                    </button>
                    <button type="button" class="btn btn-secondary" @click="$emit('close')" v-else>
                        Cancel
                    </button>
                </slot>
            </div>
        </div>
      </div>
    </div>
  </transition>
  
</template>

<script>
export default {
  props:
    {
      naslovS:
      {
        type: String,
        required: true
      },
      naslovE:
      {
        type: String,
        required: true
      },
      tekstS:
      {
        type: String,
        required: true
      },
      tekstE:
      {
        type: String,
        required: true
      }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
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

  .txt-area {
    margin-top:40px;
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