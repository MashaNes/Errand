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
            },
            newFinishedRequest() {
                return this.$store.state.newFinishedRequest
            },
            newSuccessfullyFinishedRequest() {
                return this.$store.state.newSuccessfullyFinishedRequest
            },
            newEditAccepted() {
                return this.$store.state.newEditAccepted
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
        watch: {
            newFinishedRequest() 
            {
                if(this.tab == "Finished") 
                {
                    this.currentPage = 1
                    const filters = {
                        created_by : null,
                        done_by : null,
                        created_or_done_by: this.$store.state.authUser.id,
                        statuses : [2, 3],
                        unrated_created_by : null,
                        unrated_done_by: null
                    }
                    this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: 1, dataLoaded: true }})
                }
            },
            // eslint-disable-next-line no-unused-vars
            newSuccessfullyFinishedRequest(newVal, oldVal) 
            {
                if(newVal.bothUsersFinished && this.tab == "Finished")
                {
                    this.currentPage = 1
                    const filters = {
                        created_by : null,
                        done_by : null,
                        created_or_done_by: this.$store.state.authUser.id,
                        statuses : [2, 3],
                        unrated_created_by : null,
                        unrated_done_by: null
                    }
                    this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: 1, dataLoaded: true }})
                }
            },
            // eslint-disable-next-line no-unused-vars
            newEditAccepted(newVal, oldVal) {
                if(this.tab == 'Running' && this.$store.state.runnerAuthRequests) {
                    let ind = -1
                    ind = this.$store.state.runnerAuthRequests.results.findIndex(req => req.id == newVal.id)
                    if(ind != -1) {
                        this.$store.state.runnerAuthRequests.results.splice(ind, 1, newVal)
                    }
                }
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
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"runnerAuthRequests", page: 1, dataLoaded: false }})
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
                    this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: 1, dataLoaded: false }})
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
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: this.currentPage, dataLoaded: false }})
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
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"overAuthRequests", page: this.currentPage, dataLoaded: false }})
            },
            setSuccessMessages({textMessageS, textMessageE}) 
            {
                this.textMessageS = textMessageS
                this.textMessageE = textMessageE
                this.messagesSet = true
            },
            shouldSetFlags(type, type_id) {
                let ret = {}
                const currentRoute = this.$route.path
                if(type == 0 || type == 3 || type == 4) {
                    if(currentRoute == "/notifications") {
                        this.$store.state.notificationNumber -= 1
                        ret.setSeen = true
                        ret.setOpened = false
                    }
                    else ret = null
                    return ret
                }
                else if(((type == 1 || type == 6 || type == 7 || type == 10 || type == 2 || type == 5) && currentRoute == "/viewRequest/" + type_id) ||
                        (type == 8 && currentRoute == "/ratings/" + this.$store.state.authUser.id) ||
                        (type == 9 && currentRoute == "/achievements/" + this.$store.state.authUser.id)) {
                            this.$store.state.notificationNumber -= 1
                            ret.setSeen = true
                            ret.setOpened = true
                            return ret
                        }
                if(currentRoute == "/notifications") {
                    this.$store.state.notificationNumber -= 1
                    ret.setSeen = true
                    ret.setOpened = false
                    return ret
                }
                return null
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
                this.$store.dispatch("fillRequests", {filters: filters, objectToFill: { object:"createdAuthRequests", page: 1, dataLoaded: false }})

            let myToken = null;
            let vm = this;

            if(!this.$store.state.registeredOnFirebase) {
                this.$messaging.requestPermission()
                .then(function() {
                    //console.log("this is a notification")
                    return vm.$messaging.getToken()
                })
                .then(function(token) {
                    //console.log(token)
                    myToken = token
                    vm.$store.dispatch('firebaseRegister', myToken)
                })
                .catch(err => {
                    console.log(err)
                })

                const store = this.$store

                this.$store.state.firebaseOnMessageFunction = this.$messaging.onMessage(function(payload) {
                    if(payload.data.notification_type == 11) {
                        vm.$toasted.clear()
                        store.state.logedIn = false
                        vm.$router.push('/login')
                        if(vm.isAdmin)
                            store.dispatch("logoutUser")
                        else
                            store.dispatch("unregisterFromFirebase")
                        store.state.showModalBan = payload.data
                    }
                    else {
                        store.state.notificationNumber += 1
                        store.dispatch('fillFirebaseNotification', payload)
                        store.dispatch('notificationReaction', payload.data)
                        const flags = vm.shouldSetFlags(payload.data.notification_type, payload.data.type_id)
                        if(store.state.notifications != null) {
                            store.dispatch('fetchSpecificNotification', {id: payload.data.id, flags: flags})
                        }
                        if(flags != null)
                            store.dispatch("setNotificationFlag", {ids: [payload.data.id], opened: flags.setOpened, seen: flags.setSeen})
                        //console.log("app.vue notification")
                        //console.log(payload)
                    }
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