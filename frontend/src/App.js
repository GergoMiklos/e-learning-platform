import React from 'react';
import './bootstrap.css';
import './index.css';
import 'toasted-notes/src/styles.css';
import { ApolloProvider } from '@apollo/client';
import { BrowserRouter as Router } from 'react-router-dom';
import AppComp from './components/app/AppComp';
import client from './ApolloClient';

function App() {
  return (
    <ApolloProvider client={client}>
      <Router>
        <AppComp />
      </Router>
    </ApolloProvider>
  );
}

export default App;
