import axiosClient from './axiosClient';

const orderApi = {
    buy: (data) => {
        return axiosClient.post('/orders/buy', data);
    }
};
export default orderApi;