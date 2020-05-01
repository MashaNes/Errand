export const login = (email, password) => {
    var code = "";
    fetch("http://localhost:8000/api/auth/token/login",
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
    }).then( p => p.json().then(data =>
        {
            console.log(data)
            code = data
        }));  
        
    return code;
}