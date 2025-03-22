import {atomWithLocation} from "jotai-location";
import {useAtom} from "jotai/index";

const locationAtom = atomWithLocation();

export function useQueryParam(name: string): [string | null | undefined, (value: string) => void] {
    let [loc, setLoc] = useAtom(locationAtom);
    return [
        loc.searchParams?.get(name),
        (value) => {
            setLoc((prev) => {
                let newLocation = {
                    ...prev
                }
                newLocation.searchParams?.set(name, value);
                return newLocation;
            });
        }
    ];
}