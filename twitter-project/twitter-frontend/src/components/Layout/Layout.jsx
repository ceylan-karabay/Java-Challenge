import { Outlet } from "react-router-dom";
import Sidebar from "../Sidebar/Sidebar";
import RightSidebar from "../RightSidebar/RightSidebar";
import SearchBox from "../SearchBox/SearchBox";
import "./Layout.css";

function Layout() {
    return (
        <div className="layout">

            <Sidebar />

            <main className="layout-content">

                <div className="mobile-search">
                    <SearchBox />
                </div>

                <Outlet />

            </main>

            <RightSidebar />

        </div>
    );
}

export default Layout;