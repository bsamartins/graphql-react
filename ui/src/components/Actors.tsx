import {useQuery} from "@apollo/client";
import {LIST_ACTORS} from "../graphql/gql";

export default function Actors() {
    const { loading, error, data } = useQuery(LIST_ACTORS);

    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;
    console.log(data);
    return (
        <div>
            {data?.actors?.edges?.map(edge => edge?.node!!).map((actor) => (
                <div key={actor.id}>{actor.name}</div>
            ))}
        </div>
    );
}
