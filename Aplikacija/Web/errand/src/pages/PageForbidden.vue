<template>
  <div class="container">
    <ErrorView :titleE="'Ooooops, you are forbidden from viewing this page due to insufficient permissions of the authenticated account'"
               :titleS="'Uuuuups, zabranjen Vam je pristup ovoj stranici zbog nedostatka privilegija koje imate preko autentifikovanog naloga'"
               :status="'403'"/>
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