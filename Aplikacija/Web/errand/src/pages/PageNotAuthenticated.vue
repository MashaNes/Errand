<template>
  <div class="container">
    <ErrorView :titleE="'Ooooops, you are not authenticated to visit this page, please login'"
               :titleS="'Uuuuups, nemate privilegije da pregledate ovu stranicu, ulogujte se da biste je pogledali'"
               :status="'401'" 
               v-if="isDataLoaded && !isLogedIn"/>
    <Spinner v-else/>
  </div>
</template>

<script>
  import ErrorView from '@/components/ErrorView'
  import Spinner from "@/components/Spinner"
  export default {
    components: {
      ErrorView,
      Spinner
    },
    computed:
    {
      isDataLoaded()
      {
        return this.$store.state.isDataLoaded
      },
      isLogedIn()
      {
        return this.$store.state.logedIn
      }
    }
  }
</script>

<style scoped>
    .container
    {
        padding-top: 2px;
        padding-bottom: 50px;
    }
</style>