import React from "react";
import {CastFragment} from "../_generated__/graphql/gql";

interface Props {
    cast: CastFragment[];
}

export const CastMembers: React.FC<Props> = ({ cast }) => {
    return (
        <ul
            style={{
                display: "flex",
                listStyle: "none",
                overflow: "scroll",
                width: "stretch",
            }}
        >
            {cast.map(casting => (
                <li key={casting.actor.id}>
                    {casting.actor.name} <small>{casting.character}</small>
                </li>
            ))}
        </ul>
    );
}

export default CastMembers;
