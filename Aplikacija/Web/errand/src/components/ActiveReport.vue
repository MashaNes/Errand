<template>
    <div class="active-report-wrapper">
        <div class="item">
            <img src="../assets/send.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Poslao/la: </span>
            <span v-else class="naziv"> Sender: </span>
            <a @click="gotoProfileReporter" class="clickable"> <span> {{report.created_by.first_name}} {{report.created_by.last_name}}, <span class="email-div"> {{report.created_by.email}} </span> </span> </a>
        </div>
        <div class="item">
            <img src="../assets/reports.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Prijavljen(a): </span>
            <span v-else class="naziv"> Reported: </span>
            <a @click="gotoProfileReported" class="clickable"> <span> {{report.reported_user.first_name}} {{report.reported_user.last_name}}, <span class="email-div"> {{report.reported_user.email}} </span> </span> </a>
        </div>
        <div class="item" v-if="report.request != null">
            <img src="../assets/requests.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Zahtev: </span>
            <span v-else class="naziv"> Request: </span>
            <span class="clickable"> <a @click="gotoRequest"> {{report.request.name}} </a> </span>
        </div>
        <div class="item">
            <img src="../assets/comment.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Komentar: </span>
            <span v-else class="naziv"> Comment: </span>
            <span> {{report.comment}} </span>
        </div>
        <div class="item" v-if="report.pictures.length > 0">
            <img src="../assets/pictures.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Slike: </span>
            <span v-else class="naziv"> Photos: </span>
            <div class="photo-list">
                <div v-for="(picture,index) in pictures" :key="index" class="photo" @click="expandPicture(index)">
                    <img :src="picture.src" class="photo1">
                </div> 
            </div>
        </div>
        <div class="button-div">
            <button type="button" class="btn btn-danger" @click="showModalBan = true">
                <span v-if="isSerbian"> Odredi zabranu </span>
                <span v-else> Ban a user </span>
            </button>
            <button type="button" class="btn btn-secondary donje-dugme" @click="showModalDismiss = true">
                <span v-if="isSerbian"> Odbaci prijavu </span>
                <span v-else> Dismiss the report </span>
            </button>
        </div>
        <ModalAreYouSure v-if="showModalDismiss==true"
                         :naslovS="'Odbaciti prijavju'"
                         :naslovE="'Dismissing the report'"
                         :tekstS="'Da li ste sigurni da želite da odbacite ovu prijavu?'"
                         :tekstE="'Are you sure you want to dismiss this report?'"
                         @yes="dismiss"
                         @close="showModalDismiss = false"/>
        <ModalBan v-if="showModalBan == true" 
                  @yes="ban"
                  @close="showModalBan = false"/>
        <LightBox :media="pictures" v-if="pictureExpanded" 
                  :startAt="clickedPicture" @onClosed="pictureExpanded = false"></LightBox>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
