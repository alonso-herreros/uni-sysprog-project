import populateTable from "../lib/populateTable.js"
import {
  populateDetailsForm,
  findElementByID
 } from "../lib/populateEditMenu.js"
import {
  fetchJson,
  parseJsonResponse
} from "../lib/comms.js"
import {
  showVarToast,
  showErrorToast
} from "../pages/toasts.js"
import { getContent } from "../lib/elementHandler.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list
var selectedElementID
var selectedElement
var editEnabled = false


$(document).ready(async ()=> {
  await populateList(list)
  
  var sideMenu = $("#side-menu")

  $(".open-button").on("click", (e) =>
    toggleDetailsModal(sideMenu, e.target.closest("tr").dataset.elementID)
  )
  $(".close-button").on("click", ()=>
    closeDetailsModal(sideMenu)
  )
  $(".edit-button").on("click", () => {
    if (selectedElementID && !editEnabled)  enableEdit(sideMenu)
  })
  $(".cancel-button").on("click", () => {
    if (editEnabled)  cancelEdit(sideMenu)
  })
  $(".save-button").on("click", () => {
    if (selectedElementID && editEnabled)  saveEdit(sideMenu)
  })
})


async function fetchListObject() {
  [, list] = await fetchJson(`${STORE_PATH}/${CONTEXT.name}${LIST_PATHEXT}`)
  return list
}


async function populateList() {
  if (!list)  await fetchListObject()
  if (!list.length)  list = Object.values(list) // Convert object to array
  populateTable(
    $(".context-table")[0],
    list,
    LIST_CONFIG["fields"],
    LIST_CONFIG["meta"]
  )
}


// #region Side menu open/close
function toggleDetailsModal(sideMenu, elementID) {
  if (elementID == selectedElementID)  closeDetailsModal(sideMenu)
  else openDetailsModal(sideMenu, elementID)
}

function openDetailsModal(sideMenu, elementID) {
  toggleSideMenuVis(sideMenu, true)
  selectedElementID = elementID
  selectedElement = findElementByID(list, elementID, EDIT_CONFIG)
  populateDetailsForm(
    $(".details-form:first", sideMenu)[0],
    selectedElement,
    EDIT_CONFIG
  )
}

function closeDetailsModal(sideMenu) {
  toggleSideMenuVis(sideMenu, false)
  disableEdit(sideMenu)
  selectedElementID = null
  selectedElement = null
}

function toggleSideMenuVis(sideMenu, show) {
  show = (show!=null && show) || sideMenu.hasClass("hide")
  show = !sideMenu.toggleClass("hide", !show).hasClass("hide")
  setTimeout(() =>
    toggleMenuContent($(".content", sideMenu), show),
    300
  )
  return show
}

function toggleMenuContent(menuContent, show) {
  menuContent.toggleClass("hide", !show)
  setTimeout(() => menuContent.toggleClass("show", show), 1)
}
// #endregion


// #region Side menu edit enable/disable
function enableEdit(sideMenu) {
  const form = $(".details-form:first", sideMenu)[0]

  for (const [fieldName, field] of Object.entries(EDIT_CONFIG.fields)) {
    for (const subField of field.subFields) {
      if (!subField.set || subField.set == "false")  continue
      enableField(fieldName, field, subField)
    }
  }
  sideMenu.addClass("edit-mode")

  document.addEventListener("keydown", function(event) {
    if(event.key == "Escape")  cancelEdit(sideMenu)
  })

  editEnabled = true
}

function disableEdit(sideMenu) {
  const form = $(".details-form:first", sideMenu)[0]
  
  for (const input of $("input", form)) {
    input.readOnly = true
  }
  sideMenu.removeClass("edit-mode")

  editEnabled = false
}

function enableField(fieldName, field, subField) {
  const input = $(`input[name="${fieldName}${subField.name? "."+subField.name : ""}"]`)
  if (subField.set == "simpleAttribute") {
    var [prefix, value, suffix] = getContent(selectedElement, subField.content, true)[0]
    input.val(value)
    input.before(prefix)
    input.after(suffix)
    input.prop("readonly", false)
  }
}

function cancelEdit(sideMenu) {
  if (editEnabled) {
    disableEdit(sideMenu)
    populateDetailsForm(
      $(".details-form:first", sideMenu)[0],
      selectedElement,
      EDIT_CONFIG
    )
  }
}
// #endregion

// #region Side menu save
async function saveEdit(sideMenu) {
  const changes = buildEditChanges(sideMenu)
  if (changes == false) {
    showVarToast("Invalid input")
    return false
  }
}

function buildEditChanges(sideMenu) {
  var changes = []

  for (const [name, field] of Object.entries(EDIT_CONFIG.fields)) {
    for (const subField of field.subFields) {
      if (!subField.set || subField.set == "false")  continue
      const input = $(`input[name="${name}${subField.name? "."+subField.name : ""}"]`, sideMenu)
      if (!validateInput(subField.validation, input)) {
        showInvalidInput(subField.validation, input)
        return false
      }
      var change = buildChange(subField, input)
      if (change)  changes.push(change)
    }
  }
  return changes
}

function validateInput(validation, input) {
  if (!validation)  return true

  const value = input.val()
  switch (validation.type) {
    case "int":
      if (!Number.isInteger(Number(value)))  return false
      // Fallthrough
    case "number":
    case "float":
      if (Number.isNaN(Number(value)))  return false
      if (validation.min!=undefined && value < validation.min)  return false
      if (validation.max!=undefined && value > validation.max)  return false
      break
    case "regex":
      if (!new RegExp(validation.pattern).test(value))  return false
    case "text":
    case "string":
      if (validation.minLength!=undefined && value.length < validation.min)  return false
      if (validation.maxLength!=undefined && value.length > validation.max)  return false
      break
    case "bool":
    case "boolean":
      if (value != "true" && value != "false")  return false
      break
  }
  return true
}
function showInvalidInput(validation, input) {
  input.addClass("invalid")
  if (validation.hint) {
    //TODO: Show hint
  }
}

function buildChange(subField, input) {
  switch(subField.set) {
  case "simpleAttribute":
    if (subField.content.length != 1 || subField.content[0].attributePath.length !=1) 
      throw "Error in edit config: wrong structure for set mode 'simpleAttribute'"
    return {
      "mode": "elementAttribute",
      "attribute": subField.content[0].attributePath[0],
      "value": input.val()
    }
  }
  return false
}

