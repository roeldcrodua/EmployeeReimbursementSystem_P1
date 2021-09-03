/////////////////////////////////
// VARIABLE DECLARATIONS
/////////////////////////////////
const urlParams = new URLSearchParams(window.location.search)
const managerId = urlParams.get("fmId")

window.onload = async function(){
  //check session
  const sessionRes = await fetch(`${domain}/api/check-session`)
  try {
    const sessionUserData = await sessionRes.json()

    if (sessionUserData.userId > 0){
      // If user is an employee redirect to employee dashboard
      if (sessionUserData.roleId == 1){
        window.location = `${domain}/`
      }      
    } else {
      window.location = `${domain}/`
    }
  } catch (e) {
    console.log(e)
    window.location = `${domain}/`
  }

  let passwordResponse = await fetch(`${domain}/api/reset`);
  let passwordResponseData = passwordResponse.text();

  passwordResponseData.then( function passValue(password) {
    document.getElementById("password").value = password;
  })
}


/////////////////////////////////
// SUBMISSION OF FORM
/////////////////////////////////
let signupForm = document.getElementById("signupForm");

signupForm.onclick = async function(e) {
    e.preventDefault();
    
  let username = document.getElementById("userName").value;
  let password = document.getElementById("password").value;
  let firstname = document.getElementById("firstName").value;
  let lastname = document.getElementById("lastName").value;
  let email = document.getElementById("email").value;
  let roleid = document.getElementById("roleId").value == 0 ? 1 : document.getElementById("roleId").value ;
    
  if (username != "" && firstname != "" && lastname != "" && email != ""){
    let signupResponse = await fetch(`${domain}/api/signup`,{
        method: 'POST',
        body: JSON.stringify({
            userName: username.toLowerCase(),
            password: password,
            firstName: firstname,
            lastName: lastname,
            email: email,
            roleId: roleid
        })
    })

    let signupResponseData = await signupResponse.json();

    if(signupResponseData){
       alert("Successfully Registered the New User Employee")
       window.location = `${domain}/manager?fmId=${signupResponseData.userId}`
    }
  }
}


signupForm.addEventListener('focus', (event) => {
  event.target.style.background = 'lightyellow';
}, true);

signupForm.addEventListener('blur', (event) => {
  event.target.style.background = '';
}, true);

// Username Validation
let usernameInput = document.getElementById("userName");

usernameInput.addEventListener('blur', (async function(){
    // Get list all of all users
    const usersDataResponse = await fetch(`${domain}/api/users`)
    const usersData = usersDataResponse.json();
    let user_name = document.getElementById("userName").value.toLowerCase();

    usersData.then((user) => {
      user.forEach(element => {
        if (element.userName == user_name){
          let messageElem = document.getElementById("signup-username-message");
          messageElem.innerText = "Username already exist!"
        }
      });
    })

  })
);

usernameInput.addEventListener('focus', (function(){
        let messageElem = document.getElementById("signup-username-message");
        messageElem.innerText = "";
        usernameInput.value = "";
}));

// Email Validation
let emailInput = document.getElementById("email");

emailInput.addEventListener('blur', (async function(){
    // Get list all of all users
    const usersDataResponse = await fetch(`${domain}/api/users`)
    const usersData = usersDataResponse.json();
    let user_email = document.getElementById("email").value.toLowerCase();

    usersData.then((user) => {
      user.forEach(element => {
        if (element.email == user_email){
          let messageElem = document.getElementById("signup-email-message");
          messageElem.innerText = "Email already used!"
        }
      });
    })

  })
);

usernameInput.addEventListener('focus', (function(){
        let messageElem = document.getElementById("signup-email-message");
        messageElem.innerText = "";
        emailInput.value = "";
}));


// Go Back Button
let homeBtn = document.getElementById("home");

homeBtn.onclick = async function(){
  const sessionRes = await fetch(`${domain}/api/check-session`)
  try {
    const sessionUserData = await sessionRes.json()

    if (sessionUserData.userId > 0){
      // If user is an employee redirect to employee dashboard
      if (sessionUserData.roleId == 1){
        window.location = `${domain}/employee?userId=${sessionUserData.userId}`
      } 
      // If user is a finance manager redirect to manager dashboard
      else {
        window.location = `${domain}/manager?fmId=${sessionUserData.userId}`
      }
      
    } else {
      window.location = `${domain}/`
    }
  } catch (e) {
    console.log(e)
    window.location = `${domain}/`
  }
}