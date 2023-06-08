window.onload = populateTable


async function fetchTableCols() {
  return fetch("/var/tableCols.json").then( (response) =>
    response.json()
  ).then( (allOptions) =>
    allOptions[LIST_NAME]
  )
}


async function fetchListObject() {
  return fetch("list/listObject").then( (response) =>
    response.json()
  ).then( (listObject) =>
    listObject.list
  )
}


async function populateTable() {
  var tableCols = await fetchTableCols()
  populateTableHead(tableCols)

  var listObject = await fetchListObject()
  populateTableBody(listObject, Object.keys(tableCols))
}

function populateTableHead(tableCols) {
  var tableHead = document.getElementById("element-table-head")
  tableHead.innerHTML = ""

  tableHead.appendChild(
    newRowByIteration(Object.keys(tableCols), (key) =>
      newTHCell(tableCols[key], key)
    )
  )
}

function populateTableBody(listObject, tableColNames) {
  var tableBody = document.getElementById("element-table-body")
  tableBody.innerHTML = ""

  listObject.forEach((item) => {
    tableBody.appendChild(
      newRowByIteration( tableColNames, (key) => {
        if (key == "detailsButton") return newButtonCell()
        return newCell(item[key])
      })
    )
  })
}


function newRowByIteration(iterable, executable) {
  var row = document.createElement("tr")
  iterable.forEach( (item) => {
    row.appendChild(executable(item))
  })
  return row
}

function newCell(content) {
  return newElement("td", content)
}
function newTHCell(content) {
  return newElement("td", content)
}
function newTHCell(content, className) {
  var cell = newElement("th", content)
  cell.setAttribute("class", className)
  return cell
}

// Add an ID parameter to make the button unique to its row
function newButtonCell() {
  var cell = document.createElement("td")
  var button = document.createElement("button")
  button.setAttribute("type", "button")
  button.setAttribute("class", "open-button")
  button.innerHTML = '<i class="fas fa-xl fa-circle-info"></i>'
  cell.appendChild(button)
  return cell
}

function newElement(tag, content) {
  var element = document.createElement(tag)
  element.textContent = content
  return element
}
