/*
 * Description: User
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 8:23:51 PM
 * version $
 */

export default interface User {
  id: string;
  nome: string;
  email: string;
  token: string;
  perfilType: string;
}

export const EMPTY_USER: User = {
  id: '',
  nome: '',
  email: '',
  token: '',
  perfilType: '',
};
