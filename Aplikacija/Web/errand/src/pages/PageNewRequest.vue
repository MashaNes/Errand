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
            </div>
        </div>
    </div>
</template>

<script>
import NewRequest1 from "@/components/NewRequest1"
import NewRequest2 from "@/components/NewRequest2"
import NewRequest3 from "@/components/NewRequest3"
import NewRequest4 from "@/components/NewRequest4"
import NewRequest5 from "@/components/NewRequest5"
import NewRequest7 from "@/components/NewRequest7"
export default {
    props:
    {
        requestProp:{
            type: Object,
            required: false
        }
    },
    components:
    {
        NewRequest1,
        NewRequest2,
        NewRequest3,
        NewRequest4,
        NewRequest5,
        NewRequest7
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
            }
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
            if(this.step == 1 && (this.request.name == "" || this.request.max_dist == "" || this.request.max_dist < 0.1 || this.request.max_dist > 1000))
            {
                return true
            }
            else if(this.step == 3 && this.request.tasklist.length == 0)
                return true
            return false
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
                this.step = 6
        },
        directChanged(directCopy)
        {
            if(directCopy)
                this.step = 6
            else
                this.step = 7
        }
    },
    created()
    {
        if(this.requestProp != undefined)
            this.request = this.requestProp
        this.request.direct_user = null
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

     @media only screen and (max-width: 950px)
     {
        .bodyDiv
        {
            margin-left: 0px;
            margin-right:0px
        }
     }
</style>