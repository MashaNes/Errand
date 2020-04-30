<template>
  <div class = "navigate">
    <TheNavbarSmall class = "mali"/>
    <TheNavbarLarge class = "veliki"/>
  </div>
</template>

<script>
  import TheNavbarSmall from "@/components/TheNavbarSmall"
  import TheNavbarLarge from "@/components/TheNavbarLarge"
  export default {
    components:
    {
      TheNavbarSmall,
      TheNavbarLarge
    }

  }
</script>

<style scoped>
  @media only screen and (min-width: 500px)
  {
    .mali
    {
      width:0px;
      visibility: hidden;
      min-height: 0px;
      margin: 0px;
    }
    
    .navigate
    {
      width: 100%;
      height: 70px;
      display: flex;
      flex-direction: column-reverse;
    }

    .veliki
    {
      padding-top:15px;
    }
  }
  @media only screen and (max-width: 499px)
  {
    .veliki
    {
      width:0px;
      visibility: hidden;
      min-height: 0px;
      margin: 0px;
    }

    .navigate
    {
      height: 85px;
      display: flex;
      flex-direction: column;
      padding-bottom:0px;
      margin-bottom:0px;
      margin-top: 0px;
      padding-top: 0px;
    }

    .mali
    {
      padding-bottom:0px;
      margin-bottom:0px;
      margin-top: 10px;
    }
  }
</style>