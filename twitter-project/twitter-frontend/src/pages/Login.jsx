import { useState } from "react";
import { login } from "./../api/authApi";
import "./Login.css";

function Login({ onLoginSuccess, switchToRegister }) {
    const [usernameOrEmail, setUsernameOrEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!usernameOrEmail.trim() || !password.trim()) {
            setError("Kullanıcı adı/e-posta ve şifre zorunludur.");
            return;
        }

        try {
            setLoading(true);
            setError("");

            const response = await login({
                usernameOrEmail,
                password,
            });

            console.log("Login response:", response);

            // apiClient response interceptor sayesinde
            // response artık doğrudan authData'dır.
            const authData = response;

            localStorage.setItem(
                "token",
                authData.accessToken
            );

            localStorage.setItem(
                "user",
                JSON.stringify(authData.user)
            );

            if (typeof onLoginSuccess === "function") {
                onLoginSuccess(authData);
            }

        } catch (err) {
            console.error("Login başarısız:", err);

            setError(
                err.response?.data?.message ||
                "Giriş yapılamadı."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">

                <h1>Giriş Yap</h1>

                <p className="login-subtitle">
                    Hesabına giriş yap
                </p>

                {error && (
                    <div className="login-error">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit}>

                    <div className="form-group">
                        <label>
                            Kullanıcı adı veya e-posta
                        </label>

                        <input
                            type="text"
                            value={usernameOrEmail}
                            onChange={(e) =>
                                setUsernameOrEmail(e.target.value)
                            }
                            placeholder="Kullanıcı adı veya e-posta"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label>Şifre</label>

                        <input
                            type="password"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)
                            }
                            placeholder="Şifren"
                            disabled={loading}
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Giriş yapılıyor..."
                            : "Giriş Yap"}
                    </button>

                </form>

                <p className="register-link">
                    Hesabın yok mu?{" "}

                    <button
                        type="button"
                        onClick={switchToRegister}
                    >
                        Kayıt Ol
                    </button>
                </p>

            </div>
        </div>
    );
}

export default Login;