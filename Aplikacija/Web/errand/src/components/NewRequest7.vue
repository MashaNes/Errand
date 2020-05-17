<template>
    <div class="wrapper-step7">
        <div class="labelDiv">
            <span v-if="isSerbian"> Naziv zahteva: <span class="userData"> {{request.name}} </span></span>
            <span v-else> Request name: <span class="userData">{{request.name}}</span></span>
        </div>
        <div class="labelDiv">
            <span v-if="isSerbian"> Datum i vreme: <span class="userData">{{request.time}}</span></span>
            <span v-else> Date and time: <span class="userData">{{request.time}}</span></span>
        </div>
        <div class="labelDiv" v-if="request.broadcast">
            <span v-if="isSerbian"> Kriterijumi prikaza zahteva izvršiocima: <span class="userData">Minimalni rejting - {{request.min_rating}}/5, Maksimalno rastojanje - {{request.max_dist}}km</span></span>
            <span v-else> Criteria for showing the request to errand runners: <span class="userData">Minimal rating - {{request.min_rating}}/5, Maximum distance - {{request.max_dist}}km</span></span>
        </div>
        <div class="labelDiv" v-if="request.direct_user != null">
            <span v-if="isSerbian"> Zahtev će biti direktno poslat korisniku: <span class="userData"> {{request.direct_user.first_name}} {{request.direct_user.last_name}}</span></span>
            <span v-else> The request is going to be sent directly to a user: <span class="userData"> {{request.direct_user.first_name}} {{request.direct_user.last_name}}</span></span>
        </div>
        <div class="labelDiv">
            <span v-if="isSerbian"> Zadaci: </span>
            <span v-else> Tasks: </span>
        </div>
        <table class="table">
            <thead>
                <tr>
                    <th  class="poravnanje" scope="col"><span v-if="isSerbian">Broj</span> <span v-else>Number</span></th>
                    <th  class="poravnanje" scope="col"><span v-if="isSerbian">Naziv</span> <span v-else>Name</span></th>
                    <th  class="poravnanje" scope="col"><span v-if="isSerbian">Tip usluge</span> <span v-else>Service type</span></th>
                    <th  class="poravnanje" scope="col"><span v-if="isSerbian">Slika</span> <span v-else>Picture</span></th>
                    <th  class="poravnanje" scope="col"><span v-if="isSerbian">Spisak</span> <span v-else>Checklist</span></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="task in request.tasklist" :key="task.id">
                    <td class="poravnanje">{{task.id}}</td>
                    <td class="poravnanje">{{task.name}}</td>
                    <td class="poravnanje">
                        <span v-if="isSerbian" v-b-popover.hover.bottom="task.service_type.description_sr">{{task.service_type.service_type_sr}}</span> 
                        <span v-else v-b-popover.hover.bottom="task.service_type.description_en">{{task.service_type.service_type_en}}</span>
                    </td>
                    <td class="poravnanje">
                        <img src="../assets/finished.svg" v-if="task.picture_required"/>
                        <img src="../assets/failed.svg" v-else/>
                    </td>
                    <td class="poravnanje">
                        <img src="../assets/checklist.svg" class="spisak" v-if="task.checklist.length > 0" v-b-popover.hover.bottom="lista" @mouseover="popuniListu(task)"/>
                        <img src="../assets/failed.svg" v-else/>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="mapDiv">
            <!-- map goes here.
            Trebalo bi da sadrzi po pin za adrese iz svakog taska i da odgovarajuci pin nosi redni broj odgovarajuceg taska u tabeli (redni broj u tabeli odgovara redosledu u kome se nalaze u taklistu)
            I pin za destination adresu iz zahteva koji bi trebalo da nosi oznaku F (finalna, final).
            (Moozda, tj. ako to moze to da se izvede, da se na hover ili klik na pin prikaze popup/popover sa tekstulanim ispisom selektovane adrese) -->
            <Map />
        </div>
    </div>
</template>

<script>
import Map from "@/components/Map"

export default {
    components:
    {
        Map
    },
    props:
    {
        request:
        {
            type: Object,
            required: true
        }
    },
    data()
    {
        return{
            lista: ""
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
        popuniListu(task)
        {
            var lista = ""
            this.request.tasklist.forEach(element =>
            {
                if(element.id == task.id)
                {
                    element.checklist.forEach((elementListe,index) =>
                    {
                        lista = lista + elementListe.item
                        if(index < element.checklist.length - 1)
                            lista = lista  + ", "
                        this.lista = lista
                    })
                }
            })
        }
    },
    created()
    {
        this.request.tasklist.forEach((element, index) =>
        {
            element.id = index + 1
        })

        const markerPositions = [];
        this.request.tasklist.forEach((task, ind) => {
          if(task.address)
          {
            const newPosition = {
              pos: {
                lat: task.address.latitude,
                lng: task.address.longitude
              },
              lab: String(ind + 1),
              info: task.address.name
            }
            markerPositions.push(newPosition)
          }
        })
        if(this.request.address.name != "" && this.request.address.latitude && this.request.address.longitude)
        {
            const newPosition = {
                pos: {
                    lat: this.request.address.latitude,
                    lng: this.request.address.longitude
                },
                lab: "F",
                info: this.request.address.name
            }
            markerPositions.push(newPosition)
        }
        this.$store.dispatch('setMarkerPositions', markerPositions)
    }
}
</script>

<style scoped>
    .wrapper-step7
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .labelDiv
    {
        width:100%;
        padding:7px;
        padding-left:10px;
        display:flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items:center;
        font-size: 18px;
        font-weight: 600;
    }

    .userData
    {
        color:rgb(101, 136, 243);
    }

    .poravnanje
    {
        text-align: center;
    }

    .spisak
    {
        width:40px;
        height:40px;
    }

    .mapDiv
    {
        width: 100%;
    }
</style>