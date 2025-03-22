import {useQuery} from "@apollo/client";
import {LIST_MOVIES} from "../graphql/gql";
import {ListMoviesQuery} from "../__generated__/graphql";
import CastMembers from "./CastMembers";

export default function Movies() {
    const { loading, error, data, fetchMore } = useQuery(LIST_MOVIES, {
        variables: {
            first: 10,
        },
    });
    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;
    let hasNextPage = data?.movies?.pageInfo?.hasNextPage ?? false;
    let hasPreviousPage = data?.movies?.pageInfo?.hasPreviousPage ?? false;
    let replaceResults = (previous: ListMoviesQuery, fetchedResults?: ListMoviesQuery) => {
        return fetchedResults ?? previous;
    }
    let onClickNextPage = async () => {
        await fetchMore({
            variables: {
                after: data?.movies?.pageInfo?.endCursor
            },
            updateQuery: (previousResult, { fetchMoreResult }) => replaceResults(previousResult, fetchMoreResult),
        });
    }
    let onClickPreviousPage = async () => {
        await fetchMore({
            variables: {
                before: data?.movies?.pageInfo?.startCursor
            },
            updateQuery: (previousResult, { fetchMoreResult }) => replaceResults(previousResult, fetchMoreResult),
        });
    }
    return (
        <div>
            <button onClick={onClickPreviousPage} disabled={!hasPreviousPage}>Previous</button>
            <button onClick={onClickNextPage} disabled={!hasNextPage}>Next</button>
            <div>
                {data?.movies?.edges?.map(edge => edge?.node!!).map(movie => (
                    <div>
                        <div key={movie.id}>{movie.name}</div>
                        <CastMembers cast={movie.cast}/>
                    </div>
                ))}
            </div>
        </div>
    );
}
