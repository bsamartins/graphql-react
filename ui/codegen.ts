import type {CodegenConfig} from '@graphql-codegen/cli';

const config: CodegenConfig = {
  overwrite: true,
  schema: "http://localhost:8080/graphql",
  documents: "src/**/*.{ts,tsx}",
  generates: {
    'src/_generated__/graphql/gql.ts': {
      // preset: "client",
      plugins: [
        "typescript",
        "typescript-operations",
        "typescript-react-apollo",
      ],
      presetConfig: {
        flattenGeneratedTypes: false,
        flattenGeneratedTypesIncludeFragments: false,
        extractAllFieldsToTypes: true,
      },
      config: {
      },
    },
    "./graphql.schema.json": {
      plugins: ["introspection"]
    }
  },
  verbose: true,
};

export default config;
