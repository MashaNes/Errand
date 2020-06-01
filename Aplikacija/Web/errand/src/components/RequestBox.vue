<template>
    <div class = "wrapper" :class="color">
        <div class="request-top" v-if="myRequest.status != 0">
            <div class="user-related-div">
                <div class="user-div"> 
                    <span class="user-name" @click="goToProfile"> {{userName}} </span>
                    <p class="image is-128x128" >
                        <img class="rounded-image" :src="user.picture ? 'data:;base64,' + user.picture : require('../assets/no-picture.png')" @click="goToProfile">
                    </p>
                </div>
                <div class="button-div">
                    <button v-if="(myRequest.status > 1)" type="button" class="btn btn-danger dugme-rate" @click="reportUser">
                        <img src="../assets/report.png" class="slika">
                        <span v-if="isSerbian"> Prijavi </span>
                        <span v-else> Report </span>
                    </button>
                    <button v-if="(myRequest.status == 2 || myRequest.status == 3) && canRate" type="button" class="btn btn-success dugme-rate" @click="rateUser">
                        <!-- proveriti kad bude bilo primera!!!!!!! -->
                        <img src="../assets/rate.png" class="slika">
                        <span v-if="isSerbian"> Oceni </span>
                        <span v-else> Rate </span>
                    </button>
                </div>
            </div>
            <div class = "request-name" @click="goToRequest">
                {{myRequest.name}}
            </div>
        </div>
        <div class="request-top2" v-else>
            <a class = "cancel-request" @click="showModal = true">
                <img class = "ikonica" src="../assets/remove.svg">
            </a>
            <div class = "request-name-2" @click="goToRequest">
                {{myRequest.name}}
            </div>
        </div>
        <div class="request-bottom">
            <div class="status-div">
                <img v-if="myRequest.status == 1" src = "../assets/running.svg">
                <img v-if="myRequest.status == 0" src = "../assets/pending.svg">
                <img v-if="myRequest.status == 2" src = "../assets/finished.svg">
                <img v-if="myRequest.status == 3" src = "../assets/failed.svg">
                <span class = "request-status"> {{status}} </span>
            </div>
            <div class = "bottom-left">
                <div class = "request-date" v-if="!isSerbian"> {{myRequest.time | showTime}} </div>
                <div class = "request-date" v-else> {{dateAndTime}} </div>
                <div class = "tagovi" v-if="isSerbian">
                    <div v-for="tag in tags" :key="tag.id" class = "request-tag" v-b-popover.hover.bottom="tag.description_sr">
                        {{tag.service_type_sr}}
                    </div>
                </div>
                <div class = "tagovi" v-else>
                    <div v-for="tag in tags" :key="tag.id" class = "request-tag" v-b-popover.hover.bottom="tag.description_en">
                        {{tag.service_type_en}}
                    </div>
                </div>
            </div>
        </div>
        <ModalAreYouSure :naslovS="'Brisanje zahteva'" 
                         :tekstS="'Da li ste sigurni da želite da obrišete ovaj zahtev?'" 
                         :naslovE="'Deleting a request'"
                         :tekstE="'Are you sure you want to delete this request?'"
                         @close="showModal = false" @yes="deleteRequest" v-if="showModal"/>
        <ModalReportUser v-if="showModalReport" @close="showModalReport = false" :userToReport="user"/>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
import ModalReportUser from "@/components/ModalReportUser"

