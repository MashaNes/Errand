<template>
  <div class="container">
    <ErrorView :titleE="'Ooooops, you are not authenticated to visit this page, please login'"
               :titleS="'Uuuuups, nemate privilegije da pregledate ovu stranicu, ulogujte se da biste je pogledali'"
               :status="'401'" />
  </div>
</template>

<script>
  import ErrorView from '@/components/ErrorView'
  export default {
    components: {
      ErrorView
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