export const fetchUserServices = () => {
    return {
        UserServices: [
            {
                sevice:
                {
                    serviceType: "kupovina",
                    description: "Kupovina namirnica sa priloženog spiska"
                },
                maxDist: 5,
                paymentType: 3,
                paymentAmount: 200,
                minRating: 2
            },
            {
                sevice:
                {
                    serviceType: "isporuka",
                    description: "Prenošenje nečega sa jednog mesta na drugo"
                },
                maxDist: 7,
                paymentType: 1,
                paymentAmount: 50,
                minRating: 3.1
            },
            {
                sevice:
                {
                    serviceType: "računi",
                    description: "Plaćanje računa ili odlazak u poštu"
                },
                maxDist: 7,
                paymentType: 2,
                paymentAmount: 300,
                minRating: 4.2
            },
            {
                sevice:
                {
                    serviceType: "čišćenje snega",
                    description: "Čišćenje dvorišta/prilaza/auta od napadalog snega"
                },
                maxDist: 20,
                paymentType: 3,
                paymentAmount: 350,
                minRating: 1.5
            }
        ] 
    }
}