export default {
    props:
    {
        myRequest:{
            type:Object,
            required:true
        }
    },
    components:
    {
        ModalAreYouSure,
        ModalReportUser
    },
    data()
    {
        return{
            showModal: false,
            showModalReport: false
        }
    },
    computed:
    {
        color()
        {
            if (this.myRequest.status == 2)
                return "zeleno"
            else if(this.myRequest.status == 3)
                return "crveno"
            else if(this.myRequest.status == 0)
                return "sivo"
            else
                return "zuto"
        },
        userName()
        {
            if(this.myRequest.status != 0)
                return this.user.first_name + " " + this.user.last_name
            else
                return ""
        },
        userPicture()
        {
            if(this.myRequest.status != 0)
                return this.user.picture;
            else
                return ""
        },
        userId() 
        {
            if(this.myRequest.status != 0)
                return this.user.id
            else
                return ""
        },
        user() 
        {
            if(this.myRequest.created_by && this.myRequest.created_by.id == this.$store.state.authUser.id )
                return this.myRequest.working_with
            else
                return this.myRequest.created_by
        },
        status()
        {
            let returnValue = ""
            switch(this.myRequest.status)
            {
                case 0: 
                        if(this.isSerbian)
                            returnValue = "na čekanju"
                        else
                            returnValue = "pending"
                        break
                case 1: 
                        if(this.isSerbian)
                            returnValue = "u izvršenju"
                        else
                            returnValue = "running"
                        break
                case 2: 
                        if(this.isSerbian)
                            returnValue = "završen"
                        else
                            returnValue = "finished"
                        break
                case 3: 
                        if(this.isSerbian)
                            returnValue = "otkazan"
                        else
                            returnValue = "failed"
            }
            return returnValue
        },
        tags()
        {
            let returnValue = []
            var ids = []
            this.myRequest.tasklist.forEach(task => 
            {
                var contains = false
                ids.forEach(element =>
                {
                    if(element == task.service_type.id)
                        contains = true
                })

                if(!contains)
                {
                    ids.push(task.service_type.id)
                    returnValue.push(task.service_type)
                }
            })
            return returnValue
        },
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        canRate()
        {
            if(this.myRequest.created_by.id == this.$store.state.authUser.id)
                return !this.myRequest.rated_working_with
            else
                return !this.myRequest.rated_created_by
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
        deleteRequest()
        {
            console.log(this.myRequest)
            this.showModal = false
            this.$store.state.createdAuthRequests.results.forEach((element,index) =>
            {
                if(element.id == this.myRequest.id)
                    this.$store.state.createdAuthRequests.results.splice(index,1)
            })
            this.$store.dispatch("deleteRequest", this.myRequest.id)
        },
        goToProfile()
        {
            this.$router.push({
                name: "PageViewProfile", 
                params: {
                    id: this.user.id, 
                    user: this.user
                }
            })
        },
        goToRequest() 
        {
            this.$router.push({
                name: "PageViewRequest",
                params: {
                    id: this.myRequest.id,
                    request: this.myRequest,
                    startingView: "Details"
                }
            })
        },
        rateUser()
        {
            console.log(this.user)
            //rate the user
        },
        reportUser()
        {
            this.showModalReport = true
        }
    }
}
</script>

<style scoped>
    .wrapper
    {
        margin:10px;
        border: 1px solid rgb(43, 41, 41);
        border-radius: 15px;
        min-height: 100px;
        padding:5px;
        width: 80%;
        display:flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .request-top, .request-top2
    {
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-start;
        align-items: flex-start;
        width: 100%;
    }

    .request-bottom
    {
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-start;
        align-items: center;
        width: 100%;
        margin-top: 30px;
    }

    .user-div
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-items: center;
        margin-right: 10px;
        margin-top:10px;
        font-size: 18px;
    }

    .user-related-div
    {
        display: flex;
        flex-direction:column;
        align-items:center;
    }

    .button-div
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-items: center;
        margin-right: 10px;
    }

    .cancel-request
    {
        display: flex;
        flex-direction: row;
        align-items: flex-start;
        justify-items: center;
        margin-right: 10px;
        margin-top: 10px;
        font-size: 18px;
        width:25px;
        align-self:flex-start;
        height:100%;
    }

    .ikonica
    {
        width:22px;
        height:22px;
    }

    .status-div
    {
        display: flex;
        flex-direction: row;
        align-items: flex-end;
        justify-items: flex-end;
        margin-right: 10px;
        font-size: 18px;
    }

    .tagovi
    {
        display: flex;
        flex-direction: row;
        align-items:center;
        flex-wrap:wrap;
    }
    
    .bottom-left
    {
        flex-grow: 1;
        width:82%;
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
    }
    
    .request-date
    {
        padding: 10px;
        text-align: center;
        font-size: 18px;
    }

    .user-name
    {
        margin-right: 10px;
    }

    .user-name:hover 
    {
        cursor: pointer !important;
        text-decoration: underline;
        color:lightseagreen;

    }

    .request-name
    {
        font-weight: bold;
        font-size: 25px;
        color:rgb(11, 97, 126);
        text-align: left;
        /*width:96%;*/
        flex-grow:1;
        margin-left: 10px;
        margin-top:2px;
        margin-right:7px;
    }

    .request-name-2
    {
        font-weight: bold;
        font-size: 25px;
        color:rgb(11, 97, 126);
        text-align: left;
        width:96%;
        margin-left: 10px;
        margin-top:2px;
        margin-right:7px;
    }

    .request-name:hover, .request-name-2:hover
    {
        color:rgb(7, 133, 175);
        cursor: pointer;
    }

    .request-tag
    {
        border-radius: 5px;
        padding-left:7px;
        padding-right:7px;
        padding-top:5px;
        padding-bottom:5px;
        margin:5px;
        background-color: rgb(15, 170, 221);
        color: white;
        font-weight: bold;
    }

    .request-status
    {
        margin-left:7px;
        word-break: keep-all;
    }

    .crveno
    {
        background-color: rgb(255, 212, 212);
    }

    .zeleno
    {
        background-color: rgb(217, 250, 217);
    }

    .zuto
    {
        background-color: rgb(245, 245, 182);
    }

    .sivo
    {
        background-color: rgb(230, 230, 230);
    }

    .is-128x128 
    {
        width: 50px;
        height: 50px;
    }

    .rounded-image 
    {
        border-radius: 10px;
        height: 50px;
        width:50px;
        object-fit:cover;
        cursor: pointer;
    }

    .slika
    {
        width:18px;
        height:18px;
        margin-right:4px;
        margin-bottom:3px;
    }

    .dugme-rate
    {
        margin-right: 7px;
        margin-left: 7px;
    }

    @media only screen and (max-width: 900px)
    {
        .wrapper
        {
            width: 90%;
        }

        .request-top
        {
            flex-direction: column-reverse;
            align-items: flex-start;
        }

        .user-div
        {
            margin-left: 15px;
            margin-top: 10px;
            flex-direction: row-reverse;
        }

        .user-related-div
        {
            margin-top:15px;
            margin-bottom:15px;
        }

        .bottom-left
        {
            flex-direction: column;
            align-items: flex-start;
        }

        .request-bottom
        {
            margin-top: 10px;
        }
        
        .tagovi
        {
            margin-bottom: 5px;
            margin-left: 5px;
        }
        
        .request-date
        {
            padding: 2px;
            padding-left: 10px;
            font-size: 15px;
        }

        .user-name
        {
            margin-left: 10px;
        }

        .request-name, .request-name-2
        {
            font-size:20px;
        }

        .request-tag
        {
            font-size: 15px;
            font-weight:500;
        }

        .request-status
        {
            font-size: 16px;
        }
    }
</style>