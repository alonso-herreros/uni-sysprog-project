export {
  populateDetailsForm,
  findElementByID
}

import {
  getContent
} from "./elementHandler.js"


/**
 * Populates the given `form` with the given `element` object according to the
 * given `menuConfig` object.
 * 
 * @param {HTMLFormElement} form the `<form>` element to populate. Will be emptied.
 * @param {Object} element the object to include in the form
 * @param {Object} menuConfig the UI Config for the edit menu
 * 
 * @returns {HTMLFormElement} The `<form>` element populated
 */
function populateDetailsForm(form, element, menuConfig) {
  if (!form || !element || !menuConfig)
    throw `Missing parameter(s). form: ${form}, element: ${element}, menuConfig: ${menuConfig}`

  form.innerHTML = ""

  for (const [fieldName, fieldDesc] of Object.entries(menuConfig.fields)) {
    form.appendChild(buildSideMenuField(element, fieldName, fieldDesc))
  }

  return form
}


function buildSideMenuField(element, fieldName, fieldDesc) {
  const fieldContainer = $("<div>").addClass("field")[0]

  fieldContainer.appendChild(buildFieldLabel(fieldName, fieldDesc.title))

  fieldContainer.appendChild(buildFieldInput(element, fieldName, fieldDesc))

  return fieldContainer
}

function buildFieldLabel(fieldName, title) {
  const label = $("<label>").attr("for", fieldName).text(title)
  return label[0]
}

function buildFieldInput(element, fieldName, fieldDesc) {
  const input = $("<input>")
  input.attr("type", "text")
  input.attr("name", fieldName)
  input.attr("value", getContent(element, fieldDesc.fields[0].content))
  input.attr("readonly", true)
  return input[0]
}

function setMeta(row, element, meta) {
  for (const [fieldName, fieldContent] of Object.entries(meta)) {
    row.dataset[fieldName] = getContentPiece(element, fieldContent)
  }
}

function findElementByID(list, id, editConfig) {
  if (!list[0][editConfig.elementID])  throw "Can't determine element ID"
  for (const element of list) {
    if (element[editConfig.elementID] == id)  return element
  }
  return null
}
