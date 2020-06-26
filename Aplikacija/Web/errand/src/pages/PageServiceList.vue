<template>
    <Spinner v-if="this.$store.state.userServices == null || this.$store.state.services == null"/>
    <div class = "wrapper" v-else>
        <div class="nova-stavka">
            <NewUserService />
        </div>
        <div class="cenovnik">
            <ServiceList :editable="true"/>
        </div>
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import NewUserService from "@/components/NewUserService"
import ServiceList from "@/components/ServiceList"
export default {
    components:
    {
        NewUserService,
        ServiceList,
        Spinner
    },
    created()
    {
        if(this.$store.state.userServices == null)
            this.$store.dispatch("fillUserServices")
    }
}
</script>

<style scoped>
    .wrapper
    {
        padding-top:10px;
        padding-bottom: 10px;
        display:flex;
        flex-direction: row;
        justify-content: center;
        align-items: flex-start;
        width: 100%;
    }

    .cenovnik
    {
        width: 94%;
    }

    .nova-stavka
    {
        width: 420px;
        margin-top: 100px;
        padding:15px
    }

    @media only screen and (max-width: 1100px)
    {
        .wrapper
        {
            flex-direction: column;
            align-content: center;
            align-items: center;
        }

        .nova-stavka
        {
            width: 90%;
            margin-top:20px;
            padding:0px;
        }
    }
</style>