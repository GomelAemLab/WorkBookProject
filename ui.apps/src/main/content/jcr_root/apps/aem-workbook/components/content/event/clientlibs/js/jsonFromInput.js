const getJsonFormInputs = (parent) => {
  const json = {};
  const form = parent.getElementsByTagName('input');
  for (let i = 0; i < form.length; i++) {
    const el = form[i];
    json[el.name] = el.value;
  }
  return JSON.stringify(json);
};
