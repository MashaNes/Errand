<template>
<transition name="modal">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-container">

              <div class="modal-header">
                <slot name="header" v-if="isSerbian">
                  Registruj se kao admin
                </slot>
                <slot name="header" v-else>
                  Sign in as admin
                </slot>
              </div>

              <div class="modal-body">
                <slot name="body">
                    <div class="field">                
                        <div class="control">
                            <label class = "register-label" v-if="isSerbian"> *Korisničko ime: </label>
                            <label class = "register-label" v-else> *Username: </label>
                            <input class="input is-large"
                                    type="text"
                                    placeholder="Korisničko ime"
                                    v-model = "form.username"
                                    @blur="$v.form.username.$touch()"
                                    v-if = "isSerbian">
                                <input class="input is-large"
                                    type="text"
                                    placeholder="Username"
                                    v-model = "form.username"
                                    @blur="$v.form.username.$touch()"
                                    v-else>
                        </div>
                        <div v-if = "$v.form.username.$error" class = "form-error">
                            <span v-if = "!$v.form.username.required"
                                class = "help is-danger"> 
                                    <span v-if="isSerbian">Morate uneti korisničko ime</span>
                                    <span v-else>  Username is required </span> 
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
                </slot>
                <p class = "text-danger upozorenje" v-if="isSerbian">
                    Stavke označene sa * su obavezne
                </p>
                <p class = "text-danger upozorenje" v-else>
                    Elements marked with * are required
                </p>
              </div>

              <div class="modal-footer">
                <slot name="footer">
                  <button type="button" class="btn btn-primary" :disabled="isFormInvalid"
                      @click.prevent = "register"  v-if="isSerbian">
                    Registruj se
                  </button>
                  <button type="button" class="btn btn-primary" :disabled="isFormInvalid"
                      @click.prevent = "register"  v-else>
                    Sign in
                  </button>
                  <button type="button" class="btn btn-secondary" @click="$emit('close')" v-if="isSerbian">
                    Odustani
                  </button>
                  <button type="button" class="btn btn-secondary" @click="$emit('close')" v-else>
                    Cancel
                  </button>
                </slot>
              </div>
            </div>
          </div>
        </div>
      </transition>
</template>

<script>
import {required, minLength, sameAs} from "vuelidate/lib/validators"
export default {  
    data()
    {
        return{
            message: "",
            form:
                {
                    username: null,
                    password: null,
                    passwordConformation: null,
                    superpassword: null
                }
        }
    },
    methods:
    {
        register()
        {
            this.$v.form.$touch()
            console.log(this.form)
            this.$emit('close')
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
        }
    },
    validations:
    {
        form:
        {
            username: { required },
            password:{ required, minLength: minLength(6)},
            passwordConformation: { required, sameAs: sameAs("password") },
            superpassword: {required}
        }
    }
}
</script>

<style scoped>
    .modal-mask {
    position: fixed;
    z-index: 9998;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: table;
    transition: opacity 0.3s ease;
    }

    .modal-wrapper {
    display: table-cell;
    vertical-align: middle;
    }

    .modal-container {
    width: 700px;
    margin: 0px auto;
    padding: 20px 30px;
    background-color: #fff;
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
    transition: all 0.3s ease;
    font-family: Helvetica, Arial, sans-serif;
    }

    .modal-header h3 {
    margin-top: 0;
    color: #42b983;
    }

    .modal-body {
    margin: 20px 0;
    }

    .modal-default-button {
    float: right;
    }

    .modal-enter {
    opacity: 0;
    }

    .modal-leave-active {
    opacity: 0;
    }

    .modal-enter .modal-container,
    .modal-leave-active .modal-container {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
    }

    .polje
    {
        width: 600px;
        height: 120px;
        border: 3px solid #cccccc;
        padding: 5px;
        font-family: Tahoma, sans-serif;
        background-position: bottom right;
        background-repeat: no-repeat;
    }

      .hero.is-success {
    background: #F2F6FA;
  }
  .hero .nav, .hero.is-success .nav {
    -webkit-box-shadow: none;
    box-shadow: none;
  }
  .box {
    margin-top: 5rem;
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
</style>