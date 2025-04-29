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

const Restaurant = () => {
  const [restaurant, setRestaurant] = useState(null);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const navigate = useNavigate();
  const [isProfileVisible, setIsProfileVisible] = useState(false);
  const dispatch = useDispatch();
  const { userId } = useSelector((state) => state.auth);

  useEffect(() => {
    const fetchRestaurantProfile = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8081/api/restaurants/restaurant/${userId}`
        );
        if (response.status === 200 && response.data) {
          setRestaurant(response.data);
          console.log(
            "Restaurant profile fetched successfully:",
            response.data
          );
        }
      } catch (error) {
        console.error("Error fetching restaurant profile:", error);
      }
    };
    fetchRestaurantProfile();
  }, [userId]);

  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
    toast.success("Logged out successfully!");
  };

  const validationSchema = Yup.object({
    restoName: Yup.string().required("Restaurant name is required"),
    rating: Yup.number()
      .min(0, "Rating cannot be negative")
      .max(5, "Rating cannot be more than 5")
      .required("Enter your google rating"),
    locationLink: Yup.string()
      .url("Enter a valid URL")
      .required("Location link is required"),
    fssaiLicense: Yup.string().required("FSSAI License is required"),
    openTime: Yup.string().required("Opening time is required"),
    closeTime: Yup.string().required("Closing time is required"),
    description: Yup.string()
      .min(10, "Description must be at least 10 characters")
      .required("Description is required"),
    phoneNo: Yup.string()
      .matches(/^[0-9]{10}$/, "Phone number must be exactly 10 digits")
      .required("Phone number is required"),
    averageCost: Yup.number()
      .min(1, "Average cost must be at least 1")
      .required("Average cost is required"),
    foodType: Yup.string()
      .oneOf(["VEG", "NON_VEG", "BOTH"], "Select a valid food type")
      .required("Food type is required"),
  });

  const handleRestaurantSubmit = async (values, { setSubmitting }) => {
    try {
      const isUpdating = !!restaurant;
      const url = isUpdating
        ? `http://localhost:8081/api/restaurants/update/${userId}`
        : `http://localhost:8081/api/restaurants/register/restaurant/${userId}`;

      console.log("Submitting restaurant profile:", values);
      values.approved = false; // Set approved to false for new profiles
      const method = isUpdating ? axios.put : axios.post;
      const response = await method(url, values);

      if (response.status === 200 || response.status === 201) {
        toast.success(
          isUpdating ? "Restaurant Updated" : "Restaurant Under Consideration"
        );
        setRestaurant({ ...values, approved: false });
        setIsFormVisible(false);
      }
    } catch (error) {
      console.error("Error submitting restaurant profile:", error);
      toast.error("Failed to submit restaurant profile");
    }
    setSubmitting(false);
  };

  const initialValues = restaurant
    ? { ...restaurant, foodType: restaurant.foodType || "" }
    : {
        restoName: "",
        rating: "",
        locationLink: "",
        fssaiLicense: "",
        openTime: "",
        closeTime: "",
        description: "",
        phoneNo: "",
        averageCost: "",
        foodType: "",
      };

  return (
    <div className="relative flex min-h-screen flex-col justify-center bg-gradient-to-br from-pink-100 to-white px-6 py-12 lg:px-8 overflow-hidden">
      {/* Blurred background image */}
      <div
        className="absolute inset-0 bg-[url('/src/assets/resto.jpg')] bg-cover bg-center bg-fixed filter blur-sm brightness-50"
        aria-hidden="true"
      />

      {/* Content on top */}
      <div className="relative z-10">
        <div className="sm:mx-auto sm:w-full sm:max-w-md mb-4">
          <h2 className="text-center text-3xl font-serif font-bold text-black shadow-lg bg-gray-200/70 rounded-md p-4">
            {restaurant
              ? restaurant.approved
                ? "Update Your Restaurant Profile"
                : "Your Profile Is Under Consideration"
              : "Create Your Restaurant Profile"}
          </h2>
        </div>

        {/* ACTION AREA */}
        {!isFormVisible && (
          <div className="flex flex-col items-center">
            {restaurant ? (
              restaurant.approved ? (
                <>
                  <button
                    onClick={() => setIsFormVisible(true)}
                    className="mb-4 px-6 py-3 bg-pink-600 text-white font-semibold rounded-md hover:bg-pink-500"
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
                  className="mb-4 px-6 py-3 bg-pink-600 text-white font-semibold rounded-md hover:bg-pink-500"
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

        {/* FORM MODAL */}
        {isFormVisible && (
          <div className="flex justify-center">
            <div className="relative backdrop-blur-xs bg-white/80 p-8 w-full max-w-4xl rounded-xl shadow-lg">
              <Formik
                enableReinitialize
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={handleRestaurantSubmit}
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
                            {key === "foodType" ? (
                              <Field
                                as="select"
                                id={key}
                                name={key}
                                className="block w-full rounded-md border border-gray-300 px-3 py-2 text-gray-900 focus:z-10 focus:border-pink-500 focus:outline-none focus:ring-pink-500 sm:text-sm"
                              >
                                <option disabled value="">
                                  Select Food Type
                                </option>
                                <option value="VEG">Veg</option>
                                <option value="NON_VEG">Non Veg</option>
                                <option value="BOTH">
                                  Both(Veg and Non-Veg)
                                </option>
                              </Field>
                            ) : (
                              <Field
                                id={key}
                                name={key}
                                type={
                                  ["closeTime", "openTime"].includes(key)
                                    ? "time"
                                    : "text"
                                }
                                placeholder={`Enter your ${key}`}
                                className="block w-full rounded-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-400 focus:z-10 focus:border-pink-500 focus:outline-none focus:ring-pink-500 sm:text-sm"
                              />
                            )}
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
                        className="w-full rounded-md bg-pink-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-pink-500"
                      >
                        {isSubmitting
                          ? restaurant
                            ? "Updating..."
                            : "Submitting..."
                          : restaurant
                          ? "Update Restaurant"
                          : "Add Restaurant"}
                      </button>
                      <button
                        type="button"
                        onClick={() => setIsFormVisible(false)}
                        className="w-full rounded-md bg-gray-500 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-gray-600"
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
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm">
            <Card className="w-full max-w-md mx-4 bg-white rounded-2xl shadow-2xl">
              <CardBody className="p-8">
                <Typography
                  variant="h4"
                  color="blue-gray"
                  className="mb-6 text-center"
                >
                  Restaurant Profile
                </Typography>
                <div className="space-y-4">
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Restaurant Name:</span>{" "}
                    {restaurant.restoName}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Description:</span>{" "}
                    {restaurant.description}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Rating:</span>{" "}
                    {restaurant.rating}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Food Type:</span>{" "}
                    {restaurant.foodType}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Location:</span>{" "}
                    <a
                      href={restaurant.locationLink}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-blue-600 underline hover:text-blue-800"
                    >
                      {restaurant.locationLink}
                    </a>
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">FSSAI Licence:</span>{" "}
                    {restaurant.fssaiLicense}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Open Time:</span>{" "}
                    {restaurant.openTime}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Close Time:</span>{" "}
                    {restaurant.closeTime}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Phone No:</span>{" "}
                    {restaurant.phoneNo}
                  </Typography>
                  <Typography variant="h6" className="text-gray-700">
                    <span className="font-semibold">Average Cost:</span>{" "}
                    {restaurant.averageCost}
                  </Typography>
                </div>

                <button
                  onClick={() => setIsProfileVisible(false)}
                  className="mt-8 w-full rounded-md bg-red-500 px-4 py-2 text-white font-semibold hover:bg-red-600"
                >
                  Close
                </button>
              </CardBody>
            </Card>
          </div>
        )}
      </div>
    </div>
  );
};

export default Restaurant;
