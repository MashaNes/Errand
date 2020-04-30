<template>
  <div class="mt-3">
    <b-pagination 
      v-model="currentPage" 
      :total-rows="users.totalCount" 
      :per-page="perPage" 
      align="center"
      @input="getNextPortion"
    ></b-pagination>
    
    <div v-for="user in users.users" :key="user.id" style="display: flex; flex-direction:column; align-items:center;">
      <!-- <strong> {{user.firstName}} </strong> -->
      <UserBox
        :user="user"
      />
    </div>

    <b-pagination 

      v-model="currentPage" 
      :per-page="perPage" 
      :total-rows="users.totalCount" 
      align="center"
    ></b-pagination>

  </div>
</template>

<script>
import UserBox from "@/components/UserBox"

export default {
  components: {
    UserBox
  },
  data() {
    return {
      currentPage: 1,
      perPage: 3,
      lastPage: 1
    }
  },
  computed: {
    users() {
      return this.$store.state.usersPortion
    }
  },
  methods: {
    getNextPortion() {
      if(this.lastPage != this.currentPage)
      {
        console.log(this.currentPage + " " + this.perPage)
        this.$store.dispatch('fillUsersPortion', {
          startingIndex: (this.currentPage - 1) * this.perPage,
          numOfElements: this.perPage
        })
        this.lastPage = this.currentPage
      }
    }
  },
  created() {
    this.$store.dispatch('fillUsersPortion', {startingIndex: 0, numOfElements: 3})
  }
}
</script>


<style scoped>
  .pagination {
    margin: 5px 0 5px 0;
  }
</style>