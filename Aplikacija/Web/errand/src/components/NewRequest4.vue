<template>
    <div class="wrapper-step3">
        <div v-if="isSerbian" class="obavestenje">
            <span class="napomena"> Napomena: </span> Proverite sve do sada unete informacije o zahtevu, nakon ovog odabira više nećete moći da se vratite da ih menjate
        </div>
        <div v-else class="obavestenje">
            Note: Check all of the information you have provided about the request so far, after making this choice you will no longer have the ability to change them
        </div>
        <div v-if="isSerbian" class="pitanje">
            Da li želite da se Vaš zahtev prikaže svima koji zadovoljavaju navedene kriterijume?
        </div>
        <div v-else class="pitanje">
            Do you want this request to be shown to all of the users who fit the stated criteria?
        </div>
        <div class="buttonDiv">
            <button type="button" class="btn btn-success direktno" v-if="isSerbian" @click="grupno">
                <img src="../assets/finished.svg" class="slika">
                Da
            </button>
            <button type="button" class="btn btn-success direktno" v-else @click="grupno">
                <img src="../assets/finished.svg" class="slika">
                Yes
            </button>
            <button type="button" class="btn btn-danger" v-if="isSerbian" @click="neGrupno">
                <img src="../assets/failed.svg" class="slika">
                Ne
            </button>
            <button type="button" class="btn btn-danger" v-else @click="neGrupno">
                <img src="../assets/failed.svg" class="slika">
                No
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
            broadcast: null,
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
            if(this.broadcast)
                return "Želim opciju prikaza grupi korisnika"
            else
                return "Ne želim opciju prikaza grupi korisnika"
        },
        naslovE()
        {
            if(this.broadcast)
                return "I want to use broadcast"
            else
                return "I do not want to use broadcast"
        }
    },
    methods:
    {
        grupno()
        {
            this.broadcast = true
            this.showModal = true
        },
        neGrupno()
        {
            this.broadcast = false
            this.showModal = true
        },
        yes()
        {
            this.$emit("broadcastChanged", this.broadcast)
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

    .obavestenje
    {
        font-size: 14px;
        color:red;
    }

    .napomena
    {
        font-weight: 600;
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