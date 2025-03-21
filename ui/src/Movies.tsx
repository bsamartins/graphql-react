import {useQuery} from "@apollo/client";
import {LIST_MOVIES} from "./gql";

export default function Movies() {
    const { loading, error, data } = useQuery<ListMovies>(LIST_MOVIES);

    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;

    return (
        <div>
            {data?.data?.map((movie) => (
                <div key={movie.id}>{movie.name}</div>
            ))}
        </div>
    );
}

interface ListMovies {
    data: Movie[]
}

interface Movie {
    id: string;
    name: string;
}