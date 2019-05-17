const getJsonFormInputs = () => {
  const json = {};
  const form = document.querySelectorAll('.event-wrapper input');
  for (let i = 0; i < form.length; i++) {
    const el = form[i];
    json[el.name] = el.value;
  }
  return JSON.stringify(json);
};