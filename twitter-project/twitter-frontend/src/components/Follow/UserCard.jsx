import { useState } from "react";
import {
    followUser,
    unfollowUser,
} from "../../api/followApi";

import "./UserCard.css";

function UserCard({ user }) {

    const [following, setFollowing] = useState(
        user.following || false
    );

    const [loading, setLoading] = useState(false);

    const handleFollow = async () => {
        if (loading) return;

        try {
            setLoading(true);

            if (following) {
                await unfollowUser(user.id);
                setFollowing(false);
            } else {
                await followUser(user.id);
                setFollowing(true);
            }

        } catch (error) {
            console.error(
                "Takip işlemi başarısız:",
                error
            );
         console.log(
                "Backend hata mesajı:",
                error.response?.data
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="user-card">

            <div className="user-card-avatar">

                {user.profileImageUrl ? (
                    <img
                        src={user.profileImageUrl}
                        alt={user.username}
                    />
                ) : (
                    <span>
                        {user.username
                            ?.charAt(0)
                            .toUpperCase()}
                    </span>
                )}

            </div>

            <div className="user-card-info">

                <strong>
                    {user.fullName || user.username}
                </strong>

                <span>
                    @{user.username}
                </span>

            </div>

            <button
                className={
                    following
                        ? "unfollow-button"
                        : "follow-button"
                }
                onClick={handleFollow}
                disabled={loading}
            >
                {loading
                    ? "..."
                    : following
                        ? "Takibi Bırak"
                        : "Takip Et"}
            </button>

        </div>
    );
}

export default UserCard;