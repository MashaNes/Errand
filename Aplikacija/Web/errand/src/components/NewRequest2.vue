<template>
    <div class="wrapper-step2">
        <div class="labelDiv">
            <span v-if="isSerbian"> Finalna adresa: </span>
            <span v-else> Final address: </span>
        </div>
        <div class="mapDiv">
            <!-- Map goes here -->
            <!-- Kad god se menja adresa pozovi addressChanged-->
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
        <!-- isti komentar kao i u 'NewTask', slobodno prestilizuj ove div-ove;
        važno je da ostane @click i funkcija removeAddress -->

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
        }
    },
    data()
    {
        return{
            myAddress: this.address
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
        addressChanged(address)
        {
            this.myAddress = address
            this.$emit("addressChanged", this.myAddress)
        },
        removeAddress()
        {
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
        margin-bottom: 5px;
    }

    .mapDiv
    {
        width:100%; /* lupljena visina */

    }

    .chosen-address-div
    {
        align-self: flex-start;
        display: flex;
        font-size: 16px;
        font-weight: 600;
        width: fit-content;
        border: 1px solid black;
        padding: 5px;
        border-radius: 5px;
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
</style>