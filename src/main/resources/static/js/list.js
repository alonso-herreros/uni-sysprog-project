import {
  populateTable
} from "./populate-table.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list


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
  if (!list)  await fetchListObject(VARIANT.name)
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

  toggleSideMenu(sideMenu)

  const elementId = 2 //button.getAttribute("data-element-id")
  var element = list.filter((element) => (element.productID == elementId))[0]
  console.log(element)
  //populateSideMenu(sideMenu, element)
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
