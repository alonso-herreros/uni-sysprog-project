
$(document).ready(() => {
   selectNavbarPage()
});


function selectNavbarPage() {
  var path = location.pathname
  if (path == "/")  path = "/home"
  $('a[href="' + path + '"]').addClass('active');
}
