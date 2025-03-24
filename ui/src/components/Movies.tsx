import CastMembers from "./CastMembers";
import React, {useEffect, useState} from "react";
import {useDebounce} from "use-debounce";
import {useQueryParam} from "../hooks";
import {ListMoviesQuery, QueryMoviesArgs, useListMoviesQuery} from "../_generated__/graphql/gql";

const PAGE_SIZE = 10;

export default function Movies() {
    const [queryInput, setQueryInput] = useState<string>("");
    const [queryDebounced] = useDebounce(queryInput, 500);
    const [queryParam, setQueryParam] = useQueryParam("query");
    const [lastParam, setLastParam] = useQueryParam("last");
    const [firstParam, setFirstParam] = useQueryParam("first");

    let queryParams: QueryMoviesArgs;
    // if (firstParam) {
    //     queryParams = {
    //         last: PAGE_SIZE,
    //         before: firstParam,
    //     };
    // } else {
        queryParams = {
            first: PAGE_SIZE,
            // after: lastParam,
        }
    // }

    let variables: QueryMoviesArgs = {
        ...queryParams,
        query: queryDebounced ? queryDebounced : null
    }

    const { loading, error, data, fetchMore } = useListMoviesQuery({variables});

    useEffect(() => {
        setQueryInput(queryParam ?? "");
    }, []);

    useEffect(() => {
        setQueryParam(queryDebounced);
        setFirstParam(null);
        setLastParam(null);
    }, [queryDebounced]);

    if (loading) return <>'Loading...'</>;
    if (error) return <>`Error! ${error.message}`</>;
    let hasNextPage = data?.movies?.pageInfo?.hasNextPage ?? false;
    let hasPreviousPage = data?.movies?.pageInfo?.hasPreviousPage ?? false;
    let replaceResults = (previous: ListMoviesQuery, fetchedResults: ListMoviesQuery | null | undefined, isNext: boolean) => {
        let afterCursor = fetchedResults?.movies?.pageInfo?.endCursor ??
            fetchedResults?.movies?.pageInfo?.endCursor;
        let beforeCursor = fetchedResults?.movies?.pageInfo?.startCursor ??
            fetchedResults?.movies?.pageInfo?.startCursor;

        if (isNext) {
            setLastParam(afterCursor);
            setFirstParam(null);
        } else {
            setLastParam(null);
            setFirstParam(beforeCursor);
        }
        return fetchedResults ?? previous;
    }
    let onClickNextPage = async () => {
        await fetchMore({
            variables: {
                after: data?.movies?.pageInfo?.endCursor
            },
            updateQuery: (previousResult, { fetchMoreResult }) => replaceResults(previousResult, fetchMoreResult, true),
        });
    }
    let onClickPreviousPage = async () => {
        await fetchMore({
            variables: {
                first: null,
                last: PAGE_SIZE,
                before: data?.movies?.pageInfo?.startCursor,
                after: null,
            },
            updateQuery: (previousResult, { fetchMoreResult }) => replaceResults(previousResult, fetchMoreResult, false),
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
                {data?.movies?.edges?.map(edge => {
                    let edged = edge!!;
                    let movie = edged.node!!;
                    return (
                        <div style={{display: "flex"}}>
                            <div>
                                <MoviePoster src={movie.posterUrl} />
                            </div>
                            <div>
                                <div key={movie.id}>{movie.name}</div>
                                <div><small>{movie.id} / {edged.cursor}</small></div>
                                <CastMembers cast={movie.cast}/>
                            </div>
                        </div>
                    )
                })}
            </div>
        </div>
    );
}

const MoviePoster: React.FC<{ src?: string | null }> = ({ src }) => {
    return (
        src ? <img src={src}/> : <></>
    );
}