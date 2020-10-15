import Cookies from 'universal-cookie';
import {useEffect, useState} from "react";

const USER_DATA = 'LEARN_WELL_USER';
const cookies = new Cookies(USER_DATA);

export function useAuthentication() {
    const [userData, setUserData] = useState(() => cookies.get(USER_DATA))

    function handleChange({name, value}) {
        if(name === USER_DATA) {
            setUserData(value);
        }
    }

    useEffect(() => {
        cookies.addChangeListener(handleChange);
        return () => {
            cookies.removeChangeListener(handleChange)
        }
    })
    return {isLoggedIn: !!userData, userId: userData?.userId, token: userData?.token, setLogin, setLogout: logout}
}

const getUserId = () => {
    return cookies.get(USER_DATA)?.userId;
}

const getToken = () => {
    return cookies.get(USER_DATA)?.token;
}

const setLogin = ({userId, token}) => {
    cookies.set(USER_DATA, {userId, token})
}

const isLoggedIn = () => {
    return !!cookies.get(USER_DATA);
}

const logout =() => {
    cookies.remove(USER_DATA);
}

export default {
    getUserId,
    getToken,
    isLoggedIn,
    setLogin,
    logout
};