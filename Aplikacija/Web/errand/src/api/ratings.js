export const fetchRatings = () => {
    return {
        "1": {
            id: 1,
            grade: 3,
            comment: "Brzo obavljena kupovina, ali na pogrešnom mestu.",
            createdBy: {
                id: 2,
                firstName: "Milorad",
                lastName: "Veljković",
                //userName: "H.Potter",
                picture: "https://pickaface.net/gallery/avatar/lucywild215799dbd11cec2.png",
                email: "milorad.v@outlook.com",
                rating: 4.52,
                phone: ["0631415522", "11112222", "33334444"],
                status: "active",
                achievements: {},
                homeAddress: ["Nemanjina 12, Sokobanja", "Bulevar Nemanjića 15, Niš"],
                ratings: {}
            },
            request: {
                id: 7,
                name:"Ciscenje garaze",
                date: new Date(),
                tags:["pomoc"],
                status:"finished",
                runner: true,
                user:{}
            },
            ratedUser: 1
        },
        "2": {
            id: 2,
            grade: 5,
            comment: "Odlično obavljen posao. Bez zamerki!",
            createdBy: {
                id: 2,
                firstName: "Milorad",
                lastName: "Veljković",
                //userName: "H.Potter",
                picture: "https://pickaface.net/gallery/avatar/lucywild215799dbd11cec2.png",
                email: "milorad.v@outlook.com",
                rating: 4.52,
                phone: ["0631415522", "11112222", "33334444"],
                status: "active",
                achievements: {},
                homeAddress: ["Nemanjina 12, Sokobanja", "Bulevar Nemanjića 15, Niš"],
                ratings: {}
            },
            request: {
                id: 3,
                name:"Ciscenje snega",
                date: new Date(),
                tags:["pomoc"],
                status:"finished",
                runner: true,
                user:{}
            },
            ratedUser: 1
        },
        "3": {
            id:3,
            grade: 1,
            comment: "Užasna usluga! Nikako ne bih ponovio saradnju!",
            createdBy: {
                id: 2,
                firstName: "Milorad",
                lastName: "Veljković",
                //userName: "H.Potter",
                picture: "https://pickaface.net/gallery/avatar/lucywild215799dbd11cec2.png",
                email: "milorad.v@outlook.com",
                rating: 4.52,
                phone: ["0631415522", "11112222", "33334444"],
                status: "active",
                achievements: {},
                homeAddress: ["Nemanjina 12, Sokobanja", "Bulevar Nemanjića 15, Niš"],
                ratings: {}
            },
            request: {
                id: 4,
                name:"Cuvanje dece",
                date: new Date(),
                tags:["deca"],
                status:"failed",
                runner: false,
                user:{}
            },
            ratedUser: 3
        },
        "4": {
            id:4,
            grade: 1,
            comment: "Užasna usluga! Nikako ne bih ponovio saradnju!",
            createdBy: {
                id: 2,
                firstName: "Milorad",
                lastName: "Veljković",
                //userName: "H.Potter",
                picture: "https://pickaface.net/gallery/avatar/lucywild215799dbd11cec2.png",
                email: "milorad.v@outlook.com",
                rating: 4.52,
                phone: ["0631415522", "11112222", "33334444"],
                status: "active",
                achievements: {},
                homeAddress: ["Nemanjina 12, Sokobanja", "Bulevar Nemanjića 15, Niš"],
                ratings: {}
            },
            request: {
                id: 4,
                name:"Cuvanje dece",
                date: new Date(),
                tags:["deca"],
                status:"failed",
                runner: false,
                user:{}
            },
            ratedUser: 1
        }
    }
}