import {atomWithLocation} from "jotai-location";
import {useAtom} from "jotai/index";

const locationAtom = atomWithLocation();

export function useQueryParam(name: string): [string | null | undefined, (value: string | null | undefined) => void] {
    let [loc, setLoc] = useAtom(locationAtom);
    return [
        loc.searchParams?.get(name),
        (value) => {
            setLoc((prev) => {
                let newLocation = {
                    ...prev
                }
                if (value) {
                    newLocation.searchParams?.set(name, value);
                } else {
                    newLocation.searchParams?.delete(name);
                }
                return newLocation;
            });
        }
    ];
}