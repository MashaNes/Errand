<template>
    <b-card 
      :border-variant="progressBarVariant"
      no-body
      style="width: 300px"
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
        <div>
          <span v-if="isSerbian">
            Ocena:
          </span>
          <span v-else>
            Grade:
          </span>
        </div>

        <b-progress class="mt-2" max="5" show-value>
            <b-progress-bar 
                :value="rating.grade" 
                :variant="progressBarVariant"></b-progress-bar>
        </b-progress>
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
      }
    }
}
</script>

<style scoped>

  .achievement-container {
    margin: 30px;
    margin-left: 100px;
    margin-right:100px;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
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
    display: flex;
    flex-direction: column;
  }

  .card {
    margin: 10px;
    font-size: 14px;
    border-radius: 15px;
    width: 300px;
  }

</style>