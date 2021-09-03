const urlParams = new URLSearchParams(window.location.search)
const user_Id = urlParams.get("userId");
const managerId = urlParams.get("fmId");

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

let passwordForm = document.getElementById("signupForm");
// Edit password
passwordForm.onsubmit = async function(e) {
    e.preventDefault();
    
  let username = document.getElementById("userName").value;
  let oldPassword = document.getElementById("oldPassword").value;
  let newPassword = document.getElementById("newPassword").value;

  let passwordResponse = await fetch(`${domain}/api/reset`,{
        method: 'POST',
        body: JSON.stringify({
            userName: username.toLowerCase(),
            password: oldPassword,
            email: newPassword,
        })
    })

    if(passwordResponse.status == 200){
      alert("An email notification was sent to your email");
      window.location = `${domain}`;
  }       

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
      let messageElemUsername = document.getElementById("validate-username");
      if (user_name == "" || user_name == null){
        messageElemUsername.innerText = "Username is empty!"
      } else if (!found){
        messageElemUsername.innerText = "Username not found!"
      } else {
        messageElemUsername.innerText = "";
      }
    })

  })
);


let oldPasswordInput = document.getElementById("oldPassword");
let newPasswordInput = document.getElementById("newPassword");
let confirmPasswordInput = document.getElementById("confirmPassword");

oldPasswordInput.addEventListener('blur',(function(){
  let messageElemOld = document.getElementById("old-password-message");

    if (oldPasswordInput.value == ""){
      messageElemOld.innerText = "Password is empty!"
    } else {
      messageElemOld.innerText = "";
    }
  })
);

newPasswordInput.addEventListener('blur',(function(){
    let messageElemNew = document.getElementById("new-password-message");

    if (newPasswordInput.value == oldPasswordInput.value){
      messageElemNew.innerText = "New Password must NOT be the same!"
    } else if (newPasswordInput.value == ""){
      messageElemNew.innerText = "New Password is empty!"
    } else {
      messageElemNew.innerText = "";
    }
  })
);

confirmPasswordInput.addEventListener('blur',(function(){
  let messageElemConfirm = document.getElementById("confirm-password-message");

    if (confirmPasswordInput.value != newPasswordInput.value){
      messageElemConfirm.innerText = "Confirmed Password not matched!"
    } else if (confirmPasswordInput.value == ""){
      messageElemConfirm.innerText = "Confirmed Password is empty!"
    } else {
      messageElemConfirm.innerText = "";
    }
  })
);

