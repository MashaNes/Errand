<template>
    <div class="component-wrapper">
        <div v-if="step == 1" class="korak">
            <div class="one-element">
                <div class="labela">
                    <span v-if="isSerbian"> Ikonica: </span>
                    <span v-else> Icon: </span>
                </div>
                <div class="unos-ikonica">
                    <span class="media-center" v-if="achievement.icon != null">
                        <p class="image is-128x128">
                            <img class="rounded-image" :src="achievement.icon">
                            <img :src="require('@/assets/remove.svg')" class="remove-pic" @click="removePicture">
                        </p>
                    </span>
                    <div 
                        :class="['ikonica-msg', isDragged ? 'ikonica-msg-dragged' : '']"
                        @drop.prevent="onDrop"
                        @dragenter.prevent="onDragEnter"
                        @dragleave.prevent="onDragLeave" 
                        @dragover.prevent
                        @mouseenter.prevent="onDragEnter"
                        @mouseleave.prevent="onDragLeave"
                        @click="$refs.file.click()"
                        v-if="achievement.icon == null"
                    >
                        <i v-if="isSerbian" class="natpis"> Klinite da biste dodali ikonicu ili je prevucite ovde </i>
                        <i v-if="!isSerbian" class="natpis"> Click to add an icon or drag and drop it here </i>
                    </div>

                    
                    <input type="file" ref="file" style="display: none" accept="image/*" @change="pictureSelected"/>
                </div>
            </div>
            <div class="one-element">
                <div class="labela">
                    Ime:
                </div>
                <input type="text" v-model="achievement.name_sr" class="unos"/>
            </div>
            <div class="one-element">
                <div class="labela">
                    Name:
                </div>
                <input type="text" v-model="achievement.name_en" class="unos"/>
            </div>
            <div class="one-element">
                <div class="labela">
                    Opis:
                </div>
                <textarea v-model="achievement.description_sr" class="unos"/>
            </div>
            <div class="one-element">
                <div class="labela">
                    Description:
                </div>
                <textarea v-model="achievement.description_en" class="unos"/>
            </div>
            <div class="one-element">
                <div class="labela">
                    <span v-if="isSerbian"> Broj nivoa: </span>
                    <span v-else> Number of levels: </span>
                </div>
                <input type="number" v-model="achievement.levels" class="unos" min="1" max="10"/>
            </div>
            <div class="dugme-div">
                <button type="button" class="btn btn-info" @click="showModal = true"
                        :disabled="achievement.name_sr == '' || achievement.name_en == '' || achievement.description_sr == '' || achievement.description_en == ''
                        || achievement.levels < 1 || achievement.levels > 10 || parseInt(achievement.levels) == NaN || achievement.icon == null">
                    <span v-if="isSerbian"> Dalje </span>
                    <span v-else> Next </span>
                </button>
            </div>
        </div>
        <div v-else class="korak2">
            <v-select :options="uslovi" label="name_sr" v-model="myCondition" class="selekt" v-if="isSerbian" :clearable="false"></v-select> 
            <v-select :options="uslovi" label="name_en" v-model="myCondition" class="selekt" v-else :clearable="false"></v-select>
            <div class="input-fields" v-if="myCondition != null">
                <div class="input-field" v-for="(element,index) in conditionArray" :key="index">
                    <span v-if="isSerbian" class="nivo"> Nivo </span>
                    <span v-else class="nivo"> Level </span>
                    {{index + 1}}:
                    <input type="number" v-model="conditionArray[index]" min="1" class="vrednost"/>
                </div>
            </div>
            <button type="button" class="btn btn-success dugme" @click="dodaj"
                    :disabled="myCondition == null || arrayInvalid">
                <span v-if="isSerbian"> Dodaj uslov</span>
                <span v-else> Add condition</span>
            </button>
            <table class="table tabela">
                <thead>
                    <tr>
                        <th  class="poravnanje" scope="col"><span v-if="isSerbian">Nivo/Uslovi</span> <span v-else>Level</span></th>
                        <th  class="poravnanje" scope="col" v-for="(condition,index) in achievement.conditions" :key="condition">
                            <span v-b-popover.hover.top="achievementConditions[index]"> {{index + 1}} </span>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(nothing,index) in achievement.condition_numbers[0]" :key="index">
                        <td class="poravnanje">{{index + 1}}</td>
                        <td class="poravnanje" v-for="(nada,number) in achievement.condition_numbers" :key="number"> {{achievement.condition_numbers[number][index]}} </td>
                    </tr>
                </tbody>
            </table>
            <button type="button" class="btn btn-success dugme" @click="showModal2 = true"
                    :disabled="achievement.conditions.length == 0">
                <span v-if="isSerbian"> Kreiraj dostignuće </span>
                <span v-else> Create the achievement </span>
            </button>
        </div>
        <ModalAreYouSure v-if="showModal==true"
                         :naslovS="'Proverite unete podatke'"
                         :naslovE="'Check the entered data'"
                         :tekstS="'Da li ste sigurni da želite da nastavite? Nećete moći da se vratite nazad.'"
                         :tekstE="'Are you sure you want to continue? You will not be able to go back'"
                         @yes="nextStep"
                         @close="showModal = false"/>
        <ModalAreYouSure v-if="showModal2==true"
                         :naslovS="'Kreiranje novog dostignuća'"
                         :naslovE="'Creating a new achievement'"
                         :tekstS="'Da li ste sigurni da želite da nastavite? Nakon kreiranja dostignuće će biti dostupno svim korisnicima.'"
                         :tekstE="'Are you sure you want to continue? After creation the achievement will be visible to all of the users.'"
                         @yes="createAchievement"
                         @close="showModal2 = false"/>
    </div>
