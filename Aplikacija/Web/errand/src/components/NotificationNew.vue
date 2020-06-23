<template>
    <div class="notification-wrapper">
        <div class="title-div">
            <span v-if="isSerbian"> {{notification.title_sr}} </span>
            <span v-else> {{notification.title_en}} </span>
        </div>
        <div class="linka"></div>
        <div class="body-div">
            <div v-if="notification.working_with != 0" class="image is-128x128">
                <!-- <img class="rounded-image" :src="notification.working_with.picture ? 'data:;base64,' + notification.working_with.picture : require('../assets/no-picture.png')" @click="goToProfile"> -->
            </div>
            <span v-if="isSerbian"> {{notification.body_sr}} </span>
            <span v-else> {{notification.body_en}} </span>
        </div>
        <div class="footer-div">
            <span v-if="isSerbian"> {{dateAndTime}}</span>
            <span v-else> {{notificationTime  | showTime}}</span>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        notification:
        {
            required: true,
            type: Object
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        android()
        {
            if(this.isSerbian)
                return "Ovo obaveštenje možete otvoriti samo u okviru Errand aplikacije na svom mobilnom telefonu"
            else
                return "You can only open this notification in the Errand mobile app"
        },
        notificationTime()
        {
            var date = new Date(this.notification.timestamp)
            return date.toString()
        },
        dateAndTime()
        {
            var date = new Date(this.notification.timestamp)
            
            var day = date.getDate()
            var month = date.getMonth()+1
            var year = date.getFullYear()
            var hours = date.getHours()
            var minutes = date.getMinutes()
            
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
        goToProfile()
        {
            this.$router.push({
                name: "PageViewProfile", 
                params: {
                    id: this.notification.working_with.id, 
                    user: this.notification.working_with
                }
            })
        }
    }
}
</script>

<style scoped>
    .notification-wrapper
    {
        width:80%;
        padding:20px;
        background-color: rgb(245, 245, 182);
        display:flex;
        flex-direction: column;
        align-items: flex-start;
        border-radius:10px;
    }

    .title-div
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        width:100%;
        font-weight: 700;
        font-size: 22px;
    }

    .body-div
    {
        margin-top: 20px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        width:100%;
    }

    .linka
    {
        height:0px;
        width:300px;
        border-bottom: 1px solid rgb(46, 45, 45);
    }

    .footer-div
    {
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        justify-content: flex-start;
        margin-top:7px;
        width:100%;
    }

    .is-128x128 
    {
        width: 50px;
        height: 50px;
        margin-right:10px;
    }

    .rounded-image 
    {
        border-radius: 10px;
        height: 50px;
        width:50px;
        object-fit:cover;
        cursor: pointer;
    }

    @media only screen and (max-width: 800px)
    {
        .notification-wrapper
        {
            width:98%;
        }
    }

     @media only screen and (max-width: 550px)
    {
        .linka
        {
            width:94%;
        }
    }
</style>