import ModalBan from "@/components/ModalBan"
import LightBox from 'vue-image-lightbox'
export default {
    props:
    {
        report:
        {
            type: Object,
            required:true
        }
    },
    data()
    {
        return{
            showModalDismiss: false,
            showModalBan: false,
            clickedPicture: null,
            pictureExpanded: false
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        pictures() 
        {
            const retValue = []
            if(this.report.pictures) 
            {
                this.report.pictures.forEach(pic => {
                    retValue.push({
                        thumb: 'data:;base64,' + pic.picture,
                        src: 'data:;base64,' + pic.picture
                    })
                })
            }
            return retValue
        }
    },
    components:
    {
        ModalAreYouSure,
        ModalBan,
        LightBox
    },
    methods:
    {
        gotoProfileReported()
        {
            this.$router.push({
                name: "PageViewProfile", 
                params: {
                    id: this.report.reported_user.id, 
                    user: this.report.reported_user
                }
            })
        },
        gotoProfileReporter()
        {
            this.$router.push({
                name: "PageViewProfile", 
                params: {
                    id: this.report.created_by.id, 
                    user: this.report.created_by
                }
            })
        },
        gotoRequest()
        {
            this.$router.push({ name: 'PageViewRequestAdmin', params: {request: this.report.request, id: this.report.request.id, editable: "regular"}})
        },
        dismiss()
        {
            this.showModalDismiss = false
            this.$store.state.activeReports.forEach((element,index) => {
                if(element.id == this.report.id)
                    this.$store.state.activeReports.splice(index,1)
            });
            this.$store.dispatch("handleReport", {id: this.report.id, status: 4})
        },
        ban(information)
        {
            this.showModalBan = false
            this.$store.state.activeReports.forEach((element,index) => {
                if(element.id == this.report.id)
                    this.$store.state.activeReports.splice(index,1)
            });
            var status = 0
            if(information.sender.flag)
                status = 1
            if(information.reported.flag)
                status += 2
            console.log(status)
            this.$store.dispatch("handleReport", {id: this.report.id, status: status})
            if(information.sender.flag)
            {
                const date = new Date();
                var finalDate = new Date(date.valueOf())
                if(information.sender.time == 0)
                    finalDate.setDate(date.getDate() + parseInt(information.sender.number))
                else if(information.sender.time == 1)
                    finalDate = new Date(date.setMonth(date.getMonth() + parseInt(information.sender.number)))
                else
                    finalDate = new Date(date.setFullYear(date.getFullYear() + parseInt(information.sender.number)))
                
                var day = finalDate.getDate()
                var month = finalDate.getMonth()+1
                var year = finalDate.getFullYear()
                var hours = finalDate.getHours()
                var minutes = finalDate.getMinutes()
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

                console.log(year + "-" + monthString + "-" + dayString + " " + hoursString + ":" + minutesString)
                console.log(information.sender.comment)
                console.log(this.report.created_by)
                this.$store.dispatch("banUser", {user: this.report.created_by.id, date: finalDate, comment: information.sender.comment})
                //Posalji ka bazi kreiranje bana za report.created_by, information.sender.comment i finalDate
                //proveriti u kom formatu ide datum
            }
            if(information.reported.flag)
            {
                const date = new Date();
                var finalDateR = new Date(date.valueOf())
                if(information.reported.time == 0)
                    finalDateR.setDate(date.getDate() + parseInt(information.reported.number))
                else if(information.reported.time == 1)
                    finalDateR = new Date(date.setMonth(date.getMonth() + parseInt(information.reported.number)))
                else
                    finalDateR = new Date(date.setFullYear(date.getFullYear() + parseInt(information.reported.number)))

                day = finalDateR.getDate()
                month = finalDateR.getMonth()+1
                year = finalDateR.getFullYear()
                hours = finalDateR.getHours()
                minutes = finalDateR.getMinutes()
                monthString = month
                dayString = day
                hoursString = hours
                minutesString = minutes
                if(month < 10)
                    monthString = "0" + month
                if(day < 10)
                    dayString = "0" + day
                if(hours < 10)
                    hoursString = "0" + hours
                if(minutes < 10)
                    minutesString = "0" + minutes

                console.log(year + "-" + monthString + "-" + dayString + " " + hoursString + ":" + minutesString)
                console.log(information.reported.comment)
                console.log(this.report.reported_user)
                this.$store.dispatch("banUser", {user: this.report.reported_user.id, date: finalDateR, comment: information.reported.comment})
                //Posalji ka bazi kreiranje bana za report.reported_user, information.reported.comment i finalDateR
                //proveriti u kom formatu ide datum
            }
        },
        expandPicture(index)
        {
            this.clickedPicture = index
            this.pictureExpanded = true
        }
    }
}
</script>

<style scoped>
    .active-report-wrapper
    {
        border:1px solid black;
        border-radius: 10px;
        padding:10px;
        display: flex;
        flex-direction: column;
        width:60%;
        margin-bottom: 15px;
        margin-top: 15px;
        background-color: white;
    }
    
    .item
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        width:100%;
        border-bottom: 1px solid black;
        padding:5px;
    }

    .ikonica
    {
        margin-right:10px;
        width:24px;
        height:24px;
    }

    .naziv
    {
        margin-right: 7px;
        font-weight: 600;
    }

    .button-div
    {
        width:100%;
        display: flex;
        flex-direction: row;
        padding: 7px;
        align-items: center;
        justify-content: space-around;
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
        margin: 7px;
        width: fit-content;
        height: fit-content;
        border-radius: 5px;
    }

    .photo1
    {
        width: 70px;
        height: 70px;
        border-radius: 5px;
        border: 1px solid grey;
    }

    .photo1:hover
    {
        border: 1px solid black;
        cursor: pointer;
    }

    .clickable:hover
    {
        color: grey
    }

    @media only screen and (max-width: 900px)
    {
        .active-report-wrapper
        {
            width:98%;
        }

        .email-div
        {
            word-break: break-all;
        }
    }

    @media only screen and (max-width: 450px)
    {
        .button-div
        {
            flex-direction: column;
        }

        .donje-dugme
        {
            margin-top: 10px;
        }
    }

</style>