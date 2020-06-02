<template>
    <div class="handeled-report-wrapper">
        <div v-if="report.status == 4" class="dismissed">
            <i v-if="isSerbian"> Odbačena </i>
            <i v-else> Dismissed </i>
        </div>
        <div class="item" :class="{'opasnost' : report.status == 1 || report.status == 3}">
            <div class="bedz" v-if="report.status == 1 || report.status == 3">
                <span v-if="isSerbian"> Kaženjen(a) </span>
                <span v-else> Banned </span>
            </div>
            <div class="item-inner">
                <img src="../assets/send.svg" class="ikonica" />
                <span v-if="isSerbian" class="naziv"> Poslao/la: </span>
                <span v-else class="naziv"> Sender: </span>
                <a @click="gotoProfileReporter" class="clickable"> <span> {{report.created_by.first_name}} {{report.created_by.last_name}}, <span class="email-div"> {{report.created_by.email}} </span> </span> </a>
            </div>
        </div>
        <div class="item" :class="{'opasnost' : report.status == 2 || report.status == 3}">
            <div class="bedz" v-if="report.status == 2 || report.status == 3">
                <span v-if="isSerbian"> Kaženjen(a) </span>
                <span v-else> Banned </span>
            </div>
            <div class="item-inner">
                <img src="../assets/reports.svg" class="ikonica" />
                <span v-if="isSerbian" class="naziv"> Prijavljen(a): </span>
                <span v-else class="naziv"> Reported: </span>
                <a @click="gotoProfileReported" class="clickable"> <span> {{report.reported_user.first_name}} {{report.reported_user.last_name}}, <span class="email-div"> {{report.reported_user.email}} </span> </span> </a>
            </div>
        </div>
        <div class="item-different dodatak" v-if="report.request != null">
            <img src="../assets/requests.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Zahtev: </span>
            <span v-else class="naziv"> Request: </span>
            <span class="clickable"> <a @click="gotoRequest"> {{report.request.name}} </a> </span>
        </div>
        <div class="item-different" :class="{'dodatak' : report.pictures.length > 0}">
            <img src="../assets/comment.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Komentar: </span>
            <span v-else class="naziv"> Comment: </span>
            <span> {{report.comment}} </span>
        </div>
        <div class="item-different" v-if="report.pictures.length > 0">
            <img src="../assets/pictures.svg" class="ikonica" />
            <span v-if="isSerbian" class="naziv"> Slike: </span>
            <span v-else class="naziv"> Photos: </span>
            <div class="photo-list">
                <div v-for="(picture,index) in report.pictures" :key="index" class="photo">
                    <img :src="'data:;base64,' + picture.picture" class="photo1">
                </div> 
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        report:
        {
            type: Object,
            required: true
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        }
    },
    methods:
    {
        gotoRequest()
        {
            this.$router.push({ name: 'PageViewRequestAdmin', params: {request: this.report.request, id: this.report.request.id, editable: "regular"}})
        },
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
        }
    }
}
</script>

<style scoped>
    .handeled-report-wrapper
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

    .item-inner, .item-different
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        width:100%;
        padding:5px;
    }

    .dodatak
    {
        border-bottom: 1px solid black;
    }

    .item
    {
        border-bottom: 1px solid black;
        background-color: rgb(230, 230, 230);
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        justify-content: flex-start;
    }

    .ikonica
    {
        margin-right:10px;
    }

    .naziv
    {
        margin-right: 7px;
        font-weight: 600;
    }

    .dismissed
    {
        text-align: center;
        width:100%;
        background-color: rgb(230, 230, 230);
        font-size: 18px;
        font-weight: 600;
    }

    .opasnost
    {
        background-color: rgb(255, 212, 212);
    }

    .bedz
    {
        font-weight: 600;
        font-size:15px;
        padding-left: 7px;
        padding-right: 7px;
        padding-top: 3px;
        padding-bottom: 3px;
        margin: 7px;
        border: 1px solid black;
        background-color: white;
        color:red;
        border-radius: 30px;
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

    .clickable:hover
    {
        color: grey
    }

    .photo1
    {
        width:70px;
        height:100px;
    }

    @media only screen and (max-width: 900px)
    {
        .handeled-report-wrapper
        {
            width:98%;
        }
        
        .email-div
        {
            word-break: break-all;
        }
    }

    @media only screen and (max-width:650px)
    {
        .item
        {
            flex-direction: column-reverse;
        }
    }
</style>