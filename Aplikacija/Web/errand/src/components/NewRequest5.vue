<template>
    <div class="wrapper-step3">
        <div v-if="isSerbian" class="pitanje">
            Da li želite da pošaljete Vaš zahtev nekom konkretnom korisniku kao notifikaciju?
        </div>
        <div v-else class="pitanje">
            Do you want to send this request to a specific user as a notification?
        </div>
        <div class="buttonDiv">
            <button type="button" class="btn btn-success direktno" v-if="isSerbian" @click="direktno">
                <img src="../assets/finished.svg" class="slika">
                Da
            </button>
            <button type="button" class="btn btn-success direktno" v-else @click="direktno">
                <img src="../assets/finished.svg" class="slika">
                Yes
            </button>
            <button type="button" class="btn btn-danger" v-if="isSerbian" @click="neDirektno">
                <img src="../assets/failed.svg" class="slika">
                Ne
            </button>
            <button type="button" class="btn btn-danger" v-else @click="neDirektno">
                <img src="../assets/failed.svg" class="slika">
                No
            </button>
        </div>
        <ModalAreYouSure v-if="showModal==true"
                         :naslovS="naslovS"
                         :naslovE="naslovE"
                         :tekstS="'Da li ste sigurni?'"
                         :tekstE="'Are you sure?'"
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
                return "Želim opciju direktnog slanja"
            else
                return "Ne želim opciju direktnog slanja"
        },
        naslovE()
        {
            if(this.direct)
                return "I want to use direct sending"
            else
                return "I do not want to use direct sending"
        }
    },
    methods:
    {
        direktno()
        {
            this.direct = true
            this.showModal = true
        },
        neDirektno()
        {
            this.direct = false
            this.showModal = true
        },
        yes()
        {
            this.$emit("directChanged", this.direct)
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

    .pitanje, .obavestenje
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