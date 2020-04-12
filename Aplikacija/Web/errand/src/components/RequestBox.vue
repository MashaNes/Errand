<template>
    <div class = "wrapper">
        <div class = "request-name"> {{myRequest.name}} </div>
        <div id="navbarBasicExample" class="navbar-menu">
            <span class = "request-date"> {{myRequest.date | showTime}} </span>
            <span v-for="tag in myRequest.tags" :key="tag" class = "request-tag">{{tag}}</span>
            <span class="navbar-end">
                <img v-if="myRequest.status == 'running'" src = "../assets/running.svg">
                <img v-if="myRequest.status == 'pending'" src = "../assets/pending.svg">
                <img v-if="myRequest.status == 'finished'" src = "../assets/finished.svg">
                <img v-if="myRequest.status == 'failed'" src = "../assets/failed.svg">
                <span class = "request-status"> {{myRequest.status}} </span>
            </span>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        identifikator:{
            type:Number,
            required:true
        }
    },
    data()
    {
        return{
            myRequest: this.$store.state.requests[this.identifikator]
        }
    }
}
</script>

<style scoped>
    .wrapper
    {
        margin:30px;
        background-color: white;
        margin-left:150px;
        margin-right:150px;
        border: 2px solid rgb(43, 41, 41);
        border-radius: 15px;
        min-height: 50px;
        padding:10px
    }

    .request-name
    {
        margin:10px;
        font-weight: bold;
        font-size: 25px;
        color:rgb(11, 97, 126);
    }

    .request-date
    {
        padding: 10px;
        text-align: center;

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

    .navbar-menu
    {
        height: 40px;
    }

    .request-status
    {
        padding:10px
    }
</style>