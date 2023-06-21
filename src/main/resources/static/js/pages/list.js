import populateTable from "../lib/populateTable.js"
import {
  fetchJson
} from "../lib/comms.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list
var selectedElementID


$(document).ready(async ()=> {
  await populateList(list)
  $(".open-button").on("click", openDetailsModal)
  $(".close-button").on("click", () => toggleSideMenu($("#side-menu"), false))
})


async function fetchListObject(context) {
  [, list] = await fetchJson(`${STORE_PATH}/${context}${LIST_PATHEXT}`)
  return list
}


async function populateList() {
  if (!list)  await fetchListObject(CONTEXT.name)
  if (!list.length)  list = Object.values(list) // Convert object to array
  populateTable(
    $(".context-table")[0],
    list,
    LIST_CONFIG["fields"],
    LIST_CONFIG["meta"]
  )
}

function openDetailsModal(event) {
  if (!list)  throw "List not loaded."

  const button = event.target
  const selectedRow = button.closest("tr");
  const sideMenu = $("#side-menu")
  const elementId = selectedRow.dataset["elementID"]
  if (elementId == selectedElementID) {
    toggleSideMenu(sideMenu, false)
    selectedElementID = null
    return
  }

  toggleSideMenu(sideMenu, true)

  selectedElementID = elementId
  const element = list.filter((element) => (element["productID"] == elementId))[0]
  console.log(element)
  populateDetailsForm($(".details-form:first", sideMenu)[0], element, EDIT_CONFIG)
}

function toggleSideMenu(sideMenu, show) {
  show = (show!=null && show) || sideMenu.hasClass("hide")
  show = !sideMenu.toggleClass("hide", !show).hasClass("hide")
  setTimeout(() =>
    toggleMenuContent($(".content", sideMenu), show),
    250
  )
  return show
}

function toggleMenuContent(menuContent, show) {
  menuContent.toggleClass("hide", !show)
  setTimeout(() => menuContent.toggleClass("show", show), 1)
}
