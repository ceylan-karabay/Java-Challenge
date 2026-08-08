import Profile from "../components/Profile/Profile.jsx";
import { useParams } from "react-router-dom";
function ProfilePage() {
   const { id } = useParams();

       return <Profile userId={id} />;
}

export default ProfilePage;