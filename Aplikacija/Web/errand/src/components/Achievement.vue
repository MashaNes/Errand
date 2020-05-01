<template>
    <b-card 
      no-body
    >

      <b-card-header 
        header-bg-variant = "dark"
        header-text-variant = "white"
        align="center"
      >
        <span v-if="isSerbian">
          {{achievement.achievement.name.serbian}}
        </span>
        <span v-else>
          {{achievement.achievement.name.english}}
        </span>
      </b-card-header>

      <b-card-body 
        :class="(achievement.level >= 4) ? 'body-golden' : 
        (achievement.level > 2) ? 'body-silver' : 'body-goldenrod'">
        <b-card-text>
          <span v-if="isSerbian" >
            {{achievement.achievement.description.serbian}}
          </span>
          <span v-else> 
            {{achievement.achievement.description.english}}
          </span>
        </b-card-text>
      </b-card-body>

      <b-card-footer
        footer-bg-variant = "dark"
        footer-text-variant = "white"
      >
        <span>
          Level: {{achievement.level}}
        </span>
        <span class="image-span">
          <img 
            :src="achievement.achievement.icon"
            height="20"
            width="20"
          />
        </span>
      </b-card-footer>
    </b-card>
</template>

<script>
export default {
    props: {
      achievement: {
        required: true, 
        type: Object
      }
    },
    computed: {
      isSerbian() {
        return this.$store.state.isSerbian
      }
    }
}
</script>

<style scoped>

  .card-header {
    font-size: 18px;
    font-weight: bold;
    border-top-left-radius: 15px;
    border-top-right-radius: 15px;
  }

  .card-footer {
    font-size: 18px;
    font-weight: bold;
    border-bottom-left-radius: 15px;
    border-bottom-right-radius: 15px;
    display:flex;
    justify-content: space-between;
  }

  .card {
    margin: 50px 15% 50px 15%;
    border-radius: 15px;
  }

  .card-text {
    font-size: 25px;
  }

  .body-silver {
    background-color: rgb(207, 202, 202);
  }

  .body-golden {
    background-color: rgb(250, 212, 41);
  }

  .body-goldenrod {
    background-color: rgb(216, 166, 41);
  }

  @media only screen and (max-width:650px)
  {
    .card {
      margin: 30px 5% 30px 5%;
    }
  }

</style>