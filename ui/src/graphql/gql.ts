import {gql} from '../__generated__';

export const LIST_MOVIES = gql(`
  query ListMovies($first: Int, $last: Int, $after: String, $before: String, $query: String) {
    movies(first: $first, last: $last, after: $after, before: $before, query: $query) {
      edges {
        cursor
        node {
          id
          name
          cast {
            actor {
              id
              name            
            }
          }
        }
      }
      pageInfo {
        hasNextPage
        hasPreviousPage
        startCursor
        endCursor
      }
    }
  }
`);
