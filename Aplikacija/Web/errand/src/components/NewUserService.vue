<template>
    <div class="wrapper-component">
        <div class="opcije"> 
            <img src="../assets/service.svg" class="ikonica">
            <label class="labela" v-if="isSerbian"> Nova usluga </label>
            <label class="labela" v-else> New service </label>
        </div>
        <div class="opcije"> 
            <v-select :options="services" label="serviceType" v-model="servis" class="selekt"></v-select> 
            <img src="../assets/info.svg" v-if="servis != null" v-b-popover.hover.bottom="servis.description">
        </div>
        <div class="opcije" v-if="servis != null">
            <label class="labela-stavka" v-if="isSerbian"> Maksimalno rastojanje: </label>
            <label class="labela-stavka" v-else> Maximum distance: </label>
            <input type="number" class="input-polje" min="0.1" max="1000" step="0.1" v-model="maxDistance"> km
        </div>
        <div class="opcije" v-if="servis != null">
            <label class="labela-stavka" v-if="isSerbian"> Tip naplate: </label>
            <label class="labela-stavka" v-else> Payment type: </label>
            <select v-model="paymentType">
                <option v-if="isSerbian">po satu</option>
                <option v-if="isSerbian">po kilometru</option>
                <option v-if="isSerbian">fiksno</option>
                <option v-if="isSerbian">početna cena</option>
                <option v-if="!isSerbian">per hour</option>
                <option v-if="!isSerbian">per kilometer</option>
                <option v-if="!isSerbian">fixed</option>
                <option v-if="!isSerbian">starting price</option>
            </select>
        </div>
        <div class="opcije" v-if="servis != null">
            <label class="labela-stavka" v-if="isSerbian"> Cena: </label>
            <label class="labela-stavka" v-else> Payment amount: </label>
            <input type="number" class="input-polje vece" min="0" step="100" v-model="paymentAmount"> rsd
        </div>
        <div class="opcije" v-if="servis != null">
            <label class="labela-stavka" v-if="isSerbian"> Minimalna ocena: </label>
            <label class="labela-stavka" v-else> Minimum rating: </label>
            <input type="range" class="form-control-range skala" id="formControlRange" v-model="scale"> {{minRating}}
        </div>
        <div class="opcije" v-if="servis != null">
            <button type="button" class="btn btn-primary dugme" v-if="isSerbian" :disabled="isInvalid" @click="dodaj">Dodaj</button>
            <button type="button" class="btn btn-primary dugme" v-else :disabled="isInvalid" @click="dodaj">Add</button>
            <button type="button" class="btn btn-secondary" v-if="isSerbian" @click="cancel">Odustani</button>
            <button type="button" class="btn btn-secondary"  @click="cancel" v-else>Cancel</button>
        </div>
    </div>
</template>

<script>
import {between} from "vuelidate/lib/validators"
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
export default {
    data()
    {
        return{
            servis: null,
            maxDistance: this.$store.state.authUser.max_dist,
            paymentType: null,
            paymentAmount: 0,
            scale: (this.$store.state.authUser.min_rating - 1) *25
        }
    },
    components:
    {
        vSelect
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        services()
        {
            return this.$store.state.services.services
        },
        minRating()
        {
            return (Math.round((this.scale / 25 + 1) * 100) / 100).toFixed(2)
        },
        isInvalid()
        {
            return this.$v.maxDistance.$invalid || this.$v.paymentAmount.$invalid || this.paymentType == null
        }
    },
    methods:
    {
        cancel()
        {
            this.servis = null
            this.maxDistance = this.$store.state.authUser.max_dist
            this.paymentType = null
            this.paymentAmount = 0
            this.scale = (this.$store.state.authUser.min_rating - 1) *25
        },
        dodaj()
        {
            console.log(this.servis)
            console.log(this.maxDistance)
            console.log(this.paymentType)
            console.log(this.paymentAmount)
            console.log(this.minRating)
            //dodati userService u listu UserService-a u store-u
            //poslati dodavanjanje u bazu
            //izbaciti ovaj servis iz liste servisa
            this.cancel();
        }
    },
    validations:
    {
        maxDistance: { between : between(0.1,1000) },
        paymentAmount: {between: between(0,10000000000000000000) },
    },
}
</script>

<style scoped>
    .wrapper-component
    {
        background-color: white;
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 15px;
    }

    .selekt
    {
        width: 80%;
        margin-right: 7px;
        background-color: white;
        word-break:break-all;
        background-color: rgb(236, 236, 236);
    }

    .labela
    {
        margin-left: 5px;
        font-size: 20px;
        font-weight: 700;
        margin-bottom: 0px;
    }

    .opcije
    {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: center;
        align-items: center;
        margin-bottom: 15px;
    }

    .input-polje
    {
        width:70px;
        margin-right: 7px;
        text-align: center;
        height: 25px;
    }

    .labela-stavka
    {
        margin-bottom: 0px;
        margin-right: 7px;
    }

    .ikonica
    {
        width: 30px;
        height: 30px;
        margin-right: 7px;
    }

    .vece
    {
        width:100px;
    }

    .skala
    {
        width:120px;
        margin-right: 7px;
    }

    .dugme
    {
        margin-right:15px
    }
</style>