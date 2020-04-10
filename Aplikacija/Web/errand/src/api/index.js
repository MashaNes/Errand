export const fetchRequests = () =>
{
    return {
        "1":{
            id: 1,
            name:"Kupovina",
            date: new Date(),
            tags:["kupovina", "racuni"],
            status:"running",
            runner: false
        },
        "2":{
            id: 2,
            name:"Ciscenje snega",
            date: new Date(),
            tags:["pomoc"],
            status:"pending",
            runner: true
        },
        "3":{
            id: 3,
            name:"Ciscenje snega",
            date: new Date(),
            tags:["pomoc"],
            status:"finished",
            runner: true
        },
        "4":{
            id: 4,
            name:"Cuvanje dece",
            date: new Date(),
            tags:["deca"],
            status:"failed",
            runner: false
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
            runner: true
        },
        "7":{
            id: 7,
            name:"Ciscenje garaze",
            date: new Date(),
            tags:["pomoc"],
            status:"finished",
            runner: true
        },
        "8":{
            id: 8,
            name:"Voznja na posao",
            date: new Date(),
            tags:["deca", "voznja"],
            status:"pending",
            runner: false
        }
      }
}