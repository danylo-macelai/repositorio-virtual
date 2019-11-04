/*
 * Description: Actions relacionadas ao controle de usuario
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 8:10:26 PM
 * version $
 */

import { ThunkAction } from 'redux-thunk';
import { Action } from 'redux';

import { VirtualWebState } from '../reducers';
import User, { EMPTY_USER } from '../common/models/user';
import { getUserByToken, validaToken } from '../services/servicosApi';

const localStorage = window.localStorage;

interface SetUserAction {
  type: string;
  payload: User;
}

interface GetUserAction {
  type: string;
  payload: User;
}

interface ClearUserAction {
  type: string;
  payload: User;
}

export type UserActionType = SetUserAction | GetUserAction | ClearUserAction;

class UserAction {
  static setUserByToken(
    token: string
  ): ThunkAction<any, VirtualWebState, null, Action<string>> {
    return async dispatch => {
      // Pega o usuario pelo token
      const user = await getUserByToken(token);

      // Seta o usuario no localStorage
      localStorage.setItem('user', JSON.stringify(user));

      dispatch({
        type: 'SET_USER',
        payload: user,
      });
    };
  }

  static setUserFromStorage(): ThunkAction<
    any,
    VirtualWebState,
    null,
    Action<string>
  > {
    return async dispatch => {
      // Pega o usuario do localStorage
      const user: any = JSON.parse(localStorage.getItem('user') || '{}');

      // Valida o token
      const response = await validaToken(user.token);

      // Se o token for valido o usuario eh retornado, caso contrario ele eh removido do localStorage
      if (!response) {
        localStorage.removeItem('user');
      }

      dispatch({
        type: 'GET_USER',
        payload: response ? user : EMPTY_USER,
      });
    };
  }

  static clearUser(): UserActionType {
    // Remove o usuario do localStorage
    localStorage.removeItem('user');

    return {
      type: 'CLEAR_USER',
      payload: EMPTY_USER,
    };
  }
}

export default UserAction;
