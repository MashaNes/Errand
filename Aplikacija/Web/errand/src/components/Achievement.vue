<template>
    <b-card no-body>
      <b-card-header  header-bg-variant = "dark" header-text-variant = "white" align="center">
        <span v-if="isSerbian">
          {{achievement.achievement.name_sr}}
        </span>
        <span v-else>
          {{achievement.achievement.name_sr}}
        </span>
      </b-card-header>
      <b-card-body :class="(achievement.level >= 4) ? 'body-golden' : 
         (achievement.level > 2) ? 'body-silver' : 'body-goldenrod'">
        <b-card-text>
          <span> {{description}} </span>
        </b-card-text>
      </b-card-body>

      <b-card-footer footer-bg-variant = "dark" footer-text-variant = "white">
        <span v-if="isSerbian">
          Nivo: {{achievement.level}}
        </span>
        <span v-else>
          Level: {{achievement.level}}
        </span>
        <span class="image-span">
          <img :src="'data:;base64,' + body.icon" height="20" width="20"/>
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
      },
      body() {
        return this.achievement.achievement
      },
      description() {
        let returnText = ""
        returnText += this.body.conditions[0].condition_numbers[this.achievement.level - 1].condition_number + " "
        if(this.isSerbian) {
          returnText += this.body.description_sr
        }
        else
          returnText += this.body.description_en
        if(this.body.conditions.length > 1)
            returnText += " " + this.body.conditions[1].condition_numbers[this.achievement.level - 1].condition_number
        return returnText
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
    border:1px solid black;
  }

  .card-footer {
    font-size: 15px;
    font-weight: bold;
    border-bottom-left-radius: 15px;
    border-bottom-right-radius: 15px;
    display:flex;
    justify-content: space-between;
    border:1px solid black;
  }

  .card {
    margin: 25px 30px 25px 30px;
    border-radius: 15px;
    width: 250px;
    /* height:max-content; */
  }

  .card-text {
    font-size: 20px;
  }

  .card-body {
    border-left:1px solid black;
    border-right: 1px solid black;
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