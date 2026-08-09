import {
    BrowserRouter,
    Routes,
    Route,
    Navigate,
    useNavigate
} from "react-router-dom";

import AuthContainer from "../components/AuthContainer/AuthContainer";
import Layout from "../components/Layout/Layout";
import ProtectedRoute from "./ProtectedRoute";

import Home from "../pages/Home";
import FollowPage from "../pages/FollowPage";
import ProfilePage from "../pages/ProfilePage";

function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>

                {/* GİRİŞ GEREKTİRMEYEN SAYFALAR */}

                <Route
                    path="/login"
                    element={<AuthPage />}
                />

                <Route
                    path="/register"
                    element={<AuthPage />}
                />


                {/* GİRİŞ GEREKTİREN SAYFALAR */}

                <Route element={<ProtectedRoute />}>

                    <Route element={<Layout />}>

                        <Route
                            path="/"
                            element={<Home />}
                        />

                        <Route
                            path="/home"
                            element={<Home />}
                        />

                        <Route
                            path="/follow"
                            element={<FollowPage />}
                        />

                        <Route
                            path="/profile"
                            element={<ProfilePage />}
                        />

                        <Route
                            path="/profile/:id"
                            element={<ProfilePage />}
                        />

                    </Route>

                </Route>


                {/* BULUNAMAYAN SAYFALAR */}

                <Route
                    path="*"
                    element={<Navigate to="/" replace />}
                />

            </Routes>
        </BrowserRouter>
    );
}


function AuthPage() {
    const navigate = useNavigate();

    const handleAuthSuccess = () => {
        navigate("/");
    };

    return (
        <AuthContainer
            onAuthSuccess={handleAuthSuccess}
        />
    );
}

export default AppRouter;