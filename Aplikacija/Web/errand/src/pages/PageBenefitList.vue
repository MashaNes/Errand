<template>
    <Spinner v-if="this.$store.state.usersWithBenefit == null"/>
    <div class="wrapper" v-else>
        <div class="dodavanje-wrap">
            <div class="dugmici">
                <button type="button" @click = "dodajKorisnika" class="btn btn-primary dugme" v-if="isSerbian">Dodaj korisnika</button>
                <button type="button" @click = "dodajKorisnika" class="btn btn-primary dugme" v-else>Add a user</button>
            </div>
        </div>
        <div class="prikaz-wrap">
            <div class="natpis">
                <img class = "slika" src="../assets/discount.png">
                <span v-if="isSerbian"> Lista povlašćenih korisnika</span>
                <span v-else> List of users with benefits</span>
            </div>
            <div class="users">
                <UserBenefitBox :userBenefit="benefit" v-for="benefit in BenefitedUsers" :key="benefit.id"/>
            </div>
        </div>
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import UserBenefitBox from "@/components/UserBenefitBox"
export default {
    components:
    {
        UserBenefitBox,
        Spinner
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
            this.$router.push({ name: 'PageBrowseUsers', params: {benefitList: "benefit"}})
        }
    },
    created()
    {
        if(this.$store.state.usersWithBenefit == null)
            this.$store.dispatch("fillUsersWithBenefit")
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