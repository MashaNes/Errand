export const fetchUsers = () => {
    return {
        "1": {
            id: 1,
            firstName: "Harry",
            lastName: "Potter",
            //userName: "H.Potter",
            picture: "https://avatarfiles.alphacoders.com/121/121391.jpg",
            email: "harry.potter19961112345542@gmail.com",
            rating: 3.45,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            minRating: 4.25,
            maxDist: 10,
            benefitDiscount: 10,
            benefitRequirement: 5,
            achievements: {
                "1": {
                    id: 1,
                    userId: "1",
                    level: 2, 
                    achievement: {
                        id: 1,
                        name: {
                            english: "Busy bee",
                            serbian: "Vredna pčelica"
                        },
                        description: {
                            english: "Completed more than 15 requests in a week",
                            serbian: "Preko 15 ispunjenih zahteva u jednoj nedelji"
                        },
                        icon: require("../assets/bee.svg")
                    }
                },
                "3" : {
                    id: 3,
                    userId:"1",
                    level: 3, 
                    achievement: {
                        id: 3,
                        name: {
                            english: "Speedy Gonzales",
                            serbian: "Brzi Gonzales"
                        },
                        description: {
                            english: "Completed more than 3 requests in a day",
                            serbian: "Više od 3 ispunjena zahteva u jednom danu"
                        },
                        icon: require("../assets/speed.svg")
                    }
                },
                "4" : {
                    id: 4,
                    userId: "1",
                    level: 4,
                    achievement: {
                        id: 4,
                        name: {
                            english: "Shoplifter",
                            serbian: "Kradljivac"
                        },
                        description: {
                            english: "Completed more than 30 shopping requests",
                            serbian: "Više od 30 obavljenih kupovina"
                        },
                        icon: require("../assets/robber.svg")
                    }
                },
                "5" : {
                    id: 5,
                    userId: "1",
                    level: 1,
                    achievement: {
                        id: 5,
                        name: {
                            english: "Housefly",
                            serbian: "Kućna muva"
                        },
                        description: {
                            english: "Completed more than 15 indoors requests",
                            serbian: "Više od 15 obavljenih kućnih poslova"
                        },
                        icon: require("../assets/fly.svg")
                    }
                }
            },
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: {
                "1": {
                    id: 1,
                    grade: 3,
                    comment: "Brzo obavljena kupovina, ali na pogrešnom mestu.",
                    createdBy: 2,
                    request: 7
                },
                "2": {
                    id: 2,
                    grade: 5,
                    comment: "Odlično obavljen posao. Bez zamerki!",
                    createdBy: 2,
                    request: 3
                },
                "3": {
                    id:3,
                    grade: 1,
                    comment: "Užasna usluga! Nikako ne bih ponovio saradnju!",
                    createdBy: 2,
                    request: 7
                }
            }
        },
        "2": {
            id: 2,
            firstName: "Milorad",
            lastName: "Veljković",
            //userName: "H.Potter",
            picture: "https://pickaface.net/gallery/avatar/lucywild215799dbd11cec2.png",
            email: "milorad.v@outlook.com",
            rating: 4.52,
            phone: ["0631415522", "11112222", "33334444"],
            status: "active",
            achievements: {
                "2" : {
                    id: 2,
                    user: "2",
                    level: 2,
                    achievement: {
                        id: 2,
                        name: {
                            english: "Nerd",
                            serbian: "Štreber"
                        },
                        description: {
                            english: "Got the highest possible rating more than 10 times",
                            serbian: "Ocenjen najvišom ocenom više od 10 puta"
                        },
                        icon: require("../assets/speed.svg")
                    }
                },
                "7" : {
                    id: 7,
                    user: "2",
                    level: 7,
                    achievement: {
                        id: 3,
                        name: {
                            english: "Speedy Gonzales",
                            serbian: "Brzi Gonzales"
                        },
                        description: {
                            english: "Completed more than 3 requests in a day",
                            serbian: "Više od 3 ispunjena zahteva u jednom danu"
                        },
                        picture: require("../assets/speed.svg")
                    }
                },
                "8" : {
                    id: 8,
                    user: "2",
                    level: 2,
                    achievement: {
                        id: 5,
                        name: {
                            english: "Housefly",
                            serbian: "Kućna muva"
                        },
                        description: {
                            english: "Completed more than 15 indoors requests",
                            serbian: "Više od 15 obavljenih kućnih poslova"
                        },
                        picture: require("../assets/fly.svg")
                    }
                }
            },
            homeAddress: ["Nemanjina 12, Sokobanja", "Bulevar Nemanjića 15, Niš"],
            ratings: {}
        },
        "3": {
            id: 3,
            firstName: "Dimitrije",
            lastName: "Pantic",
            picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
            email: "dime.pantela@gmail.com",
            rating: 2.67,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            achievements: {},
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: {}
        },
        "4": {
            id: 4,
            firstName: "Srecko",
            lastName: "Sojic",
            picture: "https://www.srbijadanas.com/sites/default/files/styles/full_article_image/public/a/t/2015/09/09/srecko-sojic-foto-priscreen-ktvtelevizija-zrenjanin.jpg",
            email: "iznenadjen.i.uvredjen@gmail.com",
            rating: 3.24,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            achievements: {},
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: {}
        },
        "5": {
            id: 5,
            firstName: "Obi Wan",
            lastName: "Kenobi",
            picture: "https://www.denofgeek.com/wp-content/uploads/2019/08/star-wars-obi-wan-kenobi-1-scaled.jpg?fit=2560%2C1707",
            email: "HelloThere@galaxy.com",
            rating: 1.5,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            achievements: {},
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: {}
        },
        "6": {
            id: 6,
            firstName: "Chewbacca",
            lastName: "Chewie",
            picture: "https://pbs.twimg.com/profile_images/942329591426842624/cGFGEd2z.jpg",
            email: "Hrarrararaaaa@galaxy.com",
            rating: 4.76,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            achievements: {},
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: {}
        },
        "7": {
            id: 7,
            firstName: "Chandler",
            lastName: "Bing",
            picture: "https://i.pinimg.com/originals/a4/a4/7d/a4a47d837726daa86ece52c8dc5b812a.jpg",
            email: "CouldIBeAnyMoreSarcastic@centralPerk.com",
            rating: 3.45,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            achievements: {},
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: {}
        },
        "8":{
            id:8,
            firstName: "Derek",
            lastName: "Shepherd",
            picture: "https://alchetron.com/cdn/derek-shepherd-8b30aed5-5e2e-4a12-b8e2-8c0c90a0736-resize-750.jpeg",
            email: "McDreamy@seattlegrace.com",
            rating: 4.28,
            phone: ["123456", "220098123", "13455562"],
            status: "active",
            achievements: ["1", "3", "4", "5", "6"],
            homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
            ratings: ["1", "2", "3"]
        }
    }
}