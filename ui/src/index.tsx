import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {
    ApolloClient,
    ApolloLink,
    ApolloProvider, FetchResult,
    from,
    HttpLink,
    HttpOptions,
    InMemoryCache, Observable,
    split
} from "@apollo/client";
import {BatchHttpLink} from "@apollo/client/link/batch-http";
import {onError} from "@apollo/client/link/error";
import {client} from "./apollo";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
      <ApolloProvider client={client}>
        <App />
      </ApolloProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
