import { gql, useQuery } from '@apollo/client';

export const LIST_MOVIES = gql`
  query ListMovies {
    movies {
      id
      name
    }
  }
`;