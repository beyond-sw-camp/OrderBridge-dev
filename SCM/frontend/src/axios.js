import axios from 'axios';

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    common: {
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      'Refresh-Token': `Bearer ${localStorage.getItem('refreshToken')}`
    },
    post: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  }
});

export default instance;
