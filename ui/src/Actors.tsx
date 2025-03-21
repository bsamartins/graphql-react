import {useQuery} from "@apollo/client";
import {LIST_ACTORS, LIST_MOVIES} from "./gql";

export default function Actors() {
    const { loading, error, data } = useQuery<ListActors>(LIST_ACTORS);

    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;
    console.log(data);
    return (
        <div>
            {data?.actors?.map((actor) => (
                <div key={actor.id}>{actor.name}</div>
            ))}
        </div>
    );
}

interface ListActors {
    actors: Actor[]
}

interface Actor {
    id: string;
    name: string;
}