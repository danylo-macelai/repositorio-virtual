import * as path from 'path';

import { Sequelize } from 'sequelize-typescript';

import { ENV } from './env.config';

const operatorsAliases = {
  $in: Sequelize.Op.in,
};

export const sequelize = new Sequelize({
  dialect: ENV.SGDB_DIALECT,
  operatorsAliases: operatorsAliases,
  database: ENV.SGDB_DATABASE || null,
  username: ENV.SGDB_USERNAME || null,
  password: ENV.SGDB_PASSWORD || null,
  modelPaths: [path.resolve(`${__dirname}./../models`)],
  host: ENV.SGDB_SERVER_NAME || null,
  port: +ENV.NODE_PORT || null,
});
