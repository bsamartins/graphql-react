import {
    ApolloClient,
    ApolloLink,
    FetchResult, from,
    HttpLink,
    HttpOptions,
    InMemoryCache,
    Observable,
    split
} from "@apollo/client";
import {BatchHttpLink} from "@apollo/client/link/batch-http";
import {onError} from "@apollo/client/link/error";

const linkOptions: HttpOptions = {
    uri: '/graphql',
}

const httpLink = new HttpLink(linkOptions);
const batchHttpLink = new BatchHttpLink({
    ...linkOptions,
    batchMax: 5, // No more than 5 operations per batch
    batchInterval: 30 // Wait no more than 20ms after first batched operation
});

// https://github.com/apollographql/apollo-client/issues/1564#issuecomment-357492659
const typenameLink = new ApolloLink((operation, forward) => {
    if (operation.variables) {
        // eslint-disable-next-line no-param-reassign
        // operation.variables = safeJSON.fromString(JSON.stringify(operation.variables), omitTypename) ?? {};
    }

    return forward(operation);
});

const errorLink = onError(({ graphQLErrors, operation, networkError, response, forward }) => {
    if (networkError && 'statusCode' in networkError && networkError.statusCode === 401) {
        const observable = new Observable<FetchResult<Record<string, any>>>((observer) => {
            (async () => {
                try {
                    // await refreshAccessToken();
                    // Retry the failed request
                    const subscriber = {
                        next: observer.next.bind(observer),
                        error: observer.error.bind(observer),
                        complete: observer.complete.bind(observer)
                    };

                    forward(operation).subscribe(subscriber);
                } catch (err) {
                    // redirectToPublicPage();
                }
            })();
        });
        return observable;
    }
});

export const client = new ApolloClient({
    // uri: '/graphql',
    cache: new InMemoryCache(),
    link: split(
        (operation) =>
            operation.getContext().batch !== false &&
            process.env.NODE_ENV !== 'test' &&
            sessionStorage.getItem('automatic-requests-batching') !== 'false',
        // from([typenameLink, errorLink, batchHttpLink]),
        from([typenameLink, errorLink, httpLink])
    ),
});
