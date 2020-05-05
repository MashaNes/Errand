<template>
  <div class="wrapper">
    <div class="filter-sticky">
      <div class="filter">
        <span v-text="isSerbian ? 'Minimalna prosečna ocena:' : 'Minimal average rating:'"></span>
        <input class="filter-rating" type="number" min="0" max="4">
      </div>
      <div class="filter">
        <span v-text="isSerbian ? 'Tražite po imenu:' : 'Search by name:'"></span>
        <input class="filter-name" type="text">
      </div>
    </div>
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
    },
    isSerbian() {
      return this.$store.state.isSerbian
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
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }
  
  .body {
    background-color: #f8f9fa!important;
  }

  .users {
    display: flex; 
    flex-direction:column; 
    align-items:stretch;
  }

  .pag-top {
    margin-bottom: 40px;
    margin-top: 20px;
    z-index:0;
  }

  .pag-bottom {
    margin: 40px 0 0px 0;
    z-index:0;
  }

  .filter-sticky {
    position: sticky;
    top:70px;
    border: 1px solid black;
    border-top: hidden;
    align-self:center;
    z-index: 1;
    display: flex;
    flex-direction: column;
    background-color: white;
    padding:10px;
    border-bottom-left-radius:5px;
    border-bottom-right-radius:5px;
  }

  .filter {
    display:flex;
    margin: 5px;
    font-weight: 600;
    align-items: center;
  }

  .filter-rating {
    width: 40px;
    margin-left:10px
  }

  .filter-name {
    max-width:200px;
    margin-left:10px;
  }

  @media only screen and (max-width: 499px)
  {
    .filter-sticky {
      top:85px;
      margin-left:10px;
      margin-right: 10px;
      font-size: 15px;
    }
  }

</style>