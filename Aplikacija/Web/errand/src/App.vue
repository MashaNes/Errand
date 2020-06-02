<template>
  <div id="app">
    <TheNavbar class="please"/>
    <div class = "page-wrapper bg-light">
      <router-view />
    </div>
    <TheFooter/>
  </div>
</template>

<script>
import TheFooter from "@/components/TheFooter"
import TheNavbar from "@/components/TheNavbar"
export default {
  name: 'App',
  components: {
    TheFooter,
    TheNavbar
  },
  data()
  {
    return{
      user:
      {
        id: -1,
        token: "",
        first_name: "",
        last_name: ""
      }
    }
  },
  created()
  {
    this.user.first_name = this.$cookie.get('ime');
    this.user.last_name = this.$cookie.get('prezime');
    if(this.user.first_name != null && this.user.last_name != null)
    {
      this.$store.state.authUser = this.user
      this.$store.state.logedIn = true
      this.$store.state.isAdmin = this.$cookie.get('admin')
      this.user.token = this.$cookie.get('token');
      this.user.id = this.$cookie.get('id');
      this.$store.dispatch("getUserById", this.user)
    }
  }
}
</script>

<style>
#app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }

  .bold {
    font-weight: bold;
  }

  .cover {
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }

  .hero {
    position: relative;
  }

  .hero-body {
    padding: 3rem 1.5rem;
  }

  .hero-bg {
    background-image: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1531263060782-b024de9b9793?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1650&q=80');
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }

  .page-wrapper
  {
    min-height: 73vh;
  }

  .top-div
  {
    background-color: rgba(138, 141, 145, 0.767);
    height: 12vh;
    color: white;
    padding: 27px;
    padding-left:100px;
    font-size: 35px;
    font-weight: bold;
  }

  .please
  {
    position: sticky;
    top: 0;
    width: 100%;
    background-color: white;
    z-index: 1000000000;
  }
</style>
