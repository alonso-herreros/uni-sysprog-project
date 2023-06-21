export {
  newElement,
  addContent,
  addChild,
  addClass,
  setClass
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
