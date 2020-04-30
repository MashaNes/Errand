<template>
    <b-card 
      :border-variant="progressBarVariant"
      no-body
    >
      <b-card-body>
        <b-card-text>
          {{rating.comment}}
        </b-card-text>
      </b-card-body>

      <b-card-footer
        footer-bg-variant = "dark"
        footer-text-variant = "white"
      >
        <div class="grade">
          <div>
            <span v-if="isSerbian">
              Ocena
            </span>
            <span v-else>
              Grade
            </span>
          </div>

          <b-progress class="mt-2" max="5.0" height="20px">
              <b-progress-bar 
                :value="rating.grade"
                :variant="progressBarVariant"
              > 
                <span class="rating-grade"> {{rating.grade}} </span> 
              </b-progress-bar>
          </b-progress>
        </div>
        <div class="grade-info"> 
          <div style="margin-bottom:10px;"> 
            <span v-if="isSerbian">Ocenio/la:</span>
            <span v-else>Rated by:</span>
            <span style="font-size:18px;"> {{givenBy}} </span>
          </div>
          <div> 
            <span v-if="isSerbian">Za zahtev:</span>
            <span v-else>For request:</span>
            <span style="font-size:18px;"> {{forRequest}} </span>
          </div>
        </div>
      </b-card-footer>
    </b-card>
</template>

<script>
export default {
    props: {
      rating: {
        required: true, 
        type: Object
      }
    },
    computed: {
      isSerbian() {
        return this.$store.state.isSerbian
      },
      progressBarVariant() {
        return this.rating.grade < 2 ? 'danger' : 
               this.rating.grade < 5 ? 'warning' : 'success'
      },
      givenBy() {
        const users = this.$store.state.allUsers
        return users[this.rating.createdBy].firstName + " " + users[this.rating.createdBy].lastName
      },
      forRequest() {
        const requests = this.$store.state.requests
        return requests[this.rating.request].name
      }
    }
}
</script>

<style scoped>

  /* .achievement-container {
    margin: 30px;
    margin-left: 100px;
    margin-right:100px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  } */

  .card-header {
    font-size: 15px;
    font-weight: bold;
    border-top-left-radius: 15px;
    border-top-right-radius: 15px;
  }

  .card-footer {
    font-size: 15px;
    font-weight: bold;
    border-bottom-left-radius: 15px;
    border-bottom-right-radius: 15px;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    display:flex; 
    flex-direction:row;
  }

  .card {
    margin: 40px 10% 40px 10%;
    border-radius: 15px;
  }

  .card-text {
    font-size:20px;
    font-weight: 600;
  }

  .rating-grade {
    font-size:15px; 
    font-weight: 700; 
    color:black;
  }

  .mt-2 {
    width:80%; 
    min-width:70px;
  }

  .grade {
    flex-grow:10;
    margin-right:20px; 
    margin-left:2%;
  }

  .grade-info {
    margin-left:2%;
  }

  @media only screen and (max-width:650px)
  {
    .card-footer {
      flex-direction: column;
    }
  }

  @media only screen and (max-width:500px)
  {
    .card {
      margin: 40px 10px 40px 10px;
    }

    .grade-info {
      margin-top:20px;
    }

  }

</style>