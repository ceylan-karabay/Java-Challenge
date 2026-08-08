import { useState } from "react";
import { deleteComment } from "../../api/commentApi";
import "./CommentCard.css";

function CommentCard({ comment, onDeleted }) {
  const [deleting, setDeleting] = useState(false);

  const handleDelete = async () => {
    const confirmed = window.confirm(
      "Bu yorumu silmek istediğinize emin misiniz?"
    );

    if (!confirmed) return;

    try {
      setDeleting(true);

      await deleteComment(comment.id);

      onDeleted(comment.id);
    } catch (error) {
      console.error("Yorum silinirken hata:", error);
    } finally {
      setDeleting(false);
    }
  };

  return (
    <article className="comment-card">
      <div className="comment-avatar">
        {comment.author?.profileImageUrl ? (
          <img
            src={comment.author.profileImageUrl}
            alt={comment.author.username}
          />
        ) : (
          <div className="comment-avatar-placeholder">
            {comment.author?.username?.charAt(0).toUpperCase()}
          </div>
        )}
      </div>

      <div className="comment-content">
        <div className="comment-header">
          <div>
            <strong>{comment.author?.fullName}</strong>

            <span>@{comment.author?.username}</span>
          </div>

          <time>
            {new Date(comment.createdAt).toLocaleDateString("tr-TR")}
          </time>
        </div>

        <p>{comment.content}</p>

        <div className="comment-actions">
          <button
            type="button"
            onClick={handleDelete}
            disabled={deleting}
          >
            {deleting ? "Siliniyor..." : "Sil"}
          </button>
        </div>
      </div>
    </article>
  );
}

export default CommentCard;