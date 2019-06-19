$(".card .update").on("click", function(e) {
    var path = $(".events-container").attr("update-path");
    var eventId = e.target.closest(".card").id;
    document.location.assign(path + ".html?" + $.param({ "id": eventId }));
});
