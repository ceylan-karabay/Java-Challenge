import { useState } from "react";
import { createTweet } from "../../api/tweetApi";
import "./TweetComposer.css";

function TweetComposer({ onTweetCreated }) {
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const trimmedContent = content.trim();

        if (!trimmedContent) {
            return;
        }

        try {
            setLoading(true);

            const newTweet = await createTweet(trimmedContent);

            onTweetCreated(newTweet);

            setContent("");
        } catch (error) {
            console.error("Tweet oluşturulamadı:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="tweet-composer">
            <form onSubmit={handleSubmit}>

                <textarea
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Neler oluyor?"
                    maxLength={280}
                    disabled={loading}
                />

                <div className="tweet-composer-footer">

                    <span className="character-count">
                        {content.length}/280
                    </span>

                    <button
                        type="submit"
                        disabled={loading || !content.trim()}
                    >
                        {loading
                            ? "Gönderiliyor..."
                            : "Tweetle"}
                    </button>

                </div>

            </form>
        </div>
    );
}

export default TweetComposer;