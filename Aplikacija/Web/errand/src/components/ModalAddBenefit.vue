<template>
<transition name="modal">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-container">

              <div class="modal-header">
                <h3 name="header" v-if="isSerbian">
                  Dodaj korisnika u listu povlašćenih
                </h3>
                <h3 name="header" v-else>
                  Add the user to your benefit list
                </h3>
              </div>

              <div class="modal-body">
                <slot name="body">
                  <div class="kolona">
                    <div v-if="isSerbian"> Odaberi popust: <input type="number" min="1" max="100" v-model="benefit" class="unos"> %</div>
                    <div v-if="isSerbian && isInvalid" class="opasnost"> Popust mora da bude između 1 i 100 %</div>

                    <div v-if="!isSerbian"> Select a discount: <input type="number" min="1" max="100" v-model="benefit" class="unos"> %</div>
                    <div v-if="!isSerbian && isInvalid" class="opasnost"> Discount must be between 1 and 100 %</div>
                  </div>
                </slot>
              </div>

              <div class="modal-footer">
                <slot name="footer">
                  <button type="button" class="btn btn-primary" @click="dodajKorisnika" v-if="isSerbian" :disabled="isInvalid">
                    Dodaj
                  </button>
                  <button type="button" class="btn btn-primary" @click="dodajKorisnika" v-else :disabled="isInvalid">
                    Add
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
import {between} from "vuelidate/lib/validators"
export default {
    props:
    {
        user:
        {
            type: Object,
            required: true
        }
    },
    data()
    {
        return{
            benefit: this.$store.state.authUser.benefitDiscount
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        isInvalid()
        {
            return this.$v.benefit.$invalid
        }
    },
    validations:
    {
        benefit: { between: between(1,100)}
    },
    methods:
    {
        dodajKorisnika()
        {
            console.log(this.benefit)
            console.log(this.user)
            this.$emit('close')
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

    @media only screen and (max-width: 600px)
    {
        .modal-mask 
        {
            height: 120%;
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

    .kolona
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .opasnost
    {
        color: red;
        font-size: 14px;
    }

    .unos
    {
        margin: 3px;
        width:50px
    }
</style>