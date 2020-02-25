import {
    UPDATE_AUTH_DATA
} from './types';

export const updateAuthData = (authData) => {
    return {
        type: UPDATE_AUTH_DATA,
        payload: authData
    };
};