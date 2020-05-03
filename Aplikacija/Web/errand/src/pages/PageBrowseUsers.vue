<template>
  <div class="wrapper">
    <b-pagination 
      v-model="currentPage" 
      :total-rows="users.totalCount" 
      :per-page="perPage" 
      align="center"
      class="pag-top"
      @input="getNextPortion"
    ></b-pagination>
    <div class="users">
      <!-- <strong> {{user.firstName}} </strong> -->
      <UserBox
        :user="user"
        v-for="user in users.users" 
        :key="user.id"
        :BenefitList="benefitList"
      />
    </div>
    <b-pagination 
      v-model="currentPage" 
      :per-page="perPage" 
      :total-rows="users.totalCount" 
      align="center"
      class="pag-bottom"
    ></b-pagination>
  </div>
</template>

<script>
import UserBox from "@/components/UserBox"

export default {
  props:
  {
    benefitList:
    {
      type: Boolean,
      required: false,
      default: false
    }
  },
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
  
  .wrapper
  {
    padding-top:30px;
    padding-bottom:30px;
  }

  .users {
    display: flex; 
    flex-direction:column; 
    align-items:stretch;
  }

  .pag-top {
    margin-bottom: 40px;
  }

  .pag-bottom {
    margin: 40px 0 0px 0;
  }

</style>