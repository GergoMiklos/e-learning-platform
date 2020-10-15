//import ApolloClient from 'apollo-boost';
import {from, ApolloClient, ApolloLink, HttpLink, InMemoryCache, split} from '@apollo/client';
import {getMainDefinition} from '@apollo/client/utilities';
import {WebSocketLink} from '@apollo/client/link/ws';
import {onError} from "@apollo/client/link/error";
import AuthService from "./AuthService";

const httpLink = new HttpLink({
    uri: 'http://localhost:8080/graphql'
});

const wsLink = new WebSocketLink({
    uri: `ws://localhost:8080/subscriptions`,
    options: {
        reconnect: true
    }
});

const splitLink = split(
    ({ query }) => {
        const definition = getMainDefinition(query);
        return (definition.kind === 'OperationDefinition' && definition.operation === 'subscription');
    },
    wsLink,
    httpLink,
);

const authMiddleware = new ApolloLink((operation, forward) => {
    operation.setContext({
        headers: {
            Authorization: `Bearer ${AuthService.getToken()}`,
        }
    });
    return forward(operation);
})

const logoutMiddleware = onError(({ graphQLErrors, networkError }) => {
    if (graphQLErrors)
        graphQLErrors.forEach(({ message, locations, path, extensions }) => {
                console.log(`[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`)
                if (extensions?.UNAUTHORIZED) {
                    AuthService.logout();
                }
            }
        );
    if (networkError) console.log(`[Network error]: ${networkError}`);
});



const client = new ApolloClient({
    link: from([
        authMiddleware,
        logoutMiddleware,
        splitLink
    ]),
    cache: new InMemoryCache(),
    defaultOptions: {
        watchQuery: {
            fetchPolicy: 'cache-and-network',
            errorPolicy: 'all',
        },
        query: {
            fetchPolicy: 'network-only',
            errorPolicy: 'all',
        },
        mutate: {
            errorPolicy: 'none',
        },
        subscription: {
            fetchPolicy: 'network-only',
            errorPolicy: 'all'
        }
    }
});

export default client;