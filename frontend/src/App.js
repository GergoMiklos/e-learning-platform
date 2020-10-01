import React from 'react';
import './bootstrap.css'
import AppComp from "./components/AppComp";
import { ApolloProvider } from '@apollo/client';
import client from "./ApolloClient";


function App() {
  return (
      <ApolloProvider client={client}>
          <AppComp></AppComp>
      </ApolloProvider>
  );
}

export default App;
