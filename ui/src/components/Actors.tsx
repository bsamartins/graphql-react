import {useQuery} from "@apollo/client";
import {LIST_ACTORS} from "../graphql/gql";
import {ListActorsQuery, ListActorsQueryVariables} from "../__generated__/graphql";

export default function Actors() {
    const { loading, error, data } = useQuery<ListActorsQuery, ListActorsQueryVariables>(LIST_ACTORS);

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
