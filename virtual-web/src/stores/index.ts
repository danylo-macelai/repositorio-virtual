/*
 * Description: FIXME: Document this type
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 8:39:08 PM
 * version $
 */

import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';

import reducers from '../reducers';

export const virtualWebStore = createStore(reducers, applyMiddleware(thunk));
