import { useState } from "react";
import Login from "../../pages/Login";
import Register from "../../pages/Register";

function AuthContainer({ onAuthSuccess }) {
    const [activeTab, setActiveTab] = useState("login");

    return (
        <div
            style={{
                minHeight: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "#15202b",
            }}
        >
            <div
                style={{
                    width: "400px",
                    backgroundColor: "#192734",
                    borderRadius: "16px",
                    overflow: "hidden",
                }}
            >
                <div
                    style={{
                        textAlign: "center",
                        paddingTop: "24px",
                    }}
                >
                    <h1 style={{ color: "white" }}>
                        𝕏 Clone
                    </h1>
                </div>

                <div
                    style={{
                        display: "flex",
                        borderBottom: "1px solid #38444d",
                        marginTop: "16px",
                    }}
                >
                    <button
                        type="button"
                        onClick={() => setActiveTab("login")}
                        style={{
                            flex: 1,
                            padding: "14px",
                            background: "transparent",
                            border: "none",
                            color:
                                activeTab === "login"
                                    ? "#1d9bf0"
                                    : "#8899a6",
                            borderBottom:
                                activeTab === "login"
                                    ? "3px solid #1d9bf0"
                                    : "none",
                            fontWeight: "bold",
                            cursor: "pointer",
                        }}
                    >
                        Giriş Yap
                    </button>

                    <button
                        type="button"
                        onClick={() => setActiveTab("register")}
                        style={{
                            flex: 1,
                            padding: "14px",
                            background: "transparent",
                            border: "none",
                            color:
                                activeTab === "register"
                                    ? "#1d9bf0"
                                    : "#8899a6",
                            borderBottom:
                                activeTab === "register"
                                    ? "3px solid #1d9bf0"
                                    : "none",
                            fontWeight: "bold",
                            cursor: "pointer",
                        }}
                    >
                        Kayıt Ol
                    </button>
                </div>

                <div style={{ padding: "24px" }}>
                    {activeTab === "login" ? (
                        <Login
                            onLoginSuccess={onAuthSuccess}
                            switchToRegister={() =>
                                setActiveTab("register")
                            }
                        />
                    ) : (
                        <Register
                            onRegisterSuccess={() =>
                                setActiveTab("login")
                            }
                            switchToLogin={() =>
                                setActiveTab("login")
                            }
                        />
                    )}
                </div>
            </div>
        </div>
    );
}

export default AuthContainer;