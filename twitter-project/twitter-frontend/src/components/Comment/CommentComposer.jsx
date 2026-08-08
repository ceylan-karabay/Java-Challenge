import { useState } from "react";
import { createComment } from "../../api/commentApi";
import "./CommentComposer.css";

function CommentComposer({ tweetId, onCommentCreated }) {
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!content.trim()) return;

    try {
      setLoading(true);

      const response = await createComment(tweetId, content);

      onCommentCreated(response.data);

      setContent("");
    } catch (error) {
      console.error("Yorum eklenirken hata:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="comment-composer" onSubmit={handleSubmit}>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="Tweet'e yanıt yaz..."
        maxLength={280}
      />

      <div className="comment-composer-bottom">
        <span>{content.length}/280</span>

        <button
          type="submit"
          disabled={loading || !content.trim()}
        >
          {loading ? "Gönderiliyor..." : "Yanıtla"}
        </button>
      </div>
    </form>
  );
}

export default CommentComposer;