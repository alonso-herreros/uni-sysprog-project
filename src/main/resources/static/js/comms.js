export {
  fetchJson,
  parseJsonResponse,
  showErrorToast
}

/** Fetches and interprets a server response, expected to be JSON.
 * 
 * @param {string} url URL to fetch from
 * @param {*} options Fetch options
 * @returns {Promise<[number, Object]>} [status, response] where status is the HTTP status code and
 * response is the JSON response. If the response is not valid JSON,
 * status is false and response is the object
 * ```
 * {status: status, message: `Server sent invalid JSON: ${respRaw}`}
 * ```
 */
async function fetchJson(url, options) {
  return parseJsonResponse(fetch(url, options))
}


/** Interprets a server response, expected to be JSON.
 * 
 * @param {Promise<Response>} response Server response. May be a promise.
 * @returns {Promise<[number, Object]>} [status, response] where status is the HTTP status code
 * and response is the JSON response. If the response is not valid JSON,
 * status is false and response is the object:
 * ```
 * {status: status, message: `Server sent invalid JSON: ${respRaw}`}
 * ```
 */
async function parseJsonResponse(response) {
  response = await response
  const status = response.status
  const respRaw = await response.text()

  try {
    return [status, JSON.parse(respRaw)]
  }
  catch (e) {
    return [false, {status: status, message: `Server sent invalid JSON: ${respRaw}`}]
  }
}

function showErrorToast(serverMessage) {
  showVarToast("Server said: " + serverMessage)
}
