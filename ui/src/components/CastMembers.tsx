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
                    <div>
                        {
                            casting.actor.profilePhotoUrl &&
                                <img src={casting.actor.profilePhotoUrl}
                                     style = {{
                                         borderRadius: "100%",
                                         width: "50px",
                                         height: "50px",
                                         objectFit: "cover"
                                     }}
                                />
                        }
                    </div>
                    <div>{casting.character}</div>
                    <div>{casting.actor.name}</div>
                </li>
            ))}
        </ul>
    );
}

export default CastMembers;
