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
            <!-- dodati da se na klik predje na korisnika -->
            <span class ="link-label" @click="goToProfile()"> {{givenBy}} </span>
          </div>
          <div> 
            <span v-if="isSerbian">Za zahtev:</span>
            <span v-else>For request:</span>
            <!-- dodati da se na klik predje na sam request -->
            <span class ="link-label"> {{forRequest}} </span>
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
        return this.rating.createdBy.firstName + " " + this.rating.createdBy.lastName
      },
      forRequest() {
        return this.rating.request.name
      }
    },
    methods: {
      goToProfile()
      {
        this.$router.push({
          name: "PageViewProfile", 
          params: {
            id: this.$store.state.authUser.id, 
            user: this.$store.state.authUser
          }
        })
      }
    }
}
</script>

<style scoped>

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
    border: hidden;
    border-top: 1px solid grey;
  }

  .card {
    margin: 40px 20% 40px 20%;
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
    flex-grow:8;
    margin-right:20px; 
    margin-left:2%;
  }

  .grade-info {
    flex-grow: 1;
    margin-left:2%;
    word-break: break-all;
  }

  .link-label {
    font-size:18px;
  }

  .link-labe:hover {
    cursor: pointer !important;
    text-decoration: underline;
    color:lightseagreen;
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