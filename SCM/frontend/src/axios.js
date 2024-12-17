import axios from 'axios';

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
//   headers: {
//     common: {
//       'Authorization': 'AUTH_TOKEN'
//     },
//     post: {
//       'Content-Type': 'application/x-www-form-urlencoded'
//     }
//   }
});

export default instance;
