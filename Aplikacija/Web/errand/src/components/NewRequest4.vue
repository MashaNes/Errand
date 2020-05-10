<template>
    <div class="wrapper-step3">
        <div v-if="isSerbian" class="pitanje">
            Da li želite nekome direktno da pošaljete zahtev ili da se on prikaže svima koji zadovoljavaju navedene kriterijume?
        </div>
        <div v-else class="pitanje">
            Do you want to send this request to a specific user or do you want it to be shown to all of the users who fit the stated criteria?
        </div>
        <div class="buttonDiv">
            <button type="button" class="btn btn-info direktno" v-if="isSerbian" @click="direktno">
                <img src="../assets/direct.png" class="slika">
                Direktno
            </button>
            <button type="button" class="btn btn-info direktno" v-else @click="direktno">
                <img src="../assets/direct.png" class="slika">
                Direct
            </button>
            <button type="button" class="btn btn-info" v-if="isSerbian" @click="grupno">
                <img src="../assets/broadcast.png" class="slika">
                Grupi
            </button>
            <button type="button" class="btn btn-info" v-else @click="grupno">
                <img src="../assets/broadcast.png" class="slika">
                Broadcast
            </button>
        </div>
        <ModalAreYouSure v-if="showModal==true"
                         :naslovS="naslovS"
                         :naslovE="naslovE"
                         :tekstS="'Da li ste sigurni? Nakon ovog odabira se više ne možete vratiti nazad i menjati zahtev.'"
                         :tekstE="'Are you sure? After making this choice you will no longer be able to go back and make changes to the request.'"
                         @yes="yes"
                         @close="showModal = false"/>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
export default {
    data()
    {
        return{
            direct: null,
            showModal: false
        }
    },
    components:
    {
        ModalAreYouSure
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        naslovS()
        {
            if(this.direct)
                return "Odabrali ste direktno slanje"
            else
                return "Odabrali ste prikaz grupi"
        },
        naslovE()
        {
            if(this.direct)
                return "You choose direct sending"
            else
                return "You choose criteria based broadcast"
        }
    },
    methods:
    {
        direktno()
        {
            this.direct = true
            this.showModal = true
        },
        grupno()
        {
            this.direct = false
            this.showModal = true
        },
        yes()
        {
            this.$emit("moveOn", this.direct)
            this.showModal = false
        }
    }
}
</script>

<style scoped>
    .wrapper-step3
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .pitanje
    {
        text-align: justify;
        margin-bottom:10px;
        padding: 15px;
    }

    .buttonDiv
    {
        width:100%;
        display:flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin-bottom:20px;
    }

    .direktno
    {
        margin-right: 30px;
    }

    .slika
    {
        width: 20px;
        height:20px;
        margin-right:7px;
    }
</style>