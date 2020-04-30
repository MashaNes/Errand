<template>
    <b-card 
      :border-variant="progressBarVariant"
      no-body
    >
      <b-card-body>
        <div class="media-center">
          <p class="image is-96x96">
            <img class="rounded-image" :src="user.picture">
          </p>
        </div>
          
        <div class="info">
          <div class="info-element">
            <img 
              src="@/assets/signature.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <span>{{fullUserName}}</span>
          </div>
          <div class="info-element">
            <img 
              src="@/assets/email.svg" 
              height = "20" 
              width = "20"
              style = "margin-right: 15px"
            />
            <span>{{user.email}}</span>
          </div>
          <div class="info-element grade">
            <span v-if="isSerbian" class="grade-label">
              Prosečna ocena:
            </span>
            <span v-else class="grade-label">
              Average rating:
            </span>
          
            <b-progress class="mt-2" max="5.0" height="20px">
              <b-progress-bar 
                :value="user.rating"
                :variant="progressBarVariant"
              > 
                <span class="rating-grade"> {{user.rating}} </span> 
              </b-progress-bar>
            </b-progress>
          </div>
        </div>
      </b-card-body>
    </b-card>
</template>

<script>
export default {
    props: {
      user: {
        required: true, 
        type: Object
      }
    },
    computed: {
      isSerbian() {
        return this.$store.state.isSerbian
      },
      progressBarVariant() {
        return this.user.rating < 2.5 ? 'danger' : 
               this.user.rating < 4.5 ? 'warning' : 'success'
      },
      fullUserName() {
        return this.user.firstName + " " +this.user.lastName
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



  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:96px;
    width:96px;
    object-fit:cover;
  }

  .card-body {
    display:flex;
    flex-direction: row;
  }

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
    display:flex; 
    flex-direction:row;
  }

  .card {
    margin: 10px 15% 10px 15%;
    border-radius: 15px;
  }

  .rating-grade {
    font-size:15px; 
    font-weight: 700; 
    color:black;
  }

  .mt-2 {
    width:60%; 
    min-width:70px;
  }

  .grade-label {
    margin: 5px 10px 0 0;
  }

  .info {
    display:flex;
    flex-direction: column;
    margin-left: 10px;
    width:100%;
  }

  .info-element {
    display:flex;
    flex-direction: row;
    word-break:break-all;
    margin: 5px 0 0 5px;
    align-items: center;
  }

  .grade {
    word-break:normal;
  }

  @media only screen and (max-width:650px)
  {
    .card-footer {
      flex-direction: column;
    }

    .card-body {
      flex-direction:column;
      align-items:center;
      padding:10px;
    }

    .card {
      margin:10px 10% 0 10%;
      font-size: 15px;
      
    }

    .grade-label {
      margin: 5px 5px 0 0;
    }
    
    .info {
      margin-left: 0;
    }

    .info-element {
      flex-direction: column;
      align-items: flex-start;
      margin-top:15px;
    }

    .mt-2 {
      width:80%;
    }

  }

  /* @media only screen and (max-width:500px)
  {
    .card {
      margin: 40px 10px 40px 10px;
    }

  } */

</style>