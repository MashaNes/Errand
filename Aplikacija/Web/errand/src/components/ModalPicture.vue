<template>
<transition name="modal">
        <div class="modal-mask">
          <div class="modal-wrapper" @click="checkClose">
            <div class="modal-container" id="closable">

              <div class="modal-header">
                <h3 name="header" v-if="isSerbian" class="uspeh">
                  <img class="remove" src="@/assets/remove.svg" height="20" width="20" @click="$emit('shrinkPicture')"> 
                </h3>
              </div>

              <div class="modal-body">
                <slot name="body">
                    <img :src="'data:;base64,' + picture" class="content-pic" />
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
        picture: {
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
    created() {
    },
    methods: {
        noAction() {
            return
        },
        checkClose(e) {
            let wrap = document.getElementsByClassName('modal-wrapper')[0]
            if(e.target == wrap)
                this.$emit("shrinkPicture")
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
        z-index: 9999999999999999998;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: table;
        transition: opacity 0.3s ease;
    }

    /* @media only screen and (max-width: 600px)
    {
        .modal-mask 
        {
            height: 120%;
        }
    } */

    .modal-wrapper {
        display: table-cell;
        vertical-align: middle;
        height: 100%;
    }

    .modal-container {
        width: fit-content;
        max-width: 70%;
        min-width: 450px;
        background-color: rgb(255, 209, 109);
        margin: 0px auto;
        border-radius: 2px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
        transition: all 0.3s ease;
        font-family: Helvetica, Arial, sans-serif;
        height: fit-content;
        max-height: 90%;
        display: flex;
        flex-direction: column;
    }

    .modal-header{
        margin-top: 0;
        color: rgb(41, 80, 207);
        font-size: 18px;
        border: hidden;
        padding: 5px 5px 0px 0px;
        display: flex;
        align-items: flex-end;
        justify-content: flex-end;
    }

    .modal-body {
        padding: 0px;
        margin: 0px;
        margin-top: 5px;
        max-height: 400px;
        overflow-y: auto;
        width: fit-content;
        width:100%;
        height: 100%;
        display: flex;
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

    .content-pic {
        width: 100%;
        max-height: 100%;
        object-fit: contain;
    }

    .remove:hover {
        cursor: pointer;
        height: 100%;
    }
</style>