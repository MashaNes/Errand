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
          position: newVal[0].pos,
          label: newVal[0].lab,
          map: this.map
        })
        if(newVal[0].info) {
          const info = new window.google.maps.InfoWindow({
            content: newVal[0].info,
            maxWidth: 200
          })
          this.markers[0].addListener('mouseover', function() {
            info.open(this.map, this.markers[0])
          })
        }
        this.map.setCenter(newVal[0].pos)
      }
      else if(oldVal.length < newVal.length) {
        const newMark = new window.google.maps.Marker({
          position: newVal[newVal.length - 1],
          label: newVal[newVal.length - 1],
          map: this.map
        })
        if(newVal[newVal.length - 1].info) {
          const info = new window.google.maps.InfoWindow({
            content: newVal[newVal.length - 1].info,
            maxWidth: 200
          })
          newMark.addListener('mouseover', function() {
            info.open(this.map, newMark)
          })
        }
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
    
    window.google.maps.event.addListenerOnce(this.map, 'idle', checkForMap)

    function checkForMap() {
      if(vm.map) {
        let bounds = new window.google.maps.LatLngBounds(null)
        vm.mapMarkerPositions.forEach(mark => {
          const newMark = new window.google.maps.Marker({
            position: mark.pos,
            label: mark.lab,
            map:vm.map
          })
          if(mark.info) {
            const info = new window.google.maps.InfoWindow({
            content: mark.info,
            maxWidth: 200
            })
            newMark.addListener('mouseover', function() {
              info.open(this.map, newMark)
            })
          }
          vm.markers.push(newMark)
          bounds.extend(newMark.getPosition())
        }) 
        vm.map.setCenter(bounds.getCenter())
        vm.map.panToBounds(bounds)
        vm.map.fitBounds(bounds)
        if(vm.map.getZoom() > 17)
          vm.map.setZoom(17)
      }
      else 
        setTimeout(checkForMap, 200)
    }
    // checkForMap()
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