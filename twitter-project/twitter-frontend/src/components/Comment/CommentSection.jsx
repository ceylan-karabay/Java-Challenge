import { useEffect, useState } from "react";
import {
    getCommentsByTweetId,
    createComment,
    deleteComment,
} from "../../api/commentApi";
import "./CommentSection.css";

function CommentSection({ tweetId }) {
    const [comments, setComments] = useState([]);
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);
    const [sending, setSending] = useState(false);

    useEffect(() => {
        fetchComments();
    }, [tweetId]);

    const fetchComments = async () => {
        try {
            setLoading(true);

            const response = await getCommentsByTweetId(
                tweetId,
                0,
                10
            );

            setComments(response?.content || []);
        } catch (error) {
            console.error(
                "Yorumlar alınamadı:",
                error
            );
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const trimmedContent = content.trim();

        if (!trimmedContent || sending) {
            return;
        }

        try {
            setSending(true);

            const newComment = await createComment(
                tweetId,
                trimmedContent
            );

            setComments((prev) => [
                ...prev,
                newComment,
            ]);

            setContent("");
        } catch (error) {
            console.error(
                "Yorum oluşturulamadı:",
                error
            );
        } finally {
            setSending(false);
        }
    };

    const handleDelete = async (commentId) => {
        try {
            await deleteComment(commentId);

            setComments((prev) =>
                prev.filter(
                    (comment) =>
                        comment.id !== commentId
                )
            );
        } catch (error) {
            console.error(
                "Yorum silinemedi:",
                error
            );
        }
    };

    return (
        <div className="comment-section">

            <form
                className="comment-form"
                onSubmit={handleSubmit}
            >
                <input
                    type="text"
                    value={content}
                    onChange={(e) =>
                        setContent(e.target.value)
                    }
                    placeholder="Yorum yaz..."
                    maxLength={280}
                    disabled={sending}
                />

                <button
                    type="submit"
                    disabled={
                        sending ||
                        !content.trim()
                    }
                >
                    {sending
                        ? "..."
                        : "Reply"}
                </button>
            </form>

            {loading ? (
                <div className="comments-loading">
                    Yorumlar yükleniyor...
                </div>
            ) : comments.length === 0 ? (
                <div className="no-comments">
                    Henüz yorum yok.
                </div>
            ) : (
                <div className="comments-list">
                    {comments.map((comment) => (
                        <div
                            className="comment"
                            key={comment.id}
                        >
                            <div className="comment-avatar">
                                {comment.author
                                    ?.username
                                    ?.charAt(0)
                                    .toUpperCase()}
                            </div>

                            <div className="comment-body">
                                <div className="comment-header">
                                    <strong>
                                        {comment.author
                                            ?.fullName ||
                                            comment.author
                                                ?.username}
                                    </strong>

                                    <span>
                                        @
                                        {
                                            comment.author
                                                ?.username
                                        }
                                    </span>
                                </div>

                                <p>
                                    {comment.content}
                                </p>

                                <button
                                    type="button"
                                    onClick={() =>
                                        handleDelete(
                                            comment.id
                                        )
                                    }
                                >
                                    Sil
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default CommentSection;