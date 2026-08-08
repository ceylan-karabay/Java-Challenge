import { useState } from "react";
import {
    likeTweet,
    unlikeTweet,
    retweet,
    undoRetweet,
    deleteTweet,
} from "../../api/tweetApi";

import CommentSection from "../Comment/CommentSection";

import "./TweetCard.css";

function TweetCard({ tweet, onDeleteTweet }) {
    const [liked, setLiked] = useState(
        tweet.likedByCurrentUser || false
    );

    const [retweeted, setRetweeted] = useState(
        tweet.retweetedByCurrentUser || false
    );

    const [likeCount, setLikeCount] = useState(
        tweet.likeCount || 0
    );

    const [retweetCount, setRetweetCount] = useState(
        tweet.retweetCount || 0
    );

    const [loadingLike, setLoadingLike] = useState(false);
    const [loadingRetweet, setLoadingRetweet] = useState(false);
    const [loadingDelete, setLoadingDelete] = useState(false);
    const [showComments, setShowComments] = useState(false);

    // =========================
    // LIKE
    // =========================

    const handleLike = async () => {
        if (loadingLike) return;

        try {
            setLoadingLike(true);

            if (liked) {
                await unlikeTweet(tweet.id);

                setLiked(false);
                setLikeCount((prev) => Math.max(0, prev - 1));
            } else {
                await likeTweet(tweet.id);

                setLiked(true);
                setLikeCount((prev) => prev + 1);
            }
        } catch (error) {
            console.error("Like işlemi başarısız:", error);
        } finally {
            setLoadingLike(false);
        }
    };
    // =========================
    // RETWEET
    // =========================

    const handleRetweet = async () => {
        if (loadingRetweet) return;

        try {
            setLoadingRetweet(true);

            if (retweeted) {
                await undoRetweet(tweet.id);

                setRetweeted(false);
                setRetweetCount((prev) =>
                    Math.max(0, prev - 1)
                );
            } else {
                await retweet(tweet.id);

                setRetweeted(true);
                setRetweetCount((prev) => prev + 1);
            }
        } catch (error) {
            console.error("Retweet işlemi başarısız:", error);
        } finally {
            setLoadingRetweet(false);
        }
    };

    // =========================
    // DELETE
    // =========================

   const handleDelete = async () => {
       const confirmed = window.confirm(
           "Bu tweeti silmek istediğinize emin misiniz?"
       );

       if (!confirmed) return;

       try {
           setLoadingDelete(true);

           await deleteTweet(tweet.id);

           if (onDeleteTweet) {
               onDeleteTweet(tweet.id);
           }
       } catch (error) {
           console.error("Tweet silinemedi:", error);
       } finally {
           setLoadingDelete(false);
       }
   };

    const formattedDate = tweet.createdAt
        ? new Date(tweet.createdAt).toLocaleDateString("tr-TR")
        : "";

    return (
        <article className="tweet-card">

            {/* Avatar */}
            <div className="tweet-avatar">
                {tweet.author?.profileImageUrl ? (
                    <img
                        src={tweet.author.profileImageUrl}
                        alt={tweet.author.username}
                    />
                ) : (
                    <span>
                        {tweet.author?.username
                            ?.charAt(0)
                            .toUpperCase()}
                    </span>
                )}
            </div>

            <div className="tweet-main">

                {/* Header */}
                <div className="tweet-header">

                    <div className="tweet-author">
                        <strong>
                            {tweet.author?.fullName ||
                                tweet.author?.username}
                        </strong>

                        <span>
                            @{tweet.author?.username}
                        </span>
                    </div>

                    <time>{formattedDate}</time>
                </div>

                {/* Content */}
                <p className="tweet-text">
                    {tweet.content}
                </p>

                {/* Media */}
                {tweet.mediaUrl && (
                    <img
                        className="tweet-media"
                        src={tweet.mediaUrl}
                        alt="Tweet media"
                    />
                )}

                {/* Actions */}
                <div className="tweet-actions">

                    {/* Comment */}
                    <button
                        className="tweet-action"
                        type="button"
                        onClick={() =>
                            setShowComments((prev) => !prev)
                        }
                    >
                        💬
                        <span>
                            {tweet.replyCount || 0}
                        </span>
                    </button>


                    {/* Retweet */}
                    <button
                        className={`tweet-action ${
                            retweeted ? "retweeted" : ""
                        }`}

                        type="button"
                        onClick={handleRetweet}
                        disabled={loadingRetweet}

                    >

                        🔁
                        <span>{retweetCount}</span>
                    </button>

                    {/* Like */}
                    <button
                        className={`tweet-action ${
                            liked ? "liked" : ""
                        }`}
                        type="button"
                        onClick={handleLike}
                        disabled={loadingLike}
                    >
                        ❤️
                        <span>{likeCount}</span>
                    </button>


                    {/* Delete */}
                    <button
                        className="tweet-action delete-action"
                        type="button"
                        onClick={handleDelete}
                        disabled={loadingDelete}
                    >
                        🗑️
                    </button>

                </div>
                 {showComments && (
                                        <CommentSection tweetId={tweet.id} />
                                    )}
            </div>
        </article>
    );
}

export default TweetCard;