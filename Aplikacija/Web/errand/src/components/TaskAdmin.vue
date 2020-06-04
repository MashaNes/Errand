<template>
    <div class="task-wrapper">
        <div class="task-top">
            <div class="task-top-content">
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
                <a @click="isEditing = true"> <img v-if="editable && task.service_type.id == 1 && !isEditing" src="../assets/edit.svg" /> </a>
                <span class="naziv">
                    {{task.name}}
                </span>
            </div>
            <div class="odabir-kategorije" v-if="isEditing">
                <div class="opcije"> 
                    <v-select :options="services" label="service_type_sr" v-model="myCategory" class="selekt" v-if="isSerbian" :clearable="false"></v-select> 
                    <v-select :options="services" label="service_type_en" v-model="myCategory" class="selekt" v-else :clearable="false"></v-select> 
                    <img src="../assets/info.svg" v-if="myCategory != null && isSerbian" v-b-popover.hover.bottom="myCategory.description_sr">
                    <img src="../assets/info.svg" v-if="myCategory != null && !isSerbian" v-b-popover.hover.bottom="myCategory.description_en">
                </div>
                <button type="button" class="btn btn-success dugme" @click="finish">
                    <img src="../assets/finished.svg" />
                </button>
            </div>
        </div>
        <div class="task-bottom" v-if="isOpen">
            <div class="row-order">
                <img src="../assets/signature.svg" class="ikonica"/>
                <span v-if="isSerbian" class="ime"> Opis: </span>
                <span v-else class="ime"> Description: </span>
                {{task.description}}
            </div>
            <div class="spisak-i-slike" v-if="(task.checklist!= null && task.checklist.length) || (task.pictures!= null && task.pictures.length)">
                <div class="spisak" v-if="task.checklist!= null && task.checklist.length > 0">
                    <ul class="lista">
                        <li v-for="item in task.checklist" :key="item.id"> <i> {{item.check_list}} </i> </li>
                    </ul>
                </div>
                <div class="slike" v-if="task.pictures!= null && task.pictures.length">
                    <div v-for="(picture,index) in task.pictures" :key="index" class="photo">
                        {{picture}}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
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
    components:
    {
        vSelect
    },
    data()
    {
        return{
            myCategory: this.task.service_type,
            isEditing: false,
            sendEdit: true
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
        },
        services()
        {
            return this.$store.state.allServices
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
        },
        finish()
        {
            this.isEditing = false
            this.$emit("newCategory", {task: this.task.id, category: this.myCategory})
            if(this.wasEdited && this.sendEdit)
            {
                this.sendEdit = false
                this.$emit("editedChanged", this.wasEdited)
            }
            else if(!this.wasEdited && !this.sendEdit)
            {
                this.sendEdit = true
                this.$emit("editedChanged", this.wasEdited)
            }
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
        flex-direction: column;
        align-items: center;
        border:1px solid black;
    }

    .task-top-content
    {
        display: flex;
        flex-direction: row-reverse;
        align-items: center;
        width:100%;
        padding: 7px;
    }

    .task-bottom
    {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        width:100%;
        padding:12px;
        border-bottom: 1px solid black;
        border-left: 1px solid black;
        border-right: 1px solid black;
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

    .row-order
    {
        width:100%;
    }

    .ime
    {
        font-size: 16px;
        font-weight:600;
    }

    .ikonica
    {
        width:20px;
        height:20px;
        margin-right:5px;
    }

    .spisak-i-slike
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        width:100%;
        margin-top:15px;
    }

    .spisak
    {
        background-color: rgb(252, 234, 210);
        border-radius: 7px;
        flex-grow: 1;
        margin-right:10px;
        padding:7px;
    }

    .lista
    {
        list-style: disc;
        margin-left:17px;
    }

    .slike
    {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;
        flex-grow: 1;   
    }

    .photo
    {
        margin:7px;
        width:70px;
        height:100px;
        border:0.5px solid black;
    }

    .selekt
    {
        width: 80%;
        margin-right: 7px;
        word-break:break-all;
        background-color: rgb(236, 236, 236);
    }

    .opcije
    {
        display: flex;
        flex-direction: row;
        width: 80%;
        justify-content: center;
        align-items: center;
        margin-bottom: 15px;
        word-break:break-all;
    }

    .odabir-kategorije
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        width:100%;
    }

    .dugme
    {
        padding:0px;
        margin-bottom: 15px;
    }

    @media only screen and (max-width: 1000px)
    {
        .spisak-i-slike
        {
            flex-direction: column;
            align-items: center;
        }

        .spisak
        {
            width:90%;
        }
    }
</style>