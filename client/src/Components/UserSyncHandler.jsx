import { useAuth, useUser } from "@clerk/clerk-react";
import { useContext, useEffect, useState } from "react";
import { AppContext } from "../Context/AppContext";
import axios from "axios";
import toast from "react-hot-toast";
const UserSyncHandler = () => {

    const { isLoaded, isSignedIn, getToken } = useAuth();
    const { user } = useUser();
    const [synced, setSynced] = useState(false);
    const { backendUrl, loadUserCredits } = useContext(AppContext);

    useEffect(() => {
        const saveUser = async () => {
            if (!isLoaded || !isSignedIn || !user || synced) {
                return;
            }

            try {
                const token = await getToken();

                const userData = {
                    clerkId: user.id,
                    email: user.primaryEmailAddress.emailAddress,
                    firstName: user.firstName,
                    lastName: user.lastName,
                    photoUrl: user.imageUrl
                };
                await axios.post(backendUrl + '/users', userData, { headers: { Authorization: `Bearer ${token}` } });

                setSynced(true);
                await loadUserCredits();
                toast.success("User Synced Successfully")

            } catch (error) {
                console.error("User sync failed", error);
                if (error.response) {
                    console.error("Sync response data:", error.response.data);
                }
                toast.error("User Sync Failed. Please try again")
            }
        }
        saveUser();
    }, [isLoaded, isSignedIn, getToken, user, synced])
    return null;
}

export default UserSyncHandler
