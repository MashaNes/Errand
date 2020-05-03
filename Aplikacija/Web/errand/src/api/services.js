export const fetchServices = () => {
    return {
        services: [
            {
                serviceType: "kupovina",
                description: "Kupovina namirnica sa priloženog spiska"
            },
            {
                serviceType: "isporuka",
                description: "Prenošenje nečega sa jednog mesta na drugo"
            },
            {
                serviceType: "računi",
                description: "Plaćanje računa ili odlazak u poštu"
            },
            {
                serviceType: "čišćenje snega",
                description: "Čišćenje dvorišta/prilaza/auta od napadalog snega"
            },
            {
                serviceType: "čuvanje dece",
                description: "Vođenje računa o deci"
            },
            {
                serviceType: "prevoz",
                description: "Prevoz sa jedne lokacije na drugu"
            }
        ] 
    }
}