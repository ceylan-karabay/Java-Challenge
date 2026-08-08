import { useEffect, useState } from "react";
import TweetComposer from "../components/Tweet/TweetComposer";
import TweetCard from "../components/Tweet/TweetCard";
import { getFeed } from "../api/tweetApi";

import "./Home.css";

function Home() {
    const [tweets, setTweets] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchTweets = async () => {
        try {
            setLoading(true);

            const data = await getFeed(0, 10);

            setTweets(data?.content || []);
        } catch (error) {
            console.error("Tweetler alınamadı:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTweets();
    }, []);

    const handleTweetCreated = (newTweet) => {
        setTweets((prev) => [
            newTweet,
            ...prev
        ]);
    };

    const handleDeleteTweet = (tweetId) => {
        setTweets((prev) =>
            prev.filter(
                (tweet) => tweet.id !== tweetId
            )
        );
    };

    return (
        <div className="home">

            <div className="home-header">
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
                            onDeleteTweet={handleDeleteTweet}
                        />
                    ))}

                </div>
            )}

        </div>
    );
}

export default Home;