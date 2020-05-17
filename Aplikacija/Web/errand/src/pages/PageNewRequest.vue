<template>
    <div class="wrapperDiv"> 
        <div class="bodyDiv">
            <NewRequest1 :name="request.name" 
                         :time="request.time" 
                         :note="request.note" 
                         :minRating="request.min_rating" 
                         :maxDistance="request.max_dist" 
                         @nameChanged="NameChanged"
                         @noteChanged="NoteChanged"
                         @dateChanged="dateChanged"
                         @ratingChanged="ratingChanged"
                         @distanceChanged="distanceChanged"
                         v-if="step == 1"/>
            <NewRequest2 :address="request.address"
                         @addressChanged="addressChanged"
                         v-if="step == 2"/>
            <NewRequest3 :tasklist="request.tasklist"
                         @tasklistChanged="tasklistChanged"
                         v-if="step == 3" />
            <NewRequest4 @broadcastChanged="broadcastChanged"
                         v-if="step == 4"/>
            <NewRequest5 @directChanged="directChanged"
                         v-if="step == 5"/>
            <NewRequest7 :request="request"
                         v-if="step == 7"/>
            <div class="buttonDiv">
                <button type="button" class="btn btn-secondary" v-if="isSerbian && step < 5" :disabled="step == 1" @click="step = step - 1">Nazad</button>
                <button type="button" class="btn btn-secondary" v-if="!isSerbian && step < 5" :disabled="step == 1" @click="step = step - 1">Back</button>
                <button type="button" class="btn btn-primary" v-if="isSerbian && step < 4" :disabled="disabledNextButton" @click="step = step + 1">Dalje</button>
                <button type="button" class="btn btn-primary"  v-if="!isSerbian && step < 4" :disabled="disabledNextButton" @click="step = step + 1">Next</button>
                
                <button type="button" class="btn btn-danger" v-if="isSerbian && step == 7" @click="odustaniOdKreiranja">
                    <img src="../assets/failed.svg" class="slika">
                    Odustani
                </button>
                <button type="button" class="btn btn-danger"  v-if="!isSerbian && step == 7" @click="odustaniOdKreiranja">
                    <img src="../assets/failed.svg" class="slika">
                    Cancel
                </button>
                <button type="button" class="btn btn-success" v-if="isSerbian && step == 7" @click="kreirajZahtev">
                    <img src="../assets/finished.svg" class="slika">
                    Kreiraj zahtev
                </button>
                <button type="button" class="btn btn-success" v-if="!isSerbian && step == 7" @click="kreirajZahtev">
                    <img src="../assets/finished.svg" class="slika">
                    Create request
                </button>
            </div>
        </div>
        <ModalAreYouSure v-if="showModal==true"
                         :naslovS="naslovS"
                         :naslovE="naslovE"
                         :tekstS="tekstS"
                         :tekstE="tekstE"
                         @yes="yes"
                         @close="showModal = false"/>
    </div>
</template>

