const URL = '/bin/event';
const BUTTON_ID = 'eventSave';

const formProcessing = () => {
  const data = getJsonFormInputs();
  postEvent(URL, data);
};

document.getElementById(BUTTON_ID).addEventListener('click', formProcessing);