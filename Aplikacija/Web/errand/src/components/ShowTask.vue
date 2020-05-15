<template>
    <div class="task-div">
        <div class="task-name"> {{task.name}} </div>
        <a @click="izmeni"> <img src="../assets/edit.svg" class="ikonica odlepi"/> </a>
        <a @click="showModal = true"> <img src="../assets/remove.svg" class="ikonica"/> </a>
        <ModalAreYouSure :naslovS="'Uklanjanje zadatka'" 
                         :tekstS="'Da li ste sigurni da želite da uklonite ovaj zadatak?'" 
                         :naslovE="'Removing a task'"
                         :tekstE="'Are you sure you want to remove this task?'"
                         @close="showModal = false" @yes="obrisi" v-if="showModal"/>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    props:
    {
        task:
        {
            type: Object,
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
            showModal:false
        }
    },
    methods:
    {
        obrisi()
        {
            this.$emit("obrisi", this.task)
        },
        izmeni()
        {
            this.$emit("izmeni", this.task)
        }
    } 
}
</script>

<style scoped>
    .task-div
    {
        width:96%;
        margin-top: 10px;
        margin-bottom: 10px;
        padding:7px;
        border-radius: 10px;
        border: 1px solid grey;
        background-color: rgb(240, 240, 183);
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
    }

    .task-name
    {
        width:100%;
        text-align: left;
    }

    .ikonica
    {
        width:20px;
        height:20px;
    }

    .odlepi
    {
        margin-right: 7px;
    }
</style>