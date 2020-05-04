<template>
  <div class="container">
    <ErrorView :titleE="'Ooooops, page you are trying to access doesn\'t exist'"
               :titleS="'Uuuuups, stranica kojoj pokušavate da pristupite ne postoji'"
               :status="'404'"/>
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