<template>
    <Spinner v-if="Notifications == null" />
    <div class="page-wrapper" v-else>
        <div v-for="notification in Notifications" :key="notification.id" class="notification-div">
            <NotificationAndroid :notification="notification" v-if="notification.notification_type == 0 || notification.notification_type == 3 || notification.notification_type == 4"/>
            <NotificationNew :notification="notification" v-if="notification.notification_type != 0 && notification.notification_type != 3 && notification.notification_type != 4 && notification.opened == false"/>
            <NotificationRead :notification="notification" v-if="notification.notification_type != 0 && notification.notification_type != 3 && notification.notification_type != 4 && notification.opened == true"/>
        </div>
        <div v-if="Notifications != null && moreNotifications && isDataLoaded" class="button-div">
            <button type="button" class="btn btn-info" @click="loadMore">
                <span v-if="isSerbian"> Učitaj još </span>
                <span v-else> Load more </span>
            </button>
        </div>
        <div v-if="Notifications != null && moreNotifications && !isDataLoaded" class="button-div">
            <button type="button" class="btn btn-info" :disabled="true">
                <span v-if="isSerbian"> Učitavanje... </span>
                <span v-else> Loading... </span>
            </button>
        </div>
    </div>
</template>

<script>
    import Spinner from "@/components/Spinner"
    import NotificationAndroid from "@/components/NotificationAndroid"
    import NotificationNew from "@/components/NotificationNew"
    import NotificationRead from "@/components/NotificationRead"
    export default {
        components:
        {
            Spinner,
            NotificationAndroid,
            NotificationNew,
            NotificationRead
        },
        computed:
        {
            Notifications()
            {
                return this.$store.state.notifications
            },
            moreNotifications()
            {
                return this.$store.state.moreNotifications
            }
        },
        created()
        {
            if(this.$store.state.notifications == null)
            {
                this.$store.state.notificationsPage = 1
                this.$store.dispatch("getNotifications", 1);
            }
            else
            {
                var ids = [];
                this.$store.state.notifications.forEach(element =>
                {
                    if(element.seen == false)
                    {
                        element.seen = true
                        ids.push(element.id)
                    }
                })

                if(ids.length > 0)
                {
                    this.$store.state.notificationNumber -= ids.length
                    this.$store.dispatch("setNotificationFlag", {ids: ids, seen: true, opened: false})
                }
            }
        },
        methods:
        {
            loadMore()
            {
                this.$store.state.notificationsPage = this.$store.state.notificationsPage + 1
                this.$store.dispatch("getNotifications", this.$store.state.notificationsPage);
            }
        }
    }
</script>

<style scoped>
    .page-wrapper
    {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding:20px;
    }

    .notification-div
    {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 20px;
    }

    .button-div
    {
        width:100%;
        display:flex;
        flex-direction: column;
        align-items: center;
        margin-top:25px;
    }

    @media only screen and (max-width: 450px)
    {
        .page-wrapper
        {
            padding-left: 0px;
            padding-right: 0px;
        }
    }
</style>