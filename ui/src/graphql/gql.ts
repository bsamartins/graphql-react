import {gql} from '../__generated__';

export const LIST_MOVIES = gql(`
  query ListMovies($first: Int, $after: String) {
    movies(first: $first, after: $after) {
      edges {
        cursor
        node {
          id
          name
        }
      }
      pageInfo {
        hasNextPage
        startCursor
        endCursor
      }
    }
  }
`);

export const LIST_ACTORS = gql(`
  query ListActors {
    actors {
      edges {
        cursor
        node {
          id
          name
        }
      }
      pageInfo {
        hasNextPage
        startCursor
        endCursor
      }
    }
  }
`);