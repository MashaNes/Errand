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
                status: 1
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
                status: 2
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
                status: 3
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
                status: 4
            }
        ]

    }
}