<template>
    <div class="other-request-wrapper">
        <span class="naziv"> {{request.name}} </span>
        <div class="user-data">
            <div class="name">
                <img src="../assets/requests.svg" class="ikonica" />
                {{request.created_by.first_name}} {{request.created_by.last_name}}, 
            </div>
            {{request.created_by.email}}
        </div>
        <div class="user-data">
            <div class="name">
                <img src="../assets/running.svg" class="ikonica" />
                {{request.working_with.first_name}} {{request.working_with.last_name}}, 
            </div>
            {{request.working_with.email}}
        </div>
        <div class="tagovi">
            <div class="tag" v-for="tag in tags" :key="tag.id">
                <span v-if="isSerbian"> {{tag.service_type_sr}} </span>
                <span v-else> {{tag.service_type_en}} </span>
            </div>
            <div class="tag-other">
                <span v-if="isSerbian"> Ostalo </span>
                <span v-else> Other </span>
                <div class="count">
                    {{otherCount}}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        request:
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
        },
        tags()
        {
            let returnValue = []
            var ids = []
            this.request.tasklist.forEach(task => 
            {
                var contains = false
                ids.forEach(element =>
                {
                    if(element == task.service_type.id)
                        contains = true
                })

                if(!contains && task.service_type.id != 1)
                {
                    ids.push(task.service_type.id)
                    returnValue.push(task.service_type)
                }
            })
            return returnValue
        },
        otherCount()
        {
            var rez = 0
            this.request.tasklist.forEach(task => 
            {
                if(task.service_type.id == 1)
                    rez = rez + 1
            })
            return rez
        }
    }
}
</script>

<style scoped>
    .other-request-wrapper
    {
        display:flex;
        flex-direction: column;
        width:600px;
        padding:10px;
        margin: 15px;
        align-items: flex-start;
        border: 1px solid black;
        border-radius: 10px;
        background-color:white;
    }

    .naziv
    {
        font-size: 22px;
        font-weight:600;
        margin-bottom:15px;
    }

    .user-data
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        margin-bottom:10px;
    }

    .ikonica
    {
        margin-right: 10px;
    }

    .name
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        margin-right:7px;
        font-size: 18px;
    }

    .tagovi
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-start;
        flex-wrap: wrap;
    }

    .tag
    {
        border-radius: 5px;
        padding-left:7px;
        padding-right:7px;
        padding-top:5px;
        padding-bottom:5px;
        margin:5px;
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
        background-color: rgb(148, 25, 25);
        color: white;
        font-weight: bold;
        font-size: 16px;
        display: flex;
        flex-direction: row;
        align-items: center;
        height:fit-content;
    }

    .count
    {
        background-color: white;
        color:black;
        border-radius:20px;
        padding:0px;
        margin:3px;
        margin-left: 7px;
        padding-left: 7px;
        padding-right: 7px;
        font-size: 14px;
    }

    @media only screen and (max-width: 900px)
    {
        .user-data
        {
            flex-direction: column;
            align-items: flex-start;
        }
    }
</style>