import {
  populateTable
} from "./populate-table.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"

var list


$(document).ready(async ()=> {
  await populateList(list)
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

