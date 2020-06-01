<template>
    <div class="page-wrapper">
        <div class="navigacija">
            <a class="tab" :class="{'aktivan' : activeTab == 0}" @click="activeTab = 0">
                <span v-if="isSerbian"> Aktivne </span>
                <span v-else> Active </span>
            </a>
            <a class="tab" :class="{'aktivan' : activeTab == 1}" @click="switchToHandeledReports">
                <span v-if="isSerbian"> Obrađene </span>
                <span v-else> Handeled </span>
            </a>
        </div>
        <Spinner v-if="activeTab == 0 && activeReports == null" />
        <div class="global-div" v-else>
            <div class="content-wrapper" v-if="activeTab == 0">
                <ActiveReport v-for="report in activeReports" :key="report.id" :report="report" />
                <i v-if="activeReports.length == 0 && isSerbian" class="natpis"> Trenutno nema aktivnih prijava </i>
                <i v-if="activeReports.length == 0 && !isSerbian" class="natpis"> No active reports to display </i>
            </div>
            <div class="content-wrapper" v-else>
                <div class="pretraga">
                    <div class="pretraga-podatak">
                        <img src="../assets/send.svg" class="ikonica" />:
                        <div class="input-div">
                            <input type="email" v-model="searchData.sender" class="email-unos"
                                   @blur="$v.searchData.sender.$touch()" />
                            <span v-if="isSerbian && $v.searchData.sender.$invalid" class="upozorenje"> Email nije validan</span>
                            <span v-if="!isSerbian && $v.searchData.sender.$invalid" class="upozorenje"> Not a valid email</span>
                            <span v-if="isSerbian"> Email pošiljaoca</span>
                            <span v-else> Sender's email</span>
                        </div>
                    </div>
                    <div class="pretraga-podatak">
                        <img src="../assets/reports.svg" class="ikonica" />:
                        <div class="input-div">
                            <input type="email" v-model="searchData.reported" class="email-unos"
                                   @blur="$v.searchData.reported.$touch()" />
                            <span v-if="isSerbian && $v.searchData.reported.$invalid" class="upozorenje"> Email nije validan</span>
                            <span v-if="!isSerbian && $v.searchData.reported.$invalid" class="upozorenje"> Not a valid email</span>
                            <span v-if="isSerbian"> Email prijavljenog</span>
                            <span v-else> Reported's email</span>
                        </div>
                    </div>
                    <div class="pretraga-dugme">
                        <button type="button" class="btn btn-info" :disabled="$v.searchData.$invalid" @click="searchHandeledReports">
                            <span v-if="isSerbian"> Pretraži </span>
                            <span v-else> Search </span>
                        </button>
                    </div>
                </div>
                <b-pagination v-model="currentPage" :total-rows="handeledReports == null? 1 : handeledReports.count" :per-page="10" align="center" class="pag-top" @input="getAnotherPortion">
                </b-pagination>
                <Spinner v-if="handeledReports == null || !isDataLoaded" />
                <div class="handeledReports" v-else>
                    <HandeledReport v-for="report in handeledReports.results" :key="report.id" :report="report" />
                    <i v-if="handeledReports.results.length == 0 && isSerbian" class="natpis"> Trenutno nema obrađenih prijava </i>
                    <i v-if="handeledReports.results.length == 0 && !isSerbian" class="natpis"> No handeled reports to display </i>
                </div>
                <b-pagination v-model="currentPage" :total-rows="handeledReports == null? 1 : handeledReports.count" :per-page="10" align="center" class="pag-bottom">
                </b-pagination>
            </div>
        </div>
    </div>
</template>

<script>
import {email} from "vuelidate/lib/validators"
import Spinner from "@/components/Spinner"
import ActiveReport from "@/components/ActiveReport"
import HandeledReport from "@/components/HandeledReport"
export default {
    data()
    {
        return{
            activeTab: 0,
            searchData:
            {
                sender: "",
                reported: ""
            },
            currentPage : 1
        }
    },
    components:
    {
        Spinner,
        ActiveReport,
        HandeledReport
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
        },
        isDataLoaded()
        {
            return this.$store.state.isDataLoaded
        }
    },
    methods:
    {
        switchToHandeledReports()
        {
            this.activeTab = 1;
            this.$store.dispatch("fillHandeledReports", {reporter: null, reported: null, page: 1})
        },
        searchHandeledReports()
        {
            var reporter = this.searchData.sender == '' ? null : this.searchData.sender
            var reported = this.searchData.reported == '' ? null : this.searchData.reported
            this.$store.dispatch("fillHandeledReports", {reporter: reporter, reported: reported, page: 1})
            //with search parameters
        },
        getAnotherPortion()
        {
            var reporter = this.searchData.sender == '' ? null : this.searchData.sender
            var reported = this.searchData.reported == '' ? null : this.searchData.reported
            this.$store.dispatch("fillHandeledReports", {reporter: reporter, reported: reported, page: this.currentPage})
            //with search parameters
            //with page equal to currentPage
        }
    },
    validations:
    {
        searchData:
        {
            sender: {email},
            reported: {email}
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

    .pretraga
    {
        display: flex;
        flex-direction: row;
        justify-content: space-evenly;
        width:80%;
        padding: 7px;
        margin-bottom: 30px;
        border:1px solid black;
        border-radius: 5px;
        background-color: white;
    }

    .pretraga-podatak
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: flex-start;
    }

    .pretraga-dugme
    {
        align-self: center;
    }

    .input-div
    {
        margin-left:15px;
        display: flex;
        flex-direction: column;
        align-items:center
    }

    .email-unos
    {
        width:250px;
    }

    .ikonica
    {
        width:30px;
        height:30px;
    }

    .upozorenje
    {
        color:red;
        font-size: 12px;
    }

    .pag-top 
    {
        margin-bottom: 40px;
        margin-top: 20px;
        z-index:0;
    }

    .pag-bottom 
    {
        margin: 40px 0 0px 0;
        z-index:0;
        padding-bottom: 40px;
    }

    .natpis
    {
        margin-top: 20px;
    }

    .handeledReports
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    @media only screen and (max-width: 950px)
    {
        .pretraga
        {
            width:98%;
        }
    }

    @media only screen and (max-width: 800px)
    {
        .pretraga
        {
            flex-direction: column;
        }

        .pretraga-podatak
        {
            margin-bottom: 10px;
        }
        
        .email-unos
        {
            width:350px;
        }
    }

    @media only screen and (max-width: 500px)
    {
        .content-wrapper
        {
            padding-left: 1px;
            padding-right: 1px;
        }

        .email-unos
        {
            width:100%;
        }
    }
</style>