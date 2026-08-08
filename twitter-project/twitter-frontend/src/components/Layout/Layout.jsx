import { Outlet } from "react-router-dom";
import Sidebar from "../Sidebar/Sidebar";
import RightSidebar from "../RightSidebar/RightSidebar";
import "./Layout.css";

function Layout() {
    return (
        <div className="layout">

            <Sidebar />

            <main className="layout-content">
                <Outlet />
            </main>

            <RightSidebar />

        </div>
    );
}

export default Layout;