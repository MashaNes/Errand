<template>
    <div class="wrapper">
        <div class="dodavanje-wrap">
            <label class = "login-label" v-if="isAdding"> Email: </label>
            <v-select :options="emails" label="Email" v-model="email" class="selekt" v-if="isAdding"></v-select>
            <div class="dugmici">
                <button type="button" @click = "dodajKorisnika" class="btn btn-primary dugme" :disabled="isAdding && !email" v-if="isSerbian">Dodaj korisnika</button>
                <button type="button" @click = "dodajKorisnika" class="btn btn-primary dugme" :disabled="isAdding && !email" v-else>Add a user</button>
                <button type="button" v-if="isAdding && isSerbian" @click="odustani" class="btn btn-secondary dugme">Odustani</button>
                <button type="button" v-if="isAdding && !isSerbian" @click="odustani" class="btn btn-secondary dugme">Cancel</button>
            </div>
        </div>
        <div class="prikaz-wrap">
            <div class="natpis">
                <img class = "slika" src="../assets/discount.png">
                <span v-if="isSerbian"> Lista povlašćenih korisnika</span>
                <span v-else> List of users with benefits</span>
            </div>
            <div class="users">
                <UserBenefitBox :userBenefit="user" v-for="user in BenefitedUsers" :key="user.id"/>
            </div>
        </div>
    </div>
</template>

<script>
import UserBenefitBox from "@/components/UserBenefitBox"
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
export default {
    data()
    {
        return{
            isAdding: false,
            email: "",
        }
    },
    components:
    {
        UserBenefitBox,
        vSelect
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        BenefitedUsers() 
        {
            return this.$store.state.usersWithBenefit
        },
        emails()
        {
            return this.$store.state.emails.emails
        }
    },
    methods:
    {
        dodajKorisnika()
        {
            if(!this.isAdding)
                this.isAdding = true;
            else
            {
                console.log(this.email)
                this.email = null
                this.isAdding = false;
            }
        },
        odustani()
        {
            this.email = null
            this.isAdding = false
        }
    },
    created()
    {
        this.$store.dispatch("fillEmails")
        console.log(this.$store.state.emails)
    }
}
</script>

<style scoped>
    .wrapper
    {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        width: 100%;
        padding-top: 30px;
        padding-bottom: 30px;
    }

    .dodavanje-wrap
    {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 10px;
        padding-bottom: 20px;
    }

    .users 
    {
        display: flex; 
        flex-direction:column; 
        align-items:stretch;
    }

    .prikaz-wrap
    {
        width: 100%;
        display: flex;
        flex-direction: column;
    }

    .natpis
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        font-size: 30px;
        font-weight: 700;
        text-align: center;
    }

    .slika
    {
        width: 50px;
        height: 50px;
        margin: 5px;
        margin-left: 20px;
    }

    .login-label
    {
        margin-left: 5px;
        font-size: 18px;
    }

    .dodavanje
    {
        margin-bottom: 15px;
        width: 84%;
    }

    .selekt
    {
        width: 84%;
        margin-bottom: 20px;
        background-color: white;
        word-break:break-all;
    }

    .dugmici
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
    }

    .dugme
    {
        margin:5px;
    }
</style>