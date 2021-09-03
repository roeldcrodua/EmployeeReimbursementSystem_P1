/////////////////////////////////
// VARIABLE DECLARATIONS
/////////////////////////////////
const urlParams = new URLSearchParams(window.location.search)
const user_Id = urlParams.get("userId")
const reimb_Id = urlParams.get("reimbId")

// Lookup enum values for reimbursement types
const types = {
  VALUES  : {
    1: { description: "LODGING" , value: 1  },
    2: { description: "TRAVEL"  , value: 2  },
    3: { description: "FOOD"    , value: 3  },
    4: { description: "OTHER"   , value: 4  }
  }
}

// Lookup enum values for reimbursement statuses
const statuses = {
  VALUES  : {
    1: { description: "PENDING" , value: 1  },
    2: { description: "APPROVED", value: 2  },
    3: { description: "DENIED"  , value: 3  }
  }
}


// Declaring globally these input element forms
var inputReimbId = document.getElementById("input-reimb-id");
var inputFileOwner = document.getElementById("file-owner");
var inputReimbType = document.getElementById("input-reimb-type");
var inputReimbTypeFromUser = document.getElementById("typeId");
var inputDescription = document.getElementById("input-description");
var inputAmount = document.getElementById("input-amount");
var inputDateSubmitted = document.getElementById("date-submitted");
var inputReimbStatus = document.getElementById("reimb-status");
var inputReimbStatusFromManger = document.getElementById("statusId");
var inputDateProcessed = document.getElementById("date-processed");
var inputProcessedBy = document.getElementById("processed-by");



/////////////////////////////////
// WINDOW START/LOAD
/////////////////////////////////

window.onload = async function(){
    //check session

    const sessionRes = await fetch(`${domain}/api/check-session`)
    const sessionUserData = await sessionRes.json()

    if(sessionUserData){
       if(sessionUserData.userId != user_Id){
        window.location = `${domain}/`
      } 
   }

  populateProfileData(sessionUserData);
  populateReimbursementData(sessionUserData.userId);

  
  /////////////////////////////////
  // SUBMIT EVENT FOR FORM REIMBURSEMENT
  /////////////////////////////////
  document.getElementById('create-new-btn').onclick = async (e) => {
    e.preventDefault();
    // Checked if it is new file then use POST httpverb. If reimbId is specified then use PATCH httpverb
    let submitResponse = await fetch(`${domain}/api/reimbursement`,{
      method: inputReimbId.value == "" ? 'POST' : 'PUT',
      body: JSON.stringify({
        reimbId: inputReimbId.value == null ? 0 : inputReimbId.value,
        amount: inputAmount.value,
        description: inputDescription.value,
        typeId: inputReimbTypeFromUser.value == 0 ? 4 : inputReimbTypeFromUser.value,
        author: sessionUserData.userId
      })
    })

    if (submitResponse.status == 200){
      inputReimbId.value == "" ? alert("Successfully submitted your new filed reimbursement") : alert("Successfully edited your filed reimbursement");
        document.getElementById('reimbursement-Form').setAttribute("style", "display:none;");
        populateReimbursementData(sessionUserData.userId);
        
    } else {
      let messageElem = document.getElementById("error-message");
      messageElem.innerText = "Error was encountered!";
    }
  }

}


/////////////////////////////////
// FUNCTION populateProfileData
/////////////////////////////////
async function populateProfileData(sessionData) {

  let accountInfoElem = document.getElementById("account-information-container");
  //accountInfoElem.innerHTML = "";

  // Get the value of the role Id from the database.
  let roleIdResponse = await fetch(`${domain}/api/role?roleId=${sessionData.roleId}`)
  roleIdResponse.json().then( (role) => {
    let roleValue = role.toUpperCase();
  
    const userData = [sessionData.firstName.toUpperCase(), sessionData.lastName.toUpperCase(), sessionData.userName, sessionData.email, sessionData.userId, roleValue];
    const text = ["First Name", "Last Name", "Username", "Email", "ID No.", "Role Id"];

    userData.forEach( function createInnerDiv(value, index) {

      let divContainerElem = document.createElement("div");
      divContainerElem.className = "infoText mb-2";

      let labelElem = document.createElement("label");
      labelElem.className = "text-capitalize text-black fw-bolder";
      labelElem.innerText = text[index];

      let inputElem = document.createElement("input");
      inputElem.className = "disabled ms-5";
      inputElem.type = "text";
      inputElem.disabled = true;
      inputElem.value = value;

      divContainerElem.appendChild(labelElem);
      divContainerElem.appendChild(inputElem);
      accountInfoElem.appendChild(divContainerElem);

    });
  })
}


