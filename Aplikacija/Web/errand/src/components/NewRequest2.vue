<template>
    <div class="wrapper-step2">
        <div class="labelDiv">
            <span v-if="isSerbian"> Finalna adresa: </span>
            <span v-else> Final address: </span>
        </div>
        <div class="mapDiv">
            <AddAddressMap 
                :HasCloseButton="false"
                :AskAreYouSure="false"
                @close="addressChanged"
            />
        </div>
        <div class="chosen-address-div" v-if="myAddress.name != ''">
            <span v-if="isSerbian"> Izabrana adresa: </span>
            <span v-else> Chosen address: </span>
            <span class="address-name">{{myAddress.name}}</span>
            <img class="remove" src="@/assets/remove.svg" height="20" width="20" @click="removeAddress">
        </div>
        <div class="chosen-address-div" v-else>
            <span v-if="isSerbian"> Nije izabrana nijedna adresa </span>
            <span v-else> There is no chosen address </span>
        </div>
        <div class="row-divovi" v-if="myAddress.name != ''">
            <input type="checkbox" class="picture-required" v-model="myPictureRequired" @input="pictureRequiredChanged"/>
            <div class="labelDiv">
                <span v-if="isSerbian"> Potrebno je dostaviti sliku sa ove lokacije </span>
                <span v-else> Requires a picture to be taken at this location </span>
            </div>
        </div>
    </div>
</template>

<script>
import AddAddressMap from "@/components/AddAddressMap"

export default {
    components: {
        AddAddressMap
    },
    props:
    {
        address:
        {
            type: Object,
            required:true
        },
        picture_required:
        {
            type: Boolean,
            required:true
        }
    },
    data()
    {
        return{
            myAddress: this.address,
            myPictureRequired: this.picture_required
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
        pictureRequiredChanged()
        {
            this.$emit("pictureRequiredChanged", !this.myPictureRequired)
        },
        addressChanged(address)
        {
            this.myAddress = address
            this.$emit("addressChanged", this.myAddress)
        },
        removeAddress()
        {
            this.myPictureRequired = false
            this.$emit("pictureRequiredChanged", false)
            this.myAddress = {
                name: "",
                longitude: null,
                latitude: null
            }
        }
    }
}
</script>

<style scoped>
    .wrapper-step2
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .labelDiv
    {
        width:100%;
        padding-left:10px;
        display:flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items:center;
        font-size: 18px;
        font-weight: 600;
    }

    .mapDiv
    {
        width:100%;
    }

    .chosen-address-div
    {
        font-size: 16px;
        font-weight: 600;
        width: fit-content;
        border: 0.5px solid black;
        padding: 10px;
        border-radius: 5px;
        margin-top: 10px;
        margin-bottom:10px;
    }

    .address-name
    {
        font-weight: normal;
        padding-left: 5px;
        margin-right: 15px;
    }

    .remove:hover 
    {
        width: 22px;
        height: 22px;
        cursor: pointer;
    }

    .row-divovi
    {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: flex-start;
        align-items: center;
        margin-bottom: 15px;
        word-break:break-all;
    }

    .picture-required
    {
        margin-left: 15px;
        margin-right: 15px;
        height:20px;
        width:20px;
    }
</style>