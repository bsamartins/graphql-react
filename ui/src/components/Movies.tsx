import {useQuery} from "@apollo/client";
import {LIST_MOVIES} from "../graphql/gql";

export default function Movies() {
    const { loading, error, data, fetchMore } = useQuery(LIST_MOVIES, {
        variables: {
            first: 10,
        }
    });
    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;
    let hasNextPage = data?.movies?.pageInfo?.hasNextPage ?? false;
    let onClick = async () => {
        await fetchMore({
            variables: {
                after: data?.movies?.pageInfo?.endCursor
            }
        });
    }
    return (
        <div>
            <button onClick={onClick} disabled={!hasNextPage}>Next</button>
            <div>
                {data?.movies?.edges?.map(edge => edge?.node!!).map(movie => (
                    <div key={movie.id}>{movie.name}</div>
                ))}
            </div>
        </div>
    );
}
