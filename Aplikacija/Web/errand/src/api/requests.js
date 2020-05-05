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
            user: {
                id: 3,
                firstName: "Dimitrije",
                lastName: "Pantic",
                picture: "https://www.in4s.net/wp-content/uploads/2017/01/Pantic.jpg",
                email: "dime.pantela@gmail.com",
                rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            }
        },
        "2":{
            id: 2,
            name:"Ciscenje snega",
            date: new Date(),
            tags:["pomoc"],
            status:"pending",
            runner: false
        },
        "3":{
            id: 3,
            name:"Ciscenje snega",
            date: new Date(),
            tags:["pomoc"],
            status:"finished",
            runner: true,
            user:{
                id: 4,
                firstName: "Srecko",
                lastName: "Sojic",
                picture: "https://www.srbijadanas.com/sites/default/files/styles/full_article_image/public/a/t/2015/09/09/srecko-sojic-foto-priscreen-ktvtelevizija-zrenjanin.jpg",
                email: "iznenadjen.i.uvredjen@gmail.com",
                rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            }
        },
        "4":{
            id: 4,
            name:"Cuvanje dece",
            date: new Date(),
            tags:["deca"],
            status:"failed",
            runner: false,
            user:{
                id: 8,
                firstName: "Derek",
                lastName: "Shepherd",
                picture: "https://alchetron.com/cdn/derek-shepherd-8b30aed5-5e2e-4a12-b8e2-8c0c90a0736-resize-750.jpeg",
                email: "McDreamy@seattlegrace.com",
                rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            }
        },
        "5":{
            id: 5,
            name:"Kupi mi cokoladu",
            date: new Date(),
            tags:["kupovina", "racuni"],
            status:"pending",
            runner: false
        },
        "6":{
            id: 6,
            name:"Odnesi tetki lek",
            date: new Date(),
            tags:["pomoc"],
            status:"running",
            runner: true,
            user:{
                id: 7,
                firstName: "Chandler",
                lastName: "Bing",
                picture: "https://i.pinimg.com/originals/a4/a4/7d/a4a47d837726daa86ece52c8dc5b812a.jpg",
                email: "CouldIBeAnyMoreSarcastic@centralPerk.com",
                rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            }
        },
        "7":{
            id: 7,
            name:"Ciscenje garaze",
            date: new Date(),
            tags:["pomoc"],
            status:"finished",
            runner: true,
            user:{
                id: 5,
                firstName: "Obi Wan",
                lastName: "Kenobi",
                picture: "https://www.denofgeek.com/wp-content/uploads/2019/08/star-wars-obi-wan-kenobi-1-scaled.jpg?fit=2560%2C1707",
                email: "HelloThere@galaxy.com",
                rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            }
        },
        "8":{
            id: 8,
            name:"Voznja na posao",
            date: new Date(),
            tags:["deca", "voznja"],
            status:"running",
            runner: false,
            user:{
                id: 6,
                firstName: "Chewbacca",
                lastName: "Chewie",
                picture: "https://pbs.twimg.com/profile_images/942329591426842624/cGFGEd2z.jpg",
                email: "Hrarrararaaaa@galaxy.com",
                rating: 3.45,
                phone: ["123456", "220098123", "13455562"],
                status: "active",
                achievements: ["1", "3", "4", "5", "6"],
                homeAddress: ["4 Privet Drive, Little Whinging, Surrey England, Great Britain", "Zmaj Jovina 3, Sokobanja"],
                ratings: ["1", "2", "3"]
            }
        }
      }
}