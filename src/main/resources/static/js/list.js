import {
  populateTable
} from "./populate-table.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list


$(document).ready(async ()=> {
  await populateList(list)
  $(".open-button").on("click", openDetailsModal)
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
    TABLE_COLS,
    list
  )
}

function openDetailsModal(event) {
  if (!list)  throw "List not loaded."

  const button = event.target
  const selectedRow = button.closest("tr");
  const sideMenu = $("#side-menu")

  toggleSideMenu(sideMenu)

}

function toggleSideMenu(sideMenu) {
  sideMenu.toggleClass("hide")
  
  const menuContent = $(".content", sideMenu)

  setTimeout(() =>
    toggleMenuContent(menuContent, !sideMenu.hasClass("hide")),
    150)
}

function toggleMenuContent(menuContent, show) {
  menuContent.toggleClass("hide", !show)
  setTimeout(() => menuContent.toggleClass("show", show), 1)
}
