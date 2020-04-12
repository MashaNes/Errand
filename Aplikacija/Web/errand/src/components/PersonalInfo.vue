<template>
  <div class="personal-info">
    <span 
      class="info-title" 
      v-if="isSerbian"
    > Lični podaci </span>
    <span
      class="info-title" 
      v-else
    > Personal info </span>
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
    <b-list-group>
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
    border-bottom: solid lightgray 1px;
    border-top: solid lightgray 1px;
    border-left:hidden;
    border-right: 1px solid lightgrey;

    border-radius: 0px;

    max-width: 300px;
    align-items: flex-start;
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
  }

  .personal-info {
    margin-top: 10px;
    margin-left: 10px;
    display: flex;
    align-items: flex-start;
    flex-direction: column;
    align-items:flex-start;
    border-radius: 10px;
    border-right: 1px solid lightgray;
  }

  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
  }

  .content {
    background-color: black;
    color:white;
    text-align: center;
    margin-top: 2px;
    margin-bottom: 2px;
  }

  .info-title {
    font-size: 25px;
    font-weight: bold;
    width: 100%;
    background-color: black;
    border-top-left-radius: 6px;
    border-top-right-radius: 6px;
    text-align: center;
    color: white;
    margin-bottom: 5px;
  }

</style>