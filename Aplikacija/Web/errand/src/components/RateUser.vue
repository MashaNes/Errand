<template>
  <div class="main-container">
    <div class="picture-side">
      <div class="media-center">
        <p class="image is-128x128">
          <img class="rounded-image" :src="user.picture">
        </p>
      </div>
      <div class="content">
        {{ fullUserName }}
      </div>
    </div>
    <div class="personal-info">
      <div class="grade-title">
        <span style="margin-right:10px;" v-text="isSerbian ? 'Ocenite korisnika ': 'Rate user '"></span>
        <span> {{fullUserName}} </span>
      </div>
      <span class="grade-label" v-text="isSerbian ? 'Ocena:' : 'Grade:'"></span>

      <vue-slide-bar 
        class="slide"
        v-model="grade.value" 
        :data="grade.data"
        :range="grade.range"
      ></vue-slide-bar>

      <b-form-textarea 
        class="txt-area"
        id="textarea"
        :placeholder="isSerbian ? 'Unesite kratak komentar...': 'Add a short comment...'"
        rows="3"
        max-rows="6"
        no-resize
      >
      </b-form-textarea>
    </div>
  </div>
</template>

<script>

import VueSlideBar from 'vue-slide-bar'

export default {
  components: {
    VueSlideBar
  },
  props: {
    user: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      grade: {
        value: 1,
        data: [1, 2, 3, 4, 5],
        range: [
          {
            label:1
          },
          {
            label:2
          },
          {
            label:3
          },
          {
            label:4
          },
          {
            label:5
          }
        ]
      }
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    fullUserName() {
      return this.user.firstName + " " +this.user.lastName
    },
    progressBarVariant() {
      return this.user.rating < 2.5 ? 'danger' : 
             this.user.rating < 4.5 ? 'warning' : 'success'
    },
    formattedRating() {
      return this.user.rating.toFixed(2);
    }
  },
  methods: {
    goToProfileEdit() {
      this.$emit("editProfile");
    },
    firstElements(resource) {
      const lastIndex = this.user[resource].length;
      const arrayCopy = [...this.user[resource]];
      arrayCopy.splice(lastIndex-1, 1);
      return arrayCopy;
    },
    lastElement(resource) {
      return this.user[resource][this.user[resource].length-1];
    }
  }
}
</script>

<style scoped>

  .main-container {
    margin-top: 30px;
    margin-left: 10%;
    margin-right: 5%;
    display: flex;
    flex-direction: row;
    align-items:flex-start;
    border-radius: 10px;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height: 250px;
    width:250px;
    object-fit:cover;
  }

  .content {
    background-color: black;
    font-size: 20px;
    color:white;
    text-align: center;
    margin-top: 15px;
    margin-bottom: 2px;
    border-radius: 5px;
    word-break: break-all;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
  }

  .personal-info {
    display:flex;
    flex-direction: column;
    margin-left: 15px;
    flex-grow:2;
    margin-bottom: 30px;
  }

  .picture-side {
    flex-grow:1;
    margin-bottom: 30px;
  }

  .media-center {
    display:flex;
    flex-direction:column;
    align-items:center;
  }

  .txt-area {
    width:100%;
    margin-top:20px;
  }

  .grade-title {
    font-size: 25px;
    font-weight: bold;
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    word-break:break-all;
  }

  @media only screen and (max-width: 750px)
  {
    .main-container {
      flex-direction: column;
      margin-top: 30px;
      margin-left: 1%;
      margin-right: 1%;
      align-items:center;
      border-radius: 10px;
    }

    .personal-info {
      align-self:stretch;
      margin: 10px 10px 40px 10px;
    }

    .slide {
      margin: 0 10px 0 10px;
    }

    .grade-title {
      margin: 0 10px 20px 10px;
    }

    .grade-label {
      margin: 0 10px 0 10px;
    }
  }


</style>