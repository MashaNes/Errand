<template>
    <div>
        <div class = "top-div" v-if="isSerbian"> Pomoć </div>
        <div class = "top-div" v-else> Help </div>
        <AndroidApp />
    </div>
</template>

<script>
    import AndroidApp from "@/components/AndroidApp"
    export default {
        components:
        {
            AndroidApp
        },
        computed:
        {
            isSerbian()
            {
                return this.$store.state.isSerbian
            }
        }
    }
</script>

<style scoped>

</style>