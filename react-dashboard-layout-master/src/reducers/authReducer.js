import {
    UPDATE_AUTH_DATA
} from '../actions/types';

const INITIAL_STATE = {
    user: null,
    dropdownItems: [],
    authenticated: false,
    isLoading: false
};

export default function (state = INITIAL_STATE, action) {
    switch (action.type) {
        case UPDATE_AUTH_DATA:
            return {...state, ...action.payload};
        default:
            return state;
    }
}
