<template>
    <div>
        <nav class="tabovi">
            <a v-if="tab == 'Requested'" class="navbar-item aktivan" href="#">
                <span v-if="isSerbian">Zahtevali ste</span>
                <span v-else>Requested</span>
            </a>
            <a v-else class="navbar-item" href="#" @click="tabRequested">
                <span v-if="isSerbian">Zahtevali ste</span>
                <span v-else>Requested</span>
            </a>
            <a v-if="tab == 'Running'" class="navbar-item aktivan" href="#">
                <span v-if="isSerbian">Izvršavate</span>
                <span v-else>Running</span>
            </a>
            <a v-else class="navbar-item" href="#" @click="tabRunning">
                <span v-if="isSerbian">Izvršavate</span>
                <span v-else>Running</span>
            </a>
            <a v-if="tab == 'Finished'" class="navbar-item aktivan" href="#">
                <span v-if="isSerbian">Završeni</span>
                <span v-else>Finished</span>
            </a>
            <a v-else class="navbar-item" href="#" @click="tabFinished">
                <span v-if="isSerbian">Završeni</span>
                <span v-else>Finished</span>
            </a>
        </nav>
        <div class="button-div">
            <b-pagination v-if="tab =='Finished'"
                        v-model="currentPage" 
                        :total-rows="requests == null? 1 : requests.count" 
                        :per-page="10" 
                        align="center" class="pag-top"
                        @input="getAnotherPortion">
            </b-pagination>
        </div>
        <Spinner v-if="!requests || (tab == 'Finished' && !isDataLoaded)" />
        <div class="request-div" v-else>
            <RequestBox v-for="request in requests.results" :key="request.id" :myRequest="request" @setMessages="setSuccessMessages"/>
            <i v-if="requests.results.length == 0 && tab == 'Finished' && isSerbian" class="natpis"> Trenutno nema zahteva u ovoj kategoriji </i>
            <i v-if="requests.results.length == 0 && tab == 'Finished' && !isSerbian" class="natpis"> No requests to display in this category </i>
        </div>
        <div class="button-div">
            <b-pagination v-if="tab =='Finished'"
                        v-model="currentPage" 
                        :total-rows="requests == null? 1 : requests.count" 
                        :per-page="10" 
                        align="center" class="pag-bottom">
            </b-pagination>
        </div>
        <!--<div v-if="requests != null && tab =='Finished' && moreRequests && isDataLoaded" class="button-div">
            <button type="button" class="btn btn-info" @click="loadMore">
                <span v-if="isSerbian"> Učitaj još </span>
                <span v-else> Load more </span>
            </button>
        </div>
        <div v-if="requests != null && tab == 'Finished' && !isDataLoaded" class="button-div">
            <button type="button" class="btn btn-info" :disabled="true">
                <span v-if="isSerbian"> Učitavanje... </span>
                <span v-else> Loading... </span>
            </button>
        </div>-->
        <div v-if="requests != null && requests.results.length == 0 && tab !='Finished'" class="button-div">
            <button type="button" class="btn btn-success" @click="newRequest">
                <img src="../assets/add.svg" class="slika">
                <span v-if="isSerbian"> Novi zahtev </span>
                <span v-else> New request </span>
            </button>
        </div>
        <ModalSuccess v-if="requestCreated"
                      :textS="'Zahtev uspešno kreiran.'"
                      :textE="'Request successfully created.'"
                      @close="closeModal" />
        <ModalSuccess v-if="success" 
                      :textS="textMessageS" 
                      :textE="textMessageE" 
                      @close="closeModalSuccess" />
    </div>
</template>

