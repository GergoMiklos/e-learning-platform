import React from 'react';
import './bootstrap.css'
import AppComp from "./components/AppComp";
import { ApolloProvider } from '@apollo/react-hooks';
import client from "./ApolloClient";
import {Helmet} from 'react-helmet';


function App() {
  return (
      <ApolloProvider client={client}>
          <Helmet bodyAttributes={{style: 'background-color : Gainsboro'}}/>
          <AppComp/>
      </ApolloProvider>
  );
}

export default App;
