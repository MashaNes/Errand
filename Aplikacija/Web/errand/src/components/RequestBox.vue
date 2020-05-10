<template>
    <div class = "wrapper" :class="color">
        <div class="request-top" v-if="myRequest.status != 'pending'">
            <div class="user-div"> 
                <span class="user-name" @click="goToProfile"> {{userName}} </span>
                <p class="image is-128x128" >
                    <img class="rounded-image" :src="userPicture" @click="goToProfile">
                </p>
            </div>
            <div class = "request-name" @click="goToRequest">
                {{myRequest.name}}
            </div>
        </div>
        <div class="request-top2" v-else>
            <a class = "cancel-request" @click="showModal = true">
                <img class = "ikonica" src="../assets/remove.svg">
            </a>
            <div class = "request-name" @click="goToRequest">
                {{myRequest.name}}
            </div>
        </div>
        <div class="request-bottom">
            <div class="status-div">
                <img v-if="myRequest.status == 'running'" src = "../assets/running.svg">
                <img v-if="myRequest.status == 'pending'" src = "../assets/pending.svg">
                <img v-if="myRequest.status == 'finished'" src = "../assets/finished.svg">
                <img v-if="myRequest.status == 'failed'" src = "../assets/failed.svg">
                <span class = "request-status"> {{myRequest.status}} </span>
            </div>
            <div class = "bottom-left">
                <div class = "request-date"> {{myRequest.date | showTime}} </div>
                <div class = "tagovi">
                    <div v-for="tag in myRequest.tags" :key="tag" class = "request-tag">{{tag}}</div>
                </div>
            </div>
        </div>
        <ModalAreYouSure :naslovS="'Brisanje zahteva'" 
                         :tekstS="'Da li ste sigurni da želite da obrišete ovaj zahtev?'" 
                         :naslovE="'Deleting a request'"
                         :tekstE="'Are you sure you want to delete this request?'"
                         @close="showModal = false" @yes="deleteRequest" v-if="showModal"/>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    props:
    {
        identifikator:{
            type:Number,
            required:true
        }
    },
    components:
    {
        ModalAreYouSure
    },
    data()
    {
        return{
            myRequest: this.$store.state.requests[this.identifikator],
            showModal: false
        }
    },
    computed:
    {
        color()
        {
            if (this.myRequest.status == "finished")
                return "zeleno"
            else if(this.myRequest.status == "failed")
                return "crveno"
            else if(this.myRequest.status == "pending")
                return "sivo"
            else
                return "zuto"
        },
        userName()
        {
            if(this.myRequest.status != "pending")
                return this.myRequest.user.first_name + " " + this.myRequest.user.last_name
            else
                return ""
        },
        userPicture()
        {
            if(this.myRequest.status != "pending")
                return this.myRequest.user.picture;
            else
                return ""
        },
        userId() 
        {
            if(this.myRequest.status != "pending")
                return this.myRequest.user.id
            else
                return ""
        }
    },
    methods:
    {
        deleteRequest()
        {
            console.log(this.myRequest)
            this.showModal = false
            //ukloniti request iz odgovajace liste iz store-a
            //poslati delete ka bazi
        },
        goToProfile()
        {
            this.$router.push({
                name: "PageViewProfile", 
                params: {
                    id: this.myRequest.user.id, 
                    user: this.myRequest.user
                }
            })
        },
        goToRequest() 
        {
            this.$router.push({
                name: "PageViewRequest",
                params: {
                    id: this.myRequest.id,
                    request: this.myRequest
                }
            })
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
        align-items: center;
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

    .cancel-request
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-items: center;
        margin-right: 10px;
        font-size: 18px;
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
    }
    
    .bottom-left
    {
        flex-grow: 1;
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
    }
    
    .request-date
    {
        padding: 10px;
        text-align: center;
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

    @media only screen and (max-width: 600px)
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
        }

        .user-name
        {
            margin-left: 10px;
        }
    }

    .request-name
    {
        font-weight: bold;
        font-size: 25px;
        color:rgb(11, 97, 126);
        text-align: left;
        flex-grow: 1;
        margin-left: 10px;
        margin-top:2px;
    }

    .request-name:hover
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
        margin:5px;
        background-color: rgb(15, 170, 221);
        color: white;
        font-weight: bold;
    }

    .request-status
    {
        margin-left:7px;
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
</style>