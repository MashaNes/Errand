<template>
    <div class="page-div">
        <div class="content-div">
            <div class = "slika-div"> <img class = "slika" src="../assets/settings.png"> </div>
            <div class="settings-main"> 
                <div class="settings-element"> 
                    <div class="settings-row left m1" :class="{'e1':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverRatingText">
                        <span v-if="isSerbian"> Minimalni rejting: </span>
                        <span v-else> Minimal rating: </span>
                    </div>
                    <div class="settings-row">
                        <input type="range" class="form-control-range skala" id="formControlRange" v-model="scale" v-if="isEditing">
                        <span class="brojka" v-if="isEditing"> {{minRating}}</span>
                        <span class="brojka fs1" v-else> {{minRating}}</span>
                    </div>
                </div>
                <div class="settings-element"> 
                    <div class="settings-row left m2" :class="{'e2':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverDistanceText">
                        <span v-if="isSerbian"> Maksimalno rastojanje: </span>
                        <span v-else> Maximum distance: </span>
                    </div>
                    <div class = "vuelidate-div">
                        <div class="settings-row">
                            <input type="number" class="skala" v-model="maxDistance" min="0.1" max="1000" step="0.1" :disabled="!IsMaxDistance" 
                                   :class="{'ne-valja' : isMaxDistInvalid}"
                                   @blur="MaxDistReset"
                                   v-if="isEditing">
                            <div class="brojka fs2" v-else> {{maxDistance}} </div>
                            <span class="brojka"> km </span>
                            <!--<input class = "checkBox" type="checkbox" v-model="IsMaxDistance">-->
                        </div>
                        <label v-if="isMaxDistInvalid && isSerbian" class="danger">Između 0.1 i 1000</label>
                        <label v-if="isMaxDistInvalid && !isSerbian" class="danger">Between 0.1 and 1000</label>
                    </div>
                </div>
                <div class="settings-element"> 
                    <div class="settings-row left m3" :class="{'e3':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverDiscountText" >
                        <span v-if="isSerbian"> Popust: </span>
                        <span v-else> Discount: </span>
                    </div>
                    <div class = "vuelidate-div">
                        <div class="settings-row">
                            <input type="number" class="skala" v-model="discount" min="0" max="100" step="5"
                                   :class="{'ne-valja' : isDiscountInvalid}"
                                   @blur="DiscountReset"
                                   v-if="isEditing">
                            <div class="brojka fs3" v-else> {{discount}} </div>
                            <span class="brojka"> % </span>
                        </div>
                        <label v-if="isDiscountInvalid && isSerbian" class="danger">Između 0 i 100</label>
                        <label v-if="isDiscountInvalid && !isSerbian" class="danger">Between 0 and 100</label>
                    </div>
                </div>
                <div class="settings-element"> 
                    <div class="settings-row left m4" :class="{'e4':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverCooperationText">
                        <span v-if="isSerbian"> Broj saradnji za ostvarenje popusta: </span>
                        <span v-else> Number of cooperations for discount: </span>
                    </div>
                    <div class = "vuelidate-div">
                        <div class="settings-row">
                            <input type="number" class="skala" v-model="brSaradnji" min="0" max="20" step="1"
                                   :class="{'ne-valja' : isBrSaradnjiInvalid}"
                                   @blur="BrSaradnjiReset"
                                   v-if="isEditing">
                            <div class="brojka" v-else> {{brSaradnji}} </div>
                            <span class="skriveno"> % </span>
                        </div>
                        <label v-if="isBrSaradnjiInvalid && isSerbian" class="danger">Između 0 i 20</label>
                        <label v-if="isBrSaradnjiInvalid && !isSerbian" class="danger">Between 0 and 20</label>
                    </div>
                </div>
                <div class="button-element">
                    <button v-if="!isEditing && isSerbian" type="button" class="btn btn-primary dugme-edit" @click="isEditing = true">Izmeni</button>
                    <button v-if="!isEditing && !isSerbian" type="button" class="btn btn-primary dugme-edit" @click="isEditing = true">Edit</button>
                    <button v-if="isEditing && isSerbian" type="button" class="btn btn-primary dugme-edit" @click="save">Sačuvaj</button>
                    <button v-if="isEditing && !isSerbian" type="button" class="btn btn-primary dugme-edit" @click="save">Save</button>
                    <button v-if="isEditing && isSerbian" type="button" class="btn btn-secondary dugme-edit" @click="cancel">Odustani</button>
                    <button v-if="isEditing && !isSerbian" type="button" class="btn btn-secondary dugme-edit" @click="cancel">Cancel</button>
                </div>
            </div>
            <div class="dugmici"> 
                <router-link :to="'/benefitList'" type="button" class="btn btn-info dugme" v-if="isSerbian">Lista povlašćenih korisnika</router-link>
                <router-link :to="'/benefitList'" type="button" class="btn btn-info dugme" v-else>List of users with benefits</router-link>
                <router-link :to="'/serviceList'" type="button" class="btn btn-info dugme" v-if="isSerbian">Lista ponuđenih usluga</router-link>
                <router-link :to="'/serviceList'" type="button" class="btn btn-info dugme" v-else>List of offered services</router-link>
            </div>
        </div>
    </div>
</template>

