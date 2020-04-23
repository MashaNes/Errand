export const fetchAchievements = () => {
    return {
        "1": {
            id: 1,
            userId: "1",
            level: 2, 
            AchievementDetails: "1"
        },
        "2" : {
            id: 2,
            userId: "2",
            level: 2,
            AchievementDetails: "2"
        },
        "3" : {
            id: 3,
            userId:"1",
            level: 3, 
            AchievementDetails: "3"
        },
        "4" : {
            id: 4,
            userId: "1",
            level: 4,
            AchievementDetails: "4"
        },
        "5" : {
            id: 5,
            userId: "1",
            level: 1,
            AchievementDetails: "5"
        },
        "6" : {
            id: 6,
            userId: "1",
            level: 3,
            AchievementDetails: "6"
        },
        "7" : {
            id: 7,
            userId: "2",
            level: 7,
            AchievementDetails: "3"
        },
        "8" : {
            id: 8,
            userId: "2",
            level: 2,
            AchievementDetails: "5"
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
            picture: require("../assets/bee.svg")
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
            }
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
            picture: require("../assets/speed.svg")
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
            picture: require("../assets/robber.svg")
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
            picture: require("../assets/fly.svg")
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
            picture: require("../assets/baby.svg")
        }
    }
}