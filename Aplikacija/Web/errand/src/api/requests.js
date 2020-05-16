export const fetchRequests = () =>
{
    return {
        "1":{
            id: 1,
            name:"Kupovina",
            date: new Date(),
            tags:["kupovina", "racuni"],
            status:"running",
            runner: true,
            created_by: {
                id: 3,
                first_name: "Dimitrije",
                last_name: "Pantic",
                picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
                email: "dime.pantela@gmail.com",
                avg_rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            },
            working_with: null,
            tasklist: [
                {
                    "id": 1,
                    "address": {
                        "id": 1,
                        "name": "Sokobanja, Zmaj Jovina 3",
                        "latitude": 43.642689,
                        "longitude": 21.862980,
                        "home": false,
                        "arrived": false
                    },
                    "service_type": {
                        "id": 2,
                        "service_type_sr": "Nabavka",
                        "service_type_en": "Grocery shopping",
                        "description_sr": "Nabavka namirnica u supermarketu",
                        "description_en": "Shop for groceries in a nearby supermarket",
                        "picture_required": true
                    },
                    "checklist": [
                        {
                            "id": 1,
                            "check_list": "krompir 20kg"
                        },
                        {
                            "id": 2,
                            "check_list": "jaja 10 kom"
                        },
                        {
                            "id": 3,
                            "check_list": "mleko 1l"
                        }
                    ],
                    "name": "task1",
                    "description": "Nabavi mi sve ovo i slikaj racun.",
                    "picture_required": true,
                    "pictures": []
                },
                {
                    "id": 2,
                    "address": null,
                    "service_type": {
                        "id": 1,
                        "service_type_sr": "Ostalo",
                        "service_type_en": "Other",
                        "description_sr": "Nedefinisane usluge",
                        "description_en": "Undefined services",
                        "picture_required": false
                    },
                    "checklist": [],
                    "name": "task2",
                    "description": "Ovde nesto pise.",
                    "picture_required": false,
                    "pictures": []
                }
            ],
            "note": "Budi brz"
        },
        "2":{
            id: 2,
            name:"Ciscenje snega",
            date: new Date(),
            tags:["pomoc"],
            status:"pending",
            runner: false,
            working_with: null,
            created_by: null,
            tasklist: [
                {
                    "id": 3,
                    "address": {
                        "id": 2,
                        "name": "Neki grad, neka ulica",
                        "latitude": 44.642689,
                        "longitude": 22.862980,
                        "home": false,
                        "arrived": false
                    },
                    "service_type": {
                        "id": 2,
                        "service_type_sr": "Nabavka",
                        "service_type_en": "Grocery shopping",
                        "description_sr": "Nabavka namirnica u supermarketu",
                        "description_en": "Shop for groceries in a nearby supermarket",
                        "picture_required": true
                    },
                    "checklist": [
                        {
                            "id": 4,
                            "check_list": "Pirinač 500g"
                        },
                        {
                            "id": 5,
                            "check_list": "Dva hleba"
                        },
                        {
                            "id": 6,
                            "check_list": "Origano jedna kesica"
                        },
                        {
                            "id": 7,
                            "check_list": "Kvasac dve kocke"
                        }
                    ],
                    "name": "Nabavka u Maxi-ju",
                    "description": "Nabavi mi sve ove stvari u Maxi-ju sa adrese",
                    "picture_required": true,
                    "pictures": []
                },
                {
                    "id": 4,
                    "address": {
                        "id": 3,
                        "name": "Neka druga adresa, neki broj",
                        "latitude": 44.621289,
                        "longitude": 22.333980,
                        "home": false,
                        "arrived": false
                    },
                    "service_type": {
                        "id": 2,
                        "service_type_sr": "Nabavka",
                        "service_type_en": "Grocery shopping",
                        "description_sr": "Nabavka namirnica u supermarketu",
                        "description_en": "Shop for groceries in a nearby supermarket",
                        "picture_required": true
                    },
                    "checklist": [
                        {
                            "id": 8,
                            "check_list": "Toalet papir (celo pakovanje od 8 rolni)"
                        },
                        {
                            "id": 9,
                            "check_list": "Mer jedna boca"
                        },
                        {
                            "id": 10,
                            "check_list": "Sredstvno za brisanje podova (bilo koje, jedna boca)"
                        },
                    ],
                    "name": "Nabavka u lokalnom dragstoru",
                    "description": "Nabavi mi ove stvari u dragstoru sa adrese",
                    "picture_required": true,
                    "pictures": []
                },
                {
                    "id": 5,
                    "address": {
                        "id": 4,
                        "name": "Neka druga adresa, neki broj",
                        "latitude": 44.521289,
                        "longitude": 22.353100,
                        "home": true,
                        "arrived": false
                    },
                    "service_type": {
                        "id": 3,
                        "service_type_sr": "Dostava",
                        "service_type_en": "Delivery",
                        "description_sr": "Dostava na kućnu adresu",
                        "description_en": "Delivery at home address",
                        "picture_required": false
                    },
                    "checklist": [],
                    "name": "Dostava kupljenih stvari na kućnu adresu",
                    "description": "Dostavi mi sve ove stvari na kućnu adresu koja je navedena ovde",
                    "picture_required": true,
                    "pictures": []
                }
            ],
            "note": "Slikaj račune za obe kupovine!"
        },
        "3":{
            id: 3,
            name:"Ciscenje snega",
            date: new Date(),
            tags:["pomoc"],
            status:"finished",
            runner: true,
            created_by:{
                id: 4,
                first_name: "Srecko",
                last_name: "Sojic",
                picture: "https://www.srbijadanas.com/sites/default/files/styles/full_article_image/public/a/t/2015/09/09/srecko-sojic-foto-priscreen-ktvtelevizija-zrenjanin.jpg",
                email: "iznenadjen.i.uvredjen@gmail.com",
                avg_rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            },
            working_with: null
        },
        "4":{
            id: 4,
            name:"Cuvanje dece",
            date: new Date(),
            tags:["deca"],
            status:"failed",
            runner: false,
            working_with:{
                id: 8,
                first_name: "Derek",
                last_name: "Shepherd",
                picture: "https://alchetron.com/cdn/derek-shepherd-8b30aed5-5e2e-4a12-b8e2-8c0c90a0736-resize-750.jpeg",
                email: "McDreamy@seattlegrace.com",
                avg_rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            },
            created_by: null
        },
        "5":{
            id: 5,
            name:"Kupi mi cokoladu",
            date: new Date(),
            tags:["kupovina", "racuni"],
            status:"pending",
            runner: false,
            created_by: null,
            working_with: null
        },
        "6":{
            id: 6,
            name:"Odnesi tetki lek",
            date: new Date(),
            tags:["pomoc"],
            status:"running",
            runner: true,
            created_by:{
                id: 7,
                first_name: "Chandler",
                last_name: "Bing",
                picture: "https://i.pinimg.com/originals/a4/a4/7d/a4a47d837726daa86ece52c8dc5b812a.jpg",
                email: "CouldIBeAnyMoreSarcastic@centralPerk.com",
                avg_rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            },
            working_with: null
        },
        "7":{
            id: 7,
            name:"Ciscenje garaze",
            date: new Date(),
            tags:["pomoc"],
            status:"finished",
            runner: true,
            created_by:{
                id: 5,
                first_name: "Obi Wan",
                last_name: "Kenobi",
                picture: "https://www.denofgeek.com/wp-content/uploads/2019/08/star-wars-obi-wan-kenobi-1-scaled.jpg?fit=2560%2C1707",
                email: "HelloThere@galaxy.com",
                avg_rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            },
            working_with: null
        },
        "8":{
            id: 8,
            name:"Voznja na posao",
            date: new Date(),
            tags:["deca", "voznja"],
            status:"running",
            runner: false,
            working_with:{
                id: 6,
                first_name: "Chewbacca",
                last_name: "Chewie",
                picture: "https://pbs.twimg.com/profile_images/942329591426842624/cGFGEd2z.jpg",
                email: "Hrarrararaaaa@galaxy.com",
                avg_rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            },
            created_by: null
        }
      }
}