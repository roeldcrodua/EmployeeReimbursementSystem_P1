/////////////////////////////////
// VARIABLE DECLARATIONS
/////////////////////////////////
const urlParams = new URLSearchParams(window.location.search)
const managerId = urlParams.get("fmId")
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
var inputDescription = document.getElementById("input-description");
var inputAmount = document.getElementById("input-amount");
var inputDateSubmitted = document.getElementById("date-submitted");
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
       if(sessionUserData.userId != managerId){
        window.location = `${domain}/`
      } 
   }

  populateProfileData(sessionUserData);
  populatePendingReimbursementData();
  populateResolvedReimbursementData(sessionUserData.userId);

}

/////////////////////////////////
// SUBMIT EVENT FOR FORM REIMBURSEMENT
/////////////////////////////////
document.getElementById('reimbursement-Form').onsubmit = async (e) => {
  e.preventDefault();

  // Checked if it is new file then use POST httpverb. If reimbId is specified then use PATCH httpverb
  let submitResponse = await fetch(`${domain}/api/resolve`,{
    method: 'PATCH',
    body: JSON.stringify({
      statusId: inputReimbStatusFromManger.value,
      resolver: managerId, 
      reimbId: inputReimbId.value
    })
  })
  if (submitResponse.status == 200){
    populatePendingReimbursementData();
    populateResolvedReimbursementData(managerId);
    document.getElementById('reimbursement-Form').setAttribute("style", "display:none;");
  } else {
    let messageElem = document.getElementById("error-message");
    messageElem.innerText = "Error was encountered!";
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
// FUNCTION populatePendingReimbursementData
/////////////////////////////////
async function populatePendingReimbursementData() {
  
  //Get the table element from the html
  let table = document.getElementById("reimbursement-table-pending")
  //create a new table body
  var new_tbody = document.createElement('tbody');
  var old_tbody = table.getElementsByTagName('tbody')[0];

  // Clean-up the table and repopulate the entries.
  old_tbody.innerHTML = ``;

  // Get list all of the owned reimbursement
  const reimbursementDataResponse = await fetch(`${domain}/api/manager`);
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
      let col1 = row.insertCell(0);
      let col2 = row.insertCell(1);
      let col3 = row.insertCell(2);
      let col4 = row.insertCell(3);
      let col5 = row.insertCell(4);
      let col6 = row.insertCell(5);
      let col7 = row.insertCell(6);
 
      col1.innerHTML = element.reimbId;
      col2.innerHTML = types.VALUES[element.typeId].description;
      col3.innerHTML = element.description;
      col4.innerHTML = "$ " + element.amount;
      col5.innerHTML = convertUnixTimestampToUTCDate(element.dateSubmitted),
      col6.innerHTML = element.author,
      col7.innerHTML = `<button class="btn btn-info" id="view-btn" onclick="resolveFtn(${element.reimbId}, ${element.author})">Resolve Now?</button>`;
      
      col2.className = "ps-5 text-start";
      col3.className = "ps-5 text-start";
      col4.className = "ps-5 text-start";
      col5.className = "ps-5 text-center";
    })
   old_tbody.parentNode.replaceChild(new_tbody,old_tbody);
  })
}

/////////////////////////////////
// FUNCTION populatePendingReimbursementData
/////////////////////////////////
async function populateResolvedReimbursementData(userId) {
  
  //Get the table element from the html
  let table = document.getElementById("reimbursement-table-resolved")
  //create a new table body
  var new_tbody = document.createElement('tbody');
  var old_tbody = table.getElementsByTagName('tbody')[0];

  // Clean-up the table and repopulate the entries.
  old_tbody.innerHTML = ``;

  // Get list all of the owned reimbursement
  const reimbursementDataResponse = await fetch(`${domain}/api/resolve?fmId=${userId}`);
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
      col4.innerHTML = "$ " + element.amount;
      col5.innerHTML = convertUnixTimestampToUTCDate(element.dateSubmitted);
      col6.innerHTML = convertUnixTimestampToUTCDate(element.dateResolved);
      col7.innerHTML = element.author;
      col8.innerHTML = statuses.VALUES[element.statusId].description;
      
      col2.className = "ps-5 text-start";
      col3.className = "ps-5 text-start ";
      col4.className = "ps-5 text-start";
      col5.className = "ps-5 text-start";
     
    })
   old_tbody.parentNode.replaceChild(new_tbody,old_tbody);
  })
}

/////////////////////////////////
// FUNCTION setReimbursementData
/////////////////////////////////
async function setReimbursementData(userId) {
  // Add new entries to the reibursement file. Some are default values.
  inputDateProcessed.value = setCurrentDate(); // current date  

  // get the fullname of the resolver/manager if any
  let fullNameResolverResponse = await fetch(`${domain}/api/user`,{
    method: 'POST',
    body: JSON.stringify({
        userId: managerId
    })
  })
  try {
    let fullNameResolverResponseData = await fullNameResolverResponse.json(); 
    inputProcessedBy.value = "(ID: " + managerId + " ) " + fullNameResolverResponseData.firstName + " " + fullNameResolverResponseData.lastName;
  } catch  (e){
    inputProcessedBy.value = managerId;  // set to managerId
  }    
}

/////////////////////////////////
// FUNCTION getReimbursementData
/////////////////////////////////
async function getReimbursementData(sessionUserData, reimbId) {
  // Get a specific reimbursement file.
  const reimbursementDataResponse = await fetch(`${domain}/api/reimbursement?userId=${sessionUserData.userId}&reimbId=${reimbId}`)
  const reimbursementData = reimbursementDataResponse.json();

  reimbursementData.then(async function renderValue(element){ 
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
    inputAmount.value = "$ " + element.amount;
    inputDateSubmitted.value = convertUnixTimestampToUTCDate(element.dateSubmitted);    
    
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
// BUTTON EVENTS: view
/////////////////////////////////
// view a reimbursement file
async function resolveFtn(reimbId, userId){
  // Display the Form
  document.getElementById('reimbursement-Form').setAttribute("style", "display:block;");
  // Display the input element to render the value then 
  getReimbursementData(userId, reimbId);
  // Set the remaining values from the user manager
  setReimbursementData(userId);
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
// BUTTON EVENTS: reset-password
/////////////////////////////////
let resetLink = document.getElementById("reset-password-btn");
resetLink.onclick = function() {
    window.location = `${domain}/profile`
}

/////////////////////////////////
// BUTTON EVENTS: sign-up-btn
/////////////////////////////////
let signupLink = document.getElementById("sign-up-btn");
signupLink.onclick = function() {
    window.location = `${domain}/signup`
}