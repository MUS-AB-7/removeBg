import axios from 'axios';
import { toast } from 'react-hot-toast';
export const placeOrder = async ({ planId, getToken, onSuccess, backendUrl }) => {
    try {
        if (!backendUrl) {
            toast.error('Backend URL is not configured.');
            return;
        }

        const token = await getToken();
        const response = await axios.post(`${backendUrl}/orders?planId=${planId}`, {}, { headers: { Authorization: `Bearer ${token}` } });

        if (response.status === 200) {
            initializePayment({ order: response.data.data, getToken, onSuccess, backendUrl });
        }
    } catch (error) {
        const message = error.response?.data?.data || error.response?.data?.message || error.message;
        toast.error(message);
    }
}

const initializePayment = async ({ order, getToken, onSuccess, backendUrl }) => {

    const options = {
        key: import.meta.env.VITE_RAZORPAY_KEY_ID,
        amount: order.amount,
        currency: order.currency,
        name: "Credit Payment",
        description: "Credit Payment",
        order_id: order.id,
        receipt: order.receipt,
        handler: async (paymentDetails) => {
            try {
                const token = await getToken();
                const response = await axios.post(`${backendUrl}/orders/verify`, paymentDetails, { headers: { Authorization: `Bearer ${token}` } });
                if (response.status === 200) {
                    toast.success("Credits Added.");
                    onSuccess?.();
                }
            } catch (error) {
                const message = error.response?.data?.data || error.response?.data?.message || error.message;
                toast.error(message);
            }
        }
    }

    const rzp = new window.Razorpay(options);
    rzp.open();
}
