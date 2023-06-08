window.onload = populateTable


async function fetchListObject() {
  return fetch("list/listObject").then( (response) =>
    response.json()
  )
}


async function populateTable() {
  populateTableHead(TABLE_COLS)

  var listObject = await fetchListObject()
  populateTableBody(listObject, TABLE_COLS)
}

function populateTableHead(tableCols) {
  var tableHead = document.getElementById("element-table-head")
  tableHead.innerHTML = ""

  tableHead.appendChild(
    newRowByIteration(tableCols, (col) =>
      newTHCell(col.title, col.class)
    )
  )
}

function populateTableBody(listObject, tableCols) {
  var tableBody = document.getElementById("element-table-body")
  tableBody.innerHTML = ""

  for (const element of listObject) {
    tableBody.appendChild(
      newRowByIteration(tableCols, (col) => getColumnContent(element, col))
    )
  }
}


function getColumnContent(element, column) {
  if (column.class == "detailsButton") return newButtonCell()
  
  var cellContent = ""
  for (const pieceDescriptor of column.content) {
    cellContent += getContentPiece(element, pieceDescriptor)
  }

  return newCell(cellContent)
}

function getContentPiece(element, pieceDesc) {
  var piece = ""
  switch (pieceDesc.type) {
    case "attribute":
      piece = element
      for (const attr of pieceDesc.attributePath)  piece = piece[attr]
      break
    case "value":
      piece = pieceDesc.value
      break
  }
  piece = formatData(piece, pieceDesc.format)
  return piece
}

function formatData(data, format) {
  if (!format) return data
  switch (format.type) {
    case "float":
      data = data.toFixed(format.decimalPlaces || 2)
  }
  if (format.prefix)  data = format.prefix + data
  if (format.suffix)  data = data + format.suffix
  return data
}


function newRowByIteration(iterable, executable) {
  var row = document.createElement("tr")
  for (const item of iterable)  row.appendChild(executable(item))
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

// TODO: Add an ID parameter to make the button unique to its row
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
