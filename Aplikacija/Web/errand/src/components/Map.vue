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
      if(newVal[0].showEditsOnMap) {
        let bounds = new window.google.maps.LatLngBounds(null)
        this.markers.forEach(mark => {
          mark.setMap(null)
          mark = null
        })

        newVal.forEach(mark => {
          const newMark = new window.google.maps.Marker({
            position: mark.pos,
            label: {
              text: (mark.lab != "") ? mark.lab : " ",
              fontWeight: "600",
              fontSize: "16px"
            },
            icon: {
              url: mark.icon ? mark.icon : "https://www.iconsdb.com/icons/preview/color/FF3112/map-marker-2-xxl.png",
              scaledSize: new window.google.maps.Size(40, 40)
            },
            map: this.map
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

          this.markers.push(newMark)
          bounds.extend(newMark.getPosition())
        })
        this.map.setCenter(bounds.getCenter())
        this.map.panToBounds(bounds)
        this.map.fitBounds(bounds)
        if(this.map.getZoom() > 17)
          this.map.setZoom(17)

        return
      }
      if(oldVal.length == newVal.length) {
        this.markers[0].setMap(null)
        this.markers[0] = null
        this.markers[0] = new window.google.maps.Marker({
          position: newVal[0].pos,
          label: {
            text: (newVal[0].lab != "") ? newVal[0].lab : " ",
            fontWeight: "600",
            fontSize: "16px"
          },
          icon: {
            url: newVal[0].icon ? newVal[0].icon : "https://www.iconsdb.com/icons/preview/color/FF3112/map-marker-2-xxl.png",
            scaledSize: new window.google.maps.Size(40, 40)
          },
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
          position: newVal[newVal.length - 1].pos,
          label: {
            text: (newVal[newVal.length - 1].lab != "") ? newVal[newVal.length - 1].lab : " ",
            fontWeight: "600",
            fontSize: "16px"
          },
          icon: {
            url: newVal[newVal.length - 1].icon ? newVal[newVal.length - 1].icon : "https://www.iconsdb.com/icons/preview/color/FF3112/map-marker-2-xxl.png",
            scaledSize: new window.google.maps.Size(40, 40)
          },
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
        this.map.setCenter(newVal[newVal.length - 1].pos)
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
        if(vm.mapMarkerPositions.length == 0) {
          vm.map.setCenter({
            lat:43.639696,
            lng: 21.878703
          })
          vm.map.setZoom(17)
        }
        else {
          vm.mapMarkerPositions.forEach(mark => {
            const newMark = new window.google.maps.Marker({
              position: mark.pos,
              label: {
                text: (mark.lab != "") ? mark.lab : " ",
                fontWeight: "600",
                fontSize: "16px"
              },
              icon: {
                url: mark.icon ? mark.icon : "https://www.iconsdb.com/icons/preview/color/FF3112/map-marker-2-xxl.png",
                scaledSize: new window.google.maps.Size(40, 40)
              },
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
      }
      else 
        setTimeout(checkForMap, 200)
    }
    // checkForMap()
    this.map.addListener('click', (event) => {
      console.log(event.latLng.lat() + " " + event.latLng.lng())
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