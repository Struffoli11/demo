function signup() {

  var special_characters = "!@#$^&%*()+=-[]\/{}|:<>?,.";
  const username_input = document.getElementById('username').value;
  const password = document.getElementById('password').value;
  const confirm_password = document.getElementById('confirm_password').value;
  const email_input = document.getElementById('email').value;
  //no token in localStorage, no token needed for this resource (registration method)


  if (password == confirm_password) {
    //check that password's lenght is between 8 and 16 character long
    if(password.length < 8 || password.length > 16){
      alert("please enter a password whose lenght is not bigger than 16 characters and not less than 8 characters");
      return;
    }

    //check the same property for username
    if(username_input.length < 8 || username_input.length > 16){
      alert("please enter a username whose lenght is not bigger than 16 characters and not less than 8 characters");
      return;
    }

    //check that a valid email address was given
    if(!email_input.includes("@") && !email_input.endsWith(".com") || !email_input.endsWith(".net") || !email_input.endsWith(".it") || 
    !email_input.endsWith(".us") || !email_input.endsWith(".org") || !email_input.endsWith(".fr") || !email_input.endsWith(".es") ||
    !email_input.endsWith(".de") || !email_input.endsWith(".uk") || !email_input.endsWith(".de")){
        
        //check that there are no white spaces at the start and at the end of email_input
        if(!(email_input.trim() == email_input)){
          alert("please enter a valid email address");
          return;
        }

        //check that there are no white spaces in between the email_input string
        if(!(email_input.split(" ").length == 1)){
          alert("please enter a valid email address");
          return;
        }
        
        //check that the recipient name is not missing
        const email_address = email_input.split("@");

        if(email_address.length != 2){
            alert("please enter a valid email address");
            return;
        }

        if(email_address[0] == "" || email_address[0] == null){
          alert("please enter a valid email address");
          return;
        }

        for (let char of special_characters) {
          if(email_address[0].startsWith(char)){
            alert("please enter a valid email address");
            return;
          }
        }

        
        //check that the domain name is not missing
        const domain = email_address[1].split(".");
        if(domain.length < 2){
          alert("please enter a valid email address");
          return;
        }

        if(domain[0] == "" || domain[0] == null){
          alert("please enter a valid email address");
          return;
        }

        for (let char of special_characters) {
          if(domain[0].startsWith(char)){
            alert("please enter a valid email address");
            return;
          }
        }

    }
  }
  else {
    alert("Confirm Password and password are not the same. Please try again entering the same password in both fields");
    return;
  }
  //check that password & confirmed_Password correspond   
  alert("An email is going to be sent to your email address. Please check your email box");
  return;
  /* 
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
    window.location.href = "index.html";
    
  }); */
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
    window.location.href = "index.html";
    /* window.location.href = "homepage.html"; */
  });

}