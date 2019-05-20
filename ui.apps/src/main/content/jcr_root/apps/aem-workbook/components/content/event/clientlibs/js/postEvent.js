const postEvent = (url = '', data = {}) => {
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    dataType: 'json',
    url,
    data,
  });
};
