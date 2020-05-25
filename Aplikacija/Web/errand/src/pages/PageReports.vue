<template>
    <div class="page-wrapper">
        <div class="navigacija">
            <a class="tab" :class="{'aktivan' : activeTab == 0}" @click="activeTab = 0">
                <span v-if="isSerbian"> Aktivne </span>
                <span v-else> Active </span>
            </a>
            <a class="tab" :class="{'aktivan' : activeTab == 1}" @click="activeTab = 1">
                <span v-if="isSerbian"> Obrađene </span>
                <span v-else> Handeled </span>
            </a>
        </div>
        <Spinner v-if="activeTab == 0 && activeReports == null" />
        <div class="global-div" v-else>
            <div class="content-wrapper" v-if="activeTab == 0">
                <ActiveReport v-for="report in activeReports" :key="report.id" :report="report" />
            </div>
        </div>
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import ActiveReport from "@/components/ActiveReport"
export default {
    data()
    {
        return{
            activeTab: 0
        }
    },
    components:
    {
        Spinner,
        ActiveReport
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        activeReports()
        {
            return this.$store.state.activeReports
        },
        handeledReports()
        {
            return this.$store.state.handeledReports
        }
    },
    created()
    {
        this.$store.dispatch("fillActiveReports")
    }
}
</script>

<style scoped>
    .page-wrapper
    {
        display: flex;
        flex-direction: column;
        width:100%;
    }

    .navigacija
    {
        display: flex;
        flex-direction: row;
        width:100%;
        background-color: white;
        align-items: center;
        height:50px;
    }

    .tab
    {
        text-align: center;
        width:50%;
        height:100%;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        font-size: 20px;
        font-weight: 700;
    }

    .tab:hover
    {
        text-decoration: underline;
    }

    .aktivan
    {
        background-color: rgb(233, 233, 233);
        text-decoration: underline;
    }

    .content-wrapper
    {
        padding:20px;
        display: flex;
        flex-direction: column;
        align-items: center;
        width:100%;
    }

    .global-div
    {
        width:100%
    }

    @media only screen and (max-width: 450px)
    {
        .content-wrapper
        {
            padding-left: 1px;
            padding-right: 1px;
        }
    }
</style>