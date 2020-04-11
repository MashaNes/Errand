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
        }
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
    }
}