/////////////////////////////////
// FUNCTION populateReimbursementData
/////////////////////////////////
async function populateReimbursementData(userId) {

  //Get the table element from the html
  let table = document.getElementById("reimbursement-table")
  //create a new table body
  var new_tbody = document.createElement('tbody');
  var old_tbody = table.getElementsByTagName('tbody')[0];

  // Clean-up the table and repopulate the entries.
  old_tbody.innerHTML = ``;

  // Get list all of the owned reimbursement
  const reimbursementDataResponse = await fetch(`${domain}/api/reimbursements?userId=${userId}`)
  const reimbursementData = reimbursementDataResponse.json();


  reimbursementData.then((reimbursement) => { 
    reimbursement.sort((a,b) => {
      if (a.reimbId < b.reimbId)
        return 1; 
      if (a.reimbId > b.reimbId)
        return -1; 
      return 0;
    })
  
    reimbursement.forEach(function createRow(element){

      let row = new_tbody.insertRow();
      //row.className = "table-light";
      let col1 = row.insertCell(0);
      let col2 = row.insertCell(1);
      let col3 = row.insertCell(2);
      let col4 = row.insertCell(3);
      let col5 = row.insertCell(4);
      let col6 = row.insertCell(5);
      let col7 = row.insertCell(6);
      let col8 = row.insertCell(7);

      col1.innerHTML = element.reimbId;
      col2.innerHTML = types.VALUES[element.typeId].description;
      col3.innerHTML = element.description;
      col4.innerHTML = statuses.VALUES[element.statusId].description;
      col5.innerHTML = "$ " + element.amount;
      col6.innerHTML = convertUnixTimestampToUTCDate(element.dateSubmitted),
      col7.innerHTML = element.dateResolved == null ? "" : convertUnixTimestampToUTCDate(element.dateResolved),
      col8.innerHTML = element.statusId == 1 ? `<button class="view-btn btn-outline-success" onclick="viewFtn(${element.reimbId}, ${userId})">View</button>   <button class="remove-btn btn-outline-danger" onclick="removeFtn(${element.reimbId}, ${userId})">Remove</button>` : `<button class="view-btn btn-outline-success" onclick="viewFtn(${element.reimbId}, ${userId})">View</button>   <button class="remove-btn btn-outline-dark" onclick="removeFtn(${element.reimbId}, ${userId})" disabled >Remove</button>`;
      
      col2.className = "ps-5 text-start";
      col3.className = "ps-5 text-start";
      col4.className = "ps-5 text-start";
      col5.className = "ps-5 text-start";
      col6.className = "ps-5 text-start";
      col7.className = "ps-5 text-start";
      col8.className = "ps-5 text-start d-inline-flex ";
    })
   old_tbody.parentNode.replaceChild(new_tbody,old_tbody);
  })
}


/////////////////////////////////
// FUNCTION setReimbursementData
/////////////////////////////////
function setReimbursementData(sessionUserData) {
  // Add new entries to the reibursement file. Some are default values.
  inputReimbId.value = ""; // no value yet
  inputFileOwner.value = sessionUserData; //set to owner userId by default
  inputDateSubmitted.value = setCurrentDate(); // current date
  inputReimbStatus.value = statuses.VALUES[1].description;; // set to pending (1)
  inputDateProcessed.value = ""; // set to null
  inputProcessedBy.value = "";  // set to null

}

/////////////////////////////////
// FUNCTION getReimbursementData - rendering values to the form
/////////////////////////////////
async function getReimbursementData(sessionUserData, reimbId) {
  // Get a specific reimbursement file.
  const reimbursementDataResponse = await fetch(`${domain}/api/reimbursement?userId=${sessionUserData}&reimbId=${reimbId}`)
  const reimbursementData = reimbursementDataResponse.json();

  reimbursementData.then( async function renderValue(element){ 
    // Render the reimbursement info into the form
    inputReimbId.value = element.reimbId;

    // get the fullname of the owner
    let fullNameOwnerResponse = await fetch(`${domain}/api/user`,{
      method: 'POST',
      body: JSON.stringify({
          userId: element.author
      })
    })
    try {
      let fullNameOwnerResponseData = await fullNameOwnerResponse.json(); 
      inputFileOwner.value = "(ID: " + element.author + " ) " + fullNameOwnerResponseData.firstName + " " + fullNameOwnerResponseData.lastName;
    } catch  (e){
      inputFileOwner.value = element.author;
    }
    
    inputReimbType.value = types.VALUES[element.typeId].description;
    inputDescription.value = element.description;
    inputAmount.value = element.amount;
    inputDateSubmitted.value = convertUnixTimestampToUTCDate(element.dateSubmitted);    
    inputReimbStatus.value = statuses.VALUES[element.statusId].description;
    inputDateProcessed.value = element.dateResolved == null ? "" : convertUnixTimestampToUTCDate(element.dateResolved);

    // get the fullname of the resolver/manager if any
    if (element.resolver > 0){
      let fullNameResolverResponse = await fetch(`${domain}/api/user`,{
        method: 'POST',
        body: JSON.stringify({
            userId: element.resolver
        })
      })
      try {
        let fullNameResolverResponseData = await fullNameResolverResponse.json(); 
        inputProcessedBy.value = "(ID: " + element.resolver + " ) " + fullNameResolverResponseData.firstName + " " + fullNameResolverResponseData.lastName;
      } catch  (e){
        inputProcessedBy.value = element.resolver;
      }
    } else {
      inputProcessedBy.value = "";
    }
    
    
    
    // All resolved reimbursements are not editable anymore.
    if (element.statusId == 1){
      document.getElementById('update-btn').setAttribute("class", "btn btn-light show");
    } else {
      document.getElementById('create-new-btn').setAttribute("class", "visually-hidden");
      document.getElementById('update-btn').setAttribute("class", "visually-hidden");
      document.getElementById('input-description').setAttribute("disabled");
      document.getElementById('input-amount').setAttribute("disabled");
    }
  })   
}

