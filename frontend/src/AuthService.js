
const USER_KEY = 'TOKEN';
const TOKEN_KEY = 'USER';

//Todo cookies/ useCookiwa
const getUserId = () => {
    return localStorage.getItem(USER_KEY);
}

const getToken = () => {
    return localStorage.getItem(TOKEN_KEY);
}

const setLogin = ({userId, token}) => {
    localStorage.setItem(USER_KEY, userId.toString());
    localStorage.setItem(TOKEN_KEY, token.toString());
    console.log(localStorage.getItem(USER_KEY))
}

const isLoggedIn = () => {
    return localStorage.getItem(TOKEN_KEY) != null && localStorage.getItem(USER_KEY) != null;
}

const logout =() => {
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(TOKEN_KEY);
}


export default {
    getUserId,
    getToken,
    isLoggedIn,
    setLogin,
    logout
};