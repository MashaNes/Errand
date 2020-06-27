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
      <b-card-body :class="klasa">
        <b-card-text  class="telo">
          <img :src="'data:;base64,' + body.icon" height="60" width="60" class="slika" />
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
        
        if(this.isSerbian)
          returnText = this.body.description_sr
        else
          returnText = this.body.description_en
        
        this.body.conditions.forEach((element,index) => 
        {
          const broj = index + 1
          returnText = returnText.replace("{" + broj + "}", element.condition_numbers[this.achievement.level - 1].condition_number)
        })
        
        return returnText
      },
      klasa()
      {
        if(this.achievement.achievement.levels == 1)
          return 'body-golden'
        else if(this.achievement.achievement.levels == 2 && this.achievement.level == 1)
          return 'body-silver'
        else if(this.achievement.achievement.levels == 2 && this.achievement.level == 2)
          return 'body-golden'
        else
        {
          if(this.achievement.level < this.achievement.achievement.levels/3)
            return 'body-goldenrod'
          else if (this.achievement.level < (2 * this.achievement.achievement.levels)/3)
            return 'body-silver'
          else
            return 'body-golden'
        }
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

  .image-background
  {
      background-color:white;
      padding:3px;
      border-radius: 5px;
      padding-bottom:5px;
      height:fit-content;
  }

  .telo
  {
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: 16px;
    text-align: center;
  }

  .slika
  {
    margin-bottom:15px;
  }

  @media only screen and (max-width:650px)
  {
    .card {
      margin: 30px 5% 30px 5%;
    }
  }

</style>