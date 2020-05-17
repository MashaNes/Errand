<template>
    <Spinner v-if="services == null" />
    <div class="wrapper-newTask" v-else>
        <div class="wrapper-task">
            <div class="labelDiv">
                <span v-if="isSerbian"> <span class="zvezdica">*</span> Naziv zadatka: </span>
                <span v-else> <span class="zvezdica">*</span> Task name: </span>
            </div>
            <input type="text" v-model="myTask.name" class="name">
            <div class="labelDiv">
                <span v-if="isSerbian"> <span class="zvezdica">*</span> Tip usluge: </span>
                <span v-else> <span class="zvezdica">*</span> Service type: </span>
            </div>
            <div class="opcije"> 
                <v-select :options="services" label="service_type_sr" v-model="myTask.service_type" class="selekt" v-if="isSerbian" @input="serviceChanged"></v-select> 
                <v-select :options="services" label="service_type_en" v-model="myTask.service_type" class="selekt" v-else @input="serviceChanged"></v-select> 
                <img src="../assets/info.svg" v-if="myTask.service_type != null && isSerbian" v-b-popover.hover.bottom="myTask.service_type.description_sr">
                <img src="../assets/info.svg" v-if="myTask.service_type != null && !isSerbian" v-b-popover.hover.bottom="myTask.service_type.description_en">
            </div>
            <div class="labelDiv">
                <span v-if="isSerbian"> Opis zadatka: </span>
                <span v-else> Task description: </span>
            </div>
            <textarea type="text" v-model="myTask.description" class="name" />
            <div class="labelDiv">
                <span v-if="isSerbian"> Spisak: </span>
                <span v-else> Checklist: </span>
            </div>
            <div class="checklista">
                <div class="spisak">
                    <div class="element-spiska" v-for="element in myTask.checklist" :key="element.id">
                        <div class="string-element-spiska"> {{element.item}} </div>
                        <a @click="izbaciIzListe(element)"><img src="../assets/remove.svg" class="ikonica"></a>
                    </div>
                </div>
                <div class="dodavanje-u-spisak">
                    <input type="text" v-model="inputElement" class="input-polje" />
                    <button type="button" class="btn btn-primary dugme-checklist" @click="dodajUChecklist">
                        <span v-if="isSerbian">Dodaj u spisak</span>
                        <span v-else>Add to ckecklist</span>
                    </button>
                </div>
            </div>
            <div class="row-divovi">
                <input type="checkbox" class="picture-required" v-model="myTask.picture_required"/>
                <div class="labelDiv">
                    <span v-if="isSerbian"> Potrebno je dostaviti sliku </span>
                    <span v-else> Requires a picture </span>
                </div>
            </div>
            <div class="mapDiv">
                <AddAddressMap 
                    :HasCloseButton="false"
                    :AskAreYouSure="false"
                    :StartingAddress="myTask.address"
                    @close="addressChanged"
                />
                <!-- adresa je v-model="myTask.address" vidi oko pravljenja kopije jer treba da moze da se odbaci izmena pri edit-u -->
                <!-- u sustini treba samo da znaci ta myTask.address ne bude ista po referenci kao  task.address i da ona uvek sadrzi one azurne vrednosti-->
            </div>
            <div class="chosen-address-div" v-if="myTask.address">
                <span v-if="isSerbian"> Izabrana adresa: </span>
                <span v-else> Chosen address: </span>
                <span class="address-name">{{myTask.address.name}}</span>
                <img class="remove" src="@/assets/remove.svg" height="20" width="20" @click="removeAddress">
            </div>
            <div class="chosen-address-div" v-else>
                <span v-if="isSerbian"> Nije izabrana nijedna adresa </span>
                <span v-else> There is no chosen address </span>
            </div>
            <!-- ova dva div-a iznad ovog komentara slobodno menjaj što se stilova tiče;
            važno je da ostane ovaj @click i u <script> da ostane funkcija removeAddress;
            dodata je i funkcija addressChanged, i ona treba da ostane -->

        </div>
        <div class="button-div-newTask">
             <button type="button" class="btn btn-secondary levo-dugme" v-if="showCancelButton" @click="odustani">
                <span v-if="isSerbian">Odustani</span>
                <span v-else>Cancel</span>
            </button>
            <button type="button" class="btn btn-primary" 
                    :disabled="myTask.service_type == null || myTask.name==''"
                    @click="sacuvaj">
                <span v-if="isSerbian">Sačuvaj zadatak</span>
                <span v-else>Save task</span>
            </button>
        </div>
    </div>
</template>

<script>
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
import Spinner from "@/components/Spinner"
import AddAddressMap from "@/components/AddAddressMap"

