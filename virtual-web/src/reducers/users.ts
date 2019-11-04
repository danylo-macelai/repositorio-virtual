/*
 * Description: Reducers relacionadas ao controle de usuario
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 8:09:59 PM
 * version $
 */

import User, { EMPTY_USER } from '../common/models/user';
import { UserActionType } from '../actions/users';

class UserReducer {
  static user(user: User = EMPTY_USER, action: UserActionType): User {
    if (!action.payload) {
      return user;
    }

    if (action.payload) {
      return action.payload;
    }

    return user;
  }
}

export default UserReducer;
