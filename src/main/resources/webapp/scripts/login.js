let loginForm = document.getElementById("login-Form");

window.onload = async function(){
    // check session
    const sessionRes = await fetch(`${domain}/api/check-session`)
    const sessionUserData = await sessionRes.json()

    if(sessionUserData){
       // If user is an employee redirect to employee dashboard
       if (sessionUserData.roleId == 1){
        window.location = `${domain}/employee?userId=${sessionUserData.userId}`
      } 
      // If user is a finance manager redirect to manager dashboard
      else {
        window.location = `${domain}/manager?fmId=${sessionUserData.userId}`
      }

    }
}

// Submit the Login button
loginForm.onsubmit = async function(e) {
    e.preventDefault();
    
    let username = document.getElementById("userName").value;
    let password = document.getElementById("password").value;

    let loginResponse = await fetch(`${domain}/api/login`,{
      method: 'POST',
      body: JSON.stringify({
          userName: username.toLowerCase(),
          password: password
      })
    })
    try {
      let loginResponseData = await loginResponse.json(); 
    
      if (loginResponseData.userId > 0){
          // If user is an employee redirect to employee dashboard
          if (loginResponseData.roleId == 1){
            window.location = `${domain}/employee?userId=${loginResponseData.userId}`
          } 
          // If user is a finance manager redirect to manager dashboard
          else {
            window.location = `${domain}/manager?fmId=${loginResponseData.userId}`
          }
          
      } 
    } catch(e) {
      console.log(e);
      let messageElem = document.getElementById("validate-password");
      messageElem.innerText = "Invalid Password!";
    } 
}

let resetLink = document.getElementById("reset");
resetLink.onclick = async function() {
    window.location = `${domain}/profile`
}


// Username Validation
let usernameInput = document.getElementById("userName");
usernameInput.addEventListener('blur', (async function(){
    // Get list all of all users
    const usersDataResponse = await fetch(`${domain}/api/users`)
    const usersData = usersDataResponse.json();
    let user_name = usernameInput.value.toLowerCase();
    let found = false;

    usersData.then((user) => {
      user.forEach(element => {
        if (element.userName == user_name){
            found = true;
        }
      });
      let messageElem = document.getElementById("validate-username");
      if (user_name == "" || user_name == null){
        messageElem.innerText = "Username is empty!"
      } else if (!found){        
        messageElem.innerText = "Username not found!"
      }
    })

  })
);

usernameInput.addEventListener('focus', (function(){
    let messageElem = document.getElementById("validate-username");
    messageElem.innerText = "";
    usernameInput.value = "";
}));


// Password Validation
let passwordInput = document.getElementById("password");

passwordInput.addEventListener('focus', (function(){
    let messageElem = document.getElementById("validate-password");
    messageElem.innerText = "";
    passwordInput.value = "";
}));
