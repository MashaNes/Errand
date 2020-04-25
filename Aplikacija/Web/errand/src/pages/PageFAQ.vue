<template>
    <div>
        <header class="masthead text-white text-center">
            <div class="overlay"></div>
                <div class="container">
                    <div class="row">
                        <div class="col-xl-9 mx-auto">
                        <h1 class="mb-5" v-if="isSerbian">Često postavljana pitanja</h1>
                        <h1 class="mb-5" v-else>Frequently asked questions</h1>
                    </div>
                </div>
            </div>
        </header>
        <div class="faq-body">
            <div class="question" @click="changeQuestion(1)">
                <div class="naslov">
                    <a class="ikonica" v-if="question!=1"><img  class = "slika" src = "../assets/open-question.svg"></a>
                    <div class="pitanje">
                        <span v-if="isSerbian">Ako dodam adresu na svoj profli kome će ona da bude vidljiva?</span>
                        <span v-else>If I add an address to my profile, who will be able to see it?</span>
                    </div>
                </div>
                <div class="odgovor" v-if="question == 1">
                    <span v-if="isSerbian"> Mi poštujemo Vašu privatnost te Vaša adresa neće biti javno prikazana na Vašem profilu već ona služi da bi se po potrebi popunjavalo polje za adresu pri kreiranju zahteva, dakle služi kao pomoć Vama da ne morate stalno iznova da unosite adresu. Neće biti prikazana nikome kao deo Vašeg profila već samo ako je iskoristite u nekom zahtevu onda će biti prikazana korisniku koga ste odabrali za izvršioca Vašeg zahteva. </span>
                    <span v-else> We value your privacy so your address will not be publicly visible on your profile, the purpose of the address is that it can be used to automaticly fill an address field in a request you are creating so you don't have to enter it yourself every time. Your adress will not be publicly given, if you choose to use it as an address for a request it will be given to the user you have selected to complete your request.</span>
                </div>
            </div>
            <div class="question" @click="changeQuestion(2)">
                <div class="naslov">
                    <a class="ikonica" v-if="question!=2"><img  class = "slika" src = "../assets/open-question.svg"></a>
                    <div class="pitanje">
                        <span v-if="isSerbian">Kako biram ko će da izvrši zahtev koji kreiram?</span>
                        <span v-else>How do I choose who will be completing the request I have created?</span>
                    </div>
                </div>
                <div class="odgovor" v-if="question == 2">
                    <span v-if="isSerbian">Imate dve opcije. Možete da odaberete da Vaš zahtev bude vidljiv svim korisnicima koji zadovoljavaju određene kriterijume ili da sami odaberete konkretnog korisnika kome će kreirani zahtev biti direktno prosleđen. Ukoliko odaberete prvu opciju onda će Vaš zahtev biti vidljiv svim osobama koje zadovoljavaju Vaše kriterijume i svi oni će moći da Vam šalju ponude za izvšenje od kojih ćete Vi odabrati onu po Vašem izboru. Ukoliko odaberete drugu opciju onda će odabrani korisnik dobiti Vaš zahtev direktno i samo on će moći da Vam pošalje ponudu za izvršenje istog.</span>
                    <span v-else>You have two options. You can choose for your request to be visible to all users who satisfy your criteria or for it to be visible only to a specific user you select for thath exact request. If you choose the first option all users who satisfy the criteria you have selected will be able to find your request and send you an offer for completeing it, on the other hand, if you choose the second option, the user you have selected will get a direct notification about the request you have created and will be the only one who can send you an offer for completing it.</span>
                </div>
            </div>
            <div class="question" @click="changeQuestion(3)">
                <div class="naslov">
                    <a class="ikonica" v-if="question!=3"><img  class = "slika" src = "../assets/open-question.svg"></a>
                    <div class="pitanje">
                        <span v-if="isSerbian">Kako mogu da postavim kriterijume za selekciju korisnika koji mogu da vide moj zahtev?</span>
                        <span v-else>How can I set the criteria for selecting the users who can see my request?</span>
                    </div>
                </div>
                <div class="odgovor" v-if="question == 3">
                    <span v-if="isSerbian">Podrazumevane vrednosti za kriterijume možete podesiti odabirom taba "Podešavanja" iz menija, ti kriterijumi biće sačuvani i iskorišćeni pri svakom zahtevu koji kreirate, ali to Vas neće sprečiti da ih u bilo kom trenutku promenite za neki konkretan zahtev koji trenutno kreirate, uvek ćete pri kreiranju imati mogućnost da to uradite.</span>
                    <span v-else>You can set the default values for the criteria by choosing the "Settings" tab from the menu, those criteria will be saved and used for every request you create. However, that will not stop you from changing the values of the criteria for a specific request you are creating, you can do that at any time. </span>
                </div>
            </div>
            <div class="question" @click="changeQuestion(4)">
                <div class="naslov">
                    <a class="ikonica" v-if="question!=4"><img  class = "slika" src = "../assets/open-question.svg"></a>
                    <div class="pitanje">
                        <span v-if="isSerbian">Koga ja mogu da ocenim i ko može da oceni mene?</span>
                        <span v-else>Who can I rate and who can rate me?</span>
                    </div>
                </div>
                <div class="odgovor" v-if="question == 4">
                    <span v-if="isSerbian">Možete dati ocenu svakom korisniku sa kojim ste sarađivali na nekom zahtevu, bilo da ste Vi kreirali zahtev ili taj korisnik, i za svaki od zahteva možete dati po jednu ocenu tom korisniku. Takođe Vas može da oceni svaki korisnik koji je sa Vama sarađivao i to onoliko puta koliko je sarađivao sa Vama.</span>
                    <span v-else>You can rate any user you have cooperated with on a request, no matter if you were the one to create the request or not, and you can rate them as many times as you have copperated with each other. Similarly, you can be rated by any user you have cooperated with for every request you have cooperated on.</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    computed:
    {
        isSerbian()
            {
                return this.$store.state.isSerbian
            }
    },
    data()
    {
        return{
            question: 0        
        }
    },
    methods:
    {
        changeQuestion(questionNumber)
        {
            this.question=questionNumber
        }
    }
}
</script>

<style scoped>
    .ikonica
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        align-content: center;
        justify-items: center;
    }

    .slika
    {
        width: 40px;
    }

    .faq-body
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 100%;
        padding-top: 30px;
        padding-bottom: 30px;
    }

    .naslov
    {
        font-size: 25px;
        font-weight: 700;
        margin-bottom: 10px;
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-start;
        width: 100%;
    }

    .odgovor
    {
        text-align: justify;
        font-size: 18px;
        color: rgb(30, 55, 77);
    }

    .pitanje
    {
        width: 100%;
        text-align: center;
    }

    .question
    {
        background-color: white;
        border-radius: 15px;
        padding:15px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 70%;
        margin-bottom: 20px;
    }

    @media only screen and (max-width: 600px)
    {
        .question
        {
            width:95%;
        }
    }

    body {
        font-family: 'Lato', 'Helvetica Neue', Helvetica, Arial, sans-serif;
        }

    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
        font-family: 'Lato', 'Helvetica Neue', Helvetica, Arial, sans-serif;
        font-weight: 700;
    }

    header.masthead {
        position: relative;
        background-color: #343a40;
        background: url("../assets/faq.jpg") no-repeat center center;
        background-size: cover;
        padding-top: 8rem;
        padding-bottom: 8rem;
    }

    header.masthead .overlay {
        position: absolute;
        background-color: #212529;
        height: 100%;
        width: 100%;
        top: 0;
        left: 0;
        opacity: 0.3;
    }

    header.masthead h1 {
        font-size: 2rem;
    }

    @media (min-width: 768px) {
        header.masthead {
            padding-top: 12rem;
            padding-bottom: 12rem;
        }
        header.masthead h1 {
            font-size: 3rem;
        }
    }

    .call-to-action {
        position: relative;
        background-color: #343a40;
        background: url("../assets/faq.jpg") no-repeat center center;
        background-size: cover;
        padding-top: 7rem;
        padding-bottom: 7rem;
    }

    .call-to-action .overlay {
        position: absolute;
        background-color: #212529;
        height: 100%;
        width: 100%;
        top: 0;
        left: 0;
        opacity: 0.3;
    }
</style>