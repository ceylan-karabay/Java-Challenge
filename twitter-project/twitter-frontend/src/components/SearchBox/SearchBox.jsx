
import { useEffect, useState } from "react";
import { searchUsers } from "../../api/userApi";
import "./SearchBox.css";
import { useNavigate } from "react-router-dom";

function SearchBox() {
    const [query, setQuery] = useState("");
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        const trimmedQuery = query.trim();

        if (!trimmedQuery) {
            setUsers([]);
            return;
        }

        const timer = setTimeout(() => {
            fetchUsers(trimmedQuery);
        }, 400);

        return () => clearTimeout(timer);
    }, [query]);

    const fetchUsers = async (searchQuery) => {
        try {
            setLoading(true);

            const response = await searchUsers(
                searchQuery,
                0,
                10
            );

            console.log("Arama sonucu:", response);

            setUsers(response?.content || []);
        } catch (error) {
            console.error(
                "Kullanıcı araması başarısız:",
                error
            );

            setUsers([]);
        } finally {
            setLoading(false);
        }
    };

    // Kullanıcı profiline git
    const handleUserClick = (user) => {
        //console.log("Tıklanan kullanıcı:", user);
        //console.log("Kullanıcı ID:", user.id);
        //console.log("ID tipi:", typeof user.id);

        navigate(`/profile/${user.id}`);

        // Arama sonuçlarını kapat
        setQuery("");
        setUsers([]);
    };

    return (
        <div className="search-wrapper">

            <form
                className="search-box"
                onSubmit={(e) => e.preventDefault()}
            >
                <span className="search-icon">
                    🔍
                </span>

                <input
                    type="text"
                    placeholder="Kullanıcı ara..."
                    value={query}
                    onChange={(e) =>
                        setQuery(e.target.value)
                    }
                />
            </form>

            {loading && (
                <div className="search-results">
                    <div className="search-message">
                        Aranıyor...
                    </div>
                </div>
            )}

            {!loading &&
                query.trim() &&
                users.length === 0 && (
                    <div className="search-results">
                        <div className="search-message">
                            Kullanıcı bulunamadı.
                        </div>
                    </div>
                )}

            {!loading && users.length > 0 && (
                <div className="search-results">

                    {users.map((user) => (
                        <div
                            className="search-user"
                            key={user.id}
                            onClick={() =>
                                handleUserClick(user)
                            }
                        >

                            <div className="search-user-avatar">
                                {user.username
                                    ?.charAt(0)
                                    .toUpperCase()}
                            </div>

                            <div className="search-user-info">

                                <strong>
                                    {user.fullName ||
                                        user.username}
                                </strong>

                                <span>
                                    @{user.username}
                                </span>

                            </div>

                        </div>
                    ))}

                </div>
            )}

        </div>
    );
}

export default SearchBox;