/////////////////////////////////
// FUNCTION convertUnixTimestampToUTCDate
/////////////////////////////////
function convertUnixTimestampToUTCDate(inputDate){
  let newDate = new Date(inputDate)
  return newDate.toLocaleDateString();
}


/////////////////////////////////
// FUNCTION setCurrentDate
/////////////////////////////////
function setCurrentDate(){
  const timeElapsed = Date.now();
  const today = new Date(timeElapsed);    
  return today.toLocaleDateString();
}


/////////////////////////////////
// BUTTON EVENTS: logout
/////////////////////////////////
// end session and redirect to login when logout button is clicked
let logoutBtn = document.getElementById("logout-btn")
logoutBtn.onclick = async function(){
    let logoutRes = await fetch(`${domain}/api/logout`)
    window.location = `${domain}/`
}


/////////////////////////////////
// BUTTON EVENTS: create-btn
/////////////////////////////////
  // create new reimbursement file
  let createBtn = document.getElementById("create-btn")
  createBtn.onclick =  function(){
    // Display the Form
    document.getElementById('reimbursement-Form').setAttribute("style", "display:block;");
    // Display the select element to choose the option required
    // Set the default values for new submission file
    document.getElementById('typeId').setAttribute("style", "display:block;");
    document.getElementById('reimb-status').setAttribute("style", "display:block;");
    document.getElementById('input-description').removeAttribute("disabled");
    document.getElementById('input-amount').removeAttribute("disabled");
    document.getElementById('input-reimb-type').setAttribute("style", "display:none;");
    document.getElementById('create-new-btn').setAttribute("class", "btn btn-success show");
    setReimbursementData(user_Id);
  }


/////////////////////////////////
// BUTTON EVENTS: view
/////////////////////////////////
// view a reimbursement file
function viewFtn(reimbId, userId){
  getReimbursementData(userId, reimbId)
  // Display the Form
  document.getElementById('reimbursement-Form').setAttribute("style", "display:block;");
  // Display the input element to render the value then
  // Edit the reimbursment file if reimbId param is specified
  document.getElementById('input-reimb-type').setAttribute("style", "display:block;");
  document.getElementById('reimb-status').setAttribute("style", "display:block;");    
  document.getElementById('typeId').setAttribute("style", "display:none;");
  document.getElementById('input-description').setAttribute("disabled", true);
  document.getElementById('input-amount').setAttribute("disabled", true);  
  }

/////////////////////////////////
// BUTTON EVENTS: delete
/////////////////////////////////
//delete item given id
async function removeFtn(reimbId, userId){
    let removeRes = await fetch(`${domain}/api/reimbursement?userId=${userId}&reimbId=${reimbId}`,{
        method: "DELETE"
    })

    if(removeRes.status == 200){
      populateReimbursementData(userId);
    }
}

/////////////////////////////////
// BUTTON EVENTS: close
/////////////////////////////////
// Close the Form
let cancelBtn = document.getElementById("cancel-btn")
cancelBtn.onclick = async function(){
  document.getElementById('reimbursement-Form').setAttribute("style", "display:none;");
  
}

/////////////////////////////////
// BUTTON EVENTS: edit
/////////////////////////////////
// Edit the Form and Enable the submit button
let editBtn = document.getElementById("update-btn")
editBtn.onclick = async function(){
  document.getElementById('typeId').setAttribute("style", "display:block;");
  document.getElementById('reimb-status').setAttribute("style", "display:block;");
  document.getElementById('input-description').removeAttribute("disabled");
  document.getElementById('input-amount').removeAttribute("disabled");
  document.getElementById('input-reimb-type').setAttribute("style", "display:none;");
  document.getElementById('update-btn').setAttribute("class", "btn btn-light visually-hidden");
  document.getElementById('create-new-btn').setAttribute("class", "btn btn-success show");

}

/////////////////////////////////
// BUTTON EVENTS: reset-password
/////////////////////////////////
let resetLink = document.getElementById("reset-password-btn");
resetLink.onclick = function() {
    window.location = `${domain}/profile`
}