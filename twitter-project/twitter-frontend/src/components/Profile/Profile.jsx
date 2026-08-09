import { useEffect, useState } from "react";
import { getUserById } from "../../api/userApi";
import { getTweetsByUserId } from "../../api/tweetApi";
import TweetCard from "../Tweet/TweetCard";
import "./Profile.css";

function Profile({ userId }) {

      return (
            <section className="profile">
                <h1>PROFILE TEST</h1>
            </section>
        );

    const [user, setUser] = useState(null);
    const [tweets, setTweets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchProfile();
    }, [userId]);

    const fetchProfile = async () => {

        try {

            setLoading(true);

            const storedUser = JSON.parse(
                localStorage.getItem("user")
            );

            /*
             * Eğer URL'de id yoksa
             * kendi profilimizi açıyoruz.
             */
            const profileUserId =
                userId || storedUser?.id;

            console.log(
                "Profil için kullanılacak ID:",
                profileUserId
            );

            if (!profileUserId) {
                console.error(
                    "Kullanıcı ID bulunamadı."
                );
                return;
            }

            /*
             * Kullanıcı profilini getir
             */
            const userResponse =
                await getUserById(profileUserId);

            console.log(
                "Profil kullanıcı:",
                userResponse
            );

            setUser(userResponse);

            /*
             * Kullanıcının tweetlerini getir
             */
            const tweetResponse =
                await getTweetsByUserId(
                    profileUserId,
                    0,
                    10
                );

            console.log(
                "Profil tweetleri:",
                tweetResponse
            );

            setTweets(
                tweetResponse?.content || []
            );

        } catch (error) {

            console.error(
                "Profil yüklenemedi:",
                error
            );

            setUser(null);
            setTweets([]);

        } finally {

            setLoading(false);

        }
    };

    if (loading) {
        return (
            <div className="profile-loading">
                Profil yükleniyor...
            </div>
        );
    }

    if (!user) {
        return (
            <div className="profile-error">
                Profil bilgileri bulunamadı.
            </div>
        );
    }

    return (
        <section className="profile">

            {/* COVER */}

            <div className="profile-cover">

                <div className="profile-avatar">

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

            </div>


            {/* PROFILE INFO */}

            <div className="profile-info">

                <h1>
                    {user.fullName ||
                        user.username}
                </h1>

                <p className="profile-username">
                    @{user.username}
                </p>

                {user.bio && (
                    <p className="profile-bio">
                        {user.bio}
                    </p>
                )}

                <div className="profile-stats">

                    <div>
                        <strong>
                            {user.followersCount || 0}
                        </strong>

                        <span>
                            Followers
                        </span>
                    </div>

                    <div>
                        <strong>
                            {user.followingCount || 0}
                        </strong>

                        <span>
                            Following
                        </span>
                    </div>

                </div>

            </div>


            {/* TABS */}

            <div className="profile-tabs">

                <button className="active">
                    Tweets
                </button>

                <button>
                    Replies
                </button>

                <button>
                    Media
                </button>

                <button>
                    Likes
                </button>

            </div>


            {/* TWEETS */}

            <div className="profile-tweets">

                {tweets.length === 0 ? (

                    <p className="profile-empty">
                        Henüz tweet paylaşılmamış.
                    </p>

                ) : (

                    tweets.map((tweet) => (

                        <TweetCard
                            key={tweet.id}
                            tweet={tweet}
                        />

                    ))

                )}

            </div>

        </section>
    );
}

export default Profile;