<template>
    <div class="task-wrapper">
        <div class="task-top">
            <a @click="fokus" v-if="!isOpen"> <img src="../assets/down-chevron.svg" class="ikonica" /> </a>
            <a @click="close" v-else> <img src="../assets/up-chevron.svg" class="ikonica" /> </a>
            <div class="tag" v-if="!editable || myCategory.id != 1">
                <span v-if="isSerbian" v-b-popover.hover.bottom="myCategory.description_sr"> {{myCategory.service_type_sr}} </span>
                <span v-else v-b-popover.hover.bottom="myCategory.description_en"> {{myCategory.service_type_en}} </span>
            </div>
            <div class="tag-other" v-else>
                <span v-if="isSerbian"> {{myCategory.service_type_sr}} </span>
                <span v-else> {{myCategory.service_type_en}} </span>
            </div>
            <img v-if="editable && task.service_type.id == 1" src="../assets/edit.svg" />
            <span class="naziv">
                {{task.name}}
            </span>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        task:
        {
            type: Object,
            required: true
        },
        myNumber:
        {
            type: Number,
            required: true
        },
        currentNumber:
        {
            type: Number,
            required: true
        },
        editable:
        {
            type:Boolean,
            required:true
        }
    },
    data()
    {
        return{
            myCategory: this.task.service_type
        }
    },
    computed:
    {
        isOpen()
        {
            return this.myNumber == this.currentNumber
        },
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        wasEdited()
        {
            return this.myCategory.id != 1 && this.task.service_type.id == 1
        }
    },
    methods:
    {
        fokus()
        {
            this.$emit("fokus")
        },
        close()
        {
            this.$emit("close")
        }
    }
}
</script>

<style scoped>
    .task-wrapper
    {
        width:100%;
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
    }

    .task-top
    {
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        width:100%;
        padding: 7px;
        border:1px solid black;
    }

    .naziv
    {
        text-align: left;
        margin-left:7px;
        font-size:18px;
        font-weight: 600;
        width:100%;
    }

    .ikonica
    {
        width:24px;
        height:24px;
    }

    .tag
    {
        border-radius: 5px;
        padding-left:7px;
        padding-right:7px;
        padding-top:5px;
        padding-bottom:5px;
        margin:5px;
        margin-right:15px;
        background-color: rgb(15, 170, 221);
        color: white;
        font-weight: bold;
        font-size: 16px;
    }

    .tag-other
    {
        border-radius: 5px;
        padding-left:7px;
        padding-right:7px;
        padding-top:5px;
        padding-bottom:5px;
        margin:5px;
        margin-right:15px;
        background-color: rgb(148, 25, 25);
        color: white;
        font-weight: bold;
        font-size: 16px;
        display: flex;
        flex-direction: row;
        align-items: center;
        height:fit-content;
    }

    .zvezdica
    {
        font-size: 30px;
        margin-right:7px;
        font-weight: 700;
    }
</style>