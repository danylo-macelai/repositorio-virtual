/*
 * Description: Reducers
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 8:36:48 PM
 * version $
 */

import { combineReducers } from 'redux';

import UserReducer from './users';

const rootReducer = combineReducers({
  user: UserReducer.user,
});

export type VirtualWebState = ReturnType<typeof rootReducer>;

export default rootReducer;
