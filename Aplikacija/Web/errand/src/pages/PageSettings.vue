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
                        <input type="range" class="form-control-range skala" id="formControlRange" v-model="scale">
                        <span class="brojka"> {{minRating}}</span>
                    </div>
                </div>
                <div class="settings-element"> 
                    <div class="settings-row left m2" :class="{'e2':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverDistanceText">
                        <span v-if="isSerbian"> Maksimalno rastojanje: </span>
                        <span v-else> Maximum distance: </span>
                    </div>
                    <div class="settings-row">
                        <input type="number" class="skala" v-model="maxDistance" min="0.1" max="1000" step="0.1" :disabled="!IsMaxDistance">
                        <span class="brojka"> km </span>
                        <!--<input class = "checkBox" type="checkbox" v-model="IsMaxDistance">-->
                    </div>
                </div>
                <div class="settings-element"> 
                    <div class="settings-row left m3" :class="{'e3':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverDiscountText">
                        <span v-if="isSerbian"> Popust: </span>
                        <span v-else> Discount: </span>
                    </div>
                    <div class="settings-row">
                        <input type="number" class="skala" v-model="discount" min="0" max="100" step="10">
                        <span class="brojka"> % </span>
                    </div>
                </div>
                <div class="settings-element"> 
                    <div class="settings-row left m4" :class="{'e4':!isSerbian}">
                        <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverCooperationText">
                        <span v-if="isSerbian"> Broj saradnji za ostvarenje popusta: </span>
                        <span v-else> Number of cooperations for discount: </span>
                    </div>
                    <div class="settings-row">
                        <input type="number" class="skala" v-model="brSaradnji" min="0" max="20" step="1">
                        <span class="skriveno"> % </span>
                    </div>
                </div>
            </div>
            <div class="dugmici"> 
                <button type="button" class="btn btn-info dugme" v-if="isSerbian">Lista povlašćenih korisnika</button>
                <button type="button" class="btn btn-info dugme" v-else>List of users with benefits</button>
                <button type="button" class="btn btn-info dugme" v-if="isSerbian">Lista ponuđenih usluga</button>
                <button type="button" class="btn btn-info dugme" v-else>List of offered services</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data()
    {
        return{
            scale: 0,
            maxDistance: 1000,
            IsMaxDistance: true,
            discount: 0,
            brSaradnji: 5
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

    .settings-element
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