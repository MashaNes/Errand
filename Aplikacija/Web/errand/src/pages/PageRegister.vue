<template>
  <section class="hero is-success is-fullheight">
    <div class="hero-body" v-if="!tryRegister || !isDataLoaded || !isLogedIn">
      <div class="container has-text-centered">
        <div class="column is-4 is-offset-4">
          <h3 class="title has-text-grey" v-if = "isSerbian">Registruj se</h3>
          <h3 class="title has-text-grey" v-else>Register</h3>
          <p class="subtitle has-text-grey" v-if = "isSerbian">Registrujte se da biste nastavili.</p>
          <p class="subtitle has-text-grey" v-else>Please register to proceed.</p>
          <div class="box">
            <form>
              <div class="field">                
                <div class="control">
                  <label class = "register-label" v-if="isSerbian"> *Ime: </label>
                  <label class = "register-label" v-else> *Name: </label>
                  <input class="input is-large"
                         type="text"
                         placeholder="Ime"
                         v-model = "form.name"
                         @blur="$v.form.name.$touch()"
                         v-if = "isSerbian">
                    <input class="input is-large"
                         type="text"
                         placeholder="Name"
                         v-model = "form.name"
                         @blur="$v.form.name.$touch()"
                         v-else>
                </div>
                <div v-if = "$v.form.name.$error" class = "form-error">
                    <span v-if = "!$v.form.name.required"
                          class = "help is-danger"> 
                            <span v-if="isSerbian">Morate uneti ime</span>
                            <span v-else>  Name is required </span> 
                          </span>
                </div>
              </div>
              <div class="field">
                <div class="control">
                  <label class = "register-label" v-if="isSerbian"> *Prezime: </label>
                  <label class = "register-label" v-else> *Last name: </label>
                  <input class="input is-large"
                         type="text"
                         placeholder="Prezime"
                         v-model = "form.lastName"
                         @blur="$v.form.lastName.$touch()"
                         v-if = "isSerbian">
                    <input class="input is-large"
                         type="text"
                         placeholder="Last name"
                         v-model = "form.lastName"
                         @blur="$v.form.lastName.$touch()"
                         v-else>
                </div>
                <div v-if = "$v.form.lastName.$error" class = "form-error">
                    <span v-if = "!$v.form.lastName.required"
                          class = "help is-danger"> 
                          <span v-if="isSerbian">Morate uneti prezime</span>
                            <span v-else>  Last name is required </span>  
                    </span>
                </div>
              </div>
              <div class="field">
                <div class="control">
                  <label class = "register-label"> *Email: </label>
                  <input class="input is-large"
                         type="email"
                         placeholder="Email"
                         v-model = "form.email"
                         @blur="$v.form.email.$touch()">
                </div>
                <div v-if = "$v.form.email.$error" class = "form-error">
                    <span v-if = "!$v.form.email.required"
                          class = "help is-danger"> 
                          <span v-if="isSerbian">Morate uneti e-mail adresu</span>
                            <span v-else>E-mail address is required </span>  
                        </span>
                </div>
                <div v-if = "$v.form.email.$error" class = "form-error">
                    <span v-if = "!$v.form.email.email"
                          class = "help is-danger">
                            <span v-if="isSerbian">E-mail adresa nije validna</span>
                            <span v-else>E-mail address is not valid </span>  
                    </span>
                </div>
              </div>
              <div class = "field">
                <div class = "control">
                  <label class = "register-label" v-if="isSerbian"> Broj telefona: </label>
                  <label class = "register-label" v-else> Phone number: </label>
                  <VuePhoneNumberInput v-model="form.phone" :no-example="true"/>
                </div>
              </div>
              <div class="field">
                <div class="control">
                  <label class = "register-label" v-if="isSerbian"> *Lozinka: </label>
                  <label class = "register-label" v-else> *Password: </label>
                  <input class="input is-large"
                         type="password"
                         placeholder="Lozinka"
                         autocomplete="new-password"
                         v-model = "form.password"
                         @blur="$v.form.password.$touch()"
                         v-if="isSerbian">
                    <input class="input is-large"
                         type="password"
                         placeholder="Password"
                         autocomplete="new-password"
                         v-model = "form.password"
                         @blur="$v.form.password.$touch()"
                         v-else>
                </div>
                <div v-if = "$v.form.password.$error" class = "form-error">
                    <span v-if = "!$v.form.password.required"
                          class = "help is-danger"> 
                            <span v-if="isSerbian">Morate uneti lozinku</span>
                            <span v-else>Password is required </span>  
                          </span>
                    <span v-if = "!$v.form.password.minLength"
                          class = "help is-danger">
                            <span v-if="isSerbian">Lozinka mora biti duga makar 6 karaktera</span>
                            <span v-else>Password must be at least 6 characters long</span>  
                    </span>
                </div>
              </div>
              <div class="field">
                <div class="control">
                  <label class = "register-label" v-if="isSerbian"> *Potvrda lozinke: </label>
                  <label class = "register-label" v-else> *Password conformation: </label>
                  <input class="input is-large"
                         type="password"
                         placeholder="Potvrda lozinke"
                         autocomplete="off"
                         v-model = "form.passwordConformation"
                         @blur="$v.form.passwordConformation.$touch()"
                         v-if="isSerbian">
                    <input class="input is-large"
                         type="password"
                         placeholder="Password Confirmation"
                         autocomplete="off"
                         v-model = "form.passwordConformation"
                         @blur="$v.form.passwordConformation.$touch()"
                         v-else>
                </div>
                <div v-if = "$v.form.passwordConformation.$error" class = "form-error">
                    <span v-if = "!$v.form.passwordConformation.required"
                          class = "help is-danger">
                            <span v-if="isSerbian">Morate uneti potvrdu lozinke</span>
                            <span v-else>Password conformation is required</span> 
                          </span>
                    <span v-if = "!$v.form.passwordConformation.sameAs"
                          class = "help is-danger">
                            <span v-if="isSerbian">Potvrda lozninke mora biti ista kao i lozinka</span>
                            <span v-else>Password conformation needs to be the same as the password </span> 
                    </span>
                </div>
              </div>
              <button :disabled="isFormInvalid || !isDataLoaded"
                      @click.prevent = "register" 
                      type="submit" 
                      class="button is-block is-info is-large is-fullwidth">
                        <div class="spinner-border ucitavanje" role="status" v-if="!isDataLoaded">
                          <span class="sr-only">Loading...</span>
                        </div>
                        <span v-if="isSerbian && isDataLoaded">Registruj se</span>
                        <span v-if="!isSerbian && isDataLoaded">Register</span>
                      </button>
            </form>
            <p class = "text-danger upozorenje" v-if="tryRegister && isDataLoaded && isSerbian">
              Već postoji nalog sa tom email adresom
            </p>
            <p class = "text-danger upozorenje" v-if="tryRegister && isDataLoaded && !isSerbian">
              An account with that email address already exists
            </p>
            <p class = "text-danger upozorenje" v-if="isSerbian">
              Stavke označene sa * su obavezne
            </p>
            <p class = "text-danger upozorenje" v-else>
              Elements marked with * are required
            </p>
          </div>

          <p class="has-text-grey">
            <router-link :to = "'/'">
                <span v-if="isSerbian">Početna stranica</span>
                <span v-else>Home</span>
            </router-link>
            &nbsp;·&nbsp;
            <router-link :to = "'/login'">
                <span v-if="isSerbian">Prijavi se</span>
                <span v-else>Login</span>
            </router-link>
          </p>
          <div id="mapid"></div>
        </div>
      </div>
    </div>
    <div v-else @mouseover="navigateTo" class="ceoEkran">
    </div>
  </section>
