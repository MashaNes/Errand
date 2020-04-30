<template>
  <div class="mt-3">
      <b-pagination v-model="currentPage" :total-rows="rows" align="center"></b-pagination>
      
      <b-pagination style="position:relative; bottom:0;" v-model="currentPage" :total-rows="rows" align="center"></b-pagination>
  </div>
</template>

<script>
export default {
  
}
</script>


<style scoped>

</style>