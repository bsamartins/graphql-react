import {gql} from '../__generated__';

export const CAST_FRAGMENT = gql(`
  fragment CastFragment on Cast {
    character
    actor {
      id
      name            
    }
  }
`);

export const LIST_MOVIES = gql(`
  query ListMovies($first: Int, $last: Int, $after: String, $before: String, $query: String) {
    movies(first: $first, last: $last, after: $after, before: $before, query: $query) {
      edges {
        cursor
        node {
          id
          name
          cast {
            ...CastFragment
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
  ${CAST_FRAGMENT}
`);