</template>

<script>
    import {required, email, minLength, sameAs} from "vuelidate/lib/validators"
    import VuePhoneNumberInput from 'vue-phone-number-input';
    import 'vue-phone-number-input/dist/vue-phone-number-input.css';

    export default {
        components:
        {
          VuePhoneNumberInput
        },
        data(){
            return{
                form:
                {
                    name: null,
                    lastName: null,
                    email: null,
                    password: null,
                    passwordConformation: null,
                    phone: null,
                    address: null
                },
                tryRegister: false
            }
        },
        computed:
        {
            isFormInvalid(){
                return this.$v.form.$invalid
            },
            isSerbian()
            {
                return this.$store.state.isSerbian
            },
            isDataLoaded()
            {
              return this.$store.state.isDataLoaded
            },
            isLogedIn()
            {
              return this.$store.state.logedIn
            }
        },
        validations:
        {
            form:
            {
                name: { required },
                lastName: { required },
                email: { required,email },
                password:{ required, minLength: minLength(6)},
                passwordConformation: { required, sameAs: sameAs("password") }
            }
        },
        methods:
        {
            register()
            {
              this.$v.form.$touch()
              //this.$router.push('/profile')
              this.$store.dispatch("createUser", this.form)
                /*.then(() => {
                  if(this.$store.state.authUser != null)
                    this.$router.push('/requests')
                })*/
              this.tryRegister = true
            },
            navigateTo()
            {
              this.$router.push('/profile/' + this.$store.state.authUser.id)
            }
        }
    } 
</script>

<style scoped>
  .hero.is-success {
    background: #F2F6FA;
  }
  .hero .nav, .hero.is-success .nav {
    -webkit-box-shadow: none;
    box-shadow: none;
  }
  .box {
    margin-top: 1rem;
  }
  .avatar {
    margin-top: -70px;
    padding-bottom: 20px;
  }
  .avatar img {
    padding: 5px;
    background: #fff;
    border-radius: 50%;
    -webkit-box-shadow: 0 2px 3px rgba(10,10,10,.1), 0 0 0 1px rgba(10,10,10,.1);
    box-shadow: 0 2px 3px rgba(10,10,10,.1), 0 0 0 1px rgba(10,10,10,.1);
  }
  input {
    font-weight: 300;
  }
  p {
    font-weight: 700;
  }
  p.subtitle {
    padding-top: 1rem;
  }

  .register-label
  {
    margin-left: 5px;
    font-size: 18px;
  }

  .upozorenje
  {
    margin-top: 10px;
    font-weight: lighter;
  }

  #mapid { height: 180px; }
</style>