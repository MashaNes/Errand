<template>
  <div id="map" class="map" ref="map"></div>
</template>

<script>

import {mapState} from 'vuex'

export default {
  props: {
  
  },
  data() {
    return {
      map: null,
      markers: []
    }
  },
  computed: mapState(['mapMarkerPositions']),
  methods: {
    mapClick(latLng) {
      this.$emit('mapClick', latLng)
    }
  },
  watch: {
    mapMarkerPositions(newVal, oldVal) {
      if(oldVal.length == newVal.length) {
        this.markers[0].setMap(null)
        this.markers[0] = null
        this.markers[0] = new window.google.maps.Marker({
          position: newVal[0],
          map: this.map
        })
        this.map.setCenter(newVal[0])
      }
      else if(oldVal.length < newVal.length) {
        const newMark = new window.google.maps.Marker({
          position: newVal[newVal.length - 1],
          map: this.map
        })
        this.markers.push(newMark)
      }
      else {
        this.markers[this.markers.length-1].setMap(null)
        this.markers[this.markers.length - 1] = null
        this.markers.pop()
      }
    }
  },
  mounted() {
    this.map = new window.google.maps.Map(this.$refs["map"], {
      center: {
        lat: 43.639696,
        lng: 21.878703
      },
      zoom: 10
    })
    const vm = this
    function checkForMap() {
      if(vm.map) {
        vm.mapMarkerPositions.forEach(mark => {
          const newMark = new window.google.maps.Marker({
            position: mark,
            map:vm.map
          })
          vm.markers.push(newMark)
        }) 
      }
      else 
        setTimeout(checkForMap, 200)
    }
    checkForMap()
    this.map.addListener('click', (event) => {
      this.mapClick(event.latLng)
    })
  }
}
</script>

<style scoped>
  .map {
    min-height: 400px;
    align-self: stretch;
  }
</style>