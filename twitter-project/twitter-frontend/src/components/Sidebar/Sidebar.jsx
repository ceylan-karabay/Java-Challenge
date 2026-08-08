import { NavLink, useNavigate } from "react-router-dom";
import "./Sidebar.css";

function Sidebar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");

        navigate("/login");
    };

    return (
        <aside className="sidebar">

            {/* Logo */}
            <div className="sidebar-logo">
                𝕏
            </div>

            {/* Menü */}
            <nav className="sidebar-menu">

                <NavLink
                    to="/"
                    className="sidebar-link"
                >
                    🏠
                    <span>Home</span>
                </NavLink>

                <NavLink
                    to="/explore"
                    className="sidebar-link"
                >
                    🔍
                    <span>Explore</span>
                </NavLink>

                <NavLink
                    to="/follow"
                    className="sidebar-link"
                >
                    👥
                    <span>Follow</span>
                </NavLink>

                <NavLink
                    to="/profile"
                    className="sidebar-link"
                >
                    👤
                    <span>Profile</span>
                </NavLink>

            </nav>

            {/* Tweet */}
            <button
                className="sidebar-post-button"
                onClick={() => navigate("/")}
            >
                Post
            </button>

            {/* Logout */}
            <button
                className="sidebar-logout"
                onClick={handleLogout}
            >
                🚪
                <span>Logout</span>
            </button>

        </aside>
    );
}

export default Sidebar;