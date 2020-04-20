<template>
    <div class = "wrapper">
        <div class = "element">
            <img class = "picture-div" src = "../assets/delivery.png">
            <div class = "inner-div" v-if="isSerbian"> 
                <div class="text-div">
                    Ako tražite mogućnosti izvršavanja usluga kao što su pretraga zahteva dostupnih za izršavanje, pretraga Vama lično prosleđenih zahteva, slanje ponuda, pregled informacija vezanih za ponude... Za takve funkcionalnosti moraćete da preuzmete našu Android aplikaciju namenjenu za korišćenje na telefonu kako će Vam telefon biti neophodan pri obavljanju poslova izvršenja usluga. U mobilnu aplikaciju možete se prijaviti sa već postojećim nalogom, ne morate kreirati novi.
                </div>
                <div class = "dugme-div">
                    <button type="button" class="btn btn-primary dugme">Preuzmi mobilnu aplikaciju </button>
                </div>
            </div>
            <div class = "inner-div" v-else>
                <div class="text-div">
                    If you are looking for the ability to be an errand runner and the possibilities such as searching for requests to fulfil, finding requests sent personally to you, sending offers, finding information regarding your offers etc. Then you must download our Android application made for mobile usage which will be necessary for you if you desire to run Errands using our platform. When logging in to the mobile application you can use the account you have already created, there is no need for you to create a new one.
                </div>
                <div class = "dugme-div">
                    <button type="button" class="btn btn-primary dugme">Download mobile app</button>
                </div>
            </div>  
        </div>
        <div class = "element">
            <p class = "other" v-if="isSerbian">Treba Vam neka druga pomoć?   <a href="#" class="card-link">Kontaktirajte nas</a> </p>
            <p class = "other" v-else>Need help with something else?   <a href="#" class="card-link">Contact us</a> </p>
        </div>
    </div>
</template>

<script>
export default {
    data()
    {
        return{
            textSerbian:"Ako tražite mogućnosti izvršavanja usluga kao što su pretraga zahteva dostupnih za izršavanje, pretraga Vama lično prosleđenih zahteva, slanje ponuda, pregled informacija vezanih za ponude... Za takve funkcionalnosti moraćete da preuzmete našu Android aplikaciju namenjenu za korišćenje na telefonu kako će Vam telefon biti neophodan pri obavljanju poslova izvršenja usluga. U mobilnu aplikaciju možete se prijaviti sa već postojećim nalogom, ne morate kreirati novi.",
            textEnglish:""
        }
    },  
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        }
    }
}
</script>

<style scoped>

    .text-div
    {
        font-size: 18px;
        text-align: center;
        margin-top: 15px;
    }

    .picture-div
    {
        width:25%;
        height:70%;
    }
    .inner-div
    {
        width:40%;
        height:70%;
        margin-left:40px;
        
        display: flex;
        justify-content: center;
        flex-direction: column;
    }
    
    .wrapper
    {
        display: flex;
        justify-content: center;
        flex-direction: column;
    }

    .element
    {
        margin-top:50px;
        width: 100%;
        height: 100%;
        justify-content: center;
        display: flex;
        flex-direction: row;
    }

    .dugme
    {
        width:fit-content
    }

    .dugme-div
    {
        display: flex;
        width: 100%;
        height: 100%;
        justify-content: center;
        align-self: flex-end;
        justify-self: flex-end;
        margin-top:50px;
    }

    .other
    {
        font-size: 18px;
        padding:40px;
    }
</style>