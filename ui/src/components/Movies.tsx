import CastMembers from "./CastMembers";
import React, {useState} from "react";
import {useDebounce} from "use-debounce";
import {CastFragment, ListMoviesQuery, QueryMoviesArgs, useListMoviesQuery} from "../_generated__/graphql/gql";
import {Box, Button, Card, CardContent, CardMedia, TextField, Typography} from "@mui/material";

const PAGE_SIZE = 10;

export default function Movies() {
    const [queryInput, setQueryInput] = useState<string>("");
    const [queryDebounced] = useDebounce(queryInput, 500);

    let variables: QueryMoviesArgs = {
        first: PAGE_SIZE,
        query: queryDebounced ? queryDebounced : null
    }

    const { loading, data, fetchMore } = useListMoviesQuery({
        variables,
        notifyOnNetworkStatusChange: true,
    });

    let hasNextPage = data?.movies?.pageInfo?.hasNextPage ?? false;
    let hasPreviousPage = data?.movies?.pageInfo?.hasPreviousPage ?? false;
    let replaceResults = (previous: ListMoviesQuery, fetchedResults: ListMoviesQuery | null | undefined, isNext: boolean) => {
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
            updateQuery: (previousResult, {fetchMoreResult}) => replaceResults(previousResult, fetchMoreResult, false),
        });
    }

    return (
        <div>
            <Box sx={{ display: "flex" }}>
                <TextField
                    variant="outlined"
                    size="small"
                    value={queryInput}
                    onChange={(event) => {
                        const value = event.target.value;
                        setQueryInput(value);
                    }}
                    disabled={loading}
                />
                <Button onClick={onClickPreviousPage} disabled={!hasPreviousPage || loading}>Previous</Button>
                <Button onClick={onClickNextPage} disabled={!hasNextPage || loading}>Next</Button>
            </Box>
            <Box sx={{ marginTop: "10px" }}>
                {data?.movies?.edges?.map(edge => {
                    let edged = edge!!;
                    let movie = edged.node!!;
                    return (
                        <MovieCard key={movie.id} movie={movie} />
                    )
                })}
            </Box>
        </div>
    );
}

interface MovieCardProps {
    movie: {
        id: number;
        title: string;
        posterUrl?: string | null;
        cast: CastFragment[];
    };
}

const MovieCard: React.FC<MovieCardProps> = ({ movie }) => (
    <Card key={movie.id}
          sx={{
              display: "flex",
              marginBottom: "10px",
          }}
        >
        <CardMedia>
            <MoviePoster src={movie.posterUrl}/>
        </CardMedia>
        <Box>
            <CardContent>
                <Typography component="div" variant="h5">{movie.title}</Typography>
            </CardContent>
            <Box>
                <CastMembers cast={movie.cast}/>
            </Box>
        </Box>
    </Card>
)

const MoviePoster: React.FC<{ src?: string | null }> = ({ src }) => {
    let [failed, setFailed] = useState(false);
    return (
        (src && !failed) ?
            <img
                src={src}
                onError={(e) => {
                    e.currentTarget.onerror = null;
                    // Set image here
                    e.currentTarget.src = "";
                    setFailed(true);
                }}
            />
            : <>// No Image //</>
    );
}