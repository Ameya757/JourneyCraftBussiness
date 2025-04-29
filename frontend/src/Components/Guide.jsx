import React, { useState, useEffect } from "react";
import axios from "axios";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { LogOut } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../store/authSlice";
import { Card, CardBody, Typography } from "@material-tailwind/react";

const Guide = () => {
  const [guide, setGuide] = useState(null);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const navigate = useNavigate();
  const [isProfileVisible, setIsProfileVisible] = useState(false);
  const dispatch = useDispatch();
  const { userId } = useSelector((state) => state.auth);

  useEffect(() => {
    const fetchGuideProfile = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8081/api/guides/guide/${userId}`
        );
        if (response.status === 200 && response.data) {
          setGuide(response.data);
        }
      } catch (error) {
        console.error("Error fetching guide profile:", error);
      }
    };
    fetchGuideProfile();
  }, [userId]);

  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
    toast.success("Logged out successfully!");
  };

  const validationSchema = Yup.object({
    guidename: Yup.string().required("Guide name is required"),
    experience: Yup.number()
      .min(0, "Experience cannot be negative")
      .required("Experience is required"),
    language: Yup.string().required("Language is required"),
    bio: Yup.string()
      .min(10, "Bio must be at least 10 characters")
      .required("Bio is required"),
    latitude: Yup.number()
      .nullable()
      .min(-90, "Latitude must be between -90 and 90")
      .max(90, "Latitude must be between -90 and 90"),
    longitude: Yup.number()
      .nullable()
      .min(-180, "Longitude must be between -180 and 180")
      .max(180, "Longitude must be between -180 and 180"),
    licenseNumber: Yup.string().required("License number is required"),
    isAvailable: Yup.boolean(),
    phoneNo: Yup.string()
      .matches(/^\d{10}$/, "Phone number must be exactly 10 digits")
      .required("Phone number is required"),
  });

  const handleGuideSubmit = async (values, { setSubmitting }) => {
    try {
      const isUpdating = !!guide;
      const url = isUpdating
        ? `http://localhost:8081/api/guides/update/${userId}`
        : `http://localhost:8081/api/guides/register/guide/${userId}`;

      values.approved = false;
      const method = isUpdating ? axios.put : axios.post;
      const response = await method(url, values);

      if (response.status === 200 || response.status === 201) {
        toast.success(
          isUpdating ? "Guide Updated" : "Guide Under Consideration"
        );
        setGuide({ ...values, approved: false });
        setIsFormVisible(false);
      }
    } catch (error) {
      console.error("Error submitting guide profile:", error);
      toast.error("Failed to submit guide profile");
    }
    setSubmitting(false);
  };

  const initialValues = guide || {
    guidename: "",
    experience: "",
    language: "",
    bio: "",
    latitude: "",
    longitude: "",
    licenseNumber: "",
    isAvailable: true,
    phoneNo: "",
  };

  return (
    <div className="relative flex min-h-screen flex-col justify-center bg-gradient-to-br from-blue-100 to-white px-6 py-12 lg:px-8 overflow-hidden">
      {/* Blurred BG Image */}
      <div
        className="absolute inset-0 bg-[url('/src/assets/guide.avif')] bg-cover bg-center bg-fixed filter blur-lg"
        aria-hidden="true"
      />

      {/* All your existing content, now on top */}
      <div className="relative z-10">
        {/* Header */}
        <div className="sm:mx-auto sm:w-full sm:max-w-md mb-4">
          <h2 className="text-center text-3xl font-serif font-bold text-black shadow-lg bg-gray-200/70 rounded-md p-4">
            {guide
              ? guide.approved
                ? "Update Your Guide Profile"
                : "Your Profile Is Under Consideration"
              : "Create Your Guide Profile"}
          </h2>
        </div>

        {/* Action Buttons */}
        {!isFormVisible && (
          <div className="flex flex-col items-center">
            {guide ? (
              guide.approved ? (
                <>
                  <button
                    onClick={() => setIsFormVisible(true)}
                    className="mb-4 px-6 py-3 bg-blue-600 text-white font-semibold rounded-md hover:bg-blue-500"
                  >
                    Update Profile
                  </button>
                  <button
                    onClick={() => setIsProfileVisible(true)}
                    className="mb-4 px-6 py-3 bg-green-600 text-white font-semibold rounded-md hover:bg-green-500"
                  >
                    View Profile
                  </button>
                  <button
                    onClick={handleLogout}
                    className="flex items-center bg-red-500 text-white px-4 py-2 rounded-lg shadow-md hover:bg-red-600"
                  >
                    <LogOut className="mr-2" /> Logout
                  </button>
                </>
              ) : (
                <>
                  <button
                    onClick={() => setIsProfileVisible(true)}
                    className="mb-4 px-6 py-3 bg-green-600 text-white font-semibold rounded-md hover:bg-green-500"
                  >
                    View Profile
                  </button>
                  <button
                    onClick={handleLogout}
                    className="mt-4 flex items-center bg-red-500 text-white px-4 py-2 rounded-lg shadow-md hover:bg-red-600"
                  >
                    <LogOut className="mr-2" /> Logout
                  </button>
                </>
              )
            ) : (
              <>
                <button
                  onClick={() => setIsFormVisible(true)}
                  className="mb-4 px-6 py-3 bg-blue-600 text-white font-semibold rounded-md hover:bg-blue-500"
                >
                  Create Profile
                </button>
                <button
                  onClick={handleLogout}
                  className="flex items-center bg-red-500 text-white px-4 py-2 rounded-lg shadow-md hover:bg-red-600"
                >
                  <LogOut className="mr-2" /> Logout
                </button>
              </>
            )}
          </div>
        )}

        {/* Form Modal */}
        {isFormVisible && (
          <div className="flex justify-center">
            <div className="relative backdrop-blur-xs bg-white/80 p-8 w-full max-w-4xl rounded-xl shadow-lg">
              <Formik
                enableReinitialize
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={handleGuideSubmit}
              >
                {({ isSubmitting }) => (
                  <Form>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      {Object.keys(validationSchema.fields).map((key) => (
                        <div key={key}>
                          <label
                            htmlFor={key}
                            className="block text-sm font-medium text-gray-700"
                          >
                            {key.charAt(0).toUpperCase() + key.slice(1)}
                          </label>
                          <div className="mt-2 border border-gray-900 rounded-md shadow-sm">
                            <Field
                              id={key}
                              name={key}
                              type={
                                typeof initialValues[key] === "boolean"
                                  ? "checkbox"
                                  : "text"
                              }
                              placeholder={`Enter your ${key}`}
                              className={`${
                                typeof initialValues[key] === "boolean"
                                  ? "h-5 w-5 text-blue-600 ml-3 mt-2"
                                  : "block w-full appearance-none rounded-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-400 focus:z-10 focus:border-blue-500 focus:outline-none focus:ring-blue-500 sm:text-sm"
                              }`}
                            />
                          </div>
                          <ErrorMessage
                            name={key}
                            component="div"
                            className="text-red-600 text-sm"
                          />
                        </div>
                      ))}
                    </div>

                    <div className="mt-6 flex gap-4 justify-between">
                      <button
                        type="submit"
                        disabled={isSubmitting}
                        className="w-full justify-center rounded-md bg-blue-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-blue-500"
                      >
                        {isSubmitting
                          ? guide
                            ? "Updating..."
                            : "Submitting..."
                          : guide
                          ? "Update Guide"
                          : "Add Guide"}
                      </button>
                      <button
                        type="button"
                        onClick={() => setIsFormVisible(false)}
                        className="w-full justify-center rounded-md bg-gray-500 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-gray-600"
                      >
                        Cancel
                      </button>
                    </div>
                  </Form>
                )}
              </Formik>
            </div>
          </div>
        )}
        {isProfileVisible && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm transition-all duration-300">
            <div className="w-full max-w-2xl mx-6 bg-white rounded-3xl shadow-2xl overflow-hidden animate-fadeIn">
              <div className="p-8">
                <h2 className="text-3xl font-bold text-blue-gray-800 mb-8 text-center">
                  🧭 Guide Profile
                </h2>

                {/* 4x2 Table Grid */}
                <div className="grid grid-cols-2 gap-x-6 gap-y-4">
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Name</span>
                    <span className="font-semibold text-gray-800">
                      {guide.guidename}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Experience</span>
                    <span className="font-semibold text-gray-800">
                      {guide.experience} years
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Language</span>
                    <span className="font-semibold text-gray-800">
                      {guide.language}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Phone No</span>
                    <span className="font-semibold text-gray-800">
                      {guide.phoneNo}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Latitude</span>
                    <span className="font-semibold text-gray-800">
                      {guide.latitude}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Longitude</span>
                    <span className="font-semibold text-gray-800">
                      {guide.longitude}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">License No</span>
                    <span className="font-semibold text-gray-800">
                      {guide.licenseNumber}
                    </span>
                  </div>
                  <div className="flex flex-col">
                    <span className="text-sm text-gray-500">Available</span>
                    <span
                      className={`font-semibold ${
                        guide.isAvailable ? "text-green-600" : "text-red-600"
                      }`}
                    >
                      {guide.isAvailable ? "Yes" : "No"}
                    </span>
                  </div>
                </div>

                {/* Close Button */}
                <button
                  onClick={() => setIsProfileVisible(false)}
                  className="mt-10 w-full rounded-lg bg-gradient-to-r from-red-500 to-red-600 px-6 py-3 text-white font-bold hover:from-red-600 hover:to-red-700 transition-all duration-300"
                >
                  Close Profile
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Guide;
