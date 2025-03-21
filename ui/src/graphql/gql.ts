import {gql} from '../__generated__';

export const LIST_MOVIES = gql(`
  query ListMovies {
    movies {
      id
      name
    }
  }
`);

export const LIST_ACTORS = gql(`
  query ListActors {
    actors {
      id
      name
    }
  }
`);