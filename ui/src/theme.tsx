import React from "react";
import {ThemeProvider, useTheme} from "@mui/material";

export function Provider(props: { children: React.ReactNode }) {
    const theme = useTheme();
    return (
        <ThemeProvider theme={theme}>
            {props.children}
        </ThemeProvider>
    )
}
