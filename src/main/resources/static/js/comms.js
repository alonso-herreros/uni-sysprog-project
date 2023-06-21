export {
  parseJsonResponse,
  showErrorToast
}

/**
 * Interprets a server response, expected to be JSON.
 * 
 * @param {Response} response Server response
 * @returns [status, response] where status is the HTTP status code
 * and response is the JSON response. If the response is not valid JSON,
 * status is false and response is {message: "Server sent invalid JSON: " + respRaw}
 */
async function parseJsonResponse(response) {
  const status = response.status
  const respRaw = await response.text()

  try {
    return [status, JSON.parse(respRaw)]
  }
  catch (e) {
    return [false, {message: "Server sent invalid JSON: " + respRaw}]
  }
}

function showErrorToast(serverMessage) {
  showVarToast("Server said: " + serverMessage)
}
