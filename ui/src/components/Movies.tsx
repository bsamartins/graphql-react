import {useQuery} from "@apollo/client";
import {LIST_MOVIES} from "../graphql/gql";
import {ListMoviesQuery, ListMoviesQueryVariables} from "../__generated__/graphql";

export default function Movies() {
    const { loading, error, data } = useQuery<ListMoviesQuery, ListMoviesQueryVariables>(LIST_MOVIES);

    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;
    console.log(data);
    return (
        <div>
            {data?.movies?.map((movie) => (
                <div key={movie.id}>{movie.name}</div>
            ))}
        </div>
    );
}