</template>

<script>
import ModalAreYouSure from "@/components/ModalAreYouSure"
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
export default {
    data()
    {
        return{
            achievement:
            {
                name_sr:"",
                name_en: "",
                description_sr: "",
                description_en: "",
                icon: null,
                levels: 1,
                conditions: [],
                condition_numbers:[]
            },
            step: 1,
            showModal: false,
            showModal2: false,
            myCondition: null,
            conditionArray: null,
            uslovi:
            [
                {
                    name_sr: "Broj kreiranih zahteva",
                    name_en: "Number of created requests",
                    code: 1
                },
                {
                    name_sr: "Broj uspešno završenih zahteva",
                    name_en: "Number of successfully finished requests",
                    code: 2
                },
                {
                    name_sr: "Granica za prosečnu ocenu",
                    name_en: "Average ratnig limit",
                    code: 3
                },
                {
                    name_sr: "Broj uspešno završenih zahteva kod kojih je korisnik bio izvršilac",
                    name_en: "Number of successfully finished requests where the user was the runner",
                    code: 4
                },
                {
                    name_sr: "Broj uspešno završenih zahteva kod kojih je korisnik bio zahtevalac",
                    name_en: "Number of successfully finished requests where the user was the one to create the request",
                    code: 5
                },
                {
                    name_sr: "Broj korisnika koje ima u listi povlašćenih",
                    name_en: "Number of users in the benefit list",
                    code: 6
                },
                {
                    name_sr: "Broj neuspešno završenih zahteva",
                    name_en: "Number of failed requests",
                    code: 7
                },
                {
                    name_sr: "Broj različitih usluga koje je pružio nekome",
                    name_en: "Number of different serveces the user has given",
                    code: 8
                },
                {
                    name_sr: "Broj različitih usluga koje je zahtevao",
                    name_en: "Number of different serveces the user has requested",
                    code: 9
                }
            ],
            isDragged: false,
            dragCounter: 0
        }
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        arrayInvalid()
        {
            var flag = false
            this.conditionArray.forEach((element,index) =>
            {
                if(this.conditionArray[index] == undefined || element < 1 || isNaN(parseInt(element)))
                    flag = true
                else if(index > 0 && (parseInt(element)) < parseInt(this.conditionArray[index-1]))
                    flag = true
            })
            return flag
        },
        achievementConditions()
        {
            var uslovi = []
            this.achievement.conditions.forEach(element =>
            {
                var rez = ""
                switch(element)
                {
                    case 1:
                        rez = (this.isSerbian? "Broj kreiranih zahteva" : "Number of created requests")
                        break;
                    case 2:
                        rez = (this.isSerbian? "Broj uspešno završenih zahteva" : "Number of successfully finished requests")
                        break;
                    case 3:
                        rez = (this.isSerbian? "Granica za prosečnu ocenu" : "Average ratnig limit")
                        break;
                    case 4:
                        rez = (this.isSerbian? "Broj uspešno završenih zahteva kod kojih je korisnik bio izvršilac" : "Number of successfully finished requests where the user was the runner")
                        break;
                    case 5:
                        rez = (this.isSerbian? "Broj uspešno završenih zahteva kod kojih je korisnik bio zahtevalac" : "Number of successfully finished requests where the user was the one to create the request")
                        break;
                    case 6:
                        rez = (this.isSerbian? "Broj korisnika koje ima u listi povlašćenih" : "Number of users in the benefit list")
                        break;
                    case 7:
                        rez = (this.isSerbian? "Broj neuspešno završenih zahteva" : "Number of failed requests")
                        break;
                    case 8:
                        rez = (this.isSerbian? "Broj različitih usluga koje je pružio nekome" : "Number of different serveces the user has given")
                        break;
                    default:
                        rez = (this.isSerbian? "Broj različitih usluga koje je zahtevao" : "Number of different serveces the user has requested")
                        break;
                }
                uslovi.push(rez)
            })
            return uslovi
        }
    },
    methods:
    {
        nextStep()
        {
            this.step = 2
            this.showModal = false,
            this.conditionArray = new Array(parseInt(this.achievement.levels))
            for(var i = 0; i < this.achievement.levels; i++)
            {
                this.conditionArray[i] = 1
            }
        },
        dodaj()
        {
            this.achievement.conditions.push(this.myCondition.code)
            this.achievement.condition_numbers.push(this.conditionArray)
            this.uslovi.forEach((element,index) =>
            {
                if(element.code == this.myCondition.code)
                    this.uslovi.splice(index,1)
            })
            this.myCondition = null
            this.conditionArray = new Array(parseInt(this.achievement.levels))
            for(var i = 0; i < this.achievement.levels; i++)
            {
                this.conditionArray[i] = 1
            }
        },
        createAchievement()
        {
            if(this.achievement.icon != null) 
            {
                const splitted = this.achievement.icon.split(',')
                this.achievement.icon = splitted[1]
            } 

            this.$store.state.achievements.push(this.achievement)
            this.$store.dispatch("achievementCreate", this.achievement)
            this.$emit("achievemntCreated")
        },
        pictureSelected(e) 
        {
            const file = e.target.files[0];
            e.target.value = null
            this.addImage(file);
        },
        onDrop(e) 
        {
            e.stopPropagation();
            const file = e.dataTransfer.files[0];
            this.addImage(file);
        },
        addImage(file) 
        {
            if(!file) 
            {
                this.isDragged = false;
                this.dragCounter = 0;
                return 
            }

            if(!file.type.match('image*')) 
            {
                console.log('not an image!');
            }
            else 
            {
                const reader = new FileReader();
                reader.onload = (e) => this.achievement.icon = e.target.result;
                reader.readAsDataURL(file);
            }
            this.isDragged = false;
            this.dragCounter = 0;
        },
        onDragEnter() 
        {
            this.dragCounter ++;
            this.isDragged = true;
        },
        onDragLeave() 
        {
            if(this.dragCounter>0)
                this.dragCounter --;
            if(!this.dragCounter)
                this.isDragged = false;
        },
        removePicture() 
        {
            this.achievement.icon = null
        }
    },
    components:
    {
        ModalAreYouSure,
        vSelect
    }
}
</script>

