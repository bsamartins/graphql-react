import React from "react";
import {CastFragmentFragment} from "../__generated__/graphql";

interface Props {
    cast: [CastFragmentFragment];
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
