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
        <div class="request-div">
            <RequestBox v-for="request in requests" :key="request.id" :identifikator="request.id"/>
        </div>
        <ModalSuccess v-if="showModal"
                      :textS="'Zahtev uspešno kreiran.'"
                      :textE="'Request successfully created.'"
                      @close="closeModal" />
    </div>
</template>

<script>
    import ModalSuccess from "@/components/ModalSuccess"
    import RequestBox from "@/components/RequestBox"
    export default {
        components:
        {
            RequestBox,
            ModalSuccess
        },
        computed:
        {
            requests() 
            {
                var requests_temp = []
                if(this.tab == "Requested")
                {
                    Object.values(this.$store.state.requests).forEach(request => {
                        if((request.status == "running" || request.status == "pending") && (!request.runner))
                            requests_temp.push(request)
                    });
                }
                else if(this.tab == "Running")
                {
                    Object.values(this.$store.state.requests).forEach(request => {
                        if((request.status == "running" || request.status == "pending") && (request.runner))
                            requests_temp.push(request)
                    });
                }
                else{
                    Object.values(this.$store.state.requests).forEach(request => {
                        if(request.status == "finished" || request.status == "failed")
                            requests_temp.push(request)
                    });
                }
                return requests_temp
            },
            isSerbian()
            {
                return this.$store.state.isSerbian
            }
        },
        data()
        {
            return{
                tab:"Requested",
                showModal: this.$store.state.userAdded
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
                //ako nisu popunjeni requests koje izvrsavam, get za njih (morace da budu posebna stavka u store-i, a mozda i ovde)
            },
            tabFinished()
            {
                this.tab = "Finished"
                //ako nisu popunjeni get za zavrsene zahteve (morace da budu posebna stavka u store-i, a mozda i ovde)
            },
            closeModal()
            {
                this.$store.state.userAdded = false
            }
        },
        created()
        {
            this.$store.dispatch("fillRequests")
            //get za requests (koje sam ja zahtevao)
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
</style>