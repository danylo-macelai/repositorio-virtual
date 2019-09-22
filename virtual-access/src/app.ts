/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 13 de mai de 2019
 * version $
 */

import express from 'express';
import cors from 'cors';
import graphqlHTTP from 'express-graphql';

import { Authentication } from './middlewares/authentication.middleware';
import schema from './graphql/schema';

import { ENV } from './config/env.config';
import { Auth } from './interfaces/AuthInterface';

declare global {
  namespace Express {
    export interface Request {
      auth: Auth;
    }
  }
}

class App {
  public express: express.Application;

  constructor() {
    this.express = express();
    this.middleware();
  }

  private middleware(): void {
    this.express.use(cors());

    this.express.use(
      '/access',
      Authentication(),
      graphqlHTTP(request => ({
        schema,
        graphiql: ENV.NODE_ENV === 'development',
        context: {
          auth: request.auth,
        },
      }))
    );
  }
}

export default new App().express;
