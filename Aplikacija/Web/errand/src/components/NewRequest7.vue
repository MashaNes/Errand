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
        <div v-if="request.note != ''" class="napomena">
            <i v-if="isSerbian"> Napomena: {{request.note}}</i>
            <i v-else> Note: {{request.note}}</i>
        </div>
        <div class="labelDiv">
            <span v-if="isSerbian"> Zadaci: </span>
            <span v-else> Tasks: </span>
        </div>
        <table class="table smanji">
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
                    <td class="poravnanje"> <span  v-b-popover.hover.bottom="task.description"> {{task.name}} </span> </td>
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
        <div class="row-divovi" v-if="request.destination.name != ''">
            <img src="../assets/finished.svg" v-if="request.picture_required"/>
            <img src="../assets/failed.svg" v-else/>
            <div class="labelDiv" v-if="request.picture_required">
                <span v-if="isSerbian"> Potrebno je dostaviti sliku sa finalne lokacije </span>
                <span v-else> A picture must to be taken at the final location </span>
            </div>
            <div class="labelDiv" v-else>
                <span v-if="isSerbian"> Nije potrebno dostaviti sliku sa finalne lokacije </span>
                <span v-else> Taking a picture at the final location is not required </span>
            </div>
        </div>
        <div class="mapDiv" v-if="imaAdresa">
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
            lista: "",
            imaAdresa: false
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
                        lista = lista + elementListe.check_list
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
            this.imaAdresa = true
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
        if(this.request.destination.name != "" && this.request.destination.latitude && this.request.destination.longitude)
        {
            this.imaAdresa = true
            const newPosition = {
                pos: {
                    lat: this.request.destination.latitude,
                    lng: this.request.destination.longitude
                },
                lab: "F",
                info: this.request.destination.name
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

    .row-divovi
    {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: flex-start;
        align-items: center;
        margin-bottom: 15px;
        word-break:break-all;
    }

    .napomena
    {
        font-size: 18px;
        color:rgb(206, 94, 94);
    }

    @media only screen and (max-width: 400px)
    {
        .smanji
        {
            font-size: 12px;
        }

        .labelDiv
        {
            font-size:16px; 
        }
    }
</style>