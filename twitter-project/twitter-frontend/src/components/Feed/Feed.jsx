import { useEffect, useState } from "react";
import { getFeed } from "../../api/tweetApi";
import TweetComposer from "../Tweet/TweetComposer";
import TweetCard from "../Tweet/TweetCard";
import "./feed.css";

export default function Feed({ currentUser }) {
    const [tweets, setTweets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page] = useState(0);

    useEffect(() => {
        fetchFeed();
    }, [page]);

    const fetchFeed = async () => {
        try {
            setLoading(true);

            const data = await getFeed(page, 10);

            setTweets(data?.content || []);
        } catch (err) {
            console.error("Tweetler yüklenemedi:", err);
        } finally {
            setLoading(false);
        }
    };

    const handleTweetCreated = (newTweet) => {
        setTweets((prev) => [newTweet, ...prev]);
    };

    const handleDeleteTweet = (tweetId) => {
        setTweets((prev) =>
            prev.filter((tweet) => tweet.id !== tweetId)
        );
    };

    return (
        <div className="feed">

            <div className="feed-header">
                <h2>Home</h2>
            </div>

            <TweetComposer
                onTweetCreated={handleTweetCreated}
            />

            {loading ? (
                <div className="loading">
                    Tweetler yükleniyor...
                </div>
            ) : tweets.length === 0 ? (
                <div className="empty-feed">
                    Henüz paylaşılmış tweet bulunmuyor.
                </div>
            ) : (
                <div className="tweet-list">
                    {tweets.map((tweet) => (
                        <TweetCard
                            key={tweet.id}
                            tweet={tweet}
                            currentUser={currentUser}
                            onDeleteTweet={handleDeleteTweet}
                        />
                    ))}
                </div>
            )}

        </div>
    );
}