<script>
    import ModalSuccess from "@/components/ModalSuccess"
    import RequestBox from "@/components/RequestBox"
    import Spinner from "@/components/Spinner"
    export default {
        components:
        {
            RequestBox,
            ModalSuccess,
            Spinner
        },
        computed:
        {
            requests() 
            {
                var requests_temp = []
                if(this.tab == "Requested")
                {
                    if(!this.$store.state.createdAuthRequests)
                        requests_temp = null
                    else
                        requests_temp = this.$store.state.createdAuthRequests
                        
                }
                else if(this.tab == "Running")
                {
                    if(!this.$store.state.runnerAuthRequests)
                        requests_temp = null
                    else
                        requests_temp = this.$store.state.runnerAuthRequests
                }
                else{
                    if(!this.$store.state.overAuthRequests)
                        requests_temp = null
                    else
                        requests_temp = this.$store.state.overAuthRequests
                }
                return requests_temp
            },
            isSerbian()
            {
                return this.$store.state.isSerbian
            },
            requestCreated()
            {
                return this.$store.state.userAdded
            },
            success() 
            {
                return this.$store.state.success
            },
            isDataLoaded()
            {
                return this.$store.state.isDataLoaded
            },
            isOnPageOne()
            {
                return this.$store.state.onPageOne
            }
        },
        data()
        {
            return{
                tab:"Requested",
                currentPage : 1,
                textMessageS: "",
                textmMessageE: ""
            }
        },
        methods:{
            tabRequested()
            {
                this.tab = "Requested"
            },
            tabRunning()
            {
                this.tab = "Running"
                const filters = {
                    created_by : null,
                    done_by : this.$store.state.authUser.id,
                    created_or_done_by: null,
                    statuses : [1],
                    unrated_created_by : null,
                    unrated_done_by: null
                }
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"runnerAuthRequests", page: 1 }})
            },
            tabFinished()
            {
                this.currentPage = 1
                this.tab = "Finished"
                const filters = {
                    created_by : null,
                    done_by : null,
                    created_or_done_by: this.$store.state.authUser.id,
                    statuses : [2, 3],
                    unrated_created_by : null,
                    unrated_done_by: null
                }
                //if(this.$store.state.overAuthRequests == null || !this.isOnPageOne)
                    this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: 1 }})
            },
            closeModal()
            {
                this.$store.state.userAdded = false
            },
            newRequest()
            {
                this.$router.push("/newRequest")
            },
            closeModalSuccess() 
            {
                this.$store.state.success = false
            },
            loadMore()
            {
                this.currentPage++
                const filters = {
                    created_by : null,
                    done_by : null,
                    created_or_done_by: this.$store.state.authUser.id,
                    statuses : [2, 3],
                    unrated_created_by : null,
                    unrated_done_by: null
                }
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: this.currentPage }})
            },
            getAnotherPortion()
            {
                window.scrollTo(0, 0)
                const filters = {
                    created_by : null,
                    done_by : null,
                    created_or_done_by: this.$store.state.authUser.id,
                    statuses : [2, 3],
                    unrated_created_by : null,
                    unrated_done_by: null
                }
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: this.currentPage }})
            },
            setSuccessMessages({textMessageS, textMessageE}) 
            {
                this.textMessageS = textMessageS
                this.textMessageE = textMessageE
                this.messagesSet = true
            }
        },
        created()
        {
            if(this.$store.state.notificationNumber == -1)
            {
                this.$store.dispatch("getNotificationNumber")
            }
            const filters = {
                created_by : this.$store.state.authUser.id,
                done_by : null,
                created_or_done_by: null,
                statuses : [0, 1],
                unrated_created_by : null,
                unrated_done_by: null
            }
            if(this.$store.state.createdAuthRequests == null)
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"createdAuthRequests", page: 1 }})

            let myToken = null;
            let vm = this;

            if(!this.$store.state.registeredOnFirebase) {
                this.$messaging.requestPermission()
                .then(function() {
                    console.log("this is a notification")
                    return vm.$messaging.getToken()
                })
                .then(function(token) {
                    console.log(token)
                    myToken = token
                    vm.$store.dispatch('firebaseRegister', myToken)
                })
                .catch(err => {
                    console.log(err)
                })

                // eslint-disable-next-line no-unused-vars
                this.$store.state.firebaseOnMessageFunction = this.$messaging.onMessage(function(data) {
                    // eslint-disable-next-line no-debugger
                    //debugger
                    console.log(data)
                })
            }
        }
    }
</script>

<style scoped>
    .request-div
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        justify-items: center;
        align-content: center;
        margin-bottom: 30px;
    }

    .tabovi
    {
        background-color: white;
        padding: 7px;
        min-height: 50px;
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin-bottom: 30px;
    }
    
    
    .navbar-menu
    {
        display:flex;
    }

    .navbar-item
    {
        flex-grow: 1;
        flex-shrink: 1;
        justify-content: center;
        font-weight: 400;
        font-size: 20px;
        height: 100%;
        text-align: center;
    }

    .aktivan
    {
        background-color: rgb(233, 233, 233);
        text-decoration: underline;
    }

    .button-div
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        width: 100%;
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
</style>