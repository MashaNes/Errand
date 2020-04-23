<template>
    <b-card 
      :bg-variant="(achievement.achievementLevel > 2) ? 'warning' : 'light'"
      no-body
    >

      <b-card-header 
        header-bg-variant = "dark"
        header-text-variant = "white"
        align="center"
      >
        <span v-if="isSerbian">
          {{achievement.achievementDetails.name.serbian}}
        </span>
        <span v-else>
          {{achievement.achievementDetails.name.english}}
        </span>
      </b-card-header>

      <b-card-body>
        <b-card-text>
          <span v-if="isSerbian" >
            {{achievement.achievementDetails.description.serbian}}
          </span>
          <span v-else> 
            {{achievement.achievementDetails.description.english}}
          </span>
        </b-card-text>
      </b-card-body>

      <b-card-footer
        footer-bg-variant = "dark"
        footer-text-variant = "white"
      >
        <span>
          Level: {{achievement.achievementLevel}}
        </span>
        <span class="image-span">
          <img 
            :src="achievement.achievementDetails.picture"
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
    justify-content: space-between;
  }

  .card {
    margin: 40px 150px 40px 150px;
    border-radius: 15px;
  }

  .card-text {
    font-size: 20px;
  }

</style>