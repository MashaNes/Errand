<template>
    <div class="userService">
        <div class="editovanje" v-if="editable">
            <a class="link-editovanje"> <img class="razdvoji" src="../assets/remove.svg"> </a>
            <a class="link-editovanje"> <img src="../assets/edit.svg"> </a>
        </div>
        <div class="telo">
            <p class="ime">{{userService.service.serviceType}}</p>
            <div class="grid-div">
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Maksimalno rastojanje: </span>
                        <span v-else> Maximum distance: </span>
                    </div>
                    <div class="right-div"> 
                        {{userService.maxDist}} km
                    </div>
                </div>
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Tip naplate: </span>
                        <span v-else> Payment type: </span>
                    </div>
                    <div class="right-div"> 
                        {{paymentType}}
                    </div>
                </div>
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Cena: </span>
                        <span v-else> Payment amount: </span>
                    </div>
                    <div class="right-div"> 
                        {{userService.paymentAmount}} rsd
                    </div>
                </div>
                <div class="row-div">
                    <div class="left-div"> 
                        <span v-if="isSerbian"> Minimalna ocena: </span>
                        <span v-else> Minimum rating: </span>
                    </div>
                    <div class="right-div"> 
                        {{userService.minRating}}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        editable:
        {
            type: Boolean,
            required: true
        },
        userService:
        {
            type: Object,
            required:true
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        paymentType()
        {
            if(this.isSerbian)
            {
                if(this.userService.paymentType == 0)
                    return "po satu"
                else if(this.userService.paymentType == 1)
                    return "po kilometru"
                else if(this.userService.paymentType == 1)
                    return "fiksna"
                else
                    return "početna"
            }
            else
            {
                if(this.userService.paymentType == 0)
                    return "per hour"
                else if(this.userService.paymentType == 1)
                    return "per kilometer"
                else if(this.userService.paymentType == 1)
                    return "fixed"
                else
                    return "starting"
            }
        }
    }   
}
</script>

<style scoped>
    .userService
    {
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        justify-content: center;
        width: 380px;
        height: 200px;
        background-color: white;
        border: 1px solid grey;
        border-radius: 5px;
        margin: 10px;
        padding: 10px;
    }

    .telo
    {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        height: 100%;
    }

    .editovanje
    {
        height: 100%;
        width: fit-content;
        display: flex;
        flex-direction: column;
        align-content: flex-start;
    }

    .link-editovanje
    {
        height: fit-content;
    }

    .razdvoji
    {
        margin-bottom: 15px;
    }

    .ime
    {
        font-size: 20px;
        font-weight: 750;
        color: rgb(48, 89, 165);
    }

    .grid-div
    {
        margin-left: 20px;
        height: 150px;
        width: 285px;
        padding-top: 7px;
        display: flex;
        flex-direction: column;
    }

    .row-div
    {
        flex-grow: 1;
        flex-shrink: 1;
        width: 100%;
        margin-bottom: 7px;
        align-items: center;
        display: flex;
        flex-direction: row;
    }

    .left-div
    {
        text-align: center;
        width: 170px;
    }

    .right-div
    {
        text-align: center;
        width: 115px;
    }

    @media only screen and (max-width: 450px)
    {
        .userService
        {
            width: 110%;
        }

        .grid-div
        {
            margin-left: 5px;
        }

        .telo{
            width: 92%;
        }

        .left-div
        {
            width: 50%;
        }
    }
</style>