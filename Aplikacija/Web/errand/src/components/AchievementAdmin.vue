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
                <a @click="details" v-if="!noDetail"><img src="../assets/more.png" height="7" width="20" v-b-popover.hover.top="popoverText"/></a>
                <a @click="close" v-else><img src="../assets/remove.png" height="20" width="20" v-b-popover.hover.top="popoverText2"/></a>
            </span>
        </b-card-header>
        <b-card-body class="body-goldenrod">
            <b-card-text class="telo">
                <img :src="'data:;base64,' + achievement.icon" height="60" width="60" class="slika" />
                <span v-if="isSerbian">
                    {{achievement.description_sr}}
                </span>
                <span v-else> 
                    {{achievement.description_en}}
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
            <!--<span class="image-span">
                <span class="image-background">
                    <img :src="'data:;base64,' + achievement.icon" height="20" width="20" />
                </span>
            </span>-->
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

  .image-background
  {
      background-color:white;
      padding:3px;
      border-radius: 5px;
      padding-bottom:5px;
      height:fit-content;
  }

</style>