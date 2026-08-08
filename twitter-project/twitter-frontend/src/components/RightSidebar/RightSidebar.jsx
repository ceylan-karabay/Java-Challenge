import SearchBox from "../SearchBox/SearchBox";
import "./RightSidebar.css";

function RightSidebar() {
    return (
        <div className="right-sidebar">

            {/* SEARCH */}
            <SearchBox />

            {/* WHO TO FOLLOW */}
            <section className="right-sidebar-section">
                <h2>Who to Follow</h2>

                <div className="follow-suggestion">
                    <div className="suggestion-avatar">
                        A
                    </div>

                    <div className="suggestion-info">
                        <strong>Ahmet</strong>
                        <span>@ahmet</span>
                    </div>

                    <button>
                        Takip Et
                    </button>
                </div>

                <div className="follow-suggestion">
                    <div className="suggestion-avatar">
                        M
                    </div>

                    <div className="suggestion-info">
                        <strong>Mehmet</strong>
                        <span>@mehmet</span>
                    </div>

                    <button>
                        Takip Et
                    </button>
                </div>

                <button className="show-more">
                    Daha fazla göster
                </button>
            </section>

            {/* TRENDS */}
            <section className="right-sidebar-section trends">
                <h2>Trends</h2>

                <div className="trend">
                    <span>Gündem</span>
                    <strong>#React</strong>
                    <small>12.4K posts</small>
                </div>

                <div className="trend">
                    <span>Teknoloji</span>
                    <strong>#Java</strong>
                    <small>8.2K posts</small>
                </div>

                <div className="trend">
                    <span>Yazılım</span>
                    <strong>#SpringBoot</strong>
                    <small>5.7K posts</small>
                </div>
            </section>

        </div>
    );
}

export default RightSidebar;