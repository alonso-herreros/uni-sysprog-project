export {
  populateTable as default
}

import {
  getContent,
  getContentPiece
} from "./elementHandler.js"
import {
  newElement,
  addContent,
  setClass
} from "./htmlBuilder.js"



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
function populateTable(table, list, fields, meta) {
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


function setMeta(row, element, meta) {
  for (const [fieldName, fieldContent] of Object.entries(meta)) {
    row.dataset[fieldName] = getContentPiece(element, fieldContent)
  }
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

  return newCell(getContent(element, field.content))
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

function newButtonCell(buttonClass="open-button", content) {
  const cell = newElement("td")

  const button = newElement("button")
  addContent(button, content || newElement("i", "", "fas fa-xl fa-circle-info"))
  setClass(button, buttonClass)
  cell.appendChild(button)

  return cell
}
