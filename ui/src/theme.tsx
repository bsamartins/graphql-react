import React from "react";
import {ChakraProvider, createSystem, defaultBaseConfig} from "@chakra-ui/react";
import {ThemeProvider} from "@emotion/react";

export const system = createSystem(defaultBaseConfig, {});

export function Provider(props: { children: React.ReactNode }) {
    return (
        <ChakraProvider value={system}>
            <ThemeProvider theme={{}}>
                {props.children}
            </ThemeProvider>
        </ChakraProvider>
    )
}
