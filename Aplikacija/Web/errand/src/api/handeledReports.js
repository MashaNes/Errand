export const fetchHandeledReports = () =>
{
    return {
        count: 4,
        results: [
            {
                id: 1,
                comment: "test comment 1",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 1,
                request:
                {
                    name: "Request 1"
                },
                pictures:[]
            },
            {
                id: 2,
                comment: "test comment 2",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 2,
                request:
                {
                    name: "Request 2"
                },
                pictures:["slika1", "slika2"]
            },
            {
                id: 3,
                comment: "test comment 3",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 3,
                request: null,
                pictures:["slika1", "slika2", "slika3", "slika4", "slika5", "slika6"]
            },
            {
                id: 4,
                comment: "test comment 4",
                user:
                {
                    id:3,
                    first_name: "Jovan",
                    last_name: "Dimitrijevic",
                    email: "jovan.dimitrijevic@gmail.com"
                },
                created_by:
                {
                    id:5,
                    first_name: "Ivana",
                    last_name: "Milivojevic",
                    email: "ivana.milivojevic@gmail.com"
                },
                status: 4,
                request: null,
                pictures:[]
            }
        ]

    }
}