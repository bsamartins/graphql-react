import React from "react";
import {CastFragment} from "../_generated__/graphql/gql";

interface Props {
    cast: CastFragment[];
}

export const CastMembers: React.FC<Props> = ({ cast }) => {
    return (
        <ul>
            {cast.map(cast => (
                <li key={cast.actor.id}>{cast.actor.name}</li>
            ))}
        </ul>
    );
}

export default CastMembers;
