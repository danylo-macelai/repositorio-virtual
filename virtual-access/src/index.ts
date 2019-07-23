/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 13 de mai de 2019
 * version $
 */

import * as http from 'http';

import app from './app';
import { ENV } from './config/env.config';
import { sequelize } from './config/sequelize';
import { normalizePort, onError, onListening } from './utils/utils';

const server = http.createServer(app);
const port = normalizePort(ENV.NODE_PORT || 3000);
const host = process.env.host || '127.0.0.1';

sequelize.sync().then(() => {
  server.listen({ port, host });
  server.on('error', onError(server));
  server.on('listening', onListening(server));
});
