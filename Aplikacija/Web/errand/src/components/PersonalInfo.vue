<template>
  <div>
    <div class="media">
      <div class="media-center">
        <p class="image is-128x128">
          <img class="rounded-image" :src="user.picture">
        </p>
      </div>
      <div class="content">
        {{ fullUserName }}
      </div>
      <b-list-group>
        <b-list-group-item>
          <span class="list-key">Name:</span> 
          <span class="list-value">{{ user.firstName }}</span>
        </b-list-group-item>
        <b-list-group-item>
          <span class="list-key">Last name:</span> 
          <span class="list-value">{{ user.lastName }}</span>
        </b-list-group-item>
        <b-list-group-item>
          <span class="list-key">Email:</span> 
          <span class="list-value">{{ user.email }}</span>
        </b-list-group-item>
        <b-list-group-item>
          <span class="list-key">Phone:</span> 
          <span class="list-value">{{ user.phone }}</span>
        </b-list-group-item>
      </b-list-group>
    </div>
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
      }
    }
}
</script>

<style scoped>
  
  .list-group-item {
    display: flex;
    justify-content: space-between;
    flex-direction: column;
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

  .media {
    display: flex;
    align-items: center;
    flex-direction: column;
  }

  .rounded-image {
    border-radius: 60px;
  }


</style>