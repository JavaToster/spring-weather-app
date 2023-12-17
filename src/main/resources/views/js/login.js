function login(){
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var errorText = document.getElementById('error-message');

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/login", true);
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
            if(json.status == 200 && json.username == true){
                document.cookie = "username="+username+"; expires="+(60*60*24);
                window.open("http://localhost:8080/weather", "_self");
            }else{
                errorText.innerText = "not correct password";
            }

        }
    };
}