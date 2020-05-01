export const fetchAchievements = () => {
    return {
        "1": {
            id: 1,
            user: 1,
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
        "2" : {
            id: 2,
            user: 2,
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
        "3" : {
            id: 3,
            user:1,
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
            user: 1,
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
            user: 1,
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
        },
        "6" : {
            id: 6,
            user: 1,
            level: 3,
            achievement: {
                id: 6,
                name: {
                    english: "Newbie",
                    serbian: "Novajlija"
                },
                description: {
                    english: "Completed the first ever request",
                    serbian: "Ispunjen prvi zahtev"
                },
                icon: require("../assets/baby.svg")
            }
        },
        "7" : {
            id: 7,
            user: 2,
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
                icon: require("../assets/speed.svg")
            }
        },
        "8" : {
            id: 8,
            user: 2,
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
                icon: require("../assets/fly.svg")
            }
        },
    }
}

export const fetchAchievementDetails = () => {
    return {
        "1": {
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
        },
        "2": {
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
        },
        "3": {
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
        },
        "4": {
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
        },
        "5": {
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
        },
        "6": {
            id: 6,
            name: {
                english: "Newbie",
                serbian: "Novajlija"
            },
            description: {
                english: "Completed the first ever request",
                serbian: "Ispunjen prvi zahtev"
            },
            icon: require("../assets/baby.svg")
        }
    }
}