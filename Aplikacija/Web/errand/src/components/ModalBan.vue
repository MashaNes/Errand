<template>
<transition name="modal">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-container">

              <div class="modal-header">
                <h3 name="header" v-if="isSerbian">
                  Određivanje zabrane
                </h3>
                <h3 name="header" v-else>
                  Banning users
                </h3>
              </div>

              <div class="modal-body">
                <slot name="body">
                  <div class="row-order">
                      <span v-if="isSerbian"> Zabrana pošiljocu </span>
                      <span v-else> Ban the sender </span>
                      <input type="checkbox" v-model="information.sender.flag" class="cekiranje" />
                  </div>
                  <div class="row-order">
                      <input type="number" :min="1" :max="30" v-model="information.sender.number"  :disabled="!information.sender.flag"/>
                      <select v-model="information.sender.time" :disabled="!information.sender.flag">
                          <option :value="0"> 
                              <span v-if="isSerbian"> Dani </span>
                              <span v-else> Days </span>
                          </option>
                          <option :value="1"> 
                              <span v-if="isSerbian"> Meseci </span>
                              <span v-else> Months </span>
                          </option>
                          <option :value="2"> 
                              <span v-if="isSerbian"> Godine </span>
                              <span v-else> Years </span>
                          </option>
                      </select>
                  </div>
                  <div class="row-order">
                    <textarea class="komentar" v-model="information.sender.comment" :disabled="!information.sender.flag" />
                  </div>  
                  <div class="row-order">
                      <span v-if="isSerbian"> Zabrana prijavljenom </span>
                      <span v-else> Ban the reported </span>
                      <input type="checkbox" v-model="information.reported.flag" class="cekiranje" />
                  </div>
                  <div class="row-order">
                      <input type="number" :min="1" :max="30" v-model="information.reported.number"  :disabled="!information.reported.flag"/>
                      <select v-model="information.reported.time" :disabled="!information.reported.flag">
                          <option :value="0"> 
                              <span v-if="isSerbian"> Dani </span>
                              <span v-else> Days </span>
                          </option>
                          <option :value="1"> 
                              <span v-if="isSerbian"> Meseci </span>
                              <span v-else> Months </span>
                          </option>
                          <option :value="2"> 
                              <span v-if="isSerbian"> Godine </span>
                              <span v-else> Years </span>
                          </option>
                      </select>
                  </div>
                  <div class="row-order">
                    <textarea class="komentar" v-model="information.reported.comment" :disabled="!information.reported.flag" />
                  </div>
                </slot>
              </div>

              <div class="modal-footer">
                <slot name="footer">
                  <button type="button" class="btn btn-danger" @click="ban" 
                          v-if="isSerbian" 
                          :disabled="(information.sender.flag && (information.sender.number < 1 || information.sender.comment == '')) 
                          || (information.reported.flag && (information.reported.number < 1 || information.reported.comment == ''))
                          || (!information.sender.flag && !information.reported.flag)">
                    Zabrani
                  </button>
                  <button type="button" class="btn btn-danger" @click="ban" 
                          v-else 
                          :disabled="(information.sender.flag && (information.sender.number < 1 || information.sender.comment == '')) 
                          || (information.reported.flag && (information.reported.number < 1 || information.reported.comment == ''))
                          || (!information.sender.flag && !information.reported.flag)">
                    Ban
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
    data()
    {
        return{
            information:
            {
                sender:
                {
                    flag: false,
                    number: 1,
                    time: 0,
                    comment:""
                },
                reported:
                {
                    flag: false,
                    number: 1,
                    time: 0,
                    comment:""
                }
            }
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        }
    },
    methods:
    {
        ban()
        {
            this.$emit('yes', this.information)
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
    display: flex;
    flex-direction: column;
    align-items: center;
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

    .row-order
    {
        display: flex;
        flex-direction: row;
        width:100%;
        align-items: center;
        justify-content: space-evenly;
        margin-bottom: 5px;
    }

    .cekiranje
    {
        width:18px;
        height:18px;
    }
</style>