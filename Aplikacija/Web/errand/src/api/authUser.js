export const login = (email, password) => {
    var result = ""
    fetch("http://localhost:8000/api/v1/login/",
    {
        method: 'POST',
        headers:
        {
            "Content-type" : "application/json"
        },
        body:  JSON.stringify(
        {
            "username" : email,
            "password" : password
        })
    }).then( p => 
        {
            if(p.ok)
            {
                p.json().then(data =>
                {
                    console.log(data)
                    result =  data
                })
            }
            else
            {
                console.log("Error")
                result =  "Error"
            }
        });
    return result
}