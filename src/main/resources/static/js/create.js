
window.onload = () => {
  // Can't simply use on("submit", function) because it hides the submitter
  // Either of these work.
  //$("#create-form").on("submit", (e) => sendCreateRequest(e.originalEvent))
  $("#create-form")[0].addEventListener("submit", submitCreateForm)
  $("#create-form")[0].addEventListener("reset", cancel)
}

function submitCreateForm(event) {
  event.preventDefault()

  const form = event.target
  disableForm(form)

  sendCreateForm(form, event.submitter.value || "create").then((response) => {
    if (response)  enableForm(form)
  })
}

function sendCreateForm(form, action) {
  var force
  switch(action) {
    case "create-f":
      force = true
    case "create":
      return sendCreateRequest(form, force)
    case "load":
      return sendLoadRequest(form)
    default:
      return "Unknown form action: " + action
  }
}


async function sendCreateRequest(form, force=false) {
  const formData = new FormData(form)
  formData.append("force", force)
  
  return await fetch('/store/create', {
    method: 'POST',
    body: formData
  }).then( (response) =>
    processCreateResponse(form, response)
  )
}

async function sendLoadRequest(form) {
  return await fetch('/store/load', {
    method: 'POST',
    body: new FormData(form)
  }).then( (response) =>
    processLoadResponse(form, response)
  )
}


/**
 * 
 * @param {*} form The form that contains the sent data
 * @param {Response} response The response from the server
 * @returns Whether the request is resolved and the form should be enabled
 */
async function processCreateResponse(form, response) {
  const [status, resp] = await preprocessResponse(response)

  switch (status) {
    case 201: // Created
      showVarToast("Store '" + resp.storeName + "' created.")
      return true
    case 300: // Store already exists (probably)
      if (resp.errCode == "STORE_EXISTS") {
        showLoadPopup(form, resp.storeName)
        return false
      }
      // Fall through. Don't know how you could get here.
    default: // Includes 500: Internal server error and false from bad JSON
      showErrorToast(resp.message)
  }
  return true
}

/**
 * 
 * @param {*} form The form that contains the sent data
 * @param {Response} response The response from the server
 * @returns Whether the request is resolved and the form should be enabled
 */
async function processLoadResponse(form, response) {
  const [status, resp] = await preprocessResponse(response)

  switch (status) {
    case 200: // Loaded
      showVarToast("Store '" + resp.storeName + "' loaded.")
      return true
    case 404: // Store not found
      if (resp.errCode == "STORE_NOT_FOUND") {
        showVarToast("Store not found. Try creating it first.")
        return true
      }
      // Fall through. Don't know how you could get here.
    default: // Includes 500: Internal server error and false from bad JSON
      showErrorToast(resp.message)
  }
  return true
}


async function preprocessResponse(response, log=false) {
  const status = response.status
  const respRaw = await response.text()
  if (log)  console.log("Response: " + status + " " + respRaw)

  try {
    return [status, JSON.parse(respRaw)]
  }
  catch (e) {
    return [false, {message: "Server sent invalid JSON: " + respRaw}]
  }
}


function cancel(event) {
  event.preventDefault()
  const form = event.target
  hideLoadPopup(form)
  enableForm(form)
}


function disableForm(form) {
  form.classList.add('processing')
  form.disabled = true
  $("#submit-buttons button", form).prop("disabled", true)
}
function enableForm(form) {
  hideLoadPopup(form)
  form.classList.remove('processing')
  form.disabled = false
  $("#submit-buttons button", form).prop("disabled", false)
}

function showLoadPopup(form, storeName) {
  $("#popup #var-storeName")[0].innerText = storeName
  $("#popup", form)[0].classList.add("show")
}
function hideLoadPopup(form) {
  $("#popup", form)[0].classList.remove("show")
}

function showErrorToast(serverMessage) {
  showVarToast("Server said: " + serverMessage)
  return true
}