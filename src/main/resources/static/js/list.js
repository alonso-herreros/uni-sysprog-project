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
  populateTable(
    $(".element-table")[0],
    TABLE_COLS,
    await fetchListObject(VARIANT.name) // They will deal with the dict
  )
}


async function fetchListObject(context) {
  const listPath = `${STORE_PATH}/${context}${LIST_PATHEXT}`
  return fetch(listPath).then( (response) =>
    response.json()
  )
}

