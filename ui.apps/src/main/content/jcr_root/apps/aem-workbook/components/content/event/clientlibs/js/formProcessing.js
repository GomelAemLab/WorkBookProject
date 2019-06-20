const URL = '/bin/event/json';
const BUTTON_CLASS = 'eventSave';
const FORM_SELECTOR = '.event-wrapper';

const getClosestClickDiv = (e, className) => {
  const el = e.target.closest(className);
  return el;
};

const formProcessing = (e) => {
  const data = getJsonFormInputs(getClosestClickDiv(e, FORM_SELECTOR));
  postEvent(URL, data);
};

const buttons = document.getElementsByClassName(BUTTON_CLASS);
Array.from(buttons).forEach(el => el.addEventListener('click', formProcessing));

