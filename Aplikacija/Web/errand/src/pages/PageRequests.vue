<template>
    <div>
        <div class = "top-div" v-if="isSerbian"> Zahtevi </div>
        <div class = "top-div" v-else> Requests </div>
        <nav class="tabovi">
            <div class="container">
                <div class="navbar-menu">
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
                </div>
            </div>
        </nav>
        
        <RequestBox v-for="request in requests" :key="request.id" :identifikator="request.id"/>
    </div>
</template>

<script>
    import RequestBox from "@/components/RequestBox"
    export default {
        components:
        {
            RequestBox
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
                tab:"Requested"
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
            },
            tabFinished()
            {
                this.tab = "Finished"
            }
        }
    }
</script>

<style scoped>
    .tabovi
    {
        background-color: white;
        padding: 7px;
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
    }

    .aktivan
    {
        background-color: rgb(233, 233, 233);
        text-decoration: underline;
    }
</style>