<template>
    <div class="wrapper-step3">
        <NewTask v-if="isEmpty || isAdding" :task="taskToShow" 
                 :showCancelButton="isAdding"
                 @sacuvaj="sacuvajTask"
                 @odustani="odustani"/>
        <ShowTask v-else v-for="task in myTaskList" :key="task.id" :task="task"
                  @obrisi="obrisiTask" @izmeni="izmeniTask"/>
        <div class="button-div-3">
            <button type="button" class="btn btn-success" v-if="!isAdding && !isEmpty" @click="newTask">
                <span v-if="isSerbian">Novi zadatak</span>
                <span v-else>New task</span>
            </button>
        </div>
    </div>
</template>

<script>
import NewTask from "@/components/NewTask"
import ShowTask from "@/components/ShowTask"
export default {
    props:
    {
        tasklist:
        {
            type: Array,
            required: true
        }
    },
    components:
    {
        NewTask,
        ShowTask
    },
    data()
    {
        return{
            myTaskList: this.tasklist,
            isAdding: false,
            idGenerator: -1,
            idToShow : 0,
            taskToShow: 
            {
                id: 0,
                name: "",
                service_type: null,
                description: "",
                checklist: [],
                picture_required: false,
                pictures: "",
                adress: null
            }
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        isEmpty()
        {
            return this.myTaskList.length == 0
        },
        TaskToShow()
        {
            if(this.idToShow != 0)
            {
                this.myTaskList.forEach(element => {
                    if(element.id == this.idToShow)
                        return element
                });
            }
            return {
                id: 0,
                name: "",
                service_type: null,
                description: "",
                checklist: [],
                picture_required: false,
                pictures: "",
                adress: null
            }
        }
    },
    methods:
    {
        odustani()
        {
            this.isAdding = false
        },
        sacuvajTask(task)
        {
            if(task.id == 0)
            {
                task.id = this.idGenerator
                this.idGenerator = this.idGenerator - 1
                this.myTaskList.push(task)
            }
            else
            {
                this.myTaskList.forEach((element, index) =>
                {
                    if(element.id == task.id)
                        this.myTaskList.splice(index,1)
                })
                this.myTaskList.push(task)
            }
            this.isAdding = false
            this.$emit("tasklistChanged", this.myTaskList)
        },
        newTask()
        {
            this.taskToShow = 
                {
                    id: 0,
                    name: "",
                    service_type: null,
                    description: "",
                    checklist: [],
                    picture_required: false,
                    pictures: "",
                    adress: null
                }
            this.isAdding = true
        },
        obrisiTask(task)
        {
            this.myTaskList.forEach((element, index) =>
                {
                    if(element.id == task.id)
                        this.myTaskList.splice(index,1)
                })
            this.$emit("tasklistChanged", this.myTaskList)
            this.taskToShow = 
                {
                    id: 0,
                    name: "",
                    service_type: null,
                    description: "",
                    checklist: [],
                    picture_required: false,
                    pictures: "",
                    adress: null
                }
        },
        izmeniTask(task)
        {
            var resultTask = null
            this.myTaskList.forEach(element =>
                {
                    if(element.id == task.id)
                        resultTask = element
                })
            this.taskToShow = resultTask
            this.isAdding = true
        }
    }
}
</script>

<style scoped>
    .wrapper-step3
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom:30px;
    }

    .button-div-3
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        padding: 5px;
    }
</style>