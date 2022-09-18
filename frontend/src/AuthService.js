import { useEffect, useState } from 'react';
import storage from 'local-storage';
// eslint-disable-next-line import/no-cycle
import client from './ApolloClient';

const USER_DATA_KEY = 'LEARN_WELL_USER';

export function useAuthentication() {
  const [userData, setUserData] = useState(() => storage.get(USER_DATA_KEY));

  function handleChange(value) {
    setUserData(value);
  }

  useEffect(() => {
    storage.on(USER_DATA_KEY, handleChange);
    return () => {
      storage.off(USER_DATA_KEY, handleChange);
    };
  });
  return {
    isLoggedIn: !!userData,
    userId: userData?.userId,
    token: userData?.token,
    setLogin,
    setLogout: logout,
  };
}

const getUserId = () => storage.get(USER_DATA_KEY)?.userId;

const getToken = () => storage.get(USER_DATA_KEY)?.token;

const setLogin = ({ userId, token }) => {
  storage.set(USER_DATA_KEY, { userId, token });
};

const isLoggedIn = () => !!storage.get(USER_DATA_KEY);

const logout = () => {
  storage.remove(USER_DATA_KEY);
  client.resetStore();
};

export default {
  getUserId,
  getToken,
  isLoggedIn,
  setLogin,
  logout,
};
