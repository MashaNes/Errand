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
      console.log('watcher')
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
      zoom: 17
    })
    const vm = this
    
    window.google.maps.event.addListener(this.map, 'idle', checkForMap)

    function checkForMap() {
      if(vm.map) {
        let bounds = new window.google.maps.LatLngBounds()
        vm.mapMarkerPositions.forEach(mark => {
          const newMark = new window.google.maps.Marker({
            position: mark,
            map:vm.map
          })
          vm.markers.push(newMark)
          bounds.extend(newMark.getPosition())
        }) 
        console.log('startAdjust')
        vm.map.setCenter(bounds.getCenter())
        vm.map.panToBounds(bounds)
        vm.map.fitBounds(bounds)
        console.log('endAdjust')
        window.google.maps.event.clearListeners(vm.map, 'idle')
      }
      else 
        setTimeout(checkForMap, 200)
    }
    // checkForMap()
    this.map.addListener('click', (event) => {
      this.mapClick(event.latLng)
    })
    console.log('mounted')
    console.log(this.map.getZoom())
  }
}
</script>

<style scoped>
  .map {
    min-height: 400px;
    align-self: stretch;
  }
</style>