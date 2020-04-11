<template>
  <div class="achievement-container">
    <b-card 
      :bg-variant="(achievement.achievementLevel > 2) ? 'warning' : 'light'"
      :key="achievement.id"
      v-for="achievement in achievements"
      no-body
      style = "max-height: 200px"
    >
      <!-- <template v-slot:header>
        <h4 class="mb-0">{{achievement.achievementDetails.name.english}}</h4>
      </template> -->

      <b-card-header 
        header-bg-variant = "dark"
        header-text-variant = "white"
        align="center"
      >
        {{achievement.achievementDetails.name.english}}
      </b-card-header>

      <b-card-body
        
      >
        <b-card-text>
          {{achievement.achievementDetails.description.english}}
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

      <!-- <b-card-img src="https://placekitten.com/480/210" alt="Image" bottom></b-card-img> -->
    </b-card>
  </div>
</template>

<script>
export default {
    props: {
      achievements: {
        required: true, 
        type: Object
      }
    }
}
</script>

<style scoped>

  .achievement-container {
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
    display:flex;
    justify-content: space-between;
  }

  .card {
    margin: 10px;
    font-size: 12px;
    border-radius: 15px;
  }

</style>