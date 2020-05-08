<template>
  <div :class="!forWideScreen ? 'pic-plus-info' : 'pic-plus-info-wide'">
    <div :class="!forWideScreen ? 'media-center' : 'media-center-wide'">
      <p class="image is-96x96">
        <img :class="!forWideScreen ? 'rounded-image' : 'rounded-image-wide'" :src="'data:;base64,'+user.picture">
      </p>
    </div>        
    <div :class="!forWideScreen ? 'info-only' : 'info-only-wide'">
      <div :class="!forWideScreen ? 'info-element' : 'info-element-wide'">
        <img 
          src="@/assets/signature.svg" 
          height = "20" 
          width = "20"
          style = "margin-right: 15px"
        />
        <span>{{fullUserName}}</span>
      </div>
      <div :class="[!forWideScreen ? 'info-element' : 'info-element-wide', !forWideScreen ? 'to-hide' : 'to-hide-wide']" >
        <img 
          src="@/assets/email.svg" 
          height = "20" 
          width = "20"
          style = "margin-right: 15px"
        />
        <span>{{user.email}}</span>
      </div>
      <div :class="[!forWideScreen ? 'info-element' : 'info-element-wide', !forWideScreen ? 'to-hide' : 'to-hide-wide']">
        <img 
          src="@/assets/call.svg" 
          height = "20" 
          width = "20"
          style = "margin-right: 10%"
        />
        <div class="phone-number">
          <!-- <div v-for="p in user.phone" :key="p">{{ p }}</div> -->
          <div>{{user.phone}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      required: true
    },
    forWideScreen: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    fullUserName() {
      return this.user.first_name + " " +this.user.last_name
    },
    isSerbian() {
      return this.$store.state.isSerbian
    }
  }
}
</script>

<style scoped>
  
  .rounded-image {
    border-radius: 60px;
    border: 2px solid grey;
    height:96px;
    width:96px;
    object-fit:cover;
  }

  .rounded-image-wide {
    border-radius: 60px;
    border: 2px solid grey;
    height:96px;
    width:96px;
    object-fit:cover;
  }

  .info-element {
    display:flex;
    flex-direction:column;
    margin-top: 8px;
    border-bottom: 1px solid lightgray;
  }

  .info-element-wide {
    display:flex;
    flex-direction:column;
    margin-top: 8px;
    border-bottom: 1px solid lightgray;
  }

  .phone-number {
    display: flex;
    flex-direction: column;
  }

  .pic-plus-info {
    display:flex;
    flex-direction:column;
    align-items: center;
  }

  .pic-plus-info-wide {
    display:flex;
    flex-direction:column;
    align-items: center;
  }

  @media only screen and (max-width: 499px)
  {
    .to-hide {
      visibility: hidden;
      height: 0px;
      width: 0px;
      margin:0px;
    }

    .rounded-image {
      height: 60px;
      width:60px;
      align-self: flex-start;
    }

    .media-center {
      margin: 10px 10px 0 10px;
      height:60px;
      width:60px;
      align-self:baseline;
    }

    .info-element {
      flex-direction: row;
    }

    .pic-plus-info {
      margin: 5px 5px 5px 0;
      display: flex;
      flex-direction: row;
    }

    .info-only {
      display:flex;
      flex-direction: column;
      margin: 0 5px 0 5px;
    }
  }

  @media only screen and (max-width: 599px)
    {
      .to-hide-wide {
        visibility: hidden;
        height: 0px;
        width: 0px;
        margin:0px;
      }

      .rounded-image-wide {
        height: 60px;
        width:60px;
        align-self: flex-start;
      }

      .media-center-wide {
        margin: 10px 10px 0 10px;
        height:60px;
        width:60px;
        align-self:baseline;
      }

      .info-element-wide {
        flex-direction: row;
      }

      .pic-plus-info-wide {
        margin: 5px 5px 5px 0;
        display: flex;
        flex-direction: row;
      }

      .info-only-wide {
        display:flex;
        flex-direction: column;
        margin: 0 5px 0 5px;
      }
    }
</style>