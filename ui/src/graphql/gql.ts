import {gql} from '../__generated__';

export const LIST_MOVIES = gql(`
  query ListMovies($first: Int, $last: Int, $after: String, $before: String) {
    movies(first: $first, last: $last, after: $after, before: $before) {
      edges {
        cursor
        node {
          id
          name
          actors {
            id
            name
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
