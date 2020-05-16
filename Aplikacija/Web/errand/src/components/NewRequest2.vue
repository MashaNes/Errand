<template>
    <div class="wrapper-step2">
        <div class="labelDiv">
            <span v-if="isSerbian"> Finalna adresa: </span>
            <span v-else> Final address: </span>
        </div>
        <div class="mapDiv">
            Map goes here
            <!-- Kad god se menja adresa pozovi addressChanged-->
        </div>
    </div>
</template>

<script>
export default {
    props:
    {
        address:
        {
            type: Object,
            required:true
        }
    },
    data()
    {
        return{
            myAddress: this.address
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        }
    },
    methods:
    {
        addressChanged()
        {
            this.$emit("addressChanged", this.myAddress)
        }
    }
}
</script>

<style scoped>
    .wrapper-step2
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .labelDiv
    {
        width:100%;
        padding-left:10px;
        display:flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items:center;
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 5px;
    }

    .mapDiv
    {
        border: 1px solid red; /*skloniti*/
        width:100%;
        height:40vh; /* lupljena visina */
    }
</style>