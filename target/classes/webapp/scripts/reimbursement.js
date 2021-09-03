const urlParams = new URLSearchParams(window.location.search)
const userId = urlParams.get("userId");
const reimbId = urlParams.get("reimbId");
const managerId = urlParams.get("fmId");

const types = {
  VALUES  : {
    1: { description: "LODGING" , value: 1  },
    2: { description: "TRAVEL"  , value: 2  },
    3: { description: "FOOD"    , value: 3  },
    4: { description: "OTHER"   , value: 4  }
  }
}

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



window.onload = async function(){
  //check session
  const sessionRes = await fetch(`${domain}/api/check-session`)
  const sessionUserData = await sessionRes.json()

    if(sessionUserData){
       if(sessionUserData.userId != userId){
        window.location = `${domain}/`
      } 
   }else{
       window.location = `${domain}/`
   }


   // Display the input element to render the value then
   // Edit the reimbursment file if reimbId param is specified
   if (reimbId != null){
      document.getElementById('input-reimb-type').setAttribute("style", "display:block;");
      document.getElementById('reimb-status').setAttribute("style", "display:block;");      
      getReimbursementData(sessionUserData, reimbId)

   }
   // Display the select element to choose the option required
   // Set the default values for new submission file
   else { 
      document.getElementById('typeId').setAttribute("style", "display:block;");
      document.getElementById('reimb-status').setAttribute("style", "display:block;");
      document.getElementById('input-description').removeAttribute("disabled");
      document.getElementById('input-amount').removeAttribute("disabled");
      setReimbursementData(sessionUserData);
   }



   // Cancel the Form and go back to Employee / Manager DashBoard
  let cancelBtn = document.getElementById("cancel-btn")
  cancelBtn.onclick = async function(){
    // Redirect to employee dashboard
    if (sessionUserData.roleId == 1){
      window.location = `${domain}/employee?userId=${sessionUserData.userId}`
    }
    // Redirect to manager dashboard
    else {
      window.location = `${domain}/manager?fmId=${sessionUserData.userId}`
    }
  }

   // Edit the Form and Enable the submit button
   let editBtn = document.getElementById("edit-btn")
   editBtn.onclick = async function(){
      document.getElementById('typeId').setAttribute("style", "display:block;");
      document.getElementById('reimb-status').setAttribute("style", "display:block;");
      document.getElementById('input-description').removeAttribute("disabled");
      document.getElementById('input-amount').removeAttribute("disabled");
      document.getElementById('input-reimb-type').setAttribute("style", "display:none;");
      document.getElementById('edit-btn').setAttribute("class", "btn btn-light show disabled");
   }

  // Submit the Form and go back to Employee Main Page
  document.getElementById('reimbursement-Form').onsubmit = async (e) => {
    e.preventDefault();

    // Checked if it is new file then use POST httpverb. If reimbId is specified then use PATCH httpverb
    let submitResponse = await fetch(`${domain}/api/reimbursement`,{
      method: reimbId == null ? 'POST' : 'PATCH',
      body: JSON.stringify({
        reimbId: reimbId == null ? 0 : reimbId,
        amount: inputAmount.value,
        description: inputDescription.value,
        typeId: inputReimbTypeFromUser.value,
        author: userId
      })
    })
    if (submitResponse.status == 200){
        window.location = `${domain}/employee?userId=${sessionUserData.userId}`
    } else {
      let messageElem = document.getElementById("error-message");
      messageElem.innerText = "Error was encountered!";
    }
  }

}

// Add new entries to the reibursement file. Some are default values.
async function setReimbursementData(sessionUserData) {

  // Initializing values
  inputReimbId.value = ""; // no value yet
  inputFileOwner.value = sessionUserData.userId; //set to owner userId by default
  inputDateSubmitted.value = setCurrentDate(); // current date
  inputReimbStatus.value = statuses.VALUES[1].description;; // set to pending (1)
  inputDateProcessed.value = ""; // set to null
  inputProcessedBy.value = "";  // set to null

}

// Render the reimbursement info into the form
async function getReimbursementData(sessionUserData, reimbId) {

  // Get a specific reimbursement file.
  const reimbursementDataResponse = await fetch(`${domain}/api/reimbursement?userId=${sessionUserData.userId}&reimbId=${reimbId}`)
  const reimbursementData = reimbursementDataResponse.json();

  reimbursementData.then( function renderValue(element){ 

    inputReimbId.value = element.reimbId;
    inputFileOwner.value = element.author;
    inputReimbType.value = types.VALUES[element.typeId].description;
    inputDescription.value = element.description;
    inputAmount.value = element.amount;
    inputDateSubmitted.value = convertUnixTimestampToUTCDate(element.dateSubmitted);    
    inputReimbStatus.value = statuses.VALUES[element.statusId].description;
    inputDateProcessed.value = element.dateResolved;
    inputProcessedBy.value = element.resolver;
    
    // All resolved reimbursements are not editable anymore.
    if (element.statusId == 1){
      document.getElementById('edit-btn').setAttribute("class", "btn btn-light show");
    }
  })    

}

function convertUnixTimestampToUTCDate(inputDate){

  let newDate = new Date(inputDate)
  return newDate.toDateString();
}

function setCurrentDate(){
  const timeElapsed = Date.now();
  const today = new Date(timeElapsed);
   
  return today.toDateString();
}