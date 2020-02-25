import React from 'react';
import { request } from '../../axios/request';

const ACCESS_TOKEN = "accessToken";

function Home(props) {

    const sendUserRequest = () => {
        console.log(localStorage.getItem(ACCESS_TOKEN), ' access token');
        request.get("car", {}, {withCredentials: true}).then(resp => {
            alert("Request ok");
            console.log(resp.data);
        }).catch(err => {
            console.log(err);
        });
    }

    const sendAdminRequest = () => {
        request.get("car/1", {withCredentials: true}).then(resp => {
            alert("Request ok");
            console.log(resp.data);
        }).catch(err => {
            console.log(err);
        });
    }

    return (
        <div style={{display: "flex", justifyContent: 'center', marginTop: 20}}>
            <div style={{marginRight: 20}}>
                <button onClick={sendUserRequest}>User authorized request</button>
            </div>
            <div>
                <button onClick={sendAdminRequest}>Admin authorized request</button>
            </div>
        </div>
    )
}

export default Home;