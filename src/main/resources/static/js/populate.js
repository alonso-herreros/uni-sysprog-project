window.onload = populateTable;


function populateTable() {
  // Make an AJAX request to retrieve the data from your server
  var xhr = new XMLHttpRequest();
  xhr.open('GET', 'list/listObject', true);
  xhr.onload = function () {
    if (xhr.status === 200) {
      var data = JSON.parse(xhr.responseText);
      
      // Clear the existing table rows
      var tableBody = document.getElementById('element-table-body');
      tableBody.innerHTML = '';

      // Loop through the data and create table rows dynamically
      data.list.forEach(function (item) {
        var row = document.createElement('tr');

        // Create table cells for each data field
        var idCell = document.createElement('td');
        idCell.textContent = item.productID;
        row.appendChild(idCell);

        var nameCell = document.createElement('td');
        nameCell.textContent = item.name;
        row.appendChild(nameCell);

        var brandCell = document.createElement('td');
        brandCell.textContent = item.brand;
        row.appendChild(brandCell);

        var buttonCell = document.createElement('td');
        var button = document.createElement('button');
        button.setAttribute('type', 'button');
        button.setAttribute('class', 'open-button');
        button.innerHTML = '<i class="fas fa-xl fa-circle-info"></i>';
        buttonCell.appendChild(button);
        row.appendChild(buttonCell);

        // Append the row to the table body
        tableBody.appendChild(row);
      });
    }
  };
  xhr.send();
}
