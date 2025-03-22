import {useQuery} from "@apollo/client";
import {LIST_MOVIES} from "../graphql/gql";
import {ListMoviesQuery} from "../__generated__/graphql";
import CastMembers from "./CastMembers";
import {useEffect, useState} from "react";
import {useDebounce} from "use-debounce";
import {useQueryParam} from "../hooks";

export default function Movies() {
    const [queryInput, setQueryInput] = useState<string>("");
    const [queryDebounced] = useDebounce(queryInput, 500);
    const [queryParam, setQueryParam] = useQueryParam("query");

    const { loading, error, data, fetchMore } = useQuery(LIST_MOVIES, {
        variables: {
            first: 10,
            query: queryDebounced ? queryDebounced : null
        },
    });

    useEffect(() => {
        setQueryInput(queryParam ?? "");
    }, []);

    useEffect(() => {
        setQueryParam(queryDebounced);
    }, [queryDebounced]);

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
                first: null,
                last: 10,
                before: data?.movies?.pageInfo?.startCursor
            },
            updateQuery: (previousResult, { fetchMoreResult }) => replaceResults(previousResult, fetchMoreResult),
        });
    }

    return (
        <div>
            <div>
                <input
                    type="text"
                    value={queryInput}
                    onChange={(event) => {
                        const value = event.target.value;
                        setQueryInput(value);
                    }}
                />
            </div>
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
