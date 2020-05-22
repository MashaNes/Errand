<template>
    <div class="service-div">
        <div class="editovanje" v-if="!isEditing">
            <a class="link-editovanje" @click="isEditing = true"> <img src="../assets/edit.svg"> </a>
        </div>
        <div class="content-div">
            <div class="label-div">
                <img src = "../assets/serbia.svg" class="zastava">
                Naziv:
            </div>
            <div class="text-div" v-if="!isEditing">
                {{service.service_type_sr}}
            </div>
            <input class="text-div" v-else v-model="myService.service_type_sr" />
            <div class="label-div">
                <img src = "../assets/uk.svg" class="zastava">
                Name:
            </div>
            <div class="text-div" v-if="!isEditing">
                {{service.service_type_en}}
            </div>
            <input class="text-div" v-else v-model="myService.service_type_en" />
            <div class="label-div">
                <img src = "../assets/serbia.svg" class="zastava">
                Opis:
            </div>
            <div class="text-div" v-if="!isEditing">
                {{service.description_sr}}
            </div>
            <textarea class="text-div" v-else v-model="myService.description_sr"> </textarea>
            <div class="label-div">
                <img src = "../assets/uk.svg" class="zastava">
                Description:
            </div>
            <div class="text-div" v-if="!isEditing">
                {{service.description_en}}
            </div>
            <textarea class="text-div" v-else v-model="myService.description_en"> </textarea>
            <div class="button-div" v-if="isEditing">
                <button type="button" class="btn btn-danger" @click="odbaciIzmenu">
                    <img src="../assets/failed.svg" class="slika">
                </button>
                <button type="button" class="btn btn-success" @click="sacuvajIzmenu">
                    <img src="../assets/finished.svg" class="slika">
                </button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        service :
        {
            type: Object,
            required: true
        }
    },
    data()
    {
        return{
            myService:
            {
                service_type_sr : this.service.service_type_sr,
                service_type_en : this.service.service_type_en,
                description_sr: this.service.description_sr,
                description_en: this.service.description_en
            },
            isEditing: false
        }
    },
    methods:
    {
        odbaciIzmenu()
        {
            this.myService.service_type_sr = this.service.service_type_sr
            this.myService.service_type_en = this.service.service_type_en
            this.myService.description_sr = this.service.description_sr
            this.myService.description_en = this.service.description_en
            this.isEditing = false
        },
        sacuvajIzmenu()
        {
            this.service.service_type_sr = this.myService.service_type_sr
            this.service.service_type_en = this.myService.service_type_en
            this.service.description_sr = this.myService.description_sr
            this.service.description_en = this.myService.description_en
            //poslati edit u bazu
            this.isEditing = false
        }
    }
}
</script>

<style scoped>
    .service-div
    {
        border: 1px solid black;
        border-radius: 10px;
        display: flex;
        flex-direction: row-reverse;
        justify-content: center;
        padding:10px;
        width:400px;
        background-color: white;
        margin:20px;
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
        z-index: 1;
    }

    .content-div
    {
        display: flex;
        flex-direction: column;
        padding-right: 10px;
        width:100%;
    }

    .label-div
    {
        font-weight: 700;
        font-size: 18px;
    }

    .text-div
    {
        width:100%;
        text-align: justify;
        margin-bottom:7px;
    }

    .button-div
    {
        margin:7px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-around;
    }

    .zastava
    {
        height: 20px;
        margin-right: 5px;
    }

    @media only screen and (max-width: 450px)
    {
        .service-div
        {
            margin-left: 1%;
            margin-right:1%;
            width:98%;
        }
    }
</style>