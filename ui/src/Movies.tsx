import {useQuery} from "@apollo/client";
import {LIST_MOVIES} from "./gql";

export default function Movies() {
    const { loading, error, data } = useQuery<Movie[]>(LIST_MOVIES);

    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;

    return (
        <div>
            {data?.map((movie) => (
                <div key={movie.id}>{movie.name}</div>
            ))}
        </div>
    );
}

interface Movie {
    id: string;
    name: string;
}