export {
  showErrorToast,
  showVarToast
}


var toastMessages = {
  "newStore": "Store created successfully!",
}


document.addEventListener("DOMContentLoaded", function() {
  var feedback = new URLSearchParams(window.location.search).get("feedback")

  if (Object.keys(toastMessages).includes(feedback)) {
    showVarToast(toastMessages[feedback])
  }
})


function showVarToast(message) {
  var variableToast = document.getElementById("variableToast")

  hideVarToast(variableToast)
  variableToast.innerHTML = message
  variableToast.classList.add("show")

  setTimeout(hideVarToast, 5000, variableToast)
}

function hideVarToast(variableToast) {
  variableToast.classList.remove("show")
}


function showErrorToast(serverMessage) {
  showVarToast("Server said: " + serverMessage)
}
