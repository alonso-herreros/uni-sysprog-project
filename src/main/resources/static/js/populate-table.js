
/** // Types
 * @typedef {Object} FormatDescriptor
 * @prop {string} [type="string"] The type of content to be formatted (e.g. `"float"`). Defaults to string.
 * @prop {number} [decimalPlaces=2] The number of decimal places to round the content to (if type is `"float"`).
 * @prop {string} [prefix] The prefix to add to the content
 * @prop {string} [suffix] The suffix to add to the content
 * 
 * @typedef {Object} ContentPieceDescriptor
 * @prop {string} type The type of content piece
 * @prop {string} [value] The value of the content piece (if type is `"value"`)
 * @prop {string[]} [attributePath] The .get(varId) path to the desired attribute (if type is `"attribute"`)
 * @prop {FormatDescriptor} [format] The format to apply to the content
 * 
 * @typedef {Object} TableField
 * @prop {string} title The title of the column
 * @prop {ContentPieceDescriptor[]} [content] The content of the column (unless `class` is `"detailsButton"`)
 * @prop {string} [class] The class of the column
 */


/**
 * Populates the given `table` with the given `list` object according to the
 * given `fields` array.
 * 
 * @param {HTMLTableElement} table the `<table>` element to populate. Will be emptied.
 * @param {Object[]} list the list object to populate the table with
 * @param {TableField[]} fields the fields to include in the table
 * @param {Object} [meta] the the metadata fields to include in each row
 * 
 * @returns {HTMLTableElement} The `<table>` element populated
 */
export function populateTable(table, list, fields, meta) {
  if (!table || !list || !fields)  throw "Missing critical arguments"

  table.innerHTML = ""
  const thead = table.appendChild(newElement("thead"))
  const tbody = table.appendChild(newElement("tbody"))

  populateTableHead(thead, fields)
  populateTableBody(tbody, list, fields, meta)

  return table
}


/**
 * Populates the given table head with the fields provided. There will be as
 * many column headers as elements in the `fields` array, with the `title`
 * and `class` from each field element.
 * 
 * @param {HTMLTableSectionElement} tableHead the `<thead>` table head to populate
 * @param {Array} fields the fields to include in the table head
 * 
 * @returns {HTMLTableSectionElement} The `<thead>` element populated
 */
function populateTableHead(tableHead, fields) {
  tableHead.appendChild(
    newRowByIteration(fields, (field) => newTHCell(field.title, field.class))
  )
  return tableHead
}

/**
 * Populates the given table body with the list provided. Each row will contain
 * as many cells as elements in the `fields` array, each cell with the content
 * described in the `content` attribute of the field element. 
 * 
 * @param {HTMLTableSectionElement} tableBody the `<tbody>` table body to populate
 * @param {Object[]} list the list to populate the table with
 * @param {TableField[]} fields the fields to include in the table
 * @param {Object} [meta] the the metadata fields to include
 * 
 * @returns {HTMLTableSectionElement} The `<tbody>` element populated
 */
function populateTableBody(tableBody, list, fields, meta) {
  for (const element of list) {
    const row = newRowByIteration(fields, (field) => buildContentCell(element, field))
    if (meta)  setMeta(row, element, meta)
    tableBody.appendChild(row)
  }
  return tableBody
}


/**
 * Creates a cell with the element's field as described in the `content`
 * attribute of the `field`. If the field is a `detailsButton`, the cell
 * will contain an appropriate button.
 * 
 * @param {Object} element the element to extract the field from
 * @param {TableField} field the field to extract the content from
 * @returns {HTMLTableCellElement} The `<td>` cell element created
 */
function buildContentCell(element, field) {
  if (field.class == "detailsButton") return newButtonCell()
  if (!field.content)  throw "Missing content"
  
  var cellContent = ""
  for (const pieceDescriptor of field.content) {
    cellContent += getContentPiece(element, pieceDescriptor)
  }

  return newCell(cellContent)
}

/**
 * Extracts the content piece described by the `pieceDesc` object from the
 * given `element`, applying the `format` attribute if present.
 * 
 * @param {Object} element the element to extract the content piece from
 * @param {ContentPieceDescriptor} pieceDesc the descriptor of the content piece to extract
 * @returns {String} the content piece described by the `pieceDesc` object
 */
function getContentPiece(element, pieceDesc) {
  var piece = ""
  switch (pieceDesc.type) {
    case "attribute":
      piece = element
      for (const attr of pieceDesc.attributePath)  piece = piece[attr]
      break
    case "const":
      piece = pieceDesc.value || ""
      break
  }
  if (pieceDesc.format)  piece = formatData(piece, pieceDesc.format)
  return piece
}

/**
 * Formats the given `data` according to the given `format` object.
 *
 * @param {any} data the data to format
 * @param {FormatDescriptor} format the format to apply to the data
 * @returns {String} the formatted data
 */
function formatData(data, format) {
  switch (format.type) {
    case "float":
      data = data.toFixed(format.decimalPlaces || 2)
  }
  if (format.prefix)  data = format.prefix + data
  if (format.suffix)  data = data + format.suffix
  return String(data)
}


function setMeta(row, element, meta) {
  for (const metaField of Object.keys(meta)) {
    row.dataset[metaField] = getContentPiece(element, meta[metaField])
  }
  console.log(row.dataset)
}


/**
 * Creates a new table row by iterating over the given `iterable` and applying
 * the given `executable` to each element. The result of each execution will
 * be appended to the row.
 * 
 * @param {Iterable} iterable The iterable to iterate over
 * @param {Function} executable The function to apply to each element in `iterable`
 * @returns {HTMLTableRowElement} The `<tr>` row element created
 */
function newRowByIteration(iterable, executable) {
  const row = document.createElement("tr")
  for (const item of iterable)  row.appendChild(executable(item))
  return row
}

/**
 * Creates a new cell of type `td` with the given `content`.
 * 
 * @param {any} content Content to place in the cell
 * @returns {HTMLTableCellElement} The `<td>` cell element created
 */
function newCell(content) {
  return newElement("td", content)
}
/**
 * Creates a new cell of type `th` with the given `content` and `className`.
 * 
 * @param {any} content Content to place in the header cell
 * @param {string} [className] Class to add to the header cell
 * @returns {HTMLTableCellElement} The `<th>` header cell element created
 */
function newTHCell(content, className) {
  const cell = newElement("th", content)
  if (className)  cell.classList.add(className)
  return cell
}

// TODO: Add an ID parameter to make the button unique to its row
/**
 * Creates a new cell of type `td` containing a button to open a modal.
 * 
 * @returns {HTMLTableCellElement} The `<td>` cell element created
 */
function newButtonCell(buttonClass="open-button", content) {
  const cell = newElement("td")

  const button = newElement("button")
  addContent(button, content || newElement("i", "", "fas fa-xl fa-circle-info"))
  setClass(button, buttonClass)
  cell.appendChild(button)

  return cell
}

/**
 * Creates a new element of the given `tag` with the given `content`.
 * 
 * @param {String} tag Tag of the element to create
 * @param {Object} [content] The content to place in the element
 * @returns {HTMLElement} The element created
 */
function newElement(tag, content, className) {
  const element = document.createElement(tag)
  if (content)  addContent(element, content)
  if (className)  setClass(element, className)
  return element
}

function addContent(element, content) {
  if (content instanceof Array) {
    content.forEach((piece) => addContent(element, piece))
  }
  else {
    if (content instanceof Node)  addChild(element, content)
    else  element.innerHTML = content
  }
  return element
}


function addChild(parent, child) {
  parent.appendChild(child)
  return parent
}

function setClass(element, classValue) {
  element.setAttribute("class", classValue)
  return element
}

function addClass(element, className) {
  element.classList.add(className)
  return element
}
