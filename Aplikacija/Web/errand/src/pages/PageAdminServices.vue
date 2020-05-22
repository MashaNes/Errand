<template>
    <Spinner v-if="servicesList == null" />
    <div class="page-wrapper" v-else>
        <div class="new-service">
            <button type="button" class="btn btn-success" v-if="!isAdding" @click="isAdding = true">
                <img src="../assets/add.svg" class="slika">
                <span v-if="isSerbian"> Kreiraj novu uslugu </span>
                <span v-else> Create a new service </span>
            </button>
            <NewService v-else @zatvori="isAdding = false"/>
        </div>
        <div class="services">
            <Service v-for="service in servicesList"
                     :key="service.id"
                     :service="service" />
        </div>
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import Service from "@/components/Service"
import NewService from "@/components/NewService"
export default {
    data()
    {
        return{
            isAdding: false
        }
    },
    components:
    {
        Spinner,
        Service,
        NewService
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        servicesList()
        {
            return this.$store.state.allServices
        }
    },
    created()
    {
        if(this.$store.state.allServices == null)
            this.$store.dispatch("fillServices")
    }
}
</script>
<style scoped>
    .page-wrapper
    {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding:20px;
    }

    .new-service
    {
        width:100%;
        display:flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        padding:10px;
    }

    .services
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        width: 100%;
        padding:10px;
    }

    @media only screen and (max-width: 450px)
    {
        .page-wrapper
        {
            padding-left: 0;
            padding-right: 0;
        }
    }
</style>