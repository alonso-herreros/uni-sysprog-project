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


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list
var selectedElementID
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
    if (editEnabled)  disableEdit(sideMenu)
  })
  $(".save-button").on("click", () => {
    if (selectedElementID && editEnabled)  save(sideMenu)
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
  populateDetailsForm(
    $(".details-form:first", sideMenu)[0],
    findElementByID(list, elementID, EDIT_CONFIG),
    EDIT_CONFIG
  )
}

function closeDetailsModal(sideMenu) {
  toggleSideMenuVis(sideMenu, false)
  disableEdit(sideMenu)
  selectedElementID = null
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
      $(`input[name="${fieldName}${subField.name? "."+subField.name : ""}"]`, form).prop("readOnly", false)
    }
  }
  sideMenu.addClass("edit-mode")

  document.addEventListener("keydown", function(event) {
    if(event.key == "Escape") {
        disableEdit(sideMenu)
    }
  });

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
// #endregion

