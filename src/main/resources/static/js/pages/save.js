import {
  parseJsonResponse,
} from "../lib/comms.js"
import {
  showVarToast,
  showErrorToast
} from "../pages/toasts.js"

$(document).ready( () => {
  $("#save-button")[0].addEventListener("click", saveStore)
})

function saveStore(event) {
  const saveButton = event.target
  
  saveButton.disabled = true

  requestSave().then((response) => {
    saveButton.disabled = false
  })
}

async function requestSave() {  
  return await fetch('/store/save', {
    method: 'POST'
  }).then( (response) =>
    processSaveResponse(response)
  )
}


async function processSaveResponse(response) {
  const [status, resp] = await parseJsonResponse(response)

  switch (status) {
    case 200: // Saved
      showVarToast("Store '" + resp.storeName + "' saved.")
      return true
      // Fall through. Don't know how you could get here.
    default: // Includes 500 Internal Server Error and false from bad JSON
      showErrorToast(resp.message)
  }
  return true
}
