<template>
  <div class="personal-info">
    <div>
      <div class="media-center">
        <p class="image is-128x128">
          <img class="rounded-image" :src="user.picture">
        </p>
      </div>
      <div class="content">
        {{ fullUserName }}
      </div>
    </div>
    <b-list-group >
      <b-list-group-item v-if="isSerbian">
        <span 
          class="info-title" 
        > Lični podaci </span>
        <b-button class="button is-primary">
          <strong>Izmeni</strong>
        </b-button>
      </b-list-group-item>
      <b-list-group-item v-else>
        <span
          class="info-title" 
        > Personal info </span>
        <b-button class="button is-primary">
          <strong>Edit</strong>
        </b-button>
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
    flex-wrap: wrap;
    justify-content: space-between;
    border: hidden;
    border-radius: 0px;
    align-items: center;
    min-width: 30px;
  }

  .list-key {
    font-weight: bold;
    font-size: 14px;
    flex-grow: 1;
    flex-shrink: 1;
    text-align: left;
  }

  .list-value {
    flex-grow: 2;
    flex-shrink: 1;
    text-align: left;
    font-size: 20px;
    margin-left: 30px;
  }

  .personal-info {
    margin: 30px;
    margin-left: 150px;
    margin-right: 150px;
    display: flex;
    align-items: flex-start;
    flex-direction: row;
    align-items:flex-start;
    border-radius: 10px;
    flex-wrap: wrap;
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
    margin-top: 15px;
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