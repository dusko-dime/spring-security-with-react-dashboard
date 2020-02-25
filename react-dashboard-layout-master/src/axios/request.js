import Axios from "axios";

const ACCESS_TOKEN = "accessToken";

export const request = Axios.create({
  baseURL: 'api',
  headers: {
    Authorization: {
      toString() {
        return `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
      }
    },
    'Content-Type': 'application/json'
  }
});