export default {
    props:
    {
        showCancelButton:
        {
            type: Boolean,
            required:true
        },
        task:
        {
            type: Object,
            required:true
        }
    },
    components:
    {
        vSelect,
        Spinner,
        AddAddressMap
    },
    data()
    {
        return{
            myTask :
            {
                id: this.task.id,
                name: this.task.name,
                service_type: this.task.service_type,
                description: this.task.description,
                checklist: [],
                picture_required: this.task.picture_required,
                address: this.task.address ? {...this.task.address} : null
            },
            oldService: this.task.service_type,
            stringIdGenerator: -1,
            inputElement: ""
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        services()
        {
            return this.$store.state.allServices
        }
    },
    methods:
    {
        serviceChanged()
        {
            if(this.myTask.name == "" || 
                (this.oldService != null && 
                (this.isSerbian && this.oldService.service_type_sr == this.myTask.name) ||
                (!this.isSerbian && this.oldService.service_type_en == this.myTask.name)))
            {
                if(this.myTask.service_type == null)
                    this.myTask.name = ""
                else if(this.isSerbian)
                    this.myTask.name = this.myTask.service_type.service_type_sr
                else
                    this.myTask.name = this.myTask.service_type.service_type_en
            }
            this.oldService = this.myTask.service_type
        },
        dodajUChecklist()
        {
            if(this.inputElement != "")
            {
                var element = {id: this.stringIdGenerator, item : this.inputElement}
                this.myTask.checklist.push(element)
                this.stringIdGenerator = this.stringIdGenerator + 1
                this.inputElement = ""
            }
        },
        izbaciIzListe(element)
        {
            this.myTask.checklist.forEach((item,index) => {
                if(item.id == element.id)
                    this.myTask.checklist.splice(index,1)
            });
        },
        addressChanged(address) 
        {
            this.myTask.address = address
        },
        removeAddress()
        {
            this.myTask.address = null
        },
        sacuvaj()
        {
            this.$emit("sacuvaj",this.myTask)
        },
        odustani()
        {
            this.$emit("odustani")
        }
    },
    created()
    {
        if(this.$store.state.allServices == null)
            this.$store.dispatch("fillServices")
        this.task.checklist.forEach(element =>
        {
            this.myTask.checklist.push(element)
        })

        var maxId = 0
        this.stringIdGenerator = maxId + 1
        this.task.checklist.forEach((element, index) =>
        {
            if(element.id > maxId)
                maxId = element.id
            if(index == this.task.checklist.length - 1)
                this.stringIdGenerator = maxId + 1
        })
    }
}
</script>

<style scoped>
    .wrapper-newTask, .wrapper-task
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .button-div-newTask
    {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        padding: 5px;
        margin-top:10px;
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
    }

    .name
    {
        width:94%;
        margin-bottom: 10px;
    }

    .selekt
    {
        width: 80%;
        margin-right: 7px;
        background-color: white;
        word-break:break-all;
        background-color: rgb(236, 236, 236);
    }

    .opcije
    {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: center;
        align-items: center;
        margin-bottom: 15px;
        word-break:break-all;
    }

    .checklista
    {
        display: flex;
        flex-direction: row;
        width: 100%;
        justify-content: space-around;
        align-items: center;
        margin-bottom: 15px;
    }

    .dodavanje-u-spisak, .spisak
    {
        display: flex;
        flex-direction: column;
        width:45%;
    }

    .dodavanje-u-spisak
    {
        align-items: center;
    }

    .spisak
    {
        border: 1px solid black;
        min-height:150px;
        /*word-break:break-all;*/
    }

    .element-spiska
    {
        width:100%;
        border-bottom: 1px solid black;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: flex-end;
        padding:7px;
    }

    .ikonica
    {
        width:20px;
        height:20px;
    }

    .odlepi
    {
        margin-right: 7px;
    }

    .input-polje
    {
        width:90%;
        word-break:break-all;
    }

    .string-element-spiska
    {
        width:100%;
        text-align: left;
    }

    .dugme-checklist
    {
        margin-top:10px;
    }

    .zvezdica
    {
        color:red;
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

    .picture-required
    {
        margin-left: 15px;
        margin-right: 15px;
        height:20px;
        width:20px;
    }

    .mapDiv
    {
        width: 100%;
    }

    .levo-dugme
    {
        margin-right:30px;
    }

    .chosen-address-div
    {
        align-self: flex-start;
        display: flex;
        font-size: 16px;
        font-weight: 600;
        width: fit-content;
        border: 1px solid black;
        padding: 5px;
        border-radius: 5px;
    }

    .address-name
    {
        font-weight: normal;
        padding-left: 5px;
        margin-right: 15px;
    }

    .remove:hover 
    {
        width: 22px;
        height: 22px;
        cursor: pointer;
    }
</style>