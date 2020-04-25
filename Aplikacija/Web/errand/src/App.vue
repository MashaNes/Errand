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
  created(){
    this.$store.dispatch("fillRequests")
    this.$store.dispatch("getUser")
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
    /*background-position: center;*/
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
    /*background-image: url("http://chicagosgirlonthego.com/Wpress/wp-content/uploads/chicago-errand-running-service-02.jpg");*/
    /*background-color: rgba(248, 249, 250);*/
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
    position: sticky; /* Set the navbar to fixed position */
    top: 0; /* Position the navbar at the top of the page */
    width: 100%; /* Full width */
    background-color: white;
    z-index: 1000000000;
  }
</style>
