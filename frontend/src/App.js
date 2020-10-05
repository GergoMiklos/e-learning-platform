import React from 'react';
import './bootstrap.css'
import 'toasted-notes/src/styles.css';
import AppComp from "./components/AppComp";
import {ApolloProvider, getApolloContext} from '@apollo/client';
import { useMemo } from "react";
import client from "./ApolloClient";

// const CustomApolloProvider = ({ client, children }) => {
//     const ApolloContext = getApolloContext();
//     const value = useMemo(() => ({ client }), [client]);
//     return <ApolloContext.Provider value={value}>{children}</ApolloContext.Provider>;
// };

function App() {
  return (
      <ApolloProvider client={client}>
          <AppComp></AppComp>
      </ApolloProvider>
  );
}

export default App;
