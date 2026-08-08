import { useEffect, useState } from "react";
import { searchUsers } from "../../api/userApi";
import UserCard from "./UserCard";
import "./Follow.css";

function Follow() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);

            const data = await searchUsers("", 0, 20);

            console.log(
                "Kullanıcı detayları:",
                data?.content
            );

            const storedUser = JSON.parse(
                localStorage.getItem("user")
            );

            const currentUserId = storedUser?.id;

            const filteredUsers = (data?.content || []).filter(
                (user) => user.id !== currentUserId
            );

            setUsers(filteredUsers);

        } catch (error) {
            console.error(
                "Kullanıcılar alınamadı:",
                error
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <section className="follow">

            <div className="follow-header">
                <h2>Takip Et</h2>

                <p>
                    Takip edebileceğin kullanıcılar
                </p>
            </div>

            {loading ? (
                <div className="follow-loading">
                    Kullanıcılar yükleniyor...
                </div>
            ) : users.length === 0 ? (
                <div className="follow-empty">
                    Kullanıcı bulunamadı.
                </div>
            ) : (
                <div className="users-list">

                    {users.map((user) => (
                        <UserCard
                            key={user.id}
                            user={user}
                        />
                    ))}

                </div>
            )}

        </section>
    );
}

export default Follow;