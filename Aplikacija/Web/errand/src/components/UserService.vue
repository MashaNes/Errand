<template>
    <div class="userService">
        <div class="editovanje" v-if="editable">
            <a class="link-editovanje" @click="showModal = true"> <img class="razdvoji" src="../assets/remove.svg"> </a>
            <a class="link-editovanje" @click="isEditing = true"> <img src="../assets/edit.svg"> </a>
        </div>
        <div class="telo">
            <p class="ime" v-if="isSerbian">{{userService.service.service_type_sr}}</p>
            <p class="ime" v-else>{{userService.service.service_type_en}}</p>
            <div class="grid-div">
                <div class="row-div fix">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Maksimalno rastojanje: </span>
                        <span v-else> Maximum distance: </span>
                    </div>
                    <div class="right-div"> 
                        <span v-if="!isEditing">{{userService.max_dist}} </span>
                        <input v-else type="number" class="input-polje" min="0.1" max="1000" step="0.1" v-model="maxDist">
                        km
                    </div>
                </div>
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Tip naplate: </span>
                        <span v-else> Payment type: </span>
                    </div>
                    <div class="right-div"> 
                        <span v-if="!isEditing"> {{paymentTypeString}} </span>
                        <select v-model="paymentType" v-else>
                            <option v-if="isSerbian" value=0>po satu</option>
                            <option v-if="isSerbian" value=1>po kilometru</option>
                            <option v-if="isSerbian" value=2>fiksno</option>
                            <option v-if="isSerbian" value=3>početna cena</option>
                            <option v-if="!isSerbian" value=0>per hour</option>
                            <option v-if="!isSerbian" value=1>per kilometer</option>
                            <option v-if="!isSerbian" value=2>fixed</option>
                            <option v-if="!isSerbian" value=3>starting price</option>
                        </select>
                    </div>
                </div>
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Cena: </span>
                        <span v-else> Payment amount: </span>
                    </div>
                    <div class="right-div"> 
                        <span v-if="!isEditing">{{userService.payment_ammount}} </span>
                        <input v-else type="number" class="input-polje vece" min="0" step="100" v-model="paymentAmount">
                        rsd
                    </div>
                </div>
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Minimalna ocena: </span>
                        <span v-else> Minimum rating: </span>
                    </div>
                    <div class="right-div"> 
                        <span v-if="!isEditing"> {{userService.min_rating}} </span>
                        <span v-else class="ocena-span"><input type="range" class="form-control-range skala" id="formControlRange" v-model="scale"> {{minRating}} </span>
                    </div>
                </div>
                <div class="row-div" v-if="isEditing">
                    <div class="left-div"> 
                        <button type="button" class="btn btn-primary dugmeS" v-if="isSerbian" @click="saveChanges" :disabled="isInvalid">Sačuvaj</button>
                        <button type="button" class="btn btn-primary dugmeS" v-else @click="saveChanges" :disabled="isInvalid">Save</button>
                    </div>
                    <div class="right-div"> 
                        <button type="button" class="btn btn-secondary dugmeS" @click="cancel" v-if="isSerbian">Odustani</button>
                        <button type="button" class="btn btn-secondary dugmeS" @click="cancel" v-else>Cancel</button>
                    </div>
                </div>
            </div>
        </div>
        <ModalAreYouSure :naslovS="'Uklanjanje usluge'" 
                         :tekstS="'Da li ste sigurni da želite da uklonite ovu uslugu?'" 
                         :naslovE="'Removing a service'"
                         :tekstE="'Are you sure you want to remove this service?'"
                         @close="showModal = false" @yes="removeService" v-if="showModal"/>
    </div>
</template>

