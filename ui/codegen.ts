import { CodegenConfig } from '@graphql-codegen/cli';

const config: CodegenConfig = {
    schema: 'http://localhost:8080/graphql',
    // this assumes that all your source files are in a top-level `src/` directory - you might need to adjust this to your file structure
    documents: ['src/**/*.{ts,tsx}'],
    generates: {
        './src/__generated__/': {
            preset: 'client',
            plugins: [],
            presetConfig: {
                gqlTagName: 'gql',
                fragmentMasking: false,
            },
            config: {
                extractAllFieldsToTypes: true,
                futureProofEnums: true,
                futureProofUnions: true,
                onlyOperationTypes: false,
                avoidOptionals: false,
            },
        }
    },
    ignoreNoDocuments: true,
};

export default config;