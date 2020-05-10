<template>
    <div class="wrapper-component">
        <UserService v-for="userService in UserServices" :key="userService.service.id" :editable="editable" :userService="userService"/>
    </div>
</template>

<script>
import UserService from "@/components/UserService"
export default {
    props:
    {
        editable:
        {
            type: Boolean,
            required: true
        }
    },
    components:
    {
        UserService
    },
    computed:
    {
        UserServices()
        {
            if(this.editable)
                return this.$store.state.userServices
            else
                return []
        }
    }
}
</script>

<style scoped>
    .wrapper-component
    {
        padding: 20px;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
    }
</style>