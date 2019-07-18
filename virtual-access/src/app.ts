import express from 'express';
import graphqlHTTP from 'express-graphql';

import schema from './graphql/schema';

import { ENV } from './config/env.config';

class App {
  public express: express.Application;

  constructor() {
    this.express = express();
    this.middleware();
  }

  private middleware(): void {
    this.express.use(
      '/access',
      graphqlHTTP({
        schema: schema,
        graphiql: ENV.NODE_ENV === 'development',
      })
    );
  }
}

export default new App().express;
