<template>
<transition name="modal">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-container">

              <div class="modal-header">
                <h3 name="header" v-if="isSerbian" class="uspeh">
                  Uspešno
                </h3>
                <h3 name="header" v-else class="uspeh">
                  Success
                </h3>
              </div>

              <div class="modal-body">
                <slot name="body">
                    <p v-if="isSerbian">
                        Korisnik uspešno dodat u listu povlašćenih korisnika
                    </p>
                    <p v-else>
                        User successfully added to the benefit list
                    </p>
                </slot>
              </div>

              <div class="modal-footer">
                <slot name="footer">
                  <button type="button" class="btn btn-primary" @click="$emit('close')">
                    OK
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
    .uspeh
    {
        color:green;
    }
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