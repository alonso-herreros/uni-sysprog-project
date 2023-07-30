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
  const subFields = fieldDesc.subFields

  fieldContainer.appendChild(buildFieldLabel(fieldName, fieldDesc.title))

  for (let i = 0; i < subFields.length; i++) {
    const subField = subFields[i]
    const fieldInput = buildFieldInput(element, fieldName, subField)
    if (i == 0)  fieldInput.classList.add("first")
    if (i == subFields.length-1)  fieldInput.classList.add("last")
    fieldContainer.appendChild(fieldInput)
  }

  return fieldContainer
}

function buildFieldLabel(fieldName, title) {
  const label = $("<label>").attr("for", fieldName).text(title)
  return label[0]
}

function buildFieldInput(element, fieldName, fieldDesc) {
  const input = $("<input>")
  input.prop("readonly", true)
  input.attr("type", fieldDesc.type || "text")
  input.attr("name", fieldName + (fieldDesc.name? "."+fieldDesc.name : ""))
  input.attr("value", getContent(element, fieldDesc.content))
  if (fieldDesc.class)  input.addClass(fieldDesc.class)
  return input[0]
}


function findElementByID(list, id, editConfig) {
  if (!list[0][editConfig.elementID])  throw "Can't determine element ID"
  for (const element of list) {
    if (element[editConfig.elementID] == id)  return element
  }
  return null
}
