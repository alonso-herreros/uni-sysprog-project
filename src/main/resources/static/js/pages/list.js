import populateTable from "../lib/populateTable.js"
import {
  populateDetailsForm,
  findElementByID
 } from "../lib/populateEditMenu.js"
import {
  fetchJson
} from "../lib/comms.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list
var selectedElementID


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

function toggleDetailsModal(sideMenu, elementID) {
  if (!list)  throw "List not loaded."

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
  selectedElementID = null
}

// #region Side menu open/close
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
