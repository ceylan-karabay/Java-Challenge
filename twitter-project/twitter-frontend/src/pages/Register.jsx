import { useState } from "react";
import { register } from "../api/authApi";

function Register({
    onRegisterSuccess,
    switchToLogin,
}) {
    const [formData, setFormData] = useState({
        username: "",
        email: "",
        password: "",
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        setError("");
        setSuccess("");

        const username = formData.username.trim();
        const email = formData.email.trim();
        const password = formData.password;

        if (!username) {
            setError("Kullanıcı adı giriniz.");
            return;
        }

        if (username.length < 3) {
            setError(
                "Kullanıcı adı en az 3 karakter olmalıdır."
            );
            return;
        }

        if (!email) {
            setError("Email giriniz.");
            return;
        }

        if (!password) {
            setError("Şifre giriniz.");
            return;
        }

        if (password.length < 6) {
            setError(
                "Şifre en az 6 karakter olmalıdır."
            );
            return;
        }

        try {
            setLoading(true);

            await register({
                username,
                email,
                password,
            });

            setSuccess(
                "Kayıt başarılı! Giriş yapabilirsiniz."
            );

            setFormData({
                username: "",
                email: "",
                password: "",
            });

            setTimeout(() => {
                onRegisterSuccess();
            }, 1000);

        } catch (error) {
            console.error(
                "Register hatası:",
                error
            );

            const message =
                error.response?.data?.message ||
                error.message ||
                "Kayıt oluşturulamadı.";

            setError(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit}>

            <h2
                style={{
                    color: "white",
                    textAlign: "center",
                    marginBottom: "24px",
                }}
            >
                Kayıt Ol
            </h2>

            {error && (
                <div
                    style={{
                        backgroundColor: "#3d1f1f",
                        color: "#ff6b6b",
                        padding: "12px",
                        borderRadius: "8px",
                        marginBottom: "16px",
                        fontSize: "14px",
                    }}
                >
                    {error}
                </div>
            )}

            {success && (
                <div
                    style={{
                        backgroundColor: "#163d2a",
                        color: "#4ade80",
                        padding: "12px",
                        borderRadius: "8px",
                        marginBottom: "16px",
                        fontSize: "14px",
                    }}
                >
                    {success}
                </div>
            )}

            <input
                type="text"
                name="username"
                placeholder="Kullanıcı adı"
                value={formData.username}
                onChange={handleChange}
                disabled={loading}
                style={inputStyle}
            />

            <input
                type="email"
                name="email"
                placeholder="Email"
                value={formData.email}
                onChange={handleChange}
                disabled={loading}
                style={inputStyle}
            />

            <input
                type="password"
                name="password"
                placeholder="Şifre"
                value={formData.password}
                onChange={handleChange}
                disabled={loading}
                style={inputStyle}
            />

            <button
                type="submit"
                disabled={loading}
                style={buttonStyle}
            >
                {loading
                    ? "Kayıt oluşturuluyor..."
                    : "Kayıt Ol"}
            </button>

            <p
                style={{
                    color: "#8899a6",
                    textAlign: "center",
                    marginTop: "20px",
                    fontSize: "14px",
                }}
            >
                Zaten hesabınız var mı?
            </p>

            <button
                type="button"
                onClick={switchToLogin}
                disabled={loading}
                style={{
                    width: "100%",
                    padding: "12px",
                    backgroundColor: "transparent",
                    color: "#1d9bf0",
                    border: "1px solid #1d9bf0",
                    borderRadius: "24px",
                    cursor: "pointer",
                    fontWeight: "bold",
                }}
            >
                Giriş Yap
            </button>
        </form>
    );
}

const inputStyle = {
    width: "100%",
    boxSizing: "border-box",
    padding: "14px",
    marginBottom: "14px",
    borderRadius: "8px",
    border: "1px solid #38444d",
    backgroundColor: "#253341",
    color: "white",
    fontSize: "15px",
    outline: "none",
};

const buttonStyle = {
    width: "100%",
    padding: "14px",
    border: "none",
    borderRadius: "24px",
    backgroundColor: "#1d9bf0",
    color: "white",
    fontSize: "16px",
    fontWeight: "bold",
    cursor: "pointer",
};

export default Register;