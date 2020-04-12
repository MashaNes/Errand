<template>
  <div class="personal-info">
    <div>
      <div class="media-center">
        <p class="image is-128x128">
          <img class="rounded-image" :src="user.picture">
        </p>
      </div>
      <div class="content">
        {{ user.userName }}
      </div>
    </div>
    <b-list-group>
      <b-list-group-item>
        <span 
          class="info-title" 
          v-if="isSerbian"
        > Lični podaci </span>
        <span
          class="info-title" 
          v-else
        > Personal info </span>
      </b-list-group-item>
      <b-list-group-item style = "font-size: 25px">
        {{fullUserName}}
      </b-list-group-item>
      <b-list-group-item>
        <img 
          src="@/assets/email.svg" 
          height = "20" 
          width = "20"
          style = "margin-right: 15px"
        />
        <span class="list-value">{{ user.email }}</span>
      </b-list-group-item>
      <b-list-group-item>
        <img 
          src="@/assets/call.svg" 
          height = "20" 
          width = "20"
          style = "margin-right: 15px"
        />
        <span class="list-value">{{ user.phone }}</span>
      </b-list-group-item>
      <b-list-group-item>
        <img 
          src="@/assets/address.svg" 
          height = "20" 
          width = "20"
          style = "margin-right: 15px"
        />
        <span class="list-value">{{ user.homeAddress }}</span>
      </b-list-group-item>
    </b-list-group>
  </div>
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
      fullUserName() {
        return this.user.firstName + " " +this.user.lastName
      },
      isSerbian() {
        return this.$store.state.isSerbian
      }
    }
}
</script>

<style scoped>
  
  .list-group-item {
    display: flex;
    justify-content: space-between;
    border: hidden;
    border-radius: 0px;
    align-items: center;
  }

  .list-key {
    font-weight: bold;
    font-size: 14px;
    flex-grow: 1;
    text-align: left;
  }

  .list-value {
    flex-grow: 2;
    text-align: left;
    font-size: 20px;
    margin-left: 30px;
  }

  .personal-info {
    margin-top: 10px;
    margin-left: 10px;
    display: flex;
    align-items: flex-start;
    flex-direction: row;
    align-items:flex-start;
    border-radius: 10px;
    border-right: 1px solid lightgray;

    background-color: white;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
  }

  .content {
    background-color: black;
    font-size: 20px;
    color:white;
    text-align: center;
    margin-top: 2px;
    margin-bottom: 2px;
    border-radius: 5px;
  }

  .info-title {
    font-size: 25px;
    font-weight: bold;
    color: black;
    margin-bottom: 5px;
  }

  .is-128x128 {
    width: 250px;
    height: 250px;
  }
</style>