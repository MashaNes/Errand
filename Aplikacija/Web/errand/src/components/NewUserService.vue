<template>
    <div class="wrapper-component">
        <div class="opcije"> 
            <img src="../assets/service.svg" class="ikonica">
            <label class="labela" v-if="isSerbian"> Nova usluga </label>
            <label class="labela" v-else> New service </label>
        </div>
        <div class="opcije"> 
            <v-select :options="services" label="service_type_sr" v-model="service" class="selekt" v-if="isSerbian"></v-select> 
            <v-select :options="services" label="service_type_en" v-model="service" class="selekt" v-else></v-select> 
            <img src="../assets/info.svg" v-if="service != null && isSerbian" v-b-popover.hover.bottom="service.description_sr">
            <img src="../assets/info.svg" v-if="service != null && !isSerbian" v-b-popover.hover.bottom="service.description_en">
        </div>
        <div class="opcije" v-if="service != null">
            <label class="labela-stavka" v-if="isSerbian"> Maksimalno rastojanje: </label>
            <label class="labela-stavka" v-else> Maximum distance: </label>
            <input type="number" class="input-polje" min="0.1" max="1000" step="0.1" v-model="max_dist"> km
        </div>
        <div class="opcije" v-if="service != null">
            <label class="labela-stavka" v-if="isSerbian"> Tip naplate: </label>
            <label class="labela-stavka" v-else> Payment type: </label>
            <select v-model="payment_type">
                <option v-if="isSerbian" value="0">po satu</option>
                <option v-if="isSerbian" value="1">po kilometru</option>
                <option v-if="isSerbian" value="2">fiksno</option>
                <option v-if="isSerbian" value="3">početna cena</option>
                <option v-if="!isSerbian" value="0">per hour</option>
                <option v-if="!isSerbian" value="1">per kilometer</option>
                <option v-if="!isSerbian" value="2">fixed</option>
                <option v-if="!isSerbian" value="3">starting price</option>
            </select>
        </div>
        <div class="opcije" v-if="service != null">
            <label class="labela-stavka" v-if="isSerbian"> Cena: </label>
            <label class="labela-stavka" v-else> Payment amount: </label>
            <input type="number" class="input-polje vece" min="0" step="100" v-model="payment_ammount"> rsd
        </div>
        <div class="opcije" v-if="service != null">
            <label class="labela-stavka" v-if="isSerbian"> Minimalna ocena: </label>
            <label class="labela-stavka" v-else> Minimum rating: </label>
            <input type="range" class="form-control-range skala" id="formControlRange" v-model="scale"> {{minRating}}
        </div>
        <div class="opcije" v-if="service != null">
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
            userService:
            {
                service: null,
                max_dist: this.$store.state.authUser.max_dist,
                payment_type: null,
                payment_ammount: 0,
                min_rating: this.$store.state.authUser.min_rating
            },
            scale: (this.$store.state.authUser.min_rating - 1) *25,
            service: null,
            max_dist: this.$store.state.authUser.max_dist,
            payment_type: null,
            payment_ammount: 0,
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
            return this.$store.state.services
        },
        minRating()
        {
            return (Math.round((this.scale / 25 + 1) * 100) / 100).toFixed(2)
        },
        isInvalid()
        {
            return this.$v.max_dist.$invalid || this.$v.payment_ammount.$invalid || this.payment_type == null
        }
    },
    methods:
    {
        cancel()
        {
            this.service = null
            this.max_dist = this.$store.state.authUser.max_dist
            this.payment_type = null
            this.payment_ammount = 0
            this.scale = (this.$store.state.authUser.min_rating - 1) *25
        },
        dodaj()
        {
            this.userService.min_rating = this.minRating
            this.userService.max_dist = this.max_dist
            this.userService.payment_type = this.payment_type
            this.userService.payment_ammount = this.payment_ammount
            this.userService.service = this.service
            this.$store.state.userServices.push(this.userService)
            this.$store.dispatch("addUserService", this.userService)
            this.$store.state.services.forEach((item, index) =>
            {
                if(item.id == this.userService.service.id)
                    this.$store.state.services.splice(index,1)
            })
            this.cancel();
        }
    },
    validations:
    {

        max_dist: { between : between(0.1,1000) },
        payment_ammount: {between: between(0,10000000000000000000) }
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