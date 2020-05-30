<template>
    <Spinner v-if="myRequest == undefined" />
    <div class="request-wrapper" v-else>
        <div class="request">
            <span class="naziv">{{myRequest.name}}</span>
            <div class="stavka row-order">
                <img src="../assets/clock.svg" class="ikonica">
                <span v-if="!isSerbian"> {{myRequest.time | showTime}} </span>
                <span v-else> {{dateAndTime}}</span>
            </div>
            <div class="stavka row-order" v-if="myRequest.note != ''">
                <img src="../assets/reports.svg" class="ikonica">
                <i class="upozorenje"> {{myRequest.note}} </i>
            </div>
            <div class="stavka user-div">
                <div class="name">
                    <img src="../assets/requests.svg" class="ikonica" />
                    {{myRequest.created_by.first_name}} {{myRequest.created_by.last_name}}, 
                </div>
                {{myRequest.created_by.email}}
            </div>
            <div class="stavka user-div" v-if="myRequest.working_with != null">
                <div class="name">
                    <img src="../assets/running.svg" class="ikonica" />
                    {{myRequest.working_with.first_name}} {{myRequest.working_with.last_name}}, 
                </div>
                {{myRequest.working_with.email}}
            </div>
            <b-card-text v-if="hasAddresses" class="mapa">
                <Map />
            </b-card-text>
            <div class="item" v-if="myRequest.pictures.length > 0">
                <img src="../assets/pictures.svg" class="ikonica" />
                <div class="photo-list">
                    <div v-for="picture in myRequest.pictures" :key="picture" class="photo">
                        {{picture}}
                    </div> 
                </div>
            </div>
            <div class="tasks-div">
                <TaskAdmin v-for="(task,index) in myRequest.tasklist" 
                          :key="task.id" :task="task" 
                          :currentNumber="currentNumber" 
                          :myNumber="index + 1" 
                          @fokus="currentNumber = index + 1"
                          @close="currentNumber = 0"
                          :editable="isEditable"
                          @editedChanged="editedChanged" />
            </div>
        </div>
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import TaskAdmin from "@/components/TaskAdmin"
import Map from "@/components/Map"
export default {
    props:
    {
        request:
        {
            type: Object,
            required:false
        }
    },
    components:
    {
        Map,
        TaskAdmin,
        Spinner
    },
    data()
    {
        return{
            hasAddresses: false,
            currentNumber: 0,
            myRequest: this.request,
            wasEdited: false
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        isEditable()
        {
            return this.$route.params.editable == "other"
        },
        dateAndTime()
        {
            var date = new Date(this.myRequest.time)
            
            var day = date.getUTCDate()
            var month = date.getUTCMonth()+1
            var year = date.getUTCFullYear()
            var hours = date.getUTCHours()
            var minutes = date.getUTCMinutes()
            
            var monthString = ""
            var hoursString = hours
            var minutesString = minutes
            if(hours < 10)
                hoursString = "0" + hours
            if(minutes < 10)
                minutesString = "0" + minutes

            switch(month)
            {
                case 1: 
                        monthString = "Januar"
                        break
                case 2: 
                        monthString = "Februar"
                        break
                case 3: 
                        monthString = "Mart"
                        break
                case 4: 
                        monthString = "April"
                        break
                case 5: 
                        monthString = "Maj"
                        break
                case 6: 
                        monthString = "Jun"
                        break
                case 7: 
                        monthString = "Jul"
                        break 
                case 8: 
                        monthString = "Avgust"
                        break
                case 9: 
                        monthString = "Septembar"
                        break
                case 10: 
                        monthString = "Oktobar"
                        break
                case 11: 
                        monthString = "Novembar"
                        break
                default:
                        monthString = "Decembar"
                        break
            }

            return day + ". " + monthString + " " + year + "." + "  " + hoursString + ":" + minutesString + "h"
        }
    },
    methods:
    {
        editedChanged(value)
        {
            this.wasEdited = value
        },
        callbackRequest()
        {
            if(this.$store.state.specificRequest == null)
            {
                setTimeout(this.callbackRequest, 200)
            }
            else if(this.$store.state.specificRequest == -1)
            {
                this.$router.push("/404")
                this.myRequest = null
            }
            else
                this.myRequest = this.$store.state.specificRequest
        },
        callbackMaps()
        {
            if(this.myRequest == undefined)
            {
                setTimeout(this.callbackMaps, 200)
            }
            else
            {
                if(this.myRequest.destination || this.myRequest.tasklist)
                {
                    const markerPositions = [];
                    if(this.myRequest.destination) 
                    {
                        this.hasAddresses = true
                        const newPosition = 
                        {
                            pos: 
                            {
                                lat: this.myRequest.destination.latitude,
                                lng: this.myRequest.destination.longitude
                            },
                            lab: "F",
                            info: this.myRequest.destination.name
                        }
                        markerPositions.push(newPosition)
                    }

                    if(this.myRequest.tasklist)
                    {
                        this.myRequest.tasklist.forEach((task, ind) => 
                        {
                            if(task.address)
                            {
                                this.hasAddresses = true
                                const newPosition = 
                                {
                                    pos: 
                                    {
                                        lat: task.address.latitude,
                                        lng: task.address.longitude
                                    },
                                    lab: String(ind + 1),
                                    info: task.address.name
                                }
                                markerPositions.push(newPosition)
                            }
                        })
                        this.$store.dispatch('setMarkerPositions', markerPositions)
                    }
                }
            }
        }
    },
    created()
    {
        if(this.myRequest == undefined)
        {
            const routeId = this.$route.params.id
            this.$store.dispatch("getRequestByIdAdmin", routeId)
            this.callbackRequest()
        }

        this.callbackMaps()
    }
}
</script>

<style scoped>

    .request-wrapper
    {
        display:flex;
        flex-direction: column;
        align-items: center;
        width:100%;
    }

    .request
    {
        margin:30px;
        padding:20px;
        border:0.6px solid black;
        border-radius: 15px;
        width:70%;
        background-color:white;
        display: flex;
        flex-direction: column;
    }

    .naziv
    {
        margin-left:20px;
        font-size: 24px;
        font-weight: 700;
        margin-bottom: 20px;
    }

    .stavka
    {
        margin-bottom:10px;
    }

    .row-order
    {
        display: flex;
        flex-direction: row;
        align-items: center;
    }

    .ikonica
    {
        width:24px;
        height:24px;
        margin-right:10px;
    }

    .user-div
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        margin-bottom:10px;
    }

    .name
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        margin-right:7px;
        font-size: 18px;
    }

    .upozorenje
    {
        color: rgb(179, 59, 59);
    }

    .mapa
    {
        margin: 10px;
    }

    .item
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        width:100%;
        padding:5px;
    }

    .photo-list
    {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;
    }

    .photo
    {
        margin:7px;
        width:70px;
        height:100px;
        border:0.5px solid black;
    }

    .tasks-div
    {
        display: flex;
        flex-direction: column;
        margin:7px;
    }

    @media only screen and (max-width: 900px)
    {
        .request
        {
            width:94%;
        }

        .user-div
        {
            flex-direction: column;
            align-items: flex-start;
        }
    }
</style>