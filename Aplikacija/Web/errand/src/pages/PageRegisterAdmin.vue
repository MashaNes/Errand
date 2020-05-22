<template>
  <Spinner v-if="!this.$store.state.isDataLoaded || this.$store.state.logedIn"/>
  <section class="hero is-success is-fullheight" v-else>
    <div class="hero-body">
      <div class="container has-text-centered">
        <div class="column is-4 is-offset-4">
          <h3 class="title has-text-grey" v-if = "isSerbian">Registruj se kao administrator</h3>
          <h3 class="title has-text-grey" v-else>Register as an administrator</h3>
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
            <div class="field">
                <div class="control">
                    <label class = "register-label" v-if="isSerbian"> *Vaša Lozinka: </label>
                    <label class = "register-label" v-else> *Your Password: </label>
                    <input class="input is-large"
                            type="password"
                            placeholder="Vaša Lozinka"
                            autocomplete="new-password"
                            v-model = "form.password"
                            @blur="$v.form.password.$touch()"
                            v-if="isSerbian">
                        <input class="input is-large"
                            type="password"
                            placeholder="Your Password"
                            autocomplete="new-password"
                            v-model = "form.password"
                            @blur="$v.form.password.$touch()"
                            v-else>
                </div>
                <div v-if = "$v.form.password.$error" class = "form-error">
                    <span v-if = "!$v.form.password.required"
                        class = "help is-danger"> 
                            <span v-if="isSerbian">Morate uneti Vašu lozinku</span>
                            <span v-else>Your password is required </span>  
                    </span>
                    <span v-if = "!$v.form.password.minLength"
                        class = "help is-danger">
                            <span v-if="isSerbian">Vaša lozinka mora biti duga makar 6 karaktera</span>
                            <span v-else>Your password must be at least 6 characters long</span>  
                    </span>
                </div>
            </div>
            <div class="field">
                <div class="control">
                    <label class = "register-label" v-if="isSerbian"> *Potvrda Vaše lozinke: </label>
                    <label class = "register-label" v-else> *Your password conformation: </label>
                    <input class="input is-large"
                            type="password"
                            placeholder="Potvrda Vaše lozinke"
                            autocomplete="off"
                            v-model = "form.passwordConformation"
                            @blur="$v.form.passwordConformation.$touch()"
                            v-if="isSerbian">
                        <input class="input is-large"
                            type="password"
                            placeholder="Your password Confirmation"
                            autocomplete="off"
                            v-model = "form.passwordConformation"
                            @blur="$v.form.passwordConformation.$touch()"
                            v-else>
                </div>
                <div v-if = "$v.form.passwordConformation.$error" class = "form-error">
                    <span v-if = "!$v.form.passwordConformation.required"
                        class = "help is-danger">
                            <span v-if="isSerbian">Morate uneti potvrdu Vaše lozinke</span>
                            <span v-else>Your password conformation is required</span> 
                    </span>
                    <span v-if = "!$v.form.passwordConformation.sameAs"
                        class = "help is-danger">
                            <span v-if="isSerbian">Potvrda lozninke mora biti ista kao i lozinka</span>
                            <span v-else>Password conformation needs to be the same as the password </span> 
                    </span>
                </div>
            </div>
            <div class="field">
                <div class="control">
                    <label class = "register-label" v-if="isSerbian"> *Glavna lozinka za admina: </label>
                    <label class = "register-label" v-else> *Master admin password: </label>
                    <input class="input is-large"
                            type="password"
                            placeholder="Glavna lozinka za admina"
                            autocomplete="off"
                            v-model = "form.superpassword"
                            @blur="$v.form.superpassword.$touch()"
                            v-if="isSerbian">
                        <input class="input is-large"
                            type="password"
                            placeholder="Master admin password"
                            autocomplete="off"
                            v-model = "form.superpassword"
                            @blur="$v.form.superpassword.$touch()"
                            v-else>
                </div>
                <div v-if = "$v.form.superpassword.$error" class = "form-error">
                    <span v-if = "!$v.form.superpassword.required"
                        class = "help is-danger">
                            <span v-if="isSerbian">Morate uneti glavnu lozinku za admina</span>
                            <span v-else>Master admin password is required</span> 
                    </span>
                </div>
            </div>
              <button :disabled="isFormInvalid"
                      @click.prevent = "register" 
                      type="submit" 
                      class="button is-block is-info is-large is-fullwidth">
                        <span v-if="isSerbian">Registruj se</span>
                        <span v-else>Register</span>
                      </button>
            </form>
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
            <router-link :to = "'/loginAdmin'">
                <span v-if="isSerbian">Prijavi se</span>
                <span v-else>Login</span>
            </router-link>
          </p>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
    import {required, minLength, sameAs, email} from "vuelidate/lib/validators"
    import Spinner from "@/components/Spinner"
    export default {
        data(){
            return{
                form:
                {
                    name: null,
                    lastName: null,
                    email: null,
                    password: null,
                    passwordConformation: null,
                    superpassword: null
                }
            }
        },
        components:
        {
          Spinner
        },
        computed:
        {
            isFormInvalid(){
                return this.$v.form.$invalid
            },
            isSerbian()
            {
                return this.$store.state.isSerbian
            }
        },
        validations:
        {
            form:
            {
                name : { required },
                lastName: { required },
                email : { required, email },
                password:{ required, minLength: minLength(6)},
                passwordConformation: { required, sameAs: sameAs("password") },
                superpassword: {required}
            }
        },
        methods:
        {
            register()
            {
              this.$v.form.$touch()
              console.log(this.form)
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