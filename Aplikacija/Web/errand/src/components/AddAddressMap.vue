<template>
  <div class="main-wrapper">
    <div class="btns">
      <b-button 
        class="button is-primary upper-btn"
        @click="$emit('close', null)"
        v-text="isSerbian ? 'Zatvori mapu' : 'Close map'"
      ></b-button>
      <b-button 
        class="button is-primary"
        @click="convertToAddress"
        v-text="isSerbian ? 'Preuzmi adresu sa mape' : 'Get address from the map'"
      ></b-button>
    </div>
    <div class="addr-input">
      <input 
        class="input is-medium"
        type="text"
        :placeholder="isSerbian ? 'Adresa' : 'Address'"
        v-model="newAddressName"
      >
      <img 
        v-if="newAddressName && addressChecked && !invalidAddress" 
        class="btn-img" 
        src="@/assets/check.svg" 
        height="33" 
        width="33"
        @click="confirmAddress"
      >
      <img 
        v-if="newAddressName && !addressChecked" 
        class="search btn-img" 
        src="@/assets/search.svg" 
        height="35" 
        width="35"
        @click="convertFromAddress"
      >
    </div>
    <span  class="span-danger" v-if="invalidAddress" v-text="isSerbian ? 
    'Adresa nije pronađena na mapi. Jeste li sigurni da postoji?' :
    'The address was not found on the map. Are you sure it exists?'"></span>
    <div class="map">
      <Map @mapClick="moveMarker" />
    </div>
  </div>
</template>

<script>
import Map from "@/components/Map"
export default {
  components: {
    Map
  },
  data() {
    return {
      previousPosition: {},
      newAddressName: "",
      fullNewAddress: null,
      previousInput: "",
      invalidAddress: false,
      markerMoved: true
    }
  },
  methods: {
    moveMarker(latLng) {
      const newMarkerPositions = [{
        pos: {
          lat: latLng.lat(),
          lng: latLng.lng(),
        },
        label: "",
        info: ""
      }]
      this.$store.dispatch('setMarkerPositions', newMarkerPositions)
      this.markerMoved = true
    },
    convertToAddress() {
      // eslint-disable-next-line no-debugger
      debugger
      if(this.markerMoved || !this.addressChecked) {

        fetch('https://maps.googleapis.com/maps/api/geocode/json?latlng='
              + this.markerPosition.pos.lat + ',' + this.markerPosition.pos.lng + 
              '&key=AIzaSyBc7vAECB9mQ1RbCrySraxt6ve0VxXO7zs', {
          method:'GET'
        }).then(p => {
          if(p.ok)
          {
            p.json().then(data => {
              console.log(data.results[0])
              this.newAddressName = data.results[0].formatted_address
              this.fullNewAddress = {
                name: this.newAddressName,
                longitude: data.results[0].geometry.location.lng,
                latitude: data.results[0].geometry.location. lat
              }
              this.previousInput = this.newAddressName
              this.invalidAddress = false
              this.markerMoved = false
            })
          }
        })
      }
    },
    convertFromAddress() {
      if(this.newAddressName == this.previousInput)
      {
        return
      }
      else {
        const vm = this
        this.previousInput = this.newAddressName
        fetch('https://maps.googleapis.com/maps/api/geocode/json?address='
        + this.newAddressName +'&key=AIzaSyBc7vAECB9mQ1RbCrySraxt6ve0VxXO7zs', {
          method: 'GET'
          }).then(p => {
          if(p.ok) {
            p.json().then(data => {
              if(data.status!='ZERO_RESULTS') {
                const newPositions = [{
                  pos: {
                    lat: data.results[0].geometry.location.lat,
                    lng: data.results[0].geometry.location.lng,
                  },
                  label: "",
                  info: ""
                }]
                vm.$store.dispatch('setMarkerPositions', newPositions)
                this.markerMoved = false
                this.invalidAddress = false
                this.fullNewAddress = {
                  name: this.newAddressName,
                  longitude: data.results[0].geometry.location.lng,
                  latitude: data.results[0].geometry.location. lat
                }
              }
              else {
                this.invalidAddress = true
              }
            })
          }
        })
      }
    },
    confirmAddress() {
      console.log(this.newAddressName)
      const toAdd = JSON.parse(JSON.stringify(this.fullNewAddress))
      this.previousPosition = {}
      this.newAddressName = "",
      this.fullNewAddress = null,
      this.previousInput = ""
      this.invalidAddress = false
      this.markerMoved = true
      const newPositions = [{
        pos: {
          lat: 43.639696,
          lng: 21.878703,
        },
        lab: "",
        info: ""
      }]
      this.$store.dispatch('setMarkerPositions', newPositions)
      this.$emit('close', toAdd)
      
    }
  },
  computed: {
    isSerbian() {
      return this.$store.state.isSerbian
    },
    addressChecked() {
      return this.newAddressName == this.previousInput
    },
    markerPosition() {
      return this.$store.state.mapMarkerPositions[0]
    }
  },
  created() {
    const newPositions = [{
      pos: {
        lat: 43.639696,
        lng: 21.878703,
      },
      lab: "",
      info: ""
    }]
    this.$store.dispatch('setMarkerPositions', newPositions)
  }
  // mounted() {
  //   this.map = new window.google.maps.Map(this.$refs["map"], {
  //     center: {
  //       lat: 43.639696,
  //       lng: 21.878703
  //     },
  //     zoom: 10
  //   })
  //   const vm = this
  //   function checkForMap() {
  //     if(vm.map) 
  //       vm.marker = new window.google.maps.Marker({
  //         position: vm.pos,
  //         map: vm.map
  //       })
  //     else 
  //       setTimeout(checkForMap, 200)
  //   }
  //   checkForMap()
  //   this.map.addListener('click', (event) => {
  //     vm.moveMarker(event.latLng)
  //   })
  // }
}
</script>

<style scoped>

  .main-wrapper {
    display:flex;
    flex-direction: column;
    align-items: flex-start;
    height:100%;
    padding: 10px 5% 100px 5%;
  }

  .map {
    min-height: 400px;
    align-self: stretch;
  }

  .button {
    align-self: flex-start;
    justify-self: flex-start;
    margin-right: 10px;
    font-weight: bold;
  }

  .input {
    margin: 10px 0px 10px 0;
  }

  .addr-input {
    display:flex;
    justify-content: stretch;
    align-items:center;
    width:100%;
  }

  .btn-img {
    cursor: pointer;
    margin-left: 10px;
  }

  .span-danger {
    color: red;
    font-size: 14px;
    margin-bottom:10px;

  }

  @media only screen and (max-width: 499px)
  {
    .span-danger {
      font-size: 12px;
    }

    .btns {
      display: flex;
      flex-direction: column;
      align-items: stretch;
    }

    .button {
      width: 100%;
      margin-bottom: 10px;
    }

    .upper-btn {
      margin-top: 30px;
    }

    .map {
      margin-top: 20px;
    }
  }

</style>