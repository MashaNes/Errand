export const fetchHandeledReports = () =>
{
    return {
        count: 4,
        results: [
            {
                id: 1,
                comment: "test comment 1",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 1,
                request:
                {
                    id: 1,
                    name:"Kupovina",
                    time: new Date(),
                    status: 3,
                    destination:
                    {
                        arrived: false,
                        home: false,
                        id: 2,
                        latitude: 43.3203158,
                        longitude: 21.9170784,
                        name: "bulevar nemanjica nis"
                    },
                    pictures: ["slika1", "slika2", "slika3"],
                    created_by: {
                        id: 3,
                        first_name: "Dimitrije",
                        last_name: "Pantic",
                        picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
                        email: "dime.pantela@gmail.com",
                        avg_rating: 3.45,
                        phone: "220098123",
                    },
                    working_with: {
                        id: 2,
                        first_name: "Jovan",
                        last_name: "Dimitrijevic",
                        picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
                        email: "jovan.dimitrijevic@gmail.com",
                        avg_rating: 3.45,
                        phone: "220098123",
                    },
                    tasklist: [
                        {
                            id: 1,
                            address: {
                                "id": 1,
                                "name": "Sokobanja, Zmaj Jovina 3",
                                "latitude": 43.642689,
                                "longitude": 21.862980,
                                "home": false,
                                "arrived": false
                            },
                            service_type: {
                                "id": 2,
                                "service_type_sr": "Nabavka",
                                "service_type_en": "Grocery shopping",
                                "description_sr": "Nabavka namirnica u supermarketu",
                                "description_en": "Shop for groceries in a nearby supermarket",
                                "picture_required": true
                            },
                            checklist: [
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
                            name: "task1",
                            description: "Nabavi mi sve ovo i slikaj racun.",
                            picture_required: true,
                            pictures: []
                        },
                        {
                            id: 2,
                            address: null,
                            service_type: {
                                "id": 1,
                                "service_type_sr": "Ostalo",
                                "service_type_en": "Other",
                                "description_sr": "Nedefinisane usluge",
                                "description_en": "Undefined services",
                                "picture_required": false
                            },
                            checklist: [],
                            name: "task2",
                            description: "Ovde nesto pise.",
                            picture_required: false,
                            pictures: ["slika1", "slika2", "slika3"]
                        }
                    ],
                    note: "Budi brz"
                },
                pictures:[]
            },
            {
                id: 2,
                comment: "test comment 2",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 2,
                request:
                {
                    id: 2,
                    name:"Ciscenje snega",
                    time: new Date(),
                    status: 2,
                    working_with: {
                        id: 3,
                        first_name: "Dimitrije",
                        last_name: "Pantic",
                        picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
                        email: "dime.pantela@gmail.com",
                        avg_rating: 3.45,
                        phone: "220098123",
                    },
                    created_by: {
                        id: 2,
                        first_name: "Jovan",
                        last_name: "Dimitrijevic",
                        picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
                        email: "jovan.dimitrijevic@gmail.com",
                        avg_rating: 3.45,
                        phone: "220098123",
                    },
                    tasklist: [
                        {
                            id: 3,
                            address: {
                                "id": 2,
                                "name": "Neki grad, neka ulica",
                                "latitude": 44.642689,
                                "longitude": 22.862980,
                                "home": false,
                                "arrived": false
                            },
                            service_type: {
                                "id": 2,
                                "service_type_sr": "Nabavka",
                                "service_type_en": "Grocery shopping",
                                "description_sr": "Nabavka namirnica u supermarketu",
                                "description_en": "Shop for groceries in a nearby supermarket",
                                "picture_required": true
                            },
                            checklist: [
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
                            name: "Nabavka u Maxi-ju",
                            description: "Nabavi mi sve ove stvari u Maxi-ju sa adrese",
                            picture_required: true,
                            pictures: ["slika1", "slika2", "slika3"]
                        },
                        {
                            id: 4,
                            address: {
                                "id": 3,
                                "name": "Neka druga adresa, neki broj",
                                "latitude": 44.621289,
                                "longitude": 22.333980,
                                "home": false,
                                "arrived": false
                            },
                            service_type: {
                                "id": 2,
                                "service_type_sr": "Nabavka",
                                "service_type_en": "Grocery shopping",
                                "description_sr": "Nabavka namirnica u supermarketu",
                                "description_en": "Shop for groceries in a nearby supermarket",
                                "picture_required": true
                            },
                            checklist: [
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
                            name: "Nabavka u lokalnom dragstoru",
                            description: "Nabavi mi ove stvari u dragstoru sa adrese",
                            picture_required: true,
                            pictures: []
                        },
                        {
                            id: 5,
                            address: {
                                "id": 4,
                                "name": "Neka druga adresa, neki broj",
                                "latitude": 44.521289,
                                "longitude": 22.353100,
                                "home": true,
                                "arrived": false
                            },
                            service_type: {
                                id: 3,
                                service_type_sr: "Dostava",
                                service_type_en: "Delivery",
                                description_sr: "Dostava na kućnu adresu",
                                description_en: "Delivery at home address",
                                picture_required: false
                            },
                            checklist: [],
                            name: "Dostava kupljenih stvari na kućnu adresu",
                            description: "Dostavi mi sve ove stvari na kućnu adresu koja je navedena ovde",
                            picture_required: true,
                            pictures: ["slika1", "slika2", "slika3"]
                        },
                        {
                            id: 6,
                            address: null,
                            service_type: {
                                id: 1,
                                service_type_sr: "Ostalo",
                                service_type_en: "Other",
                                description_sr: "Nedefinisane usluge",
                                description_en: "Undefined services",
                                picture_required: false
                            },
                            checklist: [],
                            name: "task2",
                            description: "Ovde nesto pise.",
                            picture_required: false,
                            pictures: []
                        }
                    ],
                    note: "Slikaj račune za obe kupovine!"
                },
                pictures:["slika1", "slika2"]
            },
            {
                id: 3,
                comment: "test comment 3",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 3,
                request: null,
                pictures:["slika1", "slika2", "slika3", "slika4", "slika5", "slika6"]
            },
            {
                id: 4,
                comment: "test comment 4",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 4,
                request: null,
                pictures:[]
            }
        ]

    }
}