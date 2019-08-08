/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 13 de mai de 2019
 * version $
 */

import { Server } from 'http';

import { Auth } from '../interfaces/AuthInterface';
import { UsuarioModel } from '../models/UsuarioModel';
import PerfilType from '../enums/PerfilType';

export const normalizePort = (
  val: number | string
): number | string | boolean => {
  const port: number = typeof val === 'string' ? parseInt(val) : val;
  if (isNaN(port)) return val;
  else if (port >= 0) return port;
  else return false;
};

export const onError = (server: Server) => {
  return (error: NodeJS.ErrnoException): void => {
    const addr = server.address();
    if (error.syscall !== 'listen') throw error;
    const bind =
      typeof addr === 'string' ? `pipe ${addr}` : `port ${addr.port}`;
    switch (error.code) {
      case 'EACCES':
        console.error(`${bind} requires elevated privileges`);
        process.exit(1);
        break;
      case 'EADDRINUSE':
        console.error(`${bind} is already in use`);
        process.exit(1);
        break;
      default:
        throw error;
    }
  };
};

export const onListening = (server: Server) => {
  return (): void => {
    const addr = server.address();
    const bind =
      typeof addr === 'string' ? `pipe ${addr}` : `port ${addr.port}`;
    console.log(`Listening at ${bind}...`);
  };
};

export const handleError = (error: Error) => {
  const errorMessage: string = `${error.name}: ${error.message}`;
  console.log(errorMessage);
  return Promise.reject(new Error(errorMessage));
};

export const throwError = (condition: boolean, message: string): void => {
  if (condition) {
    throw new Error(message);
  }
};

export const isAutenticado = (auth: Auth): void => {
  throwError(
    auth === undefined,
    `Acesso negado: Usuário não autenticado ou token inválido!`
  );
};

export const isAdminstrador = (auth: Auth): boolean => {
  isAutenticado(auth);

  throwError(
    auth.alterarSenha,
    `Acesso negado: É necessário alterar a sua senha.`
  );

  return auth.perfilType === PerfilType.Adminstrador;
};

export const isUsuarioOuAdminstrador = (
  autor: UsuarioModel,
  auth: Auth
): boolean => {
  return isAdminstrador(auth) || autor.get('id') === auth.id;
};
