$('#myModal').on('show.bs.modal', function (e) {
  var eventId = e.relatedTarget.closest(".card").id;
  $(this).find("input[name=id]").val(eventId);
})
