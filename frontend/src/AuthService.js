import Cookies from 'universal-cookie';
import {useEffect, useState} from "react";
import storage from "local-storage"
import client from "./ApolloClient";

const USER_DATA = 'LEARN_WELL_USER';
const cookies = new Cookies(USER_DATA);

export function useAuthentication() {
    const [userData, setUserData] = useState(() => cookies.get(USER_DATA))

    // function handleChange({name, value}) {
    //     if(name === USER_DATA) {
    //         setUserData(value);
    //     }
    // }

    function handleChange(value) {
        setUserData(value);
    }

    useEffect(() => {
        storage.on(USER_DATA, handleChange)
        //cookies.addChangeListener(handleChange);
        return () => {
            storage.off(USER_DATA, handleChange)
            //cookies.removeChangeListener(handleChange)
        }
    })
    return {isLoggedIn: !!userData, userId: userData?.userId, token: userData?.token, setLogin, setLogout: logout}
}

const getUserId = () => {
    return storage.get(USER_DATA)?.userId;
}

const getToken = () => {
    return storage.get(USER_DATA)?.token;
}

const setLogin = ({userId, token}) => {
    storage.set(USER_DATA, {userId, token})
}

const isLoggedIn = () => {
    return !!storage.get(USER_DATA);
}

const logout = () => {
    storage.remove(USER_DATA);
    client.resetStore();
}

export default {
    getUserId,
    getToken,
    isLoggedIn,
    setLogin,
    logout
};