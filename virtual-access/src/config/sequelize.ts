/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 8 de jul de 2019
 * version $
 */

import * as path from 'path';

import { Op, Dialect } from 'sequelize';
import { Sequelize } from 'sequelize-typescript';

import { ENV } from './env.config';

const operatorsAliases = {
  $in: Op.in,
};

export const sequelize = new Sequelize({
  dialect: ENV.SGDB_DIALECT as Dialect,
  operatorsAliases,
  database: ENV.SGDB_DATABASE,
  username: ENV.SGDB_USERNAME,
  password: ENV.SGDB_PASSWORD,
  models: [path.resolve(`${__dirname}./../models`)],
  host: ENV.SGDB_SERVER_NAME,
  port: Number(ENV.NODE_PORT),
});
