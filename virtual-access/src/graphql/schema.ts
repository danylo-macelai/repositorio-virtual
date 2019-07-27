/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 13 de mai de 2019
 * version $
 */

import { GraphQLSchema } from 'graphql';

import { Query } from './query';

import { Mutation } from './mutation';

export default new GraphQLSchema({
  query: Query,
  mutation: Mutation,
});
