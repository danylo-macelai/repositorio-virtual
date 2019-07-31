import PerfilType from '../enums/PerfilType';
import { UsuarioModel } from '../models/UsuarioModel';

/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 31 de jul de 2019
 * version $
 */

export const Populator = (): void => {
  var usuarios = [
    {
      nome: 'Brenicio Guilherme',
      email: 'brenicio_guilherme2@hotmail.com',
      perfilType: PerfilType.Adminstrador,
      ativo: true,
      senha: '123456',
    },
    {
      nome: 'Danylo Macelai',
      email: 'danylomacelai@gmail.com',
      perfilType: PerfilType.Adminstrador,
      ativo: true,
      senha: '123456',
    },
    {
      nome: 'Renato Araujo',
      email: 'orenatoaraujo@hotmail.com',
      perfilType: PerfilType.Adminstrador,
      ativo: true,
      senha: '123456',
    },
    {
      nome: 'Amy J. Pike',
      email: 'amy@rvirtual.com',
      perfilType: PerfilType.Padrao,
      ativo: true,
      senha: '123456',
    },
    {
      nome: 'John L. Osbourne',
      email: 'john@rvirtual.com',
      perfilType: PerfilType.Padrao,
      ativo: false,
      senha: '123456',
    },
    {
      nome: 'Mary M. Oakes',
      email: 'mary@rvirtual.com',
      perfilType: PerfilType.Padrao,
      ativo: true,
      bloqueado: true,
      senha: '123456',
    },
  ];

  UsuarioModel.count().then(n => {
    if (n === 0) {
      usuarios.forEach(usuario => {
        UsuarioModel.create(usuario);
      });
    }
  });
};