<script>
import {between} from "vuelidate/lib/validators"
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    props:
    {
        editable:
        {
            type: Boolean,
            required: true
        },
        userService:
        {
            type: Object,
            required:true
        }
    },
    components:
    {
        ModalAreYouSure
    },
    data()
    {
        return{
            showModal: false,
            isEditing: false,
            maxDist: this.userService.max_dist,
            paymentType: this.userService.payment_type,
            paymentAmount: this.userService.payment_ammount,
            scale: (this.userService.min_rating - 1) * 25
        }
    },
    validations:
    {
        maxDist: { between : between(0.1,1000) },
        paymentAmount: {between: between(0,10000000000000000000) },
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        paymentTypeString()
        {
            if(this.isSerbian)
            {
                if(this.userService.payment_type == 0)
                    return "po satu"
                else if(this.userService.payment_type == 1)
                    return "po kilometru"
                else if(this.userService.payment_type == 1)
                    return "fiksna"
                else
                    return "početna"
            }
            else
            {
                if(this.userService.payment_type == 0)
                    return "per hour"
                else if(this.userService.payment_type == 1)
                    return "per kilometer"
                else if(this.userService.payment_type == 1)
                    return "fixed"
                else
                    return "starting"
            }
        },
        minRating()
        {
            return (Math.round((this.scale / 25 + 1) * 100) / 100).toFixed(2)
        },
        isInvalid()
        {
            return this.$v.maxDist.$invalid || this.$v.paymentAmount.$invalid
        }
    },
    methods:
    {
        removeService()
        {
            console.log(this.userService)
            this.showModal = false;
            this.$store.state.userServices.forEach((item, index) =>
            {
                if(item.id == this.userService.id)
                    this.$store.state.userServices.splice(index,1)
            })
            this.$store.state.services.unshift(this.userService.service)
            this.$store.dispatch("removeUserService", this.userService.id)
        },
        saveChanges()
        {
            this.userService.max_dist = this.maxDist
            this.userService.min_rating = this.minRating
            this.userService.payment_ammount = this.paymentAmount
            this.userService.payment_type = this.paymentType
            this.isEditing = false
            console.log(this.userService)
            this.$store.dispatch("updateUserService", this.userService)
        },
        cancel()
        {
            this.maxDist = this.userService.max_dist
            this.scale = (this.userService.min_rating - 1) * 25
            this.paymentAmount = this.userService.payment_ammount
            this.paymentType = this.userService.payment_type
            this.isEditing = false
        }
    }  
}
</script>

<style scoped>
    .userService
    {
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        justify-content: center;
        width: 380px;
        height: 215px;
        background-color: white;
        border: 1px solid grey;
        border-radius: 5px;
        margin: 10px;
        padding: 10px;
    }

    .telo
    {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        height: 100%;
    }

    .editovanje
    {
        height: 100%;
        width: fit-content;
        display: flex;
        flex-direction: column;
        align-content: flex-start;
    }

    .link-editovanje
    {
        height: fit-content;
        z-index: 1;
    }

    .razdvoji
    {
        margin-bottom: 15px;
    }

    .ime
    {
        font-size: 22px;
        font-weight: 750;
        color: rgb(48, 89, 165);
    }

    .grid-div
    {
        margin-left: 15px;
        height: 150px;
        width: 295px;
        padding-top: 7px;
        display: flex;
        flex-direction: column;
    }

    .row-div
    {
        flex-grow: 1;
        flex-shrink: 1;
        width: 100%;
        margin-bottom: 7px;
        align-items: center;
        display: flex;
        flex-direction: row;
    }

    .left-div
    {
        text-align: center;
        width: 180px;
        font-weight: 550;
    }

    .right-div
    {
        text-align: center;
        width: 115px;
    }

    .ocena-span
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }

    .dugmeS
    {
        height: 25px;
        padding-left: 10px;
        padding-right: 10px;
        padding-bottom: 25px;
        padding-top: 0px;
    }

    .input-polje
    {
        width:70px;
        margin-right: 7px;
        text-align: center;
        height: 25px;
    }

    .vece
    {
        width:80px;
    }
    
    .skala
    {
        width:100px;
        margin-right: 7px;
    }

    @media only screen and (max-width: 450px)
    {
        .userService
        {
            width: 110%;
            height: 235px;
        }

        .grid-div
        {
            margin-left: 5px;
        }

        .telo{
            width: 92%;
        }

        .left-div
        {
            width: 50%;
        }  

        .skala
        {
            width:90px;
            margin-right: 5px;
        } 
    }
</style>