import {
  populateTable,
  newElement
} from "./populate-table.js"


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
  const listPath = `${STORE_PATH}/${context}${LIST_PATHEXT}`
  list = await fetch(listPath).then( (response) =>
    response.json()
  )
  return list
}


async function populateList() {
  if (!list)  await fetchListObject(CONTEXT.name)
  if (!list.length)  list = Object.values(list) // Convert object to array
  populateTable(
    $(".context-table")[0],
    list,
    TABLE_DESC["fields"],
    TABLE_DESC["meta"]
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
  const element = list.filter((element) => (element.productID == elementId))[0]
  console.log(element)
  populateSideMenu($(".content", sideMenu), element)
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

function populateSideMenu(sideMenuContent, element) {
  const detailsForm = $(".details-form", sideMenuContent)[0]
  detailsForm.innerHTML = ""
  for (const [fieldName, fieldValue] of Object.entries(element)) {
    detailsForm.appendChild(buildSideMenuField(fieldName, fieldValue))
  }
}

function buildSideMenuField(fieldName, fieldValue) {
  const label = newElement("label", fieldName)
  label.setAttribute("for", fieldName)
  const input = newElement("input")
  input.setAttribute("type", "text")
  input.setAttribute("name", fieldName)
  input.setAttribute("value", fieldValue)
  input.setAttribute("readonly", true)

  const fieldContainer = newElement("div", null, "field")
  fieldContainer.appendChild(label)
  fieldContainer.appendChild(input)
  return fieldContainer
}
