<template>
  <section class="hero is-success is-fullheight">
    <div class="hero-body">
      <div class="container has-text-centered">
        <div class="column is-4 is-offset-4">
          <h3 v-if="isSerbian" class="title has-text-grey">Prijavi se kao administrator</h3>
          <h3 v-else class="title has-text-grey">Login as administrator</h3>
          <p v-if="isSerbian" class="subtitle has-text-grey">Prijavite se kako biste nastavili.</p>
          <p v-else class="subtitle has-text-grey">Please login to proceed.</p>
          <div class="box">
            <form>
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
                </div>
            </div>
              <button @click.prevent="login" 
                      class="button is-block is-info is-large is-fullwidth"
                      :disabled="isFormInvalid">
                      <span v-if="isSerbian">Prijavi se</span>
                      <span v-else>Login</span>
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
            <router-link v-if="isSerbian" :to = "'/registerAdmin'">Registruj se</router-link>
            <router-link v-else :to = "'/registerAdmin'">Sign Up</router-link>
          </p>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
    import {required, email} from "vuelidate/lib/validators"
    export default {
        data(){
            return {
                form:
                {
                    email: null,
                    password: null
                }
            }
        },
        validations:
        {
            form:
            {
                email: { required, email },
                password:{ required}
            }
        },
        computed:
        {
            isFormInvalid() {
                return this.$v.form.$invalid
            },
            isSerbian()
            {
                return this.$store.state.isSerbian
            }
        },
        methods:
        {
            login()
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

  .login-label
  {
    margin-left: 5px;
    font-size: 18px;
  }

  .upozorenje
  {
    margin-top: 10px;
    font-weight: lighter;
  }
</style>