<template>
    <div class="wrapper-step1">
        <div class="labelDiv">
            <span v-if="isSerbian"> <span class="zvezdica">*</span> Naziv: </span>
            <span v-else> <span class="zvezdica">*</span>  Name: </span>
        </div>
        <input type="text" v-model="myName" @input="NameChanged" class="name">
        <div class="labelDiv">
            <span v-if="isSerbian"> Napomena: </span>
            <span v-else> Note: </span>
        </div>
        <textarea type="text" v-model="myNote" @input="NoteChanged" class="name" />
        <div class="labelDiv">
            <span v-if="isSerbian">  <span class="zvezdica">*</span> Datum i vreme: </span>
            <span v-else>  <span class="zvezdica">*</span> Date and time: </span>
        </div>
        <VueCtkDateTimePicker v-model="myTime" 
                              :no-label="true" 
                              :button-now-translation="'Sada'" 
                              :noClearButton="true" class="name" 
                              :format="'YYYY-MM-DD HH:mm'"
                              @input="dateChanged" 
                              v-if="isSerbian"/>
        <VueCtkDateTimePicker v-model="myTime" 
                              :no-label="true" 
                              :noClearButton="true" 
                              :locale="'en'" 
                              class="name" 
                              :format="'YYYY-MM-DD HH:mm'" 
                              @input="dateChanged"
                              v-else/>
        <div class="numberDiv">
            <div class="rowDiv">
                <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverRatingText">
                <span class="labelSpan" v-if="isSerbian">Minimalni rejting</span>
                <span class="labelSpan" v-else> Minimal rating</span>
            </div>
            <div class="rowDiv">
                <input type="range" class="form-control-range skala" id="formControlRange" v-model="scale" @input="ratingChanged">
                <span>{{myMinRating}}</span>
            </div>
        </div>
        <div class="numberDiv">
            <div class="rowDiv">
                <img class = "info" src="../assets/info.svg" v-b-popover.hover.left="popoverDistanceText">
                <span class="labelSpan" v-if="isSerbian"> <span class="zvezdica">*</span> Maksimalno rastojanje</span>
                <span class="labelSpan" v-else> <span class="zvezdica">*</span>  Maximum distance</span>
            </div>
            <div class="rowDiv">
                <input type="number" class="distanca" v-model="MyMaxDistance" min="0.1" max="1000" step="0.1" @input="distanceChanged">
                <span>km</span>
            </div>
        </div>
    </div>
</template>

<script>
import {required, between} from "vuelidate/lib/validators"
import VueCtkDateTimePicker from 'vue-ctk-date-time-picker';
import 'vue-ctk-date-time-picker/dist/vue-ctk-date-time-picker.css';
export default {
    props:
    {
        name:
        {
            type: String,
            required: true
        },
        time:
        {
            type: String,
            required: true
        },
        note:
        {
            type: String,
            required: true
        },
        minRating:
        {
            type: String,
            required: true
        },
        maxDistance:
        {
            type: String,
            required: true
        }
    },
    data()
    {
        return{
            myName:this.name,
            myTime: this.time,
            myNote: this.note,
            MyMaxDistance: this.maxDistance,
            scale: (this.minRating - 1) * 25
        }
    },
    components:
    {
        VueCtkDateTimePicker
    },
    validations:
    {
        name : { required },
        maxDistance : { between: between(0.1, 1000) }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        popoverRatingText()
        {
            if(this.isSerbian)
                return 'Minimalna prosečna ocena koju korisnik sa kojim ćete da sarađujete na ovom zahtevu mora da ima'
            else
                return 'Minimal rating the user you will cooperate with on this request has to have'
        },
        popoverDistanceText()
        {
            if(this.isSerbian)
                return 'Koliko maksimalno može korisnik sa kojim ćete da sarađujete na ovom zahtevu da bude udaljen od navedenih adresa'
            else
                return 'How far away can the user you will cooperate with on this request be from the stated addresses'
        },
        myMinRating()
        {
            return (Math.round((this.scale / 25 + 1) * 100) / 100).toFixed(2)
        }
    },
    methods:
    {
        NameChanged()
        {
            this.$emit("nameChanged", this.myName)
        },
        NoteChanged()
        {
            this.$emit("noteChanged", this.myNote)
        },
        dateChanged()
        {
            this.$emit("dateChanged", this.myTime)
        },
        ratingChanged()
        {
            this.$emit("ratingChanged", this.myMinRating)
        },
        distanceChanged()
        {
            this.$emit("distanceChanged", this.MyMaxDistance)
        }
    }
}
</script>

<style scoped>
    .wrapper-step1
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

    .name
    {
        width:94%;
        margin-bottom: 10px;
    }

    .numberDiv
    {
        display:flex;
        flex-direction: row;
        align-content: center;
        justify-content: flex-start;
        margin-bottom: 10px;
        width:100%;
    }

    .rowDiv
    {
        display:flex;
        flex-direction: row;
        align-content: center;
        justify-content: flex-start;
        width:100%;
    }

    .labelSpan
    {
        font-size: 18px;
        font-weight: 600;
        padding-left:10px;
        margin-right: 5px;
    }

    .info
    {
        margin-right: 5px;
    }

    .skala
    {
        width:80%;
        margin-right:7px;
    }

    .distanca
    {
        width:100px;
        margin-right:7px;
    }

    .zvezdica
    {
        color:red;
    }

    @media only screen and (max-width: 550px)
    {
        .numberDiv
        {
            flex-direction: column;
        }
        .skala
        {
            width:84%;
            margin-left:10px;
        }

        .distanca
        {
            margin-left:50px;
        }
    }
</style>