<script>
import {between} from "vuelidate/lib/validators"
export default {
    validations:
    {
        maxDistance: { between : between(0.1,1000) },
        discount: {between: between(0,100) },
        brSaradnji: {between: between(0,20)}
    },
    data()
    {
        return{
            scale: (this.$store.state.authUser.min_rating - 1) * 25,
            maxDistance: this.$store.state.authUser.max_dist,
            IsMaxDistance: true,
            discount: this.$store.state.authUser.benefit_discount,
            brSaradnji: this.$store.state.authUser.benefit_requirement,
            isEditing: false
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        minRating()
        {
            return (Math.round((this.scale / 25 + 1) * 100) / 100).toFixed(2)
        },
        popoverRatingText()
        {
            if(this.isSerbian)
                return 'Minimalna prosečna ocena koju korisnik mora da ima da bi mu se prikazao Vaš zahtev'
            else
                return 'Minimal rating a user has to have for them to see a request you have created'
        },
        popoverDistanceText()
        {
            if(this.isSerbian)
                return 'Koliko maksimalno može korisnik da bude udaljen od adresa na Vašem zahtevu da bi mu se on prikazao'
            else
                return 'How far away can a user be from the addresses in the request for them to still be able to see it'
        },
        popoverDiscountText()
        {
            if(this.isSerbian)
                return 'Procentualno umanjenje cene koje će automatski dobiti svi korisnici koji zadovoljavaju uslov broja međusobnih saradnji sa Vama definisan u opciji ispod ove'
            else
                return 'The percentage of dicount from regular prices users will automatically get when they cooperate with you as many times as you have selected in the option below this one'
        },
        popoverCooperationText()
        {
            if(this.isSerbian)
                return 'Koliko puta treba da sarađujete sa nekim korisnikom da bi on automatski ostvarivao popust na Vaše usluge definisan u opciji iznad'
            else
                return 'How many times do you need to cooperate with a user for them to start automatically getting a discount on your regular prices defined in the option above'
        },
        isMaxDistInvalid() 
        {
            return this.$v.maxDistance.$invalid
        },
        isDiscountInvalid() 
        {
            return this.$v.discount.$invalid
        },
        isBrSaradnjiInvalid() 
        {
            return this.$v.brSaradnji.$invalid
        }
    },
    methods:
    {
        MaxDistReset()
        {
            if(this.isMaxDistInvalid)
                this.maxDistance = 1000
        },
        DiscountReset()
        {
            if(this.isDiscountInvalid)
                this.discount = 0
        },
        BrSaradnjiReset()
        {
            if(this.isBrSaradnjiInvalid)
                this.brSaradnji = 5
        },
        save()
        {
            this.$store.state.authUser.min_rating = this.minRating
            this.$store.state.authUser.max_dist = this.maxDistance
            this.$store.state.authUser.benefit_discount = this.discount
            this.$store.state.authUser.benefit_requirement = this.brSaradnji
            this.$store.dispatch("updateUserInfo")
            this.isEditing = false
        },
        cancel()
        {
            this.scale = (this.$store.state.authUser.min_rating - 1) * 25,
            this.maxDistance = this.$store.state.authUser.max_dist
            this.discount = this.$store.state.authUser.benefit_discount
            this.brSaradnji = this.$store.state.authUser.benefit_requirement
            this.isEditing = false
        }
    }
}
</script>

<style scoped>
    .page-div
    {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        justify-items: center;
        align-content: center;
        align-items: center;
    }

    .content-div
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        background-color: white;
        width: 80%;
        margin-top: 50px;
        margin-bottom: 50px;
    }

    .dugmici
    {
        flex-grow: 1;
        flex-shrink: 1;
        display:flex;
        flex-direction: column;
        justify-content: center;
        justify-items: center;
        align-content: center;
        align-items: center;
    }

    .dugme
    {
        width: fit-content;
        margin: 15px;
    }

    .dugme-edit
    {
        margin-left:10px;
        margin-right:10px;
    }

    .settings-main
    {
        flex-grow: 2;
        flex-shrink: 2;
        display: flex;
        flex-direction: column;
        justify-content: center;
        justify-items: center;
        align-content: center;
        align-items: center;
    }

    .settings-element, .button-element
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        width: 100%;
        margin: 10px;
    }

    .settings-row
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin: 5px;
    }

    .skala
    {
        width: 200px;
        text-align: center;
    }
  
    .slika-div
    {
        flex-grow: 1;
        flex-shrink:1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        justify-items: center;
        align-content: center;
        align-items: center;
    }

    .vuelidate-div
    {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .slika
    {
        width:100px;
        height: 100px;
        margin: 30px;
    }

    .info
    {
        margin-right: 5px;
    }

    .brojka
    {
        margin-left: 10px;
        margin-right: 10px;
        font-size: 18px;
    }

    .checkBox
    {
        width: 20px;
        height: 20px;
    }

    .danger
    {
        color: red;
        font-size: 14px;
    }

    .ne-valja
    {
        background-color: rgb(255, 212, 212);
    }

    .skriveno
    {
        visibility: hidden;
    }

    .m1
    {
        margin-right: 98px;
    }

    .m2
    {
        margin-right: 66px;
    }

    .m3
    {
        margin-right: 182px;
    }
      
    .e1
    {
        margin-right: 122px;
    }

    .e2
    {
        margin-right: 98px;
    }

    .e3
    {
        margin-right: 178px;
    }

    .fs2, .fs3
    {
        margin-right:0px
    }

    .fs3
    {
        margin-left: 11px;
    }

    .fs1
    {
        margin-left: 38px;
    }

    @media only screen and (max-width: 1160px)
    {
        .content-div
        {
            flex-direction: column;
            justify-content: center;
            align-items: center;
            margin-top: 30px;
            width: 86%;
        }

        .settings-element
        {
            flex-direction: column;
        }

        .left
        {
            align-self: flex-start;
            margin-bottom: 10px;
        }
    }
</style>