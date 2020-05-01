<template>
<transition name="modal">
        <div class="modal-mask">
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
    },
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

    @media only screen and (max-width: 600px)
    {
        .modal-mask 
        {
            height: 110%;
        }
    }

    .modal-wrapper {
    display: table-cell;
    vertical-align: middle;
    }

    .modal-container {
    width: 300px;
    margin: 0px auto;
    padding: 20px 30px;
    background-color: #fff;
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
    transition: all 0.3s ease;
    font-family: Helvetica, Arial, sans-serif;
    }

    .modal-header{
    margin-top: 0;
    color: red;
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

    .polje
    {
        width: 600px;
        height: 120px;
        border: 3px solid #cccccc;
        padding: 5px;
        font-family: Tahoma, sans-serif;
        background-position: bottom right;
        background-repeat: no-repeat;
    }
</style>