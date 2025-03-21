import React from "react";
import {Actor} from "../__generated__/graphql";

interface Props {
    actors: Actor[];
}

export const MovieActor: React.FC<Props> = ({ actors }) => {
    return (
        <ul>
            {actors.map(actor => (
                <li key={actor.id}>{actor.name}</li>
            ))}
        </ul>
    );
}
