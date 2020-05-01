<template>
    <div class="wrapper">
        <div class="dodavanje-wrap">
            <!--<label class = "login-label" v-if="isAdding"> Email: </label>-->
            <input type="email"
                    v-if="isAdding"
                    placeholder="Email"
                    autofocus=""
                    autocomplete="email"
                    v-model="email"
                    class="dodavanje">
            <button type="button" @click = "dodajKorisnika" class="btn btn-primary">Dodaj korisnika</button>
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
export default {
    data()
    {
        return{
            isAdding: false,
            email: ""
        }
    },
    components:
    {
        UserBenefitBox
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
                this.isAdding = false;
            }
        }
    }
}
</script>

<style scoped>
    .wrapper
    {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        width: 100%;
        padding-top: 30px;
        padding-bottom: 30px;
    }

    .dodavanje-wrap
    {
        width: 350px;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 100px;
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

    @media only screen and (max-width: 900px)
    {
        .wrapper
        {
            flex-direction: column;
            align-items: center;
        }

        .dodavanje-wrap
        {
            padding-top: 10px;
            padding-bottom: 20px;
            width: 100%;
        }
    }
</style>