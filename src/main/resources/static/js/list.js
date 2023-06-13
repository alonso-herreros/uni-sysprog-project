import {
  populateTable
} from "./populate-table.js"


const STORE_PATH = "/store"
const LIST_PATHEXT = "/list-object"
const DETAILS_PATH = "/details"
const COLUMNS_PATHEXT = "/columns"


$(document).ready(async ()=> {
  populateList()
})


async function populateList() {
  var list = await fetchListObject(VARIANT.name)
  if (!list.length)  list = Object.values(list) // Convert object to array
  populateTable(
    $(".element-table")[0],
    TABLE_COLS,
    list
  )
}


async function fetchListObject(context) {
  const listPath = `${STORE_PATH}/${context}${LIST_PATHEXT}`
  return fetch(listPath).then( (response) =>
    response.json()
  )
}

