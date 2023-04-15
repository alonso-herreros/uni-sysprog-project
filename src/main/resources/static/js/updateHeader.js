
$(document).ready(() => {
   selectNavbarPage()
});


function selectNavbarPage() {
  $('a[href="' + location.pathname + '"]').addClass('active');
}
