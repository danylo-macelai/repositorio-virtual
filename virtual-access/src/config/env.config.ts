const dotEnv = require('dotenv');
dotEnv.config();

export const ENV = {
  NODE_ENV: process.env.NODE_ENV,
  NODE_PORT: process.env.NODE_PORT,

  SGDB_SERVER_NAME: process.env.SGDB_SERVER_NAME,
  SGDB_PORT: process.env.SGDB_PORT,
  SGDB_DATABASE: process.env.SGDB_DATABASE,
  SGDB_USERNAME: process.env.SGDB_USERNAME,
  SGDB_PASSWORD: process.env.SGDB_PASSWORD,
  SGDB_DIALECT: process.env.SGDB_DIALECT,
  SGDB_STORAGE: process.env.SGDB_STORAGE,
};
