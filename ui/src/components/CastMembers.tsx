import React from "react";
import {CastFragment} from "../_generated__/graphql/gql";
import {Box, Card, CardContent, CardMedia, Typography} from "@mui/material";

interface Props {
    cast: CastFragment[];
}

export const CastMembers: React.FC<Props> = ({ cast }) => {
    return (
        <Box
            style={{
                display: "flex",
                overflow: "scroll",
            }}
        >
            {cast.map(casting => (
                <Card key={casting.actor.id} style={{ width: "200px", marginLeft: "10px", marginBottom: "10px" }}>
                    <CardMedia image={casting.actor.profilePhotoUrl!!} sx={{ height: 140 }}/>
                    <CardContent>
                        <Typography variant="subtitle2">{casting.actor.name}</Typography>
                        <Typography variant="body2" sx={ (theme) => ({
                            color: theme.palette.grey["500"]
                        })}>{casting.character}</Typography>
                    </CardContent>
                </Card>
            ))}
        </Box>
    );
}

export default CastMembers;
