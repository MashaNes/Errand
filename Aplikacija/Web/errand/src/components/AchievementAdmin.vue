<template>
    <b-card  no-body>
        <b-card-header header-bg-variant = "dark" header-text-variant = "white" align="center">
            <span v-if="isSerbian">
                {{achievement.name_sr}}
            </span>
            <span v-else>
                {{achievement.name_en}}
            </span>
            <span class="image-span">
                <span class="image-background">
                    <a @click="details" v-if="!noDetail"><img src="../assets/more.svg" height="7" width="20" v-b-popover.hover.top="popoverText"/></a>
                    <a @click="close" v-else><img src="../assets/remove.svg" height="20" width="20" v-b-popover.hover.top="popoverText2"/></a>
                </span>
            </span>
        </b-card-header>
        <b-card-body class="body-goldenrod">
            <b-card-text>
                <span v-if="isSerbian">
                    X{{achievement.description_sr}}<span v-if="achievement.conditions.length > 1">Y</span>
                </span>
                <span v-else> 
                    X{{achievement.description_en}}<span v-if="achievement.conditions.length > 1">Y</span>
                </span>
            </b-card-text>
        </b-card-body>
        <b-card-footer footer-bg-variant = "dark" footer-text-variant = "white">
            <span v-if="isSerbian">
                Broj nivoa: {{achievement.levels}}
            </span>
            <span v-else>
                Level: {{achievement.levels}}
            </span>
            <span class="image-span">
                <span class="image-background">
                    <img :src="achievement.icon" height="20" width="20" />
                </span>
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
      },
      noDetail:
      {
          type:Boolean,
          required:true
      }
    },
    computed: {
      isSerbian() {
        return this.$store.state.isSerbian
      },
      popoverText()
      {
            if(this.isSerbian)
                return "Pogledaj detalje"
            else
                return "Open details"
      },
      popoverText2()
      {
            if(this.isSerbian)
                return "Zatvori detalje"
            else
                return "Close details"
      }
    },
    methods:
    {
        details()
        {
            this.$emit("openDetails", this.achievement)
        },
        close()
        {
            this.$emit("closeDetails")
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
    display:flex;
    justify-content: space-between;
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

  .image-background
  {
      background-color:white;
      padding:3px;
      border-radius: 5px;
      padding-bottom:5px;
      height:fit-content;
  }

</style>