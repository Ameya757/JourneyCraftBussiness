import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";
import { motion, AnimatePresence } from 'framer-motion';
import { useDispatch } from 'react-redux';
import { loginSuccess } from '../store/authSlice';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

const formVariants = {
  hidden: { opacity: 0, y: 50 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: 'easeOut' } },
  exit: { opacity: 0, y: -50, transition: { duration: 0.3, ease: 'easeIn' } },
};

const LoginSignup = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [isLogin, setIsLogin] = useState(true);
  const [message, setMessage] = useState("");

  const [otpSent, setOtpSent] = useState(false);
  const [otpVerified, setOtpVerified] = useState(false);
  const [otp, setOtp] = useState("");
  const [emailToVerify, setEmailToVerify] = useState("");

  const validationSchema = (isLogin) =>
    Yup.object().shape({
      username: Yup.string().when([], {
        is: () => !isLogin,
        then: (schema) => schema.required("Username is required"),
        otherwise: (schema) => schema,
      }),
      email: Yup.string()
        .email("Invalid email")
        .required("Email is required"),
      password: Yup.string()
        .min(8, "Minimum 8 characters")
        .matches(
          /^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
          "Must include at least one uppercase letter, one number, and one special character"
        )
        .required("Password is required"),
      role: Yup.string().when([], {
        is: () => !isLogin,
        then: (schema) => schema.required("Role is required"),
        otherwise: (schema) => schema,
      }),
    });

  const handleSendOtp = async (email) => {
    try {
      await axios.post("http://localhost:8081/api/users/send-otp", { email });
      setOtpSent(true);
      setEmailToVerify(email);
      toast.info("OTP sent to email.");
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to send OTP");
    }
  };

  const handleVerifyOtp = async () => {
    try {
      await axios.post("http://localhost:8081/api/users/verify-otp", {
        email: emailToVerify,
        otp: otp,
      });
      setOtpVerified(true);
      toast.success("Email verified successfully.");
    } catch (err) {
      toast.error(err.response?.data?.message || "Invalid OTP");
    }
  };

  const handleSubmit = async (values, { setSubmitting }) => {
    setMessage("");
    try {
      if (isLogin) {
        const res = await axios.post("http://localhost:8081/api/users/login", {
          email: values.email,
          password: values.password,
        });
        dispatch(loginSuccess({
          userId: res.data.id,
          role: res.data.role,
          email: res.data.email,
          username: res.data.username,
        }));
        toast.success(`Login successful! Hello ${res.data.username}!`);
        onLoginSuccess(res.data.role);
      } else {
        if (!otpVerified) {
          toast.error("Please verify your email before signing up.");
          setSubmitting(false);
          return;
        }

        const res = await axios.post("http://localhost:8081/api/users/register", {
          username: values.username,
          email: values.email,
          password: values.password,
          role: values.role,
        });

        toast.success("User Registered Successfully");
        onLoginSuccess(res.data.role);
      }
    } catch (error) {
      setMessage(error.response?.data?.message || "Something went wrong");
    } finally {
      setSubmitting(false);
    }
  };

  const onLoginSuccess = (role) => {
    if (role === "GUIDE") {
      navigate("/guide");
    } else if (role === "RESTAURANT") {
      navigate("/restaurant");
    } else {
      navigate("/");
    }
  };

  const initialValues = {
    username: "",
    email: "",
    password: "",
    role: "",
  };

  return (
    <div className="flex min-h-screen items-center bg-fixed justify-center bg-gray-100 px-6 py-12 bg-[url('/src/assets/background.avif')] bg-cover bg-center">
      <div className="w-full max-w-md space-y-8 bg-gray-200/80 p-8 shadow-xl rounded-2xl overflow-hidden backdrop-blur-xs">
        <AnimatePresence mode="wait">
          <motion.div
            key={isLogin ? "login" : "signup"}
            initial="hidden"
            animate="visible"
            exit="exit"
            variants={formVariants}
          >
            <h2 className="text-center text-3xl font-bold text-gray-900 font-serif font-bold">
              {isLogin ? "Sign In" : "Sign Up"}
            </h2>
            {message && <p className="text-center text-red-500">{message}</p>}

            <Formik
              enableReinitialize
              initialValues={initialValues}
              validationSchema={validationSchema(isLogin)}
              onSubmit={handleSubmit}
            >
              {({ isSubmitting, values }) => (
                <Form className="space-y-2">
                  {!isLogin && (
                    <>
                      <Field
                        name="username"
                        type="text"
                        placeholder="Username"
                        className="w-full px-4 py-2 border rounded-md focus:ring focus:ring-indigo-300"
                      />
                      <ErrorMessage name="username" component="div" className="text-red-600 text-sm" />
                    </>
                  )}

                  <Field
                    name="email"
                    type="email"
                    placeholder="Email"
                    className="w-full px-4 py-2 border rounded-md focus:ring focus:ring-indigo-300"
                  />
                  <ErrorMessage name="email" component="div" className="text-red-600 text-sm" />

                  {!isLogin && (
                    <>
                      <button
                        type="button"
                        className="text-sm text-blue-600 underline"
                        onClick={async () => {
                          await handleSendOtp(values.email);
                        }}
                      >
                        {otpSent ? "Resend OTP" : "Verify Email"}
                      </button>

                      {otpSent && !otpVerified && (
                        <div className="mt-2 space-y-1">
                          <input
                            type="text"
                            placeholder="Enter OTP"
                            value={otp}
                            onChange={(e) => setOtp(e.target.value)}
                            className="w-full px-4 py-2 border rounded-md focus:ring focus:ring-indigo-300"
                          />
                          <button
                            type="button"
                            className="text-sm text-green-600 underline"
                            onClick={handleVerifyOtp}
                          >
                            Submit OTP
                          </button>
                        </div>
                      )}

                      {otpVerified && (
                        <p className="text-sm text-green-600">Email verified ✅</p>
                      )}
                    </>
                  )}

                  <Field
                    name="password"
                    type="password"
                    placeholder="Password"
                    className="w-full px-4 py-2 border rounded-md focus:ring focus:ring-indigo-300"
                  />
                  <ErrorMessage name="password" component="div" className="text-red-600 text-sm" />

                  {!isLogin && (
                    <>
                      <Field
                        as="select"
                        name="role"
                        className="w-full px-4 py-2 border rounded-md focus:ring focus:ring-indigo-300"
                      >
                        <option value="" disabled>Select Role</option>
                        <option value="RESTAURANT">Restaurant</option>
                        <option value="GUIDE">Guide</option>
                      </Field>
                      <ErrorMessage name="role" component="div" className="text-red-600 text-sm" />
                    </>
                  )}

                  <button
                    type="submit"
                    disabled={isSubmitting}
                    className="w-full px-4 py-2 text-white bg-indigo-600 rounded-md hover:bg-indigo-500"
                  >
                    {isSubmitting
                      ? isLogin
                        ? "Signing in..."
                        : "Signing up..."
                      : isLogin
                        ? "Sign In"
                        : "Sign Up"}
                  </button>
                </Form>
              )}
            </Formik>

            <p className="mt-4 text-center text-sm">
              {isLogin ? "Not a member? " : "Already have an account? "}
              <button
                onClick={() => {
                  setIsLogin(!isLogin);
                  setMessage("");
                  setOtp("");
                  setOtpSent(false);
                  setOtpVerified(false);
                  setEmailToVerify("");
                }}
                className="text-indigo-600 hover:underline"
              >
                {isLogin ? "Sign up" : "Sign in"}
              </button>
            </p>
          </motion.div>
        </AnimatePresence>
      </div>
    </div>
  );
};

export default LoginSignup;
