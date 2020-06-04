<template>
    <Spinner v-if="Achievements == null" />
    <div class="page-wrapper" v-else>
        <div class="newAchievement">
            <button type="button" class="btn btn-success dugme" v-if="!isAdding" @click="isAdding = true">
                <img src="../assets/add.svg" class="slika">
                <span v-if="isSerbian"> Kreiraj novo dostignuće </span>
                <span v-else> Create a new achievement </span>
            </button>
            <NewAchievement v-else @achievemntCreated="isAdding=false"/>
        </div>
        <div class="achievement-detail" v-if="achievementDetails != null">
            <div class="content"> <AchievementAdmin :achievement="achievementDetails" :noDetail="true" @closeDetails="achievementDetails = null"/> </div>
            <table class="table tabela">
                <thead>
                    <tr>
                        <th  class="poravnanje" scope="col"><span v-if="isSerbian">Nivo/Uslovi</span> <span v-else>Level</span></th>
                        <th  class="poravnanje" scope="col" v-for="(condition,index) in achievementDetails.conditions" :key="condition">
                            <span v-b-popover.hover.top="achievementConditions[index]"> {{index + 1}} </span>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(nothing,index) in achievementDetails.condition_numbers[0]" :key="index">
                        <td class="poravnanje">{{index + 1}}</td>
                        <td class="poravnanje" v-for="(nada,number) in achievementDetails.condition_numbers" :key="number"> {{achievementDetails.condition_numbers[number][index]}} </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="allAchievements">
            <AchievementAdmin v-for="achievement in Achievements" :key="achievement.id" :achievement="achievement" :noDetail="false" @openDetails="openDetails"/>
        </div>
    </div>
</template>

<script>
import Spinner from "@/components/Spinner"
import NewAchievement from "@/components/NewAchievement"
import AchievementAdmin from "@/components/AchievementAdmin"
export default {
    components:
    {
        Spinner,
        AchievementAdmin,
        NewAchievement
    },
    data()
    {
        return{
            achievementDetails: null,
            isAdding: false
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        Achievements()
        {
            return this.$store.state.achievements
        },
        achievementConditions()
        {
            var uslovi = []
            this.achievementDetails.conditions.forEach(element =>
            {
                var rez = ""
                switch(element)
                {
                    case 1:
                        rez = (this.isSerbian? "Broj kreiranih zahteva" : "Number of created requests")
                        break;
                    case 2:
                        rez = (this.isSerbian? "Broj uspešno završenih zahteva" : "Number of successfully finished requests")
                        break;
                    case 3:
                        rez = (this.isSerbian? "Granica za prosečnu ocenu" : "Average ratnig limit")
                        break;
                    case 4:
                        rez = (this.isSerbian? "Broj uspešno završenih zahteva kod kojih je korisnik bio izvršilac" : "Number of successfully finished requests where the user was the runner")
                        break;
                    case 5:
                        rez = (this.isSerbian? "Broj uspešno završenih zahteva kod kojih je korisnik bio zahtevalac" : "Number of successfully finished requests where the user was the one to create the request")
                        break;
                    case 6:
                        rez = (this.isSerbian? "Broj korisnika koje ima u listi povlašćenih" : "Number of users in the benefit list")
                        break;
                    case 7:
                        rez = (this.isSerbian? "Broj neuspešno završenih zahteva" : "Number of failed requests")
                        break;
                    case 8:
                        rez = (this.isSerbian? "Broj različitih usluga koje je pružio nekome" : "Number of different serveces the user has given")
                        break;
                    default:
                        rez = (this.isSerbian? "Broj različitih usluga koje je zahtevao" : "Number of different serveces the user has requested")
                        break;
                }
                uslovi.push(rez)
            })
            return uslovi
        }
    },
    methods:
    {
        openDetails(achievement)
        {
            this.achievementDetails = achievement
        }
    },
    created()
    {
        if(this.$store.state.achievements == null)
            this.$store.dispatch("getAllAchievements")
    }
}
</script>

<style scoped>
    .page-wrapper
    {
        display: flex;
        flex-direction: column;
        width:100%;
        align-items: center;
        padding-top:30px;
        padding-bottom:30px;
    }

    .allAchievements
    {
        display:flex;
        flex-direction: row;
        width:100%;
        align-items:center;
        justify-content:flex-start;
        flex-wrap: wrap;
        margin-top: 20px;
    }

    .achievement-detail
    {
        background-color:white;
        border-radius:20px;
        display: flex;
        flex-direction: row;
        width:80%;
        justify-content: space-evenly;
        /*align-items:center;*/
    }

    .tabela
    {
        margin-top:20px;
        margin-right:20px;
        width:60%;
    }

    .poravnanje
    {
        text-align: center;
    }

    .content
    {
        height:fit-content;
        align-self:center;
    }

    .newAchievement
    {
        margin-bottom:20px;
        width:60%;
        display: flex;
        flex-direction:column;
        align-items: center;
    }

    @media only screen and (max-width:800px)
    {
        .achievement-detail
        {
            width:98%;
        }

        .newAchievement
        {
            width:98%;
        }
    }

    @media only screen and (max-width:700px)
    {
        .achievement-detail
        {
            flex-direction: column;
            align-items: center;
        }
    }
</style>