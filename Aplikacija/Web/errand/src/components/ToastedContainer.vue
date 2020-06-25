<template>
  <div>
  </div>
</template>

<script>
export default {
  props: {
    notification: {
      type: Object,
      requied: true
    }
  },
  created() {
    this.$toasted.error(this.isSerbian ? this.notification.body_sr : this.notification.body_en, {
      duration: 5000
    })
  }
}
</script>

<style scoped>

</style>