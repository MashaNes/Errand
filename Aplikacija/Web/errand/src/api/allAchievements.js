export const fetchAllAchievements = () => {
    return [
        {
            id: 1,
            name_sr:"Vredna pčelica",
            name_en: "Busy bee",
            description_sr: " uspešno ispunjenih zahteva kao izvršilac",
            description_en: " successfully completed requests as an errand runner",
            icon: require("../assets/bee.svg"),
            levels: 7,
            conditions: [4],
            condition_numbers:
            [
                [10,15,20,25,35,45,55]
            ]
        },
        {
            id: 2,
            name_sr:"Štreber",
            name_en: "Nerd",
            description_sr: " uspešno završenih zahteva i ocena preko ",
            description_en: " successfully completed requests and an average grade above ",
            icon: require("../assets/nerd.svg"),
            levels: 7,
            conditions: [2,3],
            condition_numbers:
            [
                [10,15,20,25,35,45,55],
                [4,4,4.5,4.5,4.7,4.8,4.9]
            ]
        },
        {
            id: 3,
            name_sr:"Filantrop",
            name_en: "Good guy",
            description_sr: " korisnika u listi povlašćenih uz broj uspešno završenih zahteva ",
            description_en: " users in the benefit list with a number of successfully done requests ",
            icon: require("../assets/good_guy.svg"),
            levels: 7,
            conditions: [6,4],
            condition_numbers:
            [
                [5,8,12,17,25,33,45],
                [10,15,20,25,35,45,55]
            ]
        },
        {
            id: 4,
            name_sr:"Savršene ocene",
            name_en: "Prefect record",
            description_sr: " zvezdica. Savršena prosečna ocena.",
            description_en: " stars ratings only",
            icon: require("../assets/perfect.svg"),
            levels: 1,
            conditions: [3],
            condition_numbers:
            [
                [5]
            ]
        },
        {
            id: 5,
            name_sr:"Novi zahtevaoc",
            name_en: "Newbie requester",
            description_sr: ". zahtev ikada kreiran",
            description_en: "st request created",
            levels: 1,
            conditions: [1],
            condition_numbers:
            [
                [1]
            ],
            icon: require("../assets/baby.svg")
        },
        {
            id: 6,
            name_sr:"Novajlija",
            name_en: "Newbie",
            description_sr: ". zahtev ikada uspešno završen",
            description_en: "st request ever successfully completed",
            levels: 1,
            conditions: [2],
            condition_numbers:
            [
                [1]
            ],
            icon: require("../assets/baby.svg")
        }
    ]  
}