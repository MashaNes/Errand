<template>
<Spinner v-if="!this.$store.state.isDataLoaded || this.$store.state.logedIn"/>
<div v-else>
  <!-- Masthead -->
  <header class="masthead text-white text-center">
    <div class="overlay"></div>
    <div class="container">
      <div class="row">
        <div class="col-xl-9 mx-auto">
          <h1 class="mb-5" v-if="isSerbian">Lako pronađi nekog da ti završi posao ili ti budi taj koji drugima završava poslove i zarađuje!</h1>
          <h1 class="mb-5" v-else>Find someone to run your errands or run errands for other people and earn money!</h1>
        </div>
        <div class="col-md-10 col-lg-8 col-xl-7 mx-auto">
          <form>
            <div class="form-row">
              <div class="col-12 col-md-4 mb-2 mb-md-0">
              </div>
              <div class="col-12 col-md-3">

                <router-link :to = "'/register'" type="submit" class="btn btn-block btn-lg btn-primary" v-if="isSerbian">Registruj se!</router-link>
                <router-link :to = "'/register'" type="submit" class="btn btn-block btn-lg btn-primary" v-else>Sign up!</router-link>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </header>

  <!-- Testimonials -->
  <section class="testimonials text-center bg-light">
    <div class="container">
      <h2 class="mb-5" v-if="isSerbian">Preporuke</h2>
      <h2 class="mb-5" v-else>Recommendations</h2>
      <div class="row">
        <div class="col-lg-4">
          <div class="testimonial-item mx-auto mb-5 mb-lg-0">
            <img class="img-fluid rounded-circle mb-3" src="../assets/slika1.jpg" alt="">
            <h5>Maša N.</h5>
            <p class="font-weight-light mb-0" v-if="isSerbian">"Odlična prilika za dodatnu zaradu, pogotovo u studentskim danima!"</p>
            <p class="font-weight-light mb-0" v-else>"Great opportunity to get extra pay, especially when you are a student!"</p>
          </div>
        </div>
        <div class="col-lg-4">
          <div class="testimonial-item mx-auto mb-5 mb-lg-0">
            <img class="img-fluid rounded-circle mb-3" src="../assets/slika2.jpg" alt="">
            <h5>Jovan K.</h5>
            <p class="font-weight-light mb-0" v-if="isSerbian">"Platforma daje mogućnost da lako nađete ljude kojima je potrebna pomoć i pomognete im."</p>
            <p class="font-weight-light mb-0" v-else>"Errand gives you a chance to find people who need help and help them."</p>
          </div>
        </div>
        <div class="col-lg-4">
          <div class="testimonial-item mx-auto mb-5 mb-lg-0">
            <img class="img-fluid rounded-circle mb-3" src="../assets/slika3.png" alt="">
            <h5>Milorad V.</h5>
            <p class="font-weight-light mb-0" v-if="isSerbian">"Obavljanje svakodnevnih poslova je mnogo lakše uz Errand platformu, lako mogu da pronađem ljude koji će to učiniti za mene!"</p>
            <p class="font-weight-light mb-0" v-else>"Running errands has never been easier. It is so simple to find someone to help you out!"</p>
          </div>
        </div>
      </div>
    </div>
  </section>
   <div class="admin-wrapper">
    <div class="admin" v-if="isSerbian">
        Da li si ti admin? <router-link type="button" :to="'/loginAdmin'" class="btn btn-link stop">Prijavi se</router-link> <router-link :to="'/registerAdmin'" type="button" class="btn btn-link stop">Registruj se</router-link>
    </div>
    <div class="admin" v-else>
        Are you an admin? <router-link type="button" :to="'/loginAdmin'" class="btn btn-link stop">Log in</router-link> <router-link :to="'/registerAdmin'" type="button" class="btn btn-link stop">Sign up</router-link>
    </div>
  </div>
</div>


</template>

<script>
    import Spinner from "@/components/Spinner"
    export default {
        components:
        {
          Spinner
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
    background: url("../assets/bg-masthead.jpg") no-repeat center center;
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

    .showcase .showcase-text {
    padding: 3rem;
    }

    .showcase .showcase-img {
    min-height: 30rem;
    background-size: cover;
    }

    @media (min-width: 768px) {
    .showcase .showcase-text {
        padding: 7rem;
    }
    }

    .features-icons {
    padding-top: 7rem;
    padding-bottom: 7rem;
    }

    .features-icons .features-icons-item {
    max-width: 20rem;
    }

    .features-icons .features-icons-item .features-icons-icon {
    height: 7rem;
    }

    .features-icons .features-icons-item .features-icons-icon i {
    font-size: 4.5rem;
    }

    .features-icons .features-icons-item:hover .features-icons-icon i {
    font-size: 5rem;
    }

    .testimonials {
    padding-top: 7rem;
    padding-bottom: 7rem;
    }

    .testimonials .testimonial-item {
    max-width: 18rem;
    }

    .testimonials .testimonial-item img {
    max-width: 12rem;
    box-shadow: 0px 5px 5px 0px #adb5bd;
    }

    .call-to-action {
    position: relative;
    background-color: #343a40;
    background: url("../assets/bg-masthead.jpg") no-repeat center center;
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

    footer.footer {
    padding-top: 4rem;
    padding-bottom: 4rem;
    }

    body{font-family:Lato,'Helvetica Neue',Helvetica,Arial,sans-serif}h1,h2,h3,h4,h5,h6{font-family:Lato,'Helvetica Neue',Helvetica,Arial,sans-serif;font-weight:700}header.masthead{position:relative;background-color:#343a40;background:url(../assets/bg-masthead.jpg) no-repeat center center;background-size:cover;padding-top:8rem;padding-bottom:8rem}header.masthead .overlay{position:absolute;background-color:#212529;height:100%;width:100%;top:0;left:0;opacity:.3}header.masthead h1{font-size:2rem}@media (min-width:768px){header.masthead{padding-top:12rem;padding-bottom:12rem}header.masthead h1{font-size:3rem}}.showcase .showcase-text{padding:3rem}.showcase .showcase-img{min-height:30rem;background-size:cover}@media (min-width:768px){.showcase .showcase-text{padding:7rem}}.features-icons{padding-top:7rem;padding-bottom:7rem}.features-icons .features-icons-item{max-width:20rem}.features-icons .features-icons-item .features-icons-icon{height:7rem}.features-icons .features-icons-item .features-icons-icon i{font-size:4.5rem}.features-icons .features-icons-item:hover .features-icons-icon i{font-size:5rem}.testimonials{padding-top:7rem;padding-bottom:7rem}.testimonials .testimonial-item{max-width:18rem}.testimonials .testimonial-item img{max-width:12rem;box-shadow:0 5px 5px 0 #adb5bd}.call-to-action{position:relative;background-color:#343a40;background:url(../assets/bg-masthead.jpg) no-repeat center center;background-size:cover;padding-top:7rem;padding-bottom:7rem}.call-to-action .overlay{position:absolute;background-color:#212529;height:100%;width:100%;top:0;left:0;opacity:.3}footer.footer{padding-top:4rem;padding-bottom:4rem}

    .admin
    {
        margin-top: 5px;
        margin-bottom: 15px;
    }

    .admin-wrapper
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }

    .stop
    {
        padding-top: 2px;
        padding-right: 0px;
        padding-left: 5px;
    }
</style>