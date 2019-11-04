/*
 * Description: Redenrizacao inicial do projeto
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Fri Jul 26 2019 9:41:00 PM
 * version $
 */

import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import { virtualWebStore } from './stores';

import App from './App';

ReactDOM.render(
  <Provider store={virtualWebStore}>
    <App />
  </Provider>,
  document.querySelector('#root')
);