<style scoped>
    .component-wrapper
    {
        width:100%;
        background-color:white;
        border-radius:20px;
        padding:20px;
        display:flex;
        flex-direction: column;
        align-items: flex-start;
    }

    .one-element
    {
        display: flex;
        flex-direction: row;
        width:100%;
        margin-bottom: 10px;
        align-items: center;
        justify-content: space-between;
    }

    .labela
    {
        margin-right:7px;
        font-size: 18px;
        font-weight: 600;
    }

    .unos
    {
        width:84%;
        padding-left:5px;
    }

    .unos-ikonica
    {
        height:100px;
        display:flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        width:84%;
        text-align: center;
        color: grey;
        border-radius:2px;
    }

    .ikonica-msg {
        height: 100%;
        display: flex;
        align-items: center;
        align-self: stretch;
        flex-grow: 1;
        border: 1px solid black;
        border-radius: 5px;
    }
    
    .ikonica-msg-dragged {
        cursor: pointer;
        background-color: rgb(235, 230, 230);
    }

    .is-128x128 {
        width: 80px;
        height: 80px;
        border-radius: 5px;
        margin: 0px 5px 5px 0px;
    }

    .rounded-image {
        border-radius: 5px;
        border: 1px solid black;
        height: 78px;
        width: 78px;
        object-fit:cover;
        position: relative;
        top:0;
        left:0;
        padding: 2px;
    }

    .remove-pic {
        height:20px;
        width: 20px;
        margin: 2px 0 0 2px;
        top: 0;
        left:0;
        position: absolute;
        border:1px solid transparent;
        border-radius: 20px;
        background-color: red;
        opacity: 0.4;
    }

    .remove-pic:hover {
        cursor: pointer;
        opacity: 1;
    }

    .natpis
    {
        width:100%;
    }

    .korak
    {
        width:100%;
    }

    .korak2
    {
        width:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .dugme-div
    {
        display: flex;
        flex-direction: row-reverse;
    }

    .selekt
    {
        width: 80%;
        margin-right: 7px;
        word-break:break-all;
        background-color: rgb(236, 236, 236);
    }

    .input-fields
    {
        display: flex;
        width:100%;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;
        margin-top:20px;
    }

    .input-field
    {
        display:flex;
        flex-direction: row;
        align-items: center;
        margin-right:10px;
        margin-bottom:10px;
    }

    .nivo
    {
        margin-right:4px;
    }

    .vrednost
    {
        margin-left:5px;
        width:70px;
    }

    .dugme
    {
        margin-top:20px;
    }

    .tabela
    {
        margin-top:20px;
        margin-right:20px;
        width:60%;
    }

    .poravnanje
    {
        text-align: center;
    }

    @media only screen and (max-width:550px)
    {
        .component-wrapper
        {
            padding:15px;
        }

         .one-element
        {
            flex-direction: column;
        }

        .labela
        {
            margin-bottom:7px;
        }

        .unos, .unos-ikonica
        {
            width:96%;
        }
    }
</style>