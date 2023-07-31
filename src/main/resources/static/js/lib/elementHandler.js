export {
  getContent,
  getContentPiece
}


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
 */


function getContent(element, contentDesc, split=false) {
  var content = []
  for (const pieceDescriptor of contentDesc) {
    content.push(getContentPiece(element, pieceDescriptor, split))
  }
  return split? content : content.join("")
}


/**
 * Extracts the content piece described by the `pieceDesc` object from the
 * given `element`, applying the `format` attribute if present.
 * 
 * @param {Object} element the element to extract the content piece from
 * @param {ContentPieceDescriptor} pieceDesc the descriptor of the content piece to extract
 * @returns {String} the content piece described by the `pieceDesc` object
 */
function getContentPiece(element, pieceDesc, split=false) {
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
  return formatData(piece, pieceDesc.format, split)
}

/**
 * Formats the given `data` according to the given `format` object.
 *
 * @param {any} data the data to format
 * @param {FormatDescriptor} format the format to apply to the data
 * @returns {String} the formatted data
 */
function formatData(data, format, split=false) {
  if (!format)  return split? ["",String(data),""] : String(data)
  switch (format.type) {
    case "float":
      data = data.toFixed(format.decimalPlaces || 2)
      break
    case "int":
      if (format.digits) {
        var d = format.digits, c = format.pad || "0"
        data = "" + data
        data = (format.align == "left") ? data.padEnd(d,c) :  data.padStart(d,c)
      }
      break
  }
  if (split)  return [format.prefix?format.prefix:"", data, format.suffix?format.suffix:""]
  if (format.prefix)  data = format.prefix + data
  if (format.suffix)  data = data + format.suffix
  return String(data)
}
