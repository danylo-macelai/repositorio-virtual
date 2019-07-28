/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author macelai
 * date: 13 de mai de 2019
 * version $
 */

import merge from 'lodash.merge';

import { usuarioQueries } from './schema/usuario.schema';
import { authQueries } from './schema/auth.schema';

const Query = merge(usuarioQueries, authQueries);

export { Query };