<script>
import NewRequest1 from "@/components/NewRequest1"
import NewRequest2 from "@/components/NewRequest2"
import NewRequest3 from "@/components/NewRequest3"
import NewRequest4 from "@/components/NewRequest4"
import NewRequest5 from "@/components/NewRequest5"
import NewRequest7 from "@/components/NewRequest7"
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    props:
    {
        requestProp:{
            type: Object,
            required: false
        },
        stepProp:{
            type: Number,
            required:false
        }
    },
    components:
    {
        NewRequest1,
        NewRequest2,
        NewRequest3,
        NewRequest4,
        NewRequest5,
        NewRequest7,
        ModalAreYouSure
    },
    data()
    {
        return{
            step:1,
            request:
            {
                name: "",
                time: this.getCurrendTimeAndDate(),
                note: "",
                broadcast: false,
                max_dist: this.$store.state.authUser.max_dist.toString(),
                min_rating: this.$store.state.authUser.min_rating.toString(),
                address:
                {
                    name: "",
                    longitude: null,
                    latitude: null,
                },
                tasklist:[],
                direct_user: null
            },
            showModal: false,
            kreiraj: null
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        disabledNextButton()
        {
            var mydate = new Date(this.request.time);
            var now = new Date()
            if(this.step == 1 && (this.request.name == "" || this.request.max_dist == "" || this.request.max_dist < 0.1 || this.request.max_dist > 1000 || mydate < now))
            {
                return true
            }
            else if(this.step == 3 && this.request.tasklist.length == 0)
                return true
            return false
        },
        naslovS()
        {
            if(this.kreiraj)
                return "Kreiranje zahteva"
            else
                return "Odustajanje od kreiranja zahteva"
        },
        naslovE()
        {
            if(this.kreiraj)
                return "Create the request"
            else
                return "Cancel request creation process"
        },
        tekstS()
        {
            if(this.kreiraj)
                return "Da li ste sigurni da želite da kreirate opisani zahtev?"
            else
                return "Da li ste sigurni da želite da odustanete od kreiranja? Svi do sada uneti podaci biće nepovratno odbačeni."
        },
        tekstE()
        {
            if(this.kreiraj)
                return "Are you sure you want to create the described request"
            else
                return "Are you sure you want to cancel ti request creation process? All data you have entered will be lost."
        }
    },
    methods:
    {
        getCurrendTimeAndDate()
        {
            var date = new Date()
            var day = date.getDate()
            var month = date.getMonth()+1
            var year = date.getFullYear()
            var hours = date.getHours()
            var minutes = date.getMinutes()
            var monthString = month
            var dayString = day
            var hoursString = hours
            var minutesString = minutes
            if(month < 10)
                monthString = "0" + month
            if(day < 10)
                dayString = "0" + day
            if(hours < 10)
                hoursString = "0" + hours
            if(minutes < 10)
                minutesString = "0" + minutes
            return year + "-" + monthString + "-" + dayString + " " + hoursString + ":" + minutesString
        },
        NameChanged(nameCopy)
        {
            this.request.name = nameCopy
        },
        NoteChanged(noteCopy)
        {
            this.request.note = noteCopy
        },
        dateChanged(dateCopy)
        {
            this.request.time = dateCopy
        },
        ratingChanged(ratingCopy)
        {
            this.request.min_rating = ratingCopy
        },
        distanceChanged(distanceCopy)
        {
            this.request.max_dist = distanceCopy
        },
        addressChanged(addressCopy)
        {
            this.request.address = addressCopy
        },
        tasklistChanged(newTasklist)
        {
            this.request.tasklist = newTasklist
        },
        broadcastChanged(broadcastCopy)
        {
            this.request.broadcast = broadcastCopy
            if(broadcastCopy)
                this.step = 5
            else
                //this.step = 6
                this.directUserSelect()
        },
        directChanged(directCopy)
        {
            if(directCopy)
                //this.step = 6
                this.directUserSelect()
            else
            {
                this.step = 7
                this.request.direct_user = null
            }
        },
        kreirajZahtev()
        {
            this.kreiraj = true
            this.showModal = true
        },
        odustaniOdKreiranja()
        {
            this.kreiraj = false
            this.showModal = true
        },
        yes()
        {
            if(!this.kreiraj)
                this.$router.push('/requests')
            else
            {
                this.request.tasklist.forEach((element,index) =>
                {
                    element.service_type = element.service_type.id
                    var pom = 
                    {
                        name: element.name,
                        service_type: element.service_type,
                        description: element.description,
                        checklist: element.checklist,
                        picture_required: element.picture_required,
                        adress: element.address
                    }
                    this.request.tasklist.splice(index,1,pom)
                })

                if(this.request.direct_user != null)
                    this.request.direct_user = this.request.direct_user.id
                
                this.$store.dispatch("createRequest", this.request)
                this.$router.push('/requests')
            }
        },
        directUserSelect()
        {
            this.$store.servicesRequired = []
            this.request.tasklist.forEach(element =>
            {
                this.$store.servicesRequired.push(
                    {
                        id: element.service_type.id,
                        min_rating: parseFloat(this.request.min_rating),
                        max_dist: parseFloat(this.request.max_dist)
                    }
                )
            })

            this.$store.state.requestInCreation = this.request
            this.$router.push({ name: 'PageBrowseUsers', params: {benefitList: "noBenefit", servicesList:this.$store.servicesRequired}})
        }
    },
    created()
    {
        if(this.requestProp != undefined)
            this.request = this.requestProp
        if(this.stepProp != undefined)
            this.step = this.stepProp
    }
}
</script>

<style scoped>
    .wrapperDiv
    {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        padding: 10px;
        padding-top: 30px;
        padding-bottom:30px;
    }

    .bodyDiv
    {
        background-color: white;
        border-radius: 15px;
        border:1px solid black;
        padding:10px;
        margin-left: 15%;
        margin-right:15%;
        display: flex;
        flex-direction: column;
        align-items: center;
        width:800px;
    }

    .buttonDiv
    {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
        width:100%;
        margin-top:15px;
    }

    .slika
    {
        width: 20px;
        height:20px;
        margin-right:7px;
    }

     @media only screen and (max-width: 950px)
     {
        .bodyDiv
        {
            margin-left: 0px;
            margin-right:0px
        }
     }
</style>