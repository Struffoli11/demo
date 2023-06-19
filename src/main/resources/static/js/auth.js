function signup() {


  const username_input = document.getElementById('username').value;
  const password = document.getElementById('password').value;
  const confirm_password = document.getElementById('confirm_password').value;
  const email_input = document.getElementById('email').value;
  //no token in localStorage, no token needed for this resource (registration method)


  if (password == confirm_password) {
    ;
  }
  else {
    alert("Confirm Password and password are not the same. Please try again entering the same password in both fields");
    return;
  }
  //check that password & confirmed_Password correspond   

  let registrationDto = {
    username: username_input,
    password: password,
    email : email_input
  };
  //RegistrationRequestDto object construction
  var url = "http://172.31.6.4:8080/api/v1/auth/register";
  const result = fetch(url, {
    method: "POST",
    headers: { 'Content-Type': 'application/json' },
    body : JSON.stringify(registrationDto)
  })
  .then(response => response.json()).then((response) =>{
    localStorage.setItem("token", response.access_token);
    localStorage.setItem("username", response.username);
    window.location.href = "homepage.html";
    
  });
  //sends an HTTP POST request to invoke the method to register a new user
  //this checks that there is no other user with the same username and/or email
  //then save the access token used for further requests and the user's username. 
}


function signin() {
  const username = document.getElementById("user_username");
  const password = document.getElementById("user_password");

  
  let authenticatioRequestDto = {
    username: username.value,
    password: password.value,
  };
  var url = "http://172.31.6.4:8080/api/v1/auth/authenticate";
  const myHeaders = new Headers();
      myHeaders.append('Content-Type', 'application/json');
  const result = fetch(url, {
    method: "POST",
    headers: myHeaders,
    body : JSON.stringify(authenticatioRequestDto)
  })
  .then(response => response.json()).then((response) =>{
    localStorage.setItem("token", response.access_token);
    localStorage.setItem("username", response.username);
    window.location.href = "homepage.html";
    /* window.location.href = "homepage.html"; */
  });

}