<template>
    <Spinner v-if="requests == null"/>
    <div class="wrapper-page" v-else>
        <i v-if="requests.length == 0 && isSerbian" class="natpis"> Trenutno nema nekategorizovanih zadataka </i>
        <i v-if="requests.length == 0 && !isSerbian" class="natpis"> There are no uncategorized tasks </i>
        <OtherRequestBox v-for="request in requests" :key="request.id" :request="request" />
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import OtherRequestBox from "@/components/OtherRequestBox"
export default {
    components:
    {
        Spinner,
        OtherRequestBox
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        requests()
        {
            return this.$store.state.overAuthRequests
        }
    },
    created()
    {
        this.$store.dispatch("fillOtherRequests")
        //pagintaion?
        //Nabavljanje jedne po jedne stranice i automatsko nadovezivanje pribavljenih podataka
    }
}
</script>

<style scoped>
    .wrapper-page
    {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;
        padding:30px;
    }

    .natpis
    {
        margin-top: 20px;
    }

    @media only screen and (max-width: 500px)
    {
        .wrapper-page
        {
            padding-left: 1px;
            padding-right: 1px;
        }
    }
</style>