const postEvent = (url = '', data = {}, success, error) => {
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    dataType: 'json',
    url,
    data,
    success,
    error,
  });
};