<template>
    <div class="new-service-wrapper">
        <div class="label-div">
            <img src = "../assets/serbia.svg" class="zastava">
            Naziv:
        </div>
        <input class="text-div" v-model="newService.service_type_sr" />
        <div class="label-div">
            <img src = "../assets/uk.svg" class="zastava">
            Name:
        </div>
        <input class="text-div" v-model="newService.service_type_en" />
        <div class="label-div">
            <img src = "../assets/serbia.svg" class="zastava">
            Opis:
        </div>
        <textarea class="text-div" v-model="newService.description_sr"> </textarea>
        <div class="label-div">
            <img src = "../assets/uk.svg" class="zastava">
            Description:
        </div>
        <textarea class="text-div" v-model="newService.description_en"> </textarea>
        <div class="button-div">
            <button type="button" class="btn btn-danger dugme" @click="odustani">
                <img src="../assets/failed.svg" class="slika">
                <span v-if="isSerbian"> Odustani </span>
                <span v-else> Cancel </span>
            </button>
            <button type="button" class="btn btn-success" @click="sacuvaj" 
                    :disabled="newService.service_type_sr == '' || newService.service_type_en == '' ||
                    newService.description_sr == '' || newService.description_en == ''">
                <img src="../assets/finished.svg" class="slika">
                <span v-if="isSerbian"> Kreiraj novu uslugu </span>
                <span v-else> Create a new service </span>
            </button>
        </div>
        <ModalAreYouSure :naslovS="'Dodavanje usluge'" 
                         :tekstS="'Da li ste sigurni da želite da kreirate ovu uslugu?'" 
                         :naslovE="'Adding a service'"
                         :tekstE="'Are you sure you want to create this service?'"
                         @close="showModal = false" @yes="createService" v-if="showModal"/>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    data()
    {
        return{
            newService:
            {
                service_type_sr : "",
                service_type_en : "",
                description_sr: "",
                description_en: "",
                id: -1
            },
            showModal: false
        }
    },
    components:
    {
        ModalAreYouSure
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
        odustani()
        {
            this.$emit('zatvori')
        },
        sacuvaj()
        {
            this.showModal = true
        },
        createService()
        {
            //posalji add ka bazi koji ce kad se okonca da posalje ponovno pribavljanje servisa
            this.$store.state.allServices.unshift(this.newService)
            this.$emit('zatvori')
        }
    }
}
</script>

<style scoped>
    .new-service-wrapper
    {
        width:600px;
        display: flex;
        flex-direction: column;
    }

    .label-div
    {
        font-weight: 700;
        font-size: 18px;
    }

    .text-div
    {
        width:100%;
        text-align: justify;
        margin-bottom:7px;
        height: fit-content;
    }

    .button-div
    {
        margin:7px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-around;
    }

    .zastava
    {
        height: 20px;
        margin-right: 5px;
    }

    @media only screen and (max-width: 450px)
    {
        .new-service-wrapper
        {
            width:98%;
        }

        .button-div
        {
            flex-direction: column;
        }
        
        .dugme
        {
            margin-bottom: 10px;
        }
    }
</style>