function register(){
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var password2 = document.getElementById('confirm-password').value;
    var errorText = document.getElementById('error-message');

    if(password != password2){
        errorText.innerText = "passwords don't match";
    }else{
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:8080/register", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.send(JSON.stringify
        ({
            "username": username,
            "password": password
        })
        );

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                let json = JSON.parse(xhr.responseText);
                if(json.state == 200 && json.isRegister == true){
                    window.open("http://localhost:8080/login", "_self");
                }else{
                    errorText.innerText = "this username is busy";
                }
            }
        };
    }
}