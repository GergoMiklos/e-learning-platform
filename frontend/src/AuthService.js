import {useEffect, useState} from "react";
import client from "./ApolloClient";
import storage from 'local-storage';

const USER_DATA_KEY = 'LEARN_WELL_USER';

export function useAuthentication() {
    const [userData, setUserData] = useState(() => storage.get(USER_DATA_KEY))

    function handleChange(value) {
        setUserData(value);
    }

    useEffect(() => {
        storage.on(USER_DATA_KEY, handleChange)
        return () => {
            storage.off(USER_DATA_KEY, handleChange)
        }
    })
    return {isLoggedIn: !!userData, userId: userData?.userId, token: userData?.token, setLogin, setLogout: logout}
}

const getUserId = () => {
    return storage.get(USER_DATA_KEY)?.userId;
}

const getToken = () => {
    return storage.get(USER_DATA_KEY)?.token;
}

const setLogin = ({userId, token}) => {
    storage.set(USER_DATA_KEY, {userId, token})
}

const isLoggedIn = () => {
    return !!storage.get(USER_DATA_KEY);
}

const logout = () => {
    storage.remove(USER_DATA_KEY);
    client.resetStore();
}

export default {
    getUserId,
    getToken,
    isLoggedIn,
    setLogin,
    logout
};