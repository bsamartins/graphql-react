/* eslint-disable */
import * as types from './graphql';
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';

/**
 * Map of all GraphQL operations in the project.
 *
 * This map has several performance disadvantages:
 * 1. It is not tree-shakeable, so it will include all operations in the project.
 * 2. It is not minifiable, so the string of a GraphQL query will be multiple times inside the bundle.
 * 3. It does not support dead code elimination, so it will add unused operations.
 *
 * Therefore it is highly recommended to use the babel or swc plugin for production.
 * Learn more about it here: https://the-guild.dev/graphql/codegen/plugins/presets/preset-client#reducing-bundle-size
 */
type Documents = {
    "\n  fragment CastFragment on Cast {\n    character\n    actor {\n      id\n      name            \n    }\n  }\n": typeof types.CastFragmentFragmentDoc,
    "\n  query ListMovies($first: Int, $last: Int, $after: String, $before: String, $query: String) {\n    movies(first: $first, last: $last, after: $after, before: $before, query: $query) {\n      edges {\n        cursor\n        node {\n          id\n          name\n          cast {\n            ...CastFragment\n          }          \n        }\n      }\n      pageInfo {\n        hasNextPage\n        hasPreviousPage\n        startCursor\n        endCursor\n      }\n    }\n  }  \n  \n": typeof types.ListMoviesDocument,
};
const documents: Documents = {
    "\n  fragment CastFragment on Cast {\n    character\n    actor {\n      id\n      name            \n    }\n  }\n": types.CastFragmentFragmentDoc,
    "\n  query ListMovies($first: Int, $last: Int, $after: String, $before: String, $query: String) {\n    movies(first: $first, last: $last, after: $after, before: $before, query: $query) {\n      edges {\n        cursor\n        node {\n          id\n          name\n          cast {\n            ...CastFragment\n          }          \n        }\n      }\n      pageInfo {\n        hasNextPage\n        hasPreviousPage\n        startCursor\n        endCursor\n      }\n    }\n  }  \n  \n": types.ListMoviesDocument,
};

/**
 * The gql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 *
 *
 * @example
 * ```ts
 * const query = gql(`query GetUser($id: ID!) { user(id: $id) { name } }`);
 * ```
 *
 * The query argument is unknown!
 * Please regenerate the types.
 */
export function gql(source: string): unknown;

/**
 * The gql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function gql(source: "\n  fragment CastFragment on Cast {\n    character\n    actor {\n      id\n      name            \n    }\n  }\n"): (typeof documents)["\n  fragment CastFragment on Cast {\n    character\n    actor {\n      id\n      name            \n    }\n  }\n"];
/**
 * The gql function is used to parse GraphQL queries into a document that can be used by GraphQL clients.
 */
export function gql(source: "\n  query ListMovies($first: Int, $last: Int, $after: String, $before: String, $query: String) {\n    movies(first: $first, last: $last, after: $after, before: $before, query: $query) {\n      edges {\n        cursor\n        node {\n          id\n          name\n          cast {\n            ...CastFragment\n          }          \n        }\n      }\n      pageInfo {\n        hasNextPage\n        hasPreviousPage\n        startCursor\n        endCursor\n      }\n    }\n  }  \n  \n"): (typeof documents)["\n  query ListMovies($first: Int, $last: Int, $after: String, $before: String, $query: String) {\n    movies(first: $first, last: $last, after: $after, before: $before, query: $query) {\n      edges {\n        cursor\n        node {\n          id\n          name\n          cast {\n            ...CastFragment\n          }          \n        }\n      }\n      pageInfo {\n        hasNextPage\n        hasPreviousPage\n        startCursor\n        endCursor\n      }\n    }\n  }  \n  \n"];

export function gql(source: string) {
  return (documents as any)[source] ?? {};
}

export type DocumentType<TDocumentNode extends DocumentNode<any, any>> = TDocumentNode extends DocumentNode<  infer TType,  any>  ? TType  : never;