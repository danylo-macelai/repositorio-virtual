import PerfilType from '../enums/PerfilType';

/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 27 de jul de 2019
 * version $
 */
export interface Auth {
  id: number;
  perfilType: PerfilType;
  email?: string;
}
