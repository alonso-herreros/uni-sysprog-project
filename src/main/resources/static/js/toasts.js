
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
  variableToast.classList.remove("show")
  variableToast.innerHTML = message
  variableToast.classList.add("show")
  // The popup will hide itself, and will be reset on